package com.rockwellcollins.atc.agree.tests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.testing.InjectWith;
import org.eclipse.xtext.testing.XtextRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.osate.aadl2.BooleanLiteral;
import org.osate.aadl2.ConnectedElement;
import org.osate.aadl2.DataImplementation;
import org.osate.aadl2.DataPort;
import org.osate.aadl2.DataSubcomponent;
import org.osate.aadl2.DataType;
import org.osate.aadl2.DeviceSubcomponent;
import org.osate.aadl2.DeviceType;
import org.osate.aadl2.EventDataPort;
import org.osate.aadl2.EventPort;
import org.osate.aadl2.Property;
import org.osate.testsupport.TestHelper;

import com.google.inject.Inject;
import com.itemis.xtext.testing.FluentIssueCollection;
import com.itemis.xtext.testing.XtextTest;
import com.rockwellcollins.atc.agree.agree.AgreeContract;
import com.rockwellcollins.atc.agree.agree.AgreeLibrary;
import com.rockwellcollins.atc.agree.agree.AgreePackage;
import com.rockwellcollins.atc.agree.agree.AgreeSubclause;
import com.rockwellcollins.atc.agree.agree.AlwaysStatement;
import com.rockwellcollins.atc.agree.agree.ArrayLiteralExpr;
import com.rockwellcollins.atc.agree.agree.ArraySubExpr;
import com.rockwellcollins.atc.agree.agree.ArrayType;
import com.rockwellcollins.atc.agree.agree.ArrayUpdateExpr;
import com.rockwellcollins.atc.agree.agree.AssertStatement;
import com.rockwellcollins.atc.agree.agree.AssignStatement;
import com.rockwellcollins.atc.agree.agree.AssumeStatement;
import com.rockwellcollins.atc.agree.agree.AsynchStatement;
import com.rockwellcollins.atc.agree.agree.BinaryExpr;
import com.rockwellcollins.atc.agree.agree.BoolLitExpr;
import com.rockwellcollins.atc.agree.agree.CalenStatement;
import com.rockwellcollins.atc.agree.agree.CallExpr;
import com.rockwellcollins.atc.agree.agree.ClosedTimeInterval;
import com.rockwellcollins.atc.agree.agree.ConnectionStatement;
import com.rockwellcollins.atc.agree.agree.ConstStatement;
import com.rockwellcollins.atc.agree.agree.DoubleDotRef;
import com.rockwellcollins.atc.agree.agree.EnumLitExpr;
import com.rockwellcollins.atc.agree.agree.EnumStatement;
import com.rockwellcollins.atc.agree.agree.EqStatement;
import com.rockwellcollins.atc.agree.agree.EventExpr;
import com.rockwellcollins.atc.agree.agree.ExistsExpr;
import com.rockwellcollins.atc.agree.agree.FlatmapExpr;
import com.rockwellcollins.atc.agree.agree.FloorCast;
import com.rockwellcollins.atc.agree.agree.FnDef;
import com.rockwellcollins.atc.agree.agree.FoldLeftExpr;
import com.rockwellcollins.atc.agree.agree.FoldRightExpr;
import com.rockwellcollins.atc.agree.agree.ForallExpr;
import com.rockwellcollins.atc.agree.agree.GetPropertyExpr;
import com.rockwellcollins.atc.agree.agree.GuaranteeStatement;
import com.rockwellcollins.atc.agree.agree.IfThenElseExpr;
import com.rockwellcollins.atc.agree.agree.IndicesExpr;
import com.rockwellcollins.atc.agree.agree.InitialStatement;
import com.rockwellcollins.atc.agree.agree.InputStatement;
import com.rockwellcollins.atc.agree.agree.IntLitExpr;
import com.rockwellcollins.atc.agree.agree.LatchedExpr;
import com.rockwellcollins.atc.agree.agree.LatchedStatement;
import com.rockwellcollins.atc.agree.agree.LemmaStatement;
import com.rockwellcollins.atc.agree.agree.LibraryFnDef;
import com.rockwellcollins.atc.agree.agree.LiftContractStatement;
import com.rockwellcollins.atc.agree.agree.LinearizationDef;
import com.rockwellcollins.atc.agree.agree.LinearizationInterval;
import com.rockwellcollins.atc.agree.agree.MNSynchStatement;
import com.rockwellcollins.atc.agree.agree.NamedElmExpr;
import com.rockwellcollins.atc.agree.agree.NamedID;
import com.rockwellcollins.atc.agree.agree.NodeBodyExpr;
import com.rockwellcollins.atc.agree.agree.NodeDef;
import com.rockwellcollins.atc.agree.agree.NodeEq;
import com.rockwellcollins.atc.agree.agree.NodeLemma;
import com.rockwellcollins.atc.agree.agree.OpenLeftTimeInterval;
import com.rockwellcollins.atc.agree.agree.OpenRightTimeInterval;
import com.rockwellcollins.atc.agree.agree.OpenTimeInterval;
import com.rockwellcollins.atc.agree.agree.OrderStatement;
import com.rockwellcollins.atc.agree.agree.ParamStatement;
import com.rockwellcollins.atc.agree.agree.PeriodicStatement;
import com.rockwellcollins.atc.agree.agree.PreExpr;
import com.rockwellcollins.atc.agree.agree.PrevExpr;
import com.rockwellcollins.atc.agree.agree.PrimType;
import com.rockwellcollins.atc.agree.agree.PropertyStatement;
import com.rockwellcollins.atc.agree.agree.ReachableStatement;
import com.rockwellcollins.atc.agree.agree.RealCast;
import com.rockwellcollins.atc.agree.agree.RealLitExpr;
import com.rockwellcollins.atc.agree.agree.RecordDef;
import com.rockwellcollins.atc.agree.agree.RecordLitExpr;
import com.rockwellcollins.atc.agree.agree.RecordUpdateExpr;
import com.rockwellcollins.atc.agree.agree.SelectionExpr;
import com.rockwellcollins.atc.agree.agree.SporadicStatement;
import com.rockwellcollins.atc.agree.agree.SynchStatement;
import com.rockwellcollins.atc.agree.agree.TagExpr;
import com.rockwellcollins.atc.agree.agree.ThisRef;
import com.rockwellcollins.atc.agree.agree.TimeExpr;
import com.rockwellcollins.atc.agree.agree.TimeFallExpr;
import com.rockwellcollins.atc.agree.agree.TimeOfExpr;
import com.rockwellcollins.atc.agree.agree.TimeRiseExpr;
import com.rockwellcollins.atc.agree.agree.UnaryExpr;
import com.rockwellcollins.atc.agree.agree.UninterpretedFnDef;
import com.rockwellcollins.atc.agree.agree.WhenHoldsStatement;
import com.rockwellcollins.atc.agree.agree.WhenOccursStatment;
import com.rockwellcollins.atc.agree.agree.WheneverBecomesTrueStatement;
import com.rockwellcollins.atc.agree.agree.WheneverHoldsStatement;
import com.rockwellcollins.atc.agree.agree.WheneverImpliesStatement;
import com.rockwellcollins.atc.agree.agree.WheneverOccursStatement;

@RunWith(XtextRunner.class)
@InjectWith(AgreeInjectorProvider.class)
public class AgreeParsingTest extends XtextTest {

	@Inject
	TestHelper<AgreePackage> testHelper;

	@Test
	public void testAnnexLibrary() throws Exception{
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "annex agree{**\r\n"
				+ "**};\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection= testHelper.testString(test);
		assertNotNull(issueCollection);
		EObject aadl_package_impl = issueCollection.getResource().getContents().get(0);
		assertNotNull(aadl_package_impl);
		assertTrue(aadl_package_impl.getClass().toString().contains("AadlPackageImpl"));
		assertTrue(UtilityFunctions.getStringProperty(aadl_package_impl, "name").equals("TestPackage"));
		EObject pub_sec = UtilityFunctions.getownedPublicSection(aadl_package_impl);
		assertNotNull(pub_sec);
		EObject annex_lib = UtilityFunctions.getAnnexLibrary(pub_sec, "agree");
		assertNotNull(annex_lib);
		EObject parsed_annex_lib = UtilityFunctions.getParsedAnnexLibrary(annex_lib);
		assertNotNull(parsed_annex_lib);
		assertTrue(parsed_annex_lib instanceof AgreeLibrary);
	}

	@Test
	public void testAnnexSubclause() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "		annex agree{**\r\n"
				+ "		**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		assertNotNull(issueCollection);
		EObject aadl_package_impl = issueCollection.getResource().getContents().get(0);
		assertNotNull(aadl_package_impl);
		assertTrue(aadl_package_impl.getClass().toString().contains("AadlPackageImpl"));
		assertTrue(UtilityFunctions.getStringProperty(aadl_package_impl, "name").equals("TestPackage"));
		EObject pub_sec = UtilityFunctions.getownedPublicSection(aadl_package_impl);
		assertNotNull(pub_sec);
		EObject owned_classifiers = UtilityFunctions.getownedClassifier(pub_sec, "sys");
		assertNotNull(owned_classifiers);
		EObject annex_subclause = UtilityFunctions.getAnnexSubclause(owned_classifiers, "agree");
		assertNotNull(annex_subclause);
		EObject parsed_annex_subclause = UtilityFunctions.getParsedAnnexSubclause(annex_subclause);
		assertNotNull(parsed_annex_subclause);
		assertTrue(parsed_annex_subclause instanceof AgreeSubclause);
	}

	@Test
	public void testInitialStatement() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "	annex agree{**\r\n"
				+ "		initially : true;\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "system implementation sys.impl\r\n"
				+ "end sys.impl;\r\n"
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
		assertTrue(spec instanceof InitialStatement);
		EObject expr = UtilityFunctions.getExpr(spec);
		assertNotNull(expr);
		assertTrue(expr instanceof BoolLitExpr);
		EObject expr_val = UtilityFunctions.getVal(expr);
		assertNotNull(expr_val);
		assertTrue(expr_val instanceof BooleanLiteral);
		assertTrue(Boolean.parseBoolean(UtilityFunctions.getStringProperty(expr_val, "value")));
	}

	@Test
	public void testParamStatement() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "	annex agree{**\r\n"
				+ "		parameter 3 : int;\r\n"
				+ "	**};\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "system implementation sys.impl\r\n"
				+ "end sys.impl;\r\n"
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
		assertTrue(spec instanceof ParamStatement);
		EObject spec_type = UtilityFunctions.getType(spec);
		assertNotNull(spec_type);
		assertTrue(spec_type instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(spec_type, "name").equals("int"));
		EObject expr = UtilityFunctions.getExpr(spec);
		assertNotNull(expr);
		assertTrue(expr instanceof IntLitExpr);
		assertTrue(Integer.parseInt(UtilityFunctions.getStringProperty(expr, "val")) == 3);
	}

	@Test
	public void testLiftContractStatement() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "system implementation sys.impl\r\n"
				+ "	subcomponents\r\n"
				+ "		this_sensor: device TestSensor;\r\n"
				+ "	annex agree{**\r\n"
				+ "		lift contract;\r\n"
				+ "	**};\r\n"
				+ "end sys.impl;\r\n"
				+ "\r\n"
				+ "device TestSensor\r\n"
				+ "end TestSensor;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		EObject aadl_package_impl = issueCollection.getResource().getContents().get(0);
		EObject pub_sec = UtilityFunctions.getownedPublicSection(aadl_package_impl);
		EObject owned_classifiers = UtilityFunctions.getownedClassifier(pub_sec, "sys.impl");
		EObject annex_subclause = UtilityFunctions.getAnnexSubclause(owned_classifiers, "agree");
		EObject parsed_annex_subclause = UtilityFunctions.getParsedAnnexSubclause(annex_subclause);
		EObject contract = UtilityFunctions.getContract(parsed_annex_subclause);
		assertNotNull(contract);
		assertTrue(contract instanceof AgreeContract);
		EObject spec = UtilityFunctions.getSpec(contract, 0);
		assertNotNull(spec);
		assertTrue(spec instanceof LiftContractStatement);
	}

	@Test
	public void testConnectionStatement() throws Exception {
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
				+ "		connection sensor_conn : true;\r\n"
				+ "	**};\r\n"
				+ "end sys.impl;\r\n"
				+ "\r\n"
				+ "device TestSensor\r\n"
				+ "	features\r\n"
				+ "		data_in: in event port;\r\n"
				+ "		data_out: out event port;\r\n"
				+ "end TestSensor;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		EObject aadl_package_impl = issueCollection.getResource().getContents().get(0);
		EObject pub_sec = UtilityFunctions.getownedPublicSection(aadl_package_impl);
		EObject owned_classifiers = UtilityFunctions.getownedClassifier(pub_sec, "sys.impl");
		EObject annex_subclause = UtilityFunctions.getAnnexSubclause(owned_classifiers, "agree");
		EObject parsed_annex_subclause = UtilityFunctions.getParsedAnnexSubclause(annex_subclause);
		EObject contract = UtilityFunctions.getContract(parsed_annex_subclause);
		assertNotNull(contract);
		assertTrue(contract instanceof AgreeContract);
		EObject spec = UtilityFunctions.getSpec(contract, 0);
		assertNotNull(spec);
		assertTrue(spec instanceof ConnectionStatement);
		EObject conn = UtilityFunctions.getConn(spec);
		assertNotNull(conn);
		assertTrue(UtilityFunctions.getStringProperty(conn, "name").equals("sensor_conn"));
		EObject source = UtilityFunctions.getSource(conn);
		assertNotNull(source);
		assertTrue(source instanceof ConnectedElement);
		EObject source_connectionEnd = UtilityFunctions.getConnectionEnd(source);
		assertNotNull(source_connectionEnd);
		assertTrue(source_connectionEnd instanceof EventPort);
		assertTrue(UtilityFunctions.getStringProperty(source_connectionEnd, "name").equals("data_out"));
		EObject destination = UtilityFunctions.getDestination(conn);
		assertNotNull(destination);
		assertTrue(destination instanceof ConnectedElement);
		EObject destination_connectionEnd = UtilityFunctions.getConnectionEnd(destination);
		assertNotNull(destination_connectionEnd);
		assertTrue(destination_connectionEnd instanceof EventPort);
		assertTrue(UtilityFunctions.getStringProperty(destination_connectionEnd, "name").equals("data_in"));
		EObject expr = UtilityFunctions.getExpr(spec);
		assertNotNull(expr);
		assertTrue(expr instanceof BoolLitExpr);
		EObject expr_val = UtilityFunctions.getVal(expr);
		assertNotNull(expr_val);
		assertTrue(expr_val instanceof BooleanLiteral);
		assertTrue(Boolean.parseBoolean(UtilityFunctions.getStringProperty(expr_val, "value")));
	}

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
		assertTrue(spec instanceof AssumeStatement);
		assertTrue(UtilityFunctions.getStringProperty(spec, "name").equals("A1"));
		assertTrue(UtilityFunctions.getStringProperty(spec, "str").equals("SimpleTest"));
		EObject expr = UtilityFunctions.getExpr(spec);
		assertNotNull(expr);
		assertTrue(expr instanceof BoolLitExpr);
		EObject expr_val = UtilityFunctions.getVal(expr);
		assertNotNull(expr_val);
		assertTrue(expr_val instanceof BooleanLiteral);
		assertTrue(Boolean.parseBoolean(UtilityFunctions.getStringProperty(expr_val, "value")));
	}

	@Test
	public void testGuaranteeStatement() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "	annex agree{**\r\n"
				+ "		guarantee G1 \"SimpleTest\" : true;\r\n"
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
		assertTrue(spec instanceof GuaranteeStatement);
		assertTrue(UtilityFunctions.getStringProperty(spec, "name").equals("G1"));
		assertTrue(UtilityFunctions.getStringProperty(spec, "str").equals("SimpleTest"));
		EObject expr = UtilityFunctions.getExpr(spec);
		assertNotNull(expr);
		assertTrue(expr instanceof BoolLitExpr);
		EObject expr_val = UtilityFunctions.getVal(expr);
		assertNotNull(expr_val);
		assertTrue(expr_val instanceof BooleanLiteral);
		assertTrue(Boolean.parseBoolean(UtilityFunctions.getStringProperty(expr_val, "value")));
	}

	@Test
	public void testAssertStatement() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "end sys;\r\n" + "\r\n"
				+ "system implementation sys.impl\r\n"
				+ "	annex agree{**\r\n"
				+ "		assert AS1 \"SimpleTest\" : true;\r\n"
				+ "	**};\r\n"
				+ "end sys.impl;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		EObject aadl_package_impl = issueCollection.getResource().getContents().get(0);
		EObject pub_sec = UtilityFunctions.getownedPublicSection(aadl_package_impl);
		EObject owned_classifiers = UtilityFunctions.getownedClassifier(pub_sec, "sys.impl");
		EObject annex_subclause = UtilityFunctions.getAnnexSubclause(owned_classifiers, "agree");
		EObject parsed_annex_subclause = UtilityFunctions.getParsedAnnexSubclause(annex_subclause);
		EObject contract = UtilityFunctions.getContract(parsed_annex_subclause);
		assertNotNull(contract);
		assertTrue(contract instanceof AgreeContract);
		EObject spec = UtilityFunctions.getSpec(contract, 0);
		assertNotNull(spec);
		assertTrue(spec instanceof AssertStatement);
		assertTrue(UtilityFunctions.getStringProperty(spec, "name").equals("AS1"));
		assertTrue(UtilityFunctions.getStringProperty(spec, "str").equals("SimpleTest"));
		EObject expr = UtilityFunctions.getExpr(spec);
		assertNotNull(expr);
		assertTrue(expr instanceof BoolLitExpr);
		EObject expr_val = UtilityFunctions.getVal(expr);
		assertNotNull(expr_val);
		assertTrue(expr_val instanceof BooleanLiteral);
		assertTrue(Boolean.parseBoolean(UtilityFunctions.getStringProperty(expr_val, "value")));
	}

	@Test
	public void testLemmaStatement() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "	\r\n"
				+ "system sys\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "system implementation sys.impl\r\n"
				+ "	annex agree{**\r\n"
				+ "		lemma L1 \"SimpleTest\" : true;\r\n"
				+ "	**};\r\n"
				+ "end sys.impl;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		EObject aadl_package_impl = issueCollection.getResource().getContents().get(0);
		EObject pub_sec = UtilityFunctions.getownedPublicSection(aadl_package_impl);
		EObject owned_classifiers = UtilityFunctions.getownedClassifier(pub_sec, "sys.impl");
		EObject annex_subclause = UtilityFunctions.getAnnexSubclause(owned_classifiers, "agree");
		EObject parsed_annex_subclause = UtilityFunctions.getParsedAnnexSubclause(annex_subclause);
		EObject contract = UtilityFunctions.getContract(parsed_annex_subclause);
		assertNotNull(contract);
		assertTrue(contract instanceof AgreeContract);
		EObject spec = UtilityFunctions.getSpec(contract, 0);
		assertNotNull(spec);
		assertTrue(spec instanceof LemmaStatement);
		assertTrue(UtilityFunctions.getStringProperty(spec, "name").equals("L1"));
		assertTrue(UtilityFunctions.getStringProperty(spec, "str").equals("SimpleTest"));
		EObject expr = UtilityFunctions.getExpr(spec);
		assertNotNull(expr);
		assertTrue(expr instanceof BoolLitExpr);
		EObject expr_val = UtilityFunctions.getVal(expr);
		assertNotNull(expr_val);
		assertTrue(expr_val instanceof BooleanLiteral);
		assertTrue(Boolean.parseBoolean(UtilityFunctions.getStringProperty(expr_val, "value")));
	}

	@Test
	public void testReachableStatement() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "system implementation sys.impl\r\n"
				+ "	annex agree{**\r\n"
				+ "		reachable R1 \"SimpleTest\" : true;\r\n"
				+ "	**};\r\n"
				+ "end sys.impl;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		EObject aadl_package_impl = issueCollection.getResource().getContents().get(0);
		EObject pub_sec = UtilityFunctions.getownedPublicSection(aadl_package_impl);
		EObject owned_classifiers = UtilityFunctions.getownedClassifier(pub_sec, "sys.impl");
		EObject annex_subclause = UtilityFunctions.getAnnexSubclause(owned_classifiers, "agree");
		EObject parsed_annex_subclause = UtilityFunctions.getParsedAnnexSubclause(annex_subclause);
		EObject contract = UtilityFunctions.getContract(parsed_annex_subclause);
		assertNotNull(contract);
		assertTrue(contract instanceof AgreeContract);
		EObject spec = UtilityFunctions.getSpec(contract, 0);
		assertNotNull(spec);
		assertTrue(spec instanceof ReachableStatement);
		assertTrue(UtilityFunctions.getStringProperty(spec, "name").equals("R1"));
		assertTrue(UtilityFunctions.getStringProperty(spec, "str").equals("SimpleTest"));
		EObject expr = UtilityFunctions.getExpr(spec);
		assertNotNull(expr);
		assertTrue(expr instanceof BoolLitExpr);
		EObject expr_val = UtilityFunctions.getVal(expr);
		assertNotNull(expr_val);
		assertTrue(expr_val instanceof BooleanLiteral);
		assertTrue(Boolean.parseBoolean(UtilityFunctions.getStringProperty(expr_val, "value")));
	}

	@Test
	public void testAlwaysStatement() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "	\r\n"
				+ "system sys\r\n"
				+ "	annex agree {**\r\n"
				+ "		eq test_cond : bool = true;\r\n"
				+ "		assume test \"SimpleTest\" : always test_cond;\r\n"
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
		EObject spec = UtilityFunctions.getSpec(contract, 1);
		assertNotNull(spec);
		assertTrue(spec instanceof AssumeStatement);
		assertTrue(UtilityFunctions.getStringProperty(spec, "name").equals("test"));
		assertTrue(UtilityFunctions.getStringProperty(spec, "str").equals("SimpleTest"));
		EObject pattern = UtilityFunctions.getPattern(spec);
		assertNotNull(pattern);
		assertTrue(pattern instanceof AlwaysStatement);
		EObject expr = UtilityFunctions.getExpr(pattern);
		assertNotNull(expr);
		assertTrue(expr instanceof NamedElmExpr);
		EObject expr_elm = UtilityFunctions.getElm(expr);
		assertNotNull(expr_elm);
		assertTrue(UtilityFunctions.getStringProperty(expr_elm, "name").equals("test_cond"));
		EObject expr_elm_type = UtilityFunctions.getType(expr_elm);
		assertNotNull(expr_elm_type);
		assertTrue(expr_elm_type instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(expr_elm_type, "name").equals("bool"));
	}

	@Test
	public void testWhenHoldsStatement() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
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
		EObject aadl_package_impl = issueCollection.getResource().getContents().get(0);
		EObject pub_sec = UtilityFunctions.getownedPublicSection(aadl_package_impl);
		EObject owned_classifiers = UtilityFunctions.getownedClassifier(pub_sec, "sys");
		EObject annex_subclause = UtilityFunctions.getAnnexSubclause(owned_classifiers, "agree");
		EObject parsed_annex_subclause = UtilityFunctions.getParsedAnnexSubclause(annex_subclause);
		EObject contract = UtilityFunctions.getContract(parsed_annex_subclause);
		assertNotNull(contract);
		assertTrue(contract instanceof AgreeContract);
		EObject spec = UtilityFunctions.getSpec(contract, 2);
		assertNotNull(spec);
		assertTrue(spec instanceof AssumeStatement);
		assertTrue(UtilityFunctions.getStringProperty(spec, "name").equals("test"));
		assertTrue(UtilityFunctions.getStringProperty(spec, "str").equals("SimpleTest"));
		EObject pattern = UtilityFunctions.getPattern(spec);
		assertNotNull(pattern);
		assertTrue(pattern instanceof WhenHoldsStatement);
		assertTrue(UtilityFunctions.getStringProperty(pattern, "excl").equals("exclusively"));
		EObject condition = UtilityFunctions.getCondition(pattern);
		assertNotNull(condition);
		assertTrue(condition instanceof NamedElmExpr);
		EObject condition_elm = UtilityFunctions.getElm(condition);
		assertNotNull(condition_elm);
		assertTrue(UtilityFunctions.getStringProperty(condition_elm, "name").equals("test_cond"));
		EObject condition_elm_type = UtilityFunctions.getType(condition_elm);
		assertNotNull(condition_elm_type);
		assertTrue(condition_elm_type instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(condition_elm_type, "name").equals("bool"));
		EObject conditionInterval = UtilityFunctions.getConditionInterval(pattern);
		assertNotNull(conditionInterval);
		assertTrue(conditionInterval instanceof ClosedTimeInterval);
		EObject conditionInterval_low = UtilityFunctions.getLow(conditionInterval);
		assertNotNull(conditionInterval_low);
		assertTrue(conditionInterval_low instanceof RealLitExpr);
		assertTrue(Double.parseDouble(UtilityFunctions.getStringProperty(conditionInterval_low, "val")) == 0.0);
		EObject conditionInterval_high = UtilityFunctions.getHigh(conditionInterval);
		assertNotNull(conditionInterval_high);
		assertTrue(conditionInterval_high instanceof RealLitExpr);
		assertTrue(Double.parseDouble(UtilityFunctions.getStringProperty(conditionInterval_high, "val")) == 3.0);
		EObject event = UtilityFunctions.getEvent(pattern);
		assertNotNull(event);
		assertTrue(event instanceof NamedElmExpr);
		EObject event_elm = UtilityFunctions.getElm(event);
		assertNotNull(event_elm);
		assertTrue(UtilityFunctions.getStringProperty(event_elm, "name").equals("test_event"));
		EObject event_elm_type = UtilityFunctions.getType(event_elm);
		assertNotNull(event_elm_type);
		assertTrue(event_elm_type instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(event_elm_type, "name").equals("bool"));
		EObject eventInterval = UtilityFunctions.getEventInterval(pattern);
		assertNotNull(eventInterval);
		assertTrue(eventInterval instanceof ClosedTimeInterval);
		EObject eventInterval_low = UtilityFunctions.getLow(eventInterval);
		assertNotNull(eventInterval_low);
		assertTrue(eventInterval_low instanceof RealLitExpr);
		assertTrue(Double.parseDouble(UtilityFunctions.getStringProperty(eventInterval_low, "val")) == 3.0);
		EObject eventInterval_high = UtilityFunctions.getHigh(eventInterval);
		assertNotNull(eventInterval_high);
		assertTrue(eventInterval_high instanceof RealLitExpr);
		assertTrue(Double.parseDouble(UtilityFunctions.getStringProperty(eventInterval_high, "val")) == 5.0);
	}

	@Test
	public void testWhenOccursStatement() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
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
		EObject aadl_package_impl = issueCollection.getResource().getContents().get(0);
		EObject pub_sec = UtilityFunctions.getownedPublicSection(aadl_package_impl);
		EObject owned_classifiers = UtilityFunctions.getownedClassifier(pub_sec, "sys");
		EObject annex_subclause = UtilityFunctions.getAnnexSubclause(owned_classifiers, "agree");
		EObject parsed_annex_subclause = UtilityFunctions.getParsedAnnexSubclause(annex_subclause);
		EObject contract = UtilityFunctions.getContract(parsed_annex_subclause);
		assertNotNull(contract);
		assertTrue(contract instanceof AgreeContract);
		EObject spec = UtilityFunctions.getSpec(contract, 2);
		assertNotNull(spec);
		assertTrue(spec instanceof AssumeStatement);
		assertTrue(UtilityFunctions.getStringProperty(spec, "name").equals("test"));
		assertTrue(UtilityFunctions.getStringProperty(spec, "str").equals("SimpleTest"));
		EObject pattern = UtilityFunctions.getPattern(spec);
		assertNotNull(pattern);
		assertTrue(pattern instanceof WhenOccursStatment);
		assertTrue(UtilityFunctions.getStringProperty(pattern, "excl").equals("exclusively"));
		EObject condition = UtilityFunctions.getCondition(pattern);
		assertNotNull(condition);
		assertTrue(condition instanceof NamedElmExpr);
		EObject condition_elm = UtilityFunctions.getElm(condition);
		assertNotNull(condition_elm);
		assertTrue(UtilityFunctions.getStringProperty(condition_elm, "name").equals("test_cond"));
		EObject condition_elm_type = UtilityFunctions.getType(condition_elm);
		assertNotNull(condition_elm_type);
		assertTrue(condition_elm_type instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(condition_elm_type, "name").equals("bool"));
		EObject interval = UtilityFunctions.getInterval(pattern);
		assertNotNull(interval);
		assertTrue(interval instanceof ClosedTimeInterval);
		EObject interval_low = UtilityFunctions.getLow(interval);
		assertNotNull(interval_low);
		assertTrue(interval_low instanceof RealLitExpr);
		assertTrue(Double.parseDouble(UtilityFunctions.getStringProperty(interval_low, "val")) == 0.0);
		EObject interval_high = UtilityFunctions.getHigh(interval);
		assertNotNull(interval_high);
		assertTrue(interval_high instanceof RealLitExpr);
		assertTrue(Double.parseDouble(UtilityFunctions.getStringProperty(interval_high, "val")) == 5.0);
		EObject event = UtilityFunctions.getEvent(pattern);
		assertNotNull(event);
		assertTrue(event instanceof NamedElmExpr);
		EObject event_elm = UtilityFunctions.getElm(event);
		assertNotNull(event_elm);
		assertTrue(UtilityFunctions.getStringProperty(event_elm, "name").equals("test_event"));
		EObject event_elm_type = UtilityFunctions.getType(event_elm);
		assertNotNull(event_elm_type);
		assertTrue(event_elm_type instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(event_elm_type, "name").equals("bool"));
		EObject times = UtilityFunctions.getTimes(pattern);
		assertNotNull(times);
		assertTrue(times instanceof IntLitExpr);
		assertTrue(Integer.parseInt(UtilityFunctions.getStringProperty(times, "val")) == 3);
	}

	@Test
	public void testWheneverOccursStatement() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
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
		EObject aadl_package_impl = issueCollection.getResource().getContents().get(0);
		EObject pub_sec = UtilityFunctions.getownedPublicSection(aadl_package_impl);
		EObject owned_classifiers = UtilityFunctions.getownedClassifier(pub_sec, "sys");
		EObject annex_subclause = UtilityFunctions.getAnnexSubclause(owned_classifiers, "agree");
		EObject parsed_annex_subclause = UtilityFunctions.getParsedAnnexSubclause(annex_subclause);
		EObject contract = UtilityFunctions.getContract(parsed_annex_subclause);
		assertNotNull(contract);
		assertTrue(contract instanceof AgreeContract);
		EObject spec = UtilityFunctions.getSpec(contract, 2);
		assertNotNull(spec);
		assertTrue(spec instanceof AssumeStatement);
		assertTrue(UtilityFunctions.getStringProperty(spec, "name").equals("test"));
		assertTrue(UtilityFunctions.getStringProperty(spec, "str").equals("SimpleTest"));
		EObject pattern = UtilityFunctions.getPattern(spec);
		assertNotNull(pattern);
		assertTrue(pattern instanceof WheneverOccursStatement);
		assertTrue(UtilityFunctions.getStringProperty(pattern, "excl").equals("exclusively"));
		EObject cause = UtilityFunctions.getCause(pattern);
		assertNotNull(cause);
		assertTrue(cause instanceof NamedElmExpr);
		EObject cause_elm = UtilityFunctions.getElm(cause);
		assertNotNull(cause_elm);
		assertTrue(UtilityFunctions.getStringProperty(cause_elm, "name").equals("test_cond"));
		EObject cause_elm_type = UtilityFunctions.getType(cause_elm);
		assertNotNull(cause_elm_type);
		assertTrue(cause_elm_type instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(cause_elm_type, "name").equals("bool"));
		EObject effect = UtilityFunctions.getEffect(pattern);
		assertNotNull(effect);
		assertTrue(effect instanceof NamedElmExpr);
		EObject effect_elm = UtilityFunctions.getElm(effect);
		assertNotNull(effect_elm);
		assertTrue(UtilityFunctions.getStringProperty(effect_elm, "name").equals("test_effect"));
		EObject effect_elm_type = UtilityFunctions.getType(effect_elm);
		assertNotNull(effect_elm_type);
		assertTrue(effect_elm_type instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(effect_elm_type, "name").equals("bool"));
		EObject interval = UtilityFunctions.getInterval(pattern);
		assertNotNull(interval);
		assertTrue(interval instanceof ClosedTimeInterval);
		EObject low = UtilityFunctions.getLow(interval);
		assertNotNull(low);
		assertTrue(low instanceof RealLitExpr);
		assertTrue(Double.parseDouble(UtilityFunctions.getStringProperty(low, "val")) == 0.0);
		EObject high = UtilityFunctions.getHigh(interval);
		assertNotNull(high);
		assertTrue(high instanceof RealLitExpr);
		assertTrue(Double.parseDouble(UtilityFunctions.getStringProperty(high, "val")) == 1.0);
	}

	@Test
	public void testWheneverBecomesTrueStatement() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "	annex agree {**\r\n"
				+ "		eq test_cond : bool = false;\r\n"
				+ "		eq test_effect : bool = true;\r\n"
				+ "		assume test \"SimpleTest\": whenever test_cond becomes true test_effect exclusively occurs during (0.0, 1.0];\r\n"
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
		EObject spec = UtilityFunctions.getSpec(contract, 2);
		assertNotNull(spec);
		assertTrue(spec instanceof AssumeStatement);
		assertTrue(UtilityFunctions.getStringProperty(spec, "name").equals("test"));
		assertTrue(UtilityFunctions.getStringProperty(spec, "str").equals("SimpleTest"));
		EObject pattern = UtilityFunctions.getPattern(spec);
		assertNotNull(pattern);
		assertTrue(pattern instanceof WheneverBecomesTrueStatement);
		assertTrue(UtilityFunctions.getStringProperty(pattern, "excl").equals("exclusively"));
		EObject cause = UtilityFunctions.getCause(pattern);
		assertNotNull(cause);
		assertTrue(cause instanceof NamedElmExpr);
		EObject cause_elm = UtilityFunctions.getElm(cause);
		assertNotNull(cause_elm);
		assertTrue(UtilityFunctions.getStringProperty(cause_elm, "name").equals("test_cond"));
		EObject cause_elm_type = UtilityFunctions.getType(cause_elm);
		assertNotNull(cause_elm_type);
		assertTrue(cause_elm_type instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(cause_elm_type, "name").equals("bool"));
		EObject effect = UtilityFunctions.getEffect(pattern);
		assertNotNull(effect);
		assertTrue(effect instanceof NamedElmExpr);
		EObject effect_elm = UtilityFunctions.getElm(effect);
		assertNotNull(effect_elm);
		assertTrue(UtilityFunctions.getStringProperty(effect_elm, "name").equals("test_effect"));
		EObject effect_elm_type = UtilityFunctions.getType(effect_elm);
		assertNotNull(effect_elm_type);
		assertTrue(effect_elm_type instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(effect_elm_type, "name").equals("bool"));
		EObject interval = UtilityFunctions.getInterval(pattern);
		assertNotNull(interval);
		assertTrue(interval instanceof OpenLeftTimeInterval);
		EObject low = UtilityFunctions.getLow(interval);
		assertNotNull(low);
		assertTrue(low instanceof RealLitExpr);
		assertTrue(Double.parseDouble(UtilityFunctions.getStringProperty(low, "val")) == 0.0);
		EObject high = UtilityFunctions.getHigh(interval);
		assertNotNull(high);
		assertTrue(high instanceof RealLitExpr);
		assertTrue(Double.parseDouble(UtilityFunctions.getStringProperty(high, "val")) == 1.0);
	}

	@Test
	public void testWheneverHoldsStatement() throws Exception {
		String test = "package TestPackage\r\n"
					+ "public\r\n"
					+ "\r\n"
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
		EObject aadl_package_impl = issueCollection.getResource().getContents().get(0);
		EObject pub_sec = UtilityFunctions.getownedPublicSection(aadl_package_impl);
		EObject owned_classifiers = UtilityFunctions.getownedClassifier(pub_sec, "sys");
		EObject annex_subclause = UtilityFunctions.getAnnexSubclause(owned_classifiers, "agree");
		EObject parsed_annex_subclause = UtilityFunctions.getParsedAnnexSubclause(annex_subclause);
		EObject contract = UtilityFunctions.getContract(parsed_annex_subclause);
		assertNotNull(contract);
		assertTrue(contract instanceof AgreeContract);
		EObject spec = UtilityFunctions.getSpec(contract, 2);
		assertNotNull(spec);
		assertTrue(spec instanceof AssumeStatement);
		assertTrue(UtilityFunctions.getStringProperty(spec, "name").equals("test"));
		assertTrue(UtilityFunctions.getStringProperty(spec, "str").equals("SimpleTest"));
		EObject pattern = UtilityFunctions.getPattern(spec);
		assertNotNull(pattern);
		assertTrue(pattern instanceof WheneverHoldsStatement);
		assertTrue(UtilityFunctions.getStringProperty(pattern, "excl").equals("exclusively"));
		EObject cause = UtilityFunctions.getCause(pattern);
		assertNotNull(cause);
		assertTrue(cause instanceof NamedElmExpr);
		EObject cause_elm = UtilityFunctions.getElm(cause);
		assertNotNull(cause_elm);
		assertTrue(UtilityFunctions.getStringProperty(cause_elm, "name").equals("test_cond"));
		EObject cause_elm_type = UtilityFunctions.getType(cause_elm);
		assertNotNull(cause_elm_type);
		assertTrue(cause_elm_type instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(cause_elm_type, "name").equals("bool"));
		EObject effect = UtilityFunctions.getEffect(pattern);
		assertNotNull(effect);
		assertTrue(effect instanceof NamedElmExpr);
		EObject effect_elm = UtilityFunctions.getElm(effect);
		assertNotNull(effect_elm);
		assertTrue(UtilityFunctions.getStringProperty(effect_elm, "name").equals("test_effect"));
		EObject effect_elm_type = UtilityFunctions.getType(effect_elm);
		assertNotNull(effect_elm_type);
		assertTrue(effect_elm_type instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(effect_elm_type, "name").equals("bool"));
		EObject interval = UtilityFunctions.getInterval(pattern);
		assertNotNull(interval);
		assertTrue(interval instanceof OpenRightTimeInterval);
		EObject low = UtilityFunctions.getLow(interval);
		assertNotNull(low);
		assertTrue(low instanceof RealLitExpr);
		assertTrue(Double.parseDouble(UtilityFunctions.getStringProperty(low, "val")) == 0.0);
		EObject high = UtilityFunctions.getHigh(interval);
		assertNotNull(high);
		assertTrue(high instanceof RealLitExpr);
		assertTrue(Double.parseDouble(UtilityFunctions.getStringProperty(high, "val")) == 1.0);
	}

	@Test
	public void testWheneverImpliesStatement() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
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
		EObject aadl_package_impl = issueCollection.getResource().getContents().get(0);
		EObject pub_sec = UtilityFunctions.getownedPublicSection(aadl_package_impl);
		EObject owned_classifiers = UtilityFunctions.getownedClassifier(pub_sec, "sys");
		EObject annex_subclause = UtilityFunctions.getAnnexSubclause(owned_classifiers, "agree");
		EObject parsed_annex_subclause = UtilityFunctions.getParsedAnnexSubclause(annex_subclause);
		EObject contract = UtilityFunctions.getContract(parsed_annex_subclause);
		assertNotNull(contract);
		assertTrue(contract instanceof AgreeContract);
		EObject spec = UtilityFunctions.getSpec(contract, 3);
		assertNotNull(spec);
		assertTrue(spec instanceof AssumeStatement);
		assertTrue(UtilityFunctions.getStringProperty(spec, "name").equals("test"));
		assertTrue(UtilityFunctions.getStringProperty(spec, "str").equals("SimpleTest"));
		EObject pattern = UtilityFunctions.getPattern(spec);
		assertNotNull(pattern);
		assertTrue(pattern instanceof WheneverImpliesStatement);
		assertTrue(UtilityFunctions.getStringProperty(pattern, "excl").equals("exclusively"));
		EObject cause = UtilityFunctions.getCause(pattern);
		assertNotNull(cause);
		assertTrue(cause instanceof NamedElmExpr);
		EObject cause_elm = UtilityFunctions.getElm(cause);
		assertNotNull(cause_elm);
		assertTrue(UtilityFunctions.getStringProperty(cause_elm, "name").equals("test_cond"));
		EObject cause_elm_type = UtilityFunctions.getType(cause_elm);
		assertNotNull(cause_elm_type);
		assertTrue(cause_elm_type instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(cause_elm_type, "name").equals("bool"));
		EObject lhs = UtilityFunctions.getLhs(pattern);
		assertNotNull(lhs);
		assertTrue(lhs instanceof NamedElmExpr);
		EObject lhs_elm = UtilityFunctions.getElm(lhs);
		assertNotNull(lhs_elm);
		assertTrue(UtilityFunctions.getStringProperty(lhs_elm, "name").equals("test_lhs"));
		EObject lhs_elm_type = UtilityFunctions.getType(lhs_elm);
		assertNotNull(lhs_elm_type);
		assertTrue(lhs_elm_type instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(lhs_elm_type, "name").equals("bool"));
		EObject rhs = UtilityFunctions.getRhs(pattern);
		assertNotNull(rhs);
		assertTrue(rhs instanceof NamedElmExpr);
		EObject rhs_elm = UtilityFunctions.getElm(rhs);
		assertNotNull(rhs_elm);
		assertTrue(UtilityFunctions.getStringProperty(rhs_elm, "name").equals("test_rhs"));
		EObject rhs_elm_type = UtilityFunctions.getType(rhs_elm);
		assertNotNull(rhs_elm_type);
		assertTrue(rhs_elm_type instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(rhs_elm_type, "name").equals("bool"));
		EObject interval = UtilityFunctions.getInterval(pattern);
		assertNotNull(interval);
		assertTrue(interval instanceof OpenTimeInterval);
		EObject low = UtilityFunctions.getLow(interval);
		assertNotNull(low);
		assertTrue(low instanceof RealLitExpr);
		assertTrue(Double.parseDouble(UtilityFunctions.getStringProperty(low, "val")) == 0.0);
		EObject high = UtilityFunctions.getHigh(interval);
		assertNotNull(high);
		assertTrue(high instanceof RealLitExpr);
		assertTrue(Double.parseDouble(UtilityFunctions.getStringProperty(high, "val")) == 1.0);
	}

	@Test
	public void testPeriodicStatement() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "	annex agree {**\r\n"
				+ "		eq test_cond : bool = true;\r\n"
				+ "		assume test \"SimpleTest\": condition test_cond occurs each 15.75 with jitter 5.0;\r\n"
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
		EObject spec = UtilityFunctions.getSpec(contract, 1);
		assertNotNull(spec);
		assertTrue(spec instanceof AssumeStatement);
		assertTrue(UtilityFunctions.getStringProperty(spec, "name").equals("test"));
		assertTrue(UtilityFunctions.getStringProperty(spec, "str").equals("SimpleTest"));
		EObject pattern = UtilityFunctions.getPattern(spec);
		assertNotNull(pattern);
		assertTrue(pattern instanceof PeriodicStatement);
		EObject event = UtilityFunctions.getEvent(pattern);
		assertNotNull(event);
		assertTrue(event instanceof NamedElmExpr);
		EObject event_elm = UtilityFunctions.getElm(event);
		assertNotNull(event_elm);
		assertTrue(UtilityFunctions.getStringProperty(event_elm, "name").equals("test_cond"));
		EObject event_elm_type = UtilityFunctions.getType(event_elm);
		assertNotNull(event_elm_type);
		assertTrue(event_elm_type instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(event_elm_type, "name").equals("bool"));
		EObject period = UtilityFunctions.getPeriod(pattern);
		assertNotNull(period);
		assertTrue(period instanceof RealLitExpr);
		assertTrue(Double.parseDouble(UtilityFunctions.getStringProperty(period, "val")) == 15.75);
		EObject jitter = UtilityFunctions.getJitter(pattern);
		assertNotNull(jitter);
		assertTrue(jitter instanceof RealLitExpr);
		assertTrue(Double.parseDouble(UtilityFunctions.getStringProperty(jitter, "val")) == 5.0);
	}

	@Test
	public void testSporadicStatement() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "	annex agree {**\r\n"
				+ "		eq test_cond : bool = true;\r\n"
				+ "		assume test \"SimpleTest\": condition test_cond occurs sporadic with IAT 50.0 with jitter 10.0;\r\n"
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
		EObject spec = UtilityFunctions.getSpec(contract, 1);
		assertNotNull(spec);
		assertTrue(spec instanceof AssumeStatement);
		assertTrue(UtilityFunctions.getStringProperty(spec, "name").equals("test"));
		assertTrue(UtilityFunctions.getStringProperty(spec, "str").equals("SimpleTest"));
		EObject pattern = UtilityFunctions.getPattern(spec);
		assertNotNull(pattern);
		assertTrue(pattern instanceof SporadicStatement);
		EObject event = UtilityFunctions.getEvent(pattern);
		assertNotNull(event);
		assertTrue(event instanceof NamedElmExpr);
		EObject event_elm = UtilityFunctions.getElm(event);
		assertNotNull(event_elm);
		assertTrue(UtilityFunctions.getStringProperty(event_elm, "name").equals("test_cond"));
		EObject event_elm_type = UtilityFunctions.getType(event_elm);
		assertNotNull(event_elm_type);
		assertTrue(event_elm_type instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(event_elm_type, "name").equals("bool"));
		EObject iat = UtilityFunctions.getIAT(pattern);
		assertNotNull(iat);
		assertTrue(iat instanceof RealLitExpr);
		assertTrue(Double.parseDouble(UtilityFunctions.getStringProperty(iat, "val")) == 50.0);
		EObject jitter = UtilityFunctions.getJitter(pattern);
		assertNotNull(jitter);
		assertTrue(jitter instanceof RealLitExpr);
		assertTrue(Double.parseDouble(UtilityFunctions.getStringProperty(jitter, "val")) == 10.0);
	}

	@Test
	public void testSynchStatement() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "system implementation sys.impl\r\n"
				+ "	annex agree {**\r\n"
				+ "		synchrony : 1 simult;\r\n"
				+ "	**};\r\n"
				+ "end sys.impl;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		EObject aadl_package_impl = issueCollection.getResource().getContents().get(0);
		EObject pub_sec = UtilityFunctions.getownedPublicSection(aadl_package_impl);
		EObject owned_classifiers = UtilityFunctions.getownedClassifier(pub_sec, "sys.impl");
		EObject annex_subclause = UtilityFunctions.getAnnexSubclause(owned_classifiers, "agree");
		EObject parsed_annex_subclause = UtilityFunctions.getParsedAnnexSubclause(annex_subclause);
		EObject contract = UtilityFunctions.getContract(parsed_annex_subclause);
		assertNotNull(contract);
		assertTrue(contract instanceof AgreeContract);
		EObject spec = UtilityFunctions.getSpec(contract, 0);
		assertNotNull(spec);
		assertTrue(spec instanceof SynchStatement);
		assertTrue(UtilityFunctions.getStringProperty(spec, "sim").equals("simult"));
		assertTrue(Integer.parseInt(UtilityFunctions.getStringProperty(spec, "val")) == 1);
	}

	@Test
	public void testMNSynchStatement() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "system implementation sys.impl\r\n"
				+ "	subcomponents\r\n"
				+ "		this_sensor: device TestSensor;\r\n"
				+ "		this_sensor2: device TestSensor;\r\n"
				+ "	annex agree {**\r\n"
				+ "		synchrony : this_sensor, this_sensor2 : 5, 3;\r\n"
				+ "	**};\r\n"
				+ "end sys.impl;\r\n"
				+ "\r\n"
				+ "device TestSensor\r\n"
				+ "end TestSensor;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		EObject aadl_package_impl = issueCollection.getResource().getContents().get(0);
		EObject pub_sec = UtilityFunctions.getownedPublicSection(aadl_package_impl);
		EObject owned_classifiers = UtilityFunctions.getownedClassifier(pub_sec, "sys.impl");
		EObject annex_subclause = UtilityFunctions.getAnnexSubclause(owned_classifiers, "agree");
		EObject parsed_annex_subclause = UtilityFunctions.getParsedAnnexSubclause(annex_subclause);
		EObject contract = UtilityFunctions.getContract(parsed_annex_subclause);
		assertNotNull(contract);
		assertTrue(contract instanceof AgreeContract);
		EObject spec = UtilityFunctions.getSpec(contract, 0);
		assertNotNull(spec);
		assertTrue(spec instanceof MNSynchStatement);
		assertTrue(Integer.parseInt(UtilityFunctions.getStringProperty(spec, "min")) == 3);
		assertTrue(Integer.parseInt(UtilityFunctions.getStringProperty(spec, "max")) == 5);
		EObject comp1 = spec.eCrossReferences().get(0);
		assertNotNull(comp1);
		assertTrue(comp1 instanceof DeviceSubcomponent);
		assertTrue(UtilityFunctions.getStringProperty(comp1, "name").equals("this_sensor"));
		EObject comp1_type = UtilityFunctions.getDeviceSubcomponentType(comp1);
		assertNotNull(comp1_type);
		assertTrue(comp1_type instanceof DeviceType);
		assertTrue(UtilityFunctions.getStringProperty(comp1_type, "name").equals("TestSensor"));
		EObject comp2 = spec.eCrossReferences().get(1);
		assertNotNull(comp2);
		assertTrue(comp2 instanceof DeviceSubcomponent);
		assertTrue(UtilityFunctions.getStringProperty(comp2, "name").equals("this_sensor2"));
		EObject comp2_type = UtilityFunctions.getDeviceSubcomponentType(comp1);
		assertNotNull(comp2_type);
		assertTrue(comp2_type instanceof DeviceType);
		assertTrue(UtilityFunctions.getStringProperty(comp2_type, "name").equals("TestSensor"));
	}

	@Test
	public void testCalenStatement() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "system implementation sys.impl\r\n"
				+ "	subcomponents\r\n"
				+ "		this_sensor: device TestSensor;\r\n"
				+ "	annex agree {**\r\n"
				+ "		calendar : this_sensor;\r\n"
				+ "	**};\r\n"
				+ "end sys.impl;\r\n"
				+ "\r\n"
				+ "device TestSensor\r\n"
				+ "end TestSensor;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		EObject aadl_package_impl = issueCollection.getResource().getContents().get(0);
		EObject pub_sec = UtilityFunctions.getownedPublicSection(aadl_package_impl);
		EObject owned_classifiers = UtilityFunctions.getownedClassifier(pub_sec, "sys.impl");
		EObject annex_subclause = UtilityFunctions.getAnnexSubclause(owned_classifiers, "agree");
		EObject parsed_annex_subclause = UtilityFunctions.getParsedAnnexSubclause(annex_subclause);
		EObject contract = UtilityFunctions.getContract(parsed_annex_subclause);
		assertNotNull(contract);
		assertTrue(contract instanceof AgreeContract);
		EObject spec = UtilityFunctions.getSpec(contract, 0);
		assertNotNull(spec);
		assertTrue(spec instanceof CalenStatement);
		EObject els = spec.eCrossReferences().get(0);
		assertNotNull(els);
		assertTrue(els instanceof DeviceSubcomponent);
		assertTrue(UtilityFunctions.getStringProperty(els, "name").equals("this_sensor"));
		EObject els_type = UtilityFunctions.getDeviceSubcomponentType(els);
		assertNotNull(els_type);
		assertTrue(els_type instanceof DeviceType);
		assertTrue(UtilityFunctions.getStringProperty(els_type, "name").equals("TestSensor"));
	}

	@Test
	public void testAsynchStatement() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "system implementation sys.impl\r\n"
				+ "	annex agree {**\r\n"
				+ "		synchrony : asynchronous;\r\n"
				+ "	**};\r\n"
				+ "end sys.impl;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		EObject aadl_package_impl = issueCollection.getResource().getContents().get(0);
		EObject pub_sec = UtilityFunctions.getownedPublicSection(aadl_package_impl);
		EObject owned_classifiers = UtilityFunctions.getownedClassifier(pub_sec, "sys.impl");
		EObject annex_subclause = UtilityFunctions.getAnnexSubclause(owned_classifiers, "agree");
		EObject parsed_annex_subclause = UtilityFunctions.getParsedAnnexSubclause(annex_subclause);
		EObject contract = UtilityFunctions.getContract(parsed_annex_subclause);
		assertNotNull(contract);
		assertTrue(contract instanceof AgreeContract);
		EObject spec = UtilityFunctions.getSpec(contract, 0);
		assertNotNull(spec);
		assertTrue(spec instanceof AsynchStatement);
	}

	@Test
	public void testLatchedStatement() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "system implementation sys.impl\r\n"
				+ "	annex agree {**\r\n"
				+ "		synchrony : latched;\r\n"
				+ "	**};\r\n"
				+ "end sys.impl;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		EObject aadl_package_impl = issueCollection.getResource().getContents().get(0);
		EObject pub_sec = UtilityFunctions.getownedPublicSection(aadl_package_impl);
		EObject owned_classifiers = UtilityFunctions.getownedClassifier(pub_sec, "sys.impl");
		EObject annex_subclause = UtilityFunctions.getAnnexSubclause(owned_classifiers, "agree");
		EObject parsed_annex_subclause = UtilityFunctions.getParsedAnnexSubclause(annex_subclause);
		EObject contract = UtilityFunctions.getContract(parsed_annex_subclause);
		assertNotNull(contract);
		assertTrue(contract instanceof AgreeContract);
		EObject spec = UtilityFunctions.getSpec(contract, 0);
		assertNotNull(spec);
		assertTrue(spec instanceof LatchedStatement);
	}

	@Test
	public void testOrderStatement() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "system implementation sys.impl\r\n"
				+ "	subcomponents\r\n"
				+ "		this_sensor: device TestSensor;\r\n"
				+ "	annex agree {**\r\n"
				+ "		ordering : this_sensor;\r\n"
				+ "	**};\r\n"
				+ "end sys.impl;\r\n"
				+ "\r\n"
				+ "device TestSensor\r\n"
				+ "end TestSensor;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		EObject aadl_package_impl = issueCollection.getResource().getContents().get(0);
		EObject pub_sec = UtilityFunctions.getownedPublicSection(aadl_package_impl);
		EObject owned_classifiers = UtilityFunctions.getownedClassifier(pub_sec, "sys.impl");
		EObject annex_subclause = UtilityFunctions.getAnnexSubclause(owned_classifiers, "agree");
		EObject parsed_annex_subclause = UtilityFunctions.getParsedAnnexSubclause(annex_subclause);
		EObject contract = UtilityFunctions.getContract(parsed_annex_subclause);
		assertNotNull(contract);
		assertTrue(contract instanceof AgreeContract);
		EObject spec = UtilityFunctions.getSpec(contract, 0);
		assertNotNull(spec);
		assertTrue(spec instanceof OrderStatement);
		EObject els = spec.eCrossReferences().get(0);
		assertNotNull(els);
		assertTrue(els instanceof DeviceSubcomponent);
		assertTrue(UtilityFunctions.getStringProperty(els, "name").equals("this_sensor"));
		EObject els_type = UtilityFunctions.getDeviceSubcomponentType(els);
		assertNotNull(els_type);
		assertTrue(els_type instanceof DeviceType);
		assertTrue(UtilityFunctions.getStringProperty(els_type, "name").equals("TestSensor"));
	}

	@Test
	public void testPropertyStatement() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "	annex agree{**\r\n"
				+ "		property test = false;\r\n"
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
		assertTrue(spec instanceof PropertyStatement);
		assertTrue(UtilityFunctions.getStringProperty(spec, "name").equals("test"));
		EObject expr = UtilityFunctions.getExpr(spec);
		assertNotNull(expr);
		assertTrue(expr instanceof BoolLitExpr);
		EObject expr_val = UtilityFunctions.getVal(expr);
		assertNotNull(expr_val);
		assertTrue(expr_val instanceof BooleanLiteral);
		assertFalse(Boolean.parseBoolean(UtilityFunctions.getStringProperty(expr_val, "value")));
	}

	@Test
	public void testConstStatement() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "	annex agree{**\r\n"
				+ "		const test : int = 2;\r\n"
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
		assertTrue(spec instanceof ConstStatement);
		assertTrue(UtilityFunctions.getStringProperty(spec, "name").equals("test"));
		EObject spec_type = UtilityFunctions.getType(spec);
		assertNotNull(spec_type);
		assertTrue(spec_type instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(spec_type, "name").equals("int"));
		EObject expr = UtilityFunctions.getExpr(spec);
		assertNotNull(expr);
		assertTrue(expr instanceof IntLitExpr);
		assertTrue(Integer.parseInt(UtilityFunctions.getStringProperty(expr, "val")) == 2);
	}

	@Test
	public void testEqStatement() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "	annex agree{**\r\n"
				+ "		eq test : bool = true;\r\n"
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
		assertTrue(spec instanceof EqStatement);
		EObject lhs = UtilityFunctions.getLhs(spec, 0);
		assertTrue(UtilityFunctions.getStringProperty(lhs, "name").equals("test"));
		EObject lhs_type = UtilityFunctions.getType(lhs);
		assertNotNull(lhs_type);
		assertTrue(lhs_type instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(lhs_type, "name").equals("bool"));
		EObject expr = UtilityFunctions.getExpr(spec);
		assertNotNull(expr);
		assertTrue(expr instanceof BoolLitExpr);
		EObject expr_val = UtilityFunctions.getVal(expr);
		assertNotNull(expr_val);
		assertTrue(expr_val instanceof BooleanLiteral);
		assertTrue(Boolean.parseBoolean(UtilityFunctions.getStringProperty(expr_val, "value")));
	}

	@Test
	public void testInputStatement() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "	annex agree {**\r\n"
				+ "		agree_input a : bool, b : int;\r\n"
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
		assertTrue(spec instanceof InputStatement);
		EObject lhs0 = UtilityFunctions.getLhs(spec, 0);
		assertTrue(UtilityFunctions.getStringProperty(lhs0, "name").equals("a"));
		EObject lhs0_type = UtilityFunctions.getType(lhs0);
		assertNotNull(lhs0_type);
		assertTrue(lhs0_type instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(lhs0_type, "name").equals("bool"));
		EObject lhs1 = UtilityFunctions.getLhs(spec, 1);
		assertTrue(UtilityFunctions.getStringProperty(lhs1, "name").equals("b"));
		EObject lhs1_type = UtilityFunctions.getType(lhs1);
		assertNotNull(lhs1_type);
		assertTrue(lhs1_type instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(lhs1_type, "name").equals("int"));
	}

	@Test
	public void testAssignStatement() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "	with Base_Types;\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "	features\r\n"
				+ "		data_out: out data port Base_Types::Float;\r\n"
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
		EObject aadl_package_impl = issueCollection.getResource().getContents().get(0);
		EObject pub_sec = UtilityFunctions.getownedPublicSection(aadl_package_impl);
		EObject owned_classifiers = UtilityFunctions.getownedClassifier(pub_sec, "sys.impl");
		EObject annex_subclause = UtilityFunctions.getAnnexSubclause(owned_classifiers, "agree");
		EObject parsed_annex_subclause = UtilityFunctions.getParsedAnnexSubclause(annex_subclause);
		EObject contract = UtilityFunctions.getContract(parsed_annex_subclause);
		assertNotNull(contract);
		assertTrue(contract instanceof AgreeContract);
		EObject spec = UtilityFunctions.getSpec(contract, 0);
		assertNotNull(spec);
		assertTrue(spec instanceof AssignStatement);
		EObject id = UtilityFunctions.getID(spec);
		assertNotNull(id);
		assertTrue(id instanceof DataPort);
		assertTrue(UtilityFunctions.getStringProperty(id, "name").equals("data_out"));
		assertFalse(Boolean.parseBoolean(UtilityFunctions.getStringProperty(id, "in")));
		assertTrue(Boolean.parseBoolean(UtilityFunctions.getStringProperty(id, "out")));
		EObject expr = UtilityFunctions.getExpr(spec);
		assertNotNull(expr);
		assertTrue(expr instanceof RealLitExpr);
		assertTrue(Double.parseDouble(UtilityFunctions.getStringProperty(expr, "val")) == 5.0);
	}

	@Test
	public void testFnDef() throws Exception {
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
		assertTrue(spec instanceof FnDef);
		assertTrue(UtilityFunctions.getStringProperty(spec, "name").equals("test"));
		EObject arg = UtilityFunctions.getArg(spec, 0);
		assertNotNull(arg);
		assertTrue(UtilityFunctions.getStringProperty(arg, "name").equals("x"));
		EObject arg_type = UtilityFunctions.getType(arg);
		assertNotNull(arg_type);
		assertTrue(arg_type instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(arg_type, "name").equals("real"));
		EObject spec_type = UtilityFunctions.getType(spec);
		assertNotNull(spec_type);
		assertTrue(spec_type instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(spec_type, "name").equals("bool"));
		EObject expr = UtilityFunctions.getExpr(spec);
		assertNotNull(expr);
		assertTrue(expr instanceof BoolLitExpr);
		EObject expr_val = UtilityFunctions.getVal(expr);
		assertNotNull(expr_val);
		assertTrue(expr_val instanceof BooleanLiteral);
		assertFalse(Boolean.parseBoolean(UtilityFunctions.getStringProperty(expr_val, "value")));
	}

	@Test
	public void testLibraryFnDef() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "	annex agree {**\r\n"
				+ "		external test(x : int) : int;\r\n"
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
		assertTrue(spec instanceof LibraryFnDef);
		assertTrue(UtilityFunctions.getStringProperty(spec, "name").equals("test"));
		EObject arg = UtilityFunctions.getArg(spec, 0);
		assertNotNull(arg);
		assertTrue(UtilityFunctions.getStringProperty(arg, "name").equals("x"));
		EObject arg_type = UtilityFunctions.getType(arg);
		assertNotNull(arg_type);
		assertTrue(arg_type instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(arg_type, "name").equals("int"));
		EObject spec_type = UtilityFunctions.getType(spec);
		assertNotNull(spec_type);
		assertTrue(spec_type instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(spec_type, "name").equals("int"));
	}

	@Test
	public void testUninterpretedFnDef() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "	annex agree{**\r\n"
				+ "		uninterpreted test(x : bool) : int;\r\n"
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
		assertTrue(spec instanceof UninterpretedFnDef);
		assertTrue(UtilityFunctions.getStringProperty(spec, "name").equals("test"));
		EObject arg = UtilityFunctions.getArg(spec, 0);
		assertNotNull(arg);
		assertTrue(UtilityFunctions.getStringProperty(arg, "name").equals("x"));
		EObject arg_type = UtilityFunctions.getType(arg);
		assertNotNull(arg_type);
		assertTrue(arg_type instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(arg_type, "name").equals("bool"));
		EObject spec_type = UtilityFunctions.getType(spec);
		assertNotNull(spec_type);
		assertTrue(spec_type instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(spec_type, "name").equals("int"));
	}

	@Test
	public void testLinearizationDef() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "	annex agree{**\r\n"
				+ "		linearization sq (x : real) over [0.0 .. 10.0] within 0.1 : x^2;\r\n"
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
		assertTrue(spec instanceof LinearizationDef);
		assertTrue(UtilityFunctions.getStringProperty(spec, "name").equals("sq"));
		EObject arg = UtilityFunctions.getArg(spec, 0);
		assertNotNull(arg);
		assertTrue(UtilityFunctions.getStringProperty(arg, "name").equals("x"));
		EObject arg_type = UtilityFunctions.getType(arg);
		assertNotNull(arg_type);
		assertTrue(arg_type instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(arg_type, "name").equals("real"));
		EObject interval = UtilityFunctions.getInterval(spec, 0);
		assertNotNull(interval);
		assertTrue(interval instanceof LinearizationInterval);
		EObject interval_start = UtilityFunctions.getStart(interval);
		assertNotNull(interval_start);
		assertTrue(interval_start instanceof RealLitExpr);
		assertTrue(Double.parseDouble(UtilityFunctions.getStringProperty(interval_start, "val")) == 0.0);
		EObject interval_end = UtilityFunctions.getEnd(interval);
		assertNotNull(interval_end);
		assertTrue(interval_end instanceof RealLitExpr);
		assertTrue(Double.parseDouble(UtilityFunctions.getStringProperty(interval_end, "val")) == 10.0);
		EObject precision = UtilityFunctions.getPrecision(spec);
		assertNotNull(precision);
		assertTrue(precision instanceof RealLitExpr);
		assertTrue(Double.parseDouble(UtilityFunctions.getStringProperty(precision, "val")) == 0.1);
		EObject body = UtilityFunctions.getExprBody(spec);
		assertNotNull(body);
		assertTrue(body instanceof BinaryExpr);
		assertTrue(UtilityFunctions.getStringProperty(body, "op").equals("^"));
		EObject left = UtilityFunctions.getLeft(body);
		assertNotNull(left);
		assertTrue(left instanceof NamedElmExpr);
		EObject elm = UtilityFunctions.getElm(left);
		assertNotNull(elm);
		assertTrue(UtilityFunctions.getStringProperty(elm, "name").equals("x"));
		EObject elm_type = UtilityFunctions.getType(elm);
		assertNotNull(elm_type);
		assertTrue(elm_type instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(elm_type, "name").equals("real"));
		EObject right = UtilityFunctions.getRight(body);
		assertNotNull(right);
		assertTrue(right instanceof IntLitExpr);
		assertTrue(Integer.parseInt(UtilityFunctions.getStringProperty(right, "val")) == 2);
	}

	@Test
	public void testNodeDef() throws Exception {
		String test = "package TestPackage\n"
					+ "public\n"
					+ "\n"
					+ "system sys\n"
					+ "	annex agree{**\n"
					+ "		node test(data_in: bool) returns (data_out: bool);\n"
					+ "		let\n"
					+ "			data_out = true;\n"
					+ "			lemma \"Always True\" : true;\n"
					+ "		tel;\n"
					+ "	**};\n"
					+ "end sys;\n"
					+ "\n"
					+ "system implementation sys.impl\n"
					+ "end sys.impl;\n"
					+ "\n"
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
		assertTrue(spec instanceof NodeDef);
		assertTrue(UtilityFunctions.getStringProperty(spec, "name").equals("test"));
		EObject arg = UtilityFunctions.getArg(spec, 0);
		assertNotNull(arg);
		assertTrue(UtilityFunctions.getStringProperty(arg, "name").equals("data_in"));
		EObject arg_type = UtilityFunctions.getType(arg);
		assertNotNull(arg_type);
		assertTrue(arg_type instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(arg_type, "name").equals("bool"));
		EObject ret = UtilityFunctions.getRet(spec, 0);
		assertNotNull(ret);
		assertTrue(UtilityFunctions.getStringProperty(ret, "name").equals("data_out"));
		EObject ret_type = UtilityFunctions.getType(ret);
		assertNotNull(ret_type);
		assertTrue(ret_type instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(ret_type, "name").equals("bool"));
		EObject body = UtilityFunctions.getNodeBody(spec);
		assertNotNull(body);
		assertTrue(body instanceof NodeBodyExpr);
		EObject stmt0 = UtilityFunctions.getStmt(body, 0);
		assertNotNull(stmt0);
		assertTrue(stmt0 instanceof NodeEq);
		EObject stmt0_expr = UtilityFunctions.getExpr(stmt0);
		assertNotNull(stmt0_expr);
		assertTrue(stmt0_expr instanceof BoolLitExpr);
		EObject stmt0_expr_val = UtilityFunctions.getVal(stmt0_expr);
		assertNotNull(stmt0_expr_val);
		assertTrue(stmt0_expr_val instanceof BooleanLiteral);
		assertTrue(Boolean.parseBoolean(UtilityFunctions.getStringProperty(stmt0_expr_val, "value")));
		EObject lhs = stmt0.eCrossReferences().get(0);
		assertNotNull(lhs);
		assertTrue(UtilityFunctions.getStringProperty(lhs, "name").equals("data_out"));
		EObject lhs_type = UtilityFunctions.getType(lhs);
		assertNotNull(lhs_type);
		assertTrue(lhs_type instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(lhs_type, "name").equals("bool"));
		EObject stmt1 = UtilityFunctions.getStmt(body, 1);
		assertNotNull(stmt1);
		assertTrue(stmt1 instanceof NodeLemma);
		assertTrue(UtilityFunctions.getStringProperty(stmt1, "str").equals("Always True"));
		EObject stmt1_expr = UtilityFunctions.getExpr(stmt1);
		assertNotNull(stmt1_expr);
		assertTrue(stmt1_expr instanceof BoolLitExpr);
		EObject expr_val = UtilityFunctions.getVal(stmt1_expr);
		assertNotNull(expr_val);
		assertTrue(expr_val instanceof BooleanLiteral);
		assertTrue(Boolean.parseBoolean(UtilityFunctions.getStringProperty(expr_val, "value")));
	}

	@Test
	public void testRecordDef() throws Exception {
		String test = "package TestPackage\n"
				+ "public\n"
				+ "\n"
				+ "system sys\n"
				+ "	annex agree{**\n"
				+ "		type test = struct{a : bool};\n"
				+ "	**};\n"
				+ "end sys;\n"
				+ "\n"
				+ "system implementation sys.impl\n"
				+ "end sys.impl;\n"
				+ "\n"
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
		assertTrue(spec instanceof RecordDef);
		assertTrue(UtilityFunctions.getStringProperty(spec, "name").equals("test"));
		EObject arg = UtilityFunctions.getArg(spec, 0);
		assertNotNull(arg);
		assertTrue(UtilityFunctions.getStringProperty(arg, "name").equals("a"));
		EObject arg_type = UtilityFunctions.getType(arg);
		assertNotNull(arg_type);
		assertTrue(arg_type instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(arg_type, "name").equals("bool"));
	}

	@Test
	public void testEnumStatement() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "	\r\n"
				+ "annex agree {**\r\n"
				+ "	enum test = {alpha, beta, gamma};\r\n"
				+ "**};\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		EObject aadl_package_impl = issueCollection.getResource().getContents().get(0);
		EObject pub_sec = UtilityFunctions.getownedPublicSection(aadl_package_impl);
		EObject annex_lib = UtilityFunctions.getAnnexLibrary(pub_sec, "agree");
		EObject parsed_annex_lib = UtilityFunctions.getParsedAnnexLibrary(annex_lib);
		EObject contract = UtilityFunctions.getContract(parsed_annex_lib);
		assertNotNull(contract);
		assertTrue(contract instanceof AgreeContract);
		EObject spec = UtilityFunctions.getSpec(contract, 0);
		assertNotNull(spec);
		assertTrue(spec instanceof EnumStatement);
		assertTrue(UtilityFunctions.getStringProperty(spec, "name").equals("test"));
		EObject enum0 = UtilityFunctions.getEnum(spec, 0);
		assertNotNull(enum0);
		assertTrue(enum0 instanceof NamedID);
		assertTrue(UtilityFunctions.getStringProperty(enum0, "name").equals("alpha"));
		EObject enum1 = UtilityFunctions.getEnum(spec, 1);
		assertNotNull(enum1);
		assertTrue(enum1 instanceof NamedID);
		assertTrue(UtilityFunctions.getStringProperty(enum1, "name").equals("beta"));
		EObject enum2 = UtilityFunctions.getEnum(spec, 2);
		assertNotNull(enum2);
		assertTrue(enum2 instanceof NamedID);
		assertTrue(UtilityFunctions.getStringProperty(enum2, "name").equals("gamma"));
	}

	@Test
	public void testForallExpr() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "	annex agree {**\r\n"
				+ "		eq test_arr : real[3] = [| 1.1, 2.2, 3.3|];\r\n"
				+ "		property test =  forall test_num in test_arr, test_num > 2.0;\r\n"
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
		EObject spec = UtilityFunctions.getSpec(contract, 1);
		assertNotNull(spec);
		assertTrue(spec instanceof PropertyStatement);
		assertTrue(UtilityFunctions.getStringProperty(spec, "name").equals("test"));
		EObject expr = UtilityFunctions.getExpr(spec);
		assertNotNull(expr);
		assertTrue(expr instanceof ForallExpr);
		EObject binding = UtilityFunctions.getBinding(expr);
		assertNotNull(binding);
		assertTrue(binding instanceof NamedID);
		assertTrue(UtilityFunctions.getStringProperty(binding, "name").equals("test_num"));
		EObject array = UtilityFunctions.getArray(expr);
		assertNotNull(array);
		assertTrue(array instanceof NamedElmExpr);
		EObject array_elm = UtilityFunctions.getElm(array);
		assertNotNull(array_elm);
		assertTrue(UtilityFunctions.getStringProperty(array_elm, "name").equals("test_arr"));
		EObject array_elm_type = UtilityFunctions.getType(array_elm);
		assertNotNull(array_elm_type);
		assertTrue(array_elm_type instanceof ArrayType);
		assertTrue(Integer.parseInt(UtilityFunctions.getStringProperty(array_elm_type, "size")) == 3);
		EObject array_elm_type_stem = UtilityFunctions.getStem(array_elm_type);
		assertNotNull(array_elm_type_stem);
		assertTrue(array_elm_type_stem instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(array_elm_type_stem, "name").equals("real"));
		EObject expr_expr = UtilityFunctions.getExpr(expr);
		assertNotNull(expr_expr);
		assertTrue(expr_expr instanceof BinaryExpr);
		assertTrue(UtilityFunctions.getStringProperty(expr_expr, "op").equals(">"));
		EObject expr_expr_left = UtilityFunctions.getLeft(expr_expr);
		assertNotNull(expr_expr_left);
		assertTrue(expr_expr_left instanceof NamedElmExpr);
		EObject expr_expr_left_elm = UtilityFunctions.getElm(expr_expr_left);
		assertNotNull(expr_expr_left_elm);
		assertTrue(expr_expr_left_elm instanceof NamedID);
		assertTrue(UtilityFunctions.getStringProperty(expr_expr_left_elm, "name").equals("test_num"));
		EObject expr_expr_right = UtilityFunctions.getRight(expr_expr);
		assertNotNull(expr_expr_right);
		assertTrue(expr_expr_right instanceof RealLitExpr);
		assertTrue(Double.parseDouble(UtilityFunctions.getStringProperty(expr_expr_right, "val")) == 2.0);
	}

	@Test
	public void testExistsExpr() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "	annex agree {**\r\n"
				+ "		eq test_arr : real[3] = [| 1.1, 2.2, 3.3|];\r\n"
				+ "		property test =  exists test_num in test_arr, test_num > 2.0;\r\n"
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
		EObject spec = UtilityFunctions.getSpec(contract, 1);
		assertNotNull(spec);
		assertTrue(spec instanceof PropertyStatement);
		assertTrue(UtilityFunctions.getStringProperty(spec, "name").equals("test"));
		EObject expr = UtilityFunctions.getExpr(spec);
		assertNotNull(expr);
		assertTrue(expr instanceof ExistsExpr);
		EObject binding = UtilityFunctions.getBinding(expr);
		assertNotNull(binding);
		assertTrue(binding instanceof NamedID);
		assertTrue(UtilityFunctions.getStringProperty(binding, "name").equals("test_num"));
		EObject array = UtilityFunctions.getArray(expr);
		assertNotNull(array);
		assertTrue(array instanceof NamedElmExpr);
		EObject array_elm = UtilityFunctions.getElm(array);
		assertNotNull(array_elm);
		assertTrue(UtilityFunctions.getStringProperty(array_elm, "name").equals("test_arr"));
		EObject array_elm_type = UtilityFunctions.getType(array_elm);
		assertNotNull(array_elm_type);
		assertTrue(array_elm_type instanceof ArrayType);
		assertTrue(Integer.parseInt(UtilityFunctions.getStringProperty(array_elm_type, "size")) == 3);
		EObject array_elm_type_stem = UtilityFunctions.getStem(array_elm_type);
		assertNotNull(array_elm_type_stem);
		assertTrue(array_elm_type_stem instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(array_elm_type_stem, "name").equals("real"));
		EObject expr_expr = UtilityFunctions.getExpr(expr);
		assertNotNull(expr_expr);
		assertTrue(expr_expr instanceof BinaryExpr);
		assertTrue(UtilityFunctions.getStringProperty(expr_expr, "op").equals(">"));
		EObject expr_expr_left = UtilityFunctions.getLeft(expr_expr);
		assertNotNull(expr_expr_left);
		assertTrue(expr_expr_left instanceof NamedElmExpr);
		EObject expr_expr_left_elm = UtilityFunctions.getElm(expr_expr_left);
		assertNotNull(expr_expr_left_elm);
		assertTrue(expr_expr_left_elm instanceof NamedID);
		assertTrue(UtilityFunctions.getStringProperty(expr_expr_left_elm, "name").equals("test_num"));
		EObject expr_expr_right = UtilityFunctions.getRight(expr_expr);
		assertNotNull(expr_expr_right);
		assertTrue(expr_expr_right instanceof RealLitExpr);
		assertTrue(Double.parseDouble(UtilityFunctions.getStringProperty(expr_expr_right, "val")) == 2.0);
	}

	@Test
	public void testFlatmapExpr() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "	annex agree {**\r\n"
				+ "		eq test_arr : int[3] = [| 1, 2, 3 |];\r\n"
				+ "		eq test : int[3] = flatmap test_num in test_arr, [|(test_num+1)|];\r\n"
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
		EObject spec = UtilityFunctions.getSpec(contract, 1);
		assertNotNull(spec);
		assertTrue(spec instanceof EqStatement);
		EObject lhs = UtilityFunctions.getLhs(spec, 0);
		assertTrue(UtilityFunctions.getStringProperty(lhs, "name").equals("test"));
		EObject lhs_type = UtilityFunctions.getType(lhs);
		assertNotNull(lhs_type);
		assertTrue(lhs_type instanceof ArrayType);
		assertTrue(Integer.parseInt(UtilityFunctions.getStringProperty(lhs_type, "size")) == 3);
		EObject lhs_type_stem = UtilityFunctions.getStem(lhs_type);
		assertNotNull(lhs_type_stem);
		assertTrue(lhs_type_stem instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(lhs_type_stem, "name").equals("int"));
		EObject expr = UtilityFunctions.getExpr(spec);
		assertNotNull(expr);
		assertTrue(expr instanceof FlatmapExpr);
		EObject binding = UtilityFunctions.getBinding(expr);
		assertNotNull(binding);
		assertTrue(binding instanceof NamedID);
		assertTrue(UtilityFunctions.getStringProperty(binding, "name").equals("test_num"));
		EObject array = UtilityFunctions.getArray(expr);
		assertNotNull(array);
		assertTrue(array instanceof NamedElmExpr);
		EObject array_elm = UtilityFunctions.getElm(array);
		assertNotNull(array_elm);
		assertTrue(UtilityFunctions.getStringProperty(array_elm, "name").equals("test_arr"));
		EObject array_elm_type = UtilityFunctions.getType(array_elm);
		assertNotNull(array_elm_type);
		assertTrue(array_elm_type instanceof ArrayType);
		assertTrue(Integer.parseInt(UtilityFunctions.getStringProperty(array_elm_type, "size")) == 3);
		EObject array_elm_type_stem = UtilityFunctions.getStem(array_elm_type);
		assertNotNull(array_elm_type_stem);
		assertTrue(array_elm_type_stem instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(array_elm_type_stem, "name").equals("int"));
		EObject expr_expr = UtilityFunctions.getExpr(expr);
		assertNotNull(expr_expr);
		assertTrue(expr_expr instanceof ArrayLiteralExpr);
		EObject expr_expr_elem = UtilityFunctions.getElem(expr_expr, 0);
		assertNotNull(expr_expr_elem);
		assertTrue(expr_expr_elem instanceof BinaryExpr);
		assertTrue(UtilityFunctions.getStringProperty(expr_expr_elem, "op").equals("+"));
		EObject expr_expr_elem_left = UtilityFunctions.getLeft(expr_expr_elem);
		assertNotNull(expr_expr_elem_left);
		assertTrue(expr_expr_elem_left instanceof NamedElmExpr);
		EObject expr_expr_elem_left_elm = UtilityFunctions.getElm(expr_expr_elem_left);
		assertNotNull(expr_expr_elem_left_elm);
		assertTrue(expr_expr_elem_left_elm instanceof NamedID);
		assertTrue(UtilityFunctions.getStringProperty(expr_expr_elem_left_elm, "name").equals("test_num"));
		EObject expr_expr_elem_right = UtilityFunctions.getRight(expr_expr_elem);
		assertNotNull(expr_expr_elem_right);
		assertTrue(expr_expr_elem_right instanceof IntLitExpr);
		assertTrue(Integer.parseInt(UtilityFunctions.getStringProperty(expr_expr_elem_right, "val")) == 1);
	}

	@Test
	public void testFoldLeftExpr() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "	annex agree {**\r\n"
				+ "		eq test_arr : int[3] = [| 1, 2, 3 |];\r\n"
				+ "		eq test : int = foldl test_num in test_arr into test_accum = 0, test_num+test_accum;\r\n"
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
		EObject spec = UtilityFunctions.getSpec(contract, 1);
		assertNotNull(spec);
		assertTrue(spec instanceof EqStatement);
		EObject lhs = UtilityFunctions.getLhs(spec, 0);
		assertTrue(UtilityFunctions.getStringProperty(lhs, "name").equals("test"));
		EObject lhs_type = UtilityFunctions.getType(lhs);
		assertNotNull(lhs_type);
		assertTrue(lhs_type instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(lhs_type, "name").equals("int"));
		EObject expr = UtilityFunctions.getExpr(spec);
		assertNotNull(expr);
		assertTrue(expr instanceof FoldLeftExpr);
		EObject binding = UtilityFunctions.getBinding(expr);
		assertNotNull(binding);
		assertTrue(binding instanceof NamedID);
		assertTrue(UtilityFunctions.getStringProperty(binding, "name").equals("test_num"));
		EObject array = UtilityFunctions.getArray(expr);
		assertNotNull(array);
		assertTrue(array instanceof NamedElmExpr);
		EObject array_elm = UtilityFunctions.getElm(array);
		assertNotNull(array_elm);
		assertTrue(UtilityFunctions.getStringProperty(array_elm, "name").equals("test_arr"));
		EObject array_elm_type = UtilityFunctions.getType(array_elm);
		assertNotNull(array_elm_type);
		assertTrue(array_elm_type instanceof ArrayType);
		assertTrue(Integer.parseInt(UtilityFunctions.getStringProperty(array_elm_type, "size")) == 3);
		EObject array_elm_type_stem = UtilityFunctions.getStem(array_elm_type);
		assertNotNull(array_elm_type_stem);
		assertTrue(array_elm_type_stem instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(array_elm_type_stem, "name").equals("int"));
		EObject accumulator = UtilityFunctions.getAccumulator(expr);
		assertNotNull(accumulator);
		assertTrue(accumulator instanceof NamedID);
		assertTrue(UtilityFunctions.getStringProperty(accumulator, "name").equals("test_accum"));
		EObject initial = UtilityFunctions.getInitial(expr);
		assertNotNull(initial);
		assertTrue(initial instanceof IntLitExpr);
		assertTrue(Integer.parseInt(UtilityFunctions.getStringProperty(initial, "val")) == 0);
		EObject expr_expr = UtilityFunctions.getExpr(expr);
		assertNotNull(expr_expr);
		assertTrue(expr_expr instanceof BinaryExpr);
		assertTrue(UtilityFunctions.getStringProperty(expr_expr, "op").equals("+"));
		EObject expr_expr_left = UtilityFunctions.getLeft(expr_expr);
		assertNotNull(expr_expr_left);
		assertTrue(expr_expr_left instanceof NamedElmExpr);
		EObject expr_expr_left_elm = UtilityFunctions.getElm(expr_expr_left);
		assertNotNull(expr_expr_left_elm);
		assertTrue(expr_expr_left_elm instanceof NamedID);
		assertTrue(UtilityFunctions.getStringProperty(expr_expr_left_elm, "name").equals("test_num"));
		EObject expr_expr_right = UtilityFunctions.getRight(expr_expr);
		assertNotNull(expr_expr_right);
		assertTrue(expr_expr_right instanceof NamedElmExpr);
		EObject expr_expr_right_elm = UtilityFunctions.getElm(expr_expr_right);
		assertNotNull(expr_expr_right_elm);
		assertTrue(expr_expr_right_elm instanceof NamedID);
		assertTrue(UtilityFunctions.getStringProperty(expr_expr_right_elm, "name").equals("test_accum"));
	}

	@Test
	public void testFoldRightExpr() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "	annex agree {**\r\n"
				+ "		eq test_arr : int[3] = [| 1, 2, 3 |];\r\n"
				+ "		eq test : int = foldr test_num in test_arr into test_accum = 100, test_num-test_accum;\r\n"
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
		EObject spec = UtilityFunctions.getSpec(contract, 1);
		assertNotNull(spec);
		assertTrue(spec instanceof EqStatement);
		EObject lhs = UtilityFunctions.getLhs(spec, 0);
		assertTrue(UtilityFunctions.getStringProperty(lhs, "name").equals("test"));
		EObject lhs_type = UtilityFunctions.getType(lhs);
		assertNotNull(lhs_type);
		assertTrue(lhs_type instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(lhs_type, "name").equals("int"));
		EObject expr = UtilityFunctions.getExpr(spec);
		assertNotNull(expr);
		assertTrue(expr instanceof FoldRightExpr);
		EObject binding = UtilityFunctions.getBinding(expr);
		assertNotNull(binding);
		assertTrue(binding instanceof NamedID);
		assertTrue(UtilityFunctions.getStringProperty(binding, "name").equals("test_num"));
		EObject array = UtilityFunctions.getArray(expr);
		assertNotNull(array);
		assertTrue(array instanceof NamedElmExpr);
		EObject array_elm = UtilityFunctions.getElm(array);
		assertNotNull(array_elm);
		assertTrue(UtilityFunctions.getStringProperty(array_elm, "name").equals("test_arr"));
		EObject array_elm_type = UtilityFunctions.getType(array_elm);
		assertNotNull(array_elm_type);
		assertTrue(array_elm_type instanceof ArrayType);
		assertTrue(Integer.parseInt(UtilityFunctions.getStringProperty(array_elm_type, "size")) == 3);
		EObject array_elm_type_stem = UtilityFunctions.getStem(array_elm_type);
		assertNotNull(array_elm_type_stem);
		assertTrue(array_elm_type_stem instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(array_elm_type_stem, "name").equals("int"));
		EObject accumulator = UtilityFunctions.getAccumulator(expr);
		assertNotNull(accumulator);
		assertTrue(accumulator instanceof NamedID);
		assertTrue(UtilityFunctions.getStringProperty(accumulator, "name").equals("test_accum"));
		EObject initial = UtilityFunctions.getInitial(expr);
		assertNotNull(initial);
		assertTrue(initial instanceof IntLitExpr);
		assertTrue(Integer.parseInt(UtilityFunctions.getStringProperty(initial, "val")) == 100);
		EObject expr_expr = UtilityFunctions.getExpr(expr);
		assertNotNull(expr_expr);
		assertTrue(expr_expr instanceof BinaryExpr);
		assertTrue(UtilityFunctions.getStringProperty(expr_expr, "op").equals("-"));
		EObject expr_expr_left = UtilityFunctions.getLeft(expr_expr);
		assertNotNull(expr_expr_left);
		assertTrue(expr_expr_left instanceof NamedElmExpr);
		EObject expr_expr_left_elm = UtilityFunctions.getElm(expr_expr_left);
		assertNotNull(expr_expr_left_elm);
		assertTrue(expr_expr_left_elm instanceof NamedID);
		assertTrue(UtilityFunctions.getStringProperty(expr_expr_left_elm, "name").equals("test_num"));
		EObject expr_expr_right = UtilityFunctions.getRight(expr_expr);
		assertNotNull(expr_expr_right);
		assertTrue(expr_expr_right instanceof NamedElmExpr);
		EObject expr_expr_right_elm = UtilityFunctions.getElm(expr_expr_right);
		assertNotNull(expr_expr_right_elm);
		assertTrue(expr_expr_right_elm instanceof NamedID);
		assertTrue(UtilityFunctions.getStringProperty(expr_expr_right_elm, "name").equals("test_accum"));
	}

	@Test
	public void testArrowExpr() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "	annex agree{**\r\n"
				+ "		property test = true -> false;\r\n"
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
		assertTrue(spec instanceof PropertyStatement);
		assertTrue(UtilityFunctions.getStringProperty(spec, "name").equals("test"));
		EObject expr = UtilityFunctions.getExpr(spec);
		assertNotNull(expr);
		assertTrue(expr instanceof BinaryExpr);
		assertTrue(UtilityFunctions.getStringProperty(expr, "op").equals("->"));
		EObject left = UtilityFunctions.getLeft(expr);
		assertNotNull(left);
		assertTrue(left instanceof BoolLitExpr);
		EObject val_left = UtilityFunctions.getVal(left);
		assertNotNull(val_left);
		assertTrue(val_left instanceof BooleanLiteral);
		assertTrue(Boolean.parseBoolean(UtilityFunctions.getStringProperty(val_left, "value")));
		EObject right = UtilityFunctions.getRight(expr);
		assertNotNull(right);
		assertTrue(right instanceof BoolLitExpr);
		EObject val_right = UtilityFunctions.getVal(right);
		assertNotNull(val_right);
		assertTrue(val_right instanceof BooleanLiteral);
		assertFalse(Boolean.parseBoolean(UtilityFunctions.getStringProperty(val_right, "value")));
	}

	@Test
	public void testImpliesExpr() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "	annex agree{**\r\n"
				+ "		property test = true => false;\r\n"
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
		assertTrue(spec instanceof PropertyStatement);
		assertTrue(UtilityFunctions.getStringProperty(spec, "name").equals("test"));
		EObject expr = UtilityFunctions.getExpr(spec);
		assertNotNull(expr);
		assertTrue(expr instanceof BinaryExpr);
		assertTrue(UtilityFunctions.getStringProperty(expr, "op").equals("=>"));
		EObject left = UtilityFunctions.getLeft(expr);
		assertNotNull(left);
		assertTrue(left instanceof BoolLitExpr);
		EObject val_left = UtilityFunctions.getVal(left);
		assertNotNull(val_left);
		assertTrue(val_left instanceof BooleanLiteral);
		assertTrue(Boolean.parseBoolean(UtilityFunctions.getStringProperty(val_left, "value")));
		EObject right = UtilityFunctions.getRight(expr);
		assertNotNull(right);
		assertTrue(right instanceof BoolLitExpr);
		EObject val_right = UtilityFunctions.getVal(right);
		assertNotNull(val_right);
		assertTrue(val_right instanceof BooleanLiteral);
		assertFalse(Boolean.parseBoolean(UtilityFunctions.getStringProperty(val_right, "value")));
	}

	@Test
	public void testEquivExpr() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "	annex agree{**\r\n"
				+ "		property test = true <=> false;\r\n"
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
		assertTrue(spec instanceof PropertyStatement);
		assertTrue(UtilityFunctions.getStringProperty(spec, "name").equals("test"));
		EObject expr = UtilityFunctions.getExpr(spec);
		assertNotNull(expr);
		assertTrue(expr instanceof BinaryExpr);
		assertTrue(UtilityFunctions.getStringProperty(expr, "op").equals("<=>"));
		EObject left = UtilityFunctions.getLeft(expr);
		assertNotNull(left);
		assertTrue(left instanceof BoolLitExpr);
		EObject val_left = UtilityFunctions.getVal(left);
		assertNotNull(val_left);
		assertTrue(val_left instanceof BooleanLiteral);
		assertTrue(Boolean.parseBoolean(UtilityFunctions.getStringProperty(val_left, "value")));
		EObject right = UtilityFunctions.getRight(expr);
		assertNotNull(right);
		assertTrue(right instanceof BoolLitExpr);
		EObject val_right = UtilityFunctions.getVal(right);
		assertNotNull(val_right);
		assertTrue(val_right instanceof BooleanLiteral);
		assertFalse(Boolean.parseBoolean(UtilityFunctions.getStringProperty(val_right, "value")));
	}

	@Test
	public void testOrExpr() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "	annex agree{**\r\n"
				+ "		property test = true or false;\r\n"
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
		assertTrue(spec instanceof PropertyStatement);
		assertTrue(UtilityFunctions.getStringProperty(spec, "name").equals("test"));
		EObject expr = UtilityFunctions.getExpr(spec);
		assertNotNull(expr);
		assertTrue(expr instanceof BinaryExpr);
		assertTrue(UtilityFunctions.getStringProperty(expr, "op").equals("or"));
		EObject left = UtilityFunctions.getLeft(expr);
		assertNotNull(left);
		assertTrue(left instanceof BoolLitExpr);
		EObject val_left = UtilityFunctions.getVal(left);
		assertNotNull(val_left);
		assertTrue(val_left instanceof BooleanLiteral);
		assertTrue(Boolean.parseBoolean(UtilityFunctions.getStringProperty(val_left, "value")));
		EObject right = UtilityFunctions.getRight(expr);
		assertNotNull(right);
		assertTrue(right instanceof BoolLitExpr);
		EObject val_right = UtilityFunctions.getVal(right);
		assertNotNull(val_right);
		assertTrue(val_right instanceof BooleanLiteral);
		assertFalse(Boolean.parseBoolean(UtilityFunctions.getStringProperty(val_right, "value")));
	}

	@Test
	public void testAndExpr() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "	annex agree{**\r\n"
				+ "		property test = true and false;\r\n"
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
		assertTrue(spec instanceof PropertyStatement);
		assertTrue(UtilityFunctions.getStringProperty(spec, "name").equals("test"));
		EObject expr = UtilityFunctions.getExpr(spec);
		assertNotNull(expr);
		assertTrue(expr instanceof BinaryExpr);
		assertTrue(UtilityFunctions.getStringProperty(expr, "op").equals("and"));
		EObject left = UtilityFunctions.getLeft(expr);
		assertNotNull(left);
		assertTrue(left instanceof BoolLitExpr);
		EObject val_left = UtilityFunctions.getVal(left);
		assertNotNull(val_left);
		assertTrue(val_left instanceof BooleanLiteral);
		assertTrue(Boolean.parseBoolean(UtilityFunctions.getStringProperty(val_left, "value")));
		EObject right = UtilityFunctions.getRight(expr);
		assertNotNull(right);
		assertTrue(right instanceof BoolLitExpr);
		EObject val_right = UtilityFunctions.getVal(right);
		assertNotNull(val_right);
		assertTrue(val_right instanceof BooleanLiteral);
		assertFalse(Boolean.parseBoolean(UtilityFunctions.getStringProperty(val_right, "value")));
	}

	@Test
	public void testRelateExpr() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "	annex agree{**\r\n"
				+ "		property test = 4.0 != 4.1;\r\n"
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
		assertTrue(spec instanceof PropertyStatement);
		assertTrue(UtilityFunctions.getStringProperty(spec, "name").equals("test"));
		EObject expr = UtilityFunctions.getExpr(spec);
		assertNotNull(expr);
		assertTrue(expr instanceof BinaryExpr);
		assertTrue(UtilityFunctions.getStringProperty(expr, "op").equals("!="));
		EObject left = UtilityFunctions.getLeft(expr);
		assertNotNull(left);
		assertTrue(left instanceof RealLitExpr);
		assertTrue(Double.parseDouble(UtilityFunctions.getStringProperty(left, "val")) == 4.0);
		EObject right = UtilityFunctions.getRight(expr);
		assertNotNull(right);
		assertTrue(right instanceof RealLitExpr);
		assertTrue(Double.parseDouble(UtilityFunctions.getStringProperty(right, "val")) == 4.1);
	}

	@Test
	public void testAddSubExpr() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "	annex agree{**\r\n"
				+ "		const test : real = 1.0 + 2.0;\r\n"
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
		assertTrue(spec instanceof ConstStatement);
		assertTrue(UtilityFunctions.getStringProperty(spec, "name").equals("test"));
		EObject spec_type = UtilityFunctions.getType(spec);
		assertNotNull(spec_type);
		assertTrue(spec_type instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(spec_type, "name").equals("real"));
		EObject expr = UtilityFunctions.getExpr(spec);
		assertNotNull(expr);
		assertTrue(expr instanceof BinaryExpr);
		assertTrue(UtilityFunctions.getStringProperty(expr, "op").equals("+"));
		EObject left = UtilityFunctions.getLeft(expr);
		assertNotNull(left);
		assertTrue(left instanceof RealLitExpr);
		assertTrue(Double.parseDouble(UtilityFunctions.getStringProperty(left, "val")) == 1.0);
		EObject right = UtilityFunctions.getRight(expr);
		assertNotNull(right);
		assertTrue(right instanceof RealLitExpr);
		assertTrue(Double.parseDouble(UtilityFunctions.getStringProperty(right, "val")) == 2.0);
	}

	@Test
	public void testMultDivExpr() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "	annex agree{**\r\n"
				+ "		const test : real = 10.0/3.3;\r\n"
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
		assertTrue(spec instanceof ConstStatement);
		assertTrue(UtilityFunctions.getStringProperty(spec, "name").equals("test"));
		EObject spec_type = UtilityFunctions.getType(spec);
		assertNotNull(spec_type);
		assertTrue(spec_type instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(spec_type, "name").equals("real"));
		EObject expr = UtilityFunctions.getExpr(spec);
		assertNotNull(expr);
		assertTrue(expr instanceof BinaryExpr);
		assertTrue(UtilityFunctions.getStringProperty(expr, "op").equals("/"));
		EObject left = UtilityFunctions.getLeft(expr);
		assertNotNull(left);
		assertTrue(left instanceof RealLitExpr);
		assertTrue(Double.parseDouble(UtilityFunctions.getStringProperty(left, "val")) == 10.0);
		EObject right = UtilityFunctions.getRight(expr);
		assertNotNull(right);
		assertTrue(right instanceof RealLitExpr);
		assertTrue(Double.parseDouble(UtilityFunctions.getStringProperty(right, "val")) == 3.3);
	}

	@Test
	public void testPowerExpr() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "	annex agree{**\r\n"
				+ "		const test : int = 5^2;\r\n"
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
		assertTrue(spec instanceof ConstStatement);
		assertTrue(UtilityFunctions.getStringProperty(spec, "name").equals("test"));
		EObject spec_type = UtilityFunctions.getType(spec);
		assertNotNull(spec_type);
		assertTrue(spec_type instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(spec_type, "name").equals("int"));
		EObject expr = UtilityFunctions.getExpr(spec);
		assertNotNull(expr);
		assertTrue(expr instanceof BinaryExpr);
		assertTrue(UtilityFunctions.getStringProperty(expr, "op").equals("^"));
		EObject left = UtilityFunctions.getLeft(expr);
		assertNotNull(left);
		assertTrue(left instanceof IntLitExpr);
		assertTrue(Integer.parseInt(UtilityFunctions.getStringProperty(left, "val")) == 5);
		EObject right = UtilityFunctions.getRight(expr);
		assertNotNull(right);
		assertTrue(right instanceof IntLitExpr);
		assertTrue(Integer.parseInt(UtilityFunctions.getStringProperty(right, "val")) == 2);
	}

	@Test
	public void testUnaryExpr() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "	annex agree{**\r\n"
				+ "		property test = not false;\r\n"
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
		assertTrue(spec instanceof PropertyStatement);
		assertTrue(UtilityFunctions.getStringProperty(spec, "name").equals("test"));
		EObject expr = UtilityFunctions.getExpr(spec);
		assertNotNull(expr);
		assertTrue(expr instanceof UnaryExpr);
		assertTrue(UtilityFunctions.getStringProperty(expr, "op").equals("not"));
		EObject expr_expr = UtilityFunctions.getExpr(expr);
		assertNotNull(expr_expr);
		assertTrue(expr_expr instanceof BoolLitExpr);
		EObject val = UtilityFunctions.getVal(expr_expr);
		assertNotNull(val);
		assertTrue(val instanceof BooleanLiteral);
		assertFalse(Boolean.parseBoolean(UtilityFunctions.getStringProperty(val, "value")));
	}

	@Test
	public void testIfThenElseExpr() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "	annex agree{**\r\n"
				+ "		property test = if true then false else true;\r\n"
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
		assertTrue(spec instanceof PropertyStatement);
		assertTrue(UtilityFunctions.getStringProperty(spec, "name").equals("test"));
		EObject expr = UtilityFunctions.getExpr(spec);
		assertNotNull(expr);
		assertTrue(expr instanceof IfThenElseExpr);
		EObject expr_a = UtilityFunctions.getA(expr);
		assertNotNull(expr_a);
		assertTrue(expr_a instanceof BoolLitExpr);
		EObject val_a = UtilityFunctions.getVal(expr_a);
		assertNotNull(val_a);
		assertTrue(val_a instanceof BooleanLiteral);
		assertTrue(Boolean.parseBoolean(UtilityFunctions.getStringProperty(val_a, "value")));
		EObject expr_b = UtilityFunctions.getB(expr);
		assertNotNull(expr_b);
		assertTrue(expr_b instanceof BoolLitExpr);
		EObject val_b = UtilityFunctions.getVal(expr_b);
		assertNotNull(val_b);
		assertTrue(val_b instanceof BooleanLiteral);
		assertFalse(Boolean.parseBoolean(UtilityFunctions.getStringProperty(val_b, "value")));
		EObject expr_c = UtilityFunctions.getC(expr);
		assertNotNull(expr_c);
		assertTrue(expr_c instanceof BoolLitExpr);
		EObject val_c = UtilityFunctions.getVal(expr_c);
		assertNotNull(val_c);
		assertTrue(val_c instanceof BooleanLiteral);
		assertTrue(Boolean.parseBoolean(UtilityFunctions.getStringProperty(val_c, "value")));
	}

	@Test
	public void testPrevExpr() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "	annex agree{**\r\n"
				+ "		eq test : int = prev(test, 0);\r\n"
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
		assertTrue(spec instanceof EqStatement);
		EObject lhs = UtilityFunctions.getLhs(spec, 0);
		assertTrue(UtilityFunctions.getStringProperty(lhs, "name").equals("test"));
		EObject lhs_type = UtilityFunctions.getType(lhs);
		assertNotNull(lhs_type);
		assertTrue(lhs_type instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(lhs_type, "name").equals("int"));
		EObject expr = UtilityFunctions.getExpr(spec);
		assertNotNull(expr);
		assertTrue(expr instanceof PrevExpr);
		EObject delay = UtilityFunctions.getDelay(expr);
		assertNotNull(delay);
		assertTrue(delay instanceof NamedElmExpr);
		EObject elm = UtilityFunctions.getElm(delay);
		assertNotNull(elm);
		assertTrue(UtilityFunctions.getStringProperty(elm, "name").equals("test"));
		EObject elm_type = UtilityFunctions.getType(elm);
		assertNotNull(elm_type);
		assertTrue(elm_type instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(elm_type, "name").equals("int"));
		EObject init = UtilityFunctions.getInit(expr);
		assertNotNull(init);
		assertTrue(init instanceof IntLitExpr);
		assertTrue(Integer.parseInt(UtilityFunctions.getStringProperty(init, "val")) == 0);
	}

	@Test
	public void testGetPropertyExpr() throws Exception {
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
		assertTrue(spec instanceof ConstStatement);
		assertTrue(UtilityFunctions.getStringProperty(spec, "name").equals("test"));
		EObject spec_type = UtilityFunctions.getType(spec);
		assertNotNull(spec_type);
		assertTrue(spec_type instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(spec_type, "name").equals("real"));
		EObject expr = UtilityFunctions.getExpr(spec);
		assertNotNull(expr);
		assertTrue(expr instanceof GetPropertyExpr);
		EObject component_ref = UtilityFunctions.getComponentRef(expr);
		assertNotNull(component_ref);
		assertTrue(component_ref instanceof ThisRef);
		EObject property = UtilityFunctions.getProp(expr);
		assertNotNull(property);
		assertTrue(property instanceof Property);
		assertTrue(UtilityFunctions.getStringProperty(property, "name").equals("NetWeight"));
	}

	@Test
	public void testArrayUpdateExpr() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "	annex agree {**\r\n"
				+ "		eq test_arr : int[3] = test_arr[| 1:=5 |];\r\n"
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
		assertTrue(spec instanceof EqStatement);
		EObject lhs = UtilityFunctions.getLhs(spec, 0);
		assertTrue(UtilityFunctions.getStringProperty(lhs, "name").equals("test_arr"));
		EObject lhs_type = UtilityFunctions.getType(lhs);
		assertNotNull(lhs_type);
		assertTrue(lhs_type instanceof ArrayType);
		assertTrue(Integer.parseInt(UtilityFunctions.getStringProperty(lhs_type, "size")) == 3);
		EObject lhs_type_stem = UtilityFunctions.getStem(lhs_type);
		assertNotNull(lhs_type_stem);
		assertTrue(lhs_type_stem instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(lhs_type_stem, "name").equals("int"));
		EObject expr = UtilityFunctions.getExpr(spec);
		assertNotNull(expr);
		assertTrue(expr instanceof ArrayUpdateExpr);
		EObject array = UtilityFunctions.getArray(expr);
		assertNotNull(array);
		assertTrue(array instanceof NamedElmExpr);
		EObject array_elm = UtilityFunctions.getElm(array);
		assertNotNull(array_elm);
		assertTrue(UtilityFunctions.getStringProperty(array_elm, "name").equals("test_arr"));
		EObject array_elm_type = UtilityFunctions.getType(array_elm);
		assertNotNull(array_elm_type);
		assertTrue(array_elm_type instanceof ArrayType);
		assertTrue(Integer.parseInt(UtilityFunctions.getStringProperty(array_elm_type, "size")) == 3);
		EObject array_elm_type_stem = UtilityFunctions.getStem(array_elm_type);
		assertNotNull(array_elm_type_stem);
		assertTrue(array_elm_type_stem instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(array_elm_type_stem, "name").equals("int"));
		EObject index = UtilityFunctions.getIndex(expr, 0);
		assertNotNull(index);
		assertTrue(index instanceof IntLitExpr);
		assertTrue(Integer.parseInt(UtilityFunctions.getStringProperty(index, "val")) == 1);
		EObject valueExpr = UtilityFunctions.getValueExpr(expr, 0);
		assertNotNull(valueExpr);
		assertTrue(valueExpr instanceof IntLitExpr);
		assertTrue(Integer.parseInt(UtilityFunctions.getStringProperty(valueExpr, "val")) == 5);
	}

	@Test
	public void testRecordUpdateExpr() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
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
		EObject aadl_package_impl = issueCollection.getResource().getContents().get(0);
		EObject pub_sec = UtilityFunctions.getownedPublicSection(aadl_package_impl);
		EObject owned_classifiers = UtilityFunctions.getownedClassifier(pub_sec, "sys");
		EObject annex_subclause = UtilityFunctions.getAnnexSubclause(owned_classifiers, "agree");
		EObject parsed_annex_subclause = UtilityFunctions.getParsedAnnexSubclause(annex_subclause);
		EObject contract = UtilityFunctions.getContract(parsed_annex_subclause);
		assertNotNull(contract);
		assertTrue(contract instanceof AgreeContract);
		EObject spec = UtilityFunctions.getSpec(contract, 2);
		assertNotNull(spec);
		assertTrue(spec instanceof EqStatement);
		EObject lhs = UtilityFunctions.getLhs(spec, 0);
		assertNotNull(lhs);
		assertTrue(UtilityFunctions.getStringProperty(lhs, "name").equals("foo2"));
		EObject lhs_type = UtilityFunctions.getType(lhs);
		assertNotNull(lhs_type);
		assertTrue(lhs_type instanceof DoubleDotRef);
		EObject lhs_type_elm = UtilityFunctions.getElm(lhs_type);
		assertNotNull(lhs_type_elm);
		assertTrue(lhs_type_elm instanceof RecordDef);
		assertTrue(UtilityFunctions.getStringProperty(lhs_type_elm, "name").equals("test"));
		EObject lhs_type_elm_arg = UtilityFunctions.getArg(lhs_type_elm, 0);
		assertNotNull(lhs_type_elm_arg);
		assertTrue(UtilityFunctions.getStringProperty(lhs_type_elm_arg, "name").equals("a"));
		EObject lhs_type_elm_arg_type = UtilityFunctions.getType(lhs_type_elm_arg);
		assertNotNull(lhs_type_elm_arg_type);
		assertTrue(lhs_type_elm_arg_type instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(lhs_type_elm_arg_type, "name").equals("bool"));
		EObject expr = UtilityFunctions.getExpr(spec);
		assertNotNull(expr);
		assertTrue(expr instanceof RecordUpdateExpr);
		EObject expr_record = UtilityFunctions.getRecord(expr);
		assertNotNull(expr_record);
		assertTrue(expr_record instanceof NamedElmExpr);
		EObject expr_record_elm = UtilityFunctions.getElm(expr_record);
		assertNotNull(expr_record_elm);
		assertTrue(UtilityFunctions.getStringProperty(expr_record_elm, "name").equals("foo"));
		EObject expr_record_elm_type = UtilityFunctions.getType(expr_record_elm);
		assertNotNull(expr_record_elm_type);
		assertTrue(expr_record_elm_type instanceof DoubleDotRef);
		EObject expr_record_elm_type_elm = UtilityFunctions.getElm(expr_record_elm_type);
		assertNotNull(expr_record_elm_type_elm);
		assertTrue(expr_record_elm_type_elm instanceof RecordDef);
		assertTrue(UtilityFunctions.getStringProperty(expr_record_elm_type_elm, "name").equals("test"));
		EObject expr_record_elm_type_elm_arg = UtilityFunctions.getArg(expr_record_elm_type_elm, 0);
		assertNotNull(expr_record_elm_type_elm_arg);
		assertTrue(UtilityFunctions.getStringProperty(expr_record_elm_type_elm_arg, "name").equals("a"));
		EObject expr_record_elm_type_elm_arg_type = UtilityFunctions.getType(expr_record_elm_type_elm_arg);
		assertNotNull(expr_record_elm_type_elm_arg_type);
		assertTrue(expr_record_elm_type_elm_arg_type instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(expr_record_elm_type_elm_arg_type, "name").equals("bool"));
		EObject expr_expr = UtilityFunctions.getExpr(expr);
		assertNotNull(expr_expr);
		assertTrue(expr_expr instanceof BoolLitExpr);
		EObject expr_expr_val = UtilityFunctions.getVal(expr_expr);
		assertNotNull(expr_expr_val);
		assertTrue(expr_expr_val instanceof BooleanLiteral);
		assertFalse(Boolean.parseBoolean(UtilityFunctions.getStringProperty(expr_expr_val, "value")));
		EObject expr_key = UtilityFunctions.getKey(expr);
		assertNotNull(expr_key);
		assertTrue(UtilityFunctions.getStringProperty(expr_key, "name").equals("a"));
		EObject expr_key_type = UtilityFunctions.getType(expr_key);
		assertNotNull(expr_key_type);
		assertTrue(expr_key_type instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(expr_key_type, "name").equals("bool"));
	}

	@Test
	public void testArraySubExpr() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "	annex agree {**\r\n"
				+ "		eq test_arr : real[3];\r\n"
				+ "		eq test : real = test_arr[1];\r\n"
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
		EObject spec = UtilityFunctions.getSpec(contract, 1);
		assertNotNull(spec);
		assertTrue(spec instanceof EqStatement);
		EObject lhs = UtilityFunctions.getLhs(spec, 0);
		assertTrue(UtilityFunctions.getStringProperty(lhs, "name").equals("test"));
		EObject lhs_type = UtilityFunctions.getType(lhs);
		assertNotNull(lhs_type);
		assertTrue(lhs_type instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(lhs_type, "name").equals("real"));
		EObject expr = UtilityFunctions.getExpr(spec);
		assertNotNull(expr);
		assertTrue(expr instanceof ArraySubExpr);
		EObject expr_expr = UtilityFunctions.getExpr(expr);
		assertNotNull(expr_expr);
		assertTrue(expr_expr instanceof NamedElmExpr);
		EObject elm = UtilityFunctions.getElm(expr_expr);
		assertNotNull(elm);
		assertTrue(UtilityFunctions.getStringProperty(elm, "name").equals("test_arr"));
		EObject elm_type = UtilityFunctions.getType(elm);
		assertNotNull(elm_type);
		assertTrue(elm_type instanceof ArrayType);
		assertTrue(Integer.parseInt(UtilityFunctions.getStringProperty(elm_type, "size")) == 3);
		EObject elm_type_stem = UtilityFunctions.getStem(elm_type);
		assertNotNull(elm_type_stem);
		assertTrue(elm_type_stem instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(elm_type_stem, "name").equals("real"));
		EObject index = UtilityFunctions.getIndex(expr);
		assertNotNull(index);
		assertTrue(index instanceof IntLitExpr);
		assertTrue(Integer.parseInt(UtilityFunctions.getStringProperty(index, "val")) == 1);
	}

	@Test
	public void testTagExpr() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "	features\r\n"
				+ "		data_out : out event port;\r\n"
				+ "	annex agree {**\r\n"
				+ "		eq test : bool = data_out._CLK;\r\n"
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
		assertTrue(spec instanceof EqStatement);
		EObject lhs = UtilityFunctions.getLhs(spec, 0);
		assertTrue(UtilityFunctions.getStringProperty(lhs, "name").equals("test"));
		EObject lhs_type = UtilityFunctions.getType(lhs);
		assertNotNull(lhs_type);
		assertTrue(lhs_type instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(lhs_type, "name").equals("bool"));
		EObject expr = UtilityFunctions.getExpr(spec);
		assertNotNull(expr);
		assertTrue(expr instanceof TagExpr);
		assertTrue(UtilityFunctions.getStringProperty(expr, "tag").equals("_CLK"));
		EObject stem = UtilityFunctions.getStem(expr);
		assertNotNull(stem);
		assertTrue(stem instanceof NamedElmExpr);
		EObject stem_elm = UtilityFunctions.getElm(stem);
		assertNotNull(stem_elm);
		assertTrue(stem_elm instanceof EventPort);
		assertTrue(UtilityFunctions.getStringProperty(stem_elm, "name").equals("data_out"));
		assertFalse(Boolean.parseBoolean(UtilityFunctions.getStringProperty(stem_elm, "in")));
		assertTrue(Boolean.parseBoolean(UtilityFunctions.getStringProperty(stem_elm, "out")));
	}

	@Test
	public void testSelectionExpr() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "system implementation sys.impl\r\n"
				+ "	subcomponents\r\n"
				+ "		this_sensor : device TestSensor;\r\n"
				+ "	annex agree {**\r\n"
				+ "		eq test : bool = this_sensor.data_in._CLK;\r\n"
				+ "	**};\r\n"
				+ "end sys.impl;\r\n"
				+ "\r\n"
				+ "device TestSensor\r\n"
				+ "	features\r\n"
				+ "		data_in : in event port;\r\n"
				+ "end TestSensor;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		EObject aadl_package_impl = issueCollection.getResource().getContents().get(0);
		EObject pub_sec = UtilityFunctions.getownedPublicSection(aadl_package_impl);
		EObject owned_classifiers = UtilityFunctions.getownedClassifier(pub_sec, "sys.impl");
		EObject annex_subclause = UtilityFunctions.getAnnexSubclause(owned_classifiers, "agree");
		EObject parsed_annex_subclause = UtilityFunctions.getParsedAnnexSubclause(annex_subclause);
		EObject contract = UtilityFunctions.getContract(parsed_annex_subclause);
		assertNotNull(contract);
		assertTrue(contract instanceof AgreeContract);
		EObject spec = UtilityFunctions.getSpec(contract, 0);
		assertNotNull(spec);
		assertTrue(spec instanceof EqStatement);
		EObject lhs = UtilityFunctions.getLhs(spec, 0);
		assertTrue(UtilityFunctions.getStringProperty(lhs, "name").equals("test"));
		EObject lhs_type = UtilityFunctions.getType(lhs);
		assertNotNull(lhs_type);
		assertTrue(lhs_type instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(lhs_type, "name").equals("bool"));
		EObject expr = UtilityFunctions.getExpr(spec);
		assertNotNull(expr);
		assertTrue(expr instanceof TagExpr);
		assertTrue(UtilityFunctions.getStringProperty(expr, "tag").equals("_CLK"));
		EObject stem = UtilityFunctions.getStem(expr);
		assertNotNull(stem);
		assertTrue(stem instanceof SelectionExpr);
		EObject stem_field = UtilityFunctions.getField(stem);
		assertNotNull(stem_field);
		assertTrue(stem_field instanceof EventPort);
		assertTrue(UtilityFunctions.getStringProperty(stem_field, "name").equals("data_in"));
		assertTrue(Boolean.parseBoolean(UtilityFunctions.getStringProperty(stem_field, "in")));
		assertFalse(Boolean.parseBoolean(UtilityFunctions.getStringProperty(stem_field, "out")));
		EObject stem_target = UtilityFunctions.getTarget(stem);
		assertNotNull(stem_target);
		assertTrue(stem_target instanceof NamedElmExpr);
		EObject stem_target_elm = UtilityFunctions.getElm(stem_target);
		assertNotNull(stem_target_elm);
		assertTrue(stem_target_elm instanceof DeviceSubcomponent);
		assertTrue(UtilityFunctions.getStringProperty(stem_target_elm, "name").equals("this_sensor"));
	}

	@Test
	public void testTimeExpr() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "	\r\n"
				+ "system sys\r\n"
				+ "	annex agree {**\r\n"
				+ "		eq test : real = time;\r\n"
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
		assertTrue(spec instanceof EqStatement);
		EObject lhs = UtilityFunctions.getLhs(spec, 0);
		assertTrue(UtilityFunctions.getStringProperty(lhs, "name").equals("test"));
		EObject lhs_type = UtilityFunctions.getType(lhs);
		assertNotNull(lhs_type);
		assertTrue(lhs_type instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(lhs_type, "name").equals("real"));
		EObject expr = UtilityFunctions.getExpr(spec);
		assertNotNull(expr);
		assertTrue(expr instanceof TimeExpr);
	}

	@Test
	public void testIndicesExpr() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "	annex agree {**\r\n"
				+ "		eq test_arr : int[3] = [| 1, 2, 3 |];\r\n"
				+ "		eq test : int[3] = indices(test_arr);\r\n"
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
		EObject spec = UtilityFunctions.getSpec(contract, 1);
		assertNotNull(spec);
		assertTrue(spec instanceof EqStatement);
		EObject lhs = UtilityFunctions.getLhs(spec, 0);
		assertTrue(UtilityFunctions.getStringProperty(lhs, "name").equals("test"));
		EObject lhs_type = UtilityFunctions.getType(lhs);
		assertNotNull(lhs_type);
		assertTrue(lhs_type instanceof ArrayType);
		assertTrue(Integer.parseInt(UtilityFunctions.getStringProperty(lhs_type, "size")) == 3);
		EObject lhs_type_stem = UtilityFunctions.getStem(lhs_type);
		assertNotNull(lhs_type_stem);
		assertTrue(lhs_type_stem instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(lhs_type_stem, "name").equals("int"));
		EObject expr = UtilityFunctions.getExpr(spec);
		assertNotNull(expr);
		assertTrue(expr instanceof IndicesExpr);
		EObject array = UtilityFunctions.getArray(expr);
		assertNotNull(array);
		assertTrue(array instanceof NamedElmExpr);
		EObject array_elm = UtilityFunctions.getElm(array);
		assertNotNull(array_elm);
		assertTrue(UtilityFunctions.getStringProperty(array_elm, "name").equals("test_arr"));
		EObject array_elm_type = UtilityFunctions.getType(array_elm);
		assertNotNull(array_elm_type);
		assertTrue(array_elm_type instanceof ArrayType);
		assertTrue(Integer.parseInt(UtilityFunctions.getStringProperty(array_elm_type, "size")) == 3);
		EObject array_elm_type_stem = UtilityFunctions.getStem(array_elm_type);
		assertNotNull(array_elm_type_stem);
		assertTrue(array_elm_type_stem instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(array_elm_type_stem, "name").equals("int"));
	}

	@Test
	public void testCallExpr() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "	annex agree {**\r\n"
				+ "		fun test_fn(x : real) : bool = false;\r\n"
				+ "		property test = test_fn(1.0);\r\n"
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
		EObject spec = UtilityFunctions.getSpec(contract, 1);
		assertNotNull(spec);
		assertTrue(spec instanceof PropertyStatement);
		assertTrue(UtilityFunctions.getStringProperty(spec, "name").equals("test"));
		EObject expr = UtilityFunctions.getExpr(spec);
		assertNotNull(expr);
		assertTrue(expr instanceof CallExpr);
		EObject arg = UtilityFunctions.getArg(expr, 0);
		assertNotNull(arg);
		assertTrue(arg instanceof RealLitExpr);
		assertTrue(Double.parseDouble(UtilityFunctions.getStringProperty(arg, "val")) == 1.0);
		EObject ref = UtilityFunctions.getRef(expr);
		assertNotNull(ref);
		assertTrue(ref instanceof DoubleDotRef);
		EObject ref_elm = UtilityFunctions.getElm(ref);
		assertNotNull(ref_elm);
		assertTrue(ref_elm instanceof FnDef);
		assertTrue(UtilityFunctions.getStringProperty(ref_elm, "name").equals("test_fn"));
		EObject ref_elm_arg = UtilityFunctions.getArg(ref_elm, 0);
		assertNotNull(ref_elm_arg);
		assertTrue(UtilityFunctions.getStringProperty(ref_elm_arg, "name").equals("x"));
		EObject ref_elm_arg_type = UtilityFunctions.getType(ref_elm_arg);
		assertNotNull(ref_elm_arg_type);
		assertTrue(ref_elm_arg_type instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(ref_elm_arg_type, "name").equals("real"));
		EObject ref_elm_expr = UtilityFunctions.getExpr(ref_elm);
		assertNotNull(ref_elm_expr);
		assertTrue(ref_elm_expr instanceof BoolLitExpr);
		EObject ref_elm_expr_val = UtilityFunctions.getVal(ref_elm_expr);
		assertNotNull(ref_elm_expr_val);
		assertTrue(ref_elm_expr_val instanceof BooleanLiteral);
		assertFalse(Boolean.parseBoolean(UtilityFunctions.getStringProperty(ref_elm_expr_val, "value")));
	}

	@Test
	public void testRecordLitExpr() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "	annex agree {**\r\n"
				+ "		type test = struct{a : int};\r\n"
				+ "		eq foo : test = test{a = 3};\r\n"
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
		EObject spec = UtilityFunctions.getSpec(contract, 1);
		assertNotNull(spec);
		assertTrue(spec instanceof EqStatement);
		EObject lhs = UtilityFunctions.getLhs(spec, 0);
		assertNotNull(lhs);
		assertTrue(UtilityFunctions.getStringProperty(lhs, "name").equals("foo"));
		EObject lhs_type = UtilityFunctions.getType(lhs);
		assertNotNull(lhs_type);
		assertTrue(lhs_type instanceof DoubleDotRef);
		EObject lhs_type_elm = UtilityFunctions.getElm(lhs_type);
		assertNotNull(lhs_type_elm);
		assertTrue(lhs_type_elm instanceof RecordDef);
		assertTrue(UtilityFunctions.getStringProperty(lhs_type_elm, "name").equals("test"));
		EObject lhs_type_elm_arg = UtilityFunctions.getArg(lhs_type_elm, 0);
		assertNotNull(lhs_type_elm_arg);
		assertTrue(UtilityFunctions.getStringProperty(lhs_type_elm_arg, "name").equals("a"));
		EObject lhs_type_elm_arg_type = UtilityFunctions.getType(lhs_type_elm_arg);
		assertNotNull(lhs_type_elm_arg_type);
		assertTrue(lhs_type_elm_arg_type instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(lhs_type_elm_arg_type, "name").equals("int"));
		EObject expr = UtilityFunctions.getExpr(spec);
		assertNotNull(expr);
		assertTrue(expr instanceof RecordLitExpr);
		EObject expr_arg = UtilityFunctions.getArg(expr, 0);
		assertNotNull(expr_arg);
		assertTrue(UtilityFunctions.getStringProperty(expr_arg, "name").equals("a"));
		EObject expr_arg_type = UtilityFunctions.getType(expr_arg);
		assertNotNull(expr_arg_type);
		assertTrue(expr_arg_type instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(expr_arg_type, "name").equals("int"));
		EObject expr_arg_expr = UtilityFunctions.getArgExpr(expr, 0);
		assertNotNull(expr_arg_expr);
		assertTrue(Integer.parseInt(UtilityFunctions.getStringProperty(expr_arg_expr, "val")) == 3);
		EObject expr_record_type = UtilityFunctions.getRecordType(expr);
		assertTrue(expr_record_type instanceof DoubleDotRef);
		EObject expr_record_type_elm = UtilityFunctions.getElm(expr_record_type);
		assertNotNull(expr_record_type_elm);
		assertTrue(expr_record_type_elm instanceof RecordDef);
		assertTrue(UtilityFunctions.getStringProperty(expr_record_type_elm, "name").equals("test"));
		EObject expr_record_type_elm_arg = UtilityFunctions.getArg(expr_record_type_elm, 0);
		assertNotNull(expr_record_type_elm_arg);
		assertTrue(UtilityFunctions.getStringProperty(expr_record_type_elm_arg, "name").equals("a"));
		EObject expr_record_type_elm_arg_type = UtilityFunctions.getType(expr_record_type_elm_arg);
		assertNotNull(expr_record_type_elm_arg_type);
		assertTrue(expr_record_type_elm_arg_type instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(expr_record_type_elm_arg_type, "name").equals("int"));
	}

	@Test
	public void testEnumLitExpr() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "	with Data_Model;\r\n"
				+ "\r\n"
				+ "data Primary_Color\r\n"
				+ "	properties\r\n"
				+ "		Data_Model::Data_Representation => Enum;\r\n"
				+ "		Data_Model::Enumerators => (\"Red\", \"Green\", \"Blue\");\r\n"
				+ "end Primary_Color;\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "	annex agree {**\r\n"
				+ "		eq test : Primary_Color = enum(Primary_Color, Red);\r\n"
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
		assertTrue(spec instanceof EqStatement);
		EObject lhs = UtilityFunctions.getLhs(spec, 0);
		assertTrue(UtilityFunctions.getStringProperty(lhs, "name").equals("test"));
		EObject lhs_type = UtilityFunctions.getType(lhs);
		assertNotNull(lhs_type);
		assertTrue(lhs_type instanceof DoubleDotRef);
		EObject lhs_type_elm = UtilityFunctions.getElm(lhs_type);
		assertNotNull(lhs_type_elm);
		assertTrue(lhs_type_elm instanceof DataType);
		assertTrue(UtilityFunctions.getStringProperty(lhs_type_elm, "name").equals("Primary_Color"));
		EObject expr = UtilityFunctions.getExpr(spec);
		assertNotNull(expr);
		assertTrue(expr instanceof EnumLitExpr);
		assertTrue(UtilityFunctions.getStringProperty(expr, "value").equals("Red"));
		EObject enum_type = UtilityFunctions.getEnumType(expr);
		assertNotNull(enum_type);
		assertTrue(enum_type instanceof DoubleDotRef);
		EObject enum_type_elm = UtilityFunctions.getElm(enum_type);
		assertNotNull(enum_type_elm);
		assertTrue(enum_type_elm instanceof DataType);
		assertTrue(UtilityFunctions.getStringProperty(enum_type_elm, "name").equals("Primary_Color"));
	}

	@Test
	public void testPreExpr() throws Exception {
		String test = "package TestPackage\r\n"
					+ "public\r\n" + "\r\n"
					+ "system sys\r\n"
					+ "	annex agree{**\r\n"
					+ "		eq test : int = 0 -> pre(test);\r\n"
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
		assertTrue(spec instanceof EqStatement);
		EObject lhs = UtilityFunctions.getLhs(spec, 0);
		assertTrue(UtilityFunctions.getStringProperty(lhs, "name").equals("test"));
		EObject lhs_type = UtilityFunctions.getType(lhs);
		assertNotNull(lhs_type);
		assertTrue(lhs_type instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(lhs_type, "name").equals("int"));
		EObject expr = UtilityFunctions.getExpr(spec);
		assertNotNull(expr);
		assertTrue(expr instanceof BinaryExpr);
		assertTrue(UtilityFunctions.getStringProperty(expr, "op").equals("->"));
		EObject left = UtilityFunctions.getLeft(expr);
		assertNotNull(left);
		assertTrue(left instanceof IntLitExpr);
		assertTrue(Integer.parseInt(UtilityFunctions.getStringProperty(left, "val")) == 0);
		EObject right = UtilityFunctions.getRight(expr);
		assertNotNull(right);
		assertTrue(right instanceof PreExpr);
		EObject right_expr = UtilityFunctions.getExpr(right);
		assertNotNull(right_expr);
		assertTrue(right_expr instanceof NamedElmExpr);
		EObject right_expr_elm = UtilityFunctions.getElm(right_expr);
		assertNotNull(right_expr_elm);
		assertTrue(UtilityFunctions.getStringProperty(right_expr_elm, "name").equals("test"));
		EObject right_expr_elm_type = UtilityFunctions.getType(right_expr_elm);
		assertNotNull(right_expr_elm_type);
		assertTrue(right_expr_elm_type instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(right_expr_elm_type, "name").equals("int"));
	}

	@Test
	public void testEventExpr() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "	with Base_Types;\r\n"
				+ "	\r\n"
				+ "system sys\r\n"
				+ "	features\r\n"
				+ "		test_event : in event data port Base_Types::Boolean;\r\n"
				+ "	annex agree {**\r\n"
				+ "		eq test : bool = event(test_event);\r\n"
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
		assertTrue(spec instanceof EqStatement);
		EObject lhs = UtilityFunctions.getLhs(spec, 0);
		assertTrue(UtilityFunctions.getStringProperty(lhs, "name").equals("test"));
		EObject lhs_type = UtilityFunctions.getType(lhs);
		assertNotNull(lhs_type);
		assertTrue(lhs_type instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(lhs_type, "name").equals("bool"));
		EObject expr = UtilityFunctions.getExpr(spec);
		assertNotNull(expr);
		assertTrue(expr instanceof EventExpr);
		EObject port = UtilityFunctions.getPort(expr);
		assertNotNull(port);
		assertTrue(port instanceof NamedElmExpr);
		EObject port_elm = UtilityFunctions.getElm(port);
		assertNotNull(port_elm);
		assertTrue(port_elm instanceof EventDataPort);
		assertTrue(UtilityFunctions.getStringProperty(port_elm, "name").equals("test_event"));
		assertTrue(Boolean.parseBoolean(UtilityFunctions.getStringProperty(port_elm, "in")));
		assertFalse(Boolean.parseBoolean(UtilityFunctions.getStringProperty(port_elm, "out")));
	}

	@Test
	public void testLatchedExpr() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "	with Base_Types;\r\n"
				+ "	\r\n"
				+ "system sys\r\n"
				+ "	features\r\n"
				+ "		data_in : in data port Base_Types::Boolean;\r\n"
				+ "end sys;\r\n"
				+ "\r\n"
				+ "system implementation sys.impl\r\n"
				+ "	annex agree {**\r\n"
				+ "		synchrony : latched;\r\n"
				+ "		eq test : bool = latched(data_in);\r\n"
				+ "	**};\r\n"
				+ "end sys.impl;\r\n"
				+ "\r\n"
				+ "end TestPackage;";
		FluentIssueCollection issueCollection = testHelper.testString(test);
		EObject aadl_package_impl = issueCollection.getResource().getContents().get(0);
		EObject pub_sec = UtilityFunctions.getownedPublicSection(aadl_package_impl);
		EObject owned_classifiers = UtilityFunctions.getownedClassifier(pub_sec, "sys.impl");
		EObject annex_subclause = UtilityFunctions.getAnnexSubclause(owned_classifiers, "agree");
		EObject parsed_annex_subclause = UtilityFunctions.getParsedAnnexSubclause(annex_subclause);
		EObject contract = UtilityFunctions.getContract(parsed_annex_subclause);
		assertNotNull(contract);
		assertTrue(contract instanceof AgreeContract);
		EObject spec = UtilityFunctions.getSpec(contract, 1);
		assertNotNull(spec);
		assertTrue(spec instanceof EqStatement);
		EObject lhs = UtilityFunctions.getLhs(spec, 0);
		assertTrue(UtilityFunctions.getStringProperty(lhs, "name").equals("test"));
		EObject lhs_type = UtilityFunctions.getType(lhs);
		assertNotNull(lhs_type);
		assertTrue(lhs_type instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(lhs_type, "name").equals("bool"));
		EObject expr = UtilityFunctions.getExpr(spec);
		assertNotNull(expr);
		assertTrue(expr instanceof LatchedExpr);
		EObject expr_expr = UtilityFunctions.getExpr(expr);
		assertNotNull(expr_expr);
		assertTrue(expr_expr instanceof NamedElmExpr);
		EObject expr_expr_elm = UtilityFunctions.getElm(expr_expr);
		assertNotNull(expr_expr_elm);
		assertTrue(expr_expr_elm instanceof DataPort);
		assertTrue(UtilityFunctions.getStringProperty(expr_expr_elm, "name").equals("data_in"));
		assertTrue(Boolean.parseBoolean(UtilityFunctions.getStringProperty(expr_expr_elm, "in")));
		assertFalse(Boolean.parseBoolean(UtilityFunctions.getStringProperty(expr_expr_elm, "out")));
	}

	@Test
	public void testTimeOfExpr() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "	with Base_Types;\r\n"
				+ "\r\n"
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
		assertTrue(spec instanceof EqStatement);
		EObject lhs = UtilityFunctions.getLhs(spec, 0);
		assertTrue(UtilityFunctions.getStringProperty(lhs, "name").equals("test"));
		EObject lhs_type = UtilityFunctions.getType(lhs);
		assertNotNull(lhs_type);
		assertTrue(lhs_type instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(lhs_type, "name").equals("real"));
		EObject expr = UtilityFunctions.getExpr(spec);
		assertNotNull(expr);
		assertTrue(expr instanceof TimeOfExpr);
		EObject id = UtilityFunctions.getID(expr);
		assertNotNull(id);
		assertTrue(id instanceof DataPort);
		assertTrue(UtilityFunctions.getStringProperty(id, "name").equals("data_in"));
		assertTrue(Boolean.parseBoolean(UtilityFunctions.getStringProperty(id, "in")));
		assertFalse(Boolean.parseBoolean(UtilityFunctions.getStringProperty(id, "out")));
	}

	@Test
	public void testTimeRiseExpr() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "	with Base_Types;\r\n"
				+ "\r\n"
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
		assertTrue(spec instanceof EqStatement);
		EObject lhs = UtilityFunctions.getLhs(spec, 0);
		assertTrue(UtilityFunctions.getStringProperty(lhs, "name").equals("test"));
		EObject lhs_type = UtilityFunctions.getType(lhs);
		assertNotNull(lhs_type);
		assertTrue(lhs_type instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(lhs_type, "name").equals("real"));
		EObject expr = UtilityFunctions.getExpr(spec);
		assertNotNull(expr);
		assertTrue(expr instanceof TimeRiseExpr);
		EObject id = UtilityFunctions.getID(expr);
		assertNotNull(id);
		assertTrue(id instanceof DataPort);
		assertTrue(UtilityFunctions.getStringProperty(id, "name").equals("data_in"));
		assertTrue(Boolean.parseBoolean(UtilityFunctions.getStringProperty(id, "in")));
		assertFalse(Boolean.parseBoolean(UtilityFunctions.getStringProperty(id, "out")));
	}

	@Test
	public void testTimeFallExpr() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "	with Base_Types;\r\n"
				+ "\r\n"
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
		assertTrue(spec instanceof EqStatement);
		EObject lhs = UtilityFunctions.getLhs(spec, 0);
		assertTrue(UtilityFunctions.getStringProperty(lhs, "name").equals("test"));
		EObject lhs_type = UtilityFunctions.getType(lhs);
		assertNotNull(lhs_type);
		assertTrue(lhs_type instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(lhs_type, "name").equals("real"));
		EObject expr = UtilityFunctions.getExpr(spec);
		assertNotNull(expr);
		assertTrue(expr instanceof TimeFallExpr);
		EObject id = UtilityFunctions.getID(expr);
		assertNotNull(id);
		assertTrue(id instanceof DataPort);
		assertTrue(UtilityFunctions.getStringProperty(id, "name").equals("data_in"));
		assertTrue(Boolean.parseBoolean(UtilityFunctions.getStringProperty(id, "in")));
		assertFalse(Boolean.parseBoolean(UtilityFunctions.getStringProperty(id, "out")));
	}

	@Test
	public void testFloorCast() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "	annex agree {**\r\n"
				+ "		const test : int = floor(5.2);\r\n"
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
		assertTrue(spec instanceof ConstStatement);
		assertTrue(UtilityFunctions.getStringProperty(spec, "name").equals("test"));
		EObject spec_type = UtilityFunctions.getType(spec);
		assertNotNull(spec_type);
		assertTrue(spec_type instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(spec_type, "name").equals("int"));
		EObject expr = UtilityFunctions.getExpr(spec);
		assertNotNull(expr);
		assertTrue(expr instanceof FloorCast);
		EObject expr_expr = UtilityFunctions.getExpr(expr);
		assertNotNull(expr_expr);
		assertTrue(expr_expr instanceof RealLitExpr);
		assertTrue(Double.parseDouble(UtilityFunctions.getStringProperty(expr_expr, "val")) == 5.2);
	}

	@Test
	public void testRealCast() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "	annex agree {**\r\n"
				+ "		const test : real = real(7);\r\n"
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
		assertTrue(spec instanceof ConstStatement);
		assertTrue(UtilityFunctions.getStringProperty(spec, "name").equals("test"));
		EObject spec_type = UtilityFunctions.getType(spec);
		assertNotNull(spec_type);
		assertTrue(spec_type instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(spec_type, "name").equals("real"));
		EObject expr = UtilityFunctions.getExpr(spec);
		assertNotNull(expr);
		assertTrue(expr instanceof RealCast);
		EObject expr_expr = UtilityFunctions.getExpr(expr);
		assertNotNull(expr_expr);
		assertTrue(expr_expr instanceof IntLitExpr);
		assertTrue(Integer.parseInt(UtilityFunctions.getStringProperty(expr_expr, "val")) == 7);
	}

	@Test
	public void testArrayLiteralExpr() throws Exception {
		String test = "package TestPackage\r\n"
				+ "public\r\n"
				+ "\r\n"
				+ "system sys\r\n"
				+ "	annex agree {**\r\n"
				+ "		eq test_arr : real[3] = [| 1.1, 2.2, 3.3|];\r\n"
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
		assertTrue(spec instanceof EqStatement);
		EObject lhs = UtilityFunctions.getLhs(spec, 0);
		assertTrue(UtilityFunctions.getStringProperty(lhs, "name").equals("test_arr"));
		EObject lhs_type = UtilityFunctions.getType(lhs);
		assertNotNull(lhs_type);
		assertTrue(lhs_type instanceof ArrayType);
		assertTrue(Integer.parseInt(UtilityFunctions.getStringProperty(lhs_type, "size")) == 3);
		EObject lhs_type_stem = UtilityFunctions.getStem(lhs_type);
		assertNotNull(lhs_type_stem);
		assertTrue(lhs_type_stem instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(lhs_type_stem, "name").equals("real"));
		EObject expr = UtilityFunctions.getExpr(spec);
		assertNotNull(expr);
		assertTrue(expr instanceof ArrayLiteralExpr);
		EObject elem0 = UtilityFunctions.getElem(expr, 0);
		assertNotNull(elem0);
		assertTrue(elem0 instanceof RealLitExpr);
		assertTrue(Double.parseDouble(UtilityFunctions.getStringProperty(elem0, "val")) == 1.1);
		EObject elem1 = UtilityFunctions.getElem(expr, 1);
		assertNotNull(elem1);
		assertTrue(elem1 instanceof RealLitExpr);
		assertTrue(Double.parseDouble(UtilityFunctions.getStringProperty(elem1, "val")) == 2.2);
		EObject elem2 = UtilityFunctions.getElem(expr, 2);
		assertNotNull(elem2);
		assertTrue(elem2 instanceof RealLitExpr);
		assertTrue(Double.parseDouble(UtilityFunctions.getStringProperty(elem2, "val")) == 3.3);
	}

	@Test
	public void testCar() throws Exception {
		// Car example taken from https://github.com/loonwerks/AGREE/tree/master/com.rockwellcollins.atc.agree.examples/examples/car
		String Agree_Nodes = "package Agree_Nodes\r\n"
				+ "public\r\n"
				+ "\r\n"
				+ "annex agree {** \r\n"
				+ "	\r\n"
				+ "		node abs(inp: real) returns (outp: real);\r\n"
				+ "		let\r\n"
				+ "			outp = if(inp < 0.0) then -inp else inp;\r\n"
				+ "		tel;\r\n"
				+ "	\r\n"
				+ "    node Y(inp: bool) returns (outp: bool); \r\n"
				+ "    let\r\n"
				+ "      outp = false -> pre(inp);\r\n"
				+ "    tel;  \r\n"
				+ " \r\n"
				+ "    node Z(inp: bool) returns (outp: bool); \r\n"
				+ "    let \r\n"
				+ "      outp = true -> pre(inp); \r\n"
				+ "    tel; \r\n"
				+ "     \r\n"
				+ "    node H(inp: bool) returns (outp: bool);  \r\n"
				+ "    let\r\n"
				+ "      outp = inp -> (inp and pre(outp));\r\n"
				+ "    tel;\r\n"
				+ "    \r\n"
				+ "    node O(inp: bool) returns (outp: bool); \r\n"
				+ "    let\r\n"
				+ "      outp = inp -> (inp or (pre(outp))); \r\n"
				+ "    tel ;  \r\n"
				+ "\r\n"
				+ "    node S_Univ(p: bool, q: bool) returns (outp: bool); \r\n"
				+ "    let\r\n"
				+ "      outp = (Y(O(q))) => p; \r\n"
				+ "    tel; \r\n"
				+ "\r\n"
				+ "	node T_Univ(p: bool, q: bool) returns (outp: bool);\r\n"
				+ "    let\r\n"
				+ "      outp = (S_Univ(q,p)) or H(p) ; \r\n"
				+ "    tel;\r\n"
				+ "	\r\n"
				+ "**};\r\n"
				+ "\r\n"
				+ "end Agree_Nodes;";
		String Car = "package Car\r\n"
				+ "public\r\n"
				+ "	with Agree_Nodes, Types, Transmission, Steering;\r\n"
				+ "\r\n"
				+ "	system Car\r\n"
				+ "		features\r\n"
				+ "			Target_Speed: in data port Types::speed.speed_impl;\r\n"
				+ "			Actual_Speed: out data port Types::speed.speed_impl;\r\n"
				+ "			Target_Tire_Pitch: in data port Types::pitch.pitch_impl;\r\n"
				+ "			Actual_Tire_Pitch: out data port Types::pitch.pitch_impl;\r\n"
				+ "			State_Signal: out data port Types::state_sig.impl;\r\n"
				+ "\r\n"
				+ "		annex agree {**\r\n"
				+ "			const max_accel : real = 40.0;\r\n"
				+ "			assume A1 \"target speed is positive\" : Target_Speed.val >= 0.0;\r\n"
				+ "			assume A2 \"reasonable target speed\" : Target_Speed.val < 150.0;\r\n"
				+ "			--			property const_tar_speed = \r\n"
				+ "			--				Agree_Nodes.H(Target_Speed.val = prev(Target_Speed.val,0.0)); \r\n"
				+ "			property const_tar_speed = \r\n"
				+ "							true -> Target_Speed.val = pre(Target_Speed.val);\r\n"
				+ "			guarantee G_car_1 \"actual speed is less than constant target speed\" : \r\n"
				+ "							const_tar_speed => (Actual_Speed.val <= Target_Speed.val);\r\n"
				+ "			guarantee G_car_2 \"acceleration is limited\" : \r\n"
				+ "							Agree_Nodes::abs(Actual_Speed.val - prev(Actual_Speed.val, 0.0)) < max_accel;\r\n"
				+ "		**};\r\n"
				+ "\r\n"
				+ "	end Car;\r\n"
				+ "\r\n"
				+ "	system implementation Car.Car_Impl\r\n"
				+ "		subcomponents\r\n"
				+ "			THROT: system Transmission::Throttle.Throttle_Impl;\r\n"
				+ "			CNTRL: system Transmission::Speed_Control.Speed_Control_Impl;\r\n"
				+ "			AXL: system Steering::Axle.Axle_Impl;\r\n"
				+ "			SM: system Transmission::State_Machine.Impl;\r\n"
				+ "\r\n"
				+ "		connections\r\n"
				+ "			SpeedToThrot: port CNTRL.Actuator_Input -> THROT.Actuator_Input {\r\n"
				+ "				Communication_Properties::Timing => immediate;};\r\n"
				+ "			AcSpeedToTop: port THROT.Actual -> Actual_Speed {Communication_Properties::Timing => immediate;};\r\n"
				+ "			AcSpeedToCntrl: port THROT.Actual -> CNTRL.Actual {Communication_Properties::Timing => immediate;};\r\n"
				+ "			TgSpeedToCntrl: port Target_Speed -> CNTRL.Target {Communication_Properties::Timing => immediate;};\r\n"
				+ "			TgPtichToAxl: port Target_Tire_Pitch -> AXL.Target_Tire_Direction {\r\n"
				+ "				Communication_Properties::Timing => immediate;};\r\n"
				+ "			AcPtichToCar: port AXL.Actual_Tire_Direction -> Actual_Tire_Pitch {\r\n"
				+ "				Communication_Properties::Timing => immediate;};\r\n"
				+ "			SSToSM: port SM.State_Out -> State_Signal {Communication_Properties::Timing => immediate;};\r\n"
				+ "\r\n"
				+ "	end Car.Car_Impl;\r\n"
				+ "\r\n"
				+ "end Car;";
		String Steering = "package Steering\r\n"
				+ "public\r\n"
				+ "	with Agree_Nodes;\r\n"
				+ "	with Types;\r\n"
				+ "\r\n"
				+ "	system Axle\r\n"
				+ "		features\r\n"
				+ "			Target_Tire_Direction: in data port Types::pitch.pitch_impl;\r\n"
				+ "			Actual_Tire_Direction: out data port Types::pitch.pitch_impl;\r\n"
				+ "			Speed: in data port Types::speed.speed_impl;\r\n"
				+ "\r\n"
				+ "		annex agree {**\r\n"
				+ "			--		assume G0 \"reasonable tire pitch\" :\r\n"
				+ "			--			Agree_Nodes.abs(Target_Tire_Direction.val) <= 0.45;\r\n"
				+ "			guarantee G_axle_1 \"roll limiter\" :\r\n"
				+ "				if (Agree_Nodes::abs(Target_Tire_Direction.val) > 0.20 and Speed.val > 45.0)\r\n"
				+ "					then\r\n"
				+ "						Actual_Tire_Direction.val = 0.20\r\n"
				+ "					else\r\n"
				+ "						Actual_Tire_Direction.val = Target_Tire_Direction.val;\r\n"
				+ "		**};\r\n"
				+ "\r\n"
				+ "	end Axle;\r\n"
				+ "\r\n"
				+ "	system implementation Axle.Axle_Impl\r\n"
				+ "\r\n"
				+ "		annex agree {**\r\n"
				+ "			assign Actual_Tire_Direction = Types::pitch.pitch_impl {\r\n"
				+ "				 val = if (Agree_Nodes::abs(Target_Tire_Direction.val) > 0.20 and Speed.val > 45.0)\r\n"
				+ "				 	then 0.20 else Target_Tire_Direction.val\r\n"
				+ "				 };\r\n"
				+ "		**};\r\n"
				+ "\r\n"
				+ "	end Axle.Axle_Impl;\r\n"
				+ "\r\n"
				+ "end Steering;";
		String Transmission = "package Transmission\r\n"
				+ "public\r\n"
				+ "	with Types;\r\n"
				+ "	with Base_Types;\r\n"
				+ "\r\n"
				+ "	system Speed_Control\r\n"
				+ "		features\r\n"
				+ "			Target: in data port Types::speed.speed_impl;\r\n"
				+ "			--Actuator_Input: out data port Types::actuator.actuator_impl;\r\n"
				+ "			Actuator_Input: out data port Base_Types::Float;\r\n"
				+ "			Actual: in data port Types::speed.speed_impl;\r\n"
				+ "\r\n"
				+ "		annex agree {**\r\n"
				+ "			const P : real = 1.0;\r\n"
				+ "			const D : real = 0.0;\r\n"
				+ "			const I : real = 0.0;\r\n"
				+ "			eq e : real = Target.val - Actual.val;\r\n"
				+ "			eq e_int : real = prev(e, 0.0) + e;\r\n"
				+ "			eq e_dot : real = prev(e, 0.0) - e;\r\n"
				+ "			eq u : real = P*e + D*e_dot + I*e_int;\r\n"
				+ "			guarantee G_speed_1 \"Acuator_Behavior\" : Actuator_Input = u;\r\n"
				+ "		**};\r\n"
				+ "\r\n"
				+ "	end Speed_Control;\r\n"
				+ "\r\n"
				+ "	system implementation Speed_Control.Speed_Control_Impl\r\n"
				+ "\r\n"
				+ "		annex agree {**\r\n"
				+ "			assign Actuator_Input = u;\r\n"
				+ "		**};\r\n"
				+ "\r\n"
				+ "	end Speed_Control.Speed_Control_Impl;\r\n"
				+ "\r\n"
				+ "	system Throttle\r\n"
				+ "		features\r\n"
				+ "			--Actuator_Input: in data port Types::actuator.actuator_impl;\r\n"
				+ "			Actuator_Input: in data port Base_Types::Float;\r\n"
				+ "			Actual: out data port Types::speed.speed_impl;\r\n"
				+ "\r\n"
				+ "		annex agree {**\r\n"
				+ "			guarantee G_throttle_1 \"Throttle_Behavior\" : Actual.val = prev(Actual.val, 0.0) + 0.1*Actuator_Input;\r\n"
				+ "		**};\r\n"
				+ "\r\n"
				+ "	end Throttle;\r\n"
				+ "\r\n"
				+ "	system implementation Throttle.Throttle_Impl\r\n"
				+ "\r\n"
				+ "		annex agree {**\r\n"
				+ "			--assert Actual.val = prev(Actual.val, 0.0) + 0.1*Actuator_Input;\r\n"
				+ "			assign Actual = Types::speed.speed_impl { val = prev(Actual.val, 0.0) + 0.1*Actuator_Input };\r\n"
				+ "		**};\r\n"
				+ "\r\n"
				+ "	end Throttle.Throttle_Impl;\r\n"
				+ "\r\n"
				+ "	system State_Machine\r\n"
				+ "		features\r\n"
				+ "			State_Out: out data port Types::state_sig.impl;\r\n"
				+ "\r\n"
				+ "		annex agree {**\r\n"
				+ "			-- lift SSM; \r\n"
				+ "			guarantee G_tstate_1 \"behavior\" : State_Out.val = prev(State_Out.val, 0);\r\n"
				+ "		**};\r\n"
				+ "\r\n"
				+ "	end State_Machine;\r\n"
				+ "\r\n"
				+ "	system implementation State_Machine.Impl\r\n"
				+ "		subcomponents\r\n"
				+ "			SSM: system Sub_State_Machine.Impl;\r\n"
				+ "\r\n"
				+ "		connections\r\n"
				+ "			SMToSSM: port SSM.State_Out -> State_Out {Communication_Properties::Timing => immediate;};\r\n"
				+ "\r\n"
				+ "		annex agree {**\r\n"
				+ "\r\n"
				+ "		**};\r\n"
				+ "\r\n"
				+ "	end State_Machine.impl;\r\n"
				+ "\r\n"
				+ "	system Sub_State_Machine\r\n"
				+ "		features\r\n"
				+ "			State_Out: out data port Types::state_sig.impl;\r\n"
				+ "\r\n"
				+ "		annex agree {**\r\n"
				+ "			--guarantee G0 \"test\" : true;\r\n"
				+ "			guarantee G_tsubstate_1 \"sub behavior\" : State_Out.val = prev(State_Out.val, 0);\r\n"
				+ "		**};\r\n"
				+ "\r\n"
				+ "	end Sub_State_Machine;\r\n"
				+ "\r\n"
				+ "	system implementation Sub_State_Machine.Impl\r\n"
				+ "\r\n"
				+ "		annex agree {**\r\n"
				+ "			-- assert State_Out.val = prev(State_Out.val,0);\r\n"
				+ "			assign State_Out = Types::state_sig.impl { val = prev(State_Out.val,0) };\r\n"
				+ "		**};\r\n"
				+ "\r\n"
				+ "	end Sub_State_Machine.impl;\r\n"
				+ "\r\n"
				+ "end Transmission;";
		String Types = "package Types\r\n"
				+ "public\r\n"
				+ "	with Base_Types;\r\n"
				+ "\r\n"
				+ "	data speed\r\n"
				+ "	end speed;\r\n"
				+ "	\r\n"
				+ "	data implementation speed.speed_impl\r\n"
				+ "    	subcomponents\r\n"
				+ "      		val: data Base_Types::Float;\r\n"
				+ "  	end speed.speed_impl;\r\n"
				+ "	\r\n"
				+ "	data actuator\r\n"
				+ "	end actuator;\r\n"
				+ "	\r\n"
				+ "	data implementation actuator.actuator_impl\r\n"
				+ "    	subcomponents\r\n"
				+ "      		val: data Base_Types::Float;\r\n"
				+ "  	end actuator.actuator_impl;\r\n"
				+ "  	\r\n"
				+ "  	data pitch\r\n"
				+ "  	end pitch;\r\n"
				+ "  	\r\n"
				+ "  	data implementation pitch.pitch_impl\r\n"
				+ "  		subcomponents\r\n"
				+ "  			val: data Base_Types::Float;\r\n"
				+ "  	end pitch.pitch_impl;\r\n"
				+ "  	  	\r\n"
				+ "  	data state_sig\r\n"
				+ "  	end state_sig;\r\n"
				+ "  	\r\n"
				+ "  	data implementation state_sig.impl\r\n"
				+ "  		subcomponents\r\n"
				+ "  			val: data Base_Types::Integer;\r\n"
				+ "  	end state_sig.impl;\r\n"
				+ "  	\r\n"
				+ "end Types;";
		FluentIssueCollection issueCollection = testHelper.testString(Transmission, Types);
		EObject aadl_package_impl = issueCollection.getResource().getContents().get(0);
		EObject pub_sec = UtilityFunctions.getownedPublicSection(aadl_package_impl);
		testTransmissionSpeedControl(pub_sec);
		testTransmissionSpeedControlImpl(pub_sec);
		testTransmissionThrottle(pub_sec);
		testTransmissionThrottleImpl(pub_sec);
		testTransmissionStateMachine(pub_sec);
		testTransmissionSubStateMachine(pub_sec);
		testTransmissionSubStateMachineImpl(pub_sec);

		issueCollection = testHelper.testString(Agree_Nodes);
		aadl_package_impl = issueCollection.getResource().getContents().get(0);
		pub_sec = UtilityFunctions.getownedPublicSection(aadl_package_impl);
		EObject annex_lib = UtilityFunctions.getAnnexLibrary(pub_sec, "agree");
		EObject parsed_annex_lib = UtilityFunctions.getParsedAnnexLibrary(annex_lib);
		EObject contract = UtilityFunctions.getContract(parsed_annex_lib);
		testAgreeNodes0(contract);
		testAgreeNodes1(contract);
		testAgreeNodes2(contract);
		testAgreeNodes3(contract);
		testAgreeNodes4(contract);
		testAgreeNodes5(contract);
		testAgreeNodes6(contract);

		issueCollection = testHelper.testString(Steering, Agree_Nodes, Types);
		aadl_package_impl = issueCollection.getResource().getContents().get(0);
		pub_sec = UtilityFunctions.getownedPublicSection(aadl_package_impl);
		testSteeringAxle(pub_sec);
		testSteeringAxleImpl(pub_sec);

		issueCollection = testHelper.testString(Car, Steering, Agree_Nodes, Transmission, Types);
		aadl_package_impl = issueCollection.getResource().getContents().get(0);
		pub_sec = UtilityFunctions.getownedPublicSection(aadl_package_impl);
		testCar(pub_sec);

	}

	public void testTransmissionSpeedControl(EObject pub_sec) {
		EObject owned_classifiers = UtilityFunctions.getownedClassifier(pub_sec, "Speed_Control");
		EObject annex_subclause = UtilityFunctions.getAnnexSubclause(owned_classifiers, "agree");
		EObject parsed_annex_subclause = UtilityFunctions.getParsedAnnexSubclause(annex_subclause);
		EObject contract = UtilityFunctions.getContract(parsed_annex_subclause);
		assertNotNull(contract);
		assertTrue(contract instanceof AgreeContract);
		EObject spec0 = UtilityFunctions.getSpec(contract, 0);
		assertNotNull(spec0);
		assertTrue(spec0 instanceof ConstStatement);
		assertTrue(UtilityFunctions.getStringProperty(spec0, "name").equals("P"));
		EObject spec0_type = UtilityFunctions.getType(spec0);
		assertNotNull(spec0_type);
		assertTrue(spec0_type instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(spec0_type, "name").equals("real"));
		EObject spec0_expr = UtilityFunctions.getExpr(spec0);
		assertNotNull(spec0_expr);
		assertTrue(spec0_expr instanceof RealLitExpr);
		assertTrue(Double.parseDouble(UtilityFunctions.getStringProperty(spec0_expr, "val")) == 1.0);

		EObject spec1 = UtilityFunctions.getSpec(contract, 1);
		assertNotNull(spec1);
		assertTrue(spec1 instanceof ConstStatement);
		assertTrue(UtilityFunctions.getStringProperty(spec1, "name").equals("D"));
		EObject spec1_type = UtilityFunctions.getType(spec1);
		assertNotNull(spec1_type);
		assertTrue(spec1_type instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(spec1_type, "name").equals("real"));
		EObject spec1_expr = UtilityFunctions.getExpr(spec1);
		assertNotNull(spec1_expr);
		assertTrue(spec1_expr instanceof RealLitExpr);
		assertTrue(Double.parseDouble(UtilityFunctions.getStringProperty(spec1_expr, "val")) == 0.0);

		EObject spec2 = UtilityFunctions.getSpec(contract, 2);
		assertNotNull(spec2);
		assertTrue(spec2 instanceof ConstStatement);
		assertTrue(UtilityFunctions.getStringProperty(spec2, "name").equals("I"));
		EObject spec2_type = UtilityFunctions.getType(spec2);
		assertNotNull(spec2_type);
		assertTrue(spec2_type instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(spec2_type, "name").equals("real"));
		EObject spec2_expr = UtilityFunctions.getExpr(spec2);
		assertNotNull(spec2_expr);
		assertTrue(spec2_expr instanceof RealLitExpr);
		assertTrue(Double.parseDouble(UtilityFunctions.getStringProperty(spec2_expr, "val")) == 0.0);

		EObject spec3 = UtilityFunctions.getSpec(contract, 3);
		assertNotNull(spec3);
		assertTrue(spec3 instanceof EqStatement);
		EObject spec3_lhs = UtilityFunctions.getLhs(spec3, 0);
		assertTrue(UtilityFunctions.getStringProperty(spec3_lhs, "name").equals("e"));
		EObject spec3_lhs_type = UtilityFunctions.getType(spec3_lhs);
		assertNotNull(spec3_lhs_type);
		assertTrue(spec3_lhs_type instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(spec3_lhs_type, "name").equals("real"));
		EObject spec3_expr = UtilityFunctions.getExpr(spec3);
		assertNotNull(spec3_expr);
		assertTrue(spec3_expr instanceof BinaryExpr);
		assertTrue(UtilityFunctions.getStringProperty(spec3_expr, "op").equals("-"));
		EObject spec3_expr_left = UtilityFunctions.getLeft(spec3_expr);
		assertNotNull(spec3_expr_left);
		assertTrue(spec3_expr_left instanceof SelectionExpr);
		EObject spec3_expr_left_field = UtilityFunctions.getField(spec3_expr_left);
		assertNotNull(spec3_expr_left_field);
		assertTrue(spec3_expr_left_field instanceof DataSubcomponent);
		assertTrue(UtilityFunctions.getStringProperty(spec3_expr_left_field, "name").equals("val"));
		EObject spec3_expr_left_target = UtilityFunctions.getTarget(spec3_expr_left);
		assertNotNull(spec3_expr_left_target);
		assertTrue(spec3_expr_left_target instanceof NamedElmExpr);
		EObject spec3_expr_left_target_elm = UtilityFunctions.getElm(spec3_expr_left_target);
		assertNotNull(spec3_expr_left_target_elm);
		assertTrue(spec3_expr_left_target_elm instanceof DataPort);
		assertTrue(UtilityFunctions.getStringProperty(spec3_expr_left_target_elm, "name").equals("Target"));
		assertTrue(Boolean.parseBoolean(UtilityFunctions.getStringProperty(spec3_expr_left_target_elm, "in")));
		assertFalse(Boolean.parseBoolean(UtilityFunctions.getStringProperty(spec3_expr_left_target_elm, "out")));
		EObject spec3_expr_right = UtilityFunctions.getRight(spec3_expr);
		assertNotNull(spec3_expr_right);
		assertTrue(spec3_expr_right instanceof SelectionExpr);
		EObject spec3_expr_right_field = UtilityFunctions.getField(spec3_expr_right);
		assertNotNull(spec3_expr_right_field);
		assertTrue(spec3_expr_right_field instanceof DataSubcomponent);
		assertTrue(UtilityFunctions.getStringProperty(spec3_expr_right_field, "name").equals("val"));
		EObject spec3_expr_right_target = UtilityFunctions.getTarget(spec3_expr_right);
		assertNotNull(spec3_expr_right_target);
		assertTrue(spec3_expr_right_target instanceof NamedElmExpr);
		EObject spec3_expr_right_target_elm = UtilityFunctions.getElm(spec3_expr_right_target);
		assertNotNull(spec3_expr_right_target_elm);
		assertTrue(spec3_expr_right_target_elm instanceof DataPort);
		assertTrue(UtilityFunctions.getStringProperty(spec3_expr_right_target_elm, "name").equals("Actual"));
		assertTrue(Boolean.parseBoolean(UtilityFunctions.getStringProperty(spec3_expr_right_target_elm, "in")));
		assertFalse(Boolean.parseBoolean(UtilityFunctions.getStringProperty(spec3_expr_right_target_elm, "out")));

		EObject spec4 = UtilityFunctions.getSpec(contract, 4);
		assertNotNull(spec4);
		assertTrue(spec4 instanceof EqStatement);
		EObject spec4_lhs = UtilityFunctions.getLhs(spec4, 0);
		assertTrue(UtilityFunctions.getStringProperty(spec4_lhs, "name").equals("e_int"));
		EObject spec4_lhs_type = UtilityFunctions.getType(spec4_lhs);
		assertNotNull(spec4_lhs_type);
		assertTrue(spec4_lhs_type instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(spec4_lhs_type, "name").equals("real"));
		EObject spec4_expr = UtilityFunctions.getExpr(spec4);
		assertNotNull(spec4_expr);
		assertTrue(spec4_expr instanceof BinaryExpr);
		assertTrue(UtilityFunctions.getStringProperty(spec4_expr, "op").equals("+"));
		EObject spec4_expr_left = UtilityFunctions.getLeft(spec4_expr);
		assertTrue(spec4_expr_left instanceof PrevExpr);
		EObject spec4_expr_left_delay = UtilityFunctions.getDelay(spec4_expr_left);
		assertNotNull(spec4_expr_left_delay);
		assertTrue(spec4_expr_left_delay instanceof NamedElmExpr);
		EObject spec4_expr_left_elm = UtilityFunctions.getElm(spec4_expr_left_delay);
		assertNotNull(spec4_expr_left_elm);
		assertTrue(UtilityFunctions.getStringProperty(spec4_expr_left_elm, "name").equals("e"));
		EObject spec4_expr_left_elm_type = UtilityFunctions.getType(spec4_expr_left_elm);
		assertNotNull(spec4_expr_left_elm_type);
		assertTrue(spec4_expr_left_elm_type instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(spec4_expr_left_elm_type, "name").equals("real"));
		EObject spec4_expr_left_init = UtilityFunctions.getInit(spec4_expr_left);
		assertNotNull(spec4_expr_left_init);
		assertTrue(spec4_expr_left_init instanceof RealLitExpr);
		assertTrue(Double.parseDouble(UtilityFunctions.getStringProperty(spec4_expr_left_init, "val")) == 0.0);
		EObject spec4_expr_right = UtilityFunctions.getRight(spec4_expr);
		assertNotNull(spec4_expr_right);
		assertTrue(spec4_expr_right instanceof NamedElmExpr);
		EObject spec4_expr_right_elm = UtilityFunctions.getElm(spec4_expr_right);
		assertNotNull(spec4_expr_right_elm);
		assertTrue(UtilityFunctions.getStringProperty(spec4_expr_right_elm, "name").equals("e"));
		EObject spec4_expr_right_elm_type = UtilityFunctions.getType(spec4_expr_right_elm);
		assertNotNull(spec4_expr_right_elm_type);
		assertTrue(spec4_expr_right_elm_type instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(spec4_expr_right_elm_type, "name").equals("real"));

		EObject spec5 = UtilityFunctions.getSpec(contract, 5);
		assertNotNull(spec5);
		assertTrue(spec5 instanceof EqStatement);
		EObject spec5_lhs = UtilityFunctions.getLhs(spec5, 0);
		assertTrue(UtilityFunctions.getStringProperty(spec5_lhs, "name").equals("e_dot"));
		EObject spec5_lhs_type = UtilityFunctions.getType(spec5_lhs);
		assertNotNull(spec5_lhs_type);
		assertTrue(spec5_lhs_type instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(spec5_lhs_type, "name").equals("real"));
		EObject spec5_expr = UtilityFunctions.getExpr(spec5);
		assertNotNull(spec5_expr);
		assertTrue(spec5_expr instanceof BinaryExpr);
		assertTrue(UtilityFunctions.getStringProperty(spec5_expr, "op").equals("-"));
		EObject spec5_expr_left = UtilityFunctions.getLeft(spec5_expr);
		assertTrue(spec5_expr_left instanceof PrevExpr);
		EObject spec5_expr_left_delay = UtilityFunctions.getDelay(spec5_expr_left);
		assertNotNull(spec5_expr_left_delay);
		assertTrue(spec5_expr_left_delay instanceof NamedElmExpr);
		EObject spec5_expr_left_elm = UtilityFunctions.getElm(spec5_expr_left_delay);
		assertNotNull(spec5_expr_left_elm);
		assertTrue(UtilityFunctions.getStringProperty(spec5_expr_left_elm, "name").equals("e"));
		EObject spec5_expr_left_elm_type = UtilityFunctions.getType(spec5_expr_left_elm);
		assertNotNull(spec5_expr_left_elm_type);
		assertTrue(spec5_expr_left_elm_type instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(spec5_expr_left_elm_type, "name").equals("real"));
		EObject spec5_expr_left_init = UtilityFunctions.getInit(spec5_expr_left);
		assertNotNull(spec5_expr_left_init);
		assertTrue(spec5_expr_left_init instanceof RealLitExpr);
		assertTrue(Double.parseDouble(UtilityFunctions.getStringProperty(spec5_expr_left_init, "val")) == 0.0);
		EObject spec5_expr_right = UtilityFunctions.getRight(spec5_expr);
		assertNotNull(spec5_expr_right);
		assertTrue(spec5_expr_right instanceof NamedElmExpr);
		EObject spec5_expr_right_elm = UtilityFunctions.getElm(spec5_expr_right);
		assertNotNull(spec5_expr_right_elm);
		assertTrue(UtilityFunctions.getStringProperty(spec5_expr_right_elm, "name").equals("e"));
		EObject spec5_expr_right_elm_type = UtilityFunctions.getType(spec5_expr_right_elm);
		assertNotNull(spec5_expr_right_elm_type);
		assertTrue(spec5_expr_right_elm_type instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(spec5_expr_right_elm_type, "name").equals("real"));

		EObject spec6 = UtilityFunctions.getSpec(contract, 6);
		assertNotNull(spec6);
		assertTrue(spec6 instanceof EqStatement);
		EObject spec6_lhs = UtilityFunctions.getLhs(spec6, 0);
		assertTrue(UtilityFunctions.getStringProperty(spec6_lhs, "name").equals("u"));
		EObject spec6_lhs_type = UtilityFunctions.getType(spec6_lhs);
		assertNotNull(spec6_lhs_type);
		assertTrue(spec6_lhs_type instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(spec6_lhs_type, "name").equals("real"));
		EObject spec6_expr = UtilityFunctions.getExpr(spec6);
		assertNotNull(spec6_expr);
		assertTrue(spec6_expr instanceof BinaryExpr);
		assertTrue(UtilityFunctions.getStringProperty(spec6_expr, "op").equals("+"));
		EObject spec6_expr_left = UtilityFunctions.getLeft(spec6_expr);
		assertNotNull(spec6_expr_left);
		assertTrue(spec6_expr_left instanceof BinaryExpr);
		assertTrue(UtilityFunctions.getStringProperty(spec6_expr_left, "op").equals("+"));
		EObject spec6_expr_left_left = UtilityFunctions.getLeft(spec6_expr_left);
		assertNotNull(spec6_expr_left_left);
		assertTrue(spec6_expr_left_left instanceof BinaryExpr);
		assertTrue(UtilityFunctions.getStringProperty(spec6_expr_left_left, "op").equals("*"));
		EObject spec6_expr_left_left_left = UtilityFunctions.getLeft(spec6_expr_left_left);
		assertNotNull(spec6_expr_left_left_left);
		assertTrue(spec6_expr_left_left_left instanceof NamedElmExpr);
		EObject spec6_expr_left_left_left_elm = UtilityFunctions.getElm(spec6_expr_left_left_left);
		assertNotNull(spec6_expr_left_left_left_elm);
		assertTrue(spec6_expr_left_left_left_elm instanceof ConstStatement);
		assertTrue(UtilityFunctions.getStringProperty(spec6_expr_left_left_left_elm, "name").equals("P"));
		EObject spec6_expr_left_left_left_elm_type = UtilityFunctions.getType(spec6_expr_left_left_left_elm);
		assertNotNull(spec6_expr_left_left_left_elm_type);
		assertTrue(spec6_expr_left_left_left_elm_type instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(spec6_expr_left_left_left_elm_type, "name").equals("real"));
		EObject spec6_expr_left_left_right = UtilityFunctions.getRight(spec6_expr_left_left);
		assertNotNull(spec6_expr_left_left_right);
		assertTrue(spec6_expr_left_left_right instanceof NamedElmExpr);
		EObject spec6_expr_left_left_right_elm = UtilityFunctions.getElm(spec6_expr_left_left_right);
		assertNotNull(spec6_expr_left_left_right_elm);
		assertTrue(UtilityFunctions.getStringProperty(spec6_expr_left_left_right_elm, "name").equals("e"));
		EObject spec6_expr_left_left_right_elm_type = UtilityFunctions.getType(spec6_expr_left_left_right_elm);
		assertNotNull(spec6_expr_left_left_right_elm_type);
		assertTrue(spec6_expr_left_left_right_elm_type instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(spec6_expr_left_left_right_elm_type, "name").equals("real"));
		EObject spec6_expr_left_right = UtilityFunctions.getRight(spec6_expr_left);
		assertNotNull(spec6_expr_left_right);
		assertTrue(spec6_expr_left_right instanceof BinaryExpr);
		assertTrue(UtilityFunctions.getStringProperty(spec6_expr_left_right, "op").equals("*"));
		EObject spec6_expr_left_right_left = UtilityFunctions.getLeft(spec6_expr_left_right);
		assertNotNull(spec6_expr_left_right_left);
		assertTrue(spec6_expr_left_right_left instanceof NamedElmExpr);
		EObject spec6_expr_left_right_left_elm = UtilityFunctions.getElm(spec6_expr_left_right_left);
		assertNotNull(spec6_expr_left_right_left_elm);
		assertTrue(spec6_expr_left_right_left_elm instanceof ConstStatement);
		assertTrue(UtilityFunctions.getStringProperty(spec6_expr_left_right_left_elm, "name").equals("D"));
		EObject spec6_expr_left_right_left_elm_type = UtilityFunctions.getType(spec6_expr_left_right_left_elm);
		assertNotNull(spec6_expr_left_right_left_elm_type);
		assertTrue(spec6_expr_left_right_left_elm_type instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(spec6_expr_left_right_left_elm_type, "name").equals("real"));
		EObject spec6_expr_left_right_right = UtilityFunctions.getRight(spec6_expr_left_right);
		assertNotNull(spec6_expr_left_right_right);
		assertTrue(spec6_expr_left_right_right instanceof NamedElmExpr);
		EObject spec6_expr_left_right_right_elm = UtilityFunctions.getElm(spec6_expr_left_right_right);
		assertNotNull(spec6_expr_left_right_right_elm);
		assertTrue(UtilityFunctions.getStringProperty(spec6_expr_left_right_right_elm, "name").equals("e_dot"));
		EObject spec6_expr_left_right_right_elm_type = UtilityFunctions.getType(spec6_expr_left_right_right_elm);
		assertNotNull(spec6_expr_left_right_right_elm_type);
		assertTrue(spec6_expr_left_right_right_elm_type instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(spec6_expr_left_right_right_elm_type, "name").equals("real"));
		EObject spec6_expr_right = UtilityFunctions.getRight(spec6_expr);
		assertNotNull(spec6_expr_right);
		assertTrue(spec6_expr_right instanceof BinaryExpr);
		assertTrue(UtilityFunctions.getStringProperty(spec6_expr_right, "op").equals("*"));
		EObject spec6_expr_right_left = UtilityFunctions.getLeft(spec6_expr_right);
		assertNotNull(spec6_expr_right_left);
		assertTrue(spec6_expr_right_left instanceof NamedElmExpr);
		EObject spec6_expr_right_left_elm = UtilityFunctions.getElm(spec6_expr_right_left);
		assertNotNull(spec6_expr_right_left_elm);
		assertTrue(spec6_expr_right_left_elm instanceof ConstStatement);
		assertTrue(UtilityFunctions.getStringProperty(spec6_expr_right_left_elm, "name").equals("I"));
		EObject spec6_expr_right_left_elm_type = UtilityFunctions.getType(spec6_expr_right_left_elm);
		assertNotNull(spec6_expr_right_left_elm_type);
		assertTrue(spec6_expr_right_left_elm_type instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(spec6_expr_right_left_elm_type, "name").equals("real"));
		EObject spec6_expr_right_right = UtilityFunctions.getRight(spec6_expr_right);
		assertNotNull(spec6_expr_right_right);
		assertTrue(spec6_expr_right_right instanceof NamedElmExpr);
		EObject spec6_expr_right_right_elm = UtilityFunctions.getElm(spec6_expr_right_right);
		assertNotNull(spec6_expr_right_right_elm);
		assertTrue(UtilityFunctions.getStringProperty(spec6_expr_right_right_elm, "name").equals("e_int"));
		EObject spec6_expr_right_right_elm_type = UtilityFunctions.getType(spec6_expr_right_right_elm);
		assertNotNull(spec6_expr_right_right_elm_type);
		assertTrue(spec6_expr_right_right_elm_type instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(spec6_expr_right_right_elm_type, "name").equals("real"));
	}

	public void testTransmissionSpeedControlImpl(EObject pub_sec) {
		EObject owned_classifiers = UtilityFunctions.getownedClassifier(pub_sec, "Speed_Control.Speed_Control_Impl");
		EObject annex_subclause = UtilityFunctions.getAnnexSubclause(owned_classifiers, "agree");
		EObject parsed_annex_subclause = UtilityFunctions.getParsedAnnexSubclause(annex_subclause);
		EObject contract = UtilityFunctions.getContract(parsed_annex_subclause);
		assertNotNull(contract);
		assertTrue(contract instanceof AgreeContract);
		EObject spec = UtilityFunctions.getSpec(contract, 0);
		assertNotNull(spec);
		assertTrue(spec instanceof AssignStatement);
		EObject id = UtilityFunctions.getID(spec);
		assertNotNull(id);
		assertTrue(id instanceof DataPort);
		assertTrue(UtilityFunctions.getStringProperty(id, "name").equals("Actuator_Input"));
		assertFalse(Boolean.parseBoolean(UtilityFunctions.getStringProperty(id, "in")));
		assertTrue(Boolean.parseBoolean(UtilityFunctions.getStringProperty(id, "out")));
		EObject expr = UtilityFunctions.getExpr(spec);
		assertNotNull(expr);
		assertTrue(expr instanceof NamedElmExpr);
		EObject expr_elm = UtilityFunctions.getElm(expr);
		assertNotNull(expr_elm);
		assertTrue(UtilityFunctions.getStringProperty(expr_elm, "name").equals("u"));
		EObject expr_elm_type = UtilityFunctions.getType(expr_elm);
		assertNotNull(expr_elm_type);
		assertTrue(expr_elm_type instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(expr_elm_type, "name").equals("real"));
	}

	public void testTransmissionThrottle(EObject pub_sec) {
		EObject owned_classifiers = UtilityFunctions.getownedClassifier(pub_sec, "Throttle");
		EObject annex_subclause = UtilityFunctions.getAnnexSubclause(owned_classifiers, "agree");
		EObject parsed_annex_subclause = UtilityFunctions.getParsedAnnexSubclause(annex_subclause);
		EObject contract = UtilityFunctions.getContract(parsed_annex_subclause);
		assertNotNull(contract);
		assertTrue(contract instanceof AgreeContract);
		EObject spec = UtilityFunctions.getSpec(contract, 0);
		assertNotNull(spec);
		assertTrue(spec instanceof GuaranteeStatement);
		assertTrue(UtilityFunctions.getStringProperty(spec, "name").equals("G_throttle_1"));
		assertTrue(UtilityFunctions.getStringProperty(spec, "str").equals("Throttle_Behavior"));
		EObject expr = UtilityFunctions.getExpr(spec);
		assertNotNull(expr);
		assertTrue(expr instanceof BinaryExpr);
		assertTrue(UtilityFunctions.getStringProperty(expr, "op").equals("="));
		EObject left = UtilityFunctions.getLeft(expr);
		assertNotNull(left);
		assertTrue(left instanceof SelectionExpr);
		EObject left_field = UtilityFunctions.getField(left);
		assertNotNull(left_field);
		assertTrue(left_field instanceof DataSubcomponent);
		assertTrue(UtilityFunctions.getStringProperty(left_field, "name").equals("val"));
		EObject left_target = UtilityFunctions.getTarget(left);
		assertNotNull(left_target);
		assertTrue(left_target instanceof NamedElmExpr);
		EObject left_target_elm = UtilityFunctions.getElm(left_target);
		assertNotNull(left_target_elm);
		assertTrue(left_target_elm instanceof DataPort);
		assertTrue(UtilityFunctions.getStringProperty(left_target_elm, "name").equals("Actual"));
		assertFalse(Boolean.parseBoolean(UtilityFunctions.getStringProperty(left_target_elm, "in")));
		assertTrue(Boolean.parseBoolean(UtilityFunctions.getStringProperty(left_target_elm, "out")));
		EObject right = UtilityFunctions.getRight(expr);
		assertNotNull(right);
		assertTrue(right instanceof BinaryExpr);
		assertTrue(UtilityFunctions.getStringProperty(right, "op").equals("+"));
		EObject right_left = UtilityFunctions.getLeft(right);
		assertNotNull(right_left);
		assertTrue(right_left instanceof PrevExpr);
		EObject right_left_delay = UtilityFunctions.getDelay(right_left);
		assertNotNull(right_left_delay);
		assertTrue(right_left_delay instanceof SelectionExpr);
		EObject right_left_delay_field = UtilityFunctions.getField(right_left_delay);
		assertNotNull(right_left_delay_field);
		assertTrue(right_left_delay_field instanceof DataSubcomponent);
		assertTrue(UtilityFunctions.getStringProperty(right_left_delay_field, "name").equals("val"));
		EObject right_left_delay_target = UtilityFunctions.getTarget(right_left_delay);
		assertNotNull(right_left_delay_target);
		assertTrue(right_left_delay_target instanceof NamedElmExpr);
		EObject right_left_delay_target_elm = UtilityFunctions.getElm(right_left_delay_target);
		assertNotNull(right_left_delay_target_elm);
		assertTrue(right_left_delay_target_elm instanceof DataPort);
		assertTrue(UtilityFunctions.getStringProperty(right_left_delay_target_elm, "name").equals("Actual"));
		assertFalse(Boolean.parseBoolean(UtilityFunctions.getStringProperty(right_left_delay_target_elm, "in")));
		assertTrue(Boolean.parseBoolean(UtilityFunctions.getStringProperty(right_left_delay_target_elm, "out")));
		EObject right_left_init = UtilityFunctions.getInit(right_left);
		assertNotNull(right_left_init);
		assertTrue(right_left_init instanceof RealLitExpr);
		assertTrue(Double.parseDouble(UtilityFunctions.getStringProperty(right_left_init, "val")) == 0.0);
		EObject right_right = UtilityFunctions.getRight(right);
		assertNotNull(right_right);
		assertTrue(right_right instanceof BinaryExpr);
		assertTrue(UtilityFunctions.getStringProperty(right_right, "op").equals("*"));
		EObject right_right_left = UtilityFunctions.getLeft(right_right);
		assertNotNull(right_right_left);
		assertTrue(right_right_left instanceof RealLitExpr);
		assertTrue(Double.parseDouble(UtilityFunctions.getStringProperty(right_right_left, "val")) == 0.1);
		EObject right_right_right = UtilityFunctions.getRight(right_right);
		assertNotNull(right_right_right);
		assertTrue(right_right_right instanceof NamedElmExpr);
		EObject right_right_right_elm = UtilityFunctions.getElm(right_right_right);
		assertNotNull(right_right_right_elm);
		assertTrue(right_right_right_elm instanceof DataPort);
		assertTrue(UtilityFunctions.getStringProperty(right_right_right_elm, "name").equals("Actuator_Input"));
		assertTrue(Boolean.parseBoolean(UtilityFunctions.getStringProperty(right_right_right_elm, "in")));
		assertFalse(Boolean.parseBoolean(UtilityFunctions.getStringProperty(right_right_right_elm, "out")));
	}

	public void testTransmissionThrottleImpl(EObject pub_sec) {
		EObject owned_classifiers = UtilityFunctions.getownedClassifier(pub_sec, "Throttle.Throttle_Impl");
		EObject annex_subclause = UtilityFunctions.getAnnexSubclause(owned_classifiers, "agree");
		EObject parsed_annex_subclause = UtilityFunctions.getParsedAnnexSubclause(annex_subclause);
		EObject contract = UtilityFunctions.getContract(parsed_annex_subclause);
		assertNotNull(contract);
		assertTrue(contract instanceof AgreeContract);
		EObject spec = UtilityFunctions.getSpec(contract, 0);
		assertNotNull(spec);
		assertTrue(spec instanceof AssignStatement);
		EObject id = UtilityFunctions.getID(spec);
		assertNotNull(id);
		assertTrue(id instanceof DataPort);
		assertTrue(UtilityFunctions.getStringProperty(id, "name").equals("Actual"));
		assertFalse(Boolean.parseBoolean(UtilityFunctions.getStringProperty(id, "in")));
		assertTrue(Boolean.parseBoolean(UtilityFunctions.getStringProperty(id, "out")));
		EObject expr = UtilityFunctions.getExpr(spec);
		assertNotNull(expr);
		assertTrue(expr instanceof RecordLitExpr);
		EObject expr_arg = expr.eCrossReferences().get(0);
		assertNotNull(expr_arg);
		assertTrue(expr_arg instanceof DataSubcomponent);
		assertTrue(UtilityFunctions.getStringProperty(expr_arg, "name").equals("val"));
		EObject expr_arg_expr = UtilityFunctions.getArgExpr(expr, 0);
		assertNotNull(expr_arg_expr);
		assertTrue(expr_arg_expr instanceof BinaryExpr);
		assertTrue(UtilityFunctions.getStringProperty(expr_arg_expr, "op").equals("+"));
		EObject expr_arg_expr_left = UtilityFunctions.getLeft(expr_arg_expr);
		assertNotNull(expr_arg_expr_left);
		assertTrue(expr_arg_expr_left instanceof PrevExpr);
		EObject expr_arg_expr_left_delay = UtilityFunctions.getDelay(expr_arg_expr_left);
		assertNotNull(expr_arg_expr_left_delay);
		assertTrue(expr_arg_expr_left_delay instanceof SelectionExpr);
		EObject expr_arg_expr_left_delay_field = UtilityFunctions.getField(expr_arg_expr_left_delay);
		assertNotNull(expr_arg_expr_left_delay_field);
		assertTrue(expr_arg_expr_left_delay_field instanceof DataSubcomponent);
		assertTrue(UtilityFunctions.getStringProperty(expr_arg_expr_left_delay_field, "name").equals("val"));
		EObject expr_arg_expr_left_delay_target = UtilityFunctions.getTarget(expr_arg_expr_left_delay);
		assertNotNull(expr_arg_expr_left_delay_target);
		assertTrue(expr_arg_expr_left_delay_target instanceof NamedElmExpr);
		EObject expr_arg_expr_left_delay_target_elm = UtilityFunctions.getElm(expr_arg_expr_left_delay_target);
		assertNotNull(expr_arg_expr_left_delay_target_elm);
		assertTrue(expr_arg_expr_left_delay_target_elm instanceof DataPort);
		assertTrue(UtilityFunctions.getStringProperty(expr_arg_expr_left_delay_target_elm, "name").equals("Actual"));
		assertFalse(Boolean.parseBoolean(UtilityFunctions.getStringProperty(expr_arg_expr_left_delay_target_elm, "in")));
		assertTrue(Boolean.parseBoolean(UtilityFunctions.getStringProperty(expr_arg_expr_left_delay_target_elm, "out")));
		EObject expr_arg_expr_left_init = UtilityFunctions.getInit(expr_arg_expr_left);
		assertNotNull(expr_arg_expr_left_init);
		assertTrue(expr_arg_expr_left_init instanceof RealLitExpr);
		assertTrue(Double.parseDouble(UtilityFunctions.getStringProperty(expr_arg_expr_left_init, "val")) == 0.0);
		EObject expr_arg_expr_right = UtilityFunctions.getRight(expr_arg_expr);
		assertNotNull(expr_arg_expr_right);
		assertTrue(expr_arg_expr_right instanceof BinaryExpr);
		assertTrue(UtilityFunctions.getStringProperty(expr_arg_expr_right, "op").equals("*"));
		EObject expr_arg_expr_right_left = UtilityFunctions.getLeft(expr_arg_expr_right);
		assertNotNull(expr_arg_expr_right_left);
		assertTrue(expr_arg_expr_right_left instanceof RealLitExpr);
		assertTrue(Double.parseDouble(UtilityFunctions.getStringProperty(expr_arg_expr_right_left, "val")) == 0.1);
		EObject expr_arg_expr_right_right = UtilityFunctions.getRight(expr_arg_expr_right);
		assertNotNull(expr_arg_expr_right_right);
		assertTrue(expr_arg_expr_right_right instanceof NamedElmExpr);
		EObject expr_arg_expr_right_right_elm = UtilityFunctions.getElm(expr_arg_expr_right_right);
		assertNotNull(expr_arg_expr_right_right_elm);
		assertTrue(expr_arg_expr_right_right_elm instanceof DataPort);
		assertTrue(UtilityFunctions.getStringProperty(expr_arg_expr_right_right_elm, "name").equals("Actuator_Input"));
		assertTrue(Boolean.parseBoolean(UtilityFunctions.getStringProperty(expr_arg_expr_right_right_elm, "in")));
		assertFalse(Boolean.parseBoolean(UtilityFunctions.getStringProperty(expr_arg_expr_right_right_elm, "out")));
		EObject expr_record_type = UtilityFunctions.getRecordType(expr);
		assertTrue(expr_record_type instanceof DoubleDotRef);
		EObject expr_record_type_elm = UtilityFunctions.getElm(expr_record_type);
		assertNotNull(expr_record_type_elm);
		assertTrue(expr_record_type_elm instanceof DataImplementation);
		assertTrue(UtilityFunctions.getStringProperty(expr_record_type_elm, "name").equals("speed.speed_impl"));
	}

	public void testTransmissionStateMachine(EObject pub_sec) {
		EObject owned_classifiers = UtilityFunctions.getownedClassifier(pub_sec, "State_Machine");
		EObject annex_subclause = UtilityFunctions.getAnnexSubclause(owned_classifiers, "agree");
		EObject parsed_annex_subclause = UtilityFunctions.getParsedAnnexSubclause(annex_subclause);
		EObject contract = UtilityFunctions.getContract(parsed_annex_subclause);
		assertNotNull(contract);
		assertTrue(contract instanceof AgreeContract);
		EObject spec = UtilityFunctions.getSpec(contract, 0);
		assertNotNull(spec);
		assertTrue(spec instanceof GuaranteeStatement);
		assertTrue(UtilityFunctions.getStringProperty(spec, "name").equals("G_tstate_1"));
		assertTrue(UtilityFunctions.getStringProperty(spec, "str").equals("behavior"));
		EObject expr = UtilityFunctions.getExpr(spec);
		assertNotNull(expr);
		assertTrue(expr instanceof BinaryExpr);
		assertTrue(UtilityFunctions.getStringProperty(expr, "op").equals("="));
		EObject left = UtilityFunctions.getLeft(expr);
		assertNotNull(left);
		assertTrue(left instanceof SelectionExpr);
		EObject left_field = UtilityFunctions.getField(left);
		assertNotNull(left_field);
		assertTrue(left_field instanceof DataSubcomponent);
		assertTrue(UtilityFunctions.getStringProperty(left_field, "name").equals("val"));
		EObject left_target = UtilityFunctions.getTarget(left);
		assertNotNull(left_target);
		assertTrue(left_target instanceof NamedElmExpr);
		EObject left_target_elm = UtilityFunctions.getElm(left_target);
		assertNotNull(left_target_elm);
		assertTrue(left_target_elm instanceof DataPort);
		assertTrue(UtilityFunctions.getStringProperty(left_target_elm, "name").equals("State_Out"));
		assertFalse(Boolean.parseBoolean(UtilityFunctions.getStringProperty(left_target_elm, "in")));
		assertTrue(Boolean.parseBoolean(UtilityFunctions.getStringProperty(left_target_elm, "out")));
		EObject right = UtilityFunctions.getRight(expr);
		assertNotNull(right);
		assertTrue(right instanceof PrevExpr);
		EObject right_delay = UtilityFunctions.getDelay(right);
		assertNotNull(right_delay);
		assertTrue(right_delay instanceof SelectionExpr);
		EObject right_delay_field = UtilityFunctions.getField(right_delay);
		assertNotNull(right_delay_field);
		assertTrue(right_delay_field instanceof DataSubcomponent);
		assertTrue(UtilityFunctions.getStringProperty(right_delay_field, "name").equals("val"));
		EObject right_delay_target = UtilityFunctions.getTarget(right_delay);
		assertNotNull(right_delay_target);
		assertTrue(right_delay_target instanceof NamedElmExpr);
		EObject right_delay_target_elm = UtilityFunctions.getElm(right_delay_target);
		assertNotNull(right_delay_target_elm);
		assertTrue(right_delay_target_elm instanceof DataPort);
		assertTrue(UtilityFunctions.getStringProperty(right_delay_target_elm, "name").equals("State_Out"));
		assertFalse(Boolean.parseBoolean(UtilityFunctions.getStringProperty(right_delay_target_elm, "in")));
		assertTrue(Boolean.parseBoolean(UtilityFunctions.getStringProperty(right_delay_target_elm, "out")));
		EObject right_init = UtilityFunctions.getInit(right);
		assertNotNull(right_init);
		assertTrue(right_init instanceof IntLitExpr);
		assertTrue(Integer.parseInt(UtilityFunctions.getStringProperty(right_init, "val")) == 0);
	}

	public void testTransmissionSubStateMachine(EObject pub_sec) {
		EObject owned_classifiers = UtilityFunctions.getownedClassifier(pub_sec, "Sub_State_Machine");
		EObject annex_subclause = UtilityFunctions.getAnnexSubclause(owned_classifiers, "agree");
		EObject parsed_annex_subclause = UtilityFunctions.getParsedAnnexSubclause(annex_subclause);
		EObject contract = UtilityFunctions.getContract(parsed_annex_subclause);
		assertNotNull(contract);
		assertTrue(contract instanceof AgreeContract);
		EObject spec = UtilityFunctions.getSpec(contract, 0);
		assertNotNull(spec);
		assertTrue(spec instanceof GuaranteeStatement);
		assertTrue(UtilityFunctions.getStringProperty(spec, "name").equals("G_tsubstate_1"));
		assertTrue(UtilityFunctions.getStringProperty(spec, "str").equals("sub behavior"));
		EObject expr = UtilityFunctions.getExpr(spec);
		assertNotNull(expr);
		assertTrue(expr instanceof BinaryExpr);
		assertTrue(UtilityFunctions.getStringProperty(expr, "op").equals("="));
		EObject left = UtilityFunctions.getLeft(expr);
		assertNotNull(left);
		assertTrue(left instanceof SelectionExpr);
		EObject left_field = UtilityFunctions.getField(left);
		assertNotNull(left_field);
		assertTrue(left_field instanceof DataSubcomponent);
		assertTrue(UtilityFunctions.getStringProperty(left_field, "name").equals("val"));
		EObject left_target = UtilityFunctions.getTarget(left);
		assertNotNull(left_target);
		assertTrue(left_target instanceof NamedElmExpr);
		EObject left_target_elm = UtilityFunctions.getElm(left_target);
		assertNotNull(left_target_elm);
		assertTrue(left_target_elm instanceof DataPort);
		assertTrue(UtilityFunctions.getStringProperty(left_target_elm, "name").equals("State_Out"));
		assertFalse(Boolean.parseBoolean(UtilityFunctions.getStringProperty(left_target_elm, "in")));
		assertTrue(Boolean.parseBoolean(UtilityFunctions.getStringProperty(left_target_elm, "out")));
		EObject right = UtilityFunctions.getRight(expr);
		assertNotNull(right);
		assertTrue(right instanceof PrevExpr);
		EObject right_delay = UtilityFunctions.getDelay(right);
		assertNotNull(right_delay);
		assertTrue(right_delay instanceof SelectionExpr);
		EObject right_delay_field = UtilityFunctions.getField(right_delay);
		assertNotNull(right_delay_field);
		assertTrue(right_delay_field instanceof DataSubcomponent);
		assertTrue(UtilityFunctions.getStringProperty(right_delay_field, "name").equals("val"));
		EObject right_delay_target = UtilityFunctions.getTarget(right_delay);
		assertNotNull(right_delay_target);
		assertTrue(right_delay_target instanceof NamedElmExpr);
		EObject right_delay_target_elm = UtilityFunctions.getElm(right_delay_target);
		assertNotNull(right_delay_target_elm);
		assertTrue(right_delay_target_elm instanceof DataPort);
		assertTrue(UtilityFunctions.getStringProperty(right_delay_target_elm, "name").equals("State_Out"));
		assertFalse(Boolean.parseBoolean(UtilityFunctions.getStringProperty(right_delay_target_elm, "in")));
		assertTrue(Boolean.parseBoolean(UtilityFunctions.getStringProperty(right_delay_target_elm, "out")));
		EObject right_init = UtilityFunctions.getInit(right);
		assertNotNull(right_init);
		assertTrue(right_init instanceof IntLitExpr);
		assertTrue(Integer.parseInt(UtilityFunctions.getStringProperty(right_init, "val")) == 0);
	}

	public void testTransmissionSubStateMachineImpl(EObject pub_sec) {
		EObject owned_classifiers = UtilityFunctions.getownedClassifier(pub_sec, "Sub_State_Machine.Impl");
		EObject annex_subclause = UtilityFunctions.getAnnexSubclause(owned_classifiers, "agree");
		EObject parsed_annex_subclause = UtilityFunctions.getParsedAnnexSubclause(annex_subclause);
		EObject contract = UtilityFunctions.getContract(parsed_annex_subclause);
		assertNotNull(contract);
		assertTrue(contract instanceof AgreeContract);
		EObject spec = UtilityFunctions.getSpec(contract, 0);
		assertNotNull(spec);
		assertTrue(spec instanceof AssignStatement);
		EObject id = UtilityFunctions.getID(spec);
		assertNotNull(id);
		assertTrue(id instanceof DataPort);
		assertTrue(UtilityFunctions.getStringProperty(id, "name").equals("State_Out"));
		assertFalse(Boolean.parseBoolean(UtilityFunctions.getStringProperty(id, "in")));
		assertTrue(Boolean.parseBoolean(UtilityFunctions.getStringProperty(id, "out")));
		EObject expr = UtilityFunctions.getExpr(spec);
		assertNotNull(expr);
		assertTrue(expr instanceof RecordLitExpr);
		EObject expr_arg = expr.eCrossReferences().get(0);
		assertNotNull(expr_arg);
		assertTrue(expr_arg instanceof DataSubcomponent);
		assertTrue(UtilityFunctions.getStringProperty(expr_arg, "name").equals("val"));
		EObject expr_arg_expr = UtilityFunctions.getArgExpr(expr, 0);
		assertNotNull(expr_arg_expr);
		assertTrue(expr_arg_expr instanceof PrevExpr);
		EObject expr_arg_expr_delay = UtilityFunctions.getDelay(expr_arg_expr);
		assertNotNull(expr_arg_expr_delay);
		assertTrue(expr_arg_expr_delay instanceof SelectionExpr);
		EObject expr_arg_expr_delay_field = UtilityFunctions.getField(expr_arg_expr_delay);
		assertNotNull(expr_arg_expr_delay_field);
		assertTrue(expr_arg_expr_delay_field instanceof DataSubcomponent);
		assertTrue(UtilityFunctions.getStringProperty(expr_arg_expr_delay_field, "name").equals("val"));
		EObject expr_arg_expr_delay_target = UtilityFunctions.getTarget(expr_arg_expr_delay);
		assertNotNull(expr_arg_expr_delay_target);
		assertTrue(expr_arg_expr_delay_target instanceof NamedElmExpr);
		EObject expr_arg_expr_delay_target_elm = UtilityFunctions.getElm(expr_arg_expr_delay_target);
		assertNotNull(expr_arg_expr_delay_target_elm);
		assertTrue(expr_arg_expr_delay_target_elm instanceof DataPort);
		assertTrue(UtilityFunctions.getStringProperty(expr_arg_expr_delay_target_elm, "name").equals("State_Out"));
		assertFalse(Boolean.parseBoolean(UtilityFunctions.getStringProperty(expr_arg_expr_delay_target_elm, "in")));
		assertTrue(Boolean.parseBoolean(UtilityFunctions.getStringProperty(expr_arg_expr_delay_target_elm, "out")));
		EObject expr_arg_expr_init = UtilityFunctions.getInit(expr_arg_expr);
		assertNotNull(expr_arg_expr_init);
		assertTrue(expr_arg_expr_init instanceof IntLitExpr);
		assertTrue(Integer.parseInt(UtilityFunctions.getStringProperty(expr_arg_expr_init, "val")) == 0);
		EObject expr_record_type = UtilityFunctions.getRecordType(expr);
		assertTrue(expr_record_type instanceof DoubleDotRef);
		EObject expr_record_type_elm = UtilityFunctions.getElm(expr_record_type);
		assertNotNull(expr_record_type_elm);
		assertTrue(expr_record_type_elm instanceof DataImplementation);
		assertTrue(UtilityFunctions.getStringProperty(expr_record_type_elm, "name").equals("state_sig.impl"));
	}

	public void testAgreeNodes0(EObject contract) {
		EObject spec = UtilityFunctions.getSpec(contract, 0);
		assertNotNull(spec);
		assertTrue(spec instanceof NodeDef);
		assertTrue(UtilityFunctions.getStringProperty(spec, "name").equals("abs"));
		EObject arg = UtilityFunctions.getArg(spec, 0);
		assertNotNull(arg);
		assertTrue(UtilityFunctions.getStringProperty(arg, "name").equals("inp"));
		EObject arg_type = UtilityFunctions.getType(arg);
		assertNotNull(arg_type);
		assertTrue(arg_type instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(arg_type, "name").equals("real"));
		EObject ret = UtilityFunctions.getRet(spec, 0);
		assertNotNull(ret);
		assertTrue(UtilityFunctions.getStringProperty(ret, "name").equals("outp"));
		EObject ret_type = UtilityFunctions.getType(ret);
		assertNotNull(ret_type);
		assertTrue(ret_type instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(ret_type, "name").equals("real"));
		EObject body = UtilityFunctions.getNodeBody(spec);
		assertNotNull(body);
		assertTrue(body instanceof NodeBodyExpr);
		EObject stmt = UtilityFunctions.getStmt(body, 0);
		assertNotNull(stmt);
		assertTrue(stmt instanceof NodeEq);
		EObject lhs = stmt.eCrossReferences().get(0);
		assertNotNull(lhs);
		assertTrue(UtilityFunctions.getStringProperty(lhs, "name").equals("outp"));
		EObject lhs_type = UtilityFunctions.getType(lhs);
		assertNotNull(lhs_type);
		assertTrue(lhs_type instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(lhs_type, "name").equals("real"));
		EObject expr = UtilityFunctions.getExpr(stmt);
		assertNotNull(expr);
		assertTrue(expr instanceof IfThenElseExpr);
		EObject expr_a = UtilityFunctions.getA(expr);
		assertNotNull(expr_a);
		assertTrue(expr_a instanceof BinaryExpr);
		assertTrue(UtilityFunctions.getStringProperty(expr_a, "op").equals("<"));
		EObject expr_a_left = UtilityFunctions.getLeft(expr_a);
		assertNotNull(expr_a_left);
		assertTrue(expr_a_left instanceof NamedElmExpr);
		EObject expr_a_left_elm = UtilityFunctions.getElm(expr_a_left);
		assertNotNull(expr_a_left_elm);
		assertTrue(UtilityFunctions.getStringProperty(expr_a_left_elm, "name").equals("inp"));
		EObject expr_a_left_elm_type = UtilityFunctions.getType(expr_a_left_elm);
		assertNotNull(expr_a_left_elm_type);
		assertTrue(expr_a_left_elm_type instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(expr_a_left_elm_type, "name").equals("real"));
		EObject expr_b = UtilityFunctions.getB(expr);
		assertNotNull(expr_b);
		assertTrue(expr_b instanceof UnaryExpr);
		assertTrue(UtilityFunctions.getStringProperty(expr_b, "op").equals("-"));
		EObject expr_b_expr = UtilityFunctions.getExpr(expr_b);
		assertNotNull(expr_b_expr);
		assertTrue(expr_b_expr instanceof NamedElmExpr);
		EObject expr_b_expr_elm = UtilityFunctions.getElm(expr_b_expr);
		assertNotNull(expr_b_expr_elm);
		assertTrue(UtilityFunctions.getStringProperty(expr_b_expr_elm, "name").equals("inp"));
		EObject expr_b_expr_elm_type = UtilityFunctions.getType(expr_b_expr_elm);
		assertNotNull(expr_b_expr_elm_type);
		assertTrue(expr_b_expr_elm_type instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(expr_b_expr_elm_type, "name").equals("real"));
		EObject expr_c = UtilityFunctions.getC(expr);
		assertNotNull(expr_c);
		assertTrue(expr_c instanceof NamedElmExpr);
		EObject expr_c_elm = UtilityFunctions.getElm(expr_c);
		assertNotNull(expr_c_elm);
		assertTrue(UtilityFunctions.getStringProperty(expr_c_elm, "name").equals("inp"));
		EObject expr_c_elm_type = UtilityFunctions.getType(expr_c_elm);
		assertNotNull(expr_c_elm_type);
		assertTrue(expr_c_elm_type instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(expr_c_elm_type, "name").equals("real"));
	}

	public void testAgreeNodes1(EObject contract) {
		EObject spec = UtilityFunctions.getSpec(contract, 1);
		assertNotNull(spec);
		assertTrue(spec instanceof NodeDef);
		assertTrue(UtilityFunctions.getStringProperty(spec, "name").equals("Y"));
		EObject arg = UtilityFunctions.getArg(spec, 0);
		assertNotNull(arg);
		assertTrue(UtilityFunctions.getStringProperty(arg, "name").equals("inp"));
		EObject arg_type = UtilityFunctions.getType(arg);
		assertNotNull(arg_type);
		assertTrue(arg_type instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(arg_type, "name").equals("bool"));
		EObject ret = UtilityFunctions.getRet(spec, 0);
		assertNotNull(ret);
		assertTrue(UtilityFunctions.getStringProperty(ret, "name").equals("outp"));
		EObject ret_type = UtilityFunctions.getType(ret);
		assertNotNull(ret_type);
		assertTrue(ret_type instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(ret_type, "name").equals("bool"));
		EObject body = UtilityFunctions.getNodeBody(spec);
		assertNotNull(body);
		assertTrue(body instanceof NodeBodyExpr);
		EObject stmt = UtilityFunctions.getStmt(body, 0);
		assertNotNull(stmt);
		assertTrue(stmt instanceof NodeEq);
		EObject lhs = stmt.eCrossReferences().get(0);
		assertNotNull(lhs);
		assertTrue(UtilityFunctions.getStringProperty(lhs, "name").equals("outp"));
		EObject lhs_type = UtilityFunctions.getType(lhs);
		assertNotNull(lhs_type);
		assertTrue(lhs_type instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(lhs_type, "name").equals("bool"));
		EObject expr = UtilityFunctions.getExpr(stmt);
		assertNotNull(expr);
		assertTrue(expr instanceof BinaryExpr);
		assertTrue(UtilityFunctions.getStringProperty(expr, "op").equals("->"));
		EObject left = UtilityFunctions.getLeft(expr);
		assertNotNull(left);
		assertTrue(left instanceof BoolLitExpr);
		EObject left_val = UtilityFunctions.getVal(left);
		assertNotNull(left_val);
		assertTrue(left_val instanceof BooleanLiteral);
		assertFalse(Boolean.parseBoolean(UtilityFunctions.getStringProperty(left_val, "value")));
		EObject right = UtilityFunctions.getRight(expr);
		assertNotNull(right);
		assertTrue(right instanceof PreExpr);
		EObject right_expr = UtilityFunctions.getExpr(right);
		assertNotNull(right_expr);
		assertTrue(right_expr instanceof NamedElmExpr);
		EObject right_expr_elm = UtilityFunctions.getElm(right_expr);
		assertNotNull(right_expr_elm);
		assertTrue(UtilityFunctions.getStringProperty(right_expr_elm, "name").equals("inp"));
		EObject right_expr_elm_type = UtilityFunctions.getType(right_expr_elm);
		assertNotNull(right_expr_elm_type);
		assertTrue(right_expr_elm_type instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(right_expr_elm_type, "name").equals("bool"));
	}

	public void testAgreeNodes2(EObject contract) {
		EObject spec = UtilityFunctions.getSpec(contract, 2);
		assertNotNull(spec);
		assertTrue(spec instanceof NodeDef);
		assertTrue(UtilityFunctions.getStringProperty(spec, "name").equals("Z"));
		EObject arg = UtilityFunctions.getArg(spec, 0);
		assertNotNull(arg);
		assertTrue(UtilityFunctions.getStringProperty(arg, "name").equals("inp"));
		EObject arg_type = UtilityFunctions.getType(arg);
		assertNotNull(arg_type);
		assertTrue(arg_type instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(arg_type, "name").equals("bool"));
		EObject ret = UtilityFunctions.getRet(spec, 0);
		assertNotNull(ret);
		assertTrue(UtilityFunctions.getStringProperty(ret, "name").equals("outp"));
		EObject ret_type = UtilityFunctions.getType(ret);
		assertNotNull(ret_type);
		assertTrue(ret_type instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(ret_type, "name").equals("bool"));
		EObject body = UtilityFunctions.getNodeBody(spec);
		assertNotNull(body);
		assertTrue(body instanceof NodeBodyExpr);
		EObject stmt = UtilityFunctions.getStmt(body, 0);
		assertNotNull(stmt);
		assertTrue(stmt instanceof NodeEq);
		EObject lhs = stmt.eCrossReferences().get(0);
		assertNotNull(lhs);
		assertTrue(UtilityFunctions.getStringProperty(lhs, "name").equals("outp"));
		EObject lhs_type = UtilityFunctions.getType(lhs);
		assertNotNull(lhs_type);
		assertTrue(lhs_type instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(lhs_type, "name").equals("bool"));
		EObject expr = UtilityFunctions.getExpr(stmt);
		assertNotNull(expr);
		assertTrue(expr instanceof BinaryExpr);
		assertTrue(UtilityFunctions.getStringProperty(expr, "op").equals("->"));
		EObject left = UtilityFunctions.getLeft(expr);
		assertNotNull(left);
		assertTrue(left instanceof BoolLitExpr);
		EObject left_val = UtilityFunctions.getVal(left);
		assertNotNull(left_val);
		assertTrue(left_val instanceof BooleanLiteral);
		assertTrue(Boolean.parseBoolean(UtilityFunctions.getStringProperty(left_val, "value")));
		EObject right = UtilityFunctions.getRight(expr);
		assertNotNull(right);
		assertTrue(right instanceof PreExpr);
		EObject right_expr = UtilityFunctions.getExpr(right);
		assertNotNull(right_expr);
		assertTrue(right_expr instanceof NamedElmExpr);
		EObject right_expr_elm = UtilityFunctions.getElm(right_expr);
		assertNotNull(right_expr_elm);
		assertTrue(UtilityFunctions.getStringProperty(right_expr_elm, "name").equals("inp"));
		EObject right_expr_elm_type = UtilityFunctions.getType(right_expr_elm);
		assertNotNull(right_expr_elm_type);
		assertTrue(right_expr_elm_type instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(right_expr_elm_type, "name").equals("bool"));
	}

	public void testAgreeNodes3(EObject contract) {
		EObject spec = UtilityFunctions.getSpec(contract, 3);
		assertNotNull(spec);
		assertTrue(spec instanceof NodeDef);
		assertTrue(UtilityFunctions.getStringProperty(spec, "name").equals("H"));
		EObject arg = UtilityFunctions.getArg(spec, 0);
		assertNotNull(arg);
		assertTrue(UtilityFunctions.getStringProperty(arg, "name").equals("inp"));
		EObject arg_type = UtilityFunctions.getType(arg);
		assertNotNull(arg_type);
		assertTrue(arg_type instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(arg_type, "name").equals("bool"));
		EObject ret = UtilityFunctions.getRet(spec, 0);
		assertNotNull(ret);
		assertTrue(UtilityFunctions.getStringProperty(ret, "name").equals("outp"));
		EObject ret_type = UtilityFunctions.getType(ret);
		assertNotNull(ret_type);
		assertTrue(ret_type instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(ret_type, "name").equals("bool"));
		EObject body = UtilityFunctions.getNodeBody(spec);
		assertNotNull(body);
		assertTrue(body instanceof NodeBodyExpr);
		EObject stmt = UtilityFunctions.getStmt(body, 0);
		assertNotNull(stmt);
		assertTrue(stmt instanceof NodeEq);
		EObject lhs = stmt.eCrossReferences().get(0);
		assertNotNull(lhs);
		assertTrue(UtilityFunctions.getStringProperty(lhs, "name").equals("outp"));
		EObject lhs_type = UtilityFunctions.getType(lhs);
		assertNotNull(lhs_type);
		assertTrue(lhs_type instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(lhs_type, "name").equals("bool"));
		EObject expr = UtilityFunctions.getExpr(stmt);
		assertNotNull(expr);
		assertTrue(expr instanceof BinaryExpr);
		assertTrue(UtilityFunctions.getStringProperty(expr, "op").equals("->"));
		EObject left = UtilityFunctions.getLeft(expr);
		assertNotNull(left);
		assertTrue(left instanceof NamedElmExpr);
		EObject left_elm = UtilityFunctions.getElm(left);
		assertNotNull(left_elm);
		assertTrue(UtilityFunctions.getStringProperty(left_elm, "name").equals("inp"));
		EObject left_elm_type = UtilityFunctions.getType(left_elm);
		assertNotNull(left_elm_type);
		assertTrue(left_elm_type instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(left_elm_type, "name").equals("bool"));
		EObject right = UtilityFunctions.getRight(expr);
		assertNotNull(right);
		assertTrue(right instanceof BinaryExpr);
		assertTrue(UtilityFunctions.getStringProperty(right, "op").equals("and"));
		EObject right_left = UtilityFunctions.getLeft(right);
		assertNotNull(right_left);
		assertTrue(right_left instanceof NamedElmExpr);
		EObject right_left_elm = UtilityFunctions.getElm(right_left);
		assertNotNull(right_left_elm);
		assertTrue(UtilityFunctions.getStringProperty(right_left_elm, "name").equals("inp"));
		EObject right_left_elm_type = UtilityFunctions.getType(right_left_elm);
		assertNotNull(right_left_elm_type);
		assertTrue(right_left_elm_type instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(right_left_elm_type, "name").equals("bool"));
		EObject right_right = UtilityFunctions.getRight(right);
		assertNotNull(right_right);
		assertTrue(right_right instanceof PreExpr);
		EObject right_right_expr = UtilityFunctions.getExpr(right_right);
		assertNotNull(right_right_expr);
		assertTrue(right_right_expr instanceof NamedElmExpr);
		EObject right_right_expr_elm = UtilityFunctions.getElm(right_right_expr);
		assertNotNull(right_right_expr_elm);
		assertTrue(UtilityFunctions.getStringProperty(right_right_expr_elm, "name").equals("outp"));
		EObject right_right_expr_elm_type = UtilityFunctions.getType(right_right_expr_elm);
		assertNotNull(right_right_expr_elm_type);
		assertTrue(right_right_expr_elm_type instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(right_right_expr_elm_type, "name").equals("bool"));
	}

	public void testAgreeNodes4(EObject contract) {
		EObject spec = UtilityFunctions.getSpec(contract, 4);
		assertNotNull(spec);
		assertTrue(spec instanceof NodeDef);
		assertTrue(UtilityFunctions.getStringProperty(spec, "name").equals("O"));
		EObject arg = UtilityFunctions.getArg(spec, 0);
		assertNotNull(arg);
		assertTrue(UtilityFunctions.getStringProperty(arg, "name").equals("inp"));
		EObject arg_type = UtilityFunctions.getType(arg);
		assertNotNull(arg_type);
		assertTrue(arg_type instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(arg_type, "name").equals("bool"));
		EObject ret = UtilityFunctions.getRet(spec, 0);
		assertNotNull(ret);
		assertTrue(UtilityFunctions.getStringProperty(ret, "name").equals("outp"));
		EObject ret_type = UtilityFunctions.getType(ret);
		assertNotNull(ret_type);
		assertTrue(ret_type instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(ret_type, "name").equals("bool"));
		EObject body = UtilityFunctions.getNodeBody(spec);
		assertNotNull(body);
		assertTrue(body instanceof NodeBodyExpr);
		EObject stmt = UtilityFunctions.getStmt(body, 0);
		assertNotNull(stmt);
		assertTrue(stmt instanceof NodeEq);
		EObject lhs = stmt.eCrossReferences().get(0);
		assertNotNull(lhs);
		assertTrue(UtilityFunctions.getStringProperty(lhs, "name").equals("outp"));
		EObject lhs_type = UtilityFunctions.getType(lhs);
		assertNotNull(lhs_type);
		assertTrue(lhs_type instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(lhs_type, "name").equals("bool"));
		EObject expr = UtilityFunctions.getExpr(stmt);
		assertNotNull(expr);
		assertTrue(expr instanceof BinaryExpr);
		assertTrue(UtilityFunctions.getStringProperty(expr, "op").equals("->"));
		EObject left = UtilityFunctions.getLeft(expr);
		assertNotNull(left);
		assertTrue(left instanceof NamedElmExpr);
		EObject left_elm = UtilityFunctions.getElm(left);
		assertNotNull(left_elm);
		assertTrue(UtilityFunctions.getStringProperty(left_elm, "name").equals("inp"));
		EObject left_elm_type = UtilityFunctions.getType(left_elm);
		assertNotNull(left_elm_type);
		assertTrue(left_elm_type instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(left_elm_type, "name").equals("bool"));
		EObject right = UtilityFunctions.getRight(expr);
		assertNotNull(right);
		assertTrue(right instanceof BinaryExpr);
		assertTrue(UtilityFunctions.getStringProperty(right, "op").equals("or"));
		EObject right_left = UtilityFunctions.getLeft(right);
		assertNotNull(right_left);
		assertTrue(right_left instanceof NamedElmExpr);
		EObject right_left_elm = UtilityFunctions.getElm(right_left);
		assertNotNull(right_left_elm);
		assertTrue(UtilityFunctions.getStringProperty(right_left_elm, "name").equals("inp"));
		EObject right_left_elm_type = UtilityFunctions.getType(right_left_elm);
		assertNotNull(right_left_elm_type);
		assertTrue(right_left_elm_type instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(right_left_elm_type, "name").equals("bool"));
		EObject right_right = UtilityFunctions.getRight(right);
		assertNotNull(right_right);
		assertTrue(right_right instanceof PreExpr);
		EObject right_right_expr = UtilityFunctions.getExpr(right_right);
		assertNotNull(right_right_expr);
		assertTrue(right_right_expr instanceof NamedElmExpr);
		EObject right_right_expr_elm = UtilityFunctions.getElm(right_right_expr);
		assertNotNull(right_right_expr_elm);
		assertTrue(UtilityFunctions.getStringProperty(right_right_expr_elm, "name").equals("outp"));
		EObject right_right_expr_elm_type = UtilityFunctions.getType(right_right_expr_elm);
		assertNotNull(right_right_expr_elm_type);
		assertTrue(right_right_expr_elm_type instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(right_right_expr_elm_type, "name").equals("bool"));
	}

	public void testAgreeNodes5(EObject contract) {
		EObject spec = UtilityFunctions.getSpec(contract, 5);
		assertNotNull(spec);
		assertTrue(spec instanceof NodeDef);
		assertTrue(UtilityFunctions.getStringProperty(spec, "name").equals("S_Univ"));
		EObject arg0 = UtilityFunctions.getArg(spec, 0);
		assertNotNull(arg0);
		assertTrue(UtilityFunctions.getStringProperty(arg0, "name").equals("p"));
		EObject arg0_type = UtilityFunctions.getType(arg0);
		assertNotNull(arg0_type);
		assertTrue(arg0_type instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(arg0_type, "name").equals("bool"));
		EObject arg1 = UtilityFunctions.getArg(spec, 1);
		assertNotNull(arg1);
		assertTrue(UtilityFunctions.getStringProperty(arg1, "name").equals("q"));
		EObject arg1_type = UtilityFunctions.getType(arg1);
		assertNotNull(arg1_type);
		assertTrue(arg1_type instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(arg1_type, "name").equals("bool"));
		EObject ret = UtilityFunctions.getRet(spec, 0);
		assertNotNull(ret);
		assertTrue(UtilityFunctions.getStringProperty(ret, "name").equals("outp"));
		EObject ret_type = UtilityFunctions.getType(ret);
		assertNotNull(ret_type);
		assertTrue(ret_type instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(ret_type, "name").equals("bool"));
		EObject body = UtilityFunctions.getNodeBody(spec);
		assertNotNull(body);
		assertTrue(body instanceof NodeBodyExpr);
		EObject stmt = UtilityFunctions.getStmt(body, 0);
		assertNotNull(stmt);
		assertTrue(stmt instanceof NodeEq);
		EObject lhs = stmt.eCrossReferences().get(0);
		assertNotNull(lhs);
		assertTrue(UtilityFunctions.getStringProperty(lhs, "name").equals("outp"));
		EObject lhs_type = UtilityFunctions.getType(lhs);
		assertNotNull(lhs_type);
		assertTrue(lhs_type instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(lhs_type, "name").equals("bool"));
		EObject expr = UtilityFunctions.getExpr(stmt);
		assertNotNull(expr);
		assertTrue(expr instanceof BinaryExpr);
		assertTrue(UtilityFunctions.getStringProperty(expr, "op").equals("=>"));
		EObject left = UtilityFunctions.getLeft(expr);
		assertNotNull(left);
		assertTrue(left instanceof CallExpr);
		EObject left_arg = UtilityFunctions.getArg(left, 0);
		assertNotNull(left_arg);
		assertTrue(left_arg instanceof CallExpr);
		EObject left_arg_ref = UtilityFunctions.getRef(left_arg);
		assertNotNull(left_arg_ref);
		EObject left_arg_ref_elm = UtilityFunctions.getElm(left_arg_ref);
		assertNotNull(left_arg_ref_elm);
		assertTrue(left_arg_ref_elm instanceof NodeDef);
		assertTrue(UtilityFunctions.getStringProperty(left_arg_ref_elm, "name").equals("O"));
		EObject left_arg_ref_elm_arg = UtilityFunctions.getArg(left_arg_ref_elm, 0);
		assertNotNull(left_arg_ref_elm_arg);
		assertTrue(UtilityFunctions.getStringProperty(left_arg_ref_elm_arg, "name").equals("inp"));
		EObject left_arg_ref_elm_arg_type = UtilityFunctions.getType(left_arg_ref_elm_arg);
		assertNotNull(left_arg_ref_elm_arg_type);
		assertTrue(left_arg_ref_elm_arg_type instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(left_arg_ref_elm_arg_type, "name").equals("bool"));
		EObject left_arg_ref_elm_body = UtilityFunctions.getNodeBody(left_arg_ref_elm);
		assertNotNull(left_arg_ref_elm_body);
		assertTrue(left_arg_ref_elm_body instanceof NodeBodyExpr);
		EObject left_arg_ref_elm_body_stmt = UtilityFunctions.getStmt(left_arg_ref_elm_body, 0);
		assertNotNull(left_arg_ref_elm_body_stmt instanceof NodeEq);
		EObject left_arg_ref_elm_body_stmt_lhs = left_arg_ref_elm_body_stmt.eCrossReferences().get(0);
		assertNotNull(left_arg_ref_elm_body_stmt_lhs);
		assertTrue(UtilityFunctions.getStringProperty(left_arg_ref_elm_body_stmt_lhs, "name").equals("outp"));
		EObject left_arg_ref_elm_body_stmt_lhs_type = UtilityFunctions.getType(left_arg_ref_elm_body_stmt_lhs);
		assertNotNull(left_arg_ref_elm_body_stmt_lhs_type);
		assertTrue(left_arg_ref_elm_body_stmt_lhs_type instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(left_arg_ref_elm_body_stmt_lhs_type, "name").equals("bool"));
		EObject left_arg_ref_elm_body_stmt_expr = UtilityFunctions.getExpr(left_arg_ref_elm_body_stmt);
		assertNotNull(left_arg_ref_elm_body_stmt_expr);
		assertTrue(left_arg_ref_elm_body_stmt_expr instanceof BinaryExpr);
		assertTrue(UtilityFunctions.getStringProperty(left_arg_ref_elm_body_stmt_expr, "op").equals("->"));
		EObject left_arg_ref_elm_body_stmt_expr_left = UtilityFunctions.getLeft(left_arg_ref_elm_body_stmt_expr);
		assertNotNull(left_arg_ref_elm_body_stmt_expr_left);
		assertTrue(left_arg_ref_elm_body_stmt_expr_left instanceof NamedElmExpr);
		EObject left_arg_ref_elm_body_stmt_expr_left_elm = UtilityFunctions.getElm(left_arg_ref_elm_body_stmt_expr_left);
		assertNotNull(left_arg_ref_elm_body_stmt_expr_left_elm);
		assertTrue(UtilityFunctions.getStringProperty(left_arg_ref_elm_body_stmt_expr_left_elm, "name").equals("inp"));
		EObject left_arg_ref_elm_body_stmt_expr_left_elm_type = UtilityFunctions.getType(left_arg_ref_elm_body_stmt_expr_left_elm);
		assertNotNull(left_arg_ref_elm_body_stmt_expr_left_elm_type);
		assertTrue(left_arg_ref_elm_body_stmt_expr_left_elm_type instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(left_arg_ref_elm_body_stmt_expr_left_elm_type, "name").equals("bool"));
		EObject left_arg_ref_elm_body_stmt_expr_right = UtilityFunctions.getRight(left_arg_ref_elm_body_stmt_expr);
		assertNotNull(left_arg_ref_elm_body_stmt_expr_right);
		assertTrue(left_arg_ref_elm_body_stmt_expr_right instanceof BinaryExpr);
		assertTrue(UtilityFunctions.getStringProperty(left_arg_ref_elm_body_stmt_expr_right, "op").equals("or"));
		EObject left_arg_ref_elm_body_stmt_expr_right_left = UtilityFunctions.getLeft(left_arg_ref_elm_body_stmt_expr_right);
		assertNotNull(left_arg_ref_elm_body_stmt_expr_right_left);
		assertTrue(left_arg_ref_elm_body_stmt_expr_right_left instanceof NamedElmExpr);
		EObject left_arg_ref_elm_body_stmt_expr_right_left_elm = UtilityFunctions.getElm(left_arg_ref_elm_body_stmt_expr_right_left);
		assertNotNull(left_arg_ref_elm_body_stmt_expr_right_left_elm);
		assertTrue(UtilityFunctions.getStringProperty(left_arg_ref_elm_body_stmt_expr_right_left_elm, "name").equals("inp"));
		EObject left_arg_ref_elm_body_stmt_expr_right_left_elm_type = UtilityFunctions.getType(left_arg_ref_elm_body_stmt_expr_right_left_elm);
		assertNotNull(left_arg_ref_elm_body_stmt_expr_right_left_elm_type);
		assertTrue(left_arg_ref_elm_body_stmt_expr_right_left_elm_type instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(left_arg_ref_elm_body_stmt_expr_right_left_elm_type, "name").equals("bool"));
		EObject left_arg_ref_elm_body_stmt_expr_right_right = UtilityFunctions.getRight(left_arg_ref_elm_body_stmt_expr_right);
		assertNotNull(left_arg_ref_elm_body_stmt_expr_right_right);
		assertTrue(left_arg_ref_elm_body_stmt_expr_right_right instanceof PreExpr);
		EObject left_arg_ref_elm_body_stmt_expr_right_right_expr = UtilityFunctions.getExpr(left_arg_ref_elm_body_stmt_expr_right_right);
		assertNotNull(left_arg_ref_elm_body_stmt_expr_right_right_expr);
		assertTrue(left_arg_ref_elm_body_stmt_expr_right_right_expr instanceof NamedElmExpr);
		EObject left_arg_ref_elm_body_stmt_expr_right_right_expr_elm = UtilityFunctions.getElm(left_arg_ref_elm_body_stmt_expr_right_right_expr);
		assertNotNull(left_arg_ref_elm_body_stmt_expr_right_right_expr_elm);
		assertTrue(UtilityFunctions.getStringProperty(left_arg_ref_elm_body_stmt_expr_right_right_expr_elm, "name").equals("outp"));
		EObject left_arg_ref_elm_body_stmt_expr_right_right_expr_elm_type = UtilityFunctions.getType(left_arg_ref_elm_body_stmt_expr_right_right_expr_elm);
		assertNotNull(left_arg_ref_elm_body_stmt_expr_right_right_expr_elm_type);
		assertTrue(left_arg_ref_elm_body_stmt_expr_right_right_expr_elm_type instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(left_arg_ref_elm_body_stmt_expr_right_right_expr_elm_type, "name").equals("bool"));
		EObject left_arg_ref_elm_rets = UtilityFunctions.getRet(left_arg_ref_elm, 0);
		assertNotNull(left_arg_ref_elm_rets);
		assertTrue(UtilityFunctions.getStringProperty(left_arg_ref_elm_rets, "name").equals("outp"));
		EObject left_arg_ref_elm_rets_type = UtilityFunctions.getType(left_arg_ref_elm_rets);
		assertNotNull(left_arg_ref_elm_rets_type instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(left_arg_ref_elm_rets_type, "name").equals("bool"));
		EObject left_ref = UtilityFunctions.getRef(left);
		assertNotNull(left_ref);
		assertTrue(left_ref instanceof DoubleDotRef);
		EObject left_ref_elm = UtilityFunctions.getElm(left_ref);
		assertNotNull(left_ref_elm);
		assertTrue(left_ref_elm instanceof NodeDef);
		assertTrue(UtilityFunctions.getStringProperty(left_ref_elm, "name").equals("Y"));
		EObject left_ref_elm_arg = UtilityFunctions.getArg(left_ref_elm, 0);
		assertNotNull(left_ref_elm_arg);
		assertTrue(UtilityFunctions.getStringProperty(left_ref_elm_arg, "name").equals("inp"));
		EObject left_ref_elm_arg_type = UtilityFunctions.getType(left_ref_elm_arg);
		assertNotNull(left_ref_elm_arg_type);
		assertTrue(left_ref_elm_arg_type instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(left_ref_elm_arg_type, "name").equals("bool"));
		EObject left_ref_elm_body = UtilityFunctions.getNodeBody(left_ref_elm);
		assertNotNull(left_ref_elm_body);
		assertTrue(left_ref_elm_body instanceof NodeBodyExpr);
		EObject left_ref_elm_body_stmt = UtilityFunctions.getStmt(left_ref_elm_body, 0);
		assertNotNull(left_ref_elm_body_stmt instanceof NodeEq);
		EObject left_ref_elm_body_stmt_lhs = left_ref_elm_body_stmt.eCrossReferences().get(0);
		assertNotNull(left_ref_elm_body_stmt_lhs);
		assertTrue(UtilityFunctions.getStringProperty(left_ref_elm_body_stmt_lhs, "name").equals("outp"));
		EObject left_ref_elm_body_stmt_lhs_type = UtilityFunctions.getType(left_ref_elm_body_stmt_lhs);
		assertNotNull(left_ref_elm_body_stmt_lhs_type);
		assertTrue(left_ref_elm_body_stmt_lhs_type instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(left_ref_elm_body_stmt_lhs_type, "name").equals("bool"));
		EObject left_ref_elm_body_stmt_expr = UtilityFunctions.getExpr(left_ref_elm_body_stmt);
		assertNotNull(left_ref_elm_body_stmt_expr);
		assertTrue(left_ref_elm_body_stmt_expr instanceof BinaryExpr);
		assertTrue(UtilityFunctions.getStringProperty(left_ref_elm_body_stmt_expr, "op").equals("->"));
		EObject left_ref_elm_body_stmt_left = UtilityFunctions.getLeft(left_ref_elm_body_stmt_expr);
		assertNotNull(left_ref_elm_body_stmt_left);
		assertTrue(left_ref_elm_body_stmt_left instanceof BoolLitExpr);
		EObject left_ref_elm_body_stmt_left_val = UtilityFunctions.getVal(left_ref_elm_body_stmt_left);
		assertNotNull(left_ref_elm_body_stmt_left_val);
		assertTrue(left_ref_elm_body_stmt_left_val instanceof BooleanLiteral);
		assertFalse(Boolean.parseBoolean(UtilityFunctions.getStringProperty(left_ref_elm_body_stmt_left_val, "value")));
		EObject left_ref_elm_body_stmt_right = UtilityFunctions.getRight(left_ref_elm_body_stmt_expr);
		assertNotNull(left_ref_elm_body_stmt_right);
		assertTrue(left_ref_elm_body_stmt_right instanceof PreExpr);
		EObject left_ref_elm_body_stmt_right_expr = UtilityFunctions.getExpr(left_ref_elm_body_stmt_right);
		assertNotNull(left_ref_elm_body_stmt_right_expr);
		assertTrue(left_ref_elm_body_stmt_right_expr instanceof NamedElmExpr);
		EObject left_ref_elm_body_stmt_right_expr_elm = UtilityFunctions.getElm(left_ref_elm_body_stmt_right_expr);
		assertNotNull(left_ref_elm_body_stmt_right_expr_elm);
		assertTrue(UtilityFunctions.getStringProperty(left_ref_elm_body_stmt_right_expr_elm, "name").equals("inp"));
		EObject left_ref_elm_body_stmt_right_expr_elm_type = UtilityFunctions.getType(left_ref_elm_body_stmt_right_expr_elm);
		assertNotNull(left_ref_elm_body_stmt_right_expr_elm_type);
		assertTrue(left_ref_elm_body_stmt_right_expr_elm_type instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(left_ref_elm_body_stmt_right_expr_elm_type, "name").equals("bool"));
		EObject left_ref_elm_rets = UtilityFunctions.getRet(left_ref_elm, 0);
		assertNotNull(left_ref_elm_rets);
		assertTrue(UtilityFunctions.getStringProperty(left_ref_elm_rets, "name").equals("outp"));
		EObject left_ref_elm_rets_type = UtilityFunctions.getType(left_ref_elm_rets);
		assertNotNull(left_ref_elm_rets_type instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(left_ref_elm_rets_type, "name").equals("bool"));
		EObject right = UtilityFunctions.getRight(expr);
		assertNotNull(right);
		assertTrue(right instanceof NamedElmExpr);
		EObject right_elm = UtilityFunctions.getElm(right);
		assertNotNull(right_elm);
		assertTrue(UtilityFunctions.getStringProperty(right_elm, "name").equals("p"));
		EObject right_elm_type = UtilityFunctions.getType(right_elm);
		assertNotNull(right_elm_type);
		assertTrue(right_elm_type instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(right_elm_type, "name").equals("bool"));
	}

	public void testAgreeNodes6(EObject contract) {
		EObject spec = UtilityFunctions.getSpec(contract, 6);
		assertNotNull(spec);
		assertTrue(spec instanceof NodeDef);
		assertTrue(UtilityFunctions.getStringProperty(spec, "name").equals("T_Univ"));
		EObject arg0 = UtilityFunctions.getArg(spec, 0);
		assertNotNull(arg0);
		assertTrue(UtilityFunctions.getStringProperty(arg0, "name").equals("p"));
		EObject arg0_type = UtilityFunctions.getType(arg0);
		assertNotNull(arg0_type);
		assertTrue(arg0_type instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(arg0_type, "name").equals("bool"));
		EObject arg1 = UtilityFunctions.getArg(spec, 1);
		assertNotNull(arg1);
		assertTrue(UtilityFunctions.getStringProperty(arg1, "name").equals("q"));
		EObject arg1_type = UtilityFunctions.getType(arg1);
		assertNotNull(arg1_type);
		assertTrue(arg1_type instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(arg1_type, "name").equals("bool"));
		EObject ret = UtilityFunctions.getRet(spec, 0);
		assertNotNull(ret);
		assertTrue(UtilityFunctions.getStringProperty(ret, "name").equals("outp"));
		EObject ret_type = UtilityFunctions.getType(ret);
		assertNotNull(ret_type);
		assertTrue(ret_type instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(ret_type, "name").equals("bool"));
		EObject body = UtilityFunctions.getNodeBody(spec);
		assertNotNull(body);
		assertTrue(body instanceof NodeBodyExpr);
		EObject stmt = UtilityFunctions.getStmt(body, 0);
		assertNotNull(stmt);
		assertTrue(stmt instanceof NodeEq);
		EObject lhs = stmt.eCrossReferences().get(0);
		assertNotNull(lhs);
		assertTrue(UtilityFunctions.getStringProperty(lhs, "name").equals("outp"));
		EObject lhs_type = UtilityFunctions.getType(lhs);
		assertNotNull(lhs_type);
		assertTrue(lhs_type instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(lhs_type, "name").equals("bool"));
		EObject expr = UtilityFunctions.getExpr(stmt);
		assertNotNull(expr);
		assertTrue(expr instanceof BinaryExpr);
		assertTrue(UtilityFunctions.getStringProperty(expr, "op").equals("or"));
		EObject left = UtilityFunctions.getLeft(expr);
		assertNotNull(left);
		assertTrue(left instanceof CallExpr);
		EObject left_arg0 = UtilityFunctions.getArg(spec, 0);
		assertNotNull(left_arg0);
		assertTrue(UtilityFunctions.getStringProperty(left_arg0, "name").equals("p"));
		EObject left_arg0_type = UtilityFunctions.getType(left_arg0);
		assertNotNull(left_arg0_type);
		assertTrue(left_arg0_type instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(left_arg0_type, "name").equals("bool"));
		EObject left_arg1 = UtilityFunctions.getArg(spec, 1);
		assertNotNull(left_arg1);
		assertTrue(UtilityFunctions.getStringProperty(left_arg1, "name").equals("q"));
		EObject left_arg1_type = UtilityFunctions.getType(left_arg1);
		assertNotNull(left_arg1_type);
		assertTrue(left_arg1_type instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(left_arg1_type, "name").equals("bool"));
		EObject left_ref = UtilityFunctions.getRef(left);
		assertNotNull(left_ref);
		assertTrue(left_ref instanceof DoubleDotRef);
		EObject left_ref_elm = UtilityFunctions.getElm(left_ref);
		assertNotNull(left_ref_elm);
		assertTrue(left_ref_elm instanceof NodeDef);
		assertTrue(UtilityFunctions.getStringProperty(left_ref_elm, "name").equals("S_Univ"));
		EObject left_ref_elm_arg0 = UtilityFunctions.getArg(left_ref_elm, 0);
		assertNotNull(left_ref_elm_arg0);
		assertTrue(UtilityFunctions.getStringProperty(left_ref_elm_arg0, "name").equals("p"));
		EObject left_ref_elm_arg0_type = UtilityFunctions.getType(left_ref_elm_arg0);
		assertNotNull(left_ref_elm_arg0_type);
		assertTrue(left_ref_elm_arg0_type instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(left_ref_elm_arg0_type, "name").equals("bool"));
		EObject left_ref_elm_arg1 = UtilityFunctions.getArg(left_ref_elm, 1);
		assertNotNull(left_ref_elm_arg1);
		assertTrue(UtilityFunctions.getStringProperty(left_ref_elm_arg1, "name").equals("q"));
		EObject left_ref_elm_arg1_type = UtilityFunctions.getType(left_ref_elm_arg1);
		assertNotNull(left_ref_elm_arg1_type);
		assertTrue(left_ref_elm_arg1_type instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(left_ref_elm_arg1_type, "name").equals("bool"));
		EObject left_ref_elm_ret = UtilityFunctions.getRet(left_ref_elm, 0);
		assertNotNull(left_ref_elm_ret);
		assertTrue(UtilityFunctions.getStringProperty(left_ref_elm_ret, "name").equals("outp"));
		EObject left_ref_elm_ret_type = UtilityFunctions.getType(left_ref_elm_ret);
		assertNotNull(left_ref_elm_ret_type);
		assertTrue(left_ref_elm_ret_type instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(left_ref_elm_ret_type, "name").equals("bool"));
		EObject left_ref_elm_body = UtilityFunctions.getNodeBody(left_ref_elm);
		assertNotNull(left_ref_elm_body);
		assertTrue(left_ref_elm_body instanceof NodeBodyExpr);
		EObject left_ref_elm_stmt = UtilityFunctions.getStmt(left_ref_elm_body, 0);
		assertNotNull(left_ref_elm_stmt);
		assertTrue(left_ref_elm_stmt instanceof NodeEq);
		EObject left_ref_elm_lhs = left_ref_elm_stmt.eCrossReferences().get(0);
		assertNotNull(left_ref_elm_lhs);
		assertTrue(UtilityFunctions.getStringProperty(left_ref_elm_lhs, "name").equals("outp"));
		EObject left_ref_elm_lhs_type = UtilityFunctions.getType(left_ref_elm_lhs);
		assertNotNull(left_ref_elm_lhs_type);
		assertTrue(left_ref_elm_lhs_type instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(left_ref_elm_lhs_type, "name").equals("bool"));
		EObject left_ref_elm_expr = UtilityFunctions.getExpr(left_ref_elm_stmt);
		assertNotNull(left_ref_elm_expr);
		assertTrue(left_ref_elm_expr instanceof BinaryExpr);
		assertTrue(UtilityFunctions.getStringProperty(left_ref_elm_expr, "op").equals("=>"));
		EObject left_ref_elm_left = UtilityFunctions.getLeft(left_ref_elm_expr);
		assertNotNull(left_ref_elm_left);
		assertTrue(left_ref_elm_left instanceof CallExpr);
		EObject left_ref_elm_left_arg = UtilityFunctions.getArg(left_ref_elm_left, 0);
		assertNotNull(left_ref_elm_left_arg);
		assertTrue(left_ref_elm_left_arg instanceof CallExpr);
		EObject left_ref_elm_left_arg_ref = UtilityFunctions.getRef(left_ref_elm_left_arg);
		assertNotNull(left_ref_elm_left_arg_ref);
		EObject left_ref_elm_left_arg_ref_elm = UtilityFunctions.getElm(left_ref_elm_left_arg_ref);
		assertNotNull(left_ref_elm_left_arg_ref_elm);
		assertTrue(left_ref_elm_left_arg_ref_elm instanceof NodeDef);
		assertTrue(UtilityFunctions.getStringProperty(left_ref_elm_left_arg_ref_elm, "name").equals("O"));
		EObject left_ref_elm_left_arg_ref_elm_arg = UtilityFunctions.getArg(left_ref_elm_left_arg_ref_elm, 0);
		assertNotNull(left_ref_elm_left_arg_ref_elm_arg);
		assertTrue(UtilityFunctions.getStringProperty(left_ref_elm_left_arg_ref_elm_arg, "name").equals("inp"));
		EObject left_ref_elm_left_arg_ref_elm_arg_type = UtilityFunctions.getType(left_ref_elm_left_arg_ref_elm_arg);
		assertNotNull(left_ref_elm_left_arg_ref_elm_arg_type);
		assertTrue(left_ref_elm_left_arg_ref_elm_arg_type instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(left_ref_elm_left_arg_ref_elm_arg_type, "name").equals("bool"));
		EObject left_ref_elm_left_arg_ref_elm_body = UtilityFunctions.getNodeBody(left_ref_elm_left_arg_ref_elm);
		assertNotNull(left_ref_elm_left_arg_ref_elm_body);
		assertTrue(left_ref_elm_left_arg_ref_elm_body instanceof NodeBodyExpr);
		EObject left_ref_elm_left_arg_ref_elm_body_stmt = UtilityFunctions.getStmt(left_ref_elm_left_arg_ref_elm_body, 0);
		assertNotNull(left_ref_elm_left_arg_ref_elm_body_stmt instanceof NodeEq);
		EObject left_ref_elm_left_arg_ref_elm_body_stmt_lhs = left_ref_elm_left_arg_ref_elm_body_stmt.eCrossReferences().get(0);
		assertNotNull(left_ref_elm_left_arg_ref_elm_body_stmt_lhs);
		assertTrue(UtilityFunctions.getStringProperty(left_ref_elm_left_arg_ref_elm_body_stmt_lhs, "name").equals("outp"));
		EObject left_ref_elm_left_arg_ref_elm_body_stmt_lhs_type = UtilityFunctions.getType(left_ref_elm_left_arg_ref_elm_body_stmt_lhs);
		assertNotNull(left_ref_elm_left_arg_ref_elm_body_stmt_lhs_type);
		assertTrue(left_ref_elm_left_arg_ref_elm_body_stmt_lhs_type instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(left_ref_elm_left_arg_ref_elm_body_stmt_lhs_type, "name").equals("bool"));
		EObject left_ref_elm_left_arg_ref_elm_body_stmt_expr = UtilityFunctions.getExpr(left_ref_elm_left_arg_ref_elm_body_stmt);
		assertNotNull(left_ref_elm_left_arg_ref_elm_body_stmt_expr);
		assertTrue(left_ref_elm_left_arg_ref_elm_body_stmt_expr instanceof BinaryExpr);
		assertTrue(UtilityFunctions.getStringProperty(left_ref_elm_left_arg_ref_elm_body_stmt_expr, "op").equals("->"));
		EObject left_ref_elm_left_arg_ref_elm_body_stmt_expr_left = UtilityFunctions.getLeft(left_ref_elm_left_arg_ref_elm_body_stmt_expr);
		assertNotNull(left_ref_elm_left_arg_ref_elm_body_stmt_expr_left);
		assertTrue(left_ref_elm_left_arg_ref_elm_body_stmt_expr_left instanceof NamedElmExpr);
		EObject left_ref_elm_left_arg_ref_elm_body_stmt_expr_left_elm = UtilityFunctions.getElm(left_ref_elm_left_arg_ref_elm_body_stmt_expr_left);
		assertNotNull(left_ref_elm_left_arg_ref_elm_body_stmt_expr_left_elm);
		assertTrue(UtilityFunctions.getStringProperty(left_ref_elm_left_arg_ref_elm_body_stmt_expr_left_elm, "name").equals("inp"));
		EObject left_ref_elm_left_arg_ref_elm_body_stmt_expr_left_elm_type = UtilityFunctions.getType(left_ref_elm_left_arg_ref_elm_body_stmt_expr_left_elm);
		assertNotNull(left_ref_elm_left_arg_ref_elm_body_stmt_expr_left_elm_type);
		assertTrue(left_ref_elm_left_arg_ref_elm_body_stmt_expr_left_elm_type instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(left_ref_elm_left_arg_ref_elm_body_stmt_expr_left_elm_type, "name").equals("bool"));
		EObject left_ref_elm_left_arg_ref_elm_body_stmt_expr_right = UtilityFunctions.getRight(left_ref_elm_left_arg_ref_elm_body_stmt_expr);
		assertNotNull(left_ref_elm_left_arg_ref_elm_body_stmt_expr_right);
		assertTrue(left_ref_elm_left_arg_ref_elm_body_stmt_expr_right instanceof BinaryExpr);
		assertTrue(UtilityFunctions.getStringProperty(left_ref_elm_left_arg_ref_elm_body_stmt_expr_right, "op").equals("or"));
		EObject left_ref_elm_left_arg_ref_elm_body_stmt_expr_right_left = UtilityFunctions.getLeft(left_ref_elm_left_arg_ref_elm_body_stmt_expr_right);
		assertNotNull(left_ref_elm_left_arg_ref_elm_body_stmt_expr_right_left);
		assertTrue(left_ref_elm_left_arg_ref_elm_body_stmt_expr_right_left instanceof NamedElmExpr);
		EObject left_ref_elm_left_arg_ref_elm_body_stmt_expr_right_left_elm = UtilityFunctions.getElm(left_ref_elm_left_arg_ref_elm_body_stmt_expr_right_left);
		assertNotNull(left_ref_elm_left_arg_ref_elm_body_stmt_expr_right_left_elm);
		assertTrue(UtilityFunctions.getStringProperty(left_ref_elm_left_arg_ref_elm_body_stmt_expr_right_left_elm, "name").equals("inp"));
		EObject left_ref_elm_left_arg_ref_elm_body_stmt_expr_right_left_elm_type = UtilityFunctions.getType(left_ref_elm_left_arg_ref_elm_body_stmt_expr_right_left_elm);
		assertNotNull(left_ref_elm_left_arg_ref_elm_body_stmt_expr_right_left_elm_type);
		assertTrue(left_ref_elm_left_arg_ref_elm_body_stmt_expr_right_left_elm_type instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(left_ref_elm_left_arg_ref_elm_body_stmt_expr_right_left_elm_type, "name").equals("bool"));
		EObject left_ref_elm_left_arg_ref_elm_body_stmt_expr_right_right = UtilityFunctions.getRight(left_ref_elm_left_arg_ref_elm_body_stmt_expr_right);
		assertNotNull(left_ref_elm_left_arg_ref_elm_body_stmt_expr_right_right);
		assertTrue(left_ref_elm_left_arg_ref_elm_body_stmt_expr_right_right instanceof PreExpr);
		EObject left_ref_elm_left_arg_ref_elm_body_stmt_expr_right_right_expr = UtilityFunctions.getExpr(left_ref_elm_left_arg_ref_elm_body_stmt_expr_right_right);
		assertNotNull(left_ref_elm_left_arg_ref_elm_body_stmt_expr_right_right_expr);
		assertTrue(left_ref_elm_left_arg_ref_elm_body_stmt_expr_right_right_expr instanceof NamedElmExpr);
		EObject left_ref_elm_left_arg_ref_elm_body_stmt_expr_right_right_expr_elm = UtilityFunctions.getElm(left_ref_elm_left_arg_ref_elm_body_stmt_expr_right_right_expr);
		assertNotNull(left_ref_elm_left_arg_ref_elm_body_stmt_expr_right_right_expr_elm);
		assertTrue(UtilityFunctions.getStringProperty(left_ref_elm_left_arg_ref_elm_body_stmt_expr_right_right_expr_elm, "name").equals("outp"));
		EObject left_ref_elm_left_arg_ref_elm_body_stmt_expr_right_right_expr_elm_type = UtilityFunctions.getType(left_ref_elm_left_arg_ref_elm_body_stmt_expr_right_right_expr_elm);
		assertNotNull(left_ref_elm_left_arg_ref_elm_body_stmt_expr_right_right_expr_elm_type);
		assertTrue(left_ref_elm_left_arg_ref_elm_body_stmt_expr_right_right_expr_elm_type instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(left_ref_elm_left_arg_ref_elm_body_stmt_expr_right_right_expr_elm_type, "name").equals("bool"));
		EObject left_ref_elm_left_arg_ref_elm_rets = UtilityFunctions.getRet(left_ref_elm_left_arg_ref_elm, 0);
		assertNotNull(left_ref_elm_left_arg_ref_elm_rets);
		assertTrue(UtilityFunctions.getStringProperty(left_ref_elm_left_arg_ref_elm_rets, "name").equals("outp"));
		EObject left_ref_elm_left_arg_ref_elm_rets_type = UtilityFunctions.getType(left_ref_elm_left_arg_ref_elm_rets);
		assertNotNull(left_ref_elm_left_arg_ref_elm_rets_type instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(left_ref_elm_left_arg_ref_elm_rets_type, "name").equals("bool"));
		EObject left_ref_elm_left_ref = UtilityFunctions.getRef(left_ref_elm_left);
		assertNotNull(left_ref_elm_left_ref);
		assertTrue(left_ref_elm_left_ref instanceof DoubleDotRef);
		EObject left_ref_elm_left_ref_elm = UtilityFunctions.getElm(left_ref_elm_left_ref);
		assertNotNull(left_ref_elm_left_ref_elm);
		assertTrue(left_ref_elm_left_ref_elm instanceof NodeDef);
		assertTrue(UtilityFunctions.getStringProperty(left_ref_elm_left_ref_elm, "name").equals("Y"));
		EObject left_ref_elm_left_ref_elm_arg = UtilityFunctions.getArg(left_ref_elm_left_ref_elm, 0);
		assertNotNull(left_ref_elm_left_ref_elm_arg);
		assertTrue(UtilityFunctions.getStringProperty(left_ref_elm_left_ref_elm_arg, "name").equals("inp"));
		EObject left_ref_elm_left_ref_elm_arg_type = UtilityFunctions.getType(left_ref_elm_left_ref_elm_arg);
		assertNotNull(left_ref_elm_left_ref_elm_arg_type);
		assertTrue(left_ref_elm_left_ref_elm_arg_type instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(left_ref_elm_left_ref_elm_arg_type, "name").equals("bool"));
		EObject left_ref_elm_left_ref_elm_body = UtilityFunctions.getNodeBody(left_ref_elm_left_ref_elm);
		assertNotNull(left_ref_elm_left_ref_elm_body);
		assertTrue(left_ref_elm_left_ref_elm_body instanceof NodeBodyExpr);
		EObject left_ref_elm_left_ref_elm_body_stmt = UtilityFunctions.getStmt(left_ref_elm_left_ref_elm_body, 0);
		assertNotNull(left_ref_elm_left_ref_elm_body_stmt instanceof NodeEq);
		EObject left_ref_elm_left_ref_elm_body_stmt_lhs = left_ref_elm_left_ref_elm_body_stmt.eCrossReferences().get(0);
		assertNotNull(left_ref_elm_left_ref_elm_body_stmt_lhs);
		assertTrue(UtilityFunctions.getStringProperty(left_ref_elm_left_ref_elm_body_stmt_lhs, "name").equals("outp"));
		EObject left_ref_elm_left_ref_elm_body_stmt_lhs_type = UtilityFunctions.getType(left_ref_elm_left_ref_elm_body_stmt_lhs);
		assertNotNull(left_ref_elm_left_ref_elm_body_stmt_lhs_type);
		assertTrue(left_ref_elm_left_ref_elm_body_stmt_lhs_type instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(left_ref_elm_left_ref_elm_body_stmt_lhs_type, "name").equals("bool"));
		EObject left_ref_elm_left_ref_elm_body_stmt_expr = UtilityFunctions.getExpr(left_ref_elm_left_ref_elm_body_stmt);
		assertNotNull(left_ref_elm_left_ref_elm_body_stmt_expr);
		assertTrue(left_ref_elm_left_ref_elm_body_stmt_expr instanceof BinaryExpr);
		assertTrue(UtilityFunctions.getStringProperty(left_ref_elm_left_ref_elm_body_stmt_expr, "op").equals("->"));
		EObject left_ref_elm_left_ref_elm_body_stmt_left = UtilityFunctions.getLeft(left_ref_elm_left_ref_elm_body_stmt_expr);
		assertNotNull(left_ref_elm_left_ref_elm_body_stmt_left);
		assertTrue(left_ref_elm_left_ref_elm_body_stmt_left instanceof BoolLitExpr);
		EObject left_ref_elm_left_ref_elm_body_stmt_left_val = UtilityFunctions.getVal(left_ref_elm_left_ref_elm_body_stmt_left);
		assertNotNull(left_ref_elm_left_ref_elm_body_stmt_left_val);
		assertTrue(left_ref_elm_left_ref_elm_body_stmt_left_val instanceof BooleanLiteral);
		assertFalse(Boolean.parseBoolean(UtilityFunctions.getStringProperty(left_ref_elm_left_ref_elm_body_stmt_left_val, "value")));
		EObject left_ref_elm_left_ref_elm_body_stmt_right = UtilityFunctions.getRight(left_ref_elm_left_ref_elm_body_stmt_expr);
		assertNotNull(left_ref_elm_left_ref_elm_body_stmt_right);
		assertTrue(left_ref_elm_left_ref_elm_body_stmt_right instanceof PreExpr);
		EObject left_ref_elm_left_ref_elm_body_stmt_right_expr = UtilityFunctions.getExpr(left_ref_elm_left_ref_elm_body_stmt_right);
		assertNotNull(left_ref_elm_left_ref_elm_body_stmt_right_expr);
		assertTrue(left_ref_elm_left_ref_elm_body_stmt_right_expr instanceof NamedElmExpr);
		EObject left_ref_elm_left_ref_elm_body_stmt_right_expr_elm = UtilityFunctions.getElm(left_ref_elm_left_ref_elm_body_stmt_right_expr);
		assertNotNull(left_ref_elm_left_ref_elm_body_stmt_right_expr_elm);
		assertTrue(UtilityFunctions.getStringProperty(left_ref_elm_left_ref_elm_body_stmt_right_expr_elm, "name").equals("inp"));
		EObject left_ref_elm_left_ref_elm_body_stmt_right_expr_elm_type = UtilityFunctions.getType(left_ref_elm_left_ref_elm_body_stmt_right_expr_elm);
		assertNotNull(left_ref_elm_left_ref_elm_body_stmt_right_expr_elm_type);
		assertTrue(left_ref_elm_left_ref_elm_body_stmt_right_expr_elm_type instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(left_ref_elm_left_ref_elm_body_stmt_right_expr_elm_type, "name").equals("bool"));
		EObject left_ref_elm_left_ref_elm_rets = UtilityFunctions.getRet(left_ref_elm_left_ref_elm, 0);
		assertNotNull(left_ref_elm_left_ref_elm_rets);
		assertTrue(UtilityFunctions.getStringProperty(left_ref_elm_left_ref_elm_rets, "name").equals("outp"));
		EObject left_ref_elm_left_ref_elm_rets_type = UtilityFunctions.getType(left_ref_elm_left_ref_elm_rets);
		assertNotNull(left_ref_elm_left_ref_elm_rets_type instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(left_ref_elm_left_ref_elm_rets_type, "name").equals("bool"));
		EObject left_ref_elm_right = UtilityFunctions.getRight(left_ref_elm_expr);
		assertNotNull(left_ref_elm_right);
		assertTrue(left_ref_elm_right instanceof NamedElmExpr);
		EObject left_ref_elm_right_elm = UtilityFunctions.getElm(left_ref_elm_right);
		assertNotNull(left_ref_elm_right_elm);
		assertTrue(UtilityFunctions.getStringProperty(left_ref_elm_right_elm, "name").equals("p"));
		EObject left_ref_elm_right_elm_type = UtilityFunctions.getType(left_ref_elm_right_elm);
		assertNotNull(left_ref_elm_right_elm_type);
		assertTrue(left_ref_elm_right_elm_type instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(left_ref_elm_right_elm_type, "name").equals("bool"));
		EObject right = UtilityFunctions.getRight(expr);
		assertNotNull(right);
		assertTrue(right instanceof CallExpr);
		EObject right_arg = UtilityFunctions.getArg(right, 0);
		assertNotNull(right_arg);
		assertTrue(right_arg instanceof NamedElmExpr);
		EObject right_arg_elm = UtilityFunctions.getElm(right_arg);
		assertNotNull(right_arg_elm);
		assertTrue(UtilityFunctions.getStringProperty(right_arg_elm, "name").equals("p"));
		EObject right_arg_elm_type = UtilityFunctions.getType(right_arg_elm);
		assertNotNull(right_arg_elm_type);
		assertTrue(right_arg_elm_type instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(right_arg_elm_type, "name").equals("bool"));
		EObject right_ref = UtilityFunctions.getRef(right);
		assertNotNull(right_ref);
		assertTrue(right_ref instanceof DoubleDotRef);
		EObject right_ref_elm = UtilityFunctions.getElm(right_ref);
		assertNotNull(right_ref_elm);
		assertTrue(right_ref_elm instanceof NodeDef);
		assertTrue(UtilityFunctions.getStringProperty(right_ref_elm, "name").equals("H"));
		EObject right_ref_elm_arg = UtilityFunctions.getArg(right_ref_elm, 0);
		assertNotNull(right_ref_elm_arg);
		assertTrue(UtilityFunctions.getStringProperty(right_ref_elm_arg, "name").equals("inp"));
		EObject right_ref_elm_arg_type = UtilityFunctions.getType(right_ref_elm_arg);
		assertNotNull(right_ref_elm_arg_type);
		assertTrue(right_ref_elm_arg_type instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(right_ref_elm_arg_type, "name").equals("bool"));
		EObject right_ref_elm_ret = UtilityFunctions.getRet(right_ref_elm, 0);
		assertNotNull(right_ref_elm_ret);
		assertTrue(UtilityFunctions.getStringProperty(right_ref_elm_ret, "name").equals("outp"));
		EObject right_ref_elm_ret_type = UtilityFunctions.getType(right_ref_elm_ret);
		assertNotNull(right_ref_elm_ret_type);
		assertTrue(right_ref_elm_ret_type instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(right_ref_elm_ret_type, "name").equals("bool"));
		EObject right_ref_elm_body = UtilityFunctions.getNodeBody(right_ref_elm);
		assertNotNull(right_ref_elm_body);
		assertTrue(right_ref_elm_body instanceof NodeBodyExpr);
		EObject right_ref_elm_stmt = UtilityFunctions.getStmt(right_ref_elm_body, 0);
		assertNotNull(right_ref_elm_stmt);
		assertTrue(right_ref_elm_stmt instanceof NodeEq);
		EObject right_ref_elm_lhs = stmt.eCrossReferences().get(0);
		assertNotNull(right_ref_elm_lhs);
		assertTrue(UtilityFunctions.getStringProperty(right_ref_elm_lhs, "name").equals("outp"));
		EObject right_ref_elm_lhs_type = UtilityFunctions.getType(right_ref_elm_lhs);
		assertNotNull(right_ref_elm_lhs_type);
		assertTrue(right_ref_elm_lhs_type instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(right_ref_elm_lhs_type, "name").equals("bool"));
		EObject right_ref_elm_expr = UtilityFunctions.getExpr(right_ref_elm_stmt);
		assertNotNull(right_ref_elm_expr);
		assertTrue(right_ref_elm_expr instanceof BinaryExpr);
		assertTrue(UtilityFunctions.getStringProperty(right_ref_elm_expr, "op").equals("->"));
		EObject right_ref_elm_left = UtilityFunctions.getLeft(right_ref_elm_expr);
		assertNotNull(right_ref_elm_left);
		assertTrue(right_ref_elm_left instanceof NamedElmExpr);
		EObject right_ref_elm_left_elm = UtilityFunctions.getElm(right_ref_elm_left);
		assertNotNull(right_ref_elm_left_elm);
		assertTrue(UtilityFunctions.getStringProperty(right_ref_elm_left_elm, "name").equals("inp"));
		EObject right_ref_elm_left_elm_type = UtilityFunctions.getType(right_ref_elm_left_elm);
		assertNotNull(right_ref_elm_left_elm_type);
		assertTrue(right_ref_elm_left_elm_type instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(right_ref_elm_left_elm_type, "name").equals("bool"));
		EObject right_ref_elm_right = UtilityFunctions.getRight(right_ref_elm_expr);
		assertNotNull(right_ref_elm_right);
		assertTrue(right_ref_elm_right instanceof BinaryExpr);
		assertTrue(UtilityFunctions.getStringProperty(right_ref_elm_right, "op").equals("and"));
		EObject right_ref_elm_right_left = UtilityFunctions.getLeft(right_ref_elm_right);
		assertNotNull(right_ref_elm_right_left);
		assertTrue(right_ref_elm_right_left instanceof NamedElmExpr);
		EObject right_ref_elm_right_left_elm = UtilityFunctions.getElm(right_ref_elm_right_left);
		assertNotNull(right_ref_elm_right_left_elm);
		assertTrue(UtilityFunctions.getStringProperty(right_ref_elm_right_left_elm, "name").equals("inp"));
		EObject right_ref_elm_right_left_elm_type = UtilityFunctions.getType(right_ref_elm_right_left_elm);
		assertNotNull(right_ref_elm_right_left_elm_type);
		assertTrue(right_ref_elm_right_left_elm_type instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(right_ref_elm_right_left_elm_type, "name").equals("bool"));
		EObject right_ref_elm_right_right = UtilityFunctions.getRight(right_ref_elm_right);
		assertNotNull(right_ref_elm_right_right);
		assertTrue(right_ref_elm_right_right instanceof PreExpr);
		EObject right_ref_elm_right_right_expr = UtilityFunctions.getExpr(right_ref_elm_right_right);
		assertNotNull(right_ref_elm_right_right_expr);
		assertTrue(right_ref_elm_right_right_expr instanceof NamedElmExpr);
		EObject right_ref_elm_right_right_expr_elm = UtilityFunctions.getElm(right_ref_elm_right_right_expr);
		assertNotNull(right_ref_elm_right_right_expr_elm);
		assertTrue(UtilityFunctions.getStringProperty(right_ref_elm_right_right_expr_elm, "name").equals("outp"));
		EObject right_ref_elm_right_right_expr_elm_type = UtilityFunctions.getType(right_ref_elm_right_right_expr_elm);
		assertNotNull(right_ref_elm_right_right_expr_elm_type);
		assertTrue(right_ref_elm_right_right_expr_elm_type instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(right_ref_elm_right_right_expr_elm_type, "name").equals("bool"));
	}

	public void testSteeringAxle(EObject pub_sec) {
		EObject owned_classifiers = UtilityFunctions.getownedClassifier(pub_sec, "Axle");
		EObject annex_subclause = UtilityFunctions.getAnnexSubclause(owned_classifiers, "agree");
		EObject parsed_annex_subclause = UtilityFunctions.getParsedAnnexSubclause(annex_subclause);
		EObject contract = UtilityFunctions.getContract(parsed_annex_subclause);
		assertNotNull(contract);
		assertTrue(contract instanceof AgreeContract);
		EObject spec = UtilityFunctions.getSpec(contract, 0);
		assertNotNull(spec);
		assertTrue(spec instanceof GuaranteeStatement);
		assertTrue(UtilityFunctions.getStringProperty(spec, "name").equals("G_axle_1"));
		assertTrue(UtilityFunctions.getStringProperty(spec, "str").equals("roll limiter"));
		EObject expr = UtilityFunctions.getExpr(spec);
		assertNotNull(expr);
		assertTrue(expr instanceof IfThenElseExpr);
		EObject expr_a = UtilityFunctions.getA(expr);
		assertNotNull(expr_a);
		assertTrue(expr_a instanceof BinaryExpr);
		assertTrue(UtilityFunctions.getStringProperty(expr_a, "op").equals("and"));
		EObject expr_a_left = UtilityFunctions.getLeft(expr_a);
		assertNotNull(expr_a_left);
		assertTrue(expr_a_left instanceof BinaryExpr);
		assertTrue(UtilityFunctions.getStringProperty(expr_a_left, "op").equals(">"));
		EObject expr_a_left_left = UtilityFunctions.getLeft(expr_a_left);
		assertNotNull(expr_a_left_left);
		assertTrue(expr_a_left_left instanceof CallExpr);
		EObject expr_a_left_left_arg = UtilityFunctions.getArg(expr_a_left_left, 0);
		assertNotNull(expr_a_left_left_arg);
		assertTrue(expr_a_left_left_arg instanceof SelectionExpr);
		EObject expr_a_left_left_arg_field = UtilityFunctions.getField(expr_a_left_left_arg);
		assertNotNull(expr_a_left_left_arg_field);
		assertTrue(expr_a_left_left_arg_field instanceof DataSubcomponent);
		assertTrue(UtilityFunctions.getStringProperty(expr_a_left_left_arg_field, "name").equals("val"));
		EObject expr_a_left_left_arg_target = UtilityFunctions.getTarget(expr_a_left_left_arg);
		assertNotNull(expr_a_left_left_arg_target);
		assertTrue(expr_a_left_left_arg_target instanceof NamedElmExpr);
		EObject expr_a_left_left_arg_target_elm = UtilityFunctions.getElm(expr_a_left_left_arg_target);
		assertNotNull(expr_a_left_left_arg_target_elm);
		assertTrue(expr_a_left_left_arg_target_elm instanceof DataPort);
		assertTrue(UtilityFunctions.getStringProperty(expr_a_left_left_arg_target_elm, "name").equals("Target_Tire_Direction"));
		assertTrue(Boolean.parseBoolean(UtilityFunctions.getStringProperty(expr_a_left_left_arg_target_elm, "in")));
		assertFalse(Boolean.parseBoolean(UtilityFunctions.getStringProperty(expr_a_left_left_arg_target_elm, "out")));
		EObject expr_a_left_left_ref = UtilityFunctions.getRef(expr_a_left_left);
		assertNotNull(expr_a_left_left_ref);
		assertTrue(expr_a_left_left_ref instanceof DoubleDotRef);
		EObject expr_a_left_left_ref_elm = UtilityFunctions.getElm(expr_a_left_left_ref);
		assertNotNull(expr_a_left_left_ref_elm);
		assertTrue(expr_a_left_left_ref_elm instanceof NodeDef);
		assertTrue(UtilityFunctions.getStringProperty(expr_a_left_left_ref_elm, "name").equals("abs"));
		EObject expr_a_left_left_ref_elm_arg = UtilityFunctions.getArg(expr_a_left_left_ref_elm, 0);
		assertNotNull(expr_a_left_left_ref_elm_arg);
		assertTrue(UtilityFunctions.getStringProperty(expr_a_left_left_ref_elm_arg, "name").equals("inp"));
		EObject expr_a_left_left_ref_elm_arg_type = UtilityFunctions.getType(expr_a_left_left_ref_elm_arg);
		assertNotNull(expr_a_left_left_ref_elm_arg_type);
		assertTrue(expr_a_left_left_ref_elm_arg_type instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(expr_a_left_left_ref_elm_arg_type, "name").equals("real"));
		EObject expr_a_left_left_ref_elm_ret = UtilityFunctions.getRet(expr_a_left_left_ref_elm, 0);
		assertNotNull(expr_a_left_left_ref_elm_ret);
		assertTrue(UtilityFunctions.getStringProperty(expr_a_left_left_ref_elm_ret, "name").equals("outp"));
		EObject expr_a_left_left_ref_elm_ret_type = UtilityFunctions.getType(expr_a_left_left_ref_elm_ret);
		assertNotNull(expr_a_left_left_ref_elm_ret_type);
		assertTrue(expr_a_left_left_ref_elm_ret_type instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(expr_a_left_left_ref_elm_ret_type, "name").equals("real"));
		EObject expr_a_left_left_ref_elm_body = UtilityFunctions.getNodeBody(expr_a_left_left_ref_elm);
		assertNotNull(expr_a_left_left_ref_elm_body);
		assertTrue(expr_a_left_left_ref_elm_body instanceof NodeBodyExpr);
		EObject expr_a_left_left_ref_elm_stmt = UtilityFunctions.getStmt(expr_a_left_left_ref_elm_body, 0);
		assertNotNull(expr_a_left_left_ref_elm_stmt);
		assertTrue(expr_a_left_left_ref_elm_stmt instanceof NodeEq);
		EObject expr_a_left_left_ref_elm_lhs = expr_a_left_left_ref_elm_stmt.eCrossReferences().get(0);
		assertNotNull(expr_a_left_left_ref_elm_lhs);
		assertTrue(UtilityFunctions.getStringProperty(expr_a_left_left_ref_elm_lhs, "name").equals("outp"));
		EObject expr_a_left_left_ref_elm_lhs_type = UtilityFunctions.getType(expr_a_left_left_ref_elm_lhs);
		assertNotNull(expr_a_left_left_ref_elm_lhs_type);
		assertTrue(expr_a_left_left_ref_elm_lhs_type instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(expr_a_left_left_ref_elm_lhs_type, "name").equals("real"));
		EObject expr_a_left_left_ref_elm_expr = UtilityFunctions.getExpr(expr_a_left_left_ref_elm_stmt);
		assertNotNull(expr_a_left_left_ref_elm_expr);
		assertTrue(expr_a_left_left_ref_elm_expr instanceof IfThenElseExpr);
		EObject expr_a_left_left_ref_elm_expr_a = UtilityFunctions.getA(expr_a_left_left_ref_elm_expr);
		assertNotNull(expr_a_left_left_ref_elm_expr_a);
		assertTrue(expr_a_left_left_ref_elm_expr_a instanceof BinaryExpr);
		assertTrue(UtilityFunctions.getStringProperty(expr_a_left_left_ref_elm_expr_a, "op").equals("<"));
		EObject expr_a_left_left_ref_elm_expr_a_left = UtilityFunctions.getLeft(expr_a_left_left_ref_elm_expr_a);
		assertNotNull(expr_a_left_left_ref_elm_expr_a_left);
		assertTrue(expr_a_left_left_ref_elm_expr_a_left instanceof NamedElmExpr);
		EObject expr_a_left_left_ref_elm_expr_a_left_elm = UtilityFunctions.getElm(expr_a_left_left_ref_elm_expr_a_left);
		assertNotNull(expr_a_left_left_ref_elm_expr_a_left_elm);
		assertTrue(UtilityFunctions.getStringProperty(expr_a_left_left_ref_elm_expr_a_left_elm, "name").equals("inp"));
		EObject expr_a_left_left_ref_elm_expr_a_left_elm_type = UtilityFunctions.getType(expr_a_left_left_ref_elm_expr_a_left_elm);
		assertNotNull(expr_a_left_left_ref_elm_expr_a_left_elm_type);
		assertTrue(expr_a_left_left_ref_elm_expr_a_left_elm_type instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(expr_a_left_left_ref_elm_expr_a_left_elm_type, "name").equals("real"));
		EObject expr_a_left_left_ref_elm_expr_b = UtilityFunctions.getB(expr_a_left_left_ref_elm_expr);
		assertNotNull(expr_a_left_left_ref_elm_expr_b);
		assertTrue(expr_a_left_left_ref_elm_expr_b instanceof UnaryExpr);
		assertTrue(UtilityFunctions.getStringProperty(expr_a_left_left_ref_elm_expr_b, "op").equals("-"));
		EObject expr_a_left_left_ref_elm_expr_b_expr = UtilityFunctions.getExpr(expr_a_left_left_ref_elm_expr_b);
		assertNotNull(expr_a_left_left_ref_elm_expr_b_expr);
		assertTrue(expr_a_left_left_ref_elm_expr_b_expr instanceof NamedElmExpr);
		EObject expr_a_left_left_ref_elm_expr_b_expr_elm = UtilityFunctions.getElm(expr_a_left_left_ref_elm_expr_b_expr);
		assertNotNull(expr_a_left_left_ref_elm_expr_b_expr_elm);
		assertTrue(UtilityFunctions.getStringProperty(expr_a_left_left_ref_elm_expr_b_expr_elm, "name").equals("inp"));
		EObject expr_a_left_left_ref_elm_expr_b_expr_elm_type = UtilityFunctions.getType(expr_a_left_left_ref_elm_expr_b_expr_elm);
		assertNotNull(expr_a_left_left_ref_elm_expr_b_expr_elm_type);
		assertTrue(expr_a_left_left_ref_elm_expr_b_expr_elm_type instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(expr_a_left_left_ref_elm_expr_b_expr_elm_type, "name").equals("real"));
		EObject expr_a_left_left_ref_elm_expr_c = UtilityFunctions.getC(expr_a_left_left_ref_elm_expr);
		assertNotNull(expr_a_left_left_ref_elm_expr_c);
		assertTrue(expr_a_left_left_ref_elm_expr_c instanceof NamedElmExpr);
		EObject expr_a_left_left_ref_elm_expr_c_elm = UtilityFunctions.getElm(expr_a_left_left_ref_elm_expr_c);
		assertNotNull(expr_a_left_left_ref_elm_expr_c_elm);
		assertTrue(UtilityFunctions.getStringProperty(expr_a_left_left_ref_elm_expr_c_elm, "name").equals("inp"));
		EObject expr_a_left_left_ref_elm_expr_c_elm_type = UtilityFunctions.getType(expr_a_left_left_ref_elm_expr_c_elm);
		assertNotNull(expr_a_left_left_ref_elm_expr_c_elm_type);
		assertTrue(expr_a_left_left_ref_elm_expr_c_elm_type instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(expr_a_left_left_ref_elm_expr_c_elm_type, "name").equals("real"));
		EObject expr_a_left_right = UtilityFunctions.getRight(expr_a_left);
		assertNotNull(expr_a_left_right);
		assertTrue(expr_a_left_right instanceof RealLitExpr);
		assertTrue(Double.parseDouble(UtilityFunctions.getStringProperty(expr_a_left_right, "val")) == 0.20);
		EObject expr_a_right = UtilityFunctions.getRight(expr_a);
		assertNotNull(expr_a_right);
		assertTrue(expr_a_right instanceof BinaryExpr);
		assertTrue(UtilityFunctions.getStringProperty(expr_a_right, "op").equals(">"));
		EObject expr_a_right_left = UtilityFunctions.getLeft(expr_a_right);
		assertNotNull(expr_a_right_left);
		assertTrue(expr_a_right_left instanceof SelectionExpr);
		EObject expr_a_right_left_field = UtilityFunctions.getField(expr_a_right_left);
		assertNotNull(expr_a_right_left_field);
		assertTrue(expr_a_right_left_field instanceof DataSubcomponent);
		assertTrue(UtilityFunctions.getStringProperty(expr_a_right_left_field, "name").equals("val"));
		EObject expr_a_right_left_target = UtilityFunctions.getTarget(expr_a_right_left);
		assertNotNull(expr_a_right_left_target);
		assertTrue(expr_a_right_left_target instanceof NamedElmExpr);
		EObject expr_a_right_left_target_elm = UtilityFunctions.getElm(expr_a_right_left_target);
		assertNotNull(expr_a_right_left_target_elm);
		assertTrue(expr_a_right_left_target_elm instanceof DataPort);
		assertTrue(UtilityFunctions.getStringProperty(expr_a_right_left_target_elm, "name").equals("Speed"));
		assertTrue(Boolean.parseBoolean(UtilityFunctions.getStringProperty(expr_a_right_left_target_elm, "in")));
		assertFalse(Boolean.parseBoolean(UtilityFunctions.getStringProperty(expr_a_right_left_target_elm, "out")));
		EObject expr_a_right_right = UtilityFunctions.getRight(expr_a_right);
		assertNotNull(expr_a_right_right);
		assertTrue(expr_a_right_right instanceof RealLitExpr);
		assertTrue(Double.parseDouble(UtilityFunctions.getStringProperty(expr_a_right_right, "val")) == 45.0);
		EObject expr_b = UtilityFunctions.getB(expr);
		assertNotNull(expr_b);
		assertTrue(expr_b instanceof BinaryExpr);
		assertTrue(UtilityFunctions.getStringProperty(expr_b, "op").equals("="));
		EObject expr_b_left = UtilityFunctions.getLeft(expr_b);
		assertNotNull(expr_b_left);
		assertTrue(expr_b_left instanceof SelectionExpr);
		EObject expr_b_left_field = UtilityFunctions.getField(expr_b_left);
		assertNotNull(expr_b_left_field);
		assertTrue(expr_b_left_field instanceof DataSubcomponent);
		assertTrue(UtilityFunctions.getStringProperty(expr_b_left_field, "name").equals("val"));
		EObject expr_b_left_target = UtilityFunctions.getTarget(expr_b_left);
		assertNotNull(expr_b_left_target);
		assertTrue(expr_b_left_target instanceof NamedElmExpr);
		EObject expr_b_left_target_elm = UtilityFunctions.getElm(expr_b_left_target);
		assertNotNull(expr_b_left_target_elm);
		assertTrue(expr_b_left_target_elm instanceof DataPort);
		assertTrue(UtilityFunctions.getStringProperty(expr_b_left_target_elm, "name").equals("Actual_Tire_Direction"));
		assertFalse(Boolean.parseBoolean(UtilityFunctions.getStringProperty(expr_b_left_target_elm, "in")));
		assertTrue(Boolean.parseBoolean(UtilityFunctions.getStringProperty(expr_b_left_target_elm, "out")));
		EObject expr_b_right = UtilityFunctions.getRight(expr_b);
		assertNotNull(expr_b_right);
		assertTrue(expr_b_right instanceof RealLitExpr);
		assertTrue(Double.parseDouble(UtilityFunctions.getStringProperty(expr_b_right, "val")) == 0.20);
		EObject expr_c = UtilityFunctions.getC(expr);
		assertNotNull(expr_c);
		assertTrue(expr_c instanceof BinaryExpr);
		assertTrue(UtilityFunctions.getStringProperty(expr_c, "op").equals("="));
		EObject expr_c_left = UtilityFunctions.getLeft(expr_c);
		assertNotNull(expr_c_left);
		assertTrue(expr_c_left instanceof SelectionExpr);
		EObject expr_c_left_field = UtilityFunctions.getField(expr_c_left);
		assertNotNull(expr_c_left_field);
		assertTrue(expr_c_left_field instanceof DataSubcomponent);
		assertTrue(UtilityFunctions.getStringProperty(expr_c_left_field, "name").equals("val"));
		EObject expr_c_left_target = UtilityFunctions.getTarget(expr_c_left);
		assertNotNull(expr_c_left_target);
		assertTrue(expr_c_left_target instanceof NamedElmExpr);
		EObject expr_c_left_target_elm = UtilityFunctions.getElm(expr_c_left_target);
		assertNotNull(expr_c_left_target_elm);
		assertTrue(expr_c_left_target_elm instanceof DataPort);
		assertTrue(UtilityFunctions.getStringProperty(expr_c_left_target_elm, "name").equals("Actual_Tire_Direction"));
		assertFalse(Boolean.parseBoolean(UtilityFunctions.getStringProperty(expr_c_left_target_elm, "in")));
		assertTrue(Boolean.parseBoolean(UtilityFunctions.getStringProperty(expr_c_left_target_elm, "out")));
		EObject expr_c_right = UtilityFunctions.getRight(expr_c);
		assertNotNull(expr_c_right);
		assertTrue(expr_c_right instanceof SelectionExpr);
		EObject expr_c_right_field = UtilityFunctions.getField(expr_c_right);
		assertNotNull(expr_c_right_field);
		assertTrue(expr_c_right_field instanceof DataSubcomponent);
		assertTrue(UtilityFunctions.getStringProperty(expr_c_right_field, "name").equals("val"));
		EObject expr_c_right_target = UtilityFunctions.getTarget(expr_c_right);
		assertNotNull(expr_c_right_target);
		assertTrue(expr_c_right_target instanceof NamedElmExpr);
		EObject expr_c_right_target_elm = UtilityFunctions.getElm(expr_c_right_target);
		assertNotNull(expr_c_right_target_elm);
		assertTrue(expr_c_right_target_elm instanceof DataPort);
		assertTrue(UtilityFunctions.getStringProperty(expr_c_right_target_elm, "name").equals("Target_Tire_Direction"));
		assertTrue(Boolean.parseBoolean(UtilityFunctions.getStringProperty(expr_c_right_target_elm, "in")));
		assertFalse(Boolean.parseBoolean(UtilityFunctions.getStringProperty(expr_c_right_target_elm, "out")));
	}

	public void testSteeringAxleImpl(EObject pub_sec) {
		EObject owned_classifiers = UtilityFunctions.getownedClassifier(pub_sec, "Axle.Axle_Impl");
		EObject annex_subclause = UtilityFunctions.getAnnexSubclause(owned_classifiers, "agree");
		EObject parsed_annex_subclause = UtilityFunctions.getParsedAnnexSubclause(annex_subclause);
		EObject contract = UtilityFunctions.getContract(parsed_annex_subclause);
		assertNotNull(contract);
		assertTrue(contract instanceof AgreeContract);
		EObject spec = UtilityFunctions.getSpec(contract, 0);
		assertNotNull(spec);
		assertTrue(spec instanceof AssignStatement);
		EObject id = UtilityFunctions.getID(spec);
		assertNotNull(id);
		assertTrue(id instanceof DataPort);
		assertTrue(UtilityFunctions.getStringProperty(id, "name").equals("Actual_Tire_Direction"));
		assertFalse(Boolean.parseBoolean(UtilityFunctions.getStringProperty(id, "in")));
		assertTrue(Boolean.parseBoolean(UtilityFunctions.getStringProperty(id, "out")));
		EObject expr = UtilityFunctions.getExpr(spec);
		assertNotNull(expr);
		assertTrue(expr instanceof RecordLitExpr);
		EObject expr_arg = expr.eCrossReferences().get(0);
		assertNotNull(expr_arg);
		assertTrue(expr_arg instanceof DataSubcomponent);
		assertTrue(UtilityFunctions.getStringProperty(expr_arg, "name").equals("val"));
		EObject expr_arg_expr = UtilityFunctions.getArgExpr(expr, 0);
		assertNotNull(expr_arg_expr);
		assertTrue(expr_arg_expr instanceof IfThenElseExpr);
		EObject expr_a = UtilityFunctions.getA(expr_arg_expr);
		assertNotNull(expr_a);
		assertTrue(expr_a instanceof BinaryExpr);
		assertTrue(UtilityFunctions.getStringProperty(expr_a, "op").equals("and"));
		EObject expr_a_left = UtilityFunctions.getLeft(expr_a);
		assertNotNull(expr_a_left);
		assertTrue(expr_a_left instanceof BinaryExpr);
		assertTrue(UtilityFunctions.getStringProperty(expr_a_left, "op").equals(">"));
		EObject expr_a_left_left = UtilityFunctions.getLeft(expr_a_left);
		assertNotNull(expr_a_left_left);
		assertTrue(expr_a_left_left instanceof CallExpr);
		EObject expr_a_left_left_arg = UtilityFunctions.getArg(expr_a_left_left, 0);
		assertNotNull(expr_a_left_left_arg);
		assertTrue(expr_a_left_left_arg instanceof SelectionExpr);
		EObject expr_a_left_left_arg_field = UtilityFunctions.getField(expr_a_left_left_arg);
		assertNotNull(expr_a_left_left_arg_field);
		assertTrue(expr_a_left_left_arg_field instanceof DataSubcomponent);
		assertTrue(UtilityFunctions.getStringProperty(expr_a_left_left_arg_field, "name").equals("val"));
		EObject expr_a_left_left_arg_target = UtilityFunctions.getTarget(expr_a_left_left_arg);
		assertNotNull(expr_a_left_left_arg_target);
		assertTrue(expr_a_left_left_arg_target instanceof NamedElmExpr);
		EObject expr_a_left_left_arg_target_elm = UtilityFunctions.getElm(expr_a_left_left_arg_target);
		assertNotNull(expr_a_left_left_arg_target_elm);
		assertTrue(expr_a_left_left_arg_target_elm instanceof DataPort);
		assertTrue(UtilityFunctions.getStringProperty(expr_a_left_left_arg_target_elm, "name").equals("Target_Tire_Direction"));
		assertTrue(Boolean.parseBoolean(UtilityFunctions.getStringProperty(expr_a_left_left_arg_target_elm, "in")));
		assertFalse(Boolean.parseBoolean(UtilityFunctions.getStringProperty(expr_a_left_left_arg_target_elm, "out")));
		EObject expr_a_left_left_ref = UtilityFunctions.getRef(expr_a_left_left);
		assertNotNull(expr_a_left_left_ref);
		assertTrue(expr_a_left_left_ref instanceof DoubleDotRef);
		EObject expr_a_left_left_ref_elm = UtilityFunctions.getElm(expr_a_left_left_ref);
		assertNotNull(expr_a_left_left_ref_elm);
		assertTrue(expr_a_left_left_ref_elm instanceof NodeDef);
		assertTrue(UtilityFunctions.getStringProperty(expr_a_left_left_ref_elm, "name").equals("abs"));
		EObject expr_a_left_left_ref_elm_arg = UtilityFunctions.getArg(expr_a_left_left_ref_elm, 0);
		assertNotNull(expr_a_left_left_ref_elm_arg);
		assertTrue(UtilityFunctions.getStringProperty(expr_a_left_left_ref_elm_arg, "name").equals("inp"));
		EObject expr_a_left_left_ref_elm_arg_type = UtilityFunctions.getType(expr_a_left_left_ref_elm_arg);
		assertNotNull(expr_a_left_left_ref_elm_arg_type);
		assertTrue(expr_a_left_left_ref_elm_arg_type instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(expr_a_left_left_ref_elm_arg_type, "name").equals("real"));
		EObject expr_a_left_left_ref_elm_ret = UtilityFunctions.getRet(expr_a_left_left_ref_elm, 0);
		assertNotNull(expr_a_left_left_ref_elm_ret);
		assertTrue(UtilityFunctions.getStringProperty(expr_a_left_left_ref_elm_ret, "name").equals("outp"));
		EObject expr_a_left_left_ref_elm_ret_type = UtilityFunctions.getType(expr_a_left_left_ref_elm_ret);
		assertNotNull(expr_a_left_left_ref_elm_ret_type);
		assertTrue(expr_a_left_left_ref_elm_ret_type instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(expr_a_left_left_ref_elm_ret_type, "name").equals("real"));
		EObject expr_a_left_left_ref_elm_body = UtilityFunctions.getNodeBody(expr_a_left_left_ref_elm);
		assertNotNull(expr_a_left_left_ref_elm_body);
		assertTrue(expr_a_left_left_ref_elm_body instanceof NodeBodyExpr);
		EObject expr_a_left_left_ref_elm_stmt = UtilityFunctions.getStmt(expr_a_left_left_ref_elm_body, 0);
		assertNotNull(expr_a_left_left_ref_elm_stmt);
		assertTrue(expr_a_left_left_ref_elm_stmt instanceof NodeEq);
		EObject expr_a_left_left_ref_elm_lhs = expr_a_left_left_ref_elm_stmt.eCrossReferences().get(0);
		assertNotNull(expr_a_left_left_ref_elm_lhs);
		assertTrue(UtilityFunctions.getStringProperty(expr_a_left_left_ref_elm_lhs, "name").equals("outp"));
		EObject expr_a_left_left_ref_elm_lhs_type = UtilityFunctions.getType(expr_a_left_left_ref_elm_lhs);
		assertNotNull(expr_a_left_left_ref_elm_lhs_type);
		assertTrue(expr_a_left_left_ref_elm_lhs_type instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(expr_a_left_left_ref_elm_lhs_type, "name").equals("real"));
		EObject expr_a_left_left_ref_elm_expr = UtilityFunctions.getExpr(expr_a_left_left_ref_elm_stmt);
		assertNotNull(expr_a_left_left_ref_elm_expr);
		assertTrue(expr_a_left_left_ref_elm_expr instanceof IfThenElseExpr);
		EObject expr_a_left_left_ref_elm_expr_a = UtilityFunctions.getA(expr_a_left_left_ref_elm_expr);
		assertNotNull(expr_a_left_left_ref_elm_expr_a);
		assertTrue(expr_a_left_left_ref_elm_expr_a instanceof BinaryExpr);
		assertTrue(UtilityFunctions.getStringProperty(expr_a_left_left_ref_elm_expr_a, "op").equals("<"));
		EObject expr_a_left_left_ref_elm_expr_a_left = UtilityFunctions.getLeft(expr_a_left_left_ref_elm_expr_a);
		assertNotNull(expr_a_left_left_ref_elm_expr_a_left);
		assertTrue(expr_a_left_left_ref_elm_expr_a_left instanceof NamedElmExpr);
		EObject expr_a_left_left_ref_elm_expr_a_left_elm = UtilityFunctions.getElm(expr_a_left_left_ref_elm_expr_a_left);
		assertNotNull(expr_a_left_left_ref_elm_expr_a_left_elm);
		assertTrue(UtilityFunctions.getStringProperty(expr_a_left_left_ref_elm_expr_a_left_elm, "name").equals("inp"));
		EObject expr_a_left_left_ref_elm_expr_a_left_elm_type = UtilityFunctions.getType(expr_a_left_left_ref_elm_expr_a_left_elm);
		assertNotNull(expr_a_left_left_ref_elm_expr_a_left_elm_type);
		assertTrue(expr_a_left_left_ref_elm_expr_a_left_elm_type instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(expr_a_left_left_ref_elm_expr_a_left_elm_type, "name").equals("real"));
		EObject expr_a_left_left_ref_elm_expr_b = UtilityFunctions.getB(expr_a_left_left_ref_elm_expr);
		assertNotNull(expr_a_left_left_ref_elm_expr_b);
		assertTrue(expr_a_left_left_ref_elm_expr_b instanceof UnaryExpr);
		assertTrue(UtilityFunctions.getStringProperty(expr_a_left_left_ref_elm_expr_b, "op").equals("-"));
		EObject expr_a_left_left_ref_elm_expr_b_expr = UtilityFunctions.getExpr(expr_a_left_left_ref_elm_expr_b);
		assertNotNull(expr_a_left_left_ref_elm_expr_b_expr);
		assertTrue(expr_a_left_left_ref_elm_expr_b_expr instanceof NamedElmExpr);
		EObject expr_a_left_left_ref_elm_expr_b_expr_elm = UtilityFunctions.getElm(expr_a_left_left_ref_elm_expr_b_expr);
		assertNotNull(expr_a_left_left_ref_elm_expr_b_expr_elm);
		assertTrue(UtilityFunctions.getStringProperty(expr_a_left_left_ref_elm_expr_b_expr_elm, "name").equals("inp"));
		EObject expr_a_left_left_ref_elm_expr_b_expr_elm_type = UtilityFunctions.getType(expr_a_left_left_ref_elm_expr_b_expr_elm);
		assertNotNull(expr_a_left_left_ref_elm_expr_b_expr_elm_type);
		assertTrue(expr_a_left_left_ref_elm_expr_b_expr_elm_type instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(expr_a_left_left_ref_elm_expr_b_expr_elm_type, "name").equals("real"));
		EObject expr_a_left_left_ref_elm_expr_c = UtilityFunctions.getC(expr_a_left_left_ref_elm_expr);
		assertNotNull(expr_a_left_left_ref_elm_expr_c);
		assertTrue(expr_a_left_left_ref_elm_expr_c instanceof NamedElmExpr);
		EObject expr_a_left_left_ref_elm_expr_c_elm = UtilityFunctions.getElm(expr_a_left_left_ref_elm_expr_c);
		assertNotNull(expr_a_left_left_ref_elm_expr_c_elm);
		assertTrue(UtilityFunctions.getStringProperty(expr_a_left_left_ref_elm_expr_c_elm, "name").equals("inp"));
		EObject expr_a_left_left_ref_elm_expr_c_elm_type = UtilityFunctions.getType(expr_a_left_left_ref_elm_expr_c_elm);
		assertNotNull(expr_a_left_left_ref_elm_expr_c_elm_type);
		assertTrue(expr_a_left_left_ref_elm_expr_c_elm_type instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(expr_a_left_left_ref_elm_expr_c_elm_type, "name").equals("real"));
		EObject expr_a_left_right = UtilityFunctions.getRight(expr_a_left);
		assertNotNull(expr_a_left_right);
		assertTrue(expr_a_left_right instanceof RealLitExpr);
		assertTrue(Double.parseDouble(UtilityFunctions.getStringProperty(expr_a_left_right, "val")) == 0.20);
		EObject expr_a_right = UtilityFunctions.getRight(expr_a);
		assertNotNull(expr_a_right);
		assertTrue(expr_a_right instanceof BinaryExpr);
		assertTrue(UtilityFunctions.getStringProperty(expr_a_right, "op").equals(">"));
		EObject expr_a_right_left = UtilityFunctions.getLeft(expr_a_right);
		assertNotNull(expr_a_right_left);
		assertTrue(expr_a_right_left instanceof SelectionExpr);
		EObject expr_a_right_left_field = UtilityFunctions.getField(expr_a_right_left);
		assertNotNull(expr_a_right_left_field);
		assertTrue(expr_a_right_left_field instanceof DataSubcomponent);
		assertTrue(UtilityFunctions.getStringProperty(expr_a_right_left_field, "name").equals("val"));
		EObject expr_a_right_left_target = UtilityFunctions.getTarget(expr_a_right_left);
		assertNotNull(expr_a_right_left_target);
		assertTrue(expr_a_right_left_target instanceof NamedElmExpr);
		EObject expr_a_right_left_target_elm = UtilityFunctions.getElm(expr_a_right_left_target);
		assertNotNull(expr_a_right_left_target_elm);
		assertTrue(expr_a_right_left_target_elm instanceof DataPort);
		assertTrue(UtilityFunctions.getStringProperty(expr_a_right_left_target_elm, "name").equals("Speed"));
		assertTrue(Boolean.parseBoolean(UtilityFunctions.getStringProperty(expr_a_right_left_target_elm, "in")));
		assertFalse(Boolean.parseBoolean(UtilityFunctions.getStringProperty(expr_a_right_left_target_elm, "out")));
		EObject expr_a_right_right = UtilityFunctions.getRight(expr_a_right);
		assertNotNull(expr_a_right_right);
		assertTrue(expr_a_right_right instanceof RealLitExpr);
		assertTrue(Double.parseDouble(UtilityFunctions.getStringProperty(expr_a_right_right, "val")) == 45.0);
		EObject expr_b = UtilityFunctions.getB(expr_arg_expr);
		assertNotNull(expr_b);
		assertTrue(expr_b instanceof RealLitExpr);
		assertTrue(Double.parseDouble(UtilityFunctions.getStringProperty(expr_b, "val")) == 0.20);
		EObject expr_c = UtilityFunctions.getC(expr_arg_expr);
		assertNotNull(expr_c);
		assertTrue(expr_c instanceof SelectionExpr);
		EObject expr_c_field = UtilityFunctions.getField(expr_c);
		assertNotNull(expr_c_field);
		assertTrue(expr_c_field instanceof DataSubcomponent);
		assertTrue(UtilityFunctions.getStringProperty(expr_c_field, "name").equals("val"));
		EObject expr_c_target = UtilityFunctions.getTarget(expr_c);
		assertNotNull(expr_c_target);
		assertTrue(expr_c_target instanceof NamedElmExpr);
		EObject expr_c_target_elm = UtilityFunctions.getElm(expr_c_target);
		assertNotNull(expr_c_target_elm);
		assertTrue(expr_c_target_elm instanceof DataPort);
		assertTrue(UtilityFunctions.getStringProperty(expr_c_target_elm, "name").equals("Target_Tire_Direction"));
		assertTrue(Boolean.parseBoolean(UtilityFunctions.getStringProperty(expr_c_target_elm, "in")));
		assertFalse(Boolean.parseBoolean(UtilityFunctions.getStringProperty(expr_c_target_elm, "out")));
		EObject expr_record_type = UtilityFunctions.getRecordType(expr);
		assertTrue(expr_record_type instanceof DoubleDotRef);
		EObject expr_record_type_elm = UtilityFunctions.getElm(expr_record_type);
		assertNotNull(expr_record_type_elm);
		assertTrue(expr_record_type_elm instanceof DataImplementation);
		assertTrue(UtilityFunctions.getStringProperty(expr_record_type_elm, "name").equals("pitch.pitch_impl"));
	}

	public void testCar(EObject pub_sec) {
		EObject owned_classifiers = UtilityFunctions.getownedClassifier(pub_sec, "Car");
		EObject annex_subclause = UtilityFunctions.getAnnexSubclause(owned_classifiers, "agree");
		EObject parsed_annex_subclause = UtilityFunctions.getParsedAnnexSubclause(annex_subclause);
		EObject contract = UtilityFunctions.getContract(parsed_annex_subclause);
		assertNotNull(contract);
		assertTrue(contract instanceof AgreeContract);
		EObject spec0 = UtilityFunctions.getSpec(contract, 0);
		assertNotNull(spec0);
		assertTrue(spec0 instanceof ConstStatement);
		assertTrue(UtilityFunctions.getStringProperty(spec0, "name").equals("max_accel"));
		EObject spec0_type = UtilityFunctions.getType(spec0);
		assertNotNull(spec0_type);
		assertTrue(spec0_type instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(spec0_type, "name").equals("real"));
		EObject spec0_expr = UtilityFunctions.getExpr(spec0);
		assertNotNull(spec0_expr);
		assertTrue(spec0_expr instanceof RealLitExpr);
		assertTrue(Double.parseDouble(UtilityFunctions.getStringProperty(spec0_expr, "val")) == 40.0);

		EObject spec1 = UtilityFunctions.getSpec(contract, 1);
		assertNotNull(spec1);
		assertTrue(spec1 instanceof AssumeStatement);
		assertTrue(UtilityFunctions.getStringProperty(spec1, "name").equals("A1"));
		assertTrue(UtilityFunctions.getStringProperty(spec1, "str").equals("target speed is positive"));
		EObject spec1_expr = UtilityFunctions.getExpr(spec1);
		assertNotNull(spec1_expr);
		assertTrue(spec1_expr instanceof BinaryExpr);
		assertTrue(UtilityFunctions.getStringProperty(spec1_expr, "op").equals(">="));
		EObject spec1_expr_left = UtilityFunctions.getLeft(spec1_expr);
		assertNotNull(spec1_expr_left);
		EObject spec1_expr_left_field = UtilityFunctions.getField(spec1_expr_left);
		assertNotNull(spec1_expr_left_field);
		assertTrue(spec1_expr_left_field instanceof DataSubcomponent);
		assertTrue(UtilityFunctions.getStringProperty(spec1_expr_left_field, "name").equals("val"));
		EObject spec1_expr_left_target = UtilityFunctions.getTarget(spec1_expr_left);
		assertNotNull(spec1_expr_left_target);
		assertTrue(spec1_expr_left_target instanceof NamedElmExpr);
		EObject spec1_expr_left_target_elm = UtilityFunctions.getElm(spec1_expr_left_target);
		assertNotNull(spec1_expr_left_target_elm);
		assertTrue(spec1_expr_left_target_elm instanceof DataPort);
		assertTrue(UtilityFunctions.getStringProperty(spec1_expr_left_target_elm, "name").equals("Target_Speed"));
		assertTrue(Boolean.parseBoolean(UtilityFunctions.getStringProperty(spec1_expr_left_target_elm, "in")));
		assertFalse(Boolean.parseBoolean(UtilityFunctions.getStringProperty(spec1_expr_left_target_elm, "out")));
		EObject spec1_expr_right = UtilityFunctions.getRight(spec1_expr);
		assertNotNull(spec1_expr_right);
		assertTrue(spec1_expr_right instanceof RealLitExpr);
		assertTrue(Double.parseDouble(UtilityFunctions.getStringProperty(spec1_expr_right, "val")) == 0.0);

		EObject spec2 = UtilityFunctions.getSpec(contract, 2);
		assertNotNull(spec2);
		assertTrue(spec2 instanceof AssumeStatement);
		assertTrue(UtilityFunctions.getStringProperty(spec2, "name").equals("A2"));
		assertTrue(UtilityFunctions.getStringProperty(spec2, "str").equals("reasonable target speed"));
		EObject spec2_expr = UtilityFunctions.getExpr(spec2);
		assertNotNull(spec2_expr);
		assertTrue(spec2_expr instanceof BinaryExpr);
		assertTrue(UtilityFunctions.getStringProperty(spec2_expr, "op").equals("<"));
		EObject spec2_expr_left = UtilityFunctions.getLeft(spec2_expr);
		assertNotNull(spec2_expr_left);
		EObject spec2_expr_left_field = UtilityFunctions.getField(spec2_expr_left);
		assertNotNull(spec2_expr_left_field);
		assertTrue(spec2_expr_left_field instanceof DataSubcomponent);
		assertTrue(UtilityFunctions.getStringProperty(spec2_expr_left_field, "name").equals("val"));
		EObject spec2_expr_left_target = UtilityFunctions.getTarget(spec2_expr_left);
		assertNotNull(spec2_expr_left_target);
		assertTrue(spec2_expr_left_target instanceof NamedElmExpr);
		EObject spec2_expr_left_target_elm = UtilityFunctions.getElm(spec2_expr_left_target);
		assertNotNull(spec2_expr_left_target_elm);
		assertTrue(spec2_expr_left_target_elm instanceof DataPort);
		assertTrue(UtilityFunctions.getStringProperty(spec2_expr_left_target_elm, "name").equals("Target_Speed"));
		assertTrue(Boolean.parseBoolean(UtilityFunctions.getStringProperty(spec2_expr_left_target_elm, "in")));
		assertFalse(Boolean.parseBoolean(UtilityFunctions.getStringProperty(spec2_expr_left_target_elm, "out")));
		EObject spec2_expr_right = UtilityFunctions.getRight(spec2_expr);
		assertNotNull(spec2_expr_right);
		assertTrue(spec2_expr_right instanceof RealLitExpr);
		assertTrue(Double.parseDouble(UtilityFunctions.getStringProperty(spec2_expr_right, "val")) == 150.0);

		EObject spec3 = UtilityFunctions.getSpec(contract, 3);
		assertNotNull(spec3);
		assertTrue(spec3 instanceof PropertyStatement);
		assertTrue(UtilityFunctions.getStringProperty(spec3, "name").equals("const_tar_speed"));
		EObject spec3_expr = UtilityFunctions.getExpr(spec3);
		assertNotNull(spec3_expr);
		assertTrue(spec3_expr instanceof BinaryExpr);
		assertTrue(UtilityFunctions.getStringProperty(spec3_expr, "op").equals("->"));
		EObject spec3_expr_left = UtilityFunctions.getLeft(spec3_expr);
		assertNotNull(spec3_expr_left);
		assertTrue(spec3_expr_left instanceof BoolLitExpr);
		EObject spec3_expr_left_val = UtilityFunctions.getVal(spec3_expr_left);
		assertNotNull(spec3_expr_left_val);
		assertTrue(spec3_expr_left_val instanceof BooleanLiteral);
		assertTrue(Boolean.parseBoolean(UtilityFunctions.getStringProperty(spec3_expr_left_val, "value")));
		EObject spec3_expr_right = UtilityFunctions.getRight(spec3_expr);
		assertNotNull(spec3_expr_right);
		assertTrue(spec3_expr_right instanceof BinaryExpr);
		assertTrue(UtilityFunctions.getStringProperty(spec3_expr_right, "op").equals("="));
		EObject spec3_expr_right_left = UtilityFunctions.getLeft(spec3_expr_right);
		assertNotNull(spec3_expr_right_left);
		assertTrue(spec3_expr_right_left instanceof SelectionExpr);
		EObject spec3_expr_right_left_field = UtilityFunctions.getField(spec3_expr_right_left);
		assertNotNull(spec3_expr_right_left_field);
		assertTrue(spec3_expr_right_left_field instanceof DataSubcomponent);
		assertTrue(UtilityFunctions.getStringProperty(spec3_expr_right_left_field, "name").equals("val"));
		EObject spec3_expr_right_left_target = UtilityFunctions.getTarget(spec3_expr_right_left);
		assertNotNull(spec3_expr_right_left_target);
		assertTrue(spec3_expr_right_left_target instanceof NamedElmExpr);
		EObject spec3_expr_right_left_target_elm = UtilityFunctions.getElm(spec3_expr_right_left_target);
		assertNotNull(spec3_expr_right_left_target_elm);
		assertTrue(spec3_expr_right_left_target_elm instanceof DataPort);
		assertTrue(UtilityFunctions.getStringProperty(spec3_expr_right_left_target_elm, "name").equals("Target_Speed"));
		assertTrue(Boolean.parseBoolean(UtilityFunctions.getStringProperty(spec3_expr_right_left_target_elm, "in")));
		assertFalse(Boolean.parseBoolean(UtilityFunctions.getStringProperty(spec3_expr_right_left_target_elm, "out")));
		EObject spec3_expr_right_right = UtilityFunctions.getRight(spec3_expr_right);
		assertNotNull(spec3_expr_right_right instanceof PreExpr);
		EObject spec3_expr_right_right_expr = UtilityFunctions.getExpr(spec3_expr_right_right);
		assertNotNull(spec3_expr_right_right_expr);
		assertTrue(spec3_expr_right_right_expr instanceof SelectionExpr);
		EObject spec3_expr_right_right_expr_field = UtilityFunctions.getField(spec3_expr_right_right_expr);
		assertNotNull(spec3_expr_right_right_expr_field);
		assertTrue(spec3_expr_right_right_expr_field instanceof DataSubcomponent);
		assertTrue(UtilityFunctions.getStringProperty(spec3_expr_right_right_expr_field, "name").equals("val"));
		EObject spec3_expr_right_right_expr_target = UtilityFunctions.getTarget(spec3_expr_right_right_expr);
		assertNotNull(spec3_expr_right_right_expr_target);
		assertTrue(spec3_expr_right_right_expr_target instanceof NamedElmExpr);
		EObject spec3_expr_right_right_expr_target_elm = UtilityFunctions.getElm(spec3_expr_right_right_expr_target);
		assertNotNull(spec3_expr_right_right_expr_target_elm);
		assertTrue(spec3_expr_right_right_expr_target_elm instanceof DataPort);
		assertTrue(UtilityFunctions.getStringProperty(spec3_expr_right_right_expr_target_elm, "name").equals("Target_Speed"));
		assertTrue(Boolean.parseBoolean(UtilityFunctions.getStringProperty(spec3_expr_right_right_expr_target_elm, "in")));
		assertFalse(Boolean.parseBoolean(UtilityFunctions.getStringProperty(spec3_expr_right_right_expr_target_elm, "out")));

		EObject spec4 = UtilityFunctions.getSpec(contract, 4);
		assertNotNull(spec4);
		assertTrue(spec4 instanceof GuaranteeStatement);
		assertTrue(UtilityFunctions.getStringProperty(spec4, "name").equals("G_car_1"));
		assertTrue(UtilityFunctions.getStringProperty(spec4, "str").equals("actual speed is less than constant target speed"));
		EObject spec4_expr = UtilityFunctions.getExpr(spec4);
		assertNotNull(spec4_expr);
		assertTrue(spec4_expr instanceof BinaryExpr);
		assertTrue(UtilityFunctions.getStringProperty(spec4_expr, "op").equals("=>"));
		EObject spec4_expr_left = UtilityFunctions.getLeft(spec4_expr);
		assertNotNull(spec4_expr_left);
		assertTrue(spec4_expr_left instanceof NamedElmExpr);
		EObject spec4_expr_left_elm = UtilityFunctions.getElm(spec4_expr_left);
		assertNotNull(spec4_expr_left_elm);
		assertTrue(spec4_expr_left_elm instanceof PropertyStatement);
		assertTrue(UtilityFunctions.getStringProperty(spec4_expr_left_elm, "name").equals("const_tar_speed"));
		EObject spec4_expr_left_elm_expr = UtilityFunctions.getExpr(spec4_expr_left_elm);
		assertNotNull(spec4_expr_left_elm_expr);
		assertTrue(spec4_expr_left_elm_expr instanceof BinaryExpr);
		assertTrue(UtilityFunctions.getStringProperty(spec4_expr_left_elm_expr, "op").equals("->"));
		EObject spec4_expr_left_elm_expr_left = UtilityFunctions.getLeft(spec4_expr_left_elm_expr);
		assertNotNull(spec4_expr_left_elm_expr_left);
		assertTrue(spec4_expr_left_elm_expr_left instanceof BoolLitExpr);
		EObject spec4_expr_left_elm_expr_left_val = UtilityFunctions.getVal(spec4_expr_left_elm_expr_left);
		assertNotNull(spec4_expr_left_elm_expr_left_val);
		assertTrue(spec4_expr_left_elm_expr_left_val instanceof BooleanLiteral);
		assertTrue(Boolean.parseBoolean(UtilityFunctions.getStringProperty(spec4_expr_left_elm_expr_left_val, "value")));
		EObject spec4_expr_left_elm_expr_right = UtilityFunctions.getRight(spec4_expr_left_elm_expr);
		assertNotNull(spec4_expr_left_elm_expr_right);
		assertTrue(spec4_expr_left_elm_expr_right instanceof BinaryExpr);
		assertTrue(UtilityFunctions.getStringProperty(spec4_expr_left_elm_expr_right, "op").equals("="));
		EObject spec4_expr_left_elm_expr_right_left = UtilityFunctions.getLeft(spec4_expr_left_elm_expr_right);
		assertNotNull(spec4_expr_left_elm_expr_right_left);
		assertTrue(spec4_expr_left_elm_expr_right_left instanceof SelectionExpr);
		EObject spec4_expr_left_elm_expr_right_left_field = UtilityFunctions.getField(spec4_expr_left_elm_expr_right_left);
		assertNotNull(spec4_expr_left_elm_expr_right_left_field);
		assertTrue(spec4_expr_left_elm_expr_right_left_field instanceof DataSubcomponent);
		assertTrue(UtilityFunctions.getStringProperty(spec4_expr_left_elm_expr_right_left_field, "name").equals("val"));
		EObject spec4_expr_left_elm_expr_right_left_target = UtilityFunctions.getTarget(spec4_expr_left_elm_expr_right_left);
		assertNotNull(spec4_expr_left_elm_expr_right_left_target);
		assertTrue(spec4_expr_left_elm_expr_right_left_target instanceof NamedElmExpr);
		EObject spec4_expr_left_elm_expr_right_left_target_elm = UtilityFunctions.getElm(spec4_expr_left_elm_expr_right_left_target);
		assertNotNull(spec4_expr_left_elm_expr_right_left_target_elm);
		assertTrue(spec4_expr_left_elm_expr_right_left_target_elm instanceof DataPort);
		assertTrue(UtilityFunctions.getStringProperty(spec4_expr_left_elm_expr_right_left_target_elm, "name").equals("Target_Speed"));
		assertTrue(Boolean.parseBoolean(UtilityFunctions.getStringProperty(spec4_expr_left_elm_expr_right_left_target_elm, "in")));
		assertFalse(Boolean.parseBoolean(UtilityFunctions.getStringProperty(spec4_expr_left_elm_expr_right_left_target_elm, "out")));
		EObject spec4_expr_left_elm_expr_right_right = UtilityFunctions.getRight(spec4_expr_left_elm_expr_right);
		assertNotNull(spec4_expr_left_elm_expr_right_right instanceof PreExpr);
		EObject spec4_expr_left_elm_expr_right_right_expr = UtilityFunctions.getExpr(spec4_expr_left_elm_expr_right_right);
		assertNotNull(spec4_expr_left_elm_expr_right_right_expr);
		assertTrue(spec4_expr_left_elm_expr_right_right_expr instanceof SelectionExpr);
		EObject spec4_expr_left_elm_expr_right_right_expr_field = UtilityFunctions.getField(spec4_expr_left_elm_expr_right_right_expr);
		assertNotNull(spec4_expr_left_elm_expr_right_right_expr_field);
		assertTrue(spec4_expr_left_elm_expr_right_right_expr_field instanceof DataSubcomponent);
		assertTrue(UtilityFunctions.getStringProperty(spec4_expr_left_elm_expr_right_right_expr_field, "name").equals("val"));
		EObject spec4_expr_left_elm_expr_right_right_expr_target = UtilityFunctions.getTarget(spec4_expr_left_elm_expr_right_right_expr);
		assertNotNull(spec4_expr_left_elm_expr_right_right_expr_target);
		assertTrue(spec4_expr_left_elm_expr_right_right_expr_target instanceof NamedElmExpr);
		EObject spec4_expr_left_elm_expr_right_right_expr_target_elm = UtilityFunctions.getElm(spec4_expr_left_elm_expr_right_right_expr_target);
		assertNotNull(spec4_expr_left_elm_expr_right_right_expr_target_elm);
		assertTrue(spec4_expr_left_elm_expr_right_right_expr_target_elm instanceof DataPort);
		assertTrue(UtilityFunctions.getStringProperty(spec4_expr_left_elm_expr_right_right_expr_target_elm, "name").equals("Target_Speed"));
		assertTrue(Boolean.parseBoolean(UtilityFunctions.getStringProperty(spec4_expr_left_elm_expr_right_right_expr_target_elm, "in")));
		assertFalse(Boolean.parseBoolean(UtilityFunctions.getStringProperty(spec4_expr_left_elm_expr_right_right_expr_target_elm, "out")));
		EObject spec4_expr_right = UtilityFunctions.getRight(spec4_expr);
		assertNotNull(spec4_expr_right);
		assertTrue(spec4_expr_right instanceof BinaryExpr);
		assertTrue(UtilityFunctions.getStringProperty(spec4_expr_right, "op").equals("<="));
		EObject spec4_expr_right_left = UtilityFunctions.getLeft(spec4_expr_right);
		assertNotNull(spec4_expr_right_left);
		assertTrue(spec4_expr_right_left instanceof SelectionExpr);
		EObject spec4_expr_right_left_field = UtilityFunctions.getField(spec4_expr_right_left);
		assertNotNull(spec4_expr_right_left_field);
		assertTrue(spec4_expr_right_left_field instanceof DataSubcomponent);
		assertTrue(UtilityFunctions.getStringProperty(spec4_expr_right_left_field, "name").equals("val"));
		EObject spec4_expr_right_left_target = UtilityFunctions.getTarget(spec4_expr_right_left);
		assertNotNull(spec4_expr_right_left_target);
		assertTrue(spec4_expr_right_left_target instanceof NamedElmExpr);
		EObject spec4_expr_right_left_target_elm = UtilityFunctions.getElm(spec4_expr_right_left_target);
		assertNotNull(spec4_expr_right_left_target_elm);
		assertTrue(spec4_expr_right_left_target_elm instanceof DataPort);
		assertTrue(UtilityFunctions.getStringProperty(spec4_expr_right_left_target_elm, "name").equals("Actual_Speed"));
		assertFalse(Boolean.parseBoolean(UtilityFunctions.getStringProperty(spec4_expr_right_left_target_elm, "in")));
		assertTrue(Boolean.parseBoolean(UtilityFunctions.getStringProperty(spec4_expr_right_left_target_elm, "out")));
		EObject spec4_expr_right_right = UtilityFunctions.getRight(spec4_expr_right);
		assertNotNull(spec4_expr_right_right);
		assertTrue(spec4_expr_right_right instanceof SelectionExpr);
		EObject spec4_expr_right_right_field = UtilityFunctions.getField(spec4_expr_right_right);
		assertNotNull(spec4_expr_right_right_field);
		assertTrue(spec4_expr_right_right_field instanceof DataSubcomponent);
		assertTrue(UtilityFunctions.getStringProperty(spec4_expr_right_right_field, "name").equals("val"));
		EObject spec4_expr_right_right_target = UtilityFunctions.getTarget(spec4_expr_right_right);
		assertNotNull(spec4_expr_right_right_target);
		assertTrue(spec4_expr_right_right_target instanceof NamedElmExpr);
		EObject spec4_expr_right_right_target_elm = UtilityFunctions.getElm(spec4_expr_right_right_target);
		assertNotNull(spec4_expr_right_right_target_elm);
		assertTrue(spec4_expr_right_right_target_elm instanceof DataPort);
		assertTrue(UtilityFunctions.getStringProperty(spec4_expr_right_right_target_elm, "name").equals("Target_Speed"));
		assertTrue(Boolean.parseBoolean(UtilityFunctions.getStringProperty(spec4_expr_right_right_target_elm, "in")));
		assertFalse(Boolean.parseBoolean(UtilityFunctions.getStringProperty(spec4_expr_right_right_target_elm, "out")));

		EObject spec5 = UtilityFunctions.getSpec(contract, 5);
		assertNotNull(spec5);
		assertTrue(spec5 instanceof GuaranteeStatement);
		assertTrue(UtilityFunctions.getStringProperty(spec5, "name").equals("G_car_2"));
		assertTrue(UtilityFunctions.getStringProperty(spec5, "str").equals("acceleration is limited"));
		EObject spec5_expr = UtilityFunctions.getExpr(spec5);
		assertNotNull(spec5_expr);
		assertTrue(spec5_expr instanceof BinaryExpr);
		assertTrue(UtilityFunctions.getStringProperty(spec5_expr, "op").equals("<"));
		EObject spec5_expr_left = UtilityFunctions.getLeft(spec5_expr);
		assertNotNull(spec5_expr_left);
		assertTrue(spec5_expr_left instanceof CallExpr);
		EObject spec5_expr_left_arg = UtilityFunctions.getArg(spec5_expr_left, 0);
		assertNotNull(spec5_expr_left_arg);
		assertTrue(spec5_expr_left_arg instanceof BinaryExpr);
		assertTrue(UtilityFunctions.getStringProperty(spec5_expr_left_arg, "op").equals("-"));
		EObject spec5_expr_left_arg_left = UtilityFunctions.getLeft(spec5_expr_left_arg);
		assertNotNull(spec5_expr_left_arg_left);
		assertTrue(spec5_expr_left_arg_left instanceof SelectionExpr);
		EObject spec5_expr_left_arg_left_field = UtilityFunctions.getField(spec5_expr_left_arg_left);
		assertNotNull(spec5_expr_left_arg_left_field);
		assertTrue(spec5_expr_left_arg_left_field instanceof DataSubcomponent);
		assertTrue(UtilityFunctions.getStringProperty(spec5_expr_left_arg_left_field, "name").equals("val"));
		EObject spec5_expr_left_arg_left_target = UtilityFunctions.getTarget(spec5_expr_left_arg_left);
		assertNotNull(spec5_expr_left_arg_left_target);
		assertTrue(spec5_expr_left_arg_left_target instanceof NamedElmExpr);
		EObject spec5_expr_left_arg_left_target_elm = UtilityFunctions.getElm(spec5_expr_left_arg_left_target);
		assertNotNull(spec5_expr_left_arg_left_target_elm);
		assertTrue(spec5_expr_left_arg_left_target_elm instanceof DataPort);
		assertTrue(UtilityFunctions.getStringProperty(spec5_expr_left_arg_left_target_elm, "name").equals("Actual_Speed"));
		assertFalse(Boolean.parseBoolean(UtilityFunctions.getStringProperty(spec5_expr_left_arg_left_target_elm, "in")));
		assertTrue(Boolean.parseBoolean(UtilityFunctions.getStringProperty(spec5_expr_left_arg_left_target_elm, "out")));
		EObject spec5_expr_left_arg_right = UtilityFunctions.getRight(spec5_expr_left_arg);
		assertNotNull(spec5_expr_left_arg_right);
		assertTrue(spec5_expr_left_arg_right instanceof PrevExpr);
		EObject spec5_expr_left_arg_right_delay = UtilityFunctions.getDelay(spec5_expr_left_arg_right);
		assertNotNull(spec5_expr_left_arg_right_delay);
		assertTrue(spec5_expr_left_arg_right_delay instanceof SelectionExpr);
		EObject spec5_expr_left_arg_right_delay_field = UtilityFunctions.getField(spec5_expr_left_arg_right_delay);
		assertNotNull(spec5_expr_left_arg_right_delay_field);
		assertTrue(spec5_expr_left_arg_right_delay_field instanceof DataSubcomponent);
		assertTrue(UtilityFunctions.getStringProperty(spec5_expr_left_arg_right_delay_field, "name").equals("val"));
		EObject spec5_expr_left_arg_right_delay_target = UtilityFunctions.getTarget(spec5_expr_left_arg_right_delay);
		assertNotNull(spec5_expr_left_arg_right_delay_target);
		assertTrue(spec5_expr_left_arg_right_delay_target instanceof NamedElmExpr);
		EObject spec5_expr_left_arg_right_delay_target_elm = UtilityFunctions.getElm(spec5_expr_left_arg_right_delay_target);
		assertNotNull(spec5_expr_left_arg_right_delay_target_elm);
		assertTrue(spec5_expr_left_arg_right_delay_target_elm instanceof DataPort);
		assertTrue(UtilityFunctions.getStringProperty(spec5_expr_left_arg_right_delay_target_elm, "name").equals("Actual_Speed"));
		assertFalse(Boolean.parseBoolean(UtilityFunctions.getStringProperty(spec5_expr_left_arg_right_delay_target_elm, "in")));
		assertTrue(Boolean.parseBoolean(UtilityFunctions.getStringProperty(spec5_expr_left_arg_right_delay_target_elm, "out")));
		EObject spec5_expr_left_arg_right_init = UtilityFunctions.getInit(spec5_expr_left_arg_right);
		assertNotNull(spec5_expr_left_arg_right_init);
		assertTrue(spec5_expr_left_arg_right_init instanceof RealLitExpr);
		assertTrue(Double.parseDouble(UtilityFunctions.getStringProperty(spec5_expr_left_arg_right_init, "val")) == 0.0);
		EObject spec5_expr_left_ref = UtilityFunctions.getRef(spec5_expr_left);
		assertNotNull(spec5_expr_left_ref);
		assertTrue(spec5_expr_left_ref instanceof DoubleDotRef);
		EObject spec5_expr_left_ref_elm = UtilityFunctions.getElm(spec5_expr_left_ref);
		assertNotNull(spec5_expr_left_ref_elm);
		assertTrue(spec5_expr_left_ref_elm instanceof NodeDef);
		assertTrue(UtilityFunctions.getStringProperty(spec5_expr_left_ref_elm, "name").equals("abs"));
		EObject spec5_expr_left_ref_elm_arg = UtilityFunctions.getArg(spec5_expr_left_ref_elm, 0);
		assertNotNull(spec5_expr_left_ref_elm_arg);
		assertTrue(UtilityFunctions.getStringProperty(spec5_expr_left_ref_elm_arg, "name").equals("inp"));
		EObject spec5_expr_left_ref_elm_arg_type = UtilityFunctions.getType(spec5_expr_left_ref_elm_arg);
		assertNotNull(spec5_expr_left_ref_elm_arg_type);
		assertTrue(spec5_expr_left_ref_elm_arg_type instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(spec5_expr_left_ref_elm_arg_type, "name").equals("real"));
		EObject spec5_expr_left_ref_elm_ret = UtilityFunctions.getRet(spec5_expr_left_ref_elm, 0);
		assertNotNull(spec5_expr_left_ref_elm_ret);
		assertTrue(UtilityFunctions.getStringProperty(spec5_expr_left_ref_elm_ret, "name").equals("outp"));
		EObject spec5_expr_left_ref_elm_ret_type = UtilityFunctions.getType(spec5_expr_left_ref_elm_ret);
		assertNotNull(spec5_expr_left_ref_elm_ret_type);
		assertTrue(spec5_expr_left_ref_elm_ret_type instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(spec5_expr_left_ref_elm_ret_type, "name").equals("real"));
		EObject spec5_expr_left_ref_elm_body = UtilityFunctions.getNodeBody(spec5_expr_left_ref_elm);
		assertNotNull(spec5_expr_left_ref_elm_body);
		assertTrue(spec5_expr_left_ref_elm_body instanceof NodeBodyExpr);
		EObject spec5_expr_left_ref_elm_stmt = UtilityFunctions.getStmt(spec5_expr_left_ref_elm_body, 0);
		assertNotNull(spec5_expr_left_ref_elm_stmt);
		assertTrue(spec5_expr_left_ref_elm_stmt instanceof NodeEq);
		EObject spec5_expr_left_ref_elm_lhs = spec5_expr_left_ref_elm_stmt.eCrossReferences().get(0);
		assertNotNull(spec5_expr_left_ref_elm_lhs);
		assertTrue(UtilityFunctions.getStringProperty(spec5_expr_left_ref_elm_lhs, "name").equals("outp"));
		EObject spec5_expr_left_ref_elm_lhs_type = UtilityFunctions.getType(spec5_expr_left_ref_elm_lhs);
		assertNotNull(spec5_expr_left_ref_elm_lhs_type);
		assertTrue(spec5_expr_left_ref_elm_lhs_type instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(spec5_expr_left_ref_elm_lhs_type, "name").equals("real"));
		EObject spec5_expr_left_ref_elm_expr = UtilityFunctions.getExpr(spec5_expr_left_ref_elm_stmt);
		assertNotNull(spec5_expr_left_ref_elm_expr);
		assertTrue(spec5_expr_left_ref_elm_expr instanceof IfThenElseExpr);
		EObject spec5_expr_left_ref_elm_expr_a = UtilityFunctions.getA(spec5_expr_left_ref_elm_expr);
		assertNotNull(spec5_expr_left_ref_elm_expr_a);
		assertTrue(spec5_expr_left_ref_elm_expr_a instanceof BinaryExpr);
		assertTrue(UtilityFunctions.getStringProperty(spec5_expr_left_ref_elm_expr_a, "op").equals("<"));
		EObject spec5_expr_left_ref_elm_expr_a_left = UtilityFunctions.getLeft(spec5_expr_left_ref_elm_expr_a);
		assertNotNull(spec5_expr_left_ref_elm_expr_a_left);
		assertTrue(spec5_expr_left_ref_elm_expr_a_left instanceof NamedElmExpr);
		EObject spec5_expr_left_ref_elm_expr_a_left_elm = UtilityFunctions.getElm(spec5_expr_left_ref_elm_expr_a_left);
		assertNotNull(spec5_expr_left_ref_elm_expr_a_left_elm);
		assertTrue(UtilityFunctions.getStringProperty(spec5_expr_left_ref_elm_expr_a_left_elm, "name").equals("inp"));
		EObject spec5_expr_left_ref_elm_expr_a_left_elm_type = UtilityFunctions.getType(spec5_expr_left_ref_elm_expr_a_left_elm);
		assertNotNull(spec5_expr_left_ref_elm_expr_a_left_elm_type);
		assertTrue(spec5_expr_left_ref_elm_expr_a_left_elm_type instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(spec5_expr_left_ref_elm_expr_a_left_elm_type, "name").equals("real"));
		EObject spec5_expr_left_ref_elm_expr_b = UtilityFunctions.getB(spec5_expr_left_ref_elm_expr);
		assertNotNull(spec5_expr_left_ref_elm_expr_b);
		assertTrue(spec5_expr_left_ref_elm_expr_b instanceof UnaryExpr);
		assertTrue(UtilityFunctions.getStringProperty(spec5_expr_left_ref_elm_expr_b, "op").equals("-"));
		EObject spec5_expr_left_ref_elm_expr_b_expr = UtilityFunctions.getExpr(spec5_expr_left_ref_elm_expr_b);
		assertNotNull(spec5_expr_left_ref_elm_expr_b_expr);
		assertTrue(spec5_expr_left_ref_elm_expr_b_expr instanceof NamedElmExpr);
		EObject spec5_expr_left_ref_elm_expr_b_expr_elm = UtilityFunctions.getElm(spec5_expr_left_ref_elm_expr_b_expr);
		assertNotNull(spec5_expr_left_ref_elm_expr_b_expr_elm);
		assertTrue(UtilityFunctions.getStringProperty(spec5_expr_left_ref_elm_expr_b_expr_elm, "name").equals("inp"));
		EObject spec5_expr_left_ref_elm_expr_b_expr_elm_type = UtilityFunctions.getType(spec5_expr_left_ref_elm_expr_b_expr_elm);
		assertNotNull(spec5_expr_left_ref_elm_expr_b_expr_elm_type);
		assertTrue(spec5_expr_left_ref_elm_expr_b_expr_elm_type instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(spec5_expr_left_ref_elm_expr_b_expr_elm_type, "name").equals("real"));
		EObject spec5_expr_left_ref_elm_expr_c = UtilityFunctions.getC(spec5_expr_left_ref_elm_expr);
		assertNotNull(spec5_expr_left_ref_elm_expr_c);
		assertTrue(spec5_expr_left_ref_elm_expr_c instanceof NamedElmExpr);
		EObject spec5_expr_left_ref_elm_expr_c_elm = UtilityFunctions.getElm(spec5_expr_left_ref_elm_expr_c);
		assertNotNull(spec5_expr_left_ref_elm_expr_c_elm);
		assertTrue(UtilityFunctions.getStringProperty(spec5_expr_left_ref_elm_expr_c_elm, "name").equals("inp"));
		EObject spec5_expr_left_ref_elm_expr_c_elm_type = UtilityFunctions.getType(spec5_expr_left_ref_elm_expr_c_elm);
		assertNotNull(spec5_expr_left_ref_elm_expr_c_elm_type);
		assertTrue(spec5_expr_left_ref_elm_expr_c_elm_type instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(spec5_expr_left_ref_elm_expr_c_elm_type, "name").equals("real"));
		EObject spec5_expr_right = UtilityFunctions.getRight(spec5_expr);
		assertNotNull(spec5_expr_right);
		assertTrue(spec5_expr_right instanceof NamedElmExpr);
		EObject spec5_expr_right_elm = UtilityFunctions.getElm(spec5_expr_right);
		assertNotNull(spec5_expr_right_elm);
		assertTrue(spec5_expr_right_elm instanceof ConstStatement);
		assertTrue(UtilityFunctions.getStringProperty(spec5_expr_right_elm, "name").equals("max_accel"));
		EObject spec5_expr_right_elm_type = UtilityFunctions.getType(spec5_expr_right_elm);
		assertNotNull(spec5_expr_right_elm_type);
		assertTrue(spec5_expr_right_elm_type instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(spec5_expr_right_elm_type, "name").equals("real"));
		EObject spec5_expr_right_elm_expr = UtilityFunctions.getExpr(spec5_expr_right_elm);
		assertNotNull(spec5_expr_right_elm_expr);
		assertTrue(spec5_expr_right_elm_expr instanceof RealLitExpr);
		assertTrue(Double.parseDouble(UtilityFunctions.getStringProperty(spec5_expr_right_elm_expr, "val")) == 40.0);
	}
}
