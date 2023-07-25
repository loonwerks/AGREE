# AGREE Tests
This is the test regression suite for AGREE. There are currently two different types of tests defined in com.rockwellcollins.atc.agree.tests: parsing and validation tests.

The [UtilityFunctions.java](https://github.com/loonwerks/AGREE/blob/master/com.rockwellcollins.atc.agree.tests/src/com/rockwellcollins/atc/agree/tests/UtilityFunctions.java) file has utility functions defined to aid in parsing the EMF model with java (compared to using Xtend). Detailed descriptions of each utility function are included in the comments of the file. When new tests are created, more utility functions can be added as necessary.

## Parsing AGREE Tests
The parsing tests are declared in [AgreeParsingTest.java](https://github.com/loonwerks/AGREE/blob/master/com.rockwellcollins.atc.agree.tests/src/com/rockwellcollins/atc/agree/tests/AgreeParsingTest.java). This file contains unit tests that test the grammar as defined in [Agree.xtext](https://github.com/loonwerks/AGREE/tree/intern-regression-suite/com.rockwellcollins.atc.agree/src/com/rockwellcollins/atc/agree). 

### Adding a New Parsing Test
[AgreeParsingTest.java](https://github.com/loonwerks/AGREE/blob/master/com.rockwellcollins.atc.agree.tests/src/com/rockwellcollins/atc/agree/tests/AgreeParsingTest.java) is organized in the same order as [Agree.xtext](https://github.com/loonwerks/AGREE/tree/intern-regression-suite/com.rockwellcollins.atc.agree/src/com/rockwellcollins/atc/agree) with one large example test at the end (i.e., Car); therefore, insert new tests where logically appropriate.

To insert a new test, insert `@Test` and then define the test as `public void test{GrammarElement}() throws Exception{...}` (e.g., `public void testImpliesExpr() throws Exception{...}`). Every test should like like this (or something very similar) with its own specific `String test` and testing functionality indicated by `...`:

```
@Test
public void testAssumeStatement() throws Exception {
   String test =  "package TestPackage\r\n"
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
   EObject aadl_package_impl = issueCollection.getResource().getContents().get(0);
   EObject pub_sec = UtilityFunctions.getownedPublicSection(aadl_package_impl);
   EObject owned_classifiers = UtilityFunctions.getownedClassifier(pub_sec, "sys");
   EObject annex_subclause = UtilityFunctions.getAnnexSubclause(owned_classifiers, "agree");
   EObject parsed_annex_subclause = UtilityFunctions.getParsedAnnexSubclause(annex_subclause);
   EObject contract = UtilityFunctions.getContract(parsed_annex_subclause);
   assertNotNull(contract);
   assertTrue(contract instanceof AgreeContract);
   EObject spec = UtilityFunctions.getSpec(contract, 0);
   assertNotNull(spec);
   ...
}
```
For parsing, these unit tests are checking if the correct instances/values are getting stuffed correctly in the EMF model. The goal is to keep these unit tests as simple as possible to test individual elements of the AGREE grammar. For the larger Car test, the goal was to test a realistic grammar usage example.

## Validation AGREE Tests
The validation tests are declared in [AgreeValidationTest.java](https://github.com/loonwerks/Agree/blob/master/com.rockwellcollins.atc.agree.tests/src/com/rockwellcollins/atc/agree/tests/AgreeValidationTest.java). These file contains unit tests that test the validation checks as defined in [AgreeValidator.java](https://github.com/loonwerks/AGREE/blob/master/com.rockwellcollins.atc.agree/src/com/rockwellcollins/atc/agree/validation/AgreeValidator.java). 

### Adding a New Validation Test
[AgreeValidationTest.java](https://github.com/loonwerks/AGREE/blob/master/com.rockwellcollins.atc.agree.tests/src/com/rockwellcollins/atc/agree/tests/AgreeValidationTest.java) is organized in the same order as [AgreeValidator.java](https://github.com/loonwerks/AGREE/blob/master/com.rockwellcollins.atc.agree/src/com/rockwellcollins/atc/agree/validation/AgreeValidator.java); therefore, insert new tests where logically appropriate.

To insert a new test, insert `@Test` and then define the test as `public void test{GrammarElement}{ValidationCheckDescription}() throws Exception{...}` (e.g., `public void testProveStatementMustContainClaimError() throws Exception{...}`). Every test should like like this (or something very similar) with its own specific `String test` and testing functionality indicated by `...`:

```
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
   ...
}
```

For each validation error check, there is exactly one accompanying `public void test{GrammarElement}NoErrors() throws Exception{...}` (e.g., `public void testProveStatementNoError() throws Exception{...}`) declared beforehand. Then, each validation error check has exactly one unit test (i.e., no more than one validation check can be tested per unit test). 

To check if no errors are to be thrown the `...` above in the example unit test will become the following:
```
assertTrue(issueCollection.getIssues().isEmpty());
```
If an error is supposed to be generated, `...` will look like the following:
```
List<Issue> issues = issueCollection.getIssues();
assertFalse(issues.isEmpty());
assertNotNull(UtilityFunctions.getError(issues, "Prove statements must contain a claim"));
```

## Add a New Test File
Adding a new test file is easy! Just create a new file in the [src/com.rockwellcollins.atc.agree.tests](https://github.com/loonwerks/AGREE/tree/master/com.rockwellcollins.atc.agree.tests/src/com/rockwellcollins/atc/agree/tests) that following the naming convention Agree{TestDescription}Test.java. Follow the parsing and validation tests for reference, and make sure to do the following in the new test file:
   - include `@RunWith(XtextRunner.class)` and `@InjectWith(AgreeInjectorProvider.class)` before the class definition
   - the new class `extends XTextTest`
   - insert `@Inject TestHelper<AgreePackage> testHelper;` in the class file

## Run Tests
Tests can be run in two ways:
   1. In Eclipse,
      - Open the AGREE project in Eclipse
      - Right click on com.rockwellcollins.atc.agree.tests
      - Select `Run As` > `JUnit Plug-in Test`
   2. Using the tycho-surefire plugin
      - Run `mvn clean verify` on the entire AGREE project 
         * This is done automatically through the "Build and Test Project" GitHub Action, which is triggered whenever a push is made to master or a pull request is opened, reopened, or editted


