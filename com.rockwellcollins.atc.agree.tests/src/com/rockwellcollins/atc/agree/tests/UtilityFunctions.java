package com.rockwellcollins.atc.agree.tests;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.xtext.diagnostics.Severity;
import org.eclipse.xtext.validation.Issue;

public class UtilityFunctions {

	/**
	 * Takes an EMF EObject, finds its property, and returns the property's string value if it exists
	 *
	 * @param eObject the EObject to find the property of
	 * @parm property the property's name
	 * @return the name of the property
	 */
	public static String getStringProperty(EObject eObject, String property) {
		EStructuralFeature nameFeature = eObject.eClass().getEStructuralFeature(property);
		if(nameFeature instanceof EAttribute && !nameFeature.isMany()) {
			EAttribute nameAttribute = (EAttribute)nameFeature;
			Object nameValue = eObject.eGet(nameFeature);
			return EcoreUtil.convertToString(nameAttribute.getEAttributeType(), nameValue);
		}
		return "";
	}

	/**
	 * Takes an EMF EObject and returns the subclass given by class_name if it exists
	 *
	 * @param eObject the EObject to find the subclass in
	 * @param class_name the name of the subclass
	 * @return the subclass
	 */
	private static EObject getSubclassBasedOnClassName(EObject eObject, String class_name){
		EStructuralFeature feature = eObject.eClass().getEStructuralFeature(class_name);
		if(feature instanceof EReference && !feature.isMany()) {
			return (EObject)eObject.eGet(feature);
		}
		return null;
	}

	/**
	 * Takes an EMF EObject and a property name and returns the
	 * class object if it exists
	 *
	 * @param eObject the EObject to find the property in
	 * @param name the name of the property
	 * @return the property titled "property_name"
	 */
	private static EObject getSubclassBasedOnPropertyName(EObject eObject, String property_name){
		EList<EObject> contents = eObject.eContents();
		for(EObject e : contents) {
			if(getStringProperty(e, "name").equals(property_name)) {
				return e;
			}
		}
		return null;
	}

	/**
	 * Takes an EMF EObject and returns the ownedPublicSection if it exists
	 *
	 * @param eObject the EObject to find the ownedPublicSection in
	 * @return the ownedPublicSection
	 */
	public static EObject getownedPublicSection(EObject eObject){
		return getSubclassBasedOnClassName(eObject, "ownedPublicSection");
	}

	/**
	 * Takes an EMF EObject and a classifier name and returns the
	 * class object if it exists
	 *
	 * @param eObject the EObject to find the ownedClassifier in
	 * @param name the name of the classifier
	 * @return the ownedClassifier titled "name"
	 */
	public static EObject getownedClassifier(EObject eObject, String name){
		return getSubclassBasedOnPropertyName(eObject, name);
	}

	/**
	 * Takes an EMF EObject and a classifier name and returns the
	 * class object if it exists
	 *
	 * @param eObject the EObject to find the ownedClassifier in
	 * @param name the name of the classifier
	 * @return the ownedClassifier titled "name"
	 */
	public static EObject getAnnexLibrary(EObject eObject, String name){
		return getSubclassBasedOnPropertyName(eObject, name);
	}

	/**
	 * Takes an EMF EObject and an annex name and returns the
	 * class object if it exists
	 *
	 * @param eObject the EObject to find the annex in
	 * @param name the name of the annex
	 * @return the annex subclause titled "name"
	 */
	public static EObject getAnnexSubclause(EObject eObject, String name){
		return getSubclassBasedOnPropertyName(eObject, name);
	}

	/**
	 * Takes an EMF EObject and returns the parsedAnnexLibrary if it exists
	 *
	 * @param eObject the EObject to find the parsedAnnexLibrary in
	 * @return the parsedAnnexLibrary
	 */
	public static EObject getParsedAnnexLibrary(EObject eObject){
		return getSubclassBasedOnClassName(eObject, "parsedAnnexLibrary");
	}

	/**
	 * Takes an EMF EObject and returns the parsedAnnexSubclause if it exists
	 *
	 * @param eObject the EObject to find the parsedAnnexSubclause in
	 * @return the parsedAnnexSubclause
	 */
	public static EObject getParsedAnnexSubclause(EObject eObject){
		return getSubclassBasedOnClassName(eObject, "parsedAnnexSubclause");
	}

	/**
	 * Takes an EMF EObject and returns the contract if it exists
	 *
	 * @param eObject the EObject to find the contract in
	 * @return the contract
	 */
	public static EObject getContract(EObject eObject) {
		return getSubclassBasedOnClassName(eObject, "contract");
	}

	/**
	 * Takes an EMF EObject and a spec index and returns the
	 * class object if it exists
	 *
	 * @param eObject the EObject to find the spec in
	 * @param index the index of the spec
	 * @return the spec at index
	 */
	public static EObject getSpec(EObject eObject, int index) {
		List<EObject> objects = eObject.eContents();
		List<EObject> args = new ArrayList<EObject>();
		for(EObject e : objects) {
			EStructuralFeature container_feature = e.eContainingFeature();
			if (getStringProperty(container_feature, "name").equals("specs")) {
				args.add(e);
			}
		}
		if(index < args.size()) {
			return args.get(index);
		} else {
			return null;
		}
	}

	/**
	 * Takes an EMF Object and returns the type if it exists
	 *
	 * @param eObject the argument to find the type in
	 * @return the type
	 */
	public static EObject getType(EObject eObject){
		return getSubclassBasedOnClassName(eObject, "type");
	}

	/**
	 * Takes an EMF EObject and returns the expr if it exists
	 *
	 * @param eObject the EObject to find the expr in
	 * @return the expr
	 */
	public static EObject getExpr(EObject eObject) {
		return getSubclassBasedOnClassName(eObject, "expr");
	}

	/**
	 * Takes an list of issues and returns the error with the matching message
	 *
	 * @param issues the list of issues
	 * @param message the message of the error
	 * @return the error with the matching message
	 */
	public static Issue getError(List<Issue> issues, String message) {
		for(Issue i : issues) {
			if(i.getMessage().equals(message) && i.getSeverity()==Severity.ERROR) {
				return i;
			}
		}
		return null;
	}

	/**
	 * Takes an list of issues and returns the warning with the matching message
	 *
	 * @param issues the list of issues
	 * @param message the message of the warning
	 * @return the warning with the matching message
	 */
	public static Issue getWarning(List<Issue> issues, String message) {
		for(Issue i : issues) {
			if(i.getMessage().equals(message) && i.getSeverity()==Severity.WARNING) {
				return i;
			}
		}
		return null;
	}
}
