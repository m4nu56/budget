package com.dev1.generic.model.dao.fieldDb;

public class FieldDbKeyValue {
	
	private FieldDb key;
	private Object value;
	
	public FieldDbKeyValue( FieldDb key, Object value ) {
		this.key = key;
		this.value = value;
	}

	public FieldDb getKey() { return key; }
	public void setKey(FieldDb key) { this.key = key; }

	public Object getValue() { return value; }
	public void setValue(Object value) { this.value = value; }

}
