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
package com.rockwellcollins.atc.agree.analysis.lustre.visitors;

import java.util.stream.Collectors;

import org.eclipse.emf.ecore.EObject;
import org.osate.aadl2.ComponentImplementation;
import org.osate.aadl2.ComponentType;
import org.osate.aadl2.DataPort;
import org.osate.aadl2.EventDataPort;
import org.osate.aadl2.EventPort;
import org.osate.aadl2.FeatureGroup;
import org.osate.aadl2.Property;
import org.osate.aadl2.SystemImplementation;
import org.osate.aadl2.instance.ComponentInstance;

import com.rockwellcollins.atc.agree.agree.Arg;
import com.rockwellcollins.atc.agree.agree.ArrayType;
import com.rockwellcollins.atc.agree.agree.AssertStatement;
import com.rockwellcollins.atc.agree.agree.AssumeStatement;
import com.rockwellcollins.atc.agree.agree.DoubleDotRef;
import com.rockwellcollins.atc.agree.agree.EqStatement;
import com.rockwellcollins.atc.agree.agree.GetPropertyExpr;
import com.rockwellcollins.atc.agree.agree.GuaranteeStatement;
import com.rockwellcollins.atc.agree.agree.InputStatement;
import com.rockwellcollins.atc.agree.agree.LemmaStatement;
import com.rockwellcollins.atc.agree.agree.PrimType;
import com.rockwellcollins.atc.agree.agree.PropertyStatement;
import com.rockwellcollins.atc.agree.agree.ReachableStatement;
import com.rockwellcollins.atc.agree.agree.Type;
import com.rockwellcollins.atc.agree.analysis.AgreeException;
import com.rockwellcollins.atc.agree.analysis.AgreeLayout;
import com.rockwellcollins.atc.agree.analysis.AgreeLayout.SigType;
import com.rockwellcollins.atc.agree.analysis.AgreeRenaming;
import com.rockwellcollins.atc.agree.analysis.ast.AgreeASTBuilder;
import com.rockwellcollins.atc.agree.analysis.ast.AgreeStatement;
import com.rockwellcollins.atc.agree.analysis.ast.AgreeVar;
import com.rockwellcollins.atc.agree.analysis.ast.visitors.AgreeInlineLatchedConnections;
import com.rockwellcollins.atc.agree.analysis.translation.LustreAstBuilder;

import jkind.lustre.Function;
import jkind.lustre.Node;
import jkind.lustre.Program;
import jkind.lustre.VarDecl;
import jkind.lustre.visitors.AstIterVisitor;

public class RenamingVisitor extends AstIterVisitor {

	private final AgreeRenaming renaming;
	private final ComponentInstance rootInstance;
	private final AgreeLayout layout;
	private final Program program;
	private boolean isMainNode;
	private String nodeName;
	private boolean isFunction = false;

	public static void addRenamings(Program program, AgreeRenaming renaming, ComponentInstance rootInstance,
			AgreeLayout layout) {
		RenamingVisitor visitor = new RenamingVisitor(program, renaming, rootInstance, layout);
		visitor.visit(program);
	}

	private RenamingVisitor(Program program, AgreeRenaming renaming, ComponentInstance rootInstance,
			AgreeLayout layout) {
		this.renaming = renaming;
		this.rootInstance = rootInstance;
		this.layout = layout;
		this.program = program;
	}

	@Override
	public Void visit(Program program) {
		super.visit(program);
		for (jkind.lustre.TypeDef typeDef : program.types) {
			if (typeDef.type instanceof jkind.lustre.EnumType) {
				jkind.lustre.EnumType enumType = (jkind.lustre.EnumType) typeDef.type;
				for (String enumerator : enumType.values) {
					renaming.addExplicitRename(enumerator, enumerator);
				}
			}
		}
		return null;
	}

	@Override
	public Void visit(Node node) {
		isMainNode = node.id.equals(program.main);
		nodeName = node.id;
		visitVarDecls(node.inputs);
		visitVarDecls(node.outputs);
		visitVarDecls(node.locals);
		return null;
	}

	@Override
	public Void visit(Function function) {
		isFunction = true;
		String functionName = function.id.replace("__", ".");
		renaming.addUninterpretedFnIONames(functionName);
		visitVarDecls(function.inputs);
		visitVarDecls(function.outputs);
		isFunction = false;
		return null;
	}

	@Override
	public Void visit(VarDecl e) {
		if (isFunction) {
			renaming.addUninterpretedFnIONames(e.id);
			return null;
		}
		if (e instanceof AgreeVar) {
			AgreeVar var = (AgreeVar) e;
			String category = getCategory(rootInstance, var);
			String refStr = getReferenceStr(var);

			if (isMainNode && var.reference != null) {
				// TODO: the means of detecting whether this is a consistency analysis is a hack. Fix it.
				if ((var.reference instanceof AssumeStatement
						|| (nodeName.contains("consistency") && var.reference instanceof GuaranteeStatement))
						&& category != null && category.equals("")) {
					renaming.addSupportRename(var.id, var.id);
					renaming.addSupportRefString(var.id, refStr);
					renaming.getRefMap().put(refStr, var.reference);
				} else {
					renaming.addExplicitRename(var.id, refStr);
					renaming.addToRefMap(var.id, var.reference);
				}
			} else if (var.reference instanceof GuaranteeStatement) {
				renaming.addSupportRename(nodeName + "." + var.id, category + "." + var.id);
				renaming.addSupportRefString(nodeName + "." + var.id, refStr);
				renaming.getRefMap().put(refStr, var.reference);
			} else {
				return null;
			}

			if (category != null && !layout.getCategories().contains(category)) {
				layout.addCategory(category);
			}
			layout.addElement(category, refStr, SigType.INPUT);

		}
		return null;
	}

	private String typeToString(Type ty) {
		if (ty instanceof PrimType) {
			PrimType primType = (PrimType) ty;
			String result = primType.getName();
			String lowLit = (primType.getLowNeg() != null) ? primType.getLowNeg() : "";
			String highLit = (primType.getHighNeg() != null) ? primType.getHighNeg() : "";
			if (primType.getRangeLow() != null) {
				result += " [" + lowLit + primType.getRangeLow() + ", " + highLit
						+ primType.getRangeHigh() + "]";
			}
			return result;
		} else if (ty instanceof ArrayType) {
			ArrayType arrayType = (ArrayType) ty;
			return typeToString(arrayType.getStem()) + "[" + arrayType.getSize() + "]";
		} else if (ty instanceof DoubleDotRef) {
			return ((DoubleDotRef) ty).getElm().getName();
		} else {
			throw new AgreeException("Unrecognized AGREE type " + ty.getClass().getName());
		}
	}

	private String argToString(Arg arg) {
		return arg.getName() + " : " + typeToString(arg.getType());
	}

	private String getReferenceStr(AgreeVar var) {

		String prefix = getCategory(rootInstance, var);
		if (prefix == null) {
			return null;
		}
		if (var.id.endsWith(AgreeASTBuilder.clockIDSuffix)) {
			return null;
		}

		String seperator = (prefix == "" ? "" : ".");
		EObject reference = var.reference;
		String suffix = "";

		if (var.id.endsWith(AgreeASTBuilder.eventSuffix + AgreeInlineLatchedConnections.LATCHED_SUFFIX)) {
			suffix = "._EVENT_._LATCHED_";
		} else if (var.id.endsWith(AgreeASTBuilder.eventSuffix)) {
			suffix = "._EVENT_";
		} else if (var.id.endsWith(AgreeInlineLatchedConnections.LATCHED_SUFFIX)) {
			suffix = "._LATCHED_";
		}

		if (reference instanceof GuaranteeStatement) {
			String id = ((GuaranteeStatement) reference).getName();
			if (id == null || id.isEmpty()) {
				id = "";
			} else {
				id = "[" + id + "] ";
			}
			return id + ((GuaranteeStatement) reference).getStr();
		} else if (reference instanceof AssumeStatement) {
			String id = ((AssumeStatement) reference).getName();
			if (id == null || id.isEmpty()) {
				id = "";
			} else {
				id = "[" + id + "] ";
			}
			return prefix + " assume: " + id + ((AssumeStatement) reference).getStr();
		} else if (reference instanceof LemmaStatement) {
			String id = ((LemmaStatement) reference).getName();
			if (id == null || id.isEmpty()) {
				id = "";
			} else {
				id = "[" + id + "] ";
			}
			return prefix + " lemma: " + id + ((LemmaStatement) reference).getStr();
		} else if (reference instanceof ReachableStatement) {
			renaming.addInvertedProperty(var.id);
			String id = ((ReachableStatement) reference).getName();
			if (id == null || id.isEmpty()) {
				id = "";
			} else {
				id = "[" + id + "] ";
			}
			return prefix + " reachable: " + id + ((ReachableStatement) reference).getStr();
		} else if (reference instanceof AssertStatement) {
			throw new AgreeException("We really didn't expect to see an assert statement here");
		} else if (reference instanceof Arg) {
			return prefix + seperator + ((Arg) reference).getName() + suffix;
		} else if (reference instanceof EqStatement) {
			return prefix + "eq " + String.join(", ",
					((EqStatement) reference).getLhs().stream().map(lhs -> argToString(lhs))
					.collect(Collectors.toList()));
		} else if (reference instanceof InputStatement) {
			return prefix + "agree_input " + String.join(", ", ((InputStatement) reference).getLhs().stream()
					.map(lhs -> argToString(lhs)).collect(Collectors.toList()));
		} else if (reference instanceof DataPort) {
			return prefix + seperator + ((DataPort) reference).getName() + suffix;
		} else if (reference instanceof EventPort) {
			return prefix + seperator + ((EventPort) reference).getName() + suffix;
		} else if (reference instanceof EventDataPort) {
			return prefix + seperator + ((EventDataPort) reference).getName() + suffix;
		} else if (reference instanceof FeatureGroup) {
			String featName = ((FeatureGroup) reference).getName();
			String varName = var.toString();
			featName = varName.substring(varName.indexOf(featName)).replace("__", ".");
			return prefix + seperator + featName;
		} else if (reference instanceof PropertyStatement) {
			return prefix + seperator + ((PropertyStatement) reference).getName();
		} else if (reference instanceof Property) {
			return "AADL property " + ((Property) reference).getName();
		} else if (reference instanceof GetPropertyExpr) {
			return "Get_Property(" + ((GetPropertyExpr) reference).getContainingClassifier().getName() + ", "
					+ ((Property) ((GetPropertyExpr) reference).getProp()).getName() + ")";
		} else if (reference instanceof ComponentType || reference instanceof ComponentImplementation
				|| reference instanceof SystemImplementation) {
			if (var.id.equals(LustreAstBuilder.assumeHistSufix)) {
				return "Subcomponent Assumptions";
			}
			return "Result";
		} else if (reference instanceof AgreeStatement) {
			return prefix + reference.toString();
		}
		throw new AgreeException("Unhandled reference type: '" + reference.getClass().getName() + "'");
	}

	public static String getCategory(ComponentInstance rootInstance, AgreeVar var) {
		if (var.compInst == null || var.reference == null) {
			return null;
		}
		return LustreAstBuilder.getRelativeLocation(rootInstance.getInstanceObjectPath(),
				var.compInst.getInstanceObjectPath());
	}

}
