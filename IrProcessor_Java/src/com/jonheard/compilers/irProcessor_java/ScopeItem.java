package com.jonheard.compilers.irProcessor_java;

class ScopeItem
{
	public ScopeItem(String javaType, String value)
	{
		this.javaType = javaType;
		this.value = value;
	}
	
	public String getJavaType() { return javaType; }
	public String getValue() { return value; }

	private String javaType;
	private String value;
}
