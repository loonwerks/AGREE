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

import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

import org.eclipse.xtext.EcoreUtil2
import org.eclipse.xtext.testing.InjectWith
import org.eclipse.xtext.testing.XtextRunner
import com.itemis.xtext.testing.XtextTest

import org.osate.aadl2.AadlPackage
import org.osate.aadl2.DefaultAnnexLibrary
import org.osate.aadl2.DefaultAnnexSubclause

import com.rockwellcollins.atc.agree.agree.AgreeContract
import com.rockwellcollins.atc.agree.agree.AgreeContractLibrary
import com.rockwellcollins.atc.agree.agree.AgreeContractSubclause
import com.rockwellcollins.atc.agree.agree.ConstStatement
import com.rockwellcollins.atc.agree.agree.IntLitExpr

import com.rockwellcollins.atc.agree.tests.testsupport.TestHelper
import com.rockwellcollins.atc.agree.agree.PrimType

@RunWith(XtextRunner)
@InjectWith(AgreeInjectorProvider)
class AgreeParsingTest extends XtextTest {

	@Inject
	TestHelper<AadlPackage> testHelper

	@Test
	def void testAnnexLibrary() {
		val model = '''
			package TestPackage
			public
			  annex agree {**
			    const x : int = 2;
			  **};
			end TestPackage;
		'''

		val testFileResult = issues = testHelper.testString(model)

		testFileResult.resource.contents.head as AadlPackage => [
			Assert.assertEquals(1, publicSection.ownedAnnexLibraries.size)
			publicSection.ownedAnnexLibraries.head as DefaultAnnexLibrary => [
				Assert.assertTrue(parsedAnnexLibrary instanceof AgreeContractLibrary)
				parsedAnnexLibrary as AgreeContractLibrary => [
					Assert.assertNotNull(contract)
					Assert.assertTrue(contract instanceof AgreeContract)
					contract as AgreeContract => [
						Assert.assertEquals(1, specs.size)
					]
				]
			]
		]
	}

	@Test
	def void testAnnexSubclause() {
		val model = '''
			package TestPackage
			public
			  system sys
			    -- subcomponents
			    --   none;
			    -- properties
			    --   none;
			    annex agree {**
			      const x : int = 2;
			    **};
			  end sys;
			end TestPackage;
		'''

		val testFileResult = issues = testHelper.testString(model)

		testFileResult.resource.contents.head as AadlPackage => [
			publicSection.ownedClassifiers.head => [
				Assert.assertEquals("sys", name)
				Assert.assertEquals(1, ownedAnnexSubclauses.size)
				ownedAnnexSubclauses.head as DefaultAnnexSubclause => [
					Assert.assertTrue(parsedAnnexSubclause instanceof AgreeContractSubclause)
					parsedAnnexSubclause as AgreeContractSubclause => [
						Assert.assertNotNull(contract)
						Assert.assertTrue(contract instanceof AgreeContract)
						contract as AgreeContract => [
							Assert.assertEquals(1, specs.size)
						]
					]
				]
			]
		]
	}

	@Test
	def void testConstStatement() {
		val model = '''
			package TestPackage
			public
			  annex agree {**
			    const x : int = 2;
			  **};
			end TestPackage;
		'''

		val testFileResult = issues = testHelper.testString(model)

		EcoreUtil2.getAllContentsOfType(testFileResult.resource.contents.head, AgreeContract).head => [
			Assert.assertTrue(specs.head instanceof ConstStatement)
			specs.head as ConstStatement => [
				Assert.assertEquals('x', name)
				Assert.assertTrue(type instanceof PrimType)
				type as PrimType => [
					Assert.assertEquals('int', name)
					Assert.assertNull(lowNeg)
					Assert.assertNull(rangeLow)
					Assert.assertNull(highNeg)
					Assert.assertNull(rangeHigh)
				]
				Assert.assertTrue(expr instanceof IntLitExpr)
				expr as IntLitExpr => [
					Assert.assertEquals('2', ^val)
				]
			]
		]
	}

}
