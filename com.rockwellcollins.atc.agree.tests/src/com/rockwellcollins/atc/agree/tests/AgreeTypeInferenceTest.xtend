package com.rockwellcollins.atc.agree.tests

import com.google.inject.Inject

import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

import org.eclipse.xtext.testing.InjectWith
import org.eclipse.xtext.testing.XtextRunner
import com.itemis.xtext.testing.XtextTest
import com.itemis.xtext.testing.FluentIssueCollection

import org.osate.aadl2.AadlPackage
import org.osate.aadl2.DataPort
import org.osate.aadl2.DataType
import org.osate.aadl2.DefaultAnnexSubclause
import org.osate.aadl2.SystemType

import com.rockwellcollins.atc.agree.AgreeTypeSystem
import com.rockwellcollins.atc.agree.agree.AgreeContract
import com.rockwellcollins.atc.agree.agree.AgreeContractSubclause
import com.rockwellcollins.atc.agree.agree.EqStatement
import com.rockwellcollins.atc.agree.agree.NamedElmExpr

import com.rockwellcollins.atc.agree.tests.testsupport.TestHelper

@RunWith(XtextRunner)
@InjectWith(AgreeInjectorProvider)
class AgreeTypeInferenceTest extends XtextTest {

	@Inject
	TestHelper<AadlPackage> testHelper

	@Test
	def void testExtensionOfPrimitiveTypes() {
		val model = '''
			package Scratch
			public
				with Base_Types;
			
				data bool_data_type extends Base_Types::Boolean
				end bool_data_type;
			
				data int_data_type extends Base_Types::Integer
				end int_data_type;
			
				data real_data_type extends Base_Types::Float
				end real_data_type;
			
				system SystemA
					features
						port1 : in data port bool_data_type;
						port2 : in data port int_data_type;
						port3 : in data port real_data_type;
					annex agree {**
						eq zb : bool = port1;
						eq zi : int = port2;
						eq zr : real = port3;
					**};
				end SystemA;
			
			end Scratch;
		'''

		val testResult = issues = testHelper.testString(model)
		val issueCollection = new FluentIssueCollection(testResult.resource, newArrayList, newArrayList)

		// parseResult => [
		testResult.resource.contents.head as AadlPackage => [
			Assert.assertEquals(4, publicSection.ownedClassifiers.size);
			Assert.assertEquals(3, publicSection.ownedClassifiers.filter(DataType).size)
			val boolDataType = publicSection.ownedClassifiers.filter(DataType).head
			val intDataType = publicSection.ownedClassifiers.filter(DataType).tail.head
			val realDataType = publicSection.ownedClassifiers.filter(DataType).tail.tail.head
			Assert.assertEquals(1, publicSection.ownedClassifiers.filter(SystemType).size)
			publicSection.ownedClassifiers.filter(SystemType).head => [
				Assert.assertEquals(3, ownedFeatures.filter(DataPort).size);
				val port1 = ownedFeatures.filter(DataPort).head as DataPort
				val port2 = ownedFeatures.filter(DataPort).tail.head as DataPort
				val port3 = ownedFeatures.filter(DataPort).tail.tail.head as DataPort
				Assert.assertEquals(1, ownedAnnexSubclauses.size)
				ownedAnnexSubclauses.head as DefaultAnnexSubclause => [
					Assert.assertTrue(parsedAnnexSubclause instanceof AgreeContractSubclause)
					parsedAnnexSubclause as AgreeContractSubclause => [
						Assert.assertNotNull(contract)
						Assert.assertTrue(contract instanceof AgreeContract)
						contract as AgreeContract => [
							Assert.assertEquals(3, specs.filter(EqStatement).size)
							specs.filter(EqStatement).head => [
								Assert.assertEquals(1, lhs.size)
								Assert.assertEquals('zb', lhs.head.name)
								Assert.assertTrue(expr instanceof NamedElmExpr)
								expr as NamedElmExpr => [
									Assert.assertTrue(elm instanceof DataPort)
									Assert.assertEquals(port1, elm)
									elm as DataPort => [
										Assert.assertEquals(boolDataType, dataFeatureClassifier)
									]
								]
								Assert.assertEquals(AgreeTypeSystem.Prim.BoolTypeDef, AgreeTypeSystem.infer(expr))
							]
							specs.filter(EqStatement).tail.head => [
								Assert.assertEquals(1, lhs.size)
								Assert.assertEquals('zi', lhs.head.name)
								Assert.assertTrue(expr instanceof NamedElmExpr)
								expr as NamedElmExpr => [
									Assert.assertTrue(elm instanceof DataPort)
									Assert.assertEquals(port2, elm)
									elm as DataPort => [
										Assert.assertEquals(intDataType, dataFeatureClassifier)
									]
								]
								Assert.assertEquals(AgreeTypeSystem.Prim.IntTypeDef, AgreeTypeSystem.infer(expr))
							]
							specs.filter(EqStatement).tail.tail.head => [
								Assert.assertEquals(1, lhs.size)
								Assert.assertEquals('zr', lhs.head.name)
								Assert.assertTrue(expr instanceof NamedElmExpr)
								expr as NamedElmExpr => [
									Assert.assertTrue(elm instanceof DataPort)
									Assert.assertEquals(port3, elm)
									elm as DataPort => [
										Assert.assertEquals(realDataType, dataFeatureClassifier)
									]
								]
								Assert.assertEquals(AgreeTypeSystem.Prim.RealTypeDef, AgreeTypeSystem.infer(expr))
							]
						]
					]
				]
			]
		]

		assertConstraints(issueCollection.sizeIs(0))
	}

}
