//package com.dev1.generic.model.dao.fieldDb;
//
//import java.io.Serializable;
//
//import com.dev1.generic.model2014.dao.StandardDao;
//
//public class FieldDbNoneStatic implements Serializable, FieldDb { 
//	
//	private static final long serialVersionUID = 1L;
//	
//	/************************** (constructor) ************************************
//	 * @param alias
//	 * @param columnName
//	 * @param typeSql
//	 *****************************************************************************/
//	public FieldDbNoneStatic( String alias, String columnName, int typeSql) {
//		this.alias = alias;
//		this.columnName = columnName;
//		this.typeSql = typeSql;
//		modeWrite = StandardDao.MODE_SELECT;		// Default
//	}
//
//	private String columnName;
//	private int typeSql;
//	private boolean notNull;
//	private int modeWrite;
//	private int length;
//	private String foreignKeyTable;
//	private String alias;
//
//	public void setColumnName(String columnName) { this.columnName = columnName; }
//	public void setTypeSql(int typeSql) { this.typeSql = typeSql; }
//	public void setNotNull(boolean notNull) { this.notNull = notNull; }
//	public void setModeWrite(int modeWrite) { this.modeWrite = modeWrite; }
//	public void setLength(int length) { this.length = length; }
//	public void setForeignKeyTable(String foreignKeyTable) { this.foreignKeyTable = foreignKeyTable; }
//	public void setAlias(String alias) { this.alias = alias; }
//
//	@Override
//	public String getColumnName()		{	return this.columnName; }
//	public int getLength()				{	return this.length; }
//	public int getTypeSql()				{	return this.typeSql; }
//	public boolean isNotNull()			{	return this.notNull;	}
//	public int getModeWrite()			{	return this.modeWrite; }
//	public String getForeignKeyTable()	{	return this.foreignKeyTable; }
//	public boolean isModeWrite(int mode){	return (modeWrite & mode) == mode; }	
//	public String getAlias()			{	return alias; }
//	public String getAliasColumnName()	{	return alias + ((alias != null) ? "_" : "" ) + columnName; }
//	public String getAliasColumnName(String prefix)	{ return prefix + "_" + this.columnName; }
//	public String name()				{	return this.columnName; }
//}
