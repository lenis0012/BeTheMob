package src.com.lenis0012.bukkit.btm.nms;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("serial")
public class FieldMap extends HashMap<String, Field> implements Map<String, Field> {
	
	public void init(Class<?> fromClass) {
		if(fromClass == null)
			return;
		
		for(Field field : fromClass.getDeclaredFields()) {
			String name = field.getName();
			if(!this.containsKey(name)) {
				this.put(name, field);
			}
		}
		
		this.init(fromClass.getSuperclass());
	}
}