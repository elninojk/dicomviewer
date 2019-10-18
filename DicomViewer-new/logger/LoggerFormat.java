package logger;

import java.io.IOException;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class LoggerFormat extends Formatter{

	public LoggerFormat() {
		 
	}
public String format(LogRecord record) {
        return record.getThreadID()+"::"+record.getSourceClassName()+"::"+record.getSourceMethodName()+"::"
                +new Date(record.getMillis())+"::"
                +record.getMessage()+"\n";
    }
public static FileHandler getFileHandler() throws SecurityException, IOException
	{
		FileHandler fHandler = new FileHandler(".//Logger.log");
		fHandler.setFormatter(new LoggerFormat());
		return fHandler;	
		}
}
