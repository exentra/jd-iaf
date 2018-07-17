package com.deere.tools.xml;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.joda.time.DateTimeZone.UTC;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;
import org.junit.Test;

public class XsdDateTimeAdapterTest {

	@Test
	public void testMarshalSuccessRegion2() throws Exception {
		final String expectedDate = "1970-01-02T11:17:36Z";
		final Date date = createTestDate("1970-01-02T12:17:36+01:00");

		final String result = new XsdDateTimeAdapter().marshal(date);

		assertThat(result, is(expectedDate));
	}

	@Test
	public void testMarshalSuccessRegion3() throws Exception {
		final String expectedDate = "1970-01-02T11:17:36Z";
		final Date date = createTestDate("1970-01-02T14:17:36+03:00");

		final String result = new XsdDateTimeAdapter().marshal(date);

		assertThat(result, is(expectedDate));
	}

	@Test
	public void testMarshalNull() throws Exception {

		final String result = new XsdDateTimeAdapter().marshal(null);

		assertThat(result, is(nullValue()));
	}

	@Test
	public void testUnmarshalSuccessRegion2() throws Exception {
		final Date expectedDate = createTestDate("1970-01-02T11:17:36Z");
		final String date = "1970-01-02T12:17:36+01:00";

		final Date result = new XsdDateTimeAdapter().unmarshal(date);

		assertThat(result, is(expectedDate));
	}

	@Test
	public void testUnmarshalSuccessRegion3() throws Exception {
		final Date expectedDate = createTestDate("1970-01-02T11:17:36Z");
		final String date = "1970-01-02T14:17:36+03:00";

		final Date result = new XsdDateTimeAdapter().unmarshal(date);

		assertThat(result, is(expectedDate));
	}

	@Test
	public void testUnmarshalNull() throws Exception {

		final Date result = new XsdDateTimeAdapter().unmarshal(null);

		assertThat(result, is(nullValue()));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testUnmarshalInvalid() throws Exception {
		final String invalidDate = "not a date";

		new XsdDateTimeAdapter().unmarshal(invalidDate);

		fail();
	}

	private static Date createTestDate(final String date) {
		return DateTime.parse(date, ISODateTimeFormat.dateTimeParser().withOffsetParsed()).toDateTime(UTC).toDate();
	}

}
