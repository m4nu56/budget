package com.dev1.generic.model.dao.fieldDb;


public interface FieldDb {
	public String getColumnName();
	public int getTypeSql();
	public int getModeWrite();
	public boolean isNotNull();
	public String getForeignKeyTable();
	public boolean isModeWrite(int mode);
	public String getAlias();
	public String getAliasColumnName();
	public String getAliasColumnName(String prefix);
	public String name();
//	public boolean isPrimaryKey();
}
