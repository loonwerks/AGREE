package com.rockwellcollins.atc.agree.formatting2;

import java.util.Arrays;
import java.util.function.Consumer;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.formatting2.IFormattableDocument;
import org.eclipse.xtext.formatting2.IHiddenRegionFormatter;
import org.eclipse.xtext.formatting2.regionaccess.ISemanticRegion;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import org.osate.aadl2.ArrayRange;
import org.osate.aadl2.BasicPropertyAssociation;
import org.osate.aadl2.ClassifierValue;
import org.osate.aadl2.ComputedValue;
import org.osate.aadl2.ContainmentPathElement;
import org.osate.aadl2.IntegerLiteral;
import org.osate.aadl2.ListValue;
import org.osate.aadl2.ModalPropertyValue;
import org.osate.aadl2.Operation;
import org.osate.aadl2.RangeValue;
import org.osate.aadl2.RealLiteral;
import org.osate.aadl2.RecordValue;
import org.osate.aadl2.ReferenceValue;
import org.osate.xtext.aadl2.properties.formatting2.PropertiesFormatter;

import com.rockwellcollins.atc.agree.agree.AgreeContract;
import com.rockwellcollins.atc.agree.agree.AgreeContractLibrary;
import com.rockwellcollins.atc.agree.agree.AgreeContractSubclause;
import com.rockwellcollins.atc.agree.agree.AlwaysStatement;
import com.rockwellcollins.atc.agree.agree.Arg;
import com.rockwellcollins.atc.agree.agree.ArrayLiteralExpr;
import com.rockwellcollins.atc.agree.agree.ArraySubExpr;
import com.rockwellcollins.atc.agree.agree.ArrayType;
import com.rockwellcollins.atc.agree.agree.ArrayUpdateExpr;
import com.rockwellcollins.atc.agree.agree.AssertStatement;
import com.rockwellcollins.atc.agree.agree.AssignStatement;
import com.rockwellcollins.atc.agree.agree.AssumeStatement;
import com.rockwellcollins.atc.agree.agree.BinaryExpr;
import com.rockwellcollins.atc.agree.agree.BoolLitExpr;
import com.rockwellcollins.atc.agree.agree.CallExpr;
import com.rockwellcollins.atc.agree.agree.ClosedTimeInterval;
import com.rockwellcollins.atc.agree.agree.ConnectionStatement;
import com.rockwellcollins.atc.agree.agree.ConstStatement;
import com.rockwellcollins.atc.agree.agree.EnumLitExpr;
import com.rockwellcollins.atc.agree.agree.EnumStatement;
import com.rockwellcollins.atc.agree.agree.EqStatement;
import com.rockwellcollins.atc.agree.agree.ExistsExpr;
import com.rockwellcollins.atc.agree.agree.Expr;
import com.rockwellcollins.atc.agree.agree.FlatmapExpr;
import com.rockwellcollins.atc.agree.agree.FloorCast;
import com.rockwellcollins.atc.agree.agree.FnDef;
import com.rockwellcollins.atc.agree.agree.FoldLeftExpr;
import com.rockwellcollins.atc.agree.agree.FoldRightExpr;
import com.rockwellcollins.atc.agree.agree.ForallExpr;
import com.rockwellcollins.atc.agree.agree.GetPropertyExpr;
import com.rockwellcollins.atc.agree.agree.GuaranteeStatement;
import com.rockwellcollins.atc.agree.agree.IfThenElseExpr;
import com.rockwellcollins.atc.agree.agree.IndicesExpr;
import com.rockwellcollins.atc.agree.agree.InitialStatement;
import com.rockwellcollins.atc.agree.agree.InputStatement;
import com.rockwellcollins.atc.agree.agree.LatchedExpr;
import com.rockwellcollins.atc.agree.agree.LemmaStatement;
import com.rockwellcollins.atc.agree.agree.LibraryFnDef;
import com.rockwellcollins.atc.agree.agree.LiftContractStatement;
import com.rockwellcollins.atc.agree.agree.LinearizationDef;
import com.rockwellcollins.atc.agree.agree.LinearizationInterval;
import com.rockwellcollins.atc.agree.agree.NamedID;
import com.rockwellcollins.atc.agree.agree.NodeBodyExpr;
import com.rockwellcollins.atc.agree.agree.NodeDef;
import com.rockwellcollins.atc.agree.agree.NodeEq;
import com.rockwellcollins.atc.agree.agree.NodeLemma;
import com.rockwellcollins.atc.agree.agree.NodeStmt;
import com.rockwellcollins.atc.agree.agree.OpenLeftTimeInterval;
import com.rockwellcollins.atc.agree.agree.OpenRightTimeInterval;
import com.rockwellcollins.atc.agree.agree.OpenTimeInterval;
import com.rockwellcollins.atc.agree.agree.ParamStatement;
import com.rockwellcollins.atc.agree.agree.PeriodicStatement;
import com.rockwellcollins.atc.agree.agree.PreExpr;
import com.rockwellcollins.atc.agree.agree.PrevExpr;
import com.rockwellcollins.atc.agree.agree.PropertyStatement;
import com.rockwellcollins.atc.agree.agree.RealCast;
import com.rockwellcollins.atc.agree.agree.RecordDef;
import com.rockwellcollins.atc.agree.agree.RecordLitExpr;
import com.rockwellcollins.atc.agree.agree.RecordUpdateExpr;
import com.rockwellcollins.atc.agree.agree.SelectionExpr;
import com.rockwellcollins.atc.agree.agree.SpecStatement;
import com.rockwellcollins.atc.agree.agree.SporadicStatement;
import com.rockwellcollins.atc.agree.agree.TagExpr;
import com.rockwellcollins.atc.agree.agree.UnaryExpr;
import com.rockwellcollins.atc.agree.agree.WhenHoldsStatement;
import com.rockwellcollins.atc.agree.agree.WhenOccursStatment;
import com.rockwellcollins.atc.agree.agree.WheneverBecomesTrueStatement;
import com.rockwellcollins.atc.agree.agree.WheneverHoldsStatement;
import com.rockwellcollins.atc.agree.agree.WheneverImpliesStatement;
import com.rockwellcollins.atc.agree.agree.WheneverOccursStatement;

public class AgreeFormatter extends PropertiesFormatter {

	protected void _format(final AgreeContractLibrary agreecontractlibrary,
			@Extension final IFormattableDocument document) {
		final Procedure1<IHiddenRegionFormatter> _function = it -> it.noSpace();
		document.<AgreeContractLibrary> surround(agreecontractlibrary, _function);
		this.format(agreecontractlibrary.getContract(), document);
	}

	protected void _format(final AgreeContractSubclause agreecontractsubclause,
			@Extension final IFormattableDocument document) {
		final Procedure1<IHiddenRegionFormatter> _function = it -> it.noSpace();
		document.<AgreeContractSubclause> surround(agreecontractsubclause, _function);
		this.format(agreecontractsubclause.getContract(), document);
	}

	protected void _format(final AgreeContract agreecontract, @Extension final IFormattableDocument document) {
		EList<SpecStatement> _specs = agreecontract.getSpecs();
		for (final SpecStatement specs : _specs) {
			{
				final Procedure1<IHiddenRegionFormatter> _function = it -> it.setNewLines(1);
				document.<SpecStatement> append(specs, _function);
				this.format(specs, document);
			}
		}
	}

	protected void _format(final InitialStatement initialstatement, @Extension final IFormattableDocument document) {
		this.format(initialstatement.getExpr(), document);
	}

	protected void _format(final ParamStatement paramstatement, @Extension final IFormattableDocument document) {
		this.format(paramstatement.getExpr(), document);
		this.format(paramstatement.getType(), document);
	}

	protected void _format(final ConnectionStatement connectionstatement,
			@Extension final IFormattableDocument document) {
		this.format(connectionstatement.getExpr(), document);
	}

	protected void _format(final AssumeStatement assumestatement, @Extension final IFormattableDocument document) {
		final Consumer<ISemanticRegion> _function = it -> {
			final Procedure1<IHiddenRegionFormatter> _function1 = it_1 -> it_1.noSpace();
			document.surround(it, _function1);
		};
		this.textRegionExtensions.regionFor(assumestatement.getExpr()).keywords("(").forEach(_function);
		final Consumer<ISemanticRegion> _function_1 = it -> {
			final Procedure1<IHiddenRegionFormatter> _function1 = it_1 -> it_1.noSpace();
			document.surround(it, _function1);
		};
		this.textRegionExtensions.regionFor(assumestatement.getExpr()).keywords(")").forEach(_function_1);
		final Procedure1<IHiddenRegionFormatter> _function_2 = it -> it.noSpace();
		document.prepend(this.textRegionExtensions.regionFor(assumestatement).keyword(";"), _function_2);
		this.format(assumestatement.getPattern(), document);
	}

	protected void _format(final GuaranteeStatement guaranteestatement,
			@Extension final IFormattableDocument document) {
		final Consumer<ISemanticRegion> _function = it -> {
			final Procedure1<IHiddenRegionFormatter> _function1 = it_1 -> it_1.noSpace();
			document.surround(it, _function1);
		};
		this.textRegionExtensions.regionFor(guaranteestatement.getExpr()).keywords("(").forEach(_function);
		final Consumer<ISemanticRegion> _function_1 = it -> {
			final Procedure1<IHiddenRegionFormatter> _function1 = it_1 -> it_1.noSpace();
			document.surround(it, _function1);
		};
		this.textRegionExtensions.regionFor(guaranteestatement.getExpr()).keywords(")").forEach(_function_1);
		final Procedure1<IHiddenRegionFormatter> _function_2 = it -> it.noSpace();
		document.prepend(this.textRegionExtensions.regionFor(guaranteestatement).keyword(";"), _function_2);
		this.format(guaranteestatement.getPattern(), document);
	}

	protected void _format(final AssertStatement assertstatement, @Extension final IFormattableDocument document) {
		final Consumer<ISemanticRegion> _function = it -> {
			final Procedure1<IHiddenRegionFormatter> _function1 = it_1 -> it_1.noSpace();
			document.surround(it, _function1);
		};
		this.textRegionExtensions.regionFor(assertstatement.getExpr()).keywords("(").forEach(_function);
		final Consumer<ISemanticRegion> _function_1 = it -> {
			final Procedure1<IHiddenRegionFormatter> _function1 = it_1 -> it_1.noSpace();
			document.surround(it, _function1);
		};
		this.textRegionExtensions.regionFor(assertstatement.getExpr()).keywords(")").forEach(_function_1);
		final Procedure1<IHiddenRegionFormatter> _function_2 = it -> it.noSpace();
		document.prepend(this.textRegionExtensions.regionFor(assertstatement).keyword(";"), _function_2);
		this.format(assertstatement.getPattern(), document);
	}

	protected void _format(final LemmaStatement lemmastatement, @Extension final IFormattableDocument document) {
		final Consumer<ISemanticRegion> _function = it -> {
			final Procedure1<IHiddenRegionFormatter> _function1 = it_1 -> it_1.noSpace();
			document.surround(it, _function1);
		};
		this.textRegionExtensions.regionFor(lemmastatement.getExpr()).keywords("(").forEach(_function);
		final Consumer<ISemanticRegion> _function_1 = it -> {
			final Procedure1<IHiddenRegionFormatter> _function1 = it_1 -> it_1.noSpace();
			document.surround(it, _function1);
		};
		this.textRegionExtensions.regionFor(lemmastatement.getExpr()).keywords(")").forEach(_function_1);
		final Procedure1<IHiddenRegionFormatter> _function_2 = it -> it.noSpace();
		document.prepend(this.textRegionExtensions.regionFor(lemmastatement).keyword(";"), _function_2);
		this.format(lemmastatement.getPattern(), document);
	}

	protected void _format(final LiftContractStatement liftcontractstatement,
			@Extension final IFormattableDocument document) {
		final Procedure1<IHiddenRegionFormatter> _function = it -> it.noSpace();
		document.prepend(this.textRegionExtensions.regionFor(liftcontractstatement).keyword(";"), _function);
	}

	protected void _format(final AlwaysStatement alwaysstatement, @Extension final IFormattableDocument document) {
		this.format(alwaysstatement.getExpr(), document);
	}

	protected void _format(final WhenHoldsStatement whenholdsstatement,
			@Extension final IFormattableDocument document) {
		this.format(whenholdsstatement.getCondition(), document);
		this.format(whenholdsstatement.getConditionInterval(), document);
		this.format(whenholdsstatement.getEvent(), document);
		this.format(whenholdsstatement.getEventInterval(), document);
	}

	protected void _format(final WhenOccursStatment whenoccursstatment,
			@Extension final IFormattableDocument document) {
		this.format(whenoccursstatment.getCondition(), document);
		this.format(whenoccursstatment.getTimes(), document);
		this.format(whenoccursstatment.getInterval(), document);
		this.format(whenoccursstatment.getEvent(), document);
	}

	protected void _format(final WheneverOccursStatement wheneveroccursstatement,
			@Extension final IFormattableDocument document) {
		this.format(wheneveroccursstatement.getCause(), document);
		this.format(wheneveroccursstatement.getEffect(), document);
		this.format(wheneveroccursstatement.getInterval(), document);
	}

	protected void _format(final WheneverBecomesTrueStatement wheneverbecomestruestatement,
			@Extension final IFormattableDocument document) {
		this.format(wheneverbecomestruestatement.getCause(), document);
		this.format(wheneverbecomestruestatement.getEffect(), document);
		this.format(wheneverbecomestruestatement.getInterval(), document);
	}

	protected void _format(final WheneverHoldsStatement wheneverholdsstatement,
			@Extension final IFormattableDocument document) {
		this.format(wheneverholdsstatement.getCause(), document);
		this.format(wheneverholdsstatement.getEffect(), document);
		this.format(wheneverholdsstatement.getInterval(), document);
	}

	protected void _format(final WheneverImpliesStatement wheneverimpliesstatement,
			@Extension final IFormattableDocument document) {
		this.format(wheneverimpliesstatement.getCause(), document);
		this.format(wheneverimpliesstatement.getLhs(), document);
		this.format(wheneverimpliesstatement.getRhs(), document);
		this.format(wheneverimpliesstatement.getInterval(), document);
	}

	protected void _format(final PeriodicStatement periodicstatement, @Extension final IFormattableDocument document) {
		this.format(periodicstatement.getEvent(), document);
		this.format(periodicstatement.getPeriod(), document);
		this.format(periodicstatement.getJitter(), document);
	}

	protected void _format(final SporadicStatement sporadicstatement, @Extension final IFormattableDocument document) {
		this.format(sporadicstatement.getEvent(), document);
		this.format(sporadicstatement.getIat(), document);
		this.format(sporadicstatement.getJitter(), document);
	}

	protected void _format(final ClosedTimeInterval closedtimeinterval,
			@Extension final IFormattableDocument document) {
		this.format(closedtimeinterval.getLow(), document);
		this.format(closedtimeinterval.getHigh(), document);
	}

	protected void _format(final OpenLeftTimeInterval openlefttimeinterval,
			@Extension final IFormattableDocument document) {
		this.format(openlefttimeinterval.getLow(), document);
		this.format(openlefttimeinterval.getHigh(), document);
	}

	protected void _format(final OpenRightTimeInterval openrighttimeinterval,
			@Extension final IFormattableDocument document) {
		this.format(openrighttimeinterval.getLow(), document);
		this.format(openrighttimeinterval.getHigh(), document);
	}

	protected void _format(final OpenTimeInterval opentimeinterval, @Extension final IFormattableDocument document) {
		this.format(opentimeinterval.getLow(), document);
		this.format(opentimeinterval.getHigh(), document);
	}

	protected void _format(final PropertyStatement propertystatement, @Extension final IFormattableDocument document) {
		this.format(propertystatement.getExpr(), document);
	}

	protected void _format(final ConstStatement conststatement, @Extension final IFormattableDocument document) {
		this.format(conststatement.getType(), document);
		this.format(conststatement.getExpr(), document);
	}

	protected void _format(final EqStatement eqstatement, @Extension final IFormattableDocument document) {
		EList<Arg> _lhs = eqstatement.getLhs();
		for (final Arg lhs : _lhs) {
			this.format(lhs, document);
		}
		this.format(eqstatement.getExpr(), document);
	}

	protected void _format(final InputStatement inputstatement, @Extension final IFormattableDocument document) {
		EList<Arg> _lhs = inputstatement.getLhs();
		for (final Arg lhs : _lhs) {
			this.format(lhs, document);
		}
	}

	protected void _format(final AssignStatement assignstatement, @Extension final IFormattableDocument document) {
		this.format(assignstatement.getExpr(), document);
	}

	protected void _format(final FnDef fndef, @Extension final IFormattableDocument document) {
		EList<Arg> _args = fndef.getArgs();
		for (final Arg args : _args) {
			this.format(args, document);
		}
		this.format(fndef.getType(), document);
		this.format(fndef.getExpr(), document);
	}

	protected void _format(final LibraryFnDef libraryfndef, @Extension final IFormattableDocument document) {
		EList<Arg> _args = libraryfndef.getArgs();
		for (final Arg args : _args) {
			this.format(args, document);
		}
		this.format(libraryfndef.getType(), document);
	}

	protected void _format(final LinearizationDef linearizationdef, @Extension final IFormattableDocument document) {
		EList<Arg> _args = linearizationdef.getArgs();
		for (final Arg args : _args) {
			this.format(args, document);
		}
		EList<LinearizationInterval> _intervals = linearizationdef.getIntervals();
		for (final LinearizationInterval intervals : _intervals) {
			this.format(intervals, document);
		}
		this.format(linearizationdef.getPrecision(), document);
		this.format(linearizationdef.getExprBody(), document);
	}

	protected void _format(final LinearizationInterval linearizationinterval,
			@Extension final IFormattableDocument document) {
		this.format(linearizationinterval.getStart(), document);
		this.format(linearizationinterval.getEnd(), document);
	}

	protected void _format(final NodeDef nodedef, @Extension final IFormattableDocument document) {
		EList<Arg> _args = nodedef.getArgs();
		for (final Arg args : _args) {
			this.format(args, document);
		}
		EList<Arg> _rets = nodedef.getRets();
		for (final Arg rets : _rets) {
			this.format(rets, document);
		}
		this.format(nodedef.getNodeBody(), document);
	}

	protected void _format(final NodeBodyExpr nodebodyexpr, @Extension final IFormattableDocument document) {
		EList<Arg> _locs = nodebodyexpr.getLocs();
		for (final Arg locs : _locs) {
			this.format(locs, document);
		}
		EList<NodeStmt> _stmts = nodebodyexpr.getStmts();
		for (final NodeStmt stmts : _stmts) {
			this.format(stmts, document);
		}
	}

	protected void _format(final NodeEq nodeeq, @Extension final IFormattableDocument document) {
		this.format(nodeeq.getExpr(), document);
	}

	protected void _format(final NodeLemma nodelemma, @Extension final IFormattableDocument document) {
		this.format(nodelemma.getExpr(), document);
	}

	protected void _format(final Arg arg, @Extension final IFormattableDocument document) {
		this.format(arg.getType(), document);
	}

	protected void _format(final RecordDef recorddef, @Extension final IFormattableDocument document) {
		EList<Arg> _args = recorddef.getArgs();
		for (final Arg args : _args) {
			this.format(args, document);
		}
	}

	protected void _format(final EnumStatement enumstatement, @Extension final IFormattableDocument document) {
		EList<NamedID> _enums = enumstatement.getEnums();
		for (final NamedID enums : _enums) {
			this.format(enums, document);
		}
	}

	protected void _format(final ForallExpr forallexpr, @Extension final IFormattableDocument document) {
		this.format(forallexpr.getBinding(), document);
		this.format(forallexpr.getArray(), document);
		this.format(forallexpr.getExpr(), document);
	}

	protected void _format(final ExistsExpr existsexpr, @Extension final IFormattableDocument document) {
		this.format(existsexpr.getBinding(), document);
		this.format(existsexpr.getArray(), document);
		this.format(existsexpr.getExpr(), document);
	}

	protected void _format(final FlatmapExpr flatmapexpr, @Extension final IFormattableDocument document) {
		this.format(flatmapexpr.getBinding(), document);
		this.format(flatmapexpr.getArray(), document);
		this.format(flatmapexpr.getExpr(), document);
	}

	protected void _format(final FoldLeftExpr foldleftexpr, @Extension final IFormattableDocument document) {
		this.format(foldleftexpr.getBinding(), document);
		this.format(foldleftexpr.getArray(), document);
		this.format(foldleftexpr.getAccumulator(), document);
		this.format(foldleftexpr.getInitial(), document);
		this.format(foldleftexpr.getExpr(), document);
	}

	protected void _format(final FoldRightExpr foldrightexpr, @Extension final IFormattableDocument document) {
		this.format(foldrightexpr.getBinding(), document);
		this.format(foldrightexpr.getArray(), document);
		this.format(foldrightexpr.getAccumulator(), document);
		this.format(foldrightexpr.getInitial(), document);
		this.format(foldrightexpr.getExpr(), document);
	}

	protected void _format(final BinaryExpr binaryexpr, @Extension final IFormattableDocument document) {
		this.format(binaryexpr.getRight(), document);
		this.format(binaryexpr.getLeft(), document);
	}

	protected void _format(final UnaryExpr unaryexpr, @Extension final IFormattableDocument document) {
		this.format(unaryexpr.getExpr(), document);
	}

	protected void _format(final IfThenElseExpr ifthenelseexpr, @Extension final IFormattableDocument document) {
		this.format(ifthenelseexpr.getA(), document);
		this.format(ifthenelseexpr.getB(), document);
		this.format(ifthenelseexpr.getC(), document);
	}

	protected void _format(final PrevExpr prevexpr, @Extension final IFormattableDocument document) {
		this.format(prevexpr.getDelay(), document);
		this.format(prevexpr.getInit(), document);
	}

	protected void _format(final GetPropertyExpr getpropertyexpr, @Extension final IFormattableDocument document) {
		this.format(getpropertyexpr.getComponentRef(), document);
	}

	protected void _format(final ArrayUpdateExpr arrayupdateexpr, @Extension final IFormattableDocument document) {
		EList<Expr> _indices = arrayupdateexpr.getIndices();
		for (final Expr indices : _indices) {
			this.format(indices, document);
		}
		EList<Expr> _valueExprs = arrayupdateexpr.getValueExprs();
		for (final Expr valueExprs : _valueExprs) {
			this.format(valueExprs, document);
		}
		this.format(arrayupdateexpr.getArray(), document);
	}

	protected void _format(final RecordUpdateExpr recordupdateexpr, @Extension final IFormattableDocument document) {
		this.format(recordupdateexpr.getExpr(), document);
		this.format(recordupdateexpr.getRecord(), document);
	}

	protected void _format(final ArraySubExpr arraysubexpr, @Extension final IFormattableDocument document) {
		this.format(arraysubexpr.getIndex(), document);
		this.format(arraysubexpr.getExpr(), document);
	}

	protected void _format(final IndicesExpr indicesexpr, @Extension final IFormattableDocument document) {
		this.format(indicesexpr.getArray(), document);
	}

	protected void _format(final CallExpr callexpr, @Extension final IFormattableDocument document) {
		this.format(callexpr.getRef(), document);
		EList<Expr> _args = callexpr.getArgs();
		for (final Expr args : _args) {
			this.format(args, document);
		}
	}

	protected void _format(final RecordLitExpr recordlitexpr, @Extension final IFormattableDocument document) {
		this.format(recordlitexpr.getRecordType(), document);
		EList<Expr> _argExpr = recordlitexpr.getArgExpr();
		for (final Expr argExpr : _argExpr) {
			this.format(argExpr, document);
		}
	}

	protected void _format(final EnumLitExpr enumlitexpr, @Extension final IFormattableDocument document) {
		this.format(enumlitexpr.getEnumType(), document);
	}

	protected void _format(final PreExpr preexpr, @Extension final IFormattableDocument document) {
		this.format(preexpr.getExpr(), document);
	}

	protected void _format(final LatchedExpr latchedexpr, @Extension final IFormattableDocument document) {
		this.format(latchedexpr.getExpr(), document);
	}

	protected void _format(final BoolLitExpr boollitexpr, @Extension final IFormattableDocument document) {
		this.format(boollitexpr.getVal(), document);
	}

	protected void _format(final FloorCast floorcast, @Extension final IFormattableDocument document) {
		this.format(floorcast.getExpr(), document);
	}

	protected void _format(final RealCast realcast, @Extension final IFormattableDocument document) {
		this.format(realcast.getExpr(), document);
	}

	protected void _format(final ArrayLiteralExpr arrayliteralexpr, @Extension final IFormattableDocument document) {
		EList<Expr> _elems = arrayliteralexpr.getElems();
		for (final Expr elems : _elems) {
			this.format(elems, document);
		}
	}

	protected void _format(final ArrayType arraytype, @Extension final IFormattableDocument document) {
		this.format(arraytype.getStem(), document);
	}

	protected void _format(final TagExpr tagexpr, @Extension final IFormattableDocument document) {
		this.format(tagexpr.getStem(), document);
	}

	protected void _format(final SelectionExpr selectionexpr, @Extension final IFormattableDocument document) {
		this.format(selectionexpr.getTarget(), document);
	}

	@Override
	@SuppressWarnings("unused") /* suppress warning from final else clause in ITE */
	public void format(final Object agreecontractsubclause, final IFormattableDocument document) {
		if (agreecontractsubclause instanceof AgreeContractSubclause) {
			_format((AgreeContractSubclause) agreecontractsubclause, document);
			return;
		} else if (agreecontractsubclause instanceof AgreeContractLibrary) {
			_format((AgreeContractLibrary) agreecontractsubclause, document);
			return;
		} else if (agreecontractsubclause instanceof IntegerLiteral) {
			_format((IntegerLiteral) agreecontractsubclause, document);
			return;
		} else if (agreecontractsubclause instanceof RealLiteral) {
			_format((RealLiteral) agreecontractsubclause, document);
			return;
		} else if (agreecontractsubclause instanceof AssertStatement) {
			_format((AssertStatement) agreecontractsubclause, document);
			return;
		} else if (agreecontractsubclause instanceof AssumeStatement) {
			_format((AssumeStatement) agreecontractsubclause, document);
			return;
		} else if (agreecontractsubclause instanceof FnDef) {
			_format((FnDef) agreecontractsubclause, document);
			return;
		} else if (agreecontractsubclause instanceof GuaranteeStatement) {
			_format((GuaranteeStatement) agreecontractsubclause, document);
			return;
		} else if (agreecontractsubclause instanceof LemmaStatement) {
			_format((LemmaStatement) agreecontractsubclause, document);
			return;
		} else if (agreecontractsubclause instanceof LibraryFnDef) {
			_format((LibraryFnDef) agreecontractsubclause, document);
			return;
		} else if (agreecontractsubclause instanceof LinearizationDef) {
			_format((LinearizationDef) agreecontractsubclause, document);
			return;
		} else if (agreecontractsubclause instanceof NodeDef) {
			_format((NodeDef) agreecontractsubclause, document);
			return;
		} else if (agreecontractsubclause instanceof ClassifierValue) {
			_format((ClassifierValue) agreecontractsubclause, document);
			return;
		} else if (agreecontractsubclause instanceof ComputedValue) {
			_format((ComputedValue) agreecontractsubclause, document);
			return;
		} else if (agreecontractsubclause instanceof ModalPropertyValue) {
			_format((ModalPropertyValue) agreecontractsubclause, document);
			return;
		} else if (agreecontractsubclause instanceof RangeValue) {
			_format((RangeValue) agreecontractsubclause, document);
			return;
		} else if (agreecontractsubclause instanceof RecordValue) {
			_format((RecordValue) agreecontractsubclause, document);
			return;
		} else if (agreecontractsubclause instanceof ReferenceValue) {
			_format((ReferenceValue) agreecontractsubclause, document);
			return;
		} else if (agreecontractsubclause instanceof AgreeContract) {
			_format((AgreeContract) agreecontractsubclause, document);
			return;
		} else if (agreecontractsubclause instanceof Arg) {
			_format((Arg) agreecontractsubclause, document);
			return;
		} else if (agreecontractsubclause instanceof ArrayLiteralExpr) {
			_format((ArrayLiteralExpr) agreecontractsubclause, document);
			return;
		} else if (agreecontractsubclause instanceof ArraySubExpr) {
			_format((ArraySubExpr) agreecontractsubclause, document);
			return;
		} else if (agreecontractsubclause instanceof ArrayType) {
			_format((ArrayType) agreecontractsubclause, document);
			return;
		} else if (agreecontractsubclause instanceof ArrayUpdateExpr) {
			_format((ArrayUpdateExpr) agreecontractsubclause, document);
			return;
		} else if (agreecontractsubclause instanceof AssignStatement) {
			_format((AssignStatement) agreecontractsubclause, document);
			return;
		} else if (agreecontractsubclause instanceof BinaryExpr) {
			_format((BinaryExpr) agreecontractsubclause, document);
			return;
		} else if (agreecontractsubclause instanceof BoolLitExpr) {
			_format((BoolLitExpr) agreecontractsubclause, document);
			return;
		} else if (agreecontractsubclause instanceof CallExpr) {
			_format((CallExpr) agreecontractsubclause, document);
			return;
		} else if (agreecontractsubclause instanceof ConnectionStatement) {
			_format((ConnectionStatement) agreecontractsubclause, document);
			return;
		} else if (agreecontractsubclause instanceof ConstStatement) {
			_format((ConstStatement) agreecontractsubclause, document);
			return;
		} else if (agreecontractsubclause instanceof EnumLitExpr) {
			_format((EnumLitExpr) agreecontractsubclause, document);
			return;
		} else if (agreecontractsubclause instanceof EnumStatement) {
			_format((EnumStatement) agreecontractsubclause, document);
			return;
		} else if (agreecontractsubclause instanceof EqStatement) {
			_format((EqStatement) agreecontractsubclause, document);
			return;
		} else if (agreecontractsubclause instanceof ExistsExpr) {
			_format((ExistsExpr) agreecontractsubclause, document);
			return;
		} else if (agreecontractsubclause instanceof FlatmapExpr) {
			_format((FlatmapExpr) agreecontractsubclause, document);
			return;
		} else if (agreecontractsubclause instanceof FloorCast) {
			_format((FloorCast) agreecontractsubclause, document);
			return;
		} else if (agreecontractsubclause instanceof FoldLeftExpr) {
			_format((FoldLeftExpr) agreecontractsubclause, document);
			return;
		} else if (agreecontractsubclause instanceof FoldRightExpr) {
			_format((FoldRightExpr) agreecontractsubclause, document);
			return;
		} else if (agreecontractsubclause instanceof ForallExpr) {
			_format((ForallExpr) agreecontractsubclause, document);
			return;
		} else if (agreecontractsubclause instanceof GetPropertyExpr) {
			_format((GetPropertyExpr) agreecontractsubclause, document);
			return;
		} else if (agreecontractsubclause instanceof IfThenElseExpr) {
			_format((IfThenElseExpr) agreecontractsubclause, document);
			return;
		} else if (agreecontractsubclause instanceof IndicesExpr) {
			_format((IndicesExpr) agreecontractsubclause, document);
			return;
		} else if (agreecontractsubclause instanceof InitialStatement) {
			_format((InitialStatement) agreecontractsubclause, document);
			return;
		} else if (agreecontractsubclause instanceof InputStatement) {
			_format((InputStatement) agreecontractsubclause, document);
			return;
		} else if (agreecontractsubclause instanceof LatchedExpr) {
			_format((LatchedExpr) agreecontractsubclause, document);
			return;
		} else if (agreecontractsubclause instanceof LiftContractStatement) {
			_format((LiftContractStatement) agreecontractsubclause, document);
			return;
		} else if (agreecontractsubclause instanceof NodeEq) {
			_format((NodeEq) agreecontractsubclause, document);
			return;
		} else if (agreecontractsubclause instanceof NodeLemma) {
			_format((NodeLemma) agreecontractsubclause, document);
			return;
		} else if (agreecontractsubclause instanceof ParamStatement) {
			_format((ParamStatement) agreecontractsubclause, document);
			return;
		} else if (agreecontractsubclause instanceof PreExpr) {
			_format((PreExpr) agreecontractsubclause, document);
			return;
		} else if (agreecontractsubclause instanceof PrevExpr) {
			_format((PrevExpr) agreecontractsubclause, document);
			return;
		} else if (agreecontractsubclause instanceof PropertyStatement) {
			_format((PropertyStatement) agreecontractsubclause, document);
			return;
		} else if (agreecontractsubclause instanceof RealCast) {
			_format((RealCast) agreecontractsubclause, document);
			return;
		} else if (agreecontractsubclause instanceof RecordDef) {
			_format((RecordDef) agreecontractsubclause, document);
			return;
		} else if (agreecontractsubclause instanceof RecordLitExpr) {
			_format((RecordLitExpr) agreecontractsubclause, document);
			return;
		} else if (agreecontractsubclause instanceof RecordUpdateExpr) {
			_format((RecordUpdateExpr) agreecontractsubclause, document);
			return;
		} else if (agreecontractsubclause instanceof SelectionExpr) {
			_format((SelectionExpr) agreecontractsubclause, document);
			return;
		} else if (agreecontractsubclause instanceof TagExpr) {
			_format((TagExpr) agreecontractsubclause, document);
			return;
		} else if (agreecontractsubclause instanceof UnaryExpr) {
			_format((UnaryExpr) agreecontractsubclause, document);
			return;
		} else if (agreecontractsubclause instanceof ListValue) {
			_format((ListValue) agreecontractsubclause, document);
			return;
		} else if (agreecontractsubclause instanceof Operation) {
			_format((Operation) agreecontractsubclause, document);
			return;
		} else if (agreecontractsubclause instanceof LinearizationInterval) {
			_format((LinearizationInterval) agreecontractsubclause, document);
			return;
		} else if (agreecontractsubclause instanceof NodeBodyExpr) {
			_format((NodeBodyExpr) agreecontractsubclause, document);
			return;
		} else if (agreecontractsubclause instanceof PeriodicStatement) {
			_format((PeriodicStatement) agreecontractsubclause, document);
			return;
		} else if (agreecontractsubclause instanceof SporadicStatement) {
			_format((SporadicStatement) agreecontractsubclause, document);
			return;
		} else if (agreecontractsubclause instanceof WhenHoldsStatement) {
			_format((WhenHoldsStatement) agreecontractsubclause, document);
			return;
		} else if (agreecontractsubclause instanceof WhenOccursStatment) {
			_format((WhenOccursStatment) agreecontractsubclause, document);
			return;
		} else if (agreecontractsubclause instanceof WheneverBecomesTrueStatement) {
			_format((WheneverBecomesTrueStatement) agreecontractsubclause, document);
			return;
		} else if (agreecontractsubclause instanceof WheneverHoldsStatement) {
			_format((WheneverHoldsStatement) agreecontractsubclause, document);
			return;
		} else if (agreecontractsubclause instanceof WheneverImpliesStatement) {
			_format((WheneverImpliesStatement) agreecontractsubclause, document);
			return;
		} else if (agreecontractsubclause instanceof WheneverOccursStatement) {
			_format((WheneverOccursStatement) agreecontractsubclause, document);
			return;
		} else if (agreecontractsubclause instanceof XtextResource) {
			_format((XtextResource) agreecontractsubclause, document);
			return;
		} else if (agreecontractsubclause instanceof ArrayRange) {
			_format((ArrayRange) agreecontractsubclause, document);
			return;
		} else if (agreecontractsubclause instanceof BasicPropertyAssociation) {
			_format((BasicPropertyAssociation) agreecontractsubclause, document);
			return;
		} else if (agreecontractsubclause instanceof ContainmentPathElement) {
			_format((ContainmentPathElement) agreecontractsubclause, document);
			return;
		} else if (agreecontractsubclause instanceof AlwaysStatement) {
			_format((AlwaysStatement) agreecontractsubclause, document);
			return;
		} else if (agreecontractsubclause instanceof ClosedTimeInterval) {
			_format((ClosedTimeInterval) agreecontractsubclause, document);
			return;
		} else if (agreecontractsubclause instanceof OpenLeftTimeInterval) {
			_format((OpenLeftTimeInterval) agreecontractsubclause, document);
			return;
		} else if (agreecontractsubclause instanceof OpenRightTimeInterval) {
			_format((OpenRightTimeInterval) agreecontractsubclause, document);
			return;
		} else if (agreecontractsubclause instanceof OpenTimeInterval) {
			_format((OpenTimeInterval) agreecontractsubclause, document);
			return;
		} else if (agreecontractsubclause instanceof EObject) {
			_format((EObject) agreecontractsubclause, document);
			return;
		} else if (agreecontractsubclause == null) {
			_format((Void) null, document);
			return;
		} else if (agreecontractsubclause != null) {
			_format(agreecontractsubclause, document);
			return;
		} else {
			throw new IllegalArgumentException("Unhandled parameter types: "
					+ Arrays.<Object> asList(agreecontractsubclause, document).toString());
		}
	}
}
