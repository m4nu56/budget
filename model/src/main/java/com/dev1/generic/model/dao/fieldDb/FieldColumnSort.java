//package com.dev1.generic.model.dao.fieldDb;
//
//import java.io.Serializable;
//
//import com.dev1.generic.model2014.dao.TriTable.ColumnSort;
//
//public class FieldColumnSort implements ColumnSort, Serializable {
//	
//	private static final long serialVersionUID = 1L;
//
//	FieldDb[] tabField;
//	String name;
//
//	/**************** (Contructeur) ******************************************
//	 * @param fieldDb
//	 *************************************************************************/
//	public FieldColumnSort(FieldDb ... tabFieldDb) {
//		this( tabFieldDb[0].name(), tabFieldDb);
//	}
//
//	/**************** (Contructeur) ******************************************
//	 * @param name
//	 * @param fieldDb
//	 *************************************************************************/
//	public FieldColumnSort(String name, FieldDb ... tabFieldDb) {
//		this.tabField = tabFieldDb;
//		this.name = name;
//	}
//
//	/**************** (Contructeur) ******************************************
//	 * @param columnSort
//	 * @param fieldDb
//	 *************************************************************************/
//	public FieldColumnSort(ColumnSort columnSort, FieldDb ... tabFieldDb) {
//		this( columnSort.getColumnName(), tabFieldDb);
//	}
//
//	public ColumnSort[] getValues() {
//		ColumnSort[] tabColumnSort = new ColumnSort[1];
//		tabColumnSort[0] = this;
//		return tabColumnSort;
//	}
//
//	@Override
//	public String getColumnName() { return name; }
//
//	@Override
//	public FieldDb[] getTableFieldDb() {
//		return tabField;
//	}
//}
