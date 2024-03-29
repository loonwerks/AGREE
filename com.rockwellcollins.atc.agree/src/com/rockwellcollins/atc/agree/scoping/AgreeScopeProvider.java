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
package com.rockwellcollins.atc.agree.scoping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.xtext.EcoreUtil2;
import org.eclipse.xtext.scoping.IScope;
import org.eclipse.xtext.scoping.Scopes;
import org.eclipse.xtext.scoping.impl.FilteringScope;
import org.osate.aadl2.Aadl2Package;
import org.osate.aadl2.AadlPackage;
import org.osate.aadl2.AnnexLibrary;
import org.osate.aadl2.AnnexSubclause;
import org.osate.aadl2.Classifier;
import org.osate.aadl2.ComponentClassifier;
import org.osate.aadl2.ComponentImplementation;
import org.osate.aadl2.ComponentType;
import org.osate.aadl2.DataImplementation;
import org.osate.aadl2.DataType;
import org.osate.aadl2.Element;
import org.osate.aadl2.NamedElement;
import org.osate.aadl2.PackageRename;
import org.osate.aadl2.Property;
import org.osate.aadl2.PropertyAssociation;
import org.osate.aadl2.PropertySet;
import org.osate.aadl2.PublicPackageSection;
import org.osate.aadl2.Subcomponent;
import org.osate.annexsupport.AnnexUtil;

import com.google.common.collect.Lists;
import com.rockwellcollins.atc.agree.AgreeTypeSystem;
import com.rockwellcollins.atc.agree.AgreeTypeSystem.RecordTypeDef;
import com.rockwellcollins.atc.agree.AgreeTypeSystem.TypeDef;
import com.rockwellcollins.atc.agree.agree.AgreeContract;
import com.rockwellcollins.atc.agree.agree.AgreeContractLibrary;
import com.rockwellcollins.atc.agree.agree.AgreeContractSubclause;
import com.rockwellcollins.atc.agree.agree.AgreePackage;
import com.rockwellcollins.atc.agree.agree.AssignStatement;
import com.rockwellcollins.atc.agree.agree.ComponentRef;
import com.rockwellcollins.atc.agree.agree.ConnectionStatement;
import com.rockwellcollins.atc.agree.agree.ConstStatement;
import com.rockwellcollins.atc.agree.agree.DoubleDotRef;
import com.rockwellcollins.atc.agree.agree.EnumStatement;
import com.rockwellcollins.atc.agree.agree.EqStatement;
import com.rockwellcollins.atc.agree.agree.ExistsExpr;
import com.rockwellcollins.atc.agree.agree.FlatmapExpr;
import com.rockwellcollins.atc.agree.agree.FnDef;
import com.rockwellcollins.atc.agree.agree.FoldLeftExpr;
import com.rockwellcollins.atc.agree.agree.FoldRightExpr;
import com.rockwellcollins.atc.agree.agree.ForallExpr;
import com.rockwellcollins.atc.agree.agree.GetPropertyExpr;
import com.rockwellcollins.atc.agree.agree.InputStatement;
import com.rockwellcollins.atc.agree.agree.LibraryFnDef;
import com.rockwellcollins.atc.agree.agree.LinearizationDef;
import com.rockwellcollins.atc.agree.agree.NamedElmExpr;
import com.rockwellcollins.atc.agree.agree.NodeDef;
import com.rockwellcollins.atc.agree.agree.OrderStatement;
import com.rockwellcollins.atc.agree.agree.RecordDef;
import com.rockwellcollins.atc.agree.agree.RecordLitExpr;
import com.rockwellcollins.atc.agree.agree.RecordUpdateExpr;
import com.rockwellcollins.atc.agree.agree.SelectionExpr;
import com.rockwellcollins.atc.agree.agree.SpecStatement;
import com.rockwellcollins.atc.agree.agree.SynchStatement;
import com.rockwellcollins.atc.agree.agree.ThisRef;
import com.rockwellcollins.atc.agree.agree.TimeFallExpr;
import com.rockwellcollins.atc.agree.agree.TimeOfExpr;
import com.rockwellcollins.atc.agree.agree.TimeRiseExpr;
import com.rockwellcollins.atc.agree.agree.UninterpretedFnDef;

/**
 * This class contains custom scoping description.
 *
 * see : http://www.eclipse.org/Xtext/documentation.html#scoping on how and when
 * to use it
 *
 */
public class AgreeScopeProvider extends org.osate.xtext.aadl2.properties.scoping.PropertiesScopeProvider {

	private Map<String, NamedElement> toNamedElementMap(List<NamedElement> nes) {
		Map<String, NamedElement> map = new HashMap<>();
		for (NamedElement ne : nes) {
			map.put(ne.getName(), ne);
		}
		return map;
	}
	private Map<String, NamedElement> getNamedElementsFromSpecs(EList<SpecStatement> specs) {
		Map<String, NamedElement> nelms = new HashMap<>();
		for (SpecStatement spec : specs) {
			if (spec instanceof NamedElement) {
				nelms.put(((NamedElement) spec).getName(), (NamedElement) spec);
			}

			if (spec instanceof EqStatement) {
				EqStatement eq = (EqStatement) spec;
				ArrayList<NamedElement> nes = new ArrayList<>();
				nes.addAll(eq.getLhs());
				nelms.putAll(toNamedElementMap(nes));

			} else if (spec instanceof ConstStatement) {
				ConstStatement c = (ConstStatement) spec;
				nelms.put(c.getName(), c);

			} else if (spec instanceof InputStatement) {
				ArrayList<NamedElement> nes = new ArrayList<>();
				nes.addAll(((InputStatement) spec).getLhs());
				nelms.putAll(toNamedElementMap(nes));
			} else if (spec instanceof EnumStatement) {
				ArrayList<NamedElement> nes = new ArrayList<>();
				nes.addAll(((EnumStatement) spec).getEnums());
				nelms.putAll(toNamedElementMap(nes));
			}
		}
		return nelms;
	}

	private Map<String, NamedElement> getNamedElementsFromClassifier(Classifier ctx, boolean fromCompImpl) {

		Map<String, NamedElement> components = new HashMap<>();

		components.putAll(getNamedElements(getAadlContainer(ctx)));

		for (AnnexSubclause annex : AnnexUtil.getAllAnnexSubclauses(ctx,
				AgreePackage.eINSTANCE.getAgreeContractSubclause())) {
			AgreeContract contract = (AgreeContract) ((AgreeContractSubclause) annex).getContract();

			components.putAll(getNamedElementsFromSpecs(contract.getSpecs()));

		}

		Classifier extended = ctx.getExtended();
		if (extended != null) {
			components.putAll(getNamedElementsFromClassifier(extended, false));
		}

		if (ctx instanceof ComponentImplementation) {

			components.putAll(getNamedElementsFromClassifier(((ComponentImplementation) ctx).getType(), true));

			ArrayList<NamedElement> nes = new ArrayList<>();
			nes.addAll(((ComponentImplementation) ctx).getAllSubcomponents());
			nes.addAll(((ComponentImplementation) ctx).getAllConnections());
			components.putAll(toNamedElementMap(nes));

		} else if (ctx instanceof ComponentType) {
			if (fromCompImpl) {
				ArrayList<NamedElement> nes = new ArrayList<>();
				nes.addAll(((ComponentType) ctx).getAllFeatures());
				components.putAll(toNamedElementMap(nes));
			} else {
				ArrayList<NamedElement> nes = new ArrayList<>();
				nes.addAll(((ComponentType) ctx).getOwnedFeatures());
				components.putAll(toNamedElementMap(nes));
			}

		}

		return components;

	}

	private Map<String, NamedElement> getNamedElements(EObject ctx) {

		Map<String, NamedElement> components = new HashMap<>();
		if (ctx instanceof AadlPackage) {

			components.put(((AadlPackage) ctx).getName(), (AadlPackage) ctx);

			PublicPackageSection pubSec = ((AadlPackage) ctx).getPublicSection();
			for (Element el : pubSec.getOwnedElements()) {
				if (el instanceof DataImplementation || el instanceof DataType) {
					components.put(((NamedElement) el).getName(), (NamedElement) el);
				}
			}

			for (AnnexLibrary annex : AnnexUtil.getAllActualAnnexLibraries(((AadlPackage) ctx),
					AgreePackage.eINSTANCE.getAgreeContractLibrary())) {

				AgreeContract contract = (AgreeContract) ((AgreeContractLibrary) annex).getContract();
				components.putAll(getNamedElementsFromSpecs(contract.getSpecs()));

			}


		} else {

			components.putAll(getNamedElementsFromClassifier((Classifier) ctx, false));

		}

		return components;

	}


	private EObject getAadlContainer(EObject o) {

		EObject container = o.eContainer();
		if (container == null) {
			return null;
		} else if (container instanceof AadlPackage) {
			return container;
		} else if (container instanceof Classifier) {
			return container;
		} else {
			return getAadlContainer(o.eContainer());
		}
	}

	private AadlPackage getContainingPackage(EObject o) {

		EObject container = o.eContainer();
		if (container == null) {
			return null;
		} else if (container instanceof AadlPackage) {
			return (AadlPackage) container;
		} else {
			return getContainingPackage(o.eContainer());
		}
	}

	IScope scope_NamedElement(AgreeContract ctx, EReference ref) {
		EObject container = getAadlContainer(ctx);
		AadlPackage pkg = getContainingPackage(ctx);

		Map<String, NamedElement> elems = new HashMap<>();

		for (PackageRename rename : EcoreUtil2.getAllContentsOfType(pkg, PackageRename.class)) {
			if (rename.isRenameAll()) {
				AadlPackage renamedPackage = rename.getRenamedPackage();
				elems.putAll(getNamedElements(renamedPackage));
			}
		}

		elems.putAll(getNamedElements(container));

		return Scopes.scopeFor(elems.values(), getScope(ctx.eContainer().eContainer(), ref));
	}

	IScope scope_NamedElement(NodeDef ctx, EReference ref) {
		Set<Element> components = new HashSet<>();
		components.addAll(ctx.getArgs());
		components.addAll(ctx.getRets());
		components.addAll(ctx.getNodeBody().getLocs());
		IScope outer = new FilteringScope(getScope(ctx.eContainer(), ref),
				input -> (AgreePackage.eINSTANCE.getNodeDef().isSuperTypeOf(input.getEClass())
						|| AgreePackage.eINSTANCE.getRecordDef().isSuperTypeOf(input.getEClass())
						|| AgreePackage.eINSTANCE.getConstStatement().isSuperTypeOf(input.getEClass())
						|| Aadl2Package.eINSTANCE.getAadlPackage().isSuperTypeOf(input.getEClass())
						|| Aadl2Package.eINSTANCE.getComponentClassifier().isSuperTypeOf(input.getEClass())));
		return Scopes.scopeFor(components, outer);
	}

	IScope scope_NamedElement(ConnectionStatement ctx, EReference ref) {
		EObject container = ctx.getContainingClassifier();
		IScope outerScope = IScope.NULLSCOPE;
		if (container instanceof ComponentImplementation) {
			ComponentImplementation compImpl = (ComponentImplementation) container;
			outerScope = getScope(ctx.eContainer(), ref);
			outerScope = Scopes.scopeFor(compImpl.getAllSubcomponents(), outerScope);
			outerScope = Scopes.scopeFor(compImpl.getAllConnections(), outerScope);
		}
		return outerScope;
	}

	IScope scope_NamedElement(OrderStatement ctx, EReference ref) {
		EObject container = ctx.getContainingClassifier();

		IScope outerScope = IScope.NULLSCOPE;
		if (container instanceof ComponentImplementation) {
			ComponentImplementation compImpl = (ComponentImplementation) container;
			outerScope = Scopes.scopeFor(compImpl.getAllSubcomponents(), getScope(ctx.eContainer(), ref));
		}
		return outerScope;
	}

	IScope scope_NamedElement(SynchStatement ctx, EReference ref) {
		EObject container = ctx.getContainingClassifier();
		while (!(container instanceof ComponentClassifier)) {
			container = container.eContainer();
		}

		if (container instanceof ComponentImplementation) {
			return Scopes.scopeFor(((ComponentImplementation) container).getAllSubcomponents(),
					getScope(ctx.eContainer(), ref));
		}

		return IScope.NULLSCOPE;
	}

	IScope scope_NamedElement(FnDef ctx, EReference ref) {
		return Scopes.scopeFor(ctx.getArgs(), getScope(ctx.eContainer(), ref));
	}

	IScope scope_NamedElement(LinearizationDef ctx, EReference ref) {
		return Scopes.scopeFor(ctx.getArgs(), getScope(ctx.eContainer(), ref));
	}


	IScope scope_NamedElement(LibraryFnDef ctx, EReference ref) {
		return Scopes.scopeFor(ctx.getArgs(), getScope(ctx.eContainer(), ref));
	}

	IScope scope_NamedElement(UninterpretedFnDef ctx, EReference ref) {
		return Scopes.scopeFor(ctx.getArgs(), getScope(ctx.eContainer(), ref));
	}

	IScope scope_NamedElement(SpecStatement ctx, EReference ref) {
		return getScope(ctx.eContainer(), ref);
	}

	IScope scope_NamedElement(AssignStatement ctx, EReference ref) {
		return getScope(ctx.eContainer(), ref);
	}

	// Expressions

	private List<NamedElement> getAadlComponentElements(EObject ctx) {
		List<NamedElement> components = new ArrayList<>();
		if (ctx instanceof ComponentType) {
			components.addAll(((ComponentType) ctx).getAllFeatures());

		} else if (ctx instanceof ComponentImplementation) {
			components.addAll(((ComponentImplementation) ctx).getAllSubcomponents());
			components.addAll(((ComponentImplementation) ctx).getAllConnections());
			components.addAll(getAadlComponentElements(((ComponentImplementation) ctx).getType()));
		}
		return components;
	}

//	IScope scope_NamedElement(EventExpr ctx, EReference ref) {
//		EObject container = ctx.getContainingClassifier();
//		return Scopes.scopeFor(getAadlComponentElements(container));
//	}

	IScope scope_NamedElement(TimeOfExpr ctx, EReference ref) {
		EObject container = ctx.getContainingClassifier();
		return Scopes.scopeFor(getAadlComponentElements(container));
	}

	IScope scope_NamedElement(TimeRiseExpr ctx, EReference ref) {
		EObject container = ctx.getContainingClassifier();
		return Scopes.scopeFor(getAadlComponentElements(container));
	}

	IScope scope_NamedElement(TimeFallExpr ctx, EReference ref) {
		EObject container = ctx.getContainingClassifier();
		return Scopes.scopeFor(getAadlComponentElements(container));
	}

	IScope scope_NamedElement(ForallExpr ctx, EReference ref) {
		return Scopes.scopeFor(Lists.newArrayList(ctx.getBinding()), getScope(ctx.eContainer(), ref));
	}

	IScope scope_NamedElement(ExistsExpr ctx, EReference ref) {
		return Scopes.scopeFor(Lists.newArrayList(ctx.getBinding()), getScope(ctx.eContainer(), ref));
	}

	IScope scope_NamedElement(FlatmapExpr ctx, EReference ref) {
		return Scopes.scopeFor(Lists.newArrayList(ctx.getBinding()), getScope(ctx.eContainer(), ref));
	}

	IScope scope_NamedElement(FoldLeftExpr ctx, EReference ref) {
		return Scopes.scopeFor(Lists.newArrayList(ctx.getAccumulator(), ctx.getBinding()),
				getScope(ctx.eContainer(), ref));
	}

	IScope scope_NamedElement(FoldRightExpr ctx, EReference ref) {
		return Scopes.scopeFor(Lists.newArrayList(ctx.getAccumulator(), ctx.getBinding()),
				getScope(ctx.eContainer(), ref));
	}

	protected IScope scope_GetPropertyExpr_prop(GetPropertyExpr ctx, EReference ref) {

		IScope prevScope = prevScope(ctx, ref);

		ComponentRef cr = ctx.getComponentRef();
		if (cr instanceof ThisRef) {
			List<Property> ps = new ArrayList<>();

			EObject container = ctx.getContainingClassifier();
			while (container != null) {
				if (container instanceof Classifier) {
					List<PropertyAssociation> pas = ((Classifier) container).getAllPropertyAssociations();
					for (PropertyAssociation pa : pas) {
						ps.add(pa.getProperty());
					}
					container = ((Classifier) container).eContainer();
				} else if (container instanceof AadlPackage) {
					for (PropertySet propSet : EcoreUtil2.getAllContentsOfType(container, PropertySet.class)) {
						for (Property p : propSet.getOwnedProperties()) {
							ps.add(p);
						}
//								=======
//										EList<EObject> refs  = null;
//
//								if (container instanceof NestedDotID) {
//									NestedDotID parent = (NestedDotID) container;
//									refs = parent.eCrossReferences();
//
//									if (refs.size() != 1) {
//										return new HashSet<>(); // this will throw a parsing error
//									}
//									container = refs.get(0); // figure out what this type this portion
//
//									// of the nest id is so we can figure out
//									// what we could possibly link to
//
//									if (container instanceof ThreadSubcomponent) {
//										container = ((ThreadSubcomponent) container).getComponentType();
//										result.addAll(getAadlElements(container));
//									} else if (container instanceof Subcomponent) {
//										container = ((Subcomponent) container).getComponentImplementation();
//										if (container == null) { // no implementation is provided
//											container = refs.get(0);
//											container = ((Subcomponent) container).getClassifier();
//										}
//										result.addAll(getAadlElements(container));
//									} else if (container instanceof DataPort) {
//										container = ((DataPort) container).getDataFeatureClassifier();
//										result.addAll(getAadlElements(container));
//									} else if (container instanceof EventDataPort) {
//										container = ((EventDataPort) container).getDataFeatureClassifier();
//										result.addAll(getAadlElements(container));
//									} else if (container instanceof AadlPackage) {
//										result.addAll(getAadlElements(container));
//									} else if (container instanceof FeatureGroupImpl) {
//										container = ((FeatureGroupImpl) container).getAllFeatureGroupType();
//										result.addAll(getAadlElements(container));
//									} else if (container instanceof Arg || container instanceof ConstStatement) {
//										Type type;
//
//										if (container instanceof Arg) {
//											type = ((Arg) container).getType();
//										} else {
//											type = ((ConstStatement) container).getType();
//										}
//
//										if (type instanceof RecordType) {
//											DoubleDotRef elID = ((RecordType) type).getRecord();
//											NamedElement namedEl = elID.getElm();
//
//											if (namedEl instanceof ComponentImplementation) {
//												ComponentImplementation componentImplementation = (ComponentImplementation) namedEl;
//												EList<Subcomponent> subs = componentImplementation.getAllSubcomponents();
//												result.addAll(subs);
//											} else if (namedEl instanceof RecordDefExpr) {
//												result.addAll(((RecordDefExpr) namedEl).getArgs());
//												>>>>>>> origin/develop
					}
					container = null;
				} else {
					container = container.eContainer();
				}
			}

			return Scopes.scopeFor(ps, prevScope);

		} else if (cr instanceof DoubleDotRef) {
			NamedElement ne = ((DoubleDotRef) cr).getElm();
			if (ne instanceof Subcomponent) {
				List<PropertyAssociation> pas = ((Subcomponent) ne).getOwnedPropertyAssociations();
				List<Property> ps = new ArrayList<>();
				for (PropertyAssociation pa : pas) {
					ps.add(pa.getProperty());
				}
				return Scopes.scopeFor(ps, prevScope);
			}
		}

		return IScope.NULLSCOPE;
	}


	protected IScope scope_DoubleDotRef_elm(DoubleDotRef ctx, EReference ref) {

		IScope prevScope = prevScope(ctx, ref);
		EObject container = ctx.getContainingComponentImpl();
		if (container instanceof ComponentImplementation) {
			return Scopes.scopeFor(((ComponentImplementation) ctx).getAllSubcomponents(), prevScope);
		}
		return prevScope;
	}



	private List<NamedElement> getFieldsOfNE(NamedElement leaf) {

		if (leaf instanceof RecordDef) {
			List<NamedElement> result = new LinkedList<>();
			result.addAll(((RecordDef) leaf).getArgs());
			return result;

		} else if (leaf instanceof DataImplementation) {
			List<NamedElement> result = new LinkedList<>();
			ComponentImplementation componentImplementation = (ComponentImplementation) leaf;
			List<Subcomponent> subs = componentImplementation.getAllSubcomponents();
			result.addAll(subs);
			return result;

		} else {

			return new ArrayList<NamedElement>(getNamedElements(leaf).values());

		}

	}



	IScope scope_RecordLitExpr_args(RecordLitExpr ctx, EReference ref) {
		IScope prevScope = prevScope(ctx, ref);
		NamedElement recDef = ctx.getRecordType().getElm();
		Set<Element> components = new HashSet<>();
		if (recDef instanceof DataImplementation) {
			components.addAll(((DataImplementation) recDef).getAllSubcomponents());
			return Scopes.scopeFor(components, prevScope);
		} else if (recDef instanceof RecordDef) {
			components.addAll(((RecordDef) recDef).getArgs());
			return Scopes.scopeFor(components, prevScope);
		}
		return prevScope;
	}

	IScope scope_RecordUpdateExpr_key(RecordUpdateExpr ctx, EReference ref) {
		IScope prevScope = prevScope(ctx, ref);
		TypeDef typ = AgreeTypeSystem.infer(ctx.getRecord());
		if (typ instanceof RecordTypeDef) {
			NamedElement ne = ((RecordTypeDef) typ).namedElement;
			return Scopes.scopeFor(getFieldsOfNE(ne), prevScope);
		} else {
			return IScope.NULLSCOPE;
		}
	}

	private IScope prevScope(EObject ctx, EReference ref) {
		EObject container = ctx.eContainer();
		while (container instanceof SelectionExpr) {
			container = container.eContainer();
		}
		IScope prevScope = getScope(container, ref);
		return prevScope;

	}

	protected IScope scope_NamedElmExpr_elm(NamedElmExpr ctx, EReference ref) {
		return prevScope(ctx, ref);
	}

	protected IScope scope_SelectionExpr_field(SelectionExpr ctx, EReference ref) {

		TypeDef typ = AgreeTypeSystem.infer(ctx.getTarget());

		if (typ instanceof RecordTypeDef) {
			NamedElement ne = ((RecordTypeDef) typ).namedElement;
			return Scopes.scopeFor(getFieldsOfNE(ne));
		}


		return IScope.NULLSCOPE;

	}




}
