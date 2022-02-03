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
import org.osate.aadl2.modelsupport.errorreporting.AnalysisErrorReporterManager
import org.osate.aadl2.modelsupport.errorreporting.QueuingAnalysisErrorReporter

import com.rockwellcollins.atc.agree.analysis.ast.AgreeASTBuilder
import com.rockwellcollins.atc.agree.tests.AgreeUiInjectorProvider
import com.rockwellcollins.atc.agree.tests.testsupport.TestHelper
import com.rockwellcollins.atc.agree.analysis.AgreeException
import com.rockwellcollins.atc.agree.analysis.ast.AgreeAADLConnection

@RunWith(XtextRunner)
@InjectWith(AgreeUiInjectorProvider)
class Issue30Test extends XtextTest {

	@Inject
	TestHelper<AadlPackage> testHelper

	def instantiateWithErrorMessages(ComponentImplementation ci) {
		val errorManager = new AnalysisErrorReporterManager(QueuingAnalysisErrorReporter.factory)
		val result = InstantiateModel.instantiate(ci, errorManager)
		val errorReporter = errorManager.getReporter(result.eResource()) as QueuingAnalysisErrorReporter
		val instantiationMarkers = errorReporter.getErrors()
		return (result -> instantiationMarkers)
	}

	static val issue30TestMissingConnectionModel = '''
		package Issue30TestModel
		public
			with Base_Types;
			renames Base_Types::all;
		
			system TempSensor
				features
					env_temp : in data port Integer;
					high_temp_indicator : out data port Boolean;
					temp_reading : out data port Integer;
			
				annex agree {**
					assume "Temp bounded":
						((env_temp > 0) and (env_temp < 10));
		
					guarantee "If temperature is high, output high indication.":
						(((env_temp > 8) <=> high_temp_indicator) and (env_temp = temp_reading));
				**};
		
			end TempSensor;
		
			system SensorSystem
				features
					env_temp : in data port Integer;
					temp_read : out data port Integer;
					temp_high : out data port Boolean;
			
				annex agree {**
					assume "Temp bounded":
						((env_temp > 0) and (env_temp < 10));
		
					guarantee "Temperature guarantee.":
						(env_temp = temp_read) and ((env_temp > 8) <=> temp_high);
				**};
		
			end SensorSystem;
		
			system implementation SensorSystem.impl
				subcomponents
					temp : system TempSensor;
		
				connections
					temp_out : port env_temp -> temp.env_temp;
					temp_sensor_read : port temp.temp_reading -> temp_read;
					high_temp : port temp.high_temp_indicator -> temp_high;
		
			end SensorSystem.impl;
		
			system TopLevel
				features 
					env_temp : in data port Integer;
					temp_sensor_high : out data port Boolean;
			
				annex agree {**
					assume "Temp bounded":
						(env_temp > 0) and (env_temp < 10);
				**};
			
			end TopLevel;
		
			system implementation TopLevel.impl
				subcomponents
					sensors: system SensorSystem.impl;
		
				connections
					temp_out : port env_temp -> sensors.env_temp;
					temp_indicator : port sensors.temp_high -> temp_sensor_high;
			
				annex agree{**
					lemma "The sensor only reports high when temp is actually high." :
						((env_temp > 8) <=> temp_sensor_high);
				**};
		
			end TopLevel.impl;
		
		end Issue30TestModel;
	'''

	@Test(expected=Test.None /* no exception expected */)
	def void issue30testMissingConnection() {
		val testResult = issues = testHelper.testString(
			com.rockwellcollins.atc.agree.tests.issues.Issue30Test.issue30TestMissingConnectionModel)

		val topLevelImpl = EcoreUtil2.getAllContentsOfType(testResult.resource.contents.head, ComponentImplementation).
			filter(
				it |
					"TopLevel.impl".equals(it.name)
			).head
		val instanceWithErrors = instantiateWithErrorMessages(topLevelImpl)
		val errors = instanceWithErrors.value
		Assert.assertTrue(errors.stream.anyMatch(msg|msg.message.contains("Could not continue connection from TopLevel_impl_Instance.sensors.temp.temp_reading ")))
	}

	static val issue30TestGoodConnectionModel = '''
		package Issue30TestModel
		public
			with Base_Types;
			renames Base_Types::all;
		
			system TempSensor
				features
					env_temp : in data port Integer;
					high_temp_indicator : out data port Boolean;
					temp_reading : out data port Integer;
			
				annex agree {**
					assume "Temp bounded":
						((env_temp > 0) and (env_temp < 10));
		
					guarantee "If temperature is high, output high indication.":
						(((env_temp > 8) <=> high_temp_indicator) and (env_temp = temp_reading));
				**};
		
			end TempSensor;
		
			system SensorSystem
				features
					env_temp : in data port Integer;
					temp_read : out data port Integer;
					temp_high : out data port Boolean;
			
				annex agree {**
					assume "Temp bounded":
						((env_temp > 0) and (env_temp < 10));
		
					guarantee "Temperature guarantee.":
						(env_temp = temp_read) and ((env_temp > 8) <=> temp_high);
				**};
		
			end SensorSystem;
		
			system implementation SensorSystem.impl
				subcomponents
					temp : system TempSensor;
		
				connections
					temp_out : port env_temp -> temp.env_temp;
					temp_sensor_read : port temp.temp_reading -> temp_read;
					high_temp : port temp.high_temp_indicator -> temp_high;
		
			end SensorSystem.impl;
		
			system TopLevel
				features 
					env_temp : in data port Integer;
					temp_sensor_high : out data port Boolean;
					temp_read : out data port Integer;
			
				annex agree {**
					assume "Temp bounded":
						(env_temp > 0) and (env_temp < 10);
				**};
			
			end TopLevel;
		
			system implementation TopLevel.impl
				subcomponents
					sensors: system SensorSystem.impl;
		
				connections
					temp_out : port env_temp -> sensors.env_temp;
					temp_indicator : port sensors.temp_high -> temp_sensor_high;
					temp_sensor_read : port sensors.temp_read -> temp_read;
			
				annex agree{**
					lemma "The sensor only reports high when temp is actually high." :
						((env_temp > 8) <=> temp_sensor_high);
				**};
		
			end TopLevel.impl;
		
		end Issue30TestModel;
	'''

	@Test(expected=Test.None /* no exception expected */)
	def void issue30testGoodConnection() {
		val testResult = issues = testHelper.testString(
			com.rockwellcollins.atc.agree.tests.issues.Issue30Test.issue30TestGoodConnectionModel)

		val topLevelImpl = EcoreUtil2.getAllContentsOfType(testResult.resource.contents.head, ComponentImplementation).
			filter(
				it |
					"TopLevel.impl".equals(it.name)
			).head
		val instanceWithErrors = instantiateWithErrorMessages(topLevelImpl)
		val errors = instanceWithErrors.value
		Assert.assertTrue(errors.empty)
		val agreeProgram = new AgreeASTBuilder().getAgreeProgram(instanceWithErrors.key, false)
		agreeProgram.topNode.connections.filter(AgreeAADLConnection).forEach(conn | 
			topLevelImpl.ownedConnections.contains(conn.reference)
		)
	}

	static val issue30TestFanOutModel = '''
		package Issue30TestModel
		public
			with Base_Types;
			renames Base_Types::all;
		
			system TempSensor
				features
					env_temp : in data port Integer;
					high_temp_indicator : out data port Boolean;
			
				annex agree {**
					assume "Temp bounded":
						((env_temp > 0) and (env_temp < 10));
		
					guarantee "If temperature is high, output high indication.":
						((env_temp > 8) <=> high_temp_indicator);
				**};
		
			end TempSensor;

			system BooleanSink
				features
					inp : in data port Boolean;
			end BooleanSink;
		
			system SensorSystem
				features
					env_temp : in data port Integer;
					temp_high : out data port Boolean;
			
				annex agree {**
					assume "Temp bounded":
						((env_temp > 0) and (env_temp < 10));
		
					guarantee "Temperature guarantee.":
						((env_temp > 8) <=> temp_high);
				**};
		
			end SensorSystem;
		
			system implementation SensorSystem.impl
				subcomponents
					temp1 : system TempSensor;
					temp2 : system TempSensor;
					sink1 : system BooleanSink;
					sink2 : system BooleanSink;
		
				connections
					temp_out1 : port env_temp -> temp1.env_temp;
					temp_out2 : port env_temp -> temp2.env_temp;
					temp_sink1 : port temp1.high_temp_indicator -> sink1.inp;
					temp_sink2 : port temp2.high_temp_indicator -> sink2.inp;
		
				annex agree {**
					assign temp_high = (temp1.high_temp_indicator or temp2.high_temp_indicator);
				**};
			
			end SensorSystem.impl;
		
			system TopLevel
				features 
					env_temp : in data port Integer;
					temp_sensor_high : out data port Boolean;
			
				annex agree {**
					assume "Temp bounded":
						(env_temp > 0) and (env_temp < 10);
				**};
			
			end TopLevel;
		
			system implementation TopLevel.impl
				subcomponents
					sensors: system SensorSystem.impl;
		
				connections
					temp_out : port env_temp -> sensors.env_temp;
					temp_indicator : port sensors.temp_high -> temp_sensor_high;
			
				annex agree{**
					lemma "The sensor only reports high when temp is actually high." :
						((env_temp > 8) <=> temp_sensor_high);
				**};
		
			end TopLevel.impl;
		
		end Issue30TestModel;
	'''

	@Test(expected=Test.None /* no exception expected */)
	def void issue30testFanOut() {
		val testResult = issues = testHelper.testString(
			com.rockwellcollins.atc.agree.tests.issues.Issue30Test.issue30TestFanOutModel)

		val topLevelImpl = EcoreUtil2.getAllContentsOfType(testResult.resource.contents.head, ComponentImplementation).
			filter(
				it |
					"TopLevel.impl".equals(it.name)
			).head
		val instanceWithErrors = instantiateWithErrorMessages(topLevelImpl)
		val componentInstance = instanceWithErrors.key
		val errors = instanceWithErrors.value
		Assert.assertTrue(errors.empty)
		val agreeProgram = new AgreeASTBuilder().getAgreeProgram(componentInstance, false)
		agreeProgram.topNode.connections.filter(AgreeAADLConnection).forEach(conn | 
			topLevelImpl.ownedConnections.contains(conn.reference)
		)
	}

	static val issue30TestFanInModel = '''
		package Issue30TestModel
		public
			with Base_Types;
			renames Base_Types::all;
		
			system TempSensor
				features
					env_temp : in data port Integer;
					high_temp_indicator : out data port Boolean;
			
				annex agree {**
					assume "Temp bounded":
						((env_temp > 0) and (env_temp < 10));
		
					guarantee "If temperature is high, output high indication.":
						((env_temp > 8) <=> high_temp_indicator);
				**};
		
			end TempSensor;
		
			system SensorSystem
				features
					env_temp : in data port Integer;
					temp_high : out data port Boolean;
			
				annex agree {**
					assume "Temp bounded":
						((env_temp > 0) and (env_temp < 10));
		
					guarantee "Temperature guarantee.":
						((env_temp > 8) <=> temp_high);
				**};
		
			end SensorSystem;
		
			system implementation SensorSystem.impl
				subcomponents
					temp1 : system TempSensor;
					temp2 : system TempSensor;
		
				connections
					temp_out1 : port env_temp -> temp1.env_temp;
					temp_out2 : port env_temp -> temp2.env_temp;
					temp_high_out1 : port temp1.high_temp_indicator -> temp_high;
					temp_high_out2 : port temp2.high_temp_indicator -> temp_high;
		
			end SensorSystem.impl;
		
			system TopLevel
				features 
					env_temp : in data port Integer;
					temp_sensor_high : out data port Boolean;
			
				annex agree {**
					assume "Temp bounded":
						(env_temp > 0) and (env_temp < 10);
				**};
			
			end TopLevel;
		
			system implementation TopLevel.impl
				subcomponents
					sensors: system SensorSystem.impl;
		
				connections
					temp_out : port env_temp -> sensors.env_temp;
					temp_indicator : port sensors.temp_high -> temp_sensor_high;
			
				annex agree{**
					lemma "The sensor only reports high when temp is actually high." :
						((env_temp > 8) <=> temp_sensor_high);
				**};
		
			end TopLevel.impl;
		
		end Issue30TestModel;
	'''

	@Test(expected=AgreeException)
	def void issue30testFanIn() {
		val testResult = issues = testHelper.testString(
			com.rockwellcollins.atc.agree.tests.issues.Issue30Test.issue30TestFanInModel)

		val topLevelImpl = EcoreUtil2.getAllContentsOfType(testResult.resource.contents.head, ComponentImplementation).
			filter(
				it |
					"TopLevel.impl".equals(it.name)
			).head
		val instanceWithErrors = instantiateWithErrorMessages(topLevelImpl)
		val componentInstance = instanceWithErrors.key
		val errors = instanceWithErrors.value
		// OSATE should generate an error message telling of the multiple ports fanning in
		Assert.assertTrue(errors.exists[message.contains("More than one connection instance ends at data port TopLevel_impl_Instance.temp_sensor_high")])
		try {
			new AgreeASTBuilder().getAgreeProgram(componentInstance.componentInstances.head, false)
		} catch (AgreeException e) {
			Assert.assertTrue(e.message.contains("Multiple connections to feature 'Issue30TestModel::SensorSystem.temp_high'"))
			throw e
		}
	}

}
