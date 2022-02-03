/*
 * Copyright (c) 2022, Collins Aerospace.
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
package com.rockwellcollins.atc.agree.analysis;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;

import com.rockwellcollins.atc.agree.analysis.ast.AgreeASTBuilder;
import com.rockwellcollins.atc.agree.analysis.translation.LustreAstBuilder;

import jkind.api.results.Renaming;
import jkind.results.InvalidProperty;
import jkind.results.Property;
import jkind.results.UnknownProperty;
import jkind.results.ValidProperty;

public class AgreeRenaming extends Renaming {

	/*
	 * explicitRenames:
	 * key: Lustre var
	 * value: AGREE var
	 * example: "accumulator__pressure_output" -> "accumulator.pressure_output"
	 * supportRenames:
	 * key: Lustre property
	 * value: AGREE property
	 * example: "__TOP_green_pump.GUARANTEE0" -> "green_pump.GUARANTEE0"
	 * supportRefStrings:
	 * key: AGREE child property
	 * value: String description
	 * example: "green_skid.GUARANTEE1" -> "Output always positive."
	 * refMap:
	 * key: AGREE child property string description
	 * value: Implementation
	 * example: "Output always positive" -> GuaranteeStatementImpl
	 */
	private Map<String, String> explicitRenames = new HashMap<>();
	private Map<String, String> supportRenames = new HashMap<>();
	private Map<String, String> supportRefStrings = new HashMap<>();
	private Map<String, EObject> refMap;
	private Set<String> invertedProperties = new HashSet<>();
	private Set<String> uninterpretedFnIONames = new HashSet<>();

	public AgreeRenaming() {
		refMap = new HashMap<>();
	}

	public String getLustreNameFromAgreeVar(String agreeVar) {
		for (Map.Entry<String, String> entry : explicitRenames.entrySet()) {
			if (agreeVar.equals(entry.getValue())) {
				return entry.getKey();
			}
		}
		return null;
	}

	public void addRenamings(AgreeRenaming renaming) {
		explicitRenames.putAll(renaming.explicitRenames);
		refMap.putAll(renaming.refMap);
	}

	public void addSupportRename(String from, String to) {
		supportRenames.put(from, to);
	}

	public void addSupportRefString(String from, String refStr) {
		supportRefStrings.put(renameIVC(from), refStr);
	}

	public void addUninterpretedFnIONames(String name) {
		uninterpretedFnIONames.add(name);
	}

	@Override
	public String renameIVC(String ivc) {
		return supportRenames.get(ivc);
	}

	public String getSupportRefString(String ivc) {
		return supportRefStrings.get(ivc);
	}

	public final Map<String, String> getSupportRefStrings() {
		return supportRefStrings;
	}

	public void addExplicitRename(String oldName, String newName) {
		explicitRenames.put(oldName, newName);
	}

	public void addToRefMap(String str, EObject ref) {
		if (str != null) {
			str = rename(str);
			if (str != null) {
				refMap.put(str, ref);
			}
		}
	}

	public Map<String, EObject> getRefMap() {
		return refMap;
	}

	/**
	 * @since 2.8
	 */
	public void addInvertedProperty(String propertyName) {
		invertedProperties.add(propertyName);
	}

	/**
	 * @since 2.8
	 */
	public boolean propertyIsInverted(String propertyName) {
		return invertedProperties.contains(propertyName);
	}

	/**
	 * @since 2.8
	 */
	public Set<String> getInvertedProperties() {
		return invertedProperties;
	}

	public String forceRename(String original) {

		// magic to remove the prefix
		String newName;
		newName = original.replaceAll("___Nod([^_]_?)*_", "");
		newName = newName.replace("~condact", "");
		newName = newName.replaceAll("~[0-9]*", "");
		// the following is special for kind 2 contracts
		newName = newName.replaceAll("guarantee\\[.*?\\]", "");
		newName = newName.replace("__", ".");

		return newName;

	}

	@Override
	public Property rename(Property property) {
		// another hack for kind2
		if (property.getName().matches("guarantee\\[.*?\\]")) {
			return renameKind2Prop(property);
			// return property;
		}
		return super.rename(property);
	}

	private Property renameKind2Prop(Property property) {
		if (property instanceof InvalidProperty) {
			InvalidProperty renamedInvalid = (InvalidProperty) property;
			return new InvalidProperty(renamedInvalid.getName(), renamedInvalid.getSource(),
					rename(renamedInvalid.getCounterexample()), renamedInvalid.getConflicts(),
					renamedInvalid.getRuntime(), renamedInvalid.getReport());
		} else if (property instanceof UnknownProperty) {
			UnknownProperty renamedUnknown = (UnknownProperty) property;
			UnknownProperty newProp = new UnknownProperty(renamedUnknown.getName(), renamedUnknown.getTrueFor(),
					rename(renamedUnknown.getInductiveCounterexample()), renamedUnknown.getRuntime());
			return newProp;
		}
		if (!(property instanceof ValidProperty)) {
			throw new AgreeException("Unexpected property type");
		}
		return property;
	}

	@Override
	public String rename(String original) {

		String newName = explicitRenames.get(original);
		if (newName != null) {
			return newName;
		}

		newName = forceRename(original);

		if (findBestReference(newName) == null) {
			if (original.equals("%REALIZABLE")) {
				return "Realizability Result";
			} else if (original.contains("__nodeLemma")) {
				return newName;
			} else if (newName.matches(".*\\[[0-9]*\\]")) {
				// kind2 hacks
				newName = explicitRenames.get(newName);
//               if(newName == null){
//                   return original;
//               }
				return newName;
			} else if (newName.matches("time")) {
				return "time";
			} else if (original.endsWith(AgreeASTBuilder.clockIDSuffix)) {
				return newName;
			} else if (original.endsWith(LustreAstBuilder.assumeHistSufix)) {
				return newName;
			} else if (matchUninterpretedFnIONames(newName)) {
				return newName;
			}
			return null;
		}

		return newName;

	}

	private boolean matchUninterpretedFnIONames(String name) {
		for (String uFnIOName : uninterpretedFnIONames) {
			if (name.equals(uFnIOName)) {
				return true;
			}
			// handle record type I/O
			String uFnIOString = uFnIOName + ".";
			if (name.startsWith(uFnIOString)) {
				return true;
			}
		}
		return false;
	}

	private EObject findBestReference(String refStr) {

		EObject ref = null;
		while (ref == null && refStr != null && !refStr.equals("")) {
			ref = refMap.get(refStr);
			int index = refStr.lastIndexOf(".");
			if (index == -1) {
				break;
			}
			refStr = refStr.substring(0, index);
		}

		return ref;
	}

}
