package org.sergeys.gpublish.logic;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * Custom formatter for Java logging
 * to use under 1.6 where java.util.logging.SimpleFormatter.format is not supported yet
 *
 * @author sergeys
 *
 */
public class LogFormatter extends Formatter {

    @Override
    public String format(LogRecord record) {

        if(record.getThrown() == null){
            return String.format("%s: %s\n", record.getLevel(), record.getMessage());
        }
        else{
            StringWriter sw = new StringWriter();
            record.getThrown().printStackTrace(new PrintWriter(sw));
            return String.format("%s: %s: %s\n%s\n", record.getLevel(), record.getMessage(), record.getThrown().getMessage(), sw.toString());
        }
    }

}
