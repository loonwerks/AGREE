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
package com.rockwellcollins.atc.agree.tests

import com.google.inject.Inject

import org.junit.Test
import org.junit.runner.RunWith

import org.eclipse.xtext.testing.InjectWith
import org.eclipse.xtext.testing.XtextRunner

import com.itemis.xtext.testing.FluentIssueCollection
import com.itemis.xtext.testing.XtextTest

import org.osate.aadl2.AadlPackage
import org.osate.aadl2.DefaultAnnexLibrary
import org.osate.aadl2.DefaultAnnexSubclause

import com.rockwellcollins.atc.agree.agree.AgreeContractLibrary
import com.rockwellcollins.atc.agree.agree.AgreeContractSubclause
import com.rockwellcollins.atc.agree.agree.AgreeContract
import com.rockwellcollins.atc.agree.agree.AssumeStatement
import com.rockwellcollins.atc.agree.agree.BinaryExpr
import com.rockwellcollins.atc.agree.agree.ConstStatement
import com.rockwellcollins.atc.agree.tests.testsupport.TestHelper

import static extension org.junit.Assert.assertEquals
import static extension org.osate.testsupport.AssertHelper.assertError

@RunWith(XtextRunner)
@InjectWith(AgreeInjectorProvider)
class AgreeValidatorTest extends XtextTest {

	@Inject
	TestHelper<AadlPackage> testHelper

//	@Inject
//	extension ValidationTestHelper validationHelper

	static val agreeConstantsModel = '''
		package test1
		public
		
		with Base_Types;
		
		annex agree {**
			const TEST_VAL: real = 10; -- should throw an error;
		**};
		
		system A
			features
				Input_Val: in data port Base_Types::Float;
				Output_Val: out data port Base_Types::Float;
			annex agree {**
				assume "" : Input_Val < 20; -- should throw an error
			**};
		end A;
		
		system S
			features
				Input_Val: in data port Base_Types::Float;
				Output_Val: out data port Base_Types::Float;
			annex agree {**
				assume "" : Input_Val < TEST_VAL;
			**};
		end S;
		
		system implementation S.impl
			subcomponents
				A: system A;
			connections
				c1_a: port Input_Val -> A.Input_Val; 
				c2_a: port A.Output_Val -> Output_Val;
		end S.impl;
		
		end test1;
	'''

	@Test()
	def void testConstStatementTypeMismatch() {
		val testResult = testHelper.testString(agreeConstantsModel)
		val issueCollection = new FluentIssueCollection(testResult.resource, newArrayList, newArrayList)
		val aadlPackage = testResult.resource.contents.head as AadlPackage

		aadlPackage.publicSection.ownedAnnexLibraries.head as DefaultAnnexLibrary => [
			parsedAnnexLibrary as AgreeContractLibrary => [
				contract as AgreeContract => [
					specs.head as ConstStatement => [
						it.assertError(testResult.issues, issueCollection, "The assumed type of constant statement 'TEST_VAL' is 'RealTypeDef' but the actual type is 'IntTypeDef'")
					]
				]
			]
		]
		aadlPackage.publicSection.ownedClassifiers.filter["A".equalsIgnoreCase(name)].head => [
			ownedAnnexSubclauses.head as DefaultAnnexSubclause => [
				parsedAnnexSubclause as AgreeContractSubclause => [
					contract as AgreeContract => [
						specs.head as AssumeStatement => [
							expr as BinaryExpr => [
								it.assertError(testResult.issues, issueCollection, "left and right sides of binary expression '<' are of type 'real' and 'int', but must be of the same type")
							]
						]
					]
				]
			]
		]
		//aadlPackage.assertError(AgreePackage.Literals.CONST_STATEMENT, null, "The assumed type of constant statement 'TEST_VAL' is 'RealTypeDef' but the actual type is 'IntTypeDef'")
		//aadlPackage.assertError(AgreePackage.Literals.BINARY_EXPR, null, "left and right sides of binary expression '<' are of type 'real' and 'int', but must be of the same type")
		issueCollection.sizeIs(testResult.issues.size)
		assertEquals(testResult.issues.toSet, issueCollection.issues.toSet
		)
	}

}