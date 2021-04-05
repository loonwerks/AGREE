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
import org.eclipse.xtext.testing.validation.ValidationTestHelper
import com.itemis.xtext.testing.XtextTest

import org.osate.aadl2.AadlPackage

import com.rockwellcollins.atc.agree.agree.AgreePackage

import com.rockwellcollins.atc.agree.tests.testsupport.TestHelper

@RunWith(XtextRunner)
@InjectWith(AgreeInjectorProvider)
class AgreeValidatorTest extends XtextTest {

	@Inject
	TestHelper<AadlPackage> testHelper

	@Inject
	extension ValidationTestHelper validationHelper

	static val agreeConstantsModel = '''
		package test1
		public
		
		with Base_Types;
		
		annex agree {**
			const TEST: real = 10; -- should throw an error;
		**};
		
		system A
			features
				Input: in data port Base_Types::Float;
				Output: out data port Base_Types::Float;
			annex agree {**
				assume "" : Input < 20;
			**};
		end A;
		
		system S
			features
				Input: in data port Base_Types::Float;
				Output: out data port Base_Types::Float;
			annex agree {**
				assume "" : Input < TEST; -- should throw an error
			**};
		end S;
		
		system implementation S.impl
			subcomponents
				A: system A;
			connections
				c1_a: port Input -> A.Input; 
				c2_a: port A.Output -> Output;
		end S.impl;
		
		end test1;
	'''

	@Test()
	def testConstStatementTypeMismatch() {
		val testResult = testHelper.testString(agreeConstantsModel)
		val aadlPackage = testResult.resource.contents.head as AadlPackage

		aadlPackage.assertError(AgreePackage.Literals.CONST_STATEMENT, null, "The assumed type of constant statement 'TEST' is 'RealTypeDef' but the actual type is 'IntTypeDef'")
		aadlPackage.assertError(AgreePackage.Literals.BINARY_EXPR, null, "left and right sides of binary expression '<' are of type 'real' and 'int', but must be of the same type")
	}

}