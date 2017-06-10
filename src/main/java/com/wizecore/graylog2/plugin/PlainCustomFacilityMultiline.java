package com.wizecore.graylog2.plugin;

import org.graylog2.plugin.Message;
import org.graylog2.syslog4j.SyslogIF;

public class PlainCustomFacilityMultiline extends PlainCustomFacility {

    public PlainCustomFacilityMultiline(String field) {
        super(field);
    }

    @Override
    public void send(SyslogIF syslog, int level, Message msg) {
        StringBuilder out = new StringBuilder();
        String header = createHeader(msg);
        String lines[] = msg.getMessage().split("\\r?\\n");

        for (int i = 0; i < lines.length; i++) {
            out.append(header);
            out.append(lines[i]);
            out.append("\n");
        }

        syslog.log(level, out.toString());

    }

    public String createHeader(Message msg) {
        StringBuilder out = new StringBuilder();
        appendHeader(msg, out, this.field);

        return out.toString();
    }
}
