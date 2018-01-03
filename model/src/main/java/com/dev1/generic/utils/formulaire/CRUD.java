package com.dev1.generic.utils.formulaire;

public enum CRUD {

	CREATE("C"),
	READ("R"),
	UPDATE("U"),
	DELETE("D");

	private String type;
	
	/************************** (constructor) ************************************
	 * @param type
	 *****************************************************************************/
	private CRUD(String type) {
		this.type = type;
	}
	
	public String toString(){ return type; }

	public String getActionName() {
		switch(this) {
			case CREATE: return "create";
			case UPDATE: return "edit";
			case DELETE: return "delete";
		}
		return "";
	}

	public boolean isRead() {
		return this.equals(READ);
	}
	
}
