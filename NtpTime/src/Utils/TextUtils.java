package Utils;

public class TextUtils {
	public static String toUppers(String source){
		return source.toUpperCase();
	}
	
	public static String toLowers(String source){
		return source.toLowerCase();
	}
	
	public static void main(String[] args) {
		System.out.println(toUppers("aBcDxY"));
		System.out.println(toLowers("aBcDxY"));
	}
}
