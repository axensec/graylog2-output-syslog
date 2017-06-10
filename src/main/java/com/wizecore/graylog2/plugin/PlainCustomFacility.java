package com.wizecore.graylog2.plugin;

import org.graylog2.plugin.Message;
import org.graylog2.syslog4j.SyslogIF;

import java.util.Date;

public class PlainCustomFacility implements MessageSender {
	protected String field;

	public PlainCustomFacility(String field) {
		this.field = field;
	}


	@Override
	public void send(SyslogIF syslog, int level, Message msg) {
		StringBuilder out = new StringBuilder();
		appendHeader(msg, out, this.field);

		out.append(msg.getMessage());
		String str = out.toString();
		// log.info("Sending plain message: " + level + ", " + str);
		syslog.log(level, str);
	}

	public static void appendHeader(Message msg, StringBuilder out, String field) {
		Date dt = null;
		Object ts = msg.getField("timestamp");
		if (ts != null && ts instanceof Number) {
			dt = new Date(((Number) ts).longValue());
		}
		
		if (dt == null) {
			dt = new Date();
		}
		
		// Write time
		PlainSender.appendSyslogTimestamp(dt, out);
		out.append(" ");
		
		// Write source (host)
		String source = msg.getSource();
		if (source != null) {
			out.append(source).append(" ");
		} else {
			out.append("- ");
		}
		
		// Write service
		Object facility = msg.getField(field);
		if (facility != null) {
			out.append(facility.toString()).append(" ");
		} else {
			out.append("- ");
		}

	}
}
