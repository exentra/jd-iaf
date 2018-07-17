package com.deere.tools.iaf;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DateServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	public void service(final HttpServletRequest req, final HttpServletResponse res)
			throws ServletException, IOException {

		res.setContentType("text/plain");

		final ServletOutputStream out = res.getOutputStream();

		// *** Beg new logic ***//
		final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
		simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
		final String todayGMT = simpleDateFormat.format(new Date());
		out.println(todayGMT);
		// *** End new logic ***//

		// *** Beg old logic ***//
		final Date today = new Date();
		out.println(today.toString());
		// *** End old logic ***//
	}
}