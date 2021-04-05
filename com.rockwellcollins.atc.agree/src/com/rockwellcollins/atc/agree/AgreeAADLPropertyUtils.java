/*
 * Copyright (c) 2021, Collins Aerospace.
 * Developed with the sponsorship of Defense Advanced Research Projects Agency (DARPA).
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this data,
 * including any software or models in source or binary form, as well as any drawings, specifications,
 * and documentation (collectively "the Data"), to deal in the Data without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Data, and to permit persons to whom the Data is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Data.
 *
 * THE DATA IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT
 * LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS, SPONSORS, DEVELOPERS, CONTRIBUTORS, OR COPYRIGHT HOLDERS BE LIABLE
 * FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE DATA OR THE USE OR OTHER DEALINGS IN THE DATA.
 */
package com.rockwellcollins.atc.agree;

import java.util.ArrayList;
import java.util.List;

import org.osate.aadl2.Aadl2Package;
import org.osate.aadl2.EnumerationLiteral;
import org.osate.aadl2.ListValue;
import org.osate.aadl2.NamedElement;
import org.osate.aadl2.Property;
import org.osate.aadl2.PropertyExpression;
import org.osate.aadl2.modelsupport.scoping.Aadl2GlobalScopeUtil;
import org.osate.xtext.aadl2.properties.util.PropertyUtils;

public class AgreeAADLPropertyUtils {

	public static String getPropertyEnumString(NamedElement namedEl, String property) {
		Property prop = Aadl2GlobalScopeUtil.get(namedEl, Aadl2Package.eINSTANCE.getProperty(),
				property);
		EnumerationLiteral lit = PropertyUtils.getEnumLiteral(namedEl, prop);
		return lit.getName();
	}

	public static List<PropertyExpression> getPropertyList(NamedElement namedEl, String property) {

		List<PropertyExpression> els = new ArrayList<>();
		Property prop = Aadl2GlobalScopeUtil.get(namedEl, Aadl2Package.eINSTANCE.getProperty(),
				property);
		ListValue listExpr = (ListValue) PropertyUtils.getSimplePropertyListValue(namedEl, prop);
		for (PropertyExpression propExpr : listExpr.getOwnedListElements()) {
			els.add(propExpr);
		}
		return els;
	}

}
