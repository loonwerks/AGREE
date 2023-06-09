package com.rockwellcollins.atc.agree.tests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.eclipse.xtext.testing.InjectWith;
import org.eclipse.xtext.testing.XtextRunner;
import org.eclipse.xtext.validation.Issue;
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
	public void testConstStatementTypeMismatch() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n" + "with Base_Types;\r\n" + "\r\n" + "annex agree {**\r\n"
				+ "	const TEST_VAL: real = 10; -- should throw an error;\r\n"
				+ "**};\r\n"
				+ "\r\n"
				+ "system A\r\n" + "	features\r\n" + "		Input_Val: in data port Base_Types::Float;\r\n"
				+ "		Output_Val: out data port Base_Types::Float;\r\n" + "	annex agree {**\r\n"
				+ "		assume A1 \"\" : Input_Val < 20; -- should throw an error\r\n" + "	**};\r\n" + "end A;\r\n"
				+ "\r\n"
				+ "system S\r\n" + "	features\r\n" + "		Input_Val: in data port Base_Types::Float;\r\n"
				+ "		Output_Val: out data port Base_Types::Float;\r\n" + "	annex agree {**\r\n"
				+ "		assume A1 \"\" : Input_Val < TEST_VAL;\r\n"
				+ "	**};\r\n"
				+ "end S;\r\n"
				+ "\r\n"
				+ "system implementation S.impl\r\n" + "	subcomponents\r\n" + "		A: system A;\r\n"
				+ "	connections\r\n" + "		c1_a: port Input_Val -> A.Input_Val; \r\n"
				+ "		c2_a: port A.Output_Val -> Output_Val;\r\n" + "end S.impl;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		List<Issue> issues = issueCollection.getIssues();
		assertFalse(issues.isEmpty());
		assertNotNull(UtilityFunctions.getError(issues,
				"The assumed type of constant statement 'TEST_VAL' is 'RealTypeDef' but the actual type is 'IntTypeDef'"));
		assertNotNull(UtilityFunctions.getError(issues,
				"left and right sides of binary expression '<' are of type 'real' and 'int', but must be of the same type"));
	}
}