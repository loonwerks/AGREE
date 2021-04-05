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


public class AgreeTypeWrapper {
//	protected TypeDef type;
//
//	public AgreeTypeWrapper(TypeDef type) {
//		this.type = Objects.requireNonNull(type);
//	}
//
//	public TypeDef getTypeDef() {
//		return type;
//	}
//
//	@Override
//	public int hashCode() {
//
//		if (type instanceof AgreeTypeSystem.Prim) {
//			return Objects.hash(type);
//		} else if (type instanceof AgreeTypeSystem.RangeIntTypeDef) {
//			int hash = Objects.hash(((AgreeTypeSystem.RangeIntTypeDef) type).name,
//					((AgreeTypeSystem.RangeIntTypeDef) type).low,
//					((AgreeTypeSystem.RangeIntTypeDef) type).high);
//			return hash;
//		} else if (type instanceof AgreeTypeSystem.RangeRealTypeDef) {
//			return Objects.hash(((AgreeTypeSystem.RangeRealTypeDef) type).name,
//					((AgreeTypeSystem.RangeRealTypeDef) type).low,
//					((AgreeTypeSystem.RangeRealTypeDef) type).high);
//
//		} else if (type instanceof ArrayTypeDef) {
//			ArrayTypeDef a = (ArrayTypeDef) type;
//			return Objects.hash(((ArrayTypeDef) type).name, ((ArrayTypeDef) type).baseType,
//					((ArrayTypeDef) type).dimension);
//		} else if (type instanceof RecordTypeDef) {
//			return Objects.hash(((RecordTypeDef) type).name, ((RecordTypeDef) type).fields);
//		} else if (type instanceof EnumTypeDef) {
//			return Objects.hash(((EnumTypeDef) type).name, ((EnumTypeDef) type).values);
//
//		}
//
//		throw new AgreeException("ERROR: unhandled type in hashCode");
//	}
//
//	@Override
//	public boolean equals(Object obj) {
//		if (!(obj instanceof AgreeTypeWrapper)) {
//			return false;
//		}
//
//		AgreeTypeWrapper other = (AgreeTypeWrapper) obj;
//		if (!(Objects.equals(type.getClass(), other.type.getClass()))) {
//			return false;
//		}
//
//		return AgreeTypeSystem.typesEqual(type, other.type);
//	}
//
//

}
