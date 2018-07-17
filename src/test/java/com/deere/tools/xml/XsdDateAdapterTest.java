package com.deere.tools.xml;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.util.Date;

import org.joda.time.LocalDate;
import org.junit.Test;

public class XsdDateAdapterTest {

	@Test
	public void testMarshalSuccess() throws Exception {
		final String expectedDate = "1970-01-03";
		final Date date = createTestDate("1970-01-03");

		final String result = new XsdDateAdapter().marshal(date);

		assertThat(result, is(expectedDate));
	}

	@Test
	public void testMarshalNull() throws Exception {

		final String result = new XsdDateAdapter().marshal(null);

		assertThat(result, is(nullValue()));
	}

	@Test
	public void testUnmarshalSuccess() throws Exception {
		final Date expectedDate = createTestDate("1970-01-02");
		final String date = "1970-01-02";

		final Date result = new XsdDateAdapter().unmarshal(date);

		assertThat(result, is(expectedDate));
	}

	@Test
	public void testUnmarshalNull() throws Exception {

		final Date result = new XsdDateAdapter().unmarshal(null);

		assertThat(result, is(nullValue()));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testUnmarshalInvalid() throws Exception {
		final String invalidDate = "not a date";

		new XsdDateAdapter().unmarshal(invalidDate);

		fail();
	}

	private static Date createTestDate(final String date) {
		return LocalDate.parse(date).toDate();
	}

}
