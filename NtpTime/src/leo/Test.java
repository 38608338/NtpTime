package leo;

import org.apache.log4j.Logger;
import org.apache.log4j.MDC;
import org.apache.log4j.NDC;

public class Test {
	//log4j.rootLogger д╛хо
	private org.apache.log4j.Logger logger = Logger.getLogger(getClass());
	private org.apache.log4j.Logger logger2 = Logger.getLogger("rootLogger");
	
	//log4j.logger.thisProject.file
	private org.apache.log4j.Logger logger3 = Logger.getLogger("thisProject.file");
	
	public void print(String message){
		//NDC.push("ndc");
		MDC.put("user","SessionID~127.0.0.1~url");
		logger.debug(message);
		MDC.put("user","SessionID~192.168.0.3~url");
		logger.info(message);
		MDC.put("user","SessionID~192.168.0.4~url");
		logger.warn(message);
		MDC.put("user","SessionID~192.168.0.5~url");
		logger.error(message);
	}
	public void print2(String message){
		//NDC.push("ndc");
		MDC.put("user","SessionID~127.0.0.1~url");
		logger2.debug(message);
		MDC.put("user","SessionID~192.168.0.3~url");
		logger2.info(message);
		MDC.put("user","SessionID~192.168.0.4~url");
		logger2.warn(message);
		MDC.put("user","SessionID~192.168.0.5~url");
		logger2.error(message);
		
		MDC.put("user","SessionID~127.0.0.1~url");
		logger3.debug(message);
		MDC.put("user","SessionID~192.168.0.3~url");
		logger3.info(message);
		MDC.put("user","SessionID~192.168.0.4~url");
		logger3.warn(message);
		MDC.put("user","SessionID~192.168.0.5~url");
		logger3.error(message);
	}
	public static void main(String[] args) {
		System.err.println("Oops");
		
		Test t=new Test();
		//t.print("yep");
		new Thread(t.new MyThread()).start();
		new Thread(t.new MyThread()).start();
		new Thread(t.new MyThread()).start();
		new Thread(t.new MyThread()).start();
		new Thread(t.new MyThread()).start();
		new Thread(t.new MyThread()).start();
	}
	
	private class MyThread implements Runnable{

		@Override
		public void run() {
			while (true) {
				print("DN-JACK-30");
				print2("DN-JACK-60");
				
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
}
