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
package com.rockwellcollins.atc.agree.formatting2;

import com.rockwellcollins.atc.agree.agree.AgreeContract
import com.rockwellcollins.atc.agree.agree.AgreeContractLibrary
import com.rockwellcollins.atc.agree.agree.AgreeContractSubclause
import com.rockwellcollins.atc.agree.agree.AlwaysStatement
import com.rockwellcollins.atc.agree.agree.Arg
import com.rockwellcollins.atc.agree.agree.ArrayLiteralExpr
import com.rockwellcollins.atc.agree.agree.ArraySubExpr
import com.rockwellcollins.atc.agree.agree.ArrayType
import com.rockwellcollins.atc.agree.agree.ArrayUpdateExpr
import com.rockwellcollins.atc.agree.agree.AssertStatement
import com.rockwellcollins.atc.agree.agree.AssignStatement
import com.rockwellcollins.atc.agree.agree.AssumeStatement
import com.rockwellcollins.atc.agree.agree.BinaryExpr
import com.rockwellcollins.atc.agree.agree.BoolLitExpr
import com.rockwellcollins.atc.agree.agree.CallExpr
import com.rockwellcollins.atc.agree.agree.ClosedTimeInterval
import com.rockwellcollins.atc.agree.agree.ConnectionStatement
import com.rockwellcollins.atc.agree.agree.ConstStatement
import com.rockwellcollins.atc.agree.agree.EnumLitExpr
import com.rockwellcollins.atc.agree.agree.EnumStatement
import com.rockwellcollins.atc.agree.agree.EqStatement
import com.rockwellcollins.atc.agree.agree.ExistsExpr
import com.rockwellcollins.atc.agree.agree.Expr
import com.rockwellcollins.atc.agree.agree.FlatmapExpr
import com.rockwellcollins.atc.agree.agree.FloorCast
import com.rockwellcollins.atc.agree.agree.FnDef
import com.rockwellcollins.atc.agree.agree.FoldLeftExpr
import com.rockwellcollins.atc.agree.agree.FoldRightExpr
import com.rockwellcollins.atc.agree.agree.ForallExpr
import com.rockwellcollins.atc.agree.agree.GetPropertyExpr
import com.rockwellcollins.atc.agree.agree.GuaranteeStatement
import com.rockwellcollins.atc.agree.agree.IfThenElseExpr
import com.rockwellcollins.atc.agree.agree.IndicesExpr
import com.rockwellcollins.atc.agree.agree.InitialStatement
import com.rockwellcollins.atc.agree.agree.InputStatement
import com.rockwellcollins.atc.agree.agree.LatchedExpr
import com.rockwellcollins.atc.agree.agree.LemmaStatement
import com.rockwellcollins.atc.agree.agree.LibraryFnDef
import com.rockwellcollins.atc.agree.agree.LinearizationDef
import com.rockwellcollins.atc.agree.agree.LinearizationInterval
import com.rockwellcollins.atc.agree.agree.NamedID
import com.rockwellcollins.atc.agree.agree.NodeBodyExpr
import com.rockwellcollins.atc.agree.agree.NodeDef
import com.rockwellcollins.atc.agree.agree.NodeEq
import com.rockwellcollins.atc.agree.agree.NodeLemma
import com.rockwellcollins.atc.agree.agree.NodeStmt
import com.rockwellcollins.atc.agree.agree.OpenLeftTimeInterval
import com.rockwellcollins.atc.agree.agree.OpenRightTimeInterval
import com.rockwellcollins.atc.agree.agree.OpenTimeInterval
import com.rockwellcollins.atc.agree.agree.ParamStatement
import com.rockwellcollins.atc.agree.agree.PeriodicStatement
import com.rockwellcollins.atc.agree.agree.PreExpr
import com.rockwellcollins.atc.agree.agree.PrevExpr
import com.rockwellcollins.atc.agree.agree.PropertyStatement
import com.rockwellcollins.atc.agree.agree.RealCast
import com.rockwellcollins.atc.agree.agree.RecordDef
import com.rockwellcollins.atc.agree.agree.RecordLitExpr
import com.rockwellcollins.atc.agree.agree.RecordUpdateExpr
import com.rockwellcollins.atc.agree.agree.SelectionExpr
import com.rockwellcollins.atc.agree.agree.SpecStatement
import com.rockwellcollins.atc.agree.agree.SporadicStatement
import com.rockwellcollins.atc.agree.agree.TagExpr
import com.rockwellcollins.atc.agree.agree.UnaryExpr
import com.rockwellcollins.atc.agree.agree.WhenHoldsStatement
import com.rockwellcollins.atc.agree.agree.WhenOccursStatment
import com.rockwellcollins.atc.agree.agree.WheneverBecomesTrueStatement
import com.rockwellcollins.atc.agree.agree.WheneverHoldsStatement
import com.rockwellcollins.atc.agree.agree.WheneverImpliesStatement
import com.rockwellcollins.atc.agree.agree.WheneverOccursStatement
import org.eclipse.xtext.formatting2.IFormattableDocument
import org.osate.xtext.aadl2.properties.formatting2.PropertiesFormatter

class AgreeFormatter extends PropertiesFormatter {
	
	// @Inject extension AgreeGrammarAccess

	def dispatch void format(AgreeContractLibrary agreecontractlibrary, extension IFormattableDocument document) {
		// TODO: format HiddenRegions around keywords, attributes, cross references, etc. 

		agreecontractlibrary.surround[noSpace]

		format(agreecontractlibrary.getContract(), document);
	}

	def dispatch void format(AgreeContractSubclause agreecontractsubclause, extension IFormattableDocument document) {
		// TODO: format HiddenRegions around keywords, attributes, cross references, etc. 

		agreecontractsubclause.surround[noSpace]

		format(agreecontractsubclause.getContract(), document);
	}

	def dispatch void format(AgreeContract agreecontract, extension IFormattableDocument document) {
		// TODO: format HiddenRegions around keywords, attributes, cross references, etc. 

		for (SpecStatement specs : agreecontract.getSpecs()) {
			specs.append[newLines=1]
			format(specs, document);
		}
	}

	def dispatch void format(InitialStatement initialstatement, extension IFormattableDocument document) {
		// TODO: format HiddenRegions around keywords, attributes, cross references, etc. 
		format(initialstatement.getExpr(), document);
	}

	def dispatch void format(ParamStatement paramstatement, extension IFormattableDocument document) {
		// TODO: format HiddenRegions around keywords, attributes, cross references, etc. 
		format(paramstatement.getExpr(), document);
		format(paramstatement.getType(), document);
	}

	def dispatch void format(ConnectionStatement connectionstatement, extension IFormattableDocument document) {
		// TODO: format HiddenRegions around keywords, attributes, cross references, etc. 
		format(connectionstatement.getExpr(), document);
	}

	def dispatch void format(AssumeStatement assumestatement, extension IFormattableDocument document) {
		// TODO: format HiddenRegions around keywords, attributes, cross references, etc. 

		assumestatement.getExpr().regionFor.keywords("(").forEach[surround[noSpace]]
		assumestatement.getExpr().regionFor.keywords(")").forEach[surround[noSpace]]

		assumestatement.regionFor.keyword(";").prepend[noSpace]

//		format(assumestatement.getExpr(), document);
		format(assumestatement.getPattern(), document);
	}

	def dispatch void format(GuaranteeStatement guaranteestatement, extension IFormattableDocument document) {
		// TODO: format HiddenRegions around keywords, attributes, cross references, etc. 
		
		guaranteestatement.getExpr().regionFor.keywords("(").forEach[surround[noSpace]]
		guaranteestatement.getExpr().regionFor.keywords(")").forEach[surround[noSpace]]
		
		guaranteestatement.regionFor.keyword(";").prepend[noSpace]
		
//		format(guaranteestatement.getExpr(), document);
		format(guaranteestatement.getPattern(), document);
	}

	def dispatch void format(AssertStatement assertstatement, extension IFormattableDocument document) {
		// TODO: format HiddenRegions around keywords, attributes, cross references, etc. 
		
		assertstatement.getExpr().regionFor.keywords("(").forEach[surround[noSpace]]
		assertstatement.getExpr().regionFor.keywords(")").forEach[surround[noSpace]]
		
		assertstatement.regionFor.keyword(";").prepend[noSpace]
		
//		format(assertstatement.getExpr(), document);
		format(assertstatement.getPattern(), document);
	}

	def dispatch void format(LemmaStatement lemmastatement, extension IFormattableDocument document) {
		// TODO: format HiddenRegions around keywords, attributes, cross references, etc. 
		
		lemmastatement.getExpr().regionFor.keywords("(").forEach[surround[noSpace]]
		lemmastatement.getExpr().regionFor.keywords(")").forEach[surround[noSpace]]
		
		lemmastatement.regionFor.keyword(";").prepend[noSpace]
		
//		format(lemmastatement.getExpr(), document);
		format(lemmastatement.getPattern(), document);
	}

	def dispatch void format(AlwaysStatement alwaysstatement, extension IFormattableDocument document) {
		// TODO: format HiddenRegions around keywords, attributes, cross references, etc. 
		format(alwaysstatement.getExpr(), document);
	}

	def dispatch void format(WhenHoldsStatement whenholdsstatement, extension IFormattableDocument document) {
		// TODO: format HiddenRegions around keywords, attributes, cross references, etc. 
		format(whenholdsstatement.getCondition(), document);
		format(whenholdsstatement.getConditionInterval(), document);
		format(whenholdsstatement.getEvent(), document);
		format(whenholdsstatement.getEventInterval(), document);
	}

	def dispatch void format(WhenOccursStatment whenoccursstatment, extension IFormattableDocument document) {
		// TODO: format HiddenRegions around keywords, attributes, cross references, etc. 
		format(whenoccursstatment.getCondition(), document);
		format(whenoccursstatment.getTimes(), document);
		format(whenoccursstatment.getInterval(), document);
		format(whenoccursstatment.getEvent(), document);
	}

	def dispatch void format(WheneverOccursStatement wheneveroccursstatement, extension IFormattableDocument document) {
		// TODO: format HiddenRegions around keywords, attributes, cross references, etc. 
		format(wheneveroccursstatement.getCause(), document);
		format(wheneveroccursstatement.getEffect(), document);
		format(wheneveroccursstatement.getInterval(), document);
	}

	def dispatch void format(WheneverBecomesTrueStatement wheneverbecomestruestatement, extension IFormattableDocument document) {
		// TODO: format HiddenRegions around keywords, attributes, cross references, etc. 
		format(wheneverbecomestruestatement.getCause(), document);
		format(wheneverbecomestruestatement.getEffect(), document);
		format(wheneverbecomestruestatement.getInterval(), document);
	}

	def dispatch void format(WheneverHoldsStatement wheneverholdsstatement, extension IFormattableDocument document) {
		// TODO: format HiddenRegions around keywords, attributes, cross references, etc. 
		format(wheneverholdsstatement.getCause(), document);
		format(wheneverholdsstatement.getEffect(), document);
		format(wheneverholdsstatement.getInterval(), document);
	}

	def dispatch void format(WheneverImpliesStatement wheneverimpliesstatement, extension IFormattableDocument document) {
		// TODO: format HiddenRegions around keywords, attributes, cross references, etc. 
		format(wheneverimpliesstatement.getCause(), document);
		format(wheneverimpliesstatement.getLhs(), document);
		format(wheneverimpliesstatement.getRhs(), document);
		format(wheneverimpliesstatement.getInterval(), document);
	}

	def dispatch void format(PeriodicStatement periodicstatement, extension IFormattableDocument document) {
		// TODO: format HiddenRegions around keywords, attributes, cross references, etc. 
		format(periodicstatement.getEvent(), document);
		format(periodicstatement.getPeriod(), document);
		format(periodicstatement.getJitter(), document);
	}

	def dispatch void format(SporadicStatement sporadicstatement, extension IFormattableDocument document) {
		// TODO: format HiddenRegions around keywords, attributes, cross references, etc. 
		format(sporadicstatement.getEvent(), document);
		format(sporadicstatement.getIat(), document);
		format(sporadicstatement.getJitter(), document);
	}

	def dispatch void format(ClosedTimeInterval closedtimeinterval, extension IFormattableDocument document) {
		// TODO: format HiddenRegions around keywords, attributes, cross references, etc. 
		format(closedtimeinterval.getLow(), document);
		format(closedtimeinterval.getHigh(), document);
	}

	def dispatch void format(OpenLeftTimeInterval openlefttimeinterval, extension IFormattableDocument document) {
		// TODO: format HiddenRegions around keywords, attributes, cross references, etc. 
		format(openlefttimeinterval.getLow(), document);
		format(openlefttimeinterval.getHigh(), document);
	}

	def dispatch void format(OpenRightTimeInterval openrighttimeinterval, extension IFormattableDocument document) {
		// TODO: format HiddenRegions around keywords, attributes, cross references, etc. 
		format(openrighttimeinterval.getLow(), document);
		format(openrighttimeinterval.getHigh(), document);
	}

	def dispatch void format(OpenTimeInterval opentimeinterval, extension IFormattableDocument document) {
		// TODO: format HiddenRegions around keywords, attributes, cross references, etc. 
		format(opentimeinterval.getLow(), document);
		format(opentimeinterval.getHigh(), document);
	}

	def dispatch void format(PropertyStatement propertystatement, extension IFormattableDocument document) {
		// TODO: format HiddenRegions around keywords, attributes, cross references, etc. 
		format(propertystatement.getExpr(), document);
	}

	def dispatch void format(ConstStatement conststatement, extension IFormattableDocument document) {
		// TODO: format HiddenRegions around keywords, attributes, cross references, etc. 
		format(conststatement.getType(), document);
		format(conststatement.getExpr(), document);
	}

	def dispatch void format(EqStatement eqstatement, extension IFormattableDocument document) {
		// TODO: format HiddenRegions around keywords, attributes, cross references, etc. 
		for (Arg lhs : eqstatement.getLhs()) {
			format(lhs, document);
		}
		format(eqstatement.getExpr(), document);
	}

	def dispatch void format(InputStatement inputstatement, extension IFormattableDocument document) {
		// TODO: format HiddenRegions around keywords, attributes, cross references, etc. 
		for (Arg lhs : inputstatement.getLhs()) {
			format(lhs, document);
		}
	}

	def dispatch void format(AssignStatement assignstatement, extension IFormattableDocument document) {
		// TODO: format HiddenRegions around keywords, attributes, cross references, etc. 
		format(assignstatement.getExpr(), document);
	}

	def dispatch void format(FnDef fndef, extension IFormattableDocument document) {
		// TODO: format HiddenRegions around keywords, attributes, cross references, etc. 
		for (Arg args : fndef.getArgs()) {
			format(args, document);
		}
		format(fndef.getType(), document);
		format(fndef.getExpr(), document);
	}

	def dispatch void format(LibraryFnDef libraryfndef, extension IFormattableDocument document) {
		// TODO: format HiddenRegions around keywords, attributes, cross references, etc. 
		for (Arg args : libraryfndef.getArgs()) {
			format(args, document);
		}
		format(libraryfndef.getType(), document);
	}

	def dispatch void format(LinearizationDef linearizationdef, extension IFormattableDocument document) {
		// TODO: format HiddenRegions around keywords, attributes, cross references, etc. 
		for (Arg args : linearizationdef.getArgs()) {
			format(args, document);
		}
		for (LinearizationInterval intervals : linearizationdef.getIntervals()) {
			format(intervals, document);
		}
		format(linearizationdef.getPrecision(), document);
		format(linearizationdef.getExprBody(), document);
	}

	def dispatch void format(LinearizationInterval linearizationinterval, extension IFormattableDocument document) {
		// TODO: format HiddenRegions around keywords, attributes, cross references, etc. 
		format(linearizationinterval.getStart(), document);
		format(linearizationinterval.getEnd(), document);
	}

	def dispatch void format(NodeDef nodedef, extension IFormattableDocument document) {
		// TODO: format HiddenRegions around keywords, attributes, cross references, etc. 
		for (Arg args : nodedef.getArgs()) {
			format(args, document);
		}
		for (Arg rets : nodedef.getRets()) {
			format(rets, document);
		}
		format(nodedef.getNodeBody(), document);
	}

	def dispatch void format(NodeBodyExpr nodebodyexpr, extension IFormattableDocument document) {
		// TODO: format HiddenRegions around keywords, attributes, cross references, etc. 
		for (Arg locs : nodebodyexpr.getLocs()) {
			format(locs, document);
		}
		for (NodeStmt stmts : nodebodyexpr.getStmts()) {
			format(stmts, document);
		}
	}

	def dispatch void format(NodeEq nodeeq, extension IFormattableDocument document) {
		// TODO: format HiddenRegions around keywords, attributes, cross references, etc. 
		format(nodeeq.getExpr(), document);
	}

	def dispatch void format(NodeLemma nodelemma, extension IFormattableDocument document) {
		// TODO: format HiddenRegions around keywords, attributes, cross references, etc. 
		format(nodelemma.getExpr(), document);
	}

	def dispatch void format(Arg arg, extension IFormattableDocument document) {
		// TODO: format HiddenRegions around keywords, attributes, cross references, etc. 
		format(arg.getType(), document);
	}

	def dispatch void format(RecordDef recorddef, extension IFormattableDocument document) {
		// TODO: format HiddenRegions around keywords, attributes, cross references, etc. 
		for (Arg args : recorddef.getArgs()) {
			format(args, document);
		}
	}

	def dispatch void format(EnumStatement enumstatement, extension IFormattableDocument document) {
		// TODO: format HiddenRegions around keywords, attributes, cross references, etc. 
		for (NamedID enums : enumstatement.getEnums()) {
			format(enums, document);
		}
	}

	def dispatch void format(ForallExpr forallexpr, extension IFormattableDocument document) {
		// TODO: format HiddenRegions around keywords, attributes, cross references, etc. 
		format(forallexpr.getBinding(), document);
		format(forallexpr.getArray(), document);
		format(forallexpr.getExpr(), document);
	}

	def dispatch void format(ExistsExpr existsexpr, extension IFormattableDocument document) {
		// TODO: format HiddenRegions around keywords, attributes, cross references, etc. 
		format(existsexpr.getBinding(), document);
		format(existsexpr.getArray(), document);
		format(existsexpr.getExpr(), document);
	}

	def dispatch void format(FlatmapExpr flatmapexpr, extension IFormattableDocument document) {
		// TODO: format HiddenRegions around keywords, attributes, cross references, etc. 
		format(flatmapexpr.getBinding(), document);
		format(flatmapexpr.getArray(), document);
		format(flatmapexpr.getExpr(), document);
	}

	def dispatch void format(FoldLeftExpr foldleftexpr, extension IFormattableDocument document) {
		// TODO: format HiddenRegions around keywords, attributes, cross references, etc. 
		format(foldleftexpr.getBinding(), document);
		format(foldleftexpr.getArray(), document);
		format(foldleftexpr.getAccumulator(), document);
		format(foldleftexpr.getInitial(), document);
		format(foldleftexpr.getExpr(), document);
	}

	def dispatch void format(FoldRightExpr foldrightexpr, extension IFormattableDocument document) {
		// TODO: format HiddenRegions around keywords, attributes, cross references, etc. 
		format(foldrightexpr.getBinding(), document);
		format(foldrightexpr.getArray(), document);
		format(foldrightexpr.getAccumulator(), document);
		format(foldrightexpr.getInitial(), document);
		format(foldrightexpr.getExpr(), document);
	}

	def dispatch void format(BinaryExpr binaryexpr, extension IFormattableDocument document) {
		// TODO: format HiddenRegions around keywords, attributes, cross references, etc. 
		format(binaryexpr.getRight(), document);
		format(binaryexpr.getLeft(), document);
	}

	def dispatch void format(UnaryExpr unaryexpr, extension IFormattableDocument document) {
		// TODO: format HiddenRegions around keywords, attributes, cross references, etc. 
		format(unaryexpr.getExpr(), document);
	}

	def dispatch void format(IfThenElseExpr ifthenelseexpr, extension IFormattableDocument document) {
		// TODO: format HiddenRegions around keywords, attributes, cross references, etc. 
		format(ifthenelseexpr.getA(), document);
		format(ifthenelseexpr.getB(), document);
		format(ifthenelseexpr.getC(), document);
	}

	def dispatch void format(PrevExpr prevexpr, extension IFormattableDocument document) {
		// TODO: format HiddenRegions around keywords, attributes, cross references, etc. 
		format(prevexpr.getDelay(), document);
		format(prevexpr.getInit(), document);
	}

	def dispatch void format(GetPropertyExpr getpropertyexpr, extension IFormattableDocument document) {
		// TODO: format HiddenRegions around keywords, attributes, cross references, etc. 
		format(getpropertyexpr.getComponentRef(), document);
	}

	def dispatch void format(ArrayUpdateExpr arrayupdateexpr, extension IFormattableDocument document) {
		// TODO: format HiddenRegions around keywords, attributes, cross references, etc. 
		for (Expr indices : arrayupdateexpr.getIndices()) {
			format(indices, document);
		}
		for (Expr valueExprs : arrayupdateexpr.getValueExprs()) {
			format(valueExprs, document);
		}
		format(arrayupdateexpr.getArray(), document);
	}

	def dispatch void format(RecordUpdateExpr recordupdateexpr, extension IFormattableDocument document) {
		// TODO: format HiddenRegions around keywords, attributes, cross references, etc. 
		format(recordupdateexpr.getExpr(), document);
		format(recordupdateexpr.getRecord(), document);
	}

	def dispatch void format(ArraySubExpr arraysubexpr, extension IFormattableDocument document) {
		// TODO: format HiddenRegions around keywords, attributes, cross references, etc. 
		format(arraysubexpr.getIndex(), document);
		format(arraysubexpr.getExpr(), document);
	}

	def dispatch void format(IndicesExpr indicesexpr, extension IFormattableDocument document) {
		// TODO: format HiddenRegions around keywords, attributes, cross references, etc. 
		format(indicesexpr.getArray(), document);
	}

	def dispatch void format(CallExpr callexpr, extension IFormattableDocument document) {
		// TODO: format HiddenRegions around keywords, attributes, cross references, etc. 
		format(callexpr.getRef(), document);
		for (Expr args : callexpr.getArgs()) {
			format(args, document);
		}
	}

	def dispatch void format(RecordLitExpr recordlitexpr, extension IFormattableDocument document) {
		// TODO: format HiddenRegions around keywords, attributes, cross references, etc. 
		format(recordlitexpr.getRecordType(), document);
		for (Expr argExpr : recordlitexpr.getArgExpr()) {
			format(argExpr, document);
		}
	}

	def dispatch void format(EnumLitExpr enumlitexpr, extension IFormattableDocument document) {
		// TODO: format HiddenRegions around keywords, attributes, cross references, etc. 
		format(enumlitexpr.getEnumType(), document);
	}

	def dispatch void format(PreExpr preexpr, extension IFormattableDocument document) {
		// TODO: format HiddenRegions around keywords, attributes, cross references, etc. 
		format(preexpr.getExpr(), document);
	}

	def dispatch void format(LatchedExpr latchedexpr, extension IFormattableDocument document) {
		// TODO: format HiddenRegions around keywords, attributes, cross references, etc. 
		format(latchedexpr.getExpr(), document);
	}

	def dispatch void format(BoolLitExpr boollitexpr, extension IFormattableDocument document) {
		// TODO: format HiddenRegions around keywords, attributes, cross references, etc. 
		format(boollitexpr.getVal(), document);
	}

	def dispatch void format(FloorCast floorcast, extension IFormattableDocument document) {
		// TODO: format HiddenRegions around keywords, attributes, cross references, etc. 
		format(floorcast.getExpr(), document);
	}

	def dispatch void format(RealCast realcast, extension IFormattableDocument document) {
		// TODO: format HiddenRegions around keywords, attributes, cross references, etc. 
		format(realcast.getExpr(), document);
	}

	def dispatch void format(ArrayLiteralExpr arrayliteralexpr, extension IFormattableDocument document) {
		// TODO: format HiddenRegions around keywords, attributes, cross references, etc. 
		for (Expr elems : arrayliteralexpr.getElems()) {
			format(elems, document);
		}
	}

	def dispatch void format(ArrayType arraytype, extension IFormattableDocument document) {
		// TODO: format HiddenRegions around keywords, attributes, cross references, etc. 
		format(arraytype.getStem(), document);
	}

	def dispatch void format(TagExpr tagexpr, extension IFormattableDocument document) {
		// TODO: format HiddenRegions around keywords, attributes, cross references, etc. 
		format(tagexpr.getStem(), document);
	}

	def dispatch void format(SelectionExpr selectionexpr, extension IFormattableDocument document) {
		// TODO: format HiddenRegions around keywords, attributes, cross references, etc. 
		format(selectionexpr.getTarget(), document);
	}
}
