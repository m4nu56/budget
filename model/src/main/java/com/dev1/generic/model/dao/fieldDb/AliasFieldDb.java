package com.dev1.generic.model.dao.fieldDb;

public class AliasFieldDb implements FieldDb { 
	
	private static final long serialVersionUID = 1L;

	private FieldDb fieldDb;
	private String alias;
	
	/**************** (Contructeur) ******************************************
	 * @param alias
	 * @param fieldDb
	 *************************************************************************/
	public AliasFieldDb( String alias, FieldDb fieldDb) {
		this.fieldDb = fieldDb;
		this.alias = alias;
	}

	@Override
	public String getColumnName()		{	return fieldDb.getColumnName(); }
	public int getTypeSql()				{	return fieldDb.getTypeSql();}
	public boolean isNotNull()			{	return fieldDb.isNotNull();	}
	public int getModeWrite()			{	return fieldDb.getModeWrite(); }
	public String getForeignKeyTable()	{	return fieldDb.getForeignKeyTable(); }
	public boolean isModeWrite(int mode){	return fieldDb.isModeWrite(mode); }
	public String getAlias()			{	return alias; }
	public String getAliasColumnName()	{	return getAliasColumnName(alias); }
	public String getAliasColumnName(String prefix)	{ return ((prefix != null) ? (prefix + "_") : "" ) + fieldDb.getColumnName(); }
	public String name()				{ return fieldDb.name(); }
}
