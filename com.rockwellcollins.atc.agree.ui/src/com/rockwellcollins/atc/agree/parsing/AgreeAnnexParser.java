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
package com.rockwellcollins.atc.agree.parsing;

import org.osate.aadl2.AnnexLibrary;
import org.osate.aadl2.AnnexSubclause;
import org.osate.aadl2.modelsupport.errorreporting.NullParseErrorReporter;
import org.osate.aadl2.modelsupport.errorreporting.ParseErrorReporter;
import org.osate.annexsupport.AnnexParseUtil;
import org.osate.annexsupport.AnnexParser;

import com.google.inject.Injector;
import com.rockwellcollins.atc.agree.agree.NamedSpecStatement;
import com.rockwellcollins.atc.agree.parser.antlr.AgreeParser;
import com.rockwellcollins.atc.agree.services.AgreeGrammarAccess;
import com.rockwellcollins.atc.agree.ui.internal.AgreeActivator;

// Based on EMV2AnnexParser from Error Model annex
public class AgreeAnnexParser implements AnnexParser {
	private AgreeParser parser;

	protected AgreeParser getParser() {
		if (parser == null) {
			Injector injector = AgreeActivator.getInstance()
					.getInjector(AgreeActivator.COM_ROCKWELLCOLLINS_ATC_AGREE_AGREE);
			parser = injector.getInstance(AgreeParser.class);
		}
		return parser;
	}

	protected AgreeGrammarAccess getGrammarAccess() {
		return getParser().getGrammarAccess();
	}

	@Override
	public AnnexLibrary parseAnnexLibrary(String annexName, String source, String filename, int line, int column,
			ParseErrorReporter errReporter) {
		return (AnnexLibrary) AnnexParseUtil.parse(getParser(), source, getGrammarAccess().getAgreeLibraryRule(),
				filename, line, column, errReporter);
	}

	@Override
	public AnnexSubclause parseAnnexSubclause(String annexName, String source, String filename, int line, int column,
			ParseErrorReporter errReporter) {
		return (AnnexSubclause) AnnexParseUtil.parse(getParser(), source, getGrammarAccess().getAgreeSubclauseRule(),
				filename, line, column, errReporter);
	}

	public NamedSpecStatement parseNamedSpecStatement(String namedSpecStatement) {
		return (NamedSpecStatement) AnnexParseUtil.parse(getParser(), namedSpecStatement,
				getGrammarAccess().getNamedSpecStatementRule(), "", 0, 0, NullParseErrorReporter.prototype);
	}

	/**
	 * @since 3.0
	 */
	@Override
	public String getFileExtension() {
		return "agree";
	}

}