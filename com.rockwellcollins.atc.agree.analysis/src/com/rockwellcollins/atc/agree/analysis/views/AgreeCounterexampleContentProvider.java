package com.rockwellcollins.atc.agree.analysis.views;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.eclipse.emf.ecore.EObject;

import jkind.api.ui.counterexample.CategoryHeader;
import jkind.api.ui.counterexample.CounterexampleContentProvider;
import jkind.api.ui.counterexample.SignalGrouper;
import jkind.api.ui.counterexample.Spacer;
import jkind.lustre.values.Value;
import jkind.results.Counterexample;
import jkind.results.Signal;
import jkind.results.layout.Layout;

public class AgreeCounterexampleContentProvider extends CounterexampleContentProvider {

	private final Layout layout;
	private final Map<String, EObject> refMap;

	public AgreeCounterexampleContentProvider(Layout layout, Map<String, EObject> refMap) {
		super(layout);
		this.layout = layout;
		this.refMap = refMap;
	}

	@Override
	public Object[] getElements(Object inputElement) {
		Counterexample cex = (Counterexample) inputElement;
		List<Object> result = new ArrayList<>();

		boolean first = true;
		for (String category : layout.getCategories()) {
			List<Signal<Value>> signals = cex.getCategorySignals(layout, category);
			if (!signals.isEmpty()) {
				if (first) {
					first = false;
				} else {
					result.add(new Spacer());
				}
				result.add(new CategoryHeader(category));
				List<Signal<Value>> inputSignals = signals.stream().filter(it -> {
					EObject ref = refMap.get(it.getName());
					return (ref instanceof org.osate.aadl2.Port) && ((org.osate.aadl2.Port) ref).isIn();
				}).collect(Collectors.toList());
				List<Signal<Value>> outputSignals = signals.stream().filter(it -> {
					EObject ref = refMap.get(it.getName());
					return (ref instanceof org.osate.aadl2.Port) && ((org.osate.aadl2.Port) ref).isOut();
				}).collect(Collectors.toList());
				List<Signal<Value>> otherSignals = signals.stream()
						.filter(it -> !(inputSignals.contains(it) || outputSignals.contains(it)))
						.collect(Collectors.toList());
				result.addAll(SignalGrouper.group(null, Stream.of(inputSignals, otherSignals, outputSignals)
						.flatMap(Collection::stream).collect(Collectors.toList())));
			}
		}

		return result.toArray();
	}

}
