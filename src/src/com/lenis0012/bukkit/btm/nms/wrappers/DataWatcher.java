package src.com.lenis0012.bukkit.btm.nms.wrappers;

import java.lang.reflect.Method;
import java.util.List;

import src.com.lenis0012.bukkit.btm.util.DynamicUtil;


public class DataWatcher extends WrapperBase {
	private static Class<?> DW_Class = DynamicUtil.getNMSClass("DataWatcher");
	private static Method set = DynamicUtil.getMethod(DW_Class, "a", int.class, Object.class);
	private static Method watch = DynamicUtil.getMethod(DW_Class, "watch", int.class, Object.class);
	private static Method getAllAndUnwatch = DynamicUtil.getMethod(DW_Class, "b");
	private static Method getAll = DynamicUtil.getMethod(DW_Class, "c");
	private static Method getByte = DynamicUtil.getMethod(DW_Class, "getByte", int.class);
	
	public DataWatcher(Object handle) {
		super(handle);
	}
	
	public DataWatcher() {
		super(DynamicUtil.newInstance(DW_Class));
	}
	
	public void set(int index, Object value) {
		DynamicUtil.invoke(set, handle, index, value);
	}
	
	public void watch(int index, Object value) {
		DynamicUtil.invoke(watch, handle, index, value);
	}
	
	public List<Object> getAllAndUnwatch() {
		return DynamicUtil.invoke(getAllAndUnwatch, handle);
	}
	
	public List<Object> getAll() {
		return DynamicUtil.invoke(getAll, handle);
	}
	
	public byte getByte(int index) {
		return DynamicUtil.invoke(getByte, handle, index);
	}
}