package com.deere.tools.xml;

import static org.joda.time.DateTimeZone.UTC;

import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;

/**
 * Register in bindings, e.g.
 * <xjc:javaType name="java.util.Date" xmlType="xs:dateTime" adapter=
 * "com.deere.b2b.converter.XsdDateTimeAdapter" />
 *
 */
public class XsdDateTimeAdapter extends NullsafeXmlAdapter<String, Date> {

	@Override
	protected Date unmarshalInternal(final String date) throws Exception {
		return DateTime.parse(date).toDateTime(UTC).toDate();
	}

	@Override
	protected String marshalInternal(final Date date) throws Exception {
		return new DateTime(date).toDateTime(UTC).toString(ISODateTimeFormat.dateTimeNoMillis().withZoneUTC());
	}

}