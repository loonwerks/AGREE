package com.rockwellcollins.atc.agree.tests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.eclipse.xtext.diagnostics.Severity;
import org.eclipse.xtext.testing.InjectWith;
import org.eclipse.xtext.testing.XtextRunner;
import org.eclipse.xtext.validation.Issue;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.osate.testsupport.TestHelper;

import com.google.inject.Inject;
import com.itemis.xtext.testing.FluentIssueCollection;
import com.itemis.xtext.testing.XtextTest;
import com.rockwellcollins.atc.agree.agree.AgreePackage;

@RunWith(XtextRunner.class)
@InjectWith(AgreeInjectorProvider.class)
public class AgreeValidationTest extends XtextTest {

	@Inject
	TestHelper<AgreePackage> testHelper;

	@Test
	public void testEnumStatementNoErrors() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "	\r\n"
				+ "annex agree {**\r\n"
				+ "	enum test = {alpha, beta, gamma};\r\n"
				+ "**};\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		assertTrue(issueCollection.getIssues().isEmpty());
	}

	@Test
	public void testEnumStatementMultipleUsesOfValueError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "	\r\n"
				+ "annex agree {**\r\n"
				+ "	enum test1 = {alpha, beta, gamma};\r\n"
				+ "	enum test2 = {gamma, phi, epsilon};\r\n"
				+ "**};\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues, "Multiple uses of the same enum value 'gamma' in 'TestPackage::test1' and 'TestPackage::test2'"));
	}

	@Test
	public void testEnumStatementDefinedOnlyInAADLPkgError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "	annex agree {**\r\n"
				+ "		enum test = {alpha, beta, gamma};\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues, "Enumerations can be defined only in AADL packages"));
	}

	@Test
	public void testConnectionStatementNoErrors() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "system implementation sys.impl\r\n"
				+ "	subcomponents\r\n"
				+ "		this_sensor: device TestSensor;\r\n"
				+ "		this_other_sensor: device TestSensor;\r\n"
				+ "	connections\r\n"
				+ "		sensor_conn: port this_sensor.data_out -> this_other_sensor.data_in;\r\n"
				+ "	annex agree{**\r\n"
				+ "		connection sensor_conn: true;\r\n"
				+ "	**};\r\n"
				+ "end sys.impl;\r\n"
				+ "\r\n"
				+ "device TestSensor\r\n"
				+ "	features\r\n"
				+ "		data_in: in event port;\r\n"
				+ "	    data_out: out event port;\r\n"
				+ "end TestSensor;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		for(Issue i : issueCollection.getIssues()) {
			assertFalse(i.getSeverity()==Severity.ERROR);
		}
	}

	@Test
	public void testConnectionStatementLabelNotConnectionError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "system implementation sys.impl\r\n"
				+ "	subcomponents\r\n"
				+ "		this_sensor: device TestSensor;\r\n"
				+ "		this_other_sensor: device TestSensor;\r\n"
				+ "	connections\r\n"
				+ "		sensor_conn: port this_sensor.data_out -> this_other_sensor.data_in;\r\n"
				+ "	annex agree{**\r\n"
				+ "		connection this_sensor: true;\r\n"
				+ "	**};\r\n"
				+ "end sys.impl;\r\n"
				+ "\r\n"
				+ "device TestSensor\r\n"
				+ "	features\r\n"
				+ "		data_in: in event port;\r\n"
				+ "	    data_out: out event port;\r\n"
				+ "end TestSensor;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues, "The connection label in the connection statement is not a connection"));
	}

	@Test
	public void testConnectionStatementTypeMustBeBoolTypeError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "system implementation sys.impl\r\n"
				+ "	subcomponents\r\n"
				+ "		this_sensor: device TestSensor;\r\n"
				+ "		this_other_sensor: device TestSensor;\r\n"
				+ "	connections\r\n"
				+ "		sensor_conn: port this_sensor.data_out -> this_other_sensor.data_in;\r\n"
				+ "	annex agree{**\r\n"
				+ "		connection sensor_conn: 1.0;\r\n"
				+ "	**};\r\n"
				+ "end sys.impl;\r\n"
				+ "\r\n"
				+ "device TestSensor\r\n"
				+ "	features\r\n"
				+ "		data_in: in event port;\r\n"
				+ "	    data_out: out event port;\r\n"
				+ "end TestSensor;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues, "The expression for the connection statement is of type 'real' but must be of type 'bool'"));
	}

	@Test
	public void testConnectionStatementAllowedOnlyInComponentImplError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "	annex agree{**\r\n"
				+ "		connection test: true;\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "system implementation sys.impl\r\n"
				+ "end sys.impl;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues, "Connection statements are allowed only in component implementations."));
	}

	@Test
	public void testConnectionStatementDepreciatedWarning() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "system implementation sys.impl\r\n"
				+ "	subcomponents\r\n"
				+ "		this_sensor: device TestSensor;\r\n"
				+ "		this_other_sensor: device TestSensor;\r\n"
				+ "	connections\r\n"
				+ "		sensor_conn: port this_sensor.data_out -> this_other_sensor.data_in;\r\n"
				+ "	annex agree{**\r\n"
				+ "		connection sensor_conn: true;\r\n"
				+ "	**};\r\n"
				+ "end sys.impl;\r\n"
				+ "\r\n"
				+ "device TestSensor\r\n"
				+ "	features\r\n"
				+ "		data_in: in event port;\r\n"
				+ "	    data_out: out event port;\r\n"
				+ "end TestSensor;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getWarning(issues, "Connection statements are deprecated and will be removed in a future version of AGREE."));
	}

	@Test
	public void testOrderStatementNoErrors() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "system implementation sys.impl\r\n"
				+ "	subcomponents\r\n"
				+ "		this_sensor: device TestSensor;\r\n"
				+ "		this_other_sensor: device TestSensor;\r\n"
				+ "	annex agree{**\r\n"
				+ "		ordering : this_sensor, this_other_sensor;\r\n"
				+ "	**};\r\n"
				+ "end sys.impl;\r\n"
				+ "\r\n"
				+ "device TestSensor\r\n"
				+ "end TestSensor;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		assertTrue(issueCollection.getIssues().isEmpty());
	}

	@Test
	public void testOrderStatementNotSubcomponentError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "	features\r\n"
				+ "		data_in : in event port;\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "system implementation sys.impl\r\n"
				+ "	subcomponents\r\n"
				+ "		this_sensor: device TestSensor;\r\n"
				+ "		this_other_sensor: device TestSensor;\r\n"
				+ "	annex agree{**\r\n"
				+ "		ordering : this_sensor, this_other_sensor, data_in;\r\n"
				+ "	**};\r\n"
				+ "end sys.impl;\r\n"
				+ "\r\n"
				+ "device TestSensor\r\n"
				+ "end TestSensor;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues, "Element 'data_in' is not a subcomponent of 'sys.impl'"));
	}

	@Test
	public void testOrderStatementSubcomponentsNotPresentError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "	features\r\n"
				+ "		data_in : in event port;\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "system implementation sys.impl\r\n"
				+ "	subcomponents\r\n"
				+ "		this_sensor: device TestSensor;\r\n"
				+ "		this_other_sensor: device TestSensor;\r\n"
				+ "	annex agree{**\r\n"
				+ "		ordering : this_sensor;\r\n"
				+ "	**};\r\n"
				+ "end sys.impl;\r\n"
				+ "\r\n"
				+ "device TestSensor\r\n"
				+ "end TestSensor;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues, "The following subcomponents are not present in the ordering: this_other_sensor"));
	}

	@Test
	public void testOrderStatementAppearOnlyInComponentImplError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "	features\r\n"
				+ "		data_in : in event port;\r\n"
				+ "	annex agree{**\r\n"
				+ "		ordering : data_in;\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues, "Ordering statements can appear only in component implementations"));
	}

	@Test
	public void testAssignStatementNoErrors() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "	with Base_Types;\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "	features\r\n"
				+ "		data_out : out data port Base_Types::Float;\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "system implementation sys.impl\r\n"
				+ "	annex agree{**\r\n"
				+ "		assign data_out = 5.0;\r\n"
				+ "	**};\r\n"
				+ "end sys.impl;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		assertTrue(issueCollection.getIssues().isEmpty());
	}

	@Test
	public void testAssignStatementAllowedOnlyInComponentImplError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "	with Base_Types;\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "	features\r\n"
				+ "		data_out : out data port Base_Types::Float;\r\n"
				+ "	annex agree{**\r\n"
				+ "		assign data_out = 5.0;\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues, "Assignment statements are allowed only in component implementations"));
	}

	@Test
	public void testAssignStatementInputVariablesError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "	with Base_Types;\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "	features\r\n"
				+ "		data_out : out data port Base_Types::Float;\r\n"
				+ "	annex agree{**\r\n"
				+ "		agree_input a: real;\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "system implementation sys.impl\r\n"
				+ "	annex agree{**\r\n"
				+ "		assign a = 5.0;\r\n"
				+ "	**};\r\n"
				+ "end sys.impl;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues, "Assignment to agree_input variables is illegal."));
	}

	@Test
	public void testAssignStatementLHSMustBeEqOrOutputPortError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "	with Base_Types;\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "	features\r\n"
				+ "		data_out : out data port Base_Types::Float;\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "system implementation sys.impl\r\n"
				+ "	annex agree{**\r\n"
				+ "		property test = true;\r\n"
				+ "		assign test = false;\r\n"
				+ "	**};\r\n"
				+ "end sys.impl;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues,"LHS of assignment must be an AGREE 'eq' variable or an output port of this component"));
	}

	@Test
	public void testAssignStatementTypeMismatchError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "	with Base_Types;\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "	features\r\n"
				+ "		data_out : out data port Base_Types::Float;\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "system implementation sys.impl\r\n"
				+ "	annex agree{**\r\n"
				+ "		assign data_out = 1;\r\n"
				+ "	**};\r\n"
				+ "end sys.impl;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues, "The left hand side of the assignment statement is of type 'real' but the right hand side is of type 'int'"));
	}

	@Test
	public void testAssignStatementMultipleAssignmentsError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "	with Base_Types;\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "	features\r\n"
				+ "		data_out : out data port Base_Types::Float;\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "system implementation sys.impl\r\n"
				+ "	annex agree{**\r\n"
				+ "		assign data_out = 1.0;\r\n"
				+ "		assign data_out = 2.0;\r\n"
				+ "	**};\r\n"
				+ "end sys.impl;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues,"Multiple assignments to variable 'data_out'"));
	}

	@Test
	public void testAssignStatementMultipleEqAssignmentsError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "system implementation sys.impl\r\n"
				+ "	annex agree{**\r\n"
				+ "		eq test : bool = true;\r\n"
				+ "		assign test = true;\r\n"
				+ "	**};\r\n"
				+ "end sys.impl;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues,"Multiple assignments to variable 'test'"));
	}

	@Test
	public void testArgTypeRangeMustBeOfTypeIntOrRealError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "	\r\n"
				+ "system sys\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "system implementation sys.impl\r\n"
				+ " 	annex agree{**\r\n"
				+ " 		eq test : bool[0,1];\r\n"
				+ "    **};\r\n"
				+ "end sys.impl;\r\n"
				+ "\r\n"
				+ "end TestPackage;\r\n";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues,"Ranged variables must be of type 'int' or of type 'real'"));
	}

	@Test
	public void testArgTypeContainsRealIntervalError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "	\r\n"
				+ "system sys\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "system implementation sys.impl\r\n"
				+ " 	annex agree{**\r\n"
				+ " 		eq test : int[0.0, 10];\r\n"
				+ "    **};\r\n"
				+ "end sys.impl;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues,"Ranged variable of type 'int' contains a 'real' value in its interval"));
	}

	@Test
	public void testArgTypeContainsIntIntervalError() throws Exception {
	String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "	\r\n"
				+ "system sys\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "system implementation sys.impl\r\n"
				+ " 	annex agree{**\r\n"
				+ " 		eq test : real[0, 10.0];\r\n"
				+ "    **};\r\n"
				+ "end sys.impl;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues,"Ranged variable of type 'real' contains an 'int' value in its interval"));
	}

	@Test
	public void testArgTypeLowValueGreaterThanHighValueError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "	\r\n"
				+ "system sys\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "system implementation sys.impl\r\n"
				+ " 	annex agree{**\r\n"
				+ " 		eq test : int[15, 10];\r\n"
				+ "    **};\r\n"
				+ "end sys.impl;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues,"The low value of the interval is greater than or equal to the high end"));
	}

	@Test
	public void testArgTypeMustBeRecordArrayDataImplEnumDatatypeError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "	\r\n"
				+ "system sys\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "system implementation sys.impl\r\n"
				+ " 	annex agree{**\r\n"
				+ " 		fun test_fn(x : real) : real = x;\r\n"
				+ " 		eq test : test_fn;\r\n"
				+ "    **};\r\n"
				+ "end sys.impl;\r\n"
				+ "\r\n"
				+ "end TestPackage;\r\n";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues,"types must be record definition, array definition, data implementation, enumeration, or datatype"));
	}

	@Test
	public void testArgTypeDataImplWithoutSubcomponentMustExtendBaseTypeError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "	\r\n"
				+ "system sys\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "system implementation sys.impl\r\n"
				+ " 	annex agree{**\r\n"
				+ " 		eq test : TestData.impl;\r\n"
				+ "    **};\r\n"
				+ "end sys.impl;\r\n"
				+ "\r\n"
				+ "data TestData\r\n"
				+ "end TestData;\r\n"
				+ "\r\n"
				+ "data implementation TestData.impl\r\n"
				+ "end TestData.impl;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues,"Data Implementations with no subcomponents must extend"
				+ " a Base_Type that AGREE can reason about."));
	}

	@Ignore
	public void testArgTypeDataImplWithSubcomponentCannotExtendBaseTypeError() throws Exception {
		// TODO: Issue #128
	}

	@Test
	public void testArgTypeAADLDatatypesMustExtendBaseTypeError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "	\r\n"
				+ "system sys\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "system implementation sys.impl\r\n"
				+ " 	annex agree{**\r\n"
				+ " 		eq test : TestData;\r\n"
				+ "    **};\r\n"
				+ "end sys.impl;\r\n"
				+ "\r\n"
				+ "data TestData\r\n"
				+ "end TestData;\r\n"
				+ "\r\n"
				+ "data implementation TestData.impl\r\n"
				+ "end TestData.impl;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues,"AADL Datatypes must extend a Base_Type that AGREE can reason about."));
	}

	@Test
	public void testFloorCastNoErrors() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "	features\r\n"
				+ "		data_in : in event port;\r\n"
				+ "	annex agree{**\r\n"
				+ "		const test : int = floor(3.14);\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		assertTrue(issueCollection.getIssues().isEmpty());
	}

	@Test
	public void testFloorCastNotAllowedInLinearizationBodyError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "	annex agree{**\r\n"
				+ "		linearization test (x : real) over [0.0 .. 10.0] within 0.1: floor(x);\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues,"'floor' expressions not allowed in linearization body expressions"));
	}

	@Test
	public void testFloorCastNamedThingMustBeAnExprWithTypeError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "	features\r\n"
				+ "		data_in : in event port;\r\n"
				+ "	annex agree{**\r\n"
				+ "		eq test : int = floor(data_in);\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues,"named thing must be an expression with a type"));
	}

	@Test
	public void testFloorCastMustBeOfTypeRealError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "	features\r\n"
				+ "		data_in : in event port;\r\n"
				+ "	annex agree{**\r\n"
				+ "		const test : int = floor(3);\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues,"Argument of floor cast is of type 'int' but must be of type 'real'"));
	}

	@Test
	public void testRealCastNoErrors() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "	annex agree{**\r\n"
				+ "		const test : real = real(3);\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		assertTrue(issueCollection.getIssues().isEmpty());
	}

	@Test
	public void testRealCastNotAllowedInLinearizationBodyError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "	annex agree{**\r\n"
				+ "		linearization test (x : real) over [0.0 .. 10.0] within 0.1: real(x);\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues,"'real' expressions not allowed in linearization body expressions"));
	}

	@Test
	public void testRealCastNamedThingMustBeAnExprWithTypeError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "	features\r\n"
				+ "		data_in : in event port;\r\n"
				+ "	annex agree{**\r\n"
				+ "		eq test : real = real(data_in);\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues,"named thing must be an expression with a type"));
	}

	@Test
	public void testRealCastMustBeOfTypeIntError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "	annex agree{**\r\n"
				+ "		const test : real = real(3.0);\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues,"Argument of real cast is of type 'real' but must be of type 'int'"));
	}

	@Test
	public void testEventExprNoErrors() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "	features\r\n"
				+ "		data_in : in event port;\r\n"
				+ "	annex agree{**\r\n"
				+ "		property test = event(data_in);\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		assertTrue(issueCollection.getIssues().isEmpty());
	}

	@Test
	public void testEventExprNotAllowedInLinearizationBodyError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "	features\r\n"
				+ "		data_in : in event port;\r\n"
				+ "	annex agree{**\r\n"
				+ "		linearization test (x : real) over [0.0 .. 10.0] within 0.1: event(data_in);\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues,"'event' expressions not allowed in linearization body expressions"));
	}

	@Test
	public void testEventExprArgMustBeEventOrEventDataPortError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "	annex agree{**\r\n"
				+ "		property test = event(1);\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues,"Argument of event expression must be an event port or event data port"));
	}

	@Test
	public void testLatchedExprNoErrors() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "	with Base_Types;\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "	features\r\n"
				+ "		data_in : in data port Base_Types::Boolean;\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "system implementation sys.impl\r\n"
				+ "	annex agree{**\r\n"
				+ "		synchrony : latched;\r\n"
				+ "		eq test : bool = latched(data_in);\r\n"
				+ "	**};\r\n"
				+ "end sys.impl;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		assertTrue(issueCollection.getIssues().isEmpty());
	}

	@Test
	public void testLatchedExprAppearOnlyInComponentImplThatContainLatchedSynchStatementError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "	with Base_Types;\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "	features\r\n"
				+ "		data_in : in data port Base_Types::Boolean;\r\n"
				+ "	\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "system implementation sys.impl\r\n"
				+ "	annex agree{**\r\n"
				+ "		eq test : bool = latched(data_in);\r\n"
				+ "	**};\r\n"
				+ "end sys.impl;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues,"Latched expressions can appear only in component implementations "
						+ "that contain a latched synchrony statement"));
	}

	@Test
	public void testLatchedExprAppearOnlyInComponentImplError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "	with Base_Types;\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "	features\r\n"
				+ "		data_in : in data port Base_Types::Boolean;\r\n"
				+ "	annex agree{**\r\n"
				+ "		eq test : bool = latched(data_in);\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues,"Latched expressions can appear only in component implementations"));
	}

	@Test
	public void testLatchedExprValidOnlyForInputDataPortsOrEventExprError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "	features\r\n"
				+ "		data_in : in event port;\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "system implementation sys.impl\r\n"
				+ "	annex agree{**\r\n"
				+ "		synchrony : latched;\r\n"
				+ "		eq test : bool = latched(data_in);\r\n"
				+ "	**};\r\n"
				+ "end sys.impl;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues,"Latched expressions are valid only for input data ports or event expressions over input event data ports"));
	}

	@Test
	public void testSynchStatementNoErrors() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "system implementation sys.impl\r\n"
				+ "	annex agree{**\r\n"
				+ "		synchrony : 1;\r\n"
				+ "	**};\r\n"
				+ "end sys.impl;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		assertTrue(issueCollection.getIssues().isEmpty());
	}

	@Test
	public void testSynchStatementAppearOnlyInComponentImplError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "	annex agree{**\r\n"
				+ "		synchrony : 1;\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues,"Synchrony statements can appear only in component implementations"));
	}

	@Test
	public void testSynchStatementSecondValueMustBeGreaterThanZeroError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "system implementation sys.impl\r\n"
				+ "	annex agree{**\r\n"
				+ "		synchrony : 2, 0;\r\n"
				+ "	**};\r\n"
				+ "end sys.impl;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues,"The second value of a synchrony statment must be greater than zero"));
	}

	@Test
	public void testSynchStatementSecondValueMustBeLessThanFirstError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "system implementation sys.impl\r\n"
				+ "	annex agree{**\r\n"
				+ "		synchrony : 2, 3;\r\n"
				+ "	**};\r\n"
				+ "end sys.impl;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues,"The second value of a synchrony argument must be less than the first"));
	}

	@Test
	public void testMNSynchStatementNoErrors() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "system implementation sys.impl\r\n"
				+ "	subcomponents\r\n"
				+ "		this_sensor : device TestSensor;\r\n"
				+ "		this_other_sensor : device TestSensor;\r\n"
				+ "	annex agree{**\r\n"
				+ "		synchrony : this_sensor, this_other_sensor : 5, 3;\r\n"
				+ "	**};\r\n"
				+ "end sys.impl;\r\n"
				+ "\r\n"
				+ "device TestSensor\r\n"
				+ "end TestSensor;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		assertTrue(issueCollection.getIssues().isEmpty());
	}

	@Test
	public void testMNSynchStatementAppearOnlyInComponentImplError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "	annex agree{**\r\n"
				+ "		synchrony : this_sensor, this_other_sensor : 5, 3;\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "system implementation sys.impl\r\n"
				+ "	subcomponents\r\n"
				+ "		this_sensor : device TestSensor;\r\n"
				+ "		this_other_sensor : device TestSensor;\r\n"
				+ "end sys.impl;\r\n"
				+ "\r\n"
				+ "device TestSensor\r\n"
				+ "end TestSensor;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues,"Synchrony statements can appear only in component implementations"));
	}

	@Ignore
	public void testMNSynchStatementMismatchedNumberSubcomponentsAndTimingRangesError() throws Exception {
		//TODO
	}

	@Test
	public void testMNSynchStatementElement1IsNotSubcomponentError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "	features\r\n"
				+ "		data_in : in event port;\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "system implementation sys.impl\r\n"
				+ "	subcomponents\r\n"
				+ "		this_sensor : device TestSensor;\r\n"
				+ "		this_other_sensor : device TestSensor;\r\n"
				+ "	annex agree{**\r\n"
				+ "		synchrony : data_in, this_other_sensor : 5, 3;\r\n"
				+ "	**};\r\n"
				+ "end sys.impl;\r\n"
				+ "\r\n"
				+ "device TestSensor\r\n"
				+ "end TestSensor;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues,"Element 'data_in' is not a subcomponent of 'sys.impl'"));
	}

	@Test
	public void testMNSynchStatementElement2IsNotSubcomponentError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "	features\r\n"
				+ "		data_in : in event port;\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "system implementation sys.impl\r\n"
				+ "	subcomponents\r\n"
				+ "		this_sensor : device TestSensor;\r\n"
				+ "		this_other_sensor : device TestSensor;\r\n"
				+ "	annex agree{**\r\n"
				+ "		synchrony : this_sensor, data_in : 5, 3;\r\n"
				+ "	**};\r\n"
				+ "end sys.impl;\r\n"
				+ "\r\n"
				+ "device TestSensor\r\n"
				+ "end TestSensor;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues,"Element 'data_in' is not a subcomponent of 'sys.impl'"));
	}

	@Test
	public void testMNSynchStatementQuasiSynchValueMustBeGreaterThanZeroError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "	features\r\n"
				+ "		data_in : in event port;\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "system implementation sys.impl\r\n"
				+ "	subcomponents\r\n"
				+ "		this_sensor : device TestSensor;\r\n"
				+ "		this_other_sensor : device TestSensor;\r\n"
				+ "	annex agree{**\r\n"
				+ "		synchrony : this_sensor, this_other_sensor : 1, 0;\r\n"
				+ "	**};\r\n"
				+ "end sys.impl;\r\n"
				+ "\r\n"
				+ "device TestSensor\r\n"
				+ "end TestSensor;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues,"Quasi-synchronous values must be greater than zero"));
	}

	@Test
	public void testMNSynchStatementLHSQuasiSynchValueMustBeGreaterThanRHSError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "	features\r\n"
				+ "		data_in : in event port;\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "system implementation sys.impl\r\n"
				+ "	subcomponents\r\n"
				+ "		this_sensor : device TestSensor;\r\n"
				+ "		this_other_sensor : device TestSensor;\r\n"
				+ "	annex agree{**\r\n"
				+ "		synchrony : this_sensor, this_other_sensor : 2, 3;\r\n"
				+ "	**};\r\n"
				+ "end sys.impl;\r\n"
				+ "\r\n"
				+ "device TestSensor\r\n"
				+ "end TestSensor;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues,"Left hand side quasi-synchronous values must be greater than the right hand side"));
	}

	@Test
	public void testCalenStatementNoErrors() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "system implementation sys.impl\r\n"
				+ "	subcomponents\r\n"
				+ "		this_sensor : device TestSensor;\r\n"
				+ "	annex agree{**\r\n"
				+ "		calendar : this_sensor;\r\n"
				+ "	**};\r\n"
				+ "end sys.impl;\r\n"
				+ "\r\n"
				+ "device TestSensor\r\n"
				+ "end TestSensor;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		assertTrue(issueCollection.getIssues().isEmpty());
	}

	@Test
	public void testCalenStatementAppearOnlyInComponentImplError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "	features\r\n"
				+ "		data_in : in event port;\r\n"
				+ "	annex agree{**\r\n"
				+ "		calendar : data_in;\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues,"Calendar statements can appear only in component implementations"));
	}

	@Test
	public void testCalenStatementElementNotSubcomponentError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "	features\r\n"
				+ "		data_in : in event port;\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "system implementation sys.impl\r\n"
				+ "	annex agree{**\r\n"
				+ "		calendar : data_in;\r\n"
				+ "	**};\r\n"
				+ "end sys.impl;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues,"Element 'data_in' is not a subcomponent of 'sys.impl'"));
	}

	@Test
	public void testAsynchStatementNoErrors() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "system implementation sys.impl\r\n"
				+ "	annex agree{**\r\n"
				+ "		synchrony : asynchronous;\r\n"
				+ "	**};\r\n"
				+ "end sys.impl;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		assertTrue(issueCollection.getIssues().isEmpty());
	}

	@Test
	public void testAsynchStatementAppearOnlyInComponentImplError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "	annex agree{**\r\n"
				+ "		synchrony : asynchronous;\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues,"Synchrony statements can appear only in component implementations"));
	}

	@Test
	public void testLatchedStatementNoErrors() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "system implementation sys.impl\r\n"
				+ "	annex agree{**\r\n"
				+ "		synchrony : latched;\r\n"
				+ "	**};\r\n"
				+ "end sys.impl;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		assertTrue(issueCollection.getIssues().isEmpty());
	}

	@Test
	public void testLatchedStatementAppearOnlyInComponentImplError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "	annex agree{**\r\n"
				+ "		synchrony : latched;\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues,"Synchrony statements can appear only in component implementations"));
	}

	@Ignore
	public void testDuplicateIdInSpecNoErrors() throws Exception {
		//TODO
	}

	@Ignore
	public void testDuplicateIdInSpecDuplicateIdError() throws Exception {
		//TODO
	}

	@Test
	public void testAssumeStatementNoErrors() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "	annex agree{**\r\n"
				+ "		assume A1 \"SimpleTest\" : true;\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		assertTrue(issueCollection.getIssues().isEmpty());
	}

	@Test
	public void testAssumeStatementAllowedOnlyInComponentTypesError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "system implementation sys.impl\r\n"
				+ "	annex agree{**\r\n"
				+ "		assume A1 \"SimpleTest\" : true;\r\n"
				+ "	**};\r\n"
				+ "end sys.impl;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues,"Assume statements are allowed only in component types"));
	}

	@Test
	public void testAssumeStatementMustBeOfTypeBoolError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "	annex agree{**\r\n"
				+ "		assume A1 \"SimpleTest\" : 1;\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues,"Expression for assume statement is of type 'int' but must be of type 'bool'"));
	}

	@Test
	public void testAssumeStatementNamedInfoMessage() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "	annex agree{**\r\n"
				+ "		assume \"SimpleTest\" : true;\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getInfo(issues,"It is recommended that assume statements be named."
				+ " (Hint: an identifier may be placed between the \"assume\" keyword and the quoted description.)"));
	}

	@Test
	public void testInitialStatementNoErrors() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "	annex agree{**\r\n"
				+ "		initially : true;\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		assertTrue(issueCollection.getIssues().isEmpty());
	}

	@Test
	public void testInitialStatementAllowedOnlyInComponentTypesError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "system implementation sys.impl\r\n"
				+ "	annex agree{**\r\n"
				+ "		initially : true;\r\n"
				+ "	**};\r\n"
				+ "end sys.impl;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues,"Initial statements are allowed only in component types"));
	}

	@Test
	public void testInitialStatementMustBeOfTypeBoolError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "	annex agree{**\r\n"
				+ "		initially : 1;\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues,"Expression for 'initially' statement is of type 'int' but must be of type 'bool'"));
	}

	@Test
	public void testLiftContractNoErrors() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "system implementation sys.impl\r\n"
				+ "	subcomponents\r\n"
				+ "		this_sensor : device TestSensor;\r\n"
				+ "    annex agree{**\r\n"
				+ "    	lift contract;\r\n"
				+ "    **};\r\n"
				+ "end sys.impl;\r\n"
				+ "\r\n"
				+ "device TestSensor\r\n"
				+ "end TestSensor;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		assertTrue(issueCollection.getIssues().isEmpty());
	}

	@Test
	public void testLiftContractNotAllowedInComponentImplWithAGREEAnnex() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "	annex agree{**\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "system implementation sys.impl\r\n"
				+ "	subcomponents\r\n"
				+ "		this_sensor : device TestSensor;\r\n"
				+ "    annex agree{**\r\n"
				+ "    	lift contract;\r\n"
				+ "    **};\r\n"
				+ "end sys.impl;\r\n"
				+ "\r\n"
				+ "device TestSensor\r\n"
				+ "end TestSensor;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues,"'lift contract;' statement is not allowed in component implementation whose type has an AGREE annex."));
	}

	@Test
	public void testLiftContractNotAllowedWithImplWithMoreThanOneSourceChildConnectionOutOfSameOutputError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "system implementation sys.impl\r\n"
				+ "	subcomponents\r\n"
				+ "		this_sensor : device TestSensor;\r\n"
				+ "	connections\r\n"
				+ "		sensor_conn : port this_sensor.data_out -> this_sensor.data_in;\r\n"
				+ "		other_sensor_conn : port this_sensor.data_out -> this_sensor.more_data_in;\r\n"
				+ "    annex agree{**\r\n"
				+ "    	lift contract;\r\n"
				+ "    **};\r\n"
				+ "end sys.impl;\r\n"
				+ "\r\n"
				+ "device TestSensor\r\n"
				+ "	features\r\n"
				+ "		data_in : in data port;\r\n"
				+ "		data_out : out data port;\r\n"
				+ "		more_data_in : in data port;\r\n"
				+ "end TestSensor;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues,"'lift contract;' statement is not allowed in component implementation with more than one connection out of same output TestPackage::TestSensor.data_out."));
	}

	@Test
	public void testLiftContractNotAllowedWithImplWithMoreThanOneSourceParentConnectionOutOfSameInputError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "	features\r\n"
				+ "		data_in : in data port;\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "system implementation sys.impl\r\n"
				+ "	subcomponents\r\n"
				+ "		this_sensor : device TestSensor;\r\n"
				+ "	connections\r\n"
				+ "		sensor_conn : port data_in -> this_sensor.data_in;\r\n"
				+ "		other_sensor_conn : port data_in -> this_sensor.more_data_in;\r\n"
				+ "    annex agree{**\r\n"
				+ "    	lift contract;\r\n"
				+ "    **};\r\n"
				+ "end sys.impl;\r\n"
				+ "\r\n"
				+ "device TestSensor\r\n"
				+ "	features\r\n"
				+ "		data_in : in data port;\r\n"
				+ "		more_data_in : in data port;\r\n"
				+ "end TestSensor;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues,"'lift contract;' statement is not allowed in component implementation with more than one connection out of same input TestPackage::sys.data_in."));
	}

	@Test
	public void testLiftContractNotAllowedWithImplWithMoreThanOneDestinationChildConnectionIntoSameInputError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "system implementation sys.impl\r\n"
				+ "	subcomponents\r\n"
				+ "		this_sensor : device TestSensor;\r\n"
				+ "	connections\r\n"
				+ "		sensor_conn : port this_sensor.data_out -> this_sensor.data_in;\r\n"
				+ "		other_sensor_conn : port this_sensor.more_data_out -> this_sensor.data_in;\r\n"
				+ "    annex agree{**\r\n"
				+ "    	lift contract;\r\n"
				+ "    **};\r\n"
				+ "end sys.impl;\r\n"
				+ "\r\n"
				+ "device TestSensor\r\n"
				+ "	features\r\n"
				+ "		data_in : in data port;\r\n"
				+ "		data_out : out data port;\r\n"
				+ "		more_data_out : out data port;\r\n"
				+ "end TestSensor;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues,"'lift contract;' statement is not allowed in component implementation with more than one connection into same input TestPackage::TestSensor.data_in."));
	}

	@Test
	public void testLiftContractNotAllowedWithImplWithMoreThanOneDestinationParentConnectionIntoSameOutputError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "	features\r\n"
				+ "		data_out : out data port;\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "system implementation sys.impl\r\n"
				+ "	subcomponents\r\n"
				+ "		this_sensor : device TestSensor;\r\n"
				+ "	connections\r\n"
				+ "		sensor_conn : port this_sensor.data_out -> data_out;\r\n"
				+ "		other_sensor_conn : port this_sensor.more_data_out -> data_out;\r\n"
				+ "    annex agree{**\r\n"
				+ "    	lift contract;\r\n"
				+ "    **};\r\n"
				+ "end sys.impl;\r\n"
				+ "\r\n"
				+ "device TestSensor\r\n"
				+ "	features\r\n"
				+ "		data_out : out data port;\r\n"
				+ "		more_data_out : out data port;\r\n"
				+ "end TestSensor;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues,"'lift contract;' statement is not allowed in component implementation with more than one connection into same output TestPackage::sys.data_out."));
	}

	@Test
	public void testLiftContractNotAllowedWithImplWithoutParentConnectionFromInputError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "	features\r\n"
				+ "		data_in : in event port;\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "system implementation sys.impl\r\n"
				+ "	subcomponents\r\n"
				+ "		this_sensor : device TestSensor;\r\n"
				+ "    annex agree{**\r\n"
				+ "    	lift contract;\r\n"
				+ "    **};\r\n"
				+ "end sys.impl;\r\n"
				+ "\r\n"
				+ "device TestSensor\r\n"
				+ "end TestSensor;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues,"'lift contract;' statement is not allowed in component implementation without connection from input TestPackage::sys.data_in."));
	}

	@Test
	public void testLiftContractNotAllowedWithImplWithoutParentConnectionFromOutputError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "	features\r\n"
				+ "		data_out : out event port;\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "system implementation sys.impl\r\n"
				+ "	subcomponents\r\n"
				+ "		this_sensor : device TestSensor;\r\n"
				+ "    annex agree{**\r\n"
				+ "    	lift contract;\r\n"
				+ "    **};\r\n"
				+ "end sys.impl;\r\n"
				+ "\r\n"
				+ "device TestSensor\r\n"
				+ "end TestSensor;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues,"'lift contract;' statement is not allowed in component implementation without connection to output TestPackage::sys.data_out."));
	}

	@Test
	public void testLiftContractNotAllowedWithImplWithoutChildConnectionFromInputError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "system implementation sys.impl\r\n"
				+ "	subcomponents\r\n"
				+ "		this_sensor : device TestSensor;\r\n"
				+ "    annex agree{**\r\n"
				+ "    	lift contract;\r\n"
				+ "    **};\r\n"
				+ "end sys.impl;\r\n"
				+ "\r\n"
				+ "device TestSensor\r\n"
				+ "	features\r\n"
				+ "		data_in : in data port;\r\n"
				+ "end TestSensor;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues,"'lift contract;' statement is not allowed in component implementation without connection into TestPackage::TestSensor.data_in."));
	}

	@Test
	public void testLiftContractNotAllowedWithImplWithoutChildConnectionFromOutputError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "system implementation sys.impl\r\n"
				+ "	subcomponents\r\n"
				+ "		this_sensor : device TestSensor;\r\n"
				+ "    annex agree{**\r\n"
				+ "    	lift contract;\r\n"
				+ "    **};\r\n"
				+ "end sys.impl;\r\n"
				+ "\r\n"
				+ "device TestSensor\r\n"
				+ "	features\r\n"
				+ "		data_out : out data port;\r\n"
				+ "end TestSensor;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues,"'lift contract;' statement is not allowed in component implementation without connection out of TestPackage::TestSensor.data_out."));
	}

	@Test
	public void testLiftContractNotAllowedInCompImplWithoutExactlyOneSubcomponentError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "system implementation sys.impl\r\n"
				+ "	subcomponents\r\n"
				+ "		this_sensor : device TestSensor;\r\n"
				+ "		this_other_sensor : device TestSensor;\r\n"
				+ "    annex agree{**\r\n"
				+ "    	lift contract;\r\n"
				+ "    **};\r\n"
				+ "end sys.impl;\r\n"
				+ "\r\n"
				+ "device TestSensor\r\n"
				+ "end TestSensor;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues,"'lift contract;' statement is not allowed in component implementation without exactly one subcomponent."));
	}

	@Test
	public void testLiftContractNotAllowedInComponentInterfaceError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "    annex agree{**\r\n"
				+ "    	lift contract;\r\n"
				+ "    **};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "system implementation sys.impl\r\n"
				+ "	subcomponents\r\n"
				+ "		this_sensor : device TestSensor;\r\n"
				+ "end sys.impl;\r\n"
				+ "\r\n"
				+ "device TestSensor\r\n"
				+ "end TestSensor;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues,"'lift contract;' statement is not allowed in component interface."));
	}

	@Test
	public void testAssertStatementNoErrors() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "system implementation sys.impl\r\n"
				+ "    annex agree{**\r\n"
				+ "    	assert AS1 \"SimpleTest\" : true;\r\n"
				+ "    **};\r\n"
				+ "end sys.impl;\r\n"
				+ "\r\n"
				+ "device TestSensor\r\n"
				+ "end TestSensor;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		for(Issue i : issueCollection.getIssues()) {
			assertFalse(i.getSeverity()==Severity.ERROR);
		}
	}

	@Test
	public void testAssertStatementAllowedOnlyInComponentImplError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "	annex agree{**\r\n"
				+ "    	assert AS1 \"SimpleTest\" : true;\r\n"
				+ "    **};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "system implementation sys.impl\r\n"
				+ "end sys.impl;\r\n"
				+ "\r\n"
				+ "device TestSensor\r\n"
				+ "end TestSensor;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues,"Assert statements are allowed only in component implementations."));
	}

	@Test
	public void testAssertStatementMustBeOfTypeBoolError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "system implementation sys.impl\r\n"
				+ "    annex agree{**\r\n"
				+ "    	assert AS1 \"SimpleTest\" : 1;\r\n"
				+ "    **};\r\n"
				+ "end sys.impl;\r\n"
				+ "\r\n"
				+ "device TestSensor\r\n"
				+ "end TestSensor;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues,"Expression for assert statement is of type 'int' but must be of type 'bool'"));
	}

	@Test
	public void testAssertStatementDiscourageAssertWarning() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "system implementation sys.impl\r\n"
				+ "    annex agree{**\r\n"
				+ "    	assert AS1 \"SimpleTest\" : true;\r\n"
				+ "    **};\r\n"
				+ "end sys.impl;\r\n"
				+ "\r\n"
				+ "device TestSensor\r\n"
				+ "end TestSensor;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getWarning(issues,"We highly discourage the use of assert statements. "
				+ "They can easily lead to inconsistent or unrealizable systems. "
				+ "Note that our realizability check does not verify that component "
				+ "assertions are realizable.  It is likely that you can specify the "
				+ "behavior you want by changing the subcomponent contracts or by using assignment statements."));
	}

	@Test
	public void testAssertStatementNamedInfoMessage() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "system implementation sys.impl\r\n"
				+ "    annex agree{**\r\n"
				+ "    	assert \"SimpleTest\" : true;\r\n"
				+ "    **};\r\n"
				+ "end sys.impl;\r\n"
				+ "\r\n"
				+ "device TestSensor\r\n"
				+ "end TestSensor;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getInfo(issues, "It is recommended that assert statements be named."
				+ " (Hint: an identifier may be placed between the \"assert\" keyword and the quoted description.)"));
	}

	@Test
	public void testAADLEnumeratorNoErrors() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "	with Data_Model;\r\n"
				+ "	\r\n"
				+ "	data Primary_Color\r\n"
				+ "	properties\r\n"
				+ "		Data_Model::Data_Representation => Enum;\r\n"
				+ "		Data_Model::Enumerators => (\"Red\", \"Green\", \"Blue\");\r\n"
				+ "	end Primary_Color;\r\n"
				+ "	\r\n"
				+ "	system sys\r\n"
				+ "		annex agree {**\r\n"
				+ "			eq test : Primary_Color = enum(Primary_Color, Red);\r\n"
				+ "		**};\r\n"
				+ "	end sys;\r\n"
				+ "	\r\n"
				+ "	end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		assertTrue(issueCollection.getIssues().isEmpty());
	}

	@Test
	public void testAADLEnumeratorMustReferToDataTypeEnumError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "	\r\n"
				+ "system sys\r\n"
				+ "	annex agree {**\r\n"
				+ "		type test_struct = struct{a : int};\r\n"
				+ "		eq test : test_struct = enum(test_struct, a);\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "	\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues, "AADL Enumerations must refer to a Data Type with \"Enum\" data representation "
				+ "property and have an \"Enumerators\' property value list."));
	}

	@Test
	public void testAADLEnumeratorDoesNotHaveEnumValueError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "	with Data_Model;\r\n"
				+ "	\r\n"
				+ "data Primary_Color\r\n"
				+ "	properties\r\n"
				+ "		Data_Model::Data_Representation => Enum;\r\n"
				+ "		Data_Model::Enumerators => (\"Red\", \"Green\", \"Blue\");\r\n"
				+ "end Primary_Color;\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "	annex agree {**\r\n"
				+ "		eq test : Primary_Color = enum(Primary_Color, Orange);\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues, "AADL Enumeration TestPackage::Primary_Color does not have an enumeration value Orange"));
	}

	@Test
	public void testGuaranteeStatementNoErrors() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "	\r\n"
				+ "system sys\r\n"
				+ "	annex agree {**\r\n"
				+ "		guarantee G1 \"SimpleTest\" : true;\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		assertTrue(issueCollection.getIssues().isEmpty());
	}

	@Test
	public void testGuaranteeStatementAllowedOnlyInComponentTypesError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "	\r\n"
				+ "system sys\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "system implementation sys.impl\r\n"
				+ "	annex agree {**\r\n"
				+ "		guarantee G1 \"SimpleTest\" : true;\r\n"
				+ "	**};\r\n"
				+ "end sys.impl;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues, "Guarantee statements are allowed only in component types"));
	}

	@Test
	public void testGuaranteeStatementMustBeOfTypeBoolError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "	\r\n"
				+ "system sys\r\n"
				+ "	annex agree {**\r\n"
				+ "		guarantee G1 \"SimpleTest\" : 1;\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues, "Expression for guarantee statement is of type 'int' but must be of type 'bool'"));
	}

	@Test
	public void testGuaranteeStatementNamedInfoMessage() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "	\r\n"
				+ "system sys\r\n"
				+ "	annex agree {**\r\n"
				+ "		guarantee \"SimpleTest\" : true;\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getInfo(issues, "It is recommended that guarantee statements be named."
				+ " (Hint: an identifier may be placed between the \"guarantee\" keyword and the quoted description.)"));
	}

	@Test
	public void testReachableStatementNoErrors() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "	\r\n"
				+ "system sys\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "system implementation sys.impl\r\n"
				+ "	annex agree {**\r\n"
				+ "		reachable R1 \"SimpleTest\" : true;\r\n"
				+ "	**};\r\n"
				+ "end sys.impl;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		assertTrue(issueCollection.getIssues().isEmpty());
	}

	@Test
	public void testReachableStatementAllowedOnlyInComponentImplError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "	\r\n"
				+ "system sys\r\n"
				+ "	annex agree {**\r\n"
				+ "		reachable R1 \"SimpleTest\" : true;\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues, "Reachable statements are allowed only in component implementations"));
	}

	@Test
	public void testReachableStatementMustBeOfTypeBoolError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "	\r\n"
				+ "system sys\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "system implementation sys.impl\r\n"
				+ "	annex agree {**\r\n"
				+ "		reachable R1 \"SimpleTest\" : 1;\r\n"
				+ "	**};\r\n"
				+ "end sys.impl;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues, "Expression for reachable statement is of type 'int' but must be of type 'bool'"));
	}

	@Test
	public void testReachableStatementNamedInfoMessage() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "	\r\n"
				+ "system sys\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "system implementation sys.impl\r\n"
				+ "	annex agree {**\r\n"
				+ "		reachable \"SimpleTest\" : true;\r\n"
				+ "	**};\r\n"
				+ "end sys.impl;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getInfo(issues, "It is recommended that reachable statements be named."
				+ " (Hint: an identifier may be placed between the \"reachable\" keyword and the quoted description.)"));
	}

	@Test
	public void testPeriodicStatementNoErrors() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "	\r\n"
				+ "system sys\r\n"
				+ "	annex agree {**\r\n"
				+ "		eq test_cond : bool = true;\r\n"
				+ "		assume test \"SimpleTest\": condition test_cond occurs each 3.0 with jitter 1.0;\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		assertTrue(issueCollection.getIssues().isEmpty());
	}

	@Test
	public void testPeriodicStatementCanContainOnlyIdentifiersError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "	\r\n"
				+ "system sys\r\n"
				+ "	annex agree {**\r\n"
				+ "		assume test \"SimpleTest\": condition true occurs each 3.0 with jitter 1.0;\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues, "Patterns can contain only identifiers (not general expressions)"));
	}

	@Test
	public void testPeriodicStatementMustBeOfTypeBoolError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "	\r\n"
				+ "system sys\r\n"
				+ "	annex agree {**\r\n"
				+ "		eq test_cond : int = 1;\r\n"
				+ "		assume test \"SimpleTest\": condition test_cond occurs each 3.0 with jitter 1.0;\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues, "Expression is of type 'int' but must be of type 'bool'"));
	}

	@Test
	public void testPeriodicStatementJitterMustBeRealError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "	\r\n"
				+ "system sys\r\n"
				+ "	annex agree {**\r\n"
				+ "		eq test_cond : bool = true;\r\n"
				+ "		assume test \"SimpleTest\": condition test_cond occurs each 3 with jitter 1;\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues, "The specified jitter must be a positive real literal"));
	}

	@Test
	public void testPeriodicStatementPeriodMustBeRealError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "	\r\n"
				+ "system sys\r\n"
				+ "	annex agree {**\r\n"
				+ "		eq test_cond : bool = true;\r\n"
				+ "		assume test \"SimpleTest\": condition test_cond occurs each 3 with jitter 1.0;\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues, "The specified period must be a positive real literal"));
	}

	@Test
	public void testSporadicStatementNoErrors() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "	\r\n"
				+ "system sys\r\n"
				+ "	annex agree {**\r\n"
				+ "		eq test_cond : bool = true;\r\n"
				+ "		assume test \"SimpleTest\": condition test_cond occurs sporadic with IAT 5.0 with jitter 1.0;\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		assertTrue(issueCollection.getIssues().isEmpty());
	}

	@Test
	public void testSporadicStatementCanContainOnlyIdentifiersError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "	\r\n"
				+ "system sys\r\n"
				+ "	annex agree {**\r\n"
				+ "		assume test \"SimpleTest\": condition true occurs sporadic with IAT 5.0 with jitter 1.0;\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues, "Patterns can contain only identifiers (not general expressions)"));
	}

	@Test
	public void testSporadicStatementMustBeOfTypeBoolError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "	\r\n"
				+ "system sys\r\n"
				+ "	annex agree {**\r\n"
				+ "		eq test_cond : int = 1;\r\n"
				+ "		assume test \"SimpleTest\": condition 1 occurs sporadic with IAT 5.0 with jitter 1.0;\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues,"Expression is of type 'int' but must be of type 'bool'"));
	}

	@Test
	public void testSporadicStatementJitterMustBeRealError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "	\r\n"
				+ "system sys\r\n"
				+ "	annex agree {**\r\n"
				+ "		eq test_cond : bool = true;\r\n"
				+ "		assume test \"SimpleTest\": condition test_cond occurs sporadic with IAT 5.0 with jitter 1;\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues,"The specified jitter must be a positive real literal"));
	}

	@Test
	public void testSporadicStatementInterarrivalMustBeRealError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "	\r\n"
				+ "system sys\r\n"
				+ "	annex agree {**\r\n"
				+ "		eq test_cond : bool = true;\r\n"
				+ "		assume test \"SimpleTest\": condition test_cond occurs sporadic with IAT 5 with jitter 1.0;\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues,"The specified interarrival time must be a positive real literal"));
	}

	@Test
	public void testWhenHoldsStatementNoErrors() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "	\r\n"
				+ "system sys\r\n"
				+ "	annex agree {**\r\n"
				+ "		eq test_cond : bool = true;\r\n"
				+ "		eq test_event : bool = true;\r\n"
				+ "		assume test \"SimpleTest\": when test_cond holds during [0.0, 3.0] test_event exclusively occurs during [3.0, 5.0];\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		assertTrue(issueCollection.getIssues().isEmpty());
	}

	@Test
	public void testWhenHoldsStatementLowerBoundMustBeZeroError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "	\r\n"
				+ "system sys\r\n"
				+ "	annex agree {**\r\n"
				+ "		eq test_cond : bool = true;\r\n"
				+ "		eq test_event : bool = true;\r\n"
				+ "		assume test \"SimpleTest\": when test_cond holds during [0.5, 3.0] test_event exclusively occurs during [3.0, 5.0];\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues,"The lower bound of this interval must be zero"));
	}

	@Test
	public void testWhenHoldsStatementMustHaveCauseIntervalError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "	\r\n"
				+ "system sys\r\n"
				+ "	annex agree {**\r\n"
				+ "		eq test_cond : bool = true;\r\n"
				+ "		eq test_event : bool = true;\r\n"
				+ "		assume test \"SimpleTest\": when test_cond holds during test_event exclusively occurs during [3.0, 5.0];\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues,"Statement must have a cause interval"));
	}

	@Test
	public void testWhenHoldsStatementConditionMustBeOfTypeBoolError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "	\r\n"
				+ "system sys\r\n"
				+ "	annex agree {**\r\n"
				+ "		eq test_cond : int = 1;\r\n"
				+ "		eq test_event : bool = true;\r\n"
				+ "		assume test \"SimpleTest\": when test_cond holds during [0.0, 3.0] test_event exclusively occurs during [3.0, 5.0];\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues,"The condition of a when statement is of type 'int' but must be of type 'bool'"));
	}

	@Test
	public void testWhenHoldsStatementEffectMustBeOfTypeBoolError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "	\r\n"
				+ "system sys\r\n"
				+ "	annex agree {**\r\n"
				+ "		eq test_cond : bool = true;\r\n"
				+ "		eq test_event : real = 1.0;\r\n"
				+ "		assume test \"SimpleTest\": when test_cond holds during [0.0, 3.0] test_event exclusively occurs during [3.0, 5.0];\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues,"The effect of a when statement is of type 'real' but must be of type 'bool'"));
	}

	@Test
	public void testWhenOccursStatementNoErrors() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "	\r\n"
				+ "system sys\r\n"
				+ "	annex agree {**\r\n"
				+ "		eq test_cond : bool = true;\r\n"
				+ "		eq test_event : bool = true;\r\n"
				+ "		assume test \"SimpleTest\": when test_cond occurs 3 times during [0.0, 5.0] exclusively raises test_event;\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		assertTrue(issueCollection.getIssues().isEmpty());
	}

	@Test
	public void testWhenOccursStatementConditionMustBeOfTypeBoolError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "	\r\n"
				+ "system sys\r\n"
				+ "	annex agree {**\r\n"
				+ "		eq test_cond : real = 0.0;\r\n"
				+ "		eq test_event : bool = true;\r\n"
				+ "		assume test \"SimpleTest\": when test_cond occurs 3 times during [0.0, 5.0] exclusively raises test_event;\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues,"The condition of the 'when' statement is of type 'real' but must be of type 'bool'"));
	}

	@Test
	public void testWhenOccursStatementEffectMustBeOfTypeBoolError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "	\r\n"
				+ "system sys\r\n"
				+ "	annex agree {**\r\n"
				+ "		eq test_cond : bool = true;\r\n"
				+ "		eq test_event : int = 1;\r\n"
				+ "		assume test \"SimpleTest\": when test_cond occurs 3 times during [0.0, 5.0] exclusively raises test_event;\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues,"The effect of the 'when' statement is of type 'int' but must be of type 'bool'"));
	}

	@Test
	public void testWhenOccursStatementTimesMustBeOfTypeIntError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "	\r\n"
				+ "system sys\r\n"
				+ "	annex agree {**\r\n"
				+ "		eq test_cond : bool = true;\r\n"
				+ "		eq test_event : bool = true;\r\n"
				+ "		assume test \"SimpleTest\": when test_cond occurs 3.0 times during [0.0, 5.0] exclusively raises test_event;\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues,"The 'times' of the 'when' statement is of type 'real' but must be of type 'int'"));
	}

	@Test
	public void testWheneverOccursStatementNoErrors() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "	\r\n"
				+ "system sys\r\n"
				+ "	annex agree {**\r\n"
				+ "		eq test_cond : bool = true;\r\n"
				+ "		eq test_effect : bool = true;\r\n"
				+ "		assume test \"SimpleTest\": whenever test_cond occurs test_effect exclusively occurs during [0.0, 1.0];\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		assertTrue(issueCollection.getIssues().isEmpty());
	}

	@Test
	public void testWheneverOccursStatementCauseMustBeOfTypeBoolError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "	\r\n"
				+ "system sys\r\n"
				+ "	annex agree {**\r\n"
				+ "		eq test_cond : int = 1;\r\n"
				+ "		eq test_effect : bool = true;\r\n"
				+ "		assume test \"SimpleTest\": whenever test_cond occurs test_effect exclusively occurs during [0.0, 1.0];\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues,"The cause of the 'whenever' statement is of type 'int' but must be of type 'bool'"));
	}

	@Test
	public void testWheneverOccursStatementEffectMustBeOfTypeBoolError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "	\r\n"
				+ "system sys\r\n"
				+ "	annex agree {**\r\n"
				+ "		eq test_cond : bool = true;\r\n"
				+ "		eq test_effect : int = 0;\r\n"
				+ "		assume test \"SimpleTest\": whenever test_cond occurs test_effect exclusively occurs during [0.0, 1.0];\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues,"The effect of the 'whenever' statement is of type 'int' but must be of type 'bool'"));
	}

	@Test
	public void testWheneverBecomesTrueStatementNoErrors() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "	\r\n"
				+ "system sys\r\n"
				+ "	annex agree {**\r\n"
				+ "		eq test_cond : bool = true;\r\n"
				+ "		eq test_effect : bool = true;\r\n"
				+ "		assume test \"SimpleTest\": whenever test_cond becomes true test_effect exclusively occurs during (0.0, 1.0];\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		assertTrue(issueCollection.getIssues().isEmpty());
	}

	@Test
	public void testWheneverBecomesTrueStatementCauseMustBeOfTypeBoolError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "	\r\n"
				+ "system sys\r\n"
				+ "	annex agree {**\r\n"
				+ "		eq test_cond : int = 0;\r\n"
				+ "		eq test_effect : bool = true;\r\n"
				+ "		assume test \"SimpleTest\": whenever test_cond becomes true test_effect exclusively occurs during (0.0, 1.0];\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues,"The cause of the 'whenever' statement is of type 'int' but must be of type 'bool'"));
	}

	@Test
	public void testWheneverBecomesTrueStatementEffectMustBeOfTypeBoolError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "	\r\n"
				+ "system sys\r\n"
				+ "	annex agree {**\r\n"
				+ "		eq test_cond : bool = true;\r\n"
				+ "		eq test_effect : int = 1;\r\n"
				+ "		assume test \"SimpleTest\": whenever test_cond becomes true test_effect exclusively occurs during (0.0, 1.0];\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues,"The effect of the 'whenever' statement is of type 'int' but must be of type 'bool'"));
	}

	@Test
	public void testWheneverHoldsStatementNoErrors() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "	\r\n"
				+ "system sys\r\n"
				+ "	annex agree {**\r\n"
				+ "		eq test_cond : bool = true;\r\n"
				+ "		eq test_effect : bool = true;\r\n"
				+ "		assume test \"SimpleTest\": whenever test_cond occurs test_effect exclusively holds during [0.0, 1.0);\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		assertTrue(issueCollection.getIssues().isEmpty());
	}

	@Test
	public void testWheneverHoldsStatementCauseMustBeOfTypeBoolError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "	\r\n"
				+ "system sys\r\n"
				+ "	annex agree {**\r\n"
				+ "		eq test_cond : int = 0;\r\n"
				+ "		eq test_effect : bool = true;\r\n"
				+ "		assume test \"SimpleTest\": whenever test_cond occurs test_effect exclusively holds during [0.0, 1.0);\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues, "The cause of the 'whenever' statement is of type 'int' but must be of type 'bool'"));
	}

	@Test
	public void testWheneverHoldsStatementEffectMustBeOfTypeBoolError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "	\r\n"
				+ "system sys\r\n"
				+ "	annex agree {**\r\n"
				+ "		eq test_cond : bool = true;\r\n"
				+ "		eq test_effect : real = 0.0;\r\n"
				+ "		assume test \"SimpleTest\": whenever test_cond occurs test_effect exclusively holds during [0.0, 1.0);\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues, "The effect of the 'whenever' statement is of type 'real' but must be of type 'bool'"));
	}

	@Test
	public void testWheneverImpliesStatementNoErrors() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "	\r\n"
				+ "system sys\r\n"
				+ "	annex agree {**\r\n"
				+ "		eq test_cond : bool = true;\r\n"
				+ "		eq test_lhs : bool = true;\r\n"
				+ "		eq test_rhs : bool = true;\r\n"
				+ "		assume test \"SimpleTest\": whenever test_cond occurs test_lhs implies test_rhs exclusively during (0.0, 1.0);\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		assertTrue(issueCollection.getIssues().isEmpty());
	}

	@Test
	public void testWheneverImpliesStatementCauseMustBeOfTypeBoolError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "	\r\n"
				+ "system sys\r\n"
				+ "	annex agree {**\r\n"
				+ "		eq test_cond : int = 1;\r\n"
				+ "		eq test_lhs : bool = true;\r\n"
				+ "		eq test_rhs : bool = true;\r\n"
				+ "		assume test \"SimpleTest\": whenever test_cond occurs test_lhs implies test_rhs exclusively during (0.0, 1.0);\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues, "The cause of the 'whenever' statement is of type 'int' but must be of type 'bool'"));
	}

	@Test
	public void testWheneverImpliesStatementLHSMustBeOfTypeBoolError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "	\r\n"
				+ "system sys\r\n"
				+ "	annex agree {**\r\n"
				+ "		eq test_cond : bool = true;\r\n"
				+ "		eq test_lhs : int = 0;\r\n"
				+ "		eq test_rhs : bool = true;\r\n"
				+ "		assume test \"SimpleTest\": whenever test_cond occurs test_lhs implies test_rhs exclusively during (0.0, 1.0);\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues, "The left hand side of the 'implies' of the 'whenever' statement is of type 'int' but must be of type 'bool'"));
	}

	@Test
	public void testWheneverImpliesStatementRHSMustBeOfTypeBoolError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "	\r\n"
				+ "system sys\r\n"
				+ "	annex agree {**\r\n"
				+ "		eq test_cond : bool = true;\r\n"
				+ "		eq test_lhs : bool = true;\r\n"
				+ "		eq test_rhs : int = 1;\r\n"
				+ "		assume test \"SimpleTest\": whenever test_cond occurs test_lhs implies test_rhs exclusively during (0.0, 1.0);\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues, "The rhs hand side of the 'implies' of the 'whenever' statement is of type 'int' but must be of type 'bool'"));
	}

	@Test
	public void testTimeIntervalLowerMustBeRealError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "	\r\n"
				+ "system sys\r\n"
				+ "	annex agree {**\r\n"
				+ "		eq test_cond : bool = true;\r\n"
				+ "		eq test_event : bool = true;\r\n"
				+ "		assume test \"SimpleTest\":  when test_cond occurs 3 times during [0, 5.0] exclusively raises test_event;\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues, "Lower interval must be a real valued literal"));
	}

	@Test
	public void testTimeIntervalHigherMustBeRealError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "	\r\n"
				+ "system sys\r\n"
				+ "	annex agree {**\r\n"
				+ "		eq test_cond : bool = true;\r\n"
				+ "		eq test_event : bool = true;\r\n"
				+ "		assume test \"SimpleTest\":  when test_cond occurs 3 times during [0.0, 5] exclusively raises test_event;\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues,"Higher interval must be a real valued literal"));
	}

	@Test
	public void testTimeRiseNoErrors() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "	with Base_Types;\r\n"
				+ "	\r\n"
				+ "system sys\r\n"
				+ "	features\r\n"
				+ "		data_in : in data port Base_Types::Boolean;\r\n"
				+ "	annex agree {**\r\n"
				+ "		eq test : real = timerise(data_in);\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		assertTrue(issueCollection.getIssues().isEmpty());
	}

	@Test
	public void testTimeRiseAppliedOnlyToBooleanIdentifiersError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "	with Base_Types;\r\n"
				+ "	\r\n"
				+ "system sys\r\n"
				+ "	features\r\n"
				+ "		data_in : in data port Base_Types::Integer;\r\n"
				+ "	annex agree {**\r\n"
				+ "		eq test : real = timerise(data_in);\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues,"Time functions can be applied only to Boolean identifiers"));
	}

	@Test
	public void testTimeFallNoErrors() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "	with Base_Types;\r\n"
				+ "	\r\n"
				+ "system sys\r\n"
				+ "	features\r\n"
				+ "		data_in : in data port Base_Types::Boolean;\r\n"
				+ "	annex agree {**\r\n"
				+ "		eq test : real = timefall(data_in);\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		assertTrue(issueCollection.getIssues().isEmpty());
	}

	@Test
	public void testTimeFallAppliedOnlyToBooleanIdentifiersError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "	with Base_Types;\r\n"
				+ "	\r\n"
				+ "system sys\r\n"
				+ "	features\r\n"
				+ "		data_in : in data port Base_Types::Float;\r\n"
				+ "	annex agree {**\r\n"
				+ "		eq test : real = timefall(data_in);\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues,"Time functions can be applied only to Boolean identifiers"));
	}

	@Test
	public void testTimeOfNoErrors() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "	with Base_Types;\r\n"
				+ "	\r\n"
				+ "system sys\r\n"
				+ "	features\r\n"
				+ "		data_in : in data port Base_Types::Boolean;\r\n"
				+ "	annex agree {**\r\n"
				+ "		eq test : real = timeof(data_in);\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		assertTrue(issueCollection.getIssues().isEmpty());
	}

	@Test
	public void testTimeOfAppliedOnlyToBooleanIdentifiersError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "	\r\n"
				+ "system sys\r\n"
				+ "	features\r\n"
				+ "		data_in : in event port;\r\n"
				+ "	annex agree {**\r\n"
				+ "		eq test : real = timeof(data_in);\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues,"Time functions can be applied only to Boolean identifiers"));
	}

	@Test
	public void testLemmaStatementNoErrors() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "	\r\n"
				+ "system sys\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "system implementation sys.impl\r\n"
				+ "	annex agree {**\r\n"
				+ "		lemma L1 \"SimpleTest\" : true;\r\n"
				+ "	**};\r\n"
				+ "end sys.impl;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		assertTrue(issueCollection.getIssues().isEmpty());
	}

	@Test
	public void testLemmaStatementAllowedOnlyInComponentImplError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "	\r\n"
				+ "system sys\r\n"
				+ "	annex agree {**\r\n"
				+ "		lemma L1 \"SimpleTest\" : true;\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues,"Lemma statements are allowed only in component implementations and nodes"));
	}

	@Test
	public void testLemmaStatementMustBeOfTypeBoolError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "	\r\n"
				+ "system sys\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "system implementation sys.impl\r\n"
				+ "	annex agree {**\r\n"
				+ "		lemma L1 \"SimpleTest\" : 1;\r\n"
				+ "	**};\r\n"
				+ "end sys.impl;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues,"Expression for lemma statement is of type 'int' but must be of type 'bool'"));
	}

	@Test
	public void testLemmaStatementNamedInfoMessage() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "	\r\n"
				+ "system sys\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "system implementation sys.impl\r\n"
				+ "	annex agree {**\r\n"
				+ "		lemma \"SimpleTest\" : true;\r\n"
				+ "	**};\r\n"
				+ "end sys.impl;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getInfo(issues,"It is recommended that lemma statements be named."
				+ " (Hint: an identifier may be placed between the \"lemma\" keyword and the quoted description.)"));
	}

	@Test
	public void testUnaryExprNoErrors() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "	\r\n"
				+ "system sys\r\n"
				+ "	annex agree {**\r\n"
				+ "		property test = not false;\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		assertTrue(issueCollection.getIssues().isEmpty());
	}

	@Test
	public void testUnaryExprNamedThingMustBeAnExprWithTypeError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "	\r\n"
				+ "system sys\r\n"
				+ "	features\r\n"
				+ "		data_in : in event port;\r\n"
				+ "	annex agree {**\r\n"
				+ "		property test = not data_in;\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues,"named thing must be an expression with a type"));
	}

	@Test
	public void testUnaryExprRHSMustBeOfTypeIntOrRealError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "	\r\n"
				+ "system sys\r\n"
				+ "	annex agree {**\r\n"
				+ "		property test = -true;\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues,"right side of unary expression '-' is of type 'bool' but must be of type 'int' or 'real'"));
	}

	@Test
	public void testUnaryExprRHSMustBeOfTypeBoolError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "	\r\n"
				+ "system sys\r\n"
				+ "	annex agree {**\r\n"
				+ "		eq test : int = not 1;\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues,"right side of unary expression 'not' is of type 'int' but must be of type 'bool'"));
	}

	@Test
	public void testPropertyStatementNoErrors() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "	\r\n"
				+ "system sys\r\n"
				+ "	annex agree {**\r\n"
				+ "		property test = false;\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		assertTrue(issueCollection.getIssues().isEmpty());
	}

	@Test
	public void testPropertyStatementAllowedOnlyInComponentAnnexError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "	\r\n"
				+ "annex agree {**\r\n"
				+ "	property test = false;\r\n"
				+ "**};\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues,"Property statments are allowed only in component annexes"));
	}

	@Test
	public void testPropertyStatementMustBeOfTypeBoolError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "	\r\n"
				+ "system sys\r\n"
				+ "	annex agree {**\r\n"
				+ "		property test = 10;\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues,"Property statement 'test' is of type 'int' but must be of type 'bool'"));
	}

	@Test
	public void testInputStatementNoErrors() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "	\r\n"
				+ "system sys\r\n"
				+ "	annex agree {**\r\n"
				+ "		agree_input a : bool, b : int;\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		assertTrue(issueCollection.getIssues().isEmpty());
	}

	@Test
	public void testInputStatementAllowedOnlyInComponentTypesError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "	\r\n"
				+ "annex agree {**\r\n"
				+ "	agree_input a : bool, b : int;\r\n"
				+ "**};\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues,"Input statements are allowed only in component types"));
	}

	@Test
	public void testRecordUpdateExprNoErrors() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "	\r\n"
				+ "system sys\r\n"
				+ "	annex agree {**\r\n"
				+ "		type test = struct{a: bool};\r\n"
				+ "		eq foo : test = test{a = true};\r\n"
				+ "		eq foo2 : test = foo{a := false};\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		assertTrue(issueCollection.getIssues().isEmpty());
	}

	@Test
	public void testRecordUpdateExprNotAllowedInLinearizationBodyError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "	\r\n"
				+ "system sys\r\n"
				+ "	annex agree {**\r\n"
				+ "		type test = struct{a: bool};\r\n"
				+ "		eq foo : test = test{a = true};\r\n"
				+ "		eq foo2 : test = foo{a := false};\r\n"
				+ "		linearization test_ln (x : real) over [0.0 .. 10.0] within 0.1 : foo{a := true};\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues,"Record update expressions not allowed in linearization body expression"));
	}

	@Test
	public void testRecordUpdateExprTypeMismatchError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "	\r\n"
				+ "system sys\r\n"
				+ "	annex agree {**\r\n"
				+ "		type test = struct{a: bool};\r\n"
				+ "		eq foo : test = test{a = true};\r\n"
				+ "		eq foo2 : test = foo{a := 1};\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues,"the update field is of type 'bool', but the expression is of type 'int'"));
	}

	@Test
	public void testRecordUpdateExprMustBeDataImplOrAGREERecordTypeError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "	\r\n"
				+ "system sys\r\n"
				+ "	annex agree {**\r\n"
				+ "		type test = struct{a: int};\r\n"
				+ "		eq foo : int = 2;\r\n"
				+ "		eq foo2 : test = foo{a := 1};\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues,"Record to be updated must be a data implementation or AGREE record type.  Found type 'int'."));
	}

	@Test
	public void testArrayLiteralExprNoErrors() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "	\r\n"
				+ "system sys\r\n"
				+ "	annex agree {**\r\n"
				+ "		eq test_arr : real[3] = [| 1.1, 2.2, 3.3|];\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		assertTrue(issueCollection.getIssues().isEmpty());
	}

	@Test
	public void testArrayLiteralExprMustHaveAtLeastOneElementError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "	\r\n"
				+ "system sys\r\n"
				+ "	annex agree {**\r\n"
				+ "		eq test_arr : real[0] = [| |];\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues,"Array literal must have at least one element"));
	}

	@Test
	public void testArrayLiteralExprNamedThingMustBeAnExprWithTypeError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "	\r\n"
				+ "system sys\r\n"
				+ "	features\r\n"
				+ "		data_in : in event port;\r\n"
				+ "	annex agree {**\r\n"
				+ "		eq test_arr : real[1] = [|data_in|];\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues,"named thing must be an expression with a type"));
	}

	@Test
	public void testArrayLiteralExprTypeMismatchError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "	\r\n"
				+ "system sys\r\n"
				+ "	annex agree {**\r\n"
				+ "		eq test_arr : real[3] = [|1.1, 2.2, 3|];\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues,"type of array element must be 'real', but has type 'int'"));
	}

	@Test
	public void testArrayUpdateExprNoErrors() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "	\r\n"
				+ "system sys\r\n"
				+ "	annex agree {**\r\n"
				+ "		eq test_arr : real[3] = test_arr[| 1:= 3.0|];\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		assertTrue(issueCollection.getIssues().isEmpty());
	}

	@Test
	public void testArrayUpdateExprNamedThingMustBeAnExprWithTypeError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "	\r\n"
				+ "system sys\r\n"
				+ "	features\r\n"
				+ "		data_in : in event port;\r\n"
				+ "	annex agree {**\r\n"
				+ "		eq test_arr : real[3] = data_in[| 1:= 3.0|];\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues,"named thing must be an expression with a type"));
	}

	@Test
	public void testArrayUpdateExprTypeMismatchError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "	\r\n"
				+ "system sys\r\n"
				+ "	annex agree {**\r\n"
				+ "		eq test_arr : real[3] = test_arr[| 1:= 3|];\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues,"type of array element must be 'real', but has type 'int'"));
	}

	@Test
	public void testArrayUpdateExprMustEvaluateToAnArrayError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "	with Base_Types;\r\n"
				+ "	\r\n"
				+ "system sys\r\n"
				+ "	features\r\n"
				+ "		data_in : in data port Base_Types::Float;\r\n"
				+ "	annex agree {**\r\n"
				+ "		eq test_arr : real[3] = data_in[| 1:= 3.0|];\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues,"expression must evaluate to an array"));
	}

	@Test
	public void testArraySubExprNoErrors() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "	\r\n"
				+ "system sys\r\n"
				+ "	annex agree {**\r\n"
				+ "		eq test_arr : real[3];\r\n"
				+ "		eq test : real = test_arr[1];\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		assertTrue(issueCollection.getIssues().isEmpty());
	}

	@Test
	public void testArraySubExprExprNamedThingMustBeAnExprWithTypeError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "	\r\n"
				+ "system sys\r\n"
				+ "	features\r\n"
				+ "		data_in : in data port;\r\n"
				+ "	annex agree {**\r\n"
				+ "		eq test : real = data_in[1];\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues,"named thing must be an expression with a type"));
	}

	@Test
	public void testArraySubExprIndexNamedThingMustBeAnExprWithTypeError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "	\r\n"
				+ "system sys\r\n"
				+ "	features\r\n"
				+ "		data_in : in data port;\r\n"
				+ "	annex agree {**\r\n"
				+ "		eq test_arr : real[3];\r\n"
				+ "		eq test : real = test_arr[data_in];\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues,"named thing must be an expression with a type"));
	}

	@Test
	public void testArraySubExprIndexMustBeOfTypeIntError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "	\r\n"
				+ "system sys\r\n"
				+ "	annex agree {**\r\n"
				+ "		eq test_arr : real[3];\r\n"
				+ "		eq test : real = test_arr[1.0];\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues,"index must be of type 'int'"));
	}

	@Test
	public void testArraySubExprIndexIsOutOfBoundsError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "	\r\n"
				+ "system sys\r\n"
				+ "	annex agree {**\r\n"
				+ "		eq test_arr : real[3];\r\n"
				+ "		eq test : real = test_arr[4];\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues,"Index value 4 is out of array bounds [1 .. 3]"));
	}

	@Test
	public void testArraySubExprCouldNotStaticallyComputeArrayIndexValueError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "	with Base_Types;\r\n"
				+ "	\r\n"
				+ "system sys\r\n"
				+ "	features\r\n"
				+ "		data_in : in data port Base_Types::Integer;\r\n"
				+ "	annex agree {**\r\n"
				+ "		eq test_arr : real[3];\r\n"
				+ "		eq test : real = test_arr[data_in];\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getWarning(issues,"Could not statically compute array index value"));
	}

	@Test
	public void testArraySubExprMustEvaluateToAnArrayError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "	with Base_Types;\r\n"
				+ "	\r\n"
				+ "system sys\r\n"
				+ "	features\r\n"
				+ "		data_in : in data port Base_Types::Float;\r\n"
				+ "	annex agree {**\r\n"
				+ "		eq test : real = data_in[1];\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues,"expression must evaluate to an array"));
	}

	@Test
	public void testRecordLitExprNoErrors() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "	\r\n"
				+ "system sys\r\n"
				+ "	annex agree {**\r\n"
				+ "		type test = struct{a : int, b : real, c : bool};\r\n"
				+ "		eq foo : test = test{a = 3; b = 3.3; c = true};\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		assertTrue(issueCollection.getIssues().isEmpty());
	}

	@Test
	public void testRecordLitExprArgMustBeFeatureGroupOrRecordTypeDefError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "	\r\n"
				+ "system sys\r\n"
				+ "	annex agree {**\r\n"
				+ "		type test = struct{a : int, b : real, c : bool};\r\n"
				+ "		property test_type = true;\r\n"
				+ "		eq foo : test = test_type{a = 3; b = 3.3; c = true};\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues,"Record type 'test_type' must be a feature group or a record type definition"));
	}

	@Test
	public void testRecordLitExprIncorrectNumOfArgsError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "	\r\n"
				+ "system sys\r\n"
				+ "	annex agree {**\r\n"
				+ "		type test = struct{a : int, b : real, c : bool};\r\n"
				+ "		eq foo : test = test{a = 3; b = 3.3};\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues,"Incorrect number of arguments"));
	}

	@Test
	public void testRecordLitExprNoAssignmentToVarInRecordExprError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "	\r\n"
				+ "system sys\r\n"
				+ "	annex agree {**\r\n"
				+ "		type test = struct{a : int, b : real, c : bool};\r\n"
				+ "		eq foo : test = test{a = 3; b = 3.3; b = 4.0};\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues,"No assignment to defined variable 'c' in record expression."));
	}

	@Test
	public void testRecordLitExprTypeMismatchError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "	\r\n"
				+ "system sys\r\n"
				+ "	annex agree {**\r\n"
				+ "		type test = struct{a : int, b : real, c : bool};\r\n"
				+ "		eq foo : test = test{a = 3.0; b = 3.3; c = true};\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues,"The expression assigned to 'a' does not match its definition type of 'int'"));
	}

	@Test
	public void testRecordDefExprNoErrors() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "	\r\n"
				+ "system sys\r\n"
				+ "	annex agree {**\r\n"
				+ "		type test = struct{a : int, b : real, c : bool};\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		assertTrue(issueCollection.getIssues().isEmpty());
	}

	@Test
	public void testRecordDefExprInvolvedInCyclicDefError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "	\r\n"
				+ "system sys\r\n"
				+ "	annex agree {**\r\n"
				+ "		type test = struct{a : test, b : real, c : bool};\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues,"The definition of type 'test' is involved in a cyclic definition"));
	}

	@Test
	public void testConstStatementNoErrors() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "	\r\n"
				+ "system sys\r\n"
				+ "	annex agree {**\r\n"
				+ "		const test : real = 3.14;\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		assertTrue(issueCollection.getIssues().isEmpty());
	}

	@Test
	public void testConstStatementTypeMismatchError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "	\r\n"
				+ "system sys\r\n"
				+ "	annex agree {**\r\n"
				+ "		const test : real = true;\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues, "The assumed type of constant statement 'test' is 'real' but the actual type is 'bool'"));
	}

	@Test
	public void testConstStatemntExprIsPartOfCyclicDefError() throws Exception {
		String test = "package TestPackage\r\n"
					+ "public\r\n"
					+ "\r\n"
					+ "system sys\r\n"
					+ "	annex agree {**\r\n"
					+ "		const test : int = test + 1;\r\n"
					+ "	**};\r\n"
					+ "end sys;\r\n"
					+ "\r\n"
					+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues,
				"Cyclic reference to variable 'test'"));
	}

	@Test
	public void testConstStatementNonConstExprInConstDeclarationError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "	with Base_Types;\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "	features\r\n"
				+ "		data_in : in data port Base_Types::Float;\r\n"
				+ "	annex agree{**\r\n"
				+ "		const test : real = data_in * 3.14;\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues,"Non-constant expression in constant declaration"));
	}

	@Test
	public void testNamedElementSameNameInAnnexAndFeatureError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "	features\r\n"
				+ "		test : in event port;\r\n"
				+ "	annex agree {**\r\n"
				+ "		const test : int = 100;\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues,"Element of the same name ('test') in AGREE Annex in 'sys'"));
		assertNotNull(UtilityFunctions.getError(issues,"Feature of the same name ('test') in component type"));
	}

	@Ignore
	public void testNamedElementSameNameInAnnexAndSubcomponentError() throws Exception {
		// TODO: Issue #130 - Once resolved, change "@Ignore" to "@Test"
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "	annex agree {**\r\n"
				+ "		eq test : int = 100;\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "system implementation sys.impl\r\n"
				+ "	subcomponents\r\n"
				+ "		test : device TestSensor;\r\n"
				+ "end sys.impl;\r\n"
				+ "\r\n"
				+ "device TestSensor\r\n"
				+ "end TestSensor;\r\n"
				+ "\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues,"Element of the same name ('test') in AGREE Annex in 'sys'"));
		assertNotNull(UtilityFunctions.getError(issues,
				"Subcomponent of the same name ('test') in component implementation"));
	}

	@Test
	public void testEqStatementNoErrors() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "	\r\n"
				+ "system sys\r\n"
				+ "	annex agree {**\r\n"
				+ "		eq test : bool = true;\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		assertTrue(issueCollection.getIssues().isEmpty());
	}

	@Test
	public void testEqStatementAllowedOnlyInComponentAnnexError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "	\r\n"
				+ "annex agree {**\r\n"
				+ "	eq test : bool = true;\r\n"
				+ "**};\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues,"Equation statments are allowed only in component annexes"));
	}

	@Test
	public void testEqStatementCyclicReferenceToVarError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "	annex agree {**\r\n"
				+ "		eq test : int = test + 100;\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues,"Cyclic reference to variable 'test'"));
	}

	@Test
	public void testEqStatementCannotContainRangedValueAndRHSExprError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "	annex agree {**\r\n"
				+ "		eq test : real[1.0, 3.0] = 2.4;\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues,"Equation statements cannot contain a ranged value and a right hand side expression"));
	}

	@Test
	public void testEqStatementVariableNumMismatchError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "	annex agree {**\r\n"
				+ "		eq test : int, test2 : real = 2;\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues,"Equation assigns 2 variables, but right side returns 1 values"));
	}

	@Test
	public void testEqStatementTypeMismatchError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "	annex agree {**\r\n"
				+ "		eq test : bool = 1;\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues,"The variable 'test' on the left side of equation is of type 'bool' but must be of type 'int'"));
	}

	@Test
	public void testNameOverlapMultipleSynchOrCalendarStatementError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "system implementation sys.impl\r\n"
				+ "	annex agree {**\r\n"
				+ "		synchrony : latched;\r\n"
				+ "		synchrony : asynchronous;\r\n"
				+ "	**};\r\n"
				+ "end sys.impl;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues,"Multiple synchrony or calender statements in a single contract"));
	}

	@Test
	public void testNameOverlapMultipleInitiallyStatementsError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "	annex agree {**\r\n"
				+ "		initially : true;\r\n"
				+ "		initially : false;\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues,"Multiple initially statements in a single contract"));
	}

	@Test
	public void testNameOverlapMultipleConnectionOverridesError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "system implementation sys.impl\r\n"
				+ "	subcomponents\r\n"
				+ "		this_sensor : device TestSensor;\r\n"
				+ "	connections\r\n"
				+ "		sensor_conn : port this_sensor.data_out -> this_sensor.data_in;\r\n"
				+ "	annex agree {**\r\n"
				+ "		connection sensor_conn : true;\r\n"
				+ "		connection sensor_conn : true;\r\n"
				+ "	**};\r\n"
				+ "end sys.impl;\r\n"
				+ "\r\n"
				+ "device TestSensor\r\n"
				+ "	features\r\n"
				+ "		data_in : in data port;\r\n"
				+ "		data_out : out data port;\r\n"
				+ "end TestSensor;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues,"Multiple connection overrides for connection 'sensor_conn'"));
	}

	@Test
	public void testNameOverlapAlreadyDefinedInComponentTypeContractError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "	annex agree {**\r\n"
				+ "		property test = true;\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "system implementation sys.impl\r\n"
				+ "	annex agree {**\r\n"
				+ "		property test = false;	\r\n"
				+ "	**};\r\n"
				+ "end sys.impl;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues,"'test' already defined in component type contract"));
	}

	@Test
	public void testNodeEqNoErrors() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "	annex agree {**\r\n"
				+ "		node test(data_in : bool) returns (data_out : bool);\r\n"
				+ "		let\r\n"
				+ "			data_out = true;\r\n"
				+ "		tel;\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		assertTrue(issueCollection.getIssues().isEmpty());
	}

	@Ignore
	public void testNodeEqMustBeContainedInANodeBodyError() throws Exception {
		// TODO
	}

	@Ignore
	public void testNodeEqMustBeContainedInANodeDefError() throws Exception {
		// TODO
	}

	@Ignore
	public void testNodeEqLHSMustBeAReturnVarOrLocalVarError() throws Exception {
		// TODO
	}

	@Ignore
	public void testNodeEqSomethingWentWrongWithCycleCheckerError() throws Exception {
		// TODO
	}

	@Ignore
	public void testNodeEqCyclicReferenceToVarError() throws Exception {
		// TODO
	}

	@Ignore
	public void testNodeEqCannotContainRangedValueAndRHSExprError() throws Exception {
		// TODO
	}

	@Ignore
	public void testNodeEqTypeMismatchError() throws Exception {
		// TODO
	}

	@Test
	public void testNodeLemmaNoErrors() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "	annex agree {**\r\n"
				+ "		node test(data_in : bool) returns (data_out : bool);\r\n"
				+ "		let\r\n"
				+ "			data_out = true;\r\n"
				+ "			lemma \"Always True\" : true;\r\n"
				+ "		tel;\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		assertTrue(issueCollection.getIssues().isEmpty());
	}

	@Test
	public void testNodeLemmaMustBeOfTypeBoolError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "	annex agree {**\r\n"
				+ "		node test(data_in : bool) returns (data_out : bool);\r\n"
				+ "		let\r\n"
				+ "			data_out = true;\r\n"
				+ "			lemma \"Always True\" : 1;\r\n"
				+ "		tel;\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues,"Expression for lemma statement is of type 'int' but must be of type 'bool'"));
	}

	@Ignore
	public void testNodeStmtNoErrors() throws Exception {
		// TODO
	}

	@Ignore
	public void testNodeStmtOnlyArgumentsConstantsAndNodeCallsAllowedError() throws Exception {
		// TODO
	}

	@Ignore
	public void testNodeDefNoErrors() throws Exception {
		// TODO
	}

	@Ignore
	public void testNodeDefNotAssignableValueError() throws Exception {
		// TODO
	}

	@Ignore
	public void testNodeDefVariableNeverAssignedError() throws Exception {
		// TODO
	}

	@Ignore
	public void testNodeDefAssignedMultipleTimesError() throws Exception {
		// TODO
	}

	@Test
	public void testLinearizationDefNoErrors() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "	annex agree {**\r\n"
				+ "		linearization test (x : real) over [0.0 .. 10.0] within 0.1: x^2;\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		assertTrue(issueCollection.getIssues().isEmpty());
	}

	@Test
	public void testLinearizationDefLimitedToFunctionsOfOneVariableError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "	annex agree {**\r\n"
				+ "		linearization test (x : real, y: real) over [0.0 .. 10.0, 0.0 .. 10.0] : (x*y)^2;\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues,"Linearization definitions are limited to functions of one variable."));
	}

	@Test
	public void testLinearizationDefArgMustBeOfRealTypeError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "	annex agree {**\r\n"
				+ "		linearization test (x : int) over [0.0 .. 10.0] : x^2;\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues,"Linearizations formal arguments must be of 'real' type, but found type 'int'."));
	}

	@Test
	public void testLinearizationDefNumberOfFormalVarAndIntervalsDoesNotMatchError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "	annex agree {**\r\n"
				+ "		linearization test (x : real) over [0.0 .. 10.0, 0.0 .. 10.0] : x^2;\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues,"Number of formal variables and intervals does not match."));
	}

	@Test
	public void testLinearizationDefPrecisionMustBeOfRealTypeError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "	annex agree {**\r\n"
				+ "		linearization test (x : real) over [0.0 .. 10.0] within 1 : x^2;\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues,"Linearization precision must be of 'real' type, but found type 'int'."));
	}

	@Test
	public void testLinearizationDefPrecisionMustBeConstantExprError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "	annex agree {**\r\n"
				+ "		linearization test (x : real) over [0.0 .. 10.0] within x*0.1 : x^2;\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues, "Linearization precision must be constant expression of 'real' type, found non-constant expression."));
	}

	@Test
	public void testLinearizationIntervalStartMustBeOfRealTypeError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "	annex agree {**\r\n"
				+ "		linearization test (x : real) over [0 .. 10.0] within 0.1 : x^2;\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues,"Linearization interval endpoints must be constant expressions of 'real' type, found type 'int'."));
	}

	@Test
	public void testLinearizationIntervalEndMustBeOfRealTypeError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "	annex agree {**\r\n"
				+ "		linearization test (x : real) over [0.0 .. 10] within 0.1 : x^2;\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues,"Linearization interval endpoints must be constant expressions of 'real' type, found type 'int'."));
	}

	@Test
	public void testLinearizationIntervalStartMustBeConstantError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "	annex agree {**\r\n"
				+ "		linearization test (x : real) over [0.0*x .. 10.0] within 0.1 : x^2;\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues,"Linearization interval endpoints must be constant expressions of 'real' type, found non-constant expression."));
	}

	@Test
	public void testLinearizationIntervalEndMustBeConstantError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "	annex agree {**\r\n"
				+ "		linearization test (x : real) over [0.0 .. 10.0*x] within 0.1 : x^2;\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues,"Linearization interval endpoints must be constant expressions of 'real' type, found non-constant expression."));
	}

	@Test
	public void testGetPropertyExprNoErrors() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "	with SEI;\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "	properties\r\n"
				+ "		SEI::NetWeight => 10.0kg;\r\n"
				+ "	annex agree {**\r\n"
				+ "		const test : real = Get_Property(this, SEI::NetWeight);\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		assertTrue(issueCollection.getIssues().isEmpty());
	}

	@Test
	public void testGetPropertyExprExpectedAADLPropertyOrPropertyConstError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "	with SEI;\r\n"
				+ "	with Base_Types;\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "	features\r\n"
				+ "		data_in : in data port Base_Types::Integer;\r\n"
				+ "	properties\r\n"
				+ "		SEI::NetWeight => 10.0kg;\r\n"
				+ "	annex agree {**\r\n"
				+ "		const test : int = Get_Property(this, data_in);\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues,"Expected AADL property or property constant"));
	}

	@Ignore
	public void testGetPropertyExprPropertyDoesNotApplyError() throws Exception {
		// TODO
	}

	@Test
	public void testPrevExprNoErrors() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "	annex agree {**\r\n"
				+ "		eq test : int = prev(test+1, 0);\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		assertTrue(issueCollection.getIssues().isEmpty());
	}

	@Test
	public void testPrevExprTypeMismatchError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "	annex agree {**\r\n"
				+ "		eq test : int = prev(test+1, 0.0);\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues,"The first and second arguments of the 'prev' function are of non-matching types 'int' and 'real'."));
	}

	@Test
	public void testPrevExprNotAllowedInLinearizationBodyError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "	annex agree {**\r\n"
				+ "		linearization test_ln (x : real) over [0.0 .. 10.0] within 0.1 : prev(x+1.0, 0.0);\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues,"'prev' expressions are not allowed in linearization body expressions."));
	}

	@Test
	public void testPrevExprNotAllowedInConstantExprError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "	annex agree {**\r\n"
				+ "		const test : int = prev(test+1, 0);\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues,"'prev' expressions are not allowed in constant expressions."));
	}

	@Test
	public void testCallExprNoErrors() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "	annex agree {**\r\n"
				+ "		fun test_fn(x : real) : bool = true;\r\n"
				+ "		property test1 = test_fn(1.0);\r\n"
				+ "		node test_node(data_in: bool) returns (data_out: bool);\r\n"
				+ "		let\r\n"
				+ "			data_out = true;\r\n"
				+ "		tel;\r\n"
				+ "		property test2 = test_node(true);	\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		assertTrue(issueCollection.getIssues().isEmpty());
	}

	@Test
	public void testCallExprNodeDefCannotBeAppliedInLinearizationDefError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "	annex agree {**\r\n"
				+ "		node test_node(data_in: bool) returns (data_out: bool);\r\n"
				+ "		let\r\n"
				+ "			data_out = true;\r\n"
				+ "		tel;\r\n"
				+ "		linearization test_ln (x : real) over [0.0 .. 10.0] within 0.1 : test_node(true);	\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues,"Node definitions cannot be applied in a linearization definition"));
	}

	@Test
	public void testCallExprLibraryFndRealError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "	with DReal;\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "	annex agree {**\r\n"
				+ "		eq test : real = DReal::exp(1.23);\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(
				UtilityFunctions.getWarning(issues, "dReal library functions require the use of the dReal solver"));
	}

	@Test
	public void testCallExprLibraryFnCannotBeCalledFromLogicError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "	annex agree {**\r\n"
				+ "		external test_libfn(x : int) : int;\r\n"
				+ "		eq test : int = test_libfn(5);	\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues,"Library functions cannot be called from the logic"));
	}

	@Test
	public void testCallExprFunctionArgumentNumberError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "	annex agree {**\r\n"
				+ "		fun test_fn(x : real) : bool = true;\r\n"
				+ "		property test = test_fn(1.0, 2.0);\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues,"Function definition 'test_fn' requires 1 arguments, but this function call provides 2 arguments"));
	}

	@Test
	public void testCallExprFunctionArgumentTypeMismatchError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "	annex agree {**\r\n"
				+ "		fun test_fn(x : real) : bool = true;\r\n"
				+ "		property test = test_fn(1);\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues,"Argument 0 of function call 'test_fn' is of type 'int' but must be of type 'real'."));
	}

	@Test
	public void testFnDefNoErrors() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "	annex agree {**\r\n"
				+ "		fun test(x : real) : bool = false;\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		assertTrue(issueCollection.getIssues().isEmpty());
	}

	@Test
	public void testFnDefTypeMismatchError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "	annex agree {**\r\n"
				+ "		fun test(x : real) : bool = x;\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues,"Function 'test' is of type 'bool' but its expression is of type 'real'."));
	}

	@Ignore
	public void testAbstractionNoErrors() throws Exception {
		// TODO
	}

	@Ignore
	public void testAbstractionRecursiveDependencyError() throws Exception {
		// TODO
	}

	@Test
	public void testIfThenElseExprNoErrors() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "	annex agree {**\r\n"
				+ "		property test = if true then false else true;\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		assertTrue(issueCollection.getIssues().isEmpty());
	}

	@Test
	public void testIfThenElseExprNotAllowedInLinearizationBodyExprError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "	annex agree {**\r\n"
				+ "		linearization test_ln (x : real) over [0.0 .. 10.0] within 0.1 : if x < 5.0 then x + 1.0 else x * 2.0;\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues,"If-then-else expressions not allowed in linearization body expressions"));
	}

	@Test
	public void testIfThenElseExprConditionMustBeOfTypeBoolError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "	annex agree {**\r\n"
				+ "		property test = if 1 then false else true;\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues,"The condition of the if statement is of type 'int' but must be of type 'bool'"));
	}

	@Test
	public void testIfThenElseExprThenElseTypeMismatchError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "	annex agree {**\r\n"
				+ "		property test = if true then false else 0;\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues,"The 'then' and 'else' expressions are of non-matching types 'bool' and 'int'"));
	}

	@Test
	public void testPreExprNoErrors() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "	annex agree {**\r\n"
				+ "		eq test : int = 0 -> pre(test+1);\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		assertTrue(issueCollection.getIssues().isEmpty());
	}

	@Test
	public void testPreExprNotAllowedInLinearizationBodyError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "	annex agree {**\r\n"
				+ "		linearization test_ln (x : real) over [0.0 .. 10.0] within 0.1 : 0 -> pre(x+1.0);\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues,"'pre' expressions are not allowed in linearization body expressions."));
	}

	@Test
	public void testPreExprNotAllowedInConstantExprError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "	annex agree {**\r\n"
				+ "		const test : int = pre(test+1);\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues,"'pre' expressions are not allowed in constant expressions."));
	}

	@Test
	public void testBinaryExprNoErrors() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "	annex agree {**\r\n"
				+ "		eq test1 : bool = true -> false;\r\n"
				+ "		eq test2 : bool = true => true;\r\n"
				+ "		eq test3 : bool = true <=> true;\r\n"
				+ "		eq test4 : bool = true and true;\r\n"
				+ "		eq test5 : bool = true or false;\r\n"
				+ "		eq test6 : bool = 1 = 2;\r\n"
				+ "		eq test7 : bool = 1 <> 1;\r\n"
				+ "		eq test8 : bool = 1 != 1;\r\n"
				+ "		eq test9 : bool = 3.4 < 4.0;\r\n"
				+ "		eq test10 : bool = 3.4 <= 4.0;\r\n"
				+ "		eq test11 : bool = 3.4 > 4.0;\r\n"
				+ "		eq test12 : bool = 3.4 >= 4.0;\r\n"
				+ "		eq test13 : int = 4 + 5;\r\n"
				+ "		eq test14 : int = 4 - 5;\r\n"
				+ "		eq test15 : int = 4 * 5;\r\n"
				+ "		eq test16 : int = 20 mod 5;\r\n"
				+ "		eq test17 : int = 20 div 4;\r\n"
				+ "		eq test18 : real = 15.0 / 3.0;\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		assertTrue(issueCollection.getIssues().isEmpty());
	}

	@Test
	public void testBinaryExprArrowExprNotAllowedInLinearizationBodyExprError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "	annex agree {**\r\n"
				+ "		linearization test (x : real) over [0.0 .. 10.0] within 0.1: true -> false;\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues,"Arrow '->' expressions are not allowed in linearization body expressions."));
	}

	@Test
	public void testBinaryExprArrowExprNotAllowedInConstantExprError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "	annex agree {**\r\n"
				+ "		const test : int = 0 -> pre(test+1);\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues,"Arrow '->' expressions are not allowed in constant expressions."));
	}

	@Test
	public void testBinaryExprArrowExprLHSAndRHSTypeMismatchError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "	annex agree {**\r\n"
				+ "		eq test : bool = true -> 0;\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues, "left and right sides of binary expression '->' are of type 'bool' and 'int', but must be of the same type"));
	}

	@Test
	public void testBinaryExprLogicalExprNotAllowedInLinearizationBodyExprError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "	annex agree {**\r\n"
				+ "		linearization test (x : real) over [0.0 .. 10.0] within 0.1: true or false;\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues,"Logical expressions (like 'or') are not allowed in linearization body expressions."));
	}

	@Test
	public void testBinaryExprLogicalExprLHSMustBeOfTypeBoolError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "	annex agree {**\r\n"
				+ "		eq test : bool = 0.0 <=> false;\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues,"left side of binary expression '<=>' is of type 'real' but must be of type 'bool'"));
	}

	@Test
	public void testBinaryExprLogicalExprRHSMustBeOfTypeBoolError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "	annex agree {**\r\n"
				+ "		eq test : bool = true and 1;\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues,"right side of binary expression 'and' is of type 'int' but must be of type 'bool'"));
	}

	@Test
	public void testBinaryExprLogicalComparisonExprNotAllowedInLinearizationBodyExprError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "	annex agree {**\r\n"
				+ "		linearization test (x : real) over [0.0 .. 10.0] within 0.1: 1 = 1;\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues,"Logical comparison expressions (like '=') are not allowed in linearization body expressions."));
	}

	@Test
	public void testBinaryExprLogicalComparisonExprTypeMismatchError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "	annex agree {**\r\n"
				+ "		eq test : bool = 0.0 != 0;\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues,
				"left and right sides of binary expression '!=' are of type 'real' and 'int', but must be of the same type"));
	}

	@Test
	public void testBinaryExprComparisonExprNotAllowedInLinearizationBodyExprError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "	annex agree {**\r\n"
				+ "		linearization test (x : real) over [0.0 .. 10.0] within 0.1: 3.4 < 4.0;\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues,"Comparison expressions (like '<') are not allowed in linearization body expressions."));
	}

	@Test
	public void testBinaryExprComparisonExprTypeMismatchError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "	annex agree {**\r\n"
				+ "		eq test : bool = 1 >= 0.5;\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues,"left and right sides of binary expression '>=' are of type 'int' and 'real', but must be of the same type"));
	}

	@Test
	public void testBinaryExprComparisonExprLHSMustBeOfTypeIntOrRealError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "	annex agree {**\r\n"
				+ "		eq test : bool = true > 1;\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues,"left side of binary expression '>' is of type 'bool' but must be of type 'int' or 'real'"));
	}

	@Test
	public void testBinaryExprComparisonExprRHSMustBeOfTypeIntOrRealError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "	annex agree {**\r\n"
				+ "		eq test : bool = 0 < false;\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues,"right side of binary expression '<' is of type 'bool' but must be of type 'int' or 'real'"));
	}

	@Test
	public void testBinaryExprAddSubMultExprTypeMismatchError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "	annex agree {**\r\n"
				+ "		eq test : int = 1 + 2.2;\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues,"left and right sides of binary expression '+' are of type 'int' and 'real', but must be of the same type"));
	}

	@Test
	public void testBinaryExprAddSubMultExprLHSMustBeOfTypeIntOrRealError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "	annex agree {**\r\n"
				+ "		eq test : int = true + 2;\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues,"left side of binary expression '+' is of type 'bool' but must be of type 'int' or 'real'"));
	}

	@Test
	public void testBinaryExprAddSubMultExprRHSMustBeOfTypeIntOrRealError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "	annex agree {**\r\n"
				+ "		eq test : real = 0.0 * false;\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues,"right side of binary expression '*' is of type 'bool' but must be of type 'int' or 'real'"));
	}

	@Test
	public void testBinaryExprAddSubMultExprNotConstantWarning() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "	with Base_Types;\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "	features\r\n"
				+ "		data_in : in data port Base_Types::Integer;\r\n"
				+ "	annex agree {**\r\n"
				+ "		eq inc_test : int = prev(inc_test + 1, 0);\r\n"
				+ "		eq test : int = data_in*inc_test;\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getWarning(issues,"neither the right nor the left side of binary expression '*' is constant'.  Non-linear expressions are allowed only with z3 and dReal."
				+ " With z3 they are not recomended."));
	}

	@Test
	public void testBinaryExprModDivExprNotAllowedInLinearizationBodyExprError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "	annex agree {**\r\n"
				+ "		linearization test (x : real) over [0.0 .. 10.0] within 0.1: 20 mod 5;\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues,"Integer operation expressions (like 'mod') are not allowed in linearization body expressions."));
	}

	@Test
	public void testBinaryExprModDivExprLHSMustBeOfTypeIntError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "	annex agree {**\r\n"
				+ "		eq test : real = 20.0 mod 4;\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues,"left side of binary expression 'mod' is of type 'real' but must be of type 'int'"));
	}

	@Test
	public void testBinaryExprModDivExprRHSMustBeOfTypeIntError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "	annex agree {**\r\n"
				+ "		eq test : int = 50 div true;\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues,"right side of binary expression 'div' is of type 'bool' but must be of type 'int'"));
	}

	@Test
	public void testBinaryExprModDivRHSNotConstantWarning() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "	with Base_Types;\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "	features\r\n"
				+ "		data_in : in data port Base_Types::Integer;\r\n"
				+ "	annex agree {**\r\n"
				+ "		eq test : int = 50 div data_in;\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getWarning(issues, "right side of binary expression 'div' is not constant."
				+ " Non-linear expressions are allowed only with z3."
				+ " Even with z3 they are not recomended..."));
	}

	@Test
	public void testBinaryExprDivideExprLHSMustBeOfTypeRealError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "	annex agree {**\r\n"
				+ "		eq test : int = 20 / 1.0;\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues,"left side of binary expression '/' is of type 'int' but must be of type 'real'"));
	}

	@Test
	public void testBinaryExprDivideExprRHSMustBeOfTypeRealError() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "	annex agree {**\r\n"
				+ "		eq test : real = 50.0 / 4;\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues,"right side of binary expression '/' is of type 'int' but must be of type 'real'"));
	}

	@Test
	public void testBinaryExprDivideExprRHSNotConstantWarning() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "	with Base_Types;\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "	features\r\n"
				+ "		data_in : in data port Base_Types::Float;\r\n"
				+ "	annex agree {**\r\n"
				+ "		eq test : real = 50.0 / data_in;\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getWarning(issues, "right side of binary expression '/' is not constant."
				+ " Non-linear expressions are allowed only with z3."
				+ " Even with z3 they are not recomended..."));
	}

}