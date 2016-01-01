package com.blogspot.mikelaud.je.domain.pojo;

import org.objectweb.asm.Type;

import com.blogspot.mikelaud.je.domain.types.AccFinal;
import com.blogspot.mikelaud.je.domain.types.MethodAccess;

public class DomainMethod {

	private String mName;
	private MethodAccess mAccess;
	private AccFinal mFinal;
	private Type mReturnType;
	
	public String getName() { return mName; }
	public MethodAccess getAccess() { return mAccess; }
	public AccFinal getFinal() { return mFinal; }
	public Type getReturnType() { return mReturnType; }
	
	public void setName(String aName) { mName = aName; }
	public void setAccess(MethodAccess aAccess) { mAccess = aAccess; }
	public void setFinal(AccFinal aFinal) { mFinal = aFinal; }
	public void setReturnType(Type aReturnType) { mReturnType = aReturnType; }
	
	public DomainMethod() {
		mName = "";
		mAccess = MethodAccess.Default;
		mFinal = AccFinal.No;
		mReturnType = Type.getType(Void.class);
	}
	
}
