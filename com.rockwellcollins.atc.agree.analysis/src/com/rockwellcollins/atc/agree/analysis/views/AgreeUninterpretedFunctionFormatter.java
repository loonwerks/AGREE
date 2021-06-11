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
package com.rockwellcollins.atc.agree.analysis.views;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

import jkind.lustre.values.RealValue;
import jkind.lustre.values.Value;
import jkind.results.Counterexample;
import jkind.results.FunctionTable;
import jkind.results.FunctionTableRow;

// copied from jkind.util/CounterexampleFormatter.java
public class AgreeUninterpretedFunctionFormatter {
	private static final int DECIMAL_DIGITS = 3;
	private static final String TRUNCATE_SUFFIX = "*";
	private static final String NEWLINE = System.lineSeparator();

	private final Counterexample cex;
	private boolean truncated = false;

	public AgreeUninterpretedFunctionFormatter(Counterexample cex) {
		this.cex = cex;
	}

	public String functions() {
		if (cex.getFunctionTables().isEmpty()) {
			return "";
		}

		List<FunctionTable> functionTables = cex.getFunctionTables();
		StringBuilder sb = new StringBuilder();

		sb.append(NEWLINE);
		sb.append("FUNCTIONS");
		for (FunctionTable table : functionTables) {
			if (!table.getRows().isEmpty()) {
				sb.append(NEWLINE);
				sb.append(formatFunctionTable(table));
			}
		}
		return sb.toString() + footer();
	}

	private String footer() {
		if (truncated) {
			return NEWLINE + " * display value has been truncated" + NEWLINE;
		} else {
			return "";
		}
	}

	private String getString(Value value) {
		if (value == null) {
			return "-";
		} else if (value instanceof RealValue) {
			RealValue rv = (RealValue) value;
			String text = rv.value.toTruncatedDecimal(DECIMAL_DIGITS, TRUNCATE_SUFFIX);
			if (text.contains(TRUNCATE_SUFFIX)) {
				truncated = true;
			}
			return text;
		} else {
			return value.toString();
		}
	}

	private String formatFunctionTable(FunctionTable table) {
		List<List<String>> content = new ArrayList<>();

		content.add(getFunctionHeader(table));
		for (FunctionTableRow row : table.getRows()) {
			List<String> line = new ArrayList<>();
			row.getInputs().forEach(val -> line.add(getString(val)));
			line.add("|");
			line.add(getString(row.getOutput()));
			content.add(line);
		}

		return formatContent(content, this::getFunctionTableFormatString, true);
	}

	private List<String> getFunctionHeader(FunctionTable table) {
		List<String> header = new ArrayList<>();
		table.getInputs().forEach(vd -> header.add(vd.id));
		header.add("|");
		header.add(table.getName());
		return header;
	}

	private String formatContent(List<List<String>> content, BiFunction<Integer, Integer, String> formatString,
			boolean headerLine) {
		List<Integer> minColumnWidths = computeMinimumColumnWidths(content);

		StringBuilder text = new StringBuilder();
		for (int row = 0; row < content.size(); row++) {
			List<String> rowContent = content.get(row);
			for (int col = 0; col < rowContent.size(); col++) {
				String cell = rowContent.get(col);
				String format = formatString.apply(col, minColumnWidths.get(col));
				text.append(String.format(format, cell));
			}
			text.append(NEWLINE);

			if (row == 0 && headerLine) {
				String header = text.toString().replaceAll("\\s+$", "");
				text.append(makeHeaderLine(header));
				text.append(NEWLINE);
			}
		}

		return text.toString();
	}

	@SuppressWarnings("unused")
	private String getFunctionTableFormatString(int col, int minWidth) {
		return "%" + minWidth + "s  ";
	}

	private List<Integer> computeMinimumColumnWidths(List<List<String>> content) {
		List<Integer> result = new ArrayList<>();
		for (List<String> row : content) {
			for (int i = 0; i < row.size(); i++) {
				expandToFitElement(result, i);
				int curr = result.get(i);
				int next = Math.max(row.get(i).length(), curr);
				result.set(i, next);
			}
		}
		return result;
	}

	private void expandToFitElement(List<Integer> result, int i) {
		while (result.size() - 1 < i) {
			result.add(0);
		}
	}

	private String makeHeaderLine(String header) {
		return header.replace("|", "+").replaceAll("[^+]", "-");
	}
}
