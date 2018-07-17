package com.deere.tools.xml;

import java.util.Date;

import org.joda.time.LocalDate;
import org.joda.time.format.ISODateTimeFormat;

public class XsdDateAdapter extends NullsafeXmlAdapter<String, Date> {

	@Override
	protected Date unmarshalInternal(final String date) {
		return LocalDate.parse(date).toDate();
	}

	@Override
	protected String marshalInternal(final Date date) {
		return new LocalDate(date).toString(ISODateTimeFormat.date());
	}

}