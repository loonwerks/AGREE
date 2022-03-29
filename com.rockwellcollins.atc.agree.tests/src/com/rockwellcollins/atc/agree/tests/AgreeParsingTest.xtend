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
import org.osate.aadl2.DataSubcomponent
import org.osate.aadl2.DefaultAnnexLibrary
import org.osate.aadl2.DefaultAnnexSubclause

import com.rockwellcollins.atc.agree.agree.AgreeContract
import com.rockwellcollins.atc.agree.agree.AgreeContractLibrary
import com.rockwellcollins.atc.agree.agree.AgreeContractSubclause
import com.rockwellcollins.atc.agree.agree.Arg
import com.rockwellcollins.atc.agree.agree.ArraySubExpr
import com.rockwellcollins.atc.agree.agree.AssumeStatement
import com.rockwellcollins.atc.agree.agree.BinaryExpr
import com.rockwellcollins.atc.agree.agree.ConstStatement
import com.rockwellcollins.atc.agree.agree.GuaranteeStatement
import com.rockwellcollins.atc.agree.agree.IntLitExpr
import com.rockwellcollins.atc.agree.agree.NamedElmExpr
import com.rockwellcollins.atc.agree.agree.PrimType
import com.rockwellcollins.atc.agree.agree.SelectionExpr

import com.rockwellcollins.atc.agree.tests.testsupport.TestHelper

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

	@Test
	def void testProjectionExpressionClique() {
		val model = '''
			package Array_Parse
			public
				with Base_Types;
				with Data_Model;
			
				data Alpha
					properties
						Data_Model::Data_Representation => Struct;
				end Alpha;
			
				data implementation Alpha.impl
					subcomponents
						x : data Base_Types::Integer;
						y : data Base_Types::Integer;
				end Alpha.impl;
			
				data Beta
					properties
						Data_Model::Data_Representation => Struct;
				end Beta;
			
				data implementation Beta.impl
					subcomponents
						m : data Alpha.impl[2];
				end Beta.impl;
			
				system Epsilon
					features
						inp : in data port Beta.impl;
					annex agree {**
						-- This parses
						assume A001 "Input" : (inp.m[1]).x = 1;
						-- This should now parse
						assume A002 "Input" : inp.m[1].x = 1;
			
						eq x : Beta.Impl;
						-- This parses
						guarantee G001 "Local rule" : (x.m[1]).x = 1;
						-- This should now parse
						guarantee G002 "Local rule" : x.m[1].x = 1;
			
						type RecS1 = struct { x : int, y : int };
						type RecS2 = struct { m : RecS1[2] };
						eq y : RecS2;
						-- This parses
						guarantee G003 "Local rule" : (y.m[1]).x = 1;
						-- This should now parse
						guarantee G004 "Local rule" : y.m[1].x = 1;
			
					**};
				end Epsilon;
			
			end Array_Parse;
		'''

		val testFileResult = issues = testHelper.testString(model)

		EcoreUtil2.getAllContentsOfType(testFileResult.resource.contents.head, AgreeContract).head => [
			specs.filter(AssumeStatement).filter["A001".equals(name)].head => [
				Assert.assertTrue('A001 expr instanceof BinaryExpr', expr instanceof BinaryExpr)
				(expr as BinaryExpr) => [
					Assert.assertTrue(left instanceof SelectionExpr)
					(left as SelectionExpr) => [
						Assert.assertTrue('A001 left .x field instanceof DataSubcomponent', field instanceof DataSubcomponent)
						Assert.assertEquals('A001 left .x field named "x"', 'x', field.name)
						// assert field is child of Alpha.Impl
						Assert.assertTrue('A001 left (x.m[1]) target instanceof ArraySubExpr', target instanceof ArraySubExpr)
						(target as ArraySubExpr) => [
							Assert.assertTrue('A001 left (x.m[1]) index instanceof IntLitExpr', index instanceof IntLitExpr)
							(index as IntLitExpr) => [
								Assert.assertEquals('A001 left (x.m[1]) index value equals "1"', '1', ^val)
							]
							Assert.assertTrue('A001 left x.m instanceof SelectionExpr', expr instanceof SelectionExpr)
							(expr as SelectionExpr) => [
								Assert.assertTrue('A001 left x.m field instanceof DataSubcomponent', field instanceof DataSubcomponent)
								// Assert field is child of Beta.Impl
								Assert.assertTrue('A001 left x.m target instanceof NamedElmExpr', target instanceof NamedElmExpr)
								(target as NamedElmExpr) => [
									Assert.assertEquals('A001 left x.m target named "inp"', 'inp', elm.name)
									// Assert elm is input port feature
								]
							]
						]
					]
					Assert.assertEquals('A001 op equals "="', '=', op)
					Assert.assertTrue('A001 right instanceof IntLitExpr', right instanceof IntLitExpr)
					(right as IntLitExpr) => [
						Assert.assertEquals('A001 right expr value equals "1"', '1', ^val)
					]
				]
			]
			specs.filter(AssumeStatement).filter["A002".equals(name)].head => [
				Assert.assertTrue('A002 expr instanceof BinaryExpr', expr instanceof BinaryExpr)
				(expr as BinaryExpr) => [
					Assert.assertTrue(left instanceof SelectionExpr)
					(left as SelectionExpr) => [
						Assert.assertTrue('A002 left .x field instanceof DataSubcomponent', field instanceof DataSubcomponent)
						Assert.assertEquals('A002 left .x field named "x"', 'x', field.name)
						// assert field is child of Alpha.Impl
						Assert.assertTrue('A002 left (x.m[1]) target instanceof ArraySubExpr', target instanceof ArraySubExpr)
						(target as ArraySubExpr) => [
							Assert.assertTrue('A002 left (x.m[1]) index instanceof IntLitExpr', index instanceof IntLitExpr)
							(index as IntLitExpr) => [
								Assert.assertEquals('A002 left (x.m[1]) index value equals "1"', '1', ^val)
							]
							Assert.assertTrue('A002 left x.m instanceof SelectionExpr', expr instanceof SelectionExpr)
							(expr as SelectionExpr) => [
								Assert.assertTrue('A002 left x.m field instanceof DataSubcomponent', field instanceof DataSubcomponent)
								// Assert field is child of Beta.Impl
								Assert.assertTrue('A002 left x.m target instanceof NamedElmExpr', target instanceof NamedElmExpr)
								(target as NamedElmExpr) => [
									Assert.assertEquals('A002 left x.m target named "inp"', 'inp', elm.name)
									// Assert elm is input port feature
								]
							]
						]
					]
					Assert.assertEquals('A002 op equals "="', '=', op)
					Assert.assertTrue('A002 right instanceof IntLitExpr', right instanceof IntLitExpr)
					(right as IntLitExpr) => [
						Assert.assertEquals('A002 right expr value equals "1"', '1', ^val)
					]
				]
			]
			specs.filter(GuaranteeStatement).filter["G001".equals(name)].head => [
				Assert.assertTrue('G001 expr instanceof BinaryExpr', expr instanceof BinaryExpr)
				(expr as BinaryExpr) => [
					Assert.assertTrue(left instanceof SelectionExpr)
					(left as SelectionExpr) => [
						Assert.assertTrue('G001 left .x field instanceof DataSubcomponent', field instanceof DataSubcomponent)
						Assert.assertEquals('G001 left .x field named "x"', 'x', field.name)
						// assert field is child of Alpha.Impl
						Assert.assertTrue('G001 left (x.m[1]) target instanceof ArraySubExpr', target instanceof ArraySubExpr)
						(target as ArraySubExpr) => [
							Assert.assertTrue('G001 left (x.m[1]) index instanceof IntLitExpr', index instanceof IntLitExpr)
							(index as IntLitExpr) => [
								Assert.assertEquals('G001 left (x.m[1]) index value equals "1"', '1', ^val)
							]
							Assert.assertTrue('G001 left x.m instanceof SelectionExpr', expr instanceof SelectionExpr)
							(expr as SelectionExpr) => [
								Assert.assertTrue('G001 left x.m field instanceof DataSubcomponent', field instanceof DataSubcomponent)
								// Assert field is child of Beta.Impl
								Assert.assertTrue('G001 left x.m target instanceof NamedElmExpr', target instanceof NamedElmExpr)
								(target as NamedElmExpr) => [
									Assert.assertEquals('G001 left x.m target named "x"', 'x', elm.name)
									// Assert elm is EqStatement
								]
							]
						]
					]
					Assert.assertEquals('G001 op equals "="', '=', op)
					Assert.assertTrue('G001 right instanceof IntLitExpr', right instanceof IntLitExpr)
					(right as IntLitExpr) => [
						Assert.assertEquals('G001 right expr value equals "1"', '1', ^val)
					]
				]
			]
			specs.filter(GuaranteeStatement).filter["G002".equals(name)].head => [
				Assert.assertTrue('G002 expr instanceof BinaryExpr', expr instanceof BinaryExpr)
				(expr as BinaryExpr) => [
					Assert.assertTrue(left instanceof SelectionExpr)
					(left as SelectionExpr) => [
						Assert.assertTrue('G002 left .x field instanceof DataSubcomponent', field instanceof DataSubcomponent)
						Assert.assertEquals('G002 left .x field named "x"', 'x', field.name)
						// assert field is child of Alpha.Impl
						Assert.assertTrue('G002 left (x.m[1]) target instanceof ArraySubExpr', target instanceof ArraySubExpr)
						(target as ArraySubExpr) => [
							Assert.assertTrue('G002 left (x.m[1]) index instanceof IntLitExpr', index instanceof IntLitExpr)
							(index as IntLitExpr) => [
								Assert.assertEquals('G002 left (x.m[1]) index value equals "1"', '1', ^val)
							]
							Assert.assertTrue('G002 left x.m instanceof SelectionExpr', expr instanceof SelectionExpr)
							(expr as SelectionExpr) => [
								Assert.assertTrue('G002 left x.m field instanceof DataSubcomponent', field instanceof DataSubcomponent)
								// Assert field is child of Beta.Impl
								Assert.assertTrue('G002 left x.m target instanceof NamedElmExpr', target instanceof NamedElmExpr)
								(target as NamedElmExpr) => [
									Assert.assertEquals('G002 left x.m target named "x"', 'x', elm.name)
									// Assert elm is EqStatement
								]
							]
						]
					]
					Assert.assertEquals('G002 op equals "="', '=', op)
					Assert.assertTrue('G002 right instanceof IntLitExpr', right instanceof IntLitExpr)
					(right as IntLitExpr) => [
						Assert.assertEquals('G002 right expr value equals "1"', '1', ^val)
					]
				]
			]
			specs.filter(GuaranteeStatement).filter["G003".equals(name)].head => [
				Assert.assertTrue('G001 expr instanceof BinaryExpr', expr instanceof BinaryExpr)
				(expr as BinaryExpr) => [
					Assert.assertTrue(left instanceof SelectionExpr)
					(left as SelectionExpr) => [
						Assert.assertTrue('G003 left .x field instanceof Arg', field instanceof Arg)
						Assert.assertEquals('G003 left .x field named "x"', 'x', field.name)
						// assert field is child of RecS1
						Assert.assertTrue('G003 left (x.m[1]) target instanceof ArraySubExpr', target instanceof ArraySubExpr)
						(target as ArraySubExpr) => [
							Assert.assertTrue('G003 left (x.m[1]) index instanceof IntLitExpr', index instanceof IntLitExpr)
							(index as IntLitExpr) => [
								Assert.assertEquals('G003 left (x.m[1]) index value equals "1"', '1', ^val)
							]
							Assert.assertTrue('G003 left x.m instanceof SelectionExpr', expr instanceof SelectionExpr)
							(expr as SelectionExpr) => [
								Assert.assertTrue('G003 left x.m field instanceof Arg', field instanceof Arg)
								// Assert field is child of RecS2
								Assert.assertTrue('G003 left x.m target instanceof NamedElmExpr', target instanceof NamedElmExpr)
								(target as NamedElmExpr) => [
									Assert.assertEquals('G003 left x.m target named "y"', 'y', elm.name)
									// Assert elm is EqStatement
								]
							]
						]
					]
					Assert.assertEquals('G003 op equals "="', '=', op)
					Assert.assertTrue('G003 right instanceof IntLitExpr', right instanceof IntLitExpr)
					(right as IntLitExpr) => [
						Assert.assertEquals('G003 right expr value equals "1"', '1', ^val)
					]
				]
			]
			specs.filter(GuaranteeStatement).filter["G004".equals(name)].head => [
				Assert.assertTrue('G0041 expr instanceof BinaryExpr', expr instanceof BinaryExpr)
				(expr as BinaryExpr) => [
					Assert.assertTrue(left instanceof SelectionExpr)
					(left as SelectionExpr) => [
						Assert.assertTrue('G004 left .x field instanceof Arg', field instanceof Arg)
						Assert.assertEquals('G004 left .x field named "x"', 'x', field.name)
						// assert field is child of RecS1
						Assert.assertTrue('G004 left (x.m[1]) target instanceof ArraySubExpr', target instanceof ArraySubExpr)
						(target as ArraySubExpr) => [
							Assert.assertTrue('G004 left (x.m[1]) index instanceof IntLitExpr', index instanceof IntLitExpr)
							(index as IntLitExpr) => [
								Assert.assertEquals('G004 left (x.m[1]) index value equals "1"', '1', ^val)
							]
							Assert.assertTrue('G004 left x.m instanceof SelectionExpr', expr instanceof SelectionExpr)
							(expr as SelectionExpr) => [
								Assert.assertTrue('G004 left x.m field instanceof Arg', field instanceof Arg)
								// Assert field is child of RecS2
								Assert.assertTrue('G004 left x.m target instanceof NamedElmExpr', target instanceof NamedElmExpr)
								(target as NamedElmExpr) => [
									Assert.assertEquals('G001 left x.m target named "y"', 'y', elm.name)
									// Assert elm is EqStatement
								]
							]
						]
					]
					Assert.assertEquals('G004 op equals "="', '=', op)
					Assert.assertTrue('G004 right instanceof IntLitExpr', right instanceof IntLitExpr)
					(right as IntLitExpr) => [
						Assert.assertEquals('G004 right expr value equals "1"', '1', ^val)
					]
				]
			]
		]
	}

}
