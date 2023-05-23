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
class Issue31Test extends XtextTest {

	@Inject
	TestHelper<AadlPackage> testHelper

	// @Inject
	// extension AssertHelper
	static val issue31LibPackage = '''
		package Types_Constants
			public
			with Base_Types;
			with Data_Model;
		
			-------------------------------------
			-- Data Types
			-------------------------------------
		
			data Response
				properties
					Data_Model::Data_Representation => Enum;
					Data_Model::Enumerators => ("Agree", "Disagree", "Neutral");
			end Response;
		
			data H2I
			end H2I;
		
			data implementation H2I.impl
				subcomponents
					Sensor1_Unreliable_Response: data Response;
					Sensor2_Unreliable_Response: data Response;
					Sensor3_Unreliable_Response: data Response;
			end H2I.impl;
		
			data I2H
			end I2H;
		
			data implementation I2H.impl
				subcomponents
					Sensor1_Reliable: data Base_Types::Boolean;
					Sensor2_Reliable: data Base_Types::Boolean;
					Sensor3_Reliable: data Base_Types::Boolean;
					Position: data Position.impl;
			end I2H.impl;
		
			data Position
			end Position;
		
			data implementation Position.impl
				subcomponents
					X: data Base_Types::Float;
					Y: data Base_Types::Float;
					Z: data Base_Types::Float;
			end Position.impl;
		
			data Sensor_Data
			end Sensor_Data;
		
			data implementation Sensor_Data.impl
				subcomponents
					Position: data Position.impl;
			end Sensor_Data.impl;
		
		end Types_Constants;
	'''

	static val issue31ModelPackage = '''
		package Human_IAS_Team
		public
			with Types_Constants;
			renames Types_Constants::all;
		
			-------------------------------------
			-- Human
			-------------------------------------
		
			system Human
				features
					InputFromIAS : in data port I2H.impl;
					InputFromSensor1 : in data port Sensor_Data.impl;
					InputFromSensor2 : in data port Sensor_Data.impl;
					InputFromSensor3 : in data port Sensor_Data.impl;
					Output : out data port H2I.impl;
				annex agree {**
		
					guarantee G1 "Respond to message that Sensor1 is unreliable":
						prev(not InputFromIAS.Sensor1_Reliable, false) <=> ((Output.Sensor1_Unreliable_Response = enum(Response, Agree)) or (Output.Sensor1_Unreliable_Response = enum(Response, Disagree))); 
		
					guarantee G2 "Respond to message that Sensor2 is unreliable":
						prev(not InputFromIAS.Sensor2_Reliable, false) <=> ((Output.Sensor2_Unreliable_Response = enum(Response, Agree)) or (Output.Sensor2_Unreliable_Response = enum(Response, Disagree)));
		
					guarantee G3 "Respond to message that Sensor3 is unreliable":
						prev(not InputFromIAS.Sensor2_Reliable, false) <=> ((Output.Sensor3_Unreliable_Response = enum(Response, Agree)) or (Output.Sensor3_Unreliable_Response = enum(Response, Disagree)));		
		
				**};
			end Human;
		
			-------------------------------------
			-- IAS
			-------------------------------------
		
			system IAS
				features
					InputFromHuman : in data port H2I.impl;
					InputFromSensor1 : in data port Sensor_Data.impl;
					InputFromSensor2 : in data port Sensor_Data.impl;
					InputFromSensor3 : in data port Sensor_Data.impl;
					Output : out data port I2H.impl;
		
				annex agree {**
		
					eq Sensor1_Reliable: bool;
					eq Sensor2_Reliable: bool;
					eq Sensor3_Reliable: bool;
		
					guarantee G1 "Sensor sensor status to pilot":
							(Output.Sensor1_Reliable = Sensor1_Reliable)
						and (Output.Sensor2_Reliable = Sensor2_Reliable)
						and (Output.Sensor3_Reliable = Sensor3_Reliable);
		
				**};
		
			end IAS;
		
			-------------------------------------
			-- Sensors
			-------------------------------------
		
			system Sensor1
				features
					Output : out data port Sensor_Data.impl;
			end Sensor1;
		
			system Sensor2
				features
					Output : out data port Sensor_Data.impl;
			end Sensor2;
		
			system Sensor3
				features
					Output : out data port Sensor_Data.impl;
			end Sensor3;
		
			-------------------------------------
			-- Human-Machine Team
			-------------------------------------
		
			system Top
				annex agree {**
					guarantee G0 "Placeholder gaurantee to get AGREE to run": true;
				**};
			end Top;
		
			system implementation Top.impl
				subcomponents
					Human: system Human;
					IAS: system IAS;
					Sensor1: system Sensor1;
					Sensor2: system Sensor2;
					Sensor3: system Sensor3;
				connections
					Top_impl_new_connection: feature IAS.Output -> Human.InputFromIAS;
					Top_impl_new_connection2: feature Sensor1.Output -> IAS.InputFromSensor1;
					Top_impl_new_connection3: feature Sensor2.Output -> IAS.InputFromSensor2;
					Top_impl_new_connection4: feature Sensor3.Output -> IAS.InputFromSensor3;
					Top_impl_new_connection5: feature Human.Output -> IAS.InputFromHuman;
					Top_impl_new_connection6: feature Sensor1.Output -> Human.InputFromSensor1;
					Top_impl_new_connection7: feature Sensor2.Output -> Human.InputFromSensor2;
					Top_impl_new_connection8: feature Sensor3.Output -> Human.InputFromSensor3;
				annex agree{**
		
					------------------------------------------------------------------------------
					-- LEMMAS (These should be true based on the guarantees of the subcomponents.)
					------------------------------------------------------------------------------
		
					lemma L1 "Pilot acknowledges unreliable sensor alerts": 
							(prev(not IAS.Output.Sensor1_Reliable, false) <=> ((Human.Output.Sensor1_Unreliable_Response = enum(Response, Agree)) or (Human.Output.Sensor1_Unreliable_Response = enum(Response, Disagree))))
						and (prev(not IAS.Output.Sensor2_Reliable, false) <=> ((Human.Output.Sensor2_Unreliable_Response = enum(Response, Agree)) or (Human.Output.Sensor2_Unreliable_Response = enum(Response, Disagree))))
						and (prev(not IAS.Output.Sensor3_Reliable, false) <=> ((Human.Output.Sensor3_Unreliable_Response = enum(Response, Agree)) or (Human.Output.Sensor3_Unreliable_Response = enum(Response, Disagree))));
		
				**};
			end Top.impl;
		
		end Human_IAS_Team;
	'''

	@Test(expected=Test.None /* no exception expected */ )
	def void issue31test1() {
		val testResult = issues = testHelper.testString(issue31ModelPackage, issue31LibPackage)

		val topLevelImpl = EcoreUtil2.getAllContentsOfType(testResult.resource.contents.head, ComponentImplementation).
			filter(
				it |
					"Top.impl".equals(it.name)
			).head
		val componentInstance = InstantiateModel.instantiate(topLevelImpl)
		val agreeProgram = new AgreeASTBuilder().getAgreeProgram(componentInstance, false)
		agreeProgram.globalTypes.filter(jkind.lustre.EnumType).head => [
			Assert.assertEquals("Types_Constants__Response", id)
			Assert.assertTrue(values.contains("Types_Constants__Response_Agree"))
		]
		agreeProgram.topNode.lemmas.head => [
			Assert.assertEquals("Pilot acknowledges unreliable sensor alerts", string)
			expr as jkind.lustre.BinaryExpr => [
				left as jkind.lustre.BinaryExpr => [
					left as jkind.lustre.BinaryExpr => [
						right as jkind.lustre.BinaryExpr => [
							left as jkind.lustre.BinaryExpr => [
								right as jkind.lustre.IdExpr => [
									Assert.assertEquals("Types_Constants__Response_Agree", id)
								]
							]
						]
					]
				]
			]
		]
	}

}
