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
class Issue30Test extends XtextTest {

	@Inject
	TestHelper<AadlPackage> testHelper

	// @Inject
	// extension AssertHelper
	static val issue30TestModel = '''
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
					pressure_sensor_high : out data port Boolean;
			
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
	def void issue30test1() {
		val testResult = issues = testHelper.testString(
			com.rockwellcollins.atc.agree.tests.issues.Issue30Test.issue30TestModel)

		val topLevelImpl = EcoreUtil2.getAllContentsOfType(testResult.resource.contents.head, ComponentImplementation).
			filter(
				it |
					"TopLevel.impl".equals(it.name)
			).head
		val componentInstance = InstantiateModel.instantiate(topLevelImpl)
		new AgreeASTBuilder().getAgreeProgram(componentInstance, false)
	}

}
