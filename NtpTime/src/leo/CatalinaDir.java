package leo;

import java.util.logging.Level;
import java.util.logging.Logger;

public class CatalinaDir {
    private static Logger log = Logger.getLogger("CatalinaDir");
    
	public static void main(String[] args) {
		System.out.println(System.getProperty("user.dir"));
		
		log.setLevel(Level.FINEST);
        log.finest("the finest message");
        log.finer("finer message");
        log.fine("a fine message");
        log.config("some configuration message");
        log.info("a little bit of information");
        log.warning("a warning message");
        log.severe("a severe message");
	}
}
