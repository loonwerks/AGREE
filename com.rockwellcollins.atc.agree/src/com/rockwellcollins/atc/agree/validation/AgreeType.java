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
package com.rockwellcollins.atc.agree.validation;

//public class AgreeType {
//
//	public static class Name extends AgreeType {
//
//		private String name;
//
//		public Name(String name) {
//			this.name = name;
//		}
//
//		@Override
//		public boolean equals(Object o) {
//			if (o instanceof Name) {
//				Name other = (Name) o;
//				return name.equals(other.name);
//			}
//			return false;
//		}
//
//		@Override
//		public int hashCode() {
//			return name.hashCode();
//		}
//
//		@Override
//		public String toString() {
//			return name;
//		}
//
//	}
//
//	public static class Array extends AgreeType {
//		private AgreeType stem;
//		private int size;
//
//		public Array(AgreeType stem, int size) {
//			this.stem = stem;
//			this.size = size;
//		}
//
//		public int getSize() {
//			return size;
//		}
//
//		@Override
//		public boolean equals(Object o) {
//			if (o instanceof AgreeType.Array) {
//				AgreeType.Array other = (AgreeType.Array) o;
//				return this.stem.equals(other.stem) && this.size == other.size;
//			}
//			return false;
//		}
//
//		@Override
//		public int hashCode() {
//			return this.toString().hashCode();
//		}
//
//		@Override
//		public String toString() {
//			return stem.toString() + "[" + size + "]";
//		}
//	}
//
//	public static final AgreeType BOOL = new Name("bool");
//	public static final AgreeType INT = new Name("int");
//	public static final AgreeType REAL = new Name("real");
//	public static final AgreeType ERROR = new Name("<error>");
//
//
//	public boolean isPrimitive() {
//		return equals(BOOL) || equals(INT) || equals(REAL);
//	}
//
//}
