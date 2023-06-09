package com.rockwellcollins.atc.agree.tests;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.testing.InjectWith;
import org.eclipse.xtext.testing.XtextRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.osate.testsupport.TestHelper;

import com.google.inject.Inject;
import com.itemis.xtext.testing.FluentIssueCollection;
import com.itemis.xtext.testing.XtextTest;
import com.rockwellcollins.atc.agree.agree.AgreeContract;
import com.rockwellcollins.atc.agree.agree.AgreeLibrary;
import com.rockwellcollins.atc.agree.agree.AgreePackage;
import com.rockwellcollins.atc.agree.agree.AgreeSubclause;
import com.rockwellcollins.atc.agree.agree.ConstStatement;
import com.rockwellcollins.atc.agree.agree.IntLitExpr;
import com.rockwellcollins.atc.agree.agree.PrimType;

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
				+ "	system sys\r\n"
				+ "		annex agree{**\r\n"
				+ "		**};\r\n"
				+ "	end sys;\r\n"
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
	public void testConstStatement() throws Exception {
		String test = "package TestPackage\r\n"
					  + "public\r\n"
					  + "	system sys\r\n"
					  + "		annex agree{**\r\n"
					  + "			const x : int = 2;\r\n"
					  + "		**};\r\n"
					  + "	end sys;\r\n"
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
		assertTrue(UtilityFunctions.getStringProperty(spec, "name").equals("x"));
		EObject spec_type = UtilityFunctions.getType(spec);
		assertNotNull(spec_type);
		assertTrue(spec_type instanceof PrimType);
		assertTrue(UtilityFunctions.getStringProperty(spec_type, "name").equals("int"));
		EObject expr = UtilityFunctions.getExpr(spec);
		assertNotNull(expr);
		assertTrue(expr instanceof IntLitExpr);
		assertTrue(Integer.parseInt(UtilityFunctions.getStringProperty(expr, "val")) == 2);
	}
}