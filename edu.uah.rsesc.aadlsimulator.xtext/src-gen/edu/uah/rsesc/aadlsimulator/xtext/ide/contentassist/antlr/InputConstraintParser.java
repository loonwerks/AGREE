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
 *
 * Generated by Xtext version 2.20.0.
 */

package edu.uah.rsesc.aadlsimulator.xtext.ide.contentassist.antlr;

import com.google.common.collect.ImmutableMap;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import edu.uah.rsesc.aadlsimulator.xtext.ide.contentassist.antlr.internal.InternalInputConstraintParser;
import edu.uah.rsesc.aadlsimulator.xtext.services.InputConstraintGrammarAccess;
import java.util.Map;
import org.eclipse.xtext.AbstractElement;
import org.eclipse.xtext.ide.editor.contentassist.antlr.AbstractContentAssistParser;

public class InputConstraintParser extends AbstractContentAssistParser {

	@Singleton
	public static final class NameMappings {
		
		private final Map<AbstractElement, String> mappings;
		
		@Inject
		public NameMappings(InputConstraintGrammarAccess grammarAccess) {
			ImmutableMap.Builder<AbstractElement, String> builder = ImmutableMap.builder();
			init(builder, grammarAccess);
			this.mappings = builder.build();
		}
		
		public String getRuleName(AbstractElement element) {
			return mappings.get(element);
		}
		
		private static void init(ImmutableMap.Builder<AbstractElement, String> builder, InputConstraintGrammarAccess grammarAccess) {
			builder.put(grammarAccess.getExprAccess().getAlternatives(), "rule__Expr__Alternatives");
			builder.put(grammarAccess.getFunctionAccess().getAlternatives(), "rule__Function__Alternatives");
			builder.put(grammarAccess.getRandomAccess().getAlternatives(), "rule__Random__Alternatives");
			builder.put(grammarAccess.getRefAccess().getAlternatives(), "rule__Ref__Alternatives");
			builder.put(grammarAccess.getPrimaryAccess().getAlternatives(), "rule__Primary__Alternatives");
			builder.put(grammarAccess.getIntervalAccess().getAlternatives_1(), "rule__Interval__Alternatives_1");
			builder.put(grammarAccess.getIntervalAccess().getAlternatives_5(), "rule__Interval__Alternatives_5");
			builder.put(grammarAccess.getBooleanLiteralAccess().getAlternatives(), "rule__BooleanLiteral__Alternatives");
			builder.put(grammarAccess.getOperatorAccess().getAlternatives(), "rule__Operator__Alternatives");
			builder.put(grammarAccess.getAddSubOperatorAccess().getAlternatives(), "rule__AddSubOperator__Alternatives");
			builder.put(grammarAccess.getMultDivOperatorAccess().getAlternatives(), "rule__MultDivOperator__Alternatives");
			builder.put(grammarAccess.getAddSubAccess().getGroup(), "rule__AddSub__Group__0");
			builder.put(grammarAccess.getAddSubAccess().getGroup_1(), "rule__AddSub__Group_1__0");
			builder.put(grammarAccess.getMultDivAccess().getGroup(), "rule__MultDiv__Group__0");
			builder.put(grammarAccess.getMultDivAccess().getGroup_1(), "rule__MultDiv__Group_1__0");
			builder.put(grammarAccess.getPreAccess().getGroup(), "rule__Pre__Group__0");
			builder.put(grammarAccess.getRandomIntegerAccess().getGroup(), "rule__RandomInteger__Group__0");
			builder.put(grammarAccess.getRandomRealAccess().getGroup(), "rule__RandomReal__Group__0");
			builder.put(grammarAccess.getRandomElementAccess().getGroup(), "rule__RandomElement__Group__0");
			builder.put(grammarAccess.getElementRefAccess().getGroup(), "rule__ElementRef__Group__0");
			builder.put(grammarAccess.getElementRefAccess().getGroup_2(), "rule__ElementRef__Group_2__0");
			builder.put(grammarAccess.getConstRefAccess().getGroup(), "rule__ConstRef__Group__0");
			builder.put(grammarAccess.getConstRefAccess().getGroup_1(), "rule__ConstRef__Group_1__0");
			builder.put(grammarAccess.getNegativeAccess().getGroup(), "rule__Negative__Group__0");
			builder.put(grammarAccess.getPrimaryAccess().getGroup_3(), "rule__Primary__Group_3__0");
			builder.put(grammarAccess.getIntervalAccess().getGroup(), "rule__Interval__Group__0");
			builder.put(grammarAccess.getSetAccess().getGroup(), "rule__Set__Group__0");
			builder.put(grammarAccess.getSetAccess().getGroup_2(), "rule__Set__Group_2__0");
			builder.put(grammarAccess.getSetAccess().getGroup_2_1(), "rule__Set__Group_2_1__0");
			builder.put(grammarAccess.getAddSubAccess().getOpAssignment_1_1(), "rule__AddSub__OpAssignment_1_1");
			builder.put(grammarAccess.getAddSubAccess().getRightAssignment_1_2(), "rule__AddSub__RightAssignment_1_2");
			builder.put(grammarAccess.getMultDivAccess().getOpAssignment_1_1(), "rule__MultDiv__OpAssignment_1_1");
			builder.put(grammarAccess.getMultDivAccess().getRightAssignment_1_2(), "rule__MultDiv__RightAssignment_1_2");
			builder.put(grammarAccess.getPreAccess().getRefAssignment_3(), "rule__Pre__RefAssignment_3");
			builder.put(grammarAccess.getRandomIntegerAccess().getIntervalAssignment_1(), "rule__RandomInteger__IntervalAssignment_1");
			builder.put(grammarAccess.getRandomRealAccess().getIntervalAssignment_1(), "rule__RandomReal__IntervalAssignment_1");
			builder.put(grammarAccess.getRandomElementAccess().getSetAssignment_1(), "rule__RandomElement__SetAssignment_1");
			builder.put(grammarAccess.getElementRefAccess().getIdsAssignment_1(), "rule__ElementRef__IdsAssignment_1");
			builder.put(grammarAccess.getElementRefAccess().getIdsAssignment_2_1(), "rule__ElementRef__IdsAssignment_2_1");
			builder.put(grammarAccess.getConstRefAccess().getPackageSegmentsAssignment_1_0(), "rule__ConstRef__PackageSegmentsAssignment_1_0");
			builder.put(grammarAccess.getConstRefAccess().getConstantNameAssignment_2(), "rule__ConstRef__ConstantNameAssignment_2");
			builder.put(grammarAccess.getNegativeAccess().getValueAssignment_2(), "rule__Negative__ValueAssignment_2");
			builder.put(grammarAccess.getIntervalAccess().getLeftClosedAssignment_1_0(), "rule__Interval__LeftClosedAssignment_1_0");
			builder.put(grammarAccess.getIntervalAccess().getLeftAssignment_2(), "rule__Interval__LeftAssignment_2");
			builder.put(grammarAccess.getIntervalAccess().getRightAssignment_4(), "rule__Interval__RightAssignment_4");
			builder.put(grammarAccess.getIntervalAccess().getRightClosedAssignment_5_0(), "rule__Interval__RightClosedAssignment_5_0");
			builder.put(grammarAccess.getSetAccess().getMembersAssignment_2_0(), "rule__Set__MembersAssignment_2_0");
			builder.put(grammarAccess.getSetAccess().getMembersAssignment_2_1_1(), "rule__Set__MembersAssignment_2_1_1");
			builder.put(grammarAccess.getIntegerLiteralAccess().getValueAssignment(), "rule__IntegerLiteral__ValueAssignment");
			builder.put(grammarAccess.getRealLiteralAccess().getValueAssignment(), "rule__RealLiteral__ValueAssignment");
			builder.put(grammarAccess.getBooleanLiteralAccess().getValueAssignment_0(), "rule__BooleanLiteral__ValueAssignment_0");
			builder.put(grammarAccess.getBooleanLiteralAccess().getValueAssignment_1(), "rule__BooleanLiteral__ValueAssignment_1");
		}
	}
	
	@Inject
	private NameMappings nameMappings;

	@Inject
	private InputConstraintGrammarAccess grammarAccess;

	@Override
	protected InternalInputConstraintParser createParser() {
		InternalInputConstraintParser result = new InternalInputConstraintParser(null);
		result.setGrammarAccess(grammarAccess);
		return result;
	}

	@Override
	protected String getRuleName(AbstractElement element) {
		return nameMappings.getRuleName(element);
	}

	@Override
	protected String[] getInitialHiddenTokens() {
		return new String[] { "RULE_WS", "RULE_ML_COMMENT", "RULE_SL_COMMENT" };
	}

	public InputConstraintGrammarAccess getGrammarAccess() {
		return this.grammarAccess;
	}

	public void setGrammarAccess(InputConstraintGrammarAccess grammarAccess) {
		this.grammarAccess = grammarAccess;
	}
	
	public NameMappings getNameMappings() {
		return nameMappings;
	}
	
	public void setNameMappings(NameMappings nameMappings) {
		this.nameMappings = nameMappings;
	}
}
