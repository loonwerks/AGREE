package com.rockwellcollins.atc.agree.tests.issues

import com.google.inject.Inject

import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

import org.eclipse.xtext.EcoreUtil2
import org.eclipse.xtext.testing.InjectWith
import org.eclipse.xtext.testing.XtextRunner
import com.itemis.xtext.testing.XtextTest

import org.osate.aadl2.AadlPackage
import org.osate.aadl2.ComponentImplementation
import org.osate.aadl2.instantiation.InstantiateModel

import com.rockwellcollins.atc.agree.analysis.ast.AgreeASTBuilder

import com.rockwellcollins.atc.agree.tests.AgreeUiInjectorProvider
import com.rockwellcollins.atc.agree.tests.testsupport.TestHelper
import com.rockwellcollins.atc.agree.analysis.ast.AgreeAADLConnection

@RunWith(XtextRunner)
@InjectWith(AgreeUiInjectorProvider)
class Issue24Test extends XtextTest {

	@Inject
	TestHelper<AadlPackage> testHelper

	// @Inject
	// extension AssertHelper

	static val issue24Test1Model = '''
		package test1
		public
				with Base_Types;
		
			system A
				features
					Input: in data port; -- no type specified
					Output: out data port; -- no type specified
			end A;
		
			system S
				features
					Input: in data port; -- no type specified
					Output: out data port; -- no type specified
			end S;
		
			system implementation S.impl
				subcomponents
					A: system A;
				connections
					c1: port Input -> A.Input;
					c2: port A.Output -> Output;	
			end S.impl;
		
			system A_agree extends A
				features
					Input: refined to in data port Base_Types::Integer {Classifier_Substitution_Rule => Type_Extension;}; -- refined type
					Output: refined to out data port Base_Types::Integer {Classifier_Substitution_Rule => Type_Extension;}; -- refined type
				annex agree {**
					assume "" : Input < 20;
				**};
			end A_agree;
		
			system S_agree extends S
				features
					Input: refined to in data port Base_Types::Integer {Classifier_Substitution_Rule => Type_Extension;}; -- refined type
					Output: refined to out data port Base_Types::Integer {Classifier_Substitution_Rule => Type_Extension;}; -- refined type
				annex agree {**
					assume "" : Input < 10;
				**};
			end S_agree;
		
			-- this breaks
			-- from Junaid "conflict between two Agree annexes introduced into a single component through inheritance of AADL types 
			-- (specifically, via extending a component and a refinement of its features)"
			system implementation S_agree.impl extends S.impl
				subcomponents
					A: refined to system A_agree {Classifier_Substitution_Rule => Type_Extension;};		
			end S_agree.impl;
		
			-- this does not
			system implementation S_agree.impl2
				subcomponents
					A: system A_agree;
				connections
					c1: port Input -> A.Input;
					c2: port A.Output -> Output;
			end S_agree.impl2;
		
			system S_agree_new
				features
					Input: in data port Base_Types::Integer;
					Output: out data port Base_Types::Integer;
				annex agree {**
					assume "" : Input < 10;
				**};
			end S_agree_new;
		
			-- this does not
			system implementation S_agree_new.impl
				subcomponents
					A: system A_agree;
				connections
					c1: port Input -> A.Input;
					c2: port A.Output -> Output;
			end S_agree_new.impl;
		
		end test1;
	'''

	@Test()
	def void issue24test1() {
		val testFileResult = issues = testHelper.testString(com.rockwellcollins.atc.agree.tests.issues.Issue24Test.issue24Test1Model)
		EcoreUtil2.getAllContentsOfType(testFileResult.resource.contents.head, ComponentImplementation).filter( it |
			!"S.impl".equals(it.name)
		).forEach[testComponentImplementation]
	}

	static val issue24Test2Model = '''
		package test2
		public
		
			with Base_Types;
		
			system A
				features
					Input: in data port Base_Types::Integer; -- type specified
				Output: out data port Base_Types::Integer; -- type specified
			end A;
		
			system S
				features
					Input: in data port Base_Types::Integer; -- type specified
					Output: out data port Base_Types::Integer; -- type specified
			end S;
		
			system implementation S.impl
				subcomponents
					A: system A;
				connections
					c1: port Input -> A.Input;
					c2: port A.Output -> Output;
			end S.impl;
		
			system A_agree extends A
				annex agree {**
					assume "" : Input < 20;
				**};
			end A_agree;
		
			system S_agree extends S
				annex agree {**
					assume "" : Input < 10;
				**};
			end S_agree;
		
			-- this breaks as well
			system implementation S_agree.impl extends S.impl
				subcomponents
					A: refined to system A_agree {Classifier_Substitution_Rule => Type_Extension;};	-- refined just system		
			end S_agree.impl;
		
			-- this does not
			system implementation S_agree.impl2
				subcomponents
					A: system A_agree;
				connections
					c1: port Input -> A.Input;
					c2: port A.Output -> Output;
			end S_agree.impl2;
		
			system S_agree_new
				features
					Input: in data port Base_Types::Integer;
					Output: out data port Base_Types::Integer;
				annex agree {**
					assume "" : Input < 10;
				**};
			end S_agree_new;
		
			-- this does not
			system implementation S_agree_new.impl
				subcomponents
					A: system A_agree;
				connections
					c1: port Input -> A.Input;
					c2: port A.Output -> Output;
			end S_agree_new.impl;
		
		end test2;
	'''

	@Test()
	def void issue24test2() {
		val testFileResult = issues = testHelper.testString(com.rockwellcollins.atc.agree.tests.issues.Issue24Test.issue24Test2Model)
		EcoreUtil2.getAllContentsOfType(testFileResult.resource.contents.head, ComponentImplementation).filter( it |
			!"S.impl".equals(it.name)
		).forEach[testComponentImplementation]
	}

	static val issue24Test3Model = '''
		package test3
		public
		
			with Base_Types;
		
			system A
				features
					Input: in data port; -- no type specified
					Output: out data port; -- no type specified
			end A;
		
			system S
				features
					Input: in data port; -- no type specified
					Output: out data port; -- no type specified
			end S;
		
			system implementation S.impl
				subcomponents
					A: system A;
				connections
					c1: port Input -> A.Input;
					c2: port A.Output -> Output;
			end S.impl;
		
			data packet
			end packet;
		
			data implementation packet.impl
				subcomponents
					integer1: data Base_Types::Integer;
					integer2: data Base_Types::Integer;
			end packet.impl;
		
			system A_agree extends A
				features
					Input: refined to in data port packet.impl {Classifier_Substitution_Rule => Type_Extension;}; -- refined type
					Output: refined to out data port packet.impl {Classifier_Substitution_Rule => Type_Extension;}; -- refined type
				annex agree {**
					assume "" : Input.integer1 < 20;
				**};
			end A_agree;
		
			system S_agree extends S
				features
					Input: refined to in data port packet.impl {Classifier_Substitution_Rule => Type_Extension;}; -- refined type
					Output: refined to out data port packet.impl {Classifier_Substitution_Rule => Type_Extension;}; -- refined type
				annex agree {**
					assume "" : Input.integer1 < 10;
				**};
			end S_agree;
		
			-- this breaks
			-- from Junaid "conflict between two Agree annexes introduced into a single component through inheritance of AADL types 
			-- (specifically, via extending a component and a refinement of its features)"
			system implementation S_agree.impl extends S.impl
				subcomponents
					A: refined to system A_agree {Classifier_Substitution_Rule => Type_Extension;};			
			end S_agree.impl;
		
			-- this does not
			system implementation S_agree.impl2
				subcomponents
					A: system A_agree;
				connections
					c1: port Input -> A.Input;
					c2: port A.Output -> Output;
			end S_agree.impl2;
		
			system S_agree_new
				features
					Input: in data port packet.impl;
					Output: out data port packet.impl;
				annex agree {**
					assume "" : Input.integer1 < 10;
				**};
			end S_agree_new;
		
			-- this does not
			system implementation S_agree_new.impl
				subcomponents
					A: system A_agree;
				connections
					c1: port Input -> A.Input;
					c2: port A.Output -> Output;
			end S_agree_new.impl;
		
		end test3;
	'''

	@Test()
	def void issue24test3() {
		val testFileResult = issues = testHelper.testString(com.rockwellcollins.atc.agree.tests.issues.Issue24Test.issue24Test3Model)
		EcoreUtil2.getAllContentsOfType(testFileResult.resource.contents.head, ComponentImplementation).filter( it |
			!("S.impl".equals(it.name) || "packet.impl".equals(it.name))
		).forEach[testComponentImplementation]
	}

	static val issue24Test4Model = '''
		package test4
		public
		
			with Base_Types;
		
			data packet
			end packet;
		
			data implementation packet.impl
				subcomponents
					integer1: data Base_Types::Integer;
					integer2: data Base_Types::Integer;
			end packet.impl;
		
			system A
				features
					Input: in data port packet.impl; -- type specified
					Output: out data port packet.impl; -- type specified
			end A;
		
			system S
				features
					Input: in data port packet.impl; -- type specified
					Output: out data port packet.impl; -- type specified
			end S;
		
			system implementation S.impl
				subcomponents
					A: system A;
				connections
					c1: port Input -> A.Input;
					c2: port A.Output -> Output;
			end S.impl;
		
			system A_agree extends A
				annex agree {**
					assume "" : Input.integer1 < 20;
				**};
			end A_agree;
		
			system S_agree extends S
				annex agree {**
					assume "" : Input.integer1 < 10;
				**};
			end S_agree;
		
			-- this breaks as well
			system implementation S_agree.impl extends S.impl
				subcomponents
					A: refined to system A_agree {Classifier_Substitution_Rule => Type_Extension;};	-- refined just system		
			end S_agree.impl;
		
		-- this does not
			system implementation S_agree.impl2
				subcomponents
					A: system A_agree;
				connections
					c1: port Input -> A.Input;
					c2: port A.Output -> Output;
			end S_agree.impl2;
		
			system S_agree_new
				features
					Input: in data port packet.impl;
					Output: out data port packet.impl;
				annex agree {**
					assume "" : Input.integer1 < 10;
				**};
			end S_agree_new;
		
			-- this does not
			system implementation S_agree_new.impl
				subcomponents
					A: system A_agree;
				connections
					c1: port Input -> A.Input;
					c2: port A.Output -> Output;
			end S_agree_new.impl;
		
		end test4;
	'''

	@Test()
	def void issue24test4() {
		val testFileResult = issues = testHelper.testString(com.rockwellcollins.atc.agree.tests.issues.Issue24Test.issue24Test4Model)
		EcoreUtil2.getAllContentsOfType(testFileResult.resource.contents.head, ComponentImplementation).filter( it |
			!("S.impl".equals(it.name) || "packet.impl".equals(it.name))
		).forEach[testComponentImplementation]
	}

	private def void testComponentImplementation(ComponentImplementation componentImplementation) {
		// val componentInstance = InstantiateModel.buildInstanceModelFile(componentImplementation)
		val componentInstance = InstantiateModel.instantiate(componentImplementation)
		System.out.println("Test " + componentInstance.name)
		val agreeProgram = new AgreeASTBuilder().getAgreeProgram(componentInstance, false)
		val nodesByName = agreeProgram.agreeNodes.groupBy[it.id]
		for (e : nodesByName.entrySet) {
			Assert.assertEquals(1, e.value.size)
		}
		for (agreeNode : agreeProgram.agreeNodes) {
			agreeNode.connections.forEach[Assert.assertTrue(it instanceof AgreeAADLConnection)]
			val connectionsByReference = agreeNode.connections.filter(AgreeAADLConnection).groupBy[it.reference]
			for (e : connectionsByReference.entrySet) {
				Assert.assertEquals(1, e.value.size)
			}
		}
	}

}
