package com.deere.tools.xml;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * Adapter handling repetitive null checks, so that each subclass can assume, that the supplied value is not null.
 *
 * @param <VALUE_TYPE>
 *            of adapter
 * @param <BOUND_TYPE>
 *            of adapter
 */
public abstract class NullsafeXmlAdapter<VALUE_TYPE, BOUND_TYPE> extends XmlAdapter<VALUE_TYPE, BOUND_TYPE> {

	@Override
	public final BOUND_TYPE unmarshal(final VALUE_TYPE value) throws Exception {
		if (value == null) {
			return null;
		}

		return unmarshalInternal(value);
	}

	@Override
	public final VALUE_TYPE marshal(final BOUND_TYPE value) throws Exception {
		if (value == null) {
			return null;
		}

		return marshalInternal(value);
	}

	protected abstract BOUND_TYPE unmarshalInternal(VALUE_TYPE value) throws Exception;

	protected abstract VALUE_TYPE marshalInternal(BOUND_TYPE value) throws Exception;

}
