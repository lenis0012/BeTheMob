package com.lenis0012.bukkit.btm.nms.wrappers;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import com.lenis0012.bukkit.btm.nms.FieldMap;
import com.lenis0012.bukkit.btm.util.DynamicUtil;

public class Packet extends WrapperBase {
	private static Map<Class<?>, FieldMap> globalFields = new HashMap<Class<?>, FieldMap>();
	
	private Class<?> packet_class;
	private FieldMap fields;
	
	public Packet(String name) {
		this.packet_class = DynamicUtil.getNMSClass(name);
		setHandle(DynamicUtil.newInstance(packet_class));
		
		if(!globalFields.containsKey(packet_class)) {
			FieldMap map = new FieldMap();
			map.init(packet_class);
			globalFields.put(packet_class, map);
		}
		
		this.fields = globalFields.get(packet_class);
	}
	
	public Packet(Object handle) {
		super(handle);
	}
	
	public void write(String fieldName, Object value) {
		if(!fields.containsKey(fieldName))
			throw new IllegalArgumentException("Invalid field name '" + fieldName +
					"' for class: " + packet_class.getSimpleName());
		
		Field field = fields.get(fieldName);
		DynamicUtil.setValue(handle, field, value);
	}
	
	public Object read(String fieldName, Object value) {
		if(!fields.containsKey(fieldName))
			throw new IllegalArgumentException("Invalid field name '" + fieldName +
					"' for class: " + packet_class.getSimpleName());
		
		Field field = fields.get(fieldName);
		return DynamicUtil.getValue(handle, field);
	}
}