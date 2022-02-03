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
package com.rockwellcollins.atc.agree.analysis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.rockwellcollins.atc.agree.AgreeTypeSystem;

import jkind.lustre.ArrayType;
import jkind.lustre.EnumType;
import jkind.lustre.NamedType;
import jkind.lustre.RecordType;
import jkind.lustre.Type;

public class TypeTable {


	private final Map<String, Type> typeNameToLustreType;

	public TypeTable() {
		typeNameToLustreType = new HashMap<>();
	}


	public Type updateLustreTypeMap(AgreeTypeSystem.TypeDef agreeType) {
		Type lustreType = typeNameToLustreType.get(AgreeTypeSystem.nameOfTypeDef(agreeType));
		if (lustreType == null) {
			lustreType = getLustreType(agreeType);
			if (lustreType != null) {
				typeNameToLustreType.put(AgreeTypeSystem.nameOfTypeDef(agreeType), lustreType);
			}
		}
		return lustreType;
	}

	public Type getLustreType(AgreeTypeSystem.TypeDef agreeType) {

		if (agreeType == AgreeTypeSystem.Prim.IntTypeDef) {
			return NamedType.INT;

		} else if (agreeType == AgreeTypeSystem.Prim.RealTypeDef) {
			return NamedType.REAL;

		} else if (agreeType == AgreeTypeSystem.Prim.BoolTypeDef) {
			return NamedType.BOOL;

		} else if (agreeType instanceof AgreeTypeSystem.RangeIntTypeDef) {
			return NamedType.INT;

		} else if (agreeType instanceof AgreeTypeSystem.RangeRealTypeDef) {
			return NamedType.REAL;

		} else if (agreeType instanceof AgreeTypeSystem.RecordTypeDef) {
			String name = ((AgreeTypeSystem.RecordTypeDef) agreeType).name.replace("::", "__").replace(".", "__");
			Map<String, AgreeTypeSystem.TypeDef> agreeFields = ((AgreeTypeSystem.RecordTypeDef) agreeType).fields;

			Map<String, Type> lustreFields = new HashMap<>();
			for (Entry<String, AgreeTypeSystem.TypeDef> entry : agreeFields.entrySet()) {
				String key = entry.getKey();
				Type lt = updateLustreTypeMap(entry.getValue());
				if (lt != null) {
					lustreFields.put(key, lt);
				}
			}
			RecordType lustreRecType = new RecordType(name, lustreFields);
			return lustreRecType;

		} else if (agreeType instanceof AgreeTypeSystem.EnumTypeDef) {
			String name = ((AgreeTypeSystem.EnumTypeDef) agreeType).name.replace("::", "__").replace(".", "__");
			List<String> enumValues = new ArrayList<String>();
			for (String raw : ((AgreeTypeSystem.EnumTypeDef) agreeType).values) {
				String enumValue = raw.replace("::", "__").replace(".", "__");
				enumValues.add(enumValue);
			}
			EnumType lustreEnumType = new EnumType(name, enumValues);
			return lustreEnumType;

		} else if (agreeType instanceof AgreeTypeSystem.ArrayTypeDef) {
			AgreeTypeSystem.TypeDef agreeBaseType = ((AgreeTypeSystem.ArrayTypeDef) agreeType).stemType;
			int dimension = ((AgreeTypeSystem.ArrayTypeDef) agreeType).size;

			Type lustreBaseType = updateLustreTypeMap(agreeBaseType);
			if (lustreBaseType != null) {
				ArrayType lustreArrayType = new ArrayType(lustreBaseType, dimension);
				return lustreArrayType;
			}
		}

		// Jkind does not reason over this.
		return null;


	}

	public List<Type> getLustreTypes() {
		List<Type> typeList = new ArrayList<>(typeNameToLustreType.values());
		typeList.removeIf(type -> ((type instanceof NamedType) && ((NamedType) type).isBuiltin())
				|| (type instanceof jkind.lustre.ArrayType));
		return new ArrayList<>(typeList);
	}

}
