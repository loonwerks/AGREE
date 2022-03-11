/*
 * Copyright (c) 2021, Collins Aerospace.
 * Developed with the sponsorship of Defense Advanced Research Projects Agency (DARPA).
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this data, 
 * including any software or models in source or binary form, as well as any drawings, specifications, 
 * and documentation (collectively "the Data"), to deal in the Data without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, 
 * and/or sell copies of the Data, and to permit persons to whom the Data is furnished to do so, 
 * subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all copies or 
 * substantial portions of the Data.
 * 
 * THE DATA IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT 
 * LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. 
 * IN NO EVENT SHALL THE AUTHORS, SPONSORS, DEVELOPERS, CONTRIBUTORS, OR COPYRIGHT HOLDERS BE LIABLE 
 * FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, 
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE DATA OR THE USE OR OTHER DEALINGS IN THE DATA.
 */
package com.rockwellcollins.atc.agree.tests.issues

import com.google.inject.Inject

import org.junit.Assert
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith

import org.eclipse.core.runtime.NullProgressMonitor
import org.eclipse.xtext.EcoreUtil2
import org.eclipse.xtext.resource.IEObjectDescription
import org.eclipse.xtext.testing.InjectWith
import org.eclipse.xtext.testing.XtextRunner
import com.itemis.xtext.testing.FluentIssueCollection
import com.itemis.xtext.testing.XtextTest

import org.osate.aadl2.AadlPackage
import org.osate.aadl2.ComponentImplementation
import org.osate.aadl2.DefaultAnnexSubclause
import org.osate.aadl2.PortConnection
import org.osate.aadl2.SystemSubcomponent
import org.osate.aadl2.SystemType
import org.osate.aadl2.instance.SystemInstance
import org.osate.aadl2.instantiation.InstantiateModel

import com.rockwellcollins.atc.agree.AgreeTypeSystem
import com.rockwellcollins.atc.agree.AgreeTypeSystem.Prim
import com.rockwellcollins.atc.agree.agree.AgreeContract
import com.rockwellcollins.atc.agree.agree.AgreeContractSubclause
import com.rockwellcollins.atc.agree.agree.AgreePackage
import com.rockwellcollins.atc.agree.agree.BinaryExpr
import com.rockwellcollins.atc.agree.agree.ConnectionStatement
import com.rockwellcollins.atc.agree.agree.ConstStatement
import com.rockwellcollins.atc.agree.agree.GuaranteeStatement
import com.rockwellcollins.atc.agree.agree.NamedElmExpr
import com.rockwellcollins.atc.agree.agree.SelectionExpr
import com.rockwellcollins.atc.agree.analysis.AgreeException
import com.rockwellcollins.atc.agree.analysis.EphemeralImplementationUtil
import com.rockwellcollins.atc.agree.analysis.ast.AgreeASTBuilder
import com.rockwellcollins.atc.agree.analysis.translation.LustreContractAstBuilder

import com.rockwellcollins.atc.agree.tests.AgreeUiInjectorProvider
import com.rockwellcollins.atc.agree.tests.testsupport.AssertHelper
import com.rockwellcollins.atc.agree.tests.testsupport.TestHelper

import static extension org.osate.testsupport.AssertHelper.assertWarning

@RunWith(XtextRunner)
@InjectWith(AgreeUiInjectorProvider)
class Issue17Test extends XtextTest {

	@Inject
	TestHelper<AadlPackage> testHelper

	@Inject
	extension AssertHelper

	static val alphaBravoModel = '''
		package TestPackage
		public
			with Base_Types;
			
			system Alpha
				features
					outp : out data port Base_Types::Integer;
			end Alpha;
			
			system Bravo
				features
					inp : in data port Base_Types::Integer;
			end Bravo;
			
			system Sys
			end Sys;
			
			system implementation Sys.impl
				subcomponents
					a : system Alpha;
					b : system Bravo;
				connections
					a_to_b : port a.outp -> b.inp;
				annex agree {**
					connection a_to_b : b.inp = a.outp;
				**};
			end Sys.impl;
		end TestPackage;
	'''

	@Ignore("Validation check not yet implemented")
	@Test
	def void testConnectionStatementSubcomponentsInScope() {
		val testFileResult = issues = testHelper.testString(alphaBravoModel)
		val expectedNames = #['a', 'b']

		EcoreUtil2.getAllContentsOfType(testFileResult.resource.contents.head, ConnectionStatement).head => [
			assertScopeFiltered(AgreePackage::eINSTANCE.namedElmExpr_Elm, [ IEObjectDescription x |
				expectedNames.contains(x.name) 
			], expectedNames)
		]
	}

	@Test
	def void testConnectionStatementSubcomponentsReferencesCorrect() {
		val testFileResult = issues = testHelper.testString(alphaBravoModel)
		val issueCollection = new FluentIssueCollection(testFileResult.resource, newArrayList, newArrayList)

		val expectedAadlConnection = EcoreUtil2.getAllContentsOfType(testFileResult.resource.contents.head,
			PortConnection).head
		val expectedSubcomponentA = EcoreUtil2.getAllContentsOfType(testFileResult.resource.contents.head,
			SystemSubcomponent).filter['a'.equals(name)].head
		val expectedSubcomponentB = EcoreUtil2.getAllContentsOfType(testFileResult.resource.contents.head,
			SystemSubcomponent).filter['b'.equals(name)].head
		EcoreUtil2.getAllContentsOfType(testFileResult.resource.contents.head, ConnectionStatement).head => [
			Assert.assertEquals(expectedAadlConnection, conn)
			assertWarning(testFileResult.issues, issueCollection,
				"Connection statements are deprecated and will be removed in a future version of AGREE.")
			Assert.assertTrue(expr instanceof BinaryExpr)
			expr as BinaryExpr => [
				Assert.assertTrue(left instanceof SelectionExpr)
				left as SelectionExpr => [
					Assert.assertTrue(target instanceof NamedElmExpr)
					target as NamedElmExpr => [
						Assert.assertEquals(expectedSubcomponentB, elm)
					]
				]
				Assert.assertTrue(right instanceof SelectionExpr)
				right as SelectionExpr => [
					Assert.assertTrue(target instanceof NamedElmExpr)
					target as NamedElmExpr => [
						Assert.assertEquals(expectedSubcomponentA, elm)
					]
				]
			]
		]

		issueCollection.sizeIs(testFileResult.issues.size)
		assertConstraints(issueCollection)
	}

	static val deltaCharlieModel = '''
		package Scratch
		public
			with Data_Model;
			with Base_Types;
			
			data Delta1 extends Base_Types::Integer
			end Delta1;
			
			data implementation Delta1.impl
			end Delta1.impl;
			
			data Charlie
				properties
					Data_Model::Data_Representation => Struct;
			end Charlie;
			
			data implementation Charlie.impl
				subcomponents
					id : data Delta1.impl;
			end Charlie.impl;
		
			system ScratchSys
				features
					sout : out event data port Charlie.impl;
				annex agree {**
					const id : Delta1.impl = 123456;
					guarantee sout1 "sout id is set" : sout.id = id;
				**};
			end ScratchSys;
		
		end Scratch;
	'''

	@Test
	def void testImplementationsOfPrimitiveTypeCorrectlyTyped() {
		val testFileResult = issues = testHelper.testString(
			com.rockwellcollins.atc.agree.tests.issues.Issue17Test.deltaCharlieModel)
		val systemScratchSys = EcoreUtil2.getAllContentsOfType(testFileResult.resource.contents.head, SystemType).head;

		((systemScratchSys.ownedAnnexSubclauses.head as DefaultAnnexSubclause).
			parsedAnnexSubclause as AgreeContractSubclause).contract as AgreeContract => [
			specs.filter(ConstStatement).head => [
				val declaredType = AgreeTypeSystem.typeDefFromType(type)
				Assert.assertTrue(declaredType instanceof Prim)
				declaredType as Prim => [
					Assert.assertEquals('int', name)
				]
			]
		]
	}

	@Test
	def void testImplementationsOfPrimitiveTypeReferencesCorrectlyTyped() {
		val testFileResult = issues = testHelper.testString(
			com.rockwellcollins.atc.agree.tests.issues.Issue17Test.deltaCharlieModel)
		val systemScratchSys = EcoreUtil2.getAllContentsOfType(testFileResult.resource.contents.head, SystemType).head;

		((systemScratchSys.ownedAnnexSubclauses.head as DefaultAnnexSubclause).
			parsedAnnexSubclause as AgreeContractSubclause).contract as AgreeContract => [
			specs.filter(GuaranteeStatement).head => [
				Assert.assertTrue(expr instanceof BinaryExpr)
				expr as BinaryExpr => [
					val inferredType = AgreeTypeSystem.infer(right)
					Assert.assertTrue(inferredType instanceof Prim)
					inferredType as Prim => [
						Assert.assertEquals('int', name)
					]
				]
			]
		]
	}

	@Test
	def void testImplementationsOfPrimitiveTypesNestedReferencesCorrectlyTyped() {
		val testFileResult = issues = testHelper.testString(
			com.rockwellcollins.atc.agree.tests.issues.Issue17Test.deltaCharlieModel)

		val systemScratchSys = EcoreUtil2.getAllContentsOfType(testFileResult.resource.contents.head, SystemType).head;

		((systemScratchSys.ownedAnnexSubclauses.head as DefaultAnnexSubclause).
			parsedAnnexSubclause as AgreeContractSubclause).contract as AgreeContract => [
			specs.filter(GuaranteeStatement).head => [
				Assert.assertTrue(expr instanceof BinaryExpr)
				expr as BinaryExpr => [
					Assert.assertTrue(left instanceof SelectionExpr)
					val inferredType = AgreeTypeSystem.infer(left)
					Assert.assertTrue(inferredType instanceof Prim)
					inferredType as Prim => [
						Assert.assertEquals('int', name)
					]
				]
			]
		]
	}

	@Ignore("Validation check not yet implemented")
	@Test
	def void testImplementationsOfPrimitiveTypesTranslatedCorrectly() {
		val testFileResult = issues = testHelper.testString(
			com.rockwellcollins.atc.agree.tests.issues.Issue17Test.deltaCharlieModel)

		val systemScratchSys = EcoreUtil2.getAllContentsOfType(testFileResult.resource.contents.head, SystemType).head;

		val isMonolithic = false
		val implUtil = new EphemeralImplementationUtil(new NullProgressMonitor())
		val systemScratchSysEphemeralImpl = implUtil.generateEphemeralCompImplFromType(systemScratchSys)
		val systemScratchSysInstance = getSysInstance(systemScratchSysEphemeralImpl, implUtil)

		val agreeProgram = new AgreeASTBuilder().getAgreeProgram(systemScratchSysInstance, isMonolithic)
		val lustreProgram = LustreContractAstBuilder.getContractLustreProgram(agreeProgram)
	}

	private def SystemInstance getSysInstance(ComponentImplementation ci, EphemeralImplementationUtil implUtil) {
		try {
			return InstantiateModel.buildInstanceModelFile(ci);
		} catch (Exception e) {
			throw new AgreeException("Error Instantiating model");
		}
	}



}
