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
	@SuppressWarnings("unchecked")
	public static String getStringProperty(EObject eObject, String property) {
		EStructuralFeature nameFeature = eObject.eClass().getEStructuralFeature(property);
		if(nameFeature instanceof EAttribute && !nameFeature.isMany()) {
			EAttribute nameAttribute = (EAttribute)nameFeature;
			Object nameValue = eObject.eGet(nameFeature);
			return EcoreUtil.convertToString(nameAttribute.getEAttributeType(), nameValue);
		}
		else if (nameFeature instanceof EAttribute && nameFeature.isMany()) {
			EAttribute nameAttribute = (EAttribute) nameFeature;
			EList<EObject> nameList = (EList<EObject>) eObject.eGet(nameFeature);
			return EcoreUtil.convertToString(nameAttribute.getEAttributeType(), nameList.get(0));
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
	 * Takes an EMF EObject and returns the lhs if it exists
	 *
	 * @param eObject the EObject to find the lhs in
	 * @return the lhs
	 */
	public static EObject getLhs(EObject eObject) {
		return getSubclassBasedOnClassName(eObject, "lhs");
	}

	/**
	 * Takes an EMF EObject and returns the rhs if it exists
	 *
	 * @param eObject the EObject to find the rhs in
	 * @return the rhs
	 */
	public static EObject getRhs(EObject eObject) {
		return getSubclassBasedOnClassName(eObject, "rhs");
	}

	/**
	 * Takes an EMF EObject and a lhs arg index and returns the
	 * class object if it exists
	 *
	 * @param eObject the EObject to find the lhs arg in
	 * @param index the index of the lhs arg
	 * @return the lhs arg at index
	 */
	public static EObject getLhs(EObject eObject, int index) {
		List<EObject> objects = eObject.eContents();
		List<EObject> args = new ArrayList<EObject>();
		for (EObject e : objects) {
			EStructuralFeature container_feature = e.eContainingFeature();
			if (getStringProperty(container_feature, "name").equals("lhs")) {
				args.add(e);
			}
		}
		if (index < args.size()) {
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
	 * Takes an EMF EObject and returns the recordType if it exists
	 *
	 * @param eObject the EObject to find the recordType in
	 * @return the recordType
	 */
	public static EObject getRecordType(EObject eObject) {
		return getSubclassBasedOnClassName(eObject, "recordType");
	}

	/**
	 * Takes an EMF EObject and returns the deviceSubcomponentType if it exists
	 *
	 * @param eObject the EObject to find the deviceSubcomponentType in
	 * @return the deviceSubcomponentType
	 */
	public static EObject getDeviceSubcomponentType(EObject eObject) {
		return getSubclassBasedOnClassName(eObject, "deviceSubcomponentType");
	}

	/**
	 * Takes an EMF EObject and returns the enumType if it exists
	 *
	 * @param eObject the EObject to find the enumType in
	 * @return the enumType
	 */
	public static EObject getEnumType(EObject eObject) {
		return getSubclassBasedOnClassName(eObject, "enumType");
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
	 * Takes an EMF EObject and returns the val if it exists
	 *
	 * @param eObject the EObject to find the val in
	 * @return the val
	 */
	public static EObject getVal(EObject eObject) {
		return getSubclassBasedOnClassName(eObject, "val");
	}

	/**
	 * Takes an EMF EObject and returns the id if it exists
	 *
	 * @param eObject the EObject to find the id in
	 * @return the id
	 */
	public static EObject getID(EObject eObject) {
		return getSubclassBasedOnClassName(eObject, "id");
	}

	/**
	 * Takes an EMF EObject and returns the conn if it exists
	 *
	 * @param eObject the EObject to find the conn in
	 * @return the conn
	 */
	public static EObject getConn(EObject eObject) {
		return getSubclassBasedOnClassName(eObject, "conn");
	}

	/**
	 * Takes an EMF EObject and returns the source if it exists
	 *
	 * @param eObject the EObject to find the source in
	 * @return the source
	 */
	public static EObject getSource(EObject eObject) {
		return getSubclassBasedOnClassName(eObject, "source");
	}

	/**
	 * Takes an EMF EObject and returns the destination if it exists
	 *
	 * @param eObject the EObject to find the destination in
	 * @return the destination
	 */
	public static EObject getDestination(EObject eObject) {
		return getSubclassBasedOnClassName(eObject, "destination");
	}

	/**
	 * Takes an EMF EObject and returns the context if it exists
	 *
	 * @param eObject the EObject to find the context in
	 * @return the context
	 */
	public static EObject getContext(EObject eObject) {
		return getSubclassBasedOnClassName(eObject, "context");
	}

	/**
	 * Takes an EMF EObject and returns the connectionEnd if it exists
	 *
	 * @param eObject the EObject to find the connectionEnd in
	 * @return the connectionEnd
	 */
	public static EObject getConnectionEnd(EObject eObject) {
		return getSubclassBasedOnClassName(eObject, "connectionEnd");
	}

	/**
	 * Takes an EMF EObject and an arg index and returns the
	 * class object if it exists
	 *
	 * @param eObject the EObject to find the arg in
	 * @param index the index of the arg
	 * @return the arg at index
	 */
	public static EObject getArg(EObject eObject, int index) {
		List<EObject> objects = eObject.eContents();
		List<EObject> args = new ArrayList<EObject>();
		for (EObject e : objects) {
			EStructuralFeature container_feature = e.eContainingFeature();
			if (getStringProperty(container_feature, "name").equals("args")) {
				args.add(e);
			}
		}
		List<EObject> references = eObject.eCrossReferences();
		for (EObject e : references) {
			EStructuralFeature container_feature = e.eContainingFeature();
			if (getStringProperty(container_feature, "name").equals("args")) {
				args.add(e);
			}
		}
		if (index < args.size()) {
			return args.get(index);
		} else {
			return null;
		}
	}

	/**
	 * Takes an EMF EObject and an ret index and returns the
	 * class object if it exists
	 *
	 * @param eObject the EObject to find the ret in
	 * @param index the index of the ret
	 * @return the ret at index
	 */
	public static EObject getRet(EObject eObject, int index) {
		List<EObject> objects = eObject.eContents();
		List<EObject> args = new ArrayList<EObject>();
		for (EObject e : objects) {
			EStructuralFeature container_feature = e.eContainingFeature();
			if (getStringProperty(container_feature, "name").equals("rets")) {
				args.add(e);
			}
		}
		if (index < args.size()) {
			return args.get(index);
		} else {
			return null;
		}
	}

	/**
	 * Takes an EMF EObject and returns the nodeBody if it exists
	 *
	 * @param eObject the EObject to find the nodeBody in
	 * @return the nodeBody
	 */
	public static EObject getNodeBody(EObject eObject) {
		return getSubclassBasedOnClassName(eObject, "nodeBody");
	}

	/**
	 * Takes an EMF EObject and an stmt index and returns the
	 * class object if it exists
	 *
	 * @param eObject the EObject to find the stmt in
	 * @param index the index of the stmt
	 * @return the stmt at index
	 */
	public static EObject getStmt(EObject eObject, int index) {
		List<EObject> objects = eObject.eContents();
		List<EObject> args = new ArrayList<EObject>();
		for (EObject e : objects) {
			EStructuralFeature container_feature = e.eContainingFeature();
			if (getStringProperty(container_feature, "name").equals("stmts")) {
				args.add(e);
			}
		}
		if (index < args.size()) {
			return args.get(index);
		} else {
			return null;
		}
	}

	/**
	 * Takes an EMF EObject and returns the left property if it exists
	 *
	 * @param eObject the EObject to find the left property in
	 * @return the left property
	 */
	public static EObject getLeft(EObject eObject) {
		return getSubclassBasedOnClassName(eObject, "left");
	}

	/**
	 * Takes an EMF EObject and returns the right property if it exists
	 *
	 * @param eObject the EObject to find the right property in
	 * @return the right property
	 */
	public static EObject getRight(EObject eObject) {
		return getSubclassBasedOnClassName(eObject, "right");
	}

	/**
	 * Takes an EMF EObject and returns the 'a' property if it exists
	 *
	 * @param eObject the EObject to find the 'a' property in
	 * @return the 'a' property
	 */
	public static EObject getA(EObject eObject) {
		return getSubclassBasedOnClassName(eObject, "a");
	}

	/**
	 * Takes an EMF EObject and returns the 'a' property if it exists
	 *
	 * @param eObject the EObject to find the 'a' property in
	 * @return the 'a' property
	 */
	public static EObject getB(EObject eObject) {
		return getSubclassBasedOnClassName(eObject, "b");
	}

	/**
	 * Takes an EMF EObject and returns the 'a' property if it exists
	 *
	 * @param eObject the EObject to find the 'a' property in
	 * @return the 'a' property
	 */
	public static EObject getC(EObject eObject) {
		return getSubclassBasedOnClassName(eObject, "c");
	}

	/**
	 * Takes an EMF EObject and returns the interval if it exists
	 *
	 * @param eObject the EObject to find the interval in
	 * @return the interval
	 */
	public static EObject getInterval(EObject eObject) {
		return getSubclassBasedOnClassName(eObject, "interval");
	}

	/**
	 * Takes an EMF EObject and returns the conditionInterval if it exists
	 *
	 * @param eObject the EObject to find the conditionInterval in
	 * @return the conditionInterval
	 */
	public static EObject getConditionInterval(EObject eObject) {
		return getSubclassBasedOnClassName(eObject, "conditionInterval");
	}

	/**
	 * Takes an EMF EObject and returns the eventInterval if it exists
	 *
	 * @param eObject the EObject to find the eventInterval in
	 * @return the eventInterval
	 */
	public static EObject getEventInterval(EObject eObject) {
		return getSubclassBasedOnClassName(eObject, "eventInterval");
	}

	/**
	 * Takes an EMF EObject and returns the low property if it exists
	 *
	 * @param eObject the EObject to find the low property in
	 * @return the low property
	 */
	public static EObject getLow(EObject eObject) {
		return getSubclassBasedOnClassName(eObject, "low");
	}

	/**
	 * Takes an EMF EObject and returns the high property if it exists
	 *
	 * @param eObject the EObject to find the high property in
	 * @return the high property
	 */
	public static EObject getHigh(EObject eObject) {
		return getSubclassBasedOnClassName(eObject, "high");
	}

	/**
	 * Takes an EMF EObject and an interval index and returns the
	 * class object if it exists
	 *
	 * @param eObject the EObject to find the interval in
	 * @param index the index of the interval
	 * @return the interval at index
	 */
	public static EObject getInterval(EObject eObject, int index) {
		List<EObject> objects = eObject.eContents();
		List<EObject> args = new ArrayList<EObject>();
		for (EObject e : objects) {
			EStructuralFeature container_feature = e.eContainingFeature();
			if (getStringProperty(container_feature, "name").equals("intervals")) {
				args.add(e);
			}
		}
		if (index < args.size()) {
			return args.get(index);
		} else {
			return null;
		}
	}

	/**
	 * Takes an EMF EObject and returns the precision property if it exists
	 *
	 * @param eObject the EObject to find the precision property in
	 * @return the precision property
	 */
	public static EObject getPrecision(EObject eObject) {
		return getSubclassBasedOnClassName(eObject, "precision");
	}

	/**
	 * Takes an EMF EObject and returns the start property if it exists
	 *
	 * @param eObject the EObject to find the start property in
	 * @return the start property
	 */
	public static EObject getStart(EObject eObject) {
		return getSubclassBasedOnClassName(eObject, "start");
	}

	/**
	 * Takes an EMF EObject and returns the end property if it exists
	 *
	 * @param eObject the EObject to find the end property in
	 * @return the end property
	 */
	public static EObject getEnd(EObject eObject) {
		return getSubclassBasedOnClassName(eObject, "end");
	}

	/**
	 * Takes an EMF EObject and returns the exprBody if it exists
	 *
	 * @param eObject the EObject to find the exprBody in
	 * @return the exprBody
	 */
	public static EObject getExprBody(EObject eObject) {
		return getSubclassBasedOnClassName(eObject, "exprBody");
	}

	/**
	 * Takes an EMF EObject and returns the elm property if it exists
	 *
	 * @param eObject the EObject to find the elm property in
	 * @return the elm
	 */
	public static EObject getElm(EObject eObject) {
		return getSubclassBasedOnClassName(eObject, "elm");
	}

	/**
	 * Takes an EMF EObject and returns the init property if it exists
	 *
	 * @param eObject the EObject to find the init property in
	 * @return the init property
	 */
	public static EObject getInit(EObject eObject) {
		return getSubclassBasedOnClassName(eObject, "init");
	}

	/**
	 * Takes an EMF EObject and returns the delay expression if it exists
	 *
	 * @param eObject the EObject to find the delay expression in
	 * @return the delay expression
	 */
	public static EObject getDelay(EObject eObject) {
		return getSubclassBasedOnClassName(eObject, "delay");
	}

	/**
	 * Takes an EMF EObject and returns the stem if it exists
	 *
	 * @param eObject the EObject to find the stem in
	 * @return the stem expression
	 */
	public static EObject getStem(EObject eObject) {
		return getSubclassBasedOnClassName(eObject, "stem");
	}

	/**
	 * Takes an EMF EObject and returns the index property if it exists
	 *
	 * @param eObject the EObject to find the index property in
	 * @return the index property
	 */
	public static EObject getIndex(EObject eObject) {
		return getSubclassBasedOnClassName(eObject, "index");
	}

	/**
	 * Takes an EMF EObject and an elem index and returns the
	 * class object if it exists
	 *
	 * @param eObject the EObject to find the elem in
	 * @param index the index of the elem
	 * @return the elem at index
	 */
	public static EObject getElem(EObject eObject, int index) {
		List<EObject> objects = eObject.eContents();
		List<EObject> args = new ArrayList<EObject>();
		for (EObject e : objects) {
			EStructuralFeature container_feature = e.eContainingFeature();
			if (getStringProperty(container_feature, "name").equals("elems")) {
				args.add(e);
			}
		}
		if (index < args.size()) {
			return args.get(index);
		} else {
			return null;
		}
	}

	/**
	 * Takes an EMF EObject and returns the array if it exists
	 *
	 * @param eObject the EObject to find the array in
	 * @return the array
	 */
	public static EObject getArray(EObject eObject) {
		return getSubclassBasedOnClassName(eObject, "array");
	}

	/**
	 * Takes an EMF EObject and an index number and returns the
	 * class object if it exists
	 *
	 * @param eObject the EObject to find the index in
	 * @param index the index of the index
	 * @return the index at index
	 */
	public static EObject getIndex(EObject eObject, int index) {
		List<EObject> objects = eObject.eContents();
		List<EObject> args = new ArrayList<EObject>();
		for (EObject e : objects) {
			EStructuralFeature container_feature = e.eContainingFeature();
			if (getStringProperty(container_feature, "name").equals("indices")) {
				args.add(e);
			}
		}
		if (index < args.size()) {
			return args.get(index);
		} else {
			return null;
		}
	}

	/**
	 * Takes an EMF EObject and a valueExpr index and returns the
	 * class object if it exists
	 *
	 * @param eObject the EObject to find the valueExpr in
	 * @param index the index of the valueExpr
	 * @return the valueExpr at index
	 */
	public static EObject getValueExpr(EObject eObject, int index) {
		List<EObject> objects = eObject.eContents();
		List<EObject> args = new ArrayList<EObject>();
		for (EObject e : objects) {
			EStructuralFeature container_feature = e.eContainingFeature();
			if (getStringProperty(container_feature, "name").equals("valueExprs")) {
				args.add(e);
			}
		}
		if (index < args.size()) {
			return args.get(index);
		} else {
			return null;
		}
	}

	/**
	 * Takes an EMF EObject and a argExpr index and returns the
	 * class object if it exists
	 *
	 * @param eObject the EObject to find the argExpr in
	 * @param index the index of the argExpr
	 * @return the argExpr at index
	 */
	public static EObject getArgExpr(EObject eObject, int index) {
		List<EObject> objects = eObject.eContents();
		List<EObject> args = new ArrayList<EObject>();
		for (EObject e : objects) {
			EStructuralFeature container_feature = e.eContainingFeature();
			if (getStringProperty(container_feature, "name").equals("argExpr")) {
				args.add(e);
			}
		}
		if (index < args.size()) {
			return args.get(index);
		} else {
			return null;
		}
	}

	/**
	 * Takes an EMF EObject and returns the key if it exists
	 *
	 * @param eObject the EObject to find the key in
	 * @return the key
	 */
	public static EObject getKey(EObject eObject) {
		return getSubclassBasedOnClassName(eObject, "key");
	}

	/**
	 * Takes an EMF EObject and returns the record if it exists
	 *
	 * @param eObject the EObject to find the record in
	 * @return the record
	 */
	public static EObject getRecord(EObject eObject) {
		return getSubclassBasedOnClassName(eObject, "record");
	}

	/**
	 * Takes an EMF EObject and returns the componentRef if it exists
	 *
	 * @param eObject the EObject to find the componentRef in
	 * @return the componentRef
	 */
	public static EObject getComponentRef(EObject eObject) {
		return getSubclassBasedOnClassName(eObject, "componentRef");
	}

	/**
	 * Takes an EMF EObject and returns the ref if it exists
	 *
	 * @param eObject the EObject to find the ref in
	 * @return the ref
	 */
	public static EObject getRef(EObject eObject) {
		return getSubclassBasedOnClassName(eObject, "ref");
	}

	/**
	 * Takes an EMF EObject and returns the prop if it exists
	 *
	 * @param eObject the EObject to find the prop in
	 * @return the prop
	 */
	public static EObject getProp(EObject eObject) {
		return getSubclassBasedOnClassName(eObject, "prop");
	}

	/**
	 * Takes an EMF EObject and returns the pattern if it exists
	 *
	 * @param eObject the EObject to find the pattern in
	 * @return the pattern
	 */
	public static EObject getPattern(EObject eObject) {
		return getSubclassBasedOnClassName(eObject, "pattern");
	}

	/**
	 * Takes an EMF EObject and returns the event if it exists
	 *
	 * @param eObject the EObject to find the event in
	 * @return the event
	 */
	public static EObject getEvent(EObject eObject) {
		return getSubclassBasedOnClassName(eObject, "event");
	}

	/**
	 * Takes an EMF EObject and returns the cause if it exists
	 *
	 * @param eObject the EObject to find the cause in
	 * @return the cause
	 */
	public static EObject getCause(EObject eObject) {
		return getSubclassBasedOnClassName(eObject, "cause");
	}

	/**
	 * Takes an EMF EObject and returns the condition if it exists
	 *
	 * @param eObject the EObject to find the condition in
	 * @return the condition
	 */
	public static EObject getCondition(EObject eObject) {
		return getSubclassBasedOnClassName(eObject, "condition");
	}

	/**
	 * Takes an EMF EObject and returns the effect if it exists
	 *
	 * @param eObject the EObject to find the effect in
	 * @return the effect
	 */
	public static EObject getEffect(EObject eObject) {
		return getSubclassBasedOnClassName(eObject, "effect");
	}

	/**
	 * Takes an EMF EObject and returns the jitter if it exists
	 *
	 * @param eObject the EObject to find the jitter in
	 * @return the jitter
	 */
	public static EObject getJitter(EObject eObject) {
		return getSubclassBasedOnClassName(eObject, "jitter");
	}

	/**
	 * Takes an EMF EObject and returns the period if it exists
	 *
	 * @param eObject the EObject to find the period in
	 * @return the period
	 */
	public static EObject getPeriod(EObject eObject) {
		return getSubclassBasedOnClassName(eObject, "period");
	}

	/**
	 * Takes an EMF EObject and returns the iat if it exists
	 *
	 * @param eObject the EObject to find the iat in
	 * @return the iat
	 */
	public static EObject getIAT(EObject eObject) {
		return getSubclassBasedOnClassName(eObject, "iat");
	}

	/**
	 * Takes an EMF EObject and returns the times if it exists
	 *
	 * @param eObject the EObject to find the times in
	 * @return the times
	 */
	public static EObject getTimes(EObject eObject) {
		return getSubclassBasedOnClassName(eObject, "times");
	}

	/**
	 * Takes an EMF EObject and returns the port if it exists
	 *
	 * @param eObject the EObject to find the port in
	 * @return the port
	 */
	public static EObject getPort(EObject eObject) {
		return getSubclassBasedOnClassName(eObject, "port");
	}

	/**
	 * Takes an EMF EObject and a enums index and returns the
	 * class object if it exists
	 *
	 * @param eObject the EObject to find the enum in
	 * @param index the index of the enum
	 * @return the enum at index
	 */
	public static EObject getEnum(EObject eObject, int index) {
		List<EObject> objects = eObject.eContents();
		List<EObject> args = new ArrayList<EObject>();
		for (EObject e : objects) {
			EStructuralFeature container_feature = e.eContainingFeature();
			if (getStringProperty(container_feature, "name").equals("enums")) {
				args.add(e);
			}
		}
		if (index < args.size()) {
			return args.get(index);
		} else {
			return null;
		}
	}

	/**
	 * Takes an EMF EObject and returns the target if it exists
	 *
	 * @param eObject the EObject to find the target in
	 * @return the target
	 */
	public static EObject getTarget(EObject eObject) {
		return getSubclassBasedOnClassName(eObject, "target");
	}

	/**
	 * Takes an EMF EObject and returns the field if it exists
	 *
	 * @param eObject the EObject to find the field in
	 * @return the target
	 */
	public static EObject getField(EObject eObject) {
		return getSubclassBasedOnClassName(eObject, "field");
	}

	/**
	 * Takes an EMF EObject and returns the binding if it exists
	 *
	 * @param eObject the EObject to find the binding in
	 * @return the binding
	 */
	public static EObject getBinding(EObject eObject) {
		return getSubclassBasedOnClassName(eObject, "binding");
	}

	/**
	 * Takes an EMF EObject and returns the accumulator if it exists
	 *
	 * @param eObject the EObject to find the accumulator in
	 * @return the accumulator
	 */
	public static EObject getAccumulator(EObject eObject) {
		return getSubclassBasedOnClassName(eObject, "accumulator");
	}

	/**
	 * Takes an EMF EObject and returns the initial if it exists
	 *
	 * @param eObject the EObject to find the initial in
	 * @return the initial
	 */
	public static EObject getInitial(EObject eObject) {
		return getSubclassBasedOnClassName(eObject, "initial");
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

	/**
	 * Takes an list of issues and returns the info message with the matching message
	 *
	 * @param issues the list of issues
	 * @param message the message of the info message
	 * @return the info message with the matching message
	 */
	public static Issue getInfo(List<Issue> issues, String message) {
		for (Issue i : issues) {
			if (i.getMessage().equals(message) && i.getSeverity() == Severity.INFO) {
				return i;
			}
		}
		return null;
	}
}
