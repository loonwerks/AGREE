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