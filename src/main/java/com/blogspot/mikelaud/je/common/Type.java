package com.blogspot.mikelaud.je.common;

public class Type {
	
	private String mFullName;
	private TypeType mType;
	private TypeAccess mAccess;
	private boolean mInner;
	private boolean mDeprecated;
	
	private String nvl(String aString) { return (null == aString ? "" : aString); }
	private TypeType nvl(TypeType aTypeType) { return (null == aTypeType ? TypeType.Class : aTypeType); }
	private TypeAccess nvl(TypeAccess aTypeAccess) { return (null == aTypeAccess ? TypeAccess.Default : aTypeAccess); }
	
	public String getFullName() { return mFullName; }
	public TypeType getType() { return mType; }
	public TypeAccess getAccess() { return mAccess; }
	public boolean isInner() { return mInner; }
	public boolean isDeprecates() { return mDeprecated; }
	
	public void setFullName(String aFullName) { mFullName = nvl(aFullName); }
	public void setType(TypeType aTypeType) { mType = nvl(aTypeType); }
	public void setAccess(TypeAccess aTypeAccess) { mAccess = nvl(aTypeAccess); }
	public void setInner(boolean aInner) { mInner = aInner; }
	public void setDeprecated(boolean aDeprecated) { mDeprecated = aDeprecated; }
	
	public Type() {
		mFullName = "";
		mType = TypeType.Class;
		mAccess = TypeAccess.Default;
		mInner = false;
		mDeprecated = false;
	}
	
}
