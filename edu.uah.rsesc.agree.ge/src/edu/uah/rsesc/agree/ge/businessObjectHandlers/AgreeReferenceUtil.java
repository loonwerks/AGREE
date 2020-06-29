package edu.uah.rsesc.agree.ge.businessObjectHandlers;

import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.eclipse.xtext.util.Strings;
import org.osate.aadl2.Element;
import org.osate.ge.CanonicalBusinessObjectReference;
import org.osate.ge.RelativeBusinessObjectReference;
import org.osate.ge.services.ReferenceBuilderService;

import com.rockwellcollins.atc.agree.agree.AssertStatement;
import com.rockwellcollins.atc.agree.agree.AssumeStatement;
import com.rockwellcollins.atc.agree.agree.ConstStatement;
import com.rockwellcollins.atc.agree.agree.EnumStatement;
import com.rockwellcollins.atc.agree.agree.EqStatement;
import com.rockwellcollins.atc.agree.agree.GuaranteeStatement;
import com.rockwellcollins.atc.agree.agree.LemmaStatement;
import com.rockwellcollins.atc.agree.agree.LinearizationDef;
import com.rockwellcollins.atc.agree.agree.NodeDef;
import com.rockwellcollins.atc.agree.agree.PropertyStatement;
import com.rockwellcollins.atc.agree.agree.RecordDef;
import com.rockwellcollins.atc.agree.agree.SpecStatement;

import edu.uah.rsesc.agree.ge.AgreeBusinessObjectProvider;
import edu.uah.rsesc.agree.ge.AgreeGeUtil;

/**
 * This class builds unique references for AGREE statements. However, many AGREE statements do not have unique names. In such cases an index is used to differentiate between the elements.
 * This can result in problems when removing elements with the graphical editor.
 * For example, when deleting a model statement which have other statements with the same name, the incorrect graphical shape may be removed.
 * However, the correct model element will be modified.
 * Unfortunately, the use of indices is the best referencing scheme at the moment.
 *
 */
// NOTE: Performance. Need to get all specs to get proper references. This will likely have an affect on performance.
class AgreeReferenceUtil {
	private final static String AGREE_REFERENCE_PREFIX = "agree.";

	enum ReferenceableType {
		// @formatter:off
		ASSERT_STATEMENT("assertion", AssertStatement.class, s -> s.getStr()),
		ASSUME_STATEMENT("assumption", AssumeStatement.class, s -> s.getStr()),
		CONST_STATEMENT("constant", ConstStatement.class, s -> s.getName()),
		ENUM_STATEMENT("enum", EnumStatement.class, s -> s.getName()),
		EQ_STATEMENT("eq", EqStatement.class, s -> s.getLhs().stream().map(a -> Strings.emptyIfNull(a.getName())).collect(Collectors.joining(" "))),
		GUARANTEE_STATEMENT("guarantee", GuaranteeStatement.class, s -> s.getStr()),
		LEMMA_STATEMENT("lemma", LemmaStatement.class, s -> s.getStr()),
		LINEARIZATION_DEFINITION("linearization", LinearizationDef.class, s -> s.getName()),
		NODE_DEF_EXPRESSION("node_def", NodeDef.class, s -> s.getName()),
		PROPERTY_STATEMENT("property", PropertyStatement.class, s -> s.getName()),
		RECORD_DEF_EXPRESSION("record_def", RecordDef.class, s -> s.getName());

		public final String id;
		public final Class<?> boClass;
		public final Function<?, String> nameProvider;

		// @formatter:on
		<T> ReferenceableType(final String typeSuffix, final Class<T> boClass, final Function<T, String> nameProvider) {
			this.id = AGREE_REFERENCE_PREFIX + typeSuffix;
			this.boClass = boClass;
			this.nameProvider = nameProvider;
		}
	}

	public static RelativeBusinessObjectReference getRelativeReference(final Object bo) {
		return generateReference(bo, new SpecStatementBuilder() {
			@Override
			public <T extends SpecStatement> String[] generateReference(String type, Object bo, Class<T> boClass,
					Function<T, String> nameProvider) {
				return getRelativeReference(type, bo, boClass, nameProvider);
			}
		}).map(RelativeBusinessObjectReference::new).orElse(null);
	}

	private static <T extends SpecStatement> String[] getRelativeReference(final String type,
			final Object bo,
			final Class<T> boClass, final Function<T, String> nameProvider) {
		if (boClass.isInstance(bo)) {
			final T s = boClass.cast(bo);
			final String name = nameProvider.apply(s);

			final int index = getIndex(s, boClass, nameProvider);
			return new String[] { type, name, Integer.toString(index) };
		}

		return null;
	}

	public static CanonicalBusinessObjectReference getCanonicalReference(
			final Object bo,
			final ReferenceBuilderService refBuilder) {
		return generateReference(bo, new SpecStatementBuilder() {
			@Override
			public <T extends SpecStatement> String[] generateReference(String type, Object bo, Class<T> boClass,
					Function<T, String> nameProvider) {
				return getCanonicalReference(type, bo, boClass, nameProvider, refBuilder);
			}
		}).map(CanonicalBusinessObjectReference::new).orElse(null);
	}

	private static <T extends SpecStatement> String[] getCanonicalReference(final String type,
			final Object bo,
			final Class<T> boClass, final Function<T, String> nameProvider, final ReferenceBuilderService refBuilder) {
		if (boClass.isInstance(bo)) {
			final T s = boClass.cast(bo);
			final String name = nameProvider.apply(s);

			final int index = getIndex(s, boClass, nameProvider);
			return new String[] { type,
					refBuilder.getCanonicalReference(AgreeGeUtil.getPackageOrClassifier(s)).encode(),
					name,
					Integer.toString(index) };
		}

		return null;
	}

	private interface SpecStatementBuilder {
		<T extends SpecStatement> String[] generateReference(final String type, final Object bo, final Class<T> boClass,
				final Function<T, String> nameProvider);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static Optional<String[]> generateReference(final Object bo,
			final SpecStatementBuilder specStatementRefBuilder) {
		String[] ref;

		for (final ReferenceableType rt : ReferenceableType.values()) {
			ref = specStatementRefBuilder.generateReference(rt.id, bo, (Class) rt.boClass,
					(Function) rt.nameProvider);
			if (ref != null) {
				return Optional.of(ref);
			}
		}

		return Optional.empty();
	}

	private static <T extends SpecStatement> int getIndex(final T bo,
			final Class<T> boClass,
			final Function<T, String> nameProvider) {
		final String name = nameProvider.apply(bo);
		if (name == null) {
			return 0;
		}

		int index = 0;
		if (name != null) {
			final Element owner = AgreeGeUtil.getPackageOrClassifier(bo);
			if (owner != null) {
				// Find the index for the element.
				for (final T ss : (Iterable<T>) AgreeBusinessObjectProvider.getAllSpecStatements(owner)
						.filter(boClass::isInstance)
						.map(boClass::cast)
						.filter(other -> other == bo || name.equals(nameProvider.apply(other)))::iterator) {
					if (bo == ss) {
						break;
					} else {
						index++;
					}
				}
			}
		}

		return index;
	}
}
