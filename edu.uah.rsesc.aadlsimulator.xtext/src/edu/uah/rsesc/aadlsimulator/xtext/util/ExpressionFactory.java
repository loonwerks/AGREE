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
package edu.uah.rsesc.aadlsimulator.xtext.util;

import java.math.BigDecimal;
import java.math.BigInteger;

import edu.uah.rsesc.aadlsimulator.xtext.inputConstraint.BinaryExpression;
import edu.uah.rsesc.aadlsimulator.xtext.inputConstraint.InputConstraintFactory;
import edu.uah.rsesc.aadlsimulator.xtext.inputConstraint.NegativeExpression;
import edu.uah.rsesc.aadlsimulator.xtext.inputConstraint.Operator;
import edu.uah.rsesc.aadlsimulator.xtext.inputConstraint.RealLiteral;
import edu.uah.rsesc.aadlsimulator.xtext.inputConstraint.ScalarExpression;

public class ExpressionFactory {
	public static ScalarExpression createReal(final BigInteger value) {
		final boolean isNegative = value.signum() < 0;
		BigDecimal decValue = new BigDecimal(value);

		// Negative values arnen't allowed
		if(isNegative) {
			decValue = decValue.negate();
		}

		// Adjust scale. Decimal values with a scale less than 1 do not serialize properly.
		if(decValue.scale() < 1) {
			decValue = decValue.setScale(1);
		}

		final RealLiteral realLiteral = InputConstraintFactory.eINSTANCE.createRealLiteral();
		realLiteral.setValue(decValue);

		final ScalarExpression scalarExpression;
		if(isNegative) {
			final NegativeExpression negExpr = InputConstraintFactory.eINSTANCE.createNegativeExpression();
			negExpr.setValue(realLiteral);
			scalarExpression = negExpr;
		} else {
			scalarExpression = realLiteral;
		}

		return scalarExpression;
	}

	public static BinaryExpression createFraction(final BigInteger numerator, final BigInteger denominator) {
		final BinaryExpression ic = InputConstraintFactory.eINSTANCE.createBinaryExpression();
		ic.setOp(Operator.DIVISION);

		ic.setLeft(createReal(numerator));
		ic.setRight(createReal(denominator));

		return ic;
	}
}
