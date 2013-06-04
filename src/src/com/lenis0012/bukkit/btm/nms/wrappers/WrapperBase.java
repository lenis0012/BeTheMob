package src.com.lenis0012.bukkit.btm.nms.wrappers;

public class WrapperBase {
	Object handle;
	
	public WrapperBase() {}
	
	public WrapperBase(Object handle) {
		this.handle = handle;
	}
	
	public Object getHandle() {
		return this.handle;
	}
	
	public void setHandle(Object handle) {
		this.handle = handle;
	}
	
	@Override
	public boolean equals(Object obj) {
		return this.handle.equals(obj);
	}
	
	@Override
	public String toString() {
		return this.handle.toString();
	}
}
