package com.rockwellcollins.atc.agree.validation;

import java.util.Map;

import org.eclipse.emf.ecore.EObject;

public class AgreeNullValidator extends AbstractAgreeJavaValidator {

	@Override
	protected boolean isResponsible(Map<Object, Object> context, EObject eObject) {
		return false;
	}
}
