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
package edu.uah.rsesc.aadlsimulator.agree.engine;

import java.util.Objects;

import edu.uah.rsesc.aadlsimulator.xtext.inputConstraint.ConstRefExpression;
import edu.uah.rsesc.aadlsimulator.xtext.inputConstraint.ElementRefExpression;
import jkind.lustre.Expr;
import jkind.lustre.IdExpr;
import jkind.lustre.values.Value;

public class SimulationStateReferenceEvaluator implements ReferenceEvaluator {
	public static interface SimulationStateProvider {
		AGREESimulationState getSimulationState();
	}

	private final SimulationStateProvider simStateProvider;

	public SimulationStateReferenceEvaluator(final SimulationStateProvider simStateProvider) {
		this.simStateProvider = Objects.requireNonNull(simStateProvider, "simStateProvider must not be null");
	}

	@Override
	public Expr getLustreExpression(final ElementRefExpression ref) {
		final AGREESimulationState simulationState = simStateProvider.getSimulationState();
		final String reference = String.join(".", ref.getIds());
		return new IdExpr(simulationState.getElementLustreId(simulationState.findElementByPath(reference)));
	}

	@Override
	public Value getLustreValue(final ElementRefExpression ref, int preLevel) {
		final AGREESimulationState simulationState = simStateProvider.getSimulationState();
		final int frameIndex = simulationState.getNumberOfFrames() - preLevel;
		if(frameIndex < 0) {
			throw new RuntimeException("Invalid value for preLevel: " + preLevel);
		}

		if(frameIndex >= simulationState.getNumberOfFrames()) {
			throw new RuntimeException("Unable to retrieve value for step: " + (frameIndex+1));
		}

		final String reference = String.join(".", ref.getIds());
		return simulationState.getElementLustreValue(frameIndex, simulationState.findElementByPath(reference));
	}

	@Override
	public Value getLustreValue(final ConstRefExpression ref, int preLevel) {
		final AGREESimulationState simulationState = simStateProvider.getSimulationState();
		final int frameIndex = simulationState.getNumberOfFrames() - preLevel;
		if(frameIndex < 0) {
			throw new RuntimeException("Invalid value for preLevel: " + preLevel);
		}

		if(frameIndex > simulationState.getNumberOfFrames()) {
			throw new RuntimeException("Unable to retrieve value for step: " + (frameIndex+1));
		}

		final String constantId = String.join("::", ref.getPackageSegments()) + "::" + ref.getConstantName();
		return simulationState.getConstantLustreValue(frameIndex, simulationState.findConstantById(constantId));
	}

}
