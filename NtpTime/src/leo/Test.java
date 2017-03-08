package leo;

import org.apache.log4j.Logger;
import org.apache.log4j.MDC;
import org.apache.log4j.NDC;

public class Test {
	private org.apache.log4j.Logger logger = Logger.getLogger(getClass());
	
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
				
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
}
