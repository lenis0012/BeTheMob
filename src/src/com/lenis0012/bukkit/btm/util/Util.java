package src.com.lenis0012.bukkit.btm.util;

public class Util {	
	public static boolean containsEnum(@SuppressWarnings("rawtypes") Class<? extends Enum> classobj, String val) {
	   Object[] arr = classobj.getEnumConstants();
	   for (Object e : arr) {
	      if (((Enum<?>) e).name().equals(val)) {
	         return true;
	      }
	   }
	   return false;
	}

}
