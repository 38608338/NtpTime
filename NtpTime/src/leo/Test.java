package leo;

import org.apache.log4j.Logger;

public class Test {
	private org.apache.log4j.Logger logger = Logger.getLogger(getClass());
	
	public void print(String message){
		logger.debug(message);
		logger.info(message);
		logger.warn(message);
		logger.error(message);
	}
	public static void main(String[] args) {
		System.err.println("Oops");
		
		//log4j:WARN No appenders could be found for logger (leo.Test).
		//log4j:WARN Please initialize the log4j system properly.
		//log4j:WARN See http://logging.apache.org/log4j/1.2/faq.html#noconfig for more info.

		Test t=new Test();
		t.print("yep");
	}
}
