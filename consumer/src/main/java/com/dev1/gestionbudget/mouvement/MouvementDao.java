package com.dev1.gestionbudget.mouvement;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import javax.sql.DataSource;

import org.springframework.dao.EmptyResultDataAccessException;

import com.dev1.generic.model.dao.StandardDao;
import com.dev1.generic.model.dao.fieldDb.FieldDb;
import com.dev1.generic.model.dao.fieldDb.FieldDbKeyValue;
import com.dev1.generic.utils.formulaire.CRUD;
import com.dev1.gestionbudget.model.bean.Mouvement;
import com.dev1.gestionbudget.model.exception.NotFoundException;

@Named
public class MouvementDao extends StandardDao<Mouvement> implements MouvementDaoInterface {

    @Inject 
    DataSource dataSource;
    
    @PostConstruct
    private void initialize(){
        setDataSource(dataSource);
    }
    
	public static final String tableName = "mouvement";
	public static final String alias = "m";
	
	@Override public FieldDb[] getFields() { return Field.values(); }
	@Override protected String getTableName() { return tableName; }
	@Override protected String getSequenceName() { return null; }
	@Override public String getAlias() { return alias; }

	/*******************************************************************************************************
	 * Les Fields 
	 *******************************************************************************************************/
	public enum Field implements FieldDb {
		ID						( "ID",							Types.BIGINT,		false,	MODE_SELECT ),
		DATE					( "DATE",						Types.DATE,			false,	MODE_SEL_INSERT ),
		LIBELLE					( "LIBELLE",					Types.VARCHAR,		false,	MODE_SEL_INSERT ),
		MONTANT					( "MONTANT", 					Types.NUMERIC,		false,	MODE_SEL_INS_UP ),
		EXERCICE				( "EXERCICE", 					Types.INTEGER,		false,	MODE_SEL_INS_UP ),
		MOIS					( "MOIS", 						Types.INTEGER,		false,	MODE_SEL_INS_UP ),
		;
		
		private String columnName;
		private int typeSql;
		private boolean notNull;
		private int modeWrite;
		private int length;
		private String foreignKeyTable;
		private String alias;
	
		private Field(String columnName,int typeSql,boolean notNull){
			init(columnName,typeSql, notNull, MODE_INSERT, 0, null, null);
		}
	
		private Field(String columnName,int typeSql,boolean notNull, int modeWrite){
			init(columnName,typeSql, notNull, modeWrite, 0, null, null);
		}
	
		private Field(String columnName,int typeSql, boolean notNull, int modeWrite, int length){
			init(columnName,typeSql, notNull, modeWrite, length, null, null);
		}
	
		private Field(String columnName,int typeSql, boolean notNull, int modeWrite, String foreignKeyTable){
			init(columnName,typeSql, notNull, modeWrite, 0, foreignKeyTable, null);
		}
	
		private Field(String columnName,int typeSql, boolean notNull, String alias){
			init(columnName,typeSql, notNull, MODE_SELECT , 0, null, alias);
		}
	
		private Field(String columnName,int typeSql, boolean notNull, int modeWrite, int length, String foreignKeyTable){
			init(columnName,typeSql, notNull, modeWrite, length, foreignKeyTable, null);
		}
	
		private void init(String columnName,int typeSql, boolean notNull, int modeWrite, int length, String foreignKeyTable, String alias){
			this.columnName = columnName;
			this.typeSql = typeSql;
			this.notNull = notNull;
			this.modeWrite = modeWrite;
			this.length = length;
			this.foreignKeyTable = foreignKeyTable;
			this.alias = (alias != null) ? alias : MouvementDao.alias;
		}
	
		@Override public String getColumnName()		{	return columnName; }
		public int getLength()				{	return length; }
		@Override public int getTypeSql()				{	return typeSql;}
		@Override public boolean isNotNull()			{	return notNull;	}
		@Override public int getModeWrite()			{	return modeWrite; }
		@Override public String getForeignKeyTable()	{	return foreignKeyTable; }
		@Override public boolean isModeWrite(int mode){	return (modeWrite & mode) == mode; }
		@Override public String getAlias()			{	return alias; }
		@Override public String getAliasColumnName()	{	return alias + "_" + columnName; }
		@Override public String getAliasColumnName(String alias) { return alias + "_" + columnName; }
		public int length(int mode)			{
			int lg =0;
			for(Field f: Field.values() ) {
				if (f.isModeWrite(mode) ) lg++;
			}
			return lg;
		}

	}
	
	/********************** (override) *****************************
	 * @see com.dev1.generic.model.dao.StandardDao#fillPsmtWithBean(java.sql.PreparedStatement, java.lang.Object, com.dev1.generic.utils.formulaire.CRUD, int)
	 ***************************************************************/
	@Override
	protected int fillPsmtWithBean(PreparedStatement psmt, Mouvement mouvement, CRUD crud, int pos ) throws SQLException {
		
		int modeCRUD = (crud == CRUD.CREATE) ? MODE_INSERT : MODE_UPDATE;
		
		FieldDbKeyValue[] tblFieldVal = {
			new FieldDbKeyValue(Field.ID, 			mouvement.getId()),
			new FieldDbKeyValue(Field.DATE, 		mouvement.getDate()!=null ? new java.sql.Date(mouvement.getDate().getTime()) : null),
			new FieldDbKeyValue(Field.LIBELLE, 		mouvement.getLibelle()),
			new FieldDbKeyValue(Field.MONTANT, 		mouvement.getMontant()),
			new FieldDbKeyValue(Field.EXERCICE, 	mouvement.getExercice()),
			new FieldDbKeyValue(Field.MOIS, 		mouvement.getMois()),
		};
		
		if (pos < 1) pos = 1;
		for(  FieldDb f: Field.values() ) {
			if ( f.isModeWrite(modeCRUD) ) fillPsmtData(pos++, psmt, f , rechercheValue(f, tblFieldVal) );
		}
		
		return pos;
	}
	
	@Override
	public List<Mouvement> getLstMouvement(int exercice) throws NotFoundException {
		
		List<Mouvement> lstMouvement = new ArrayList<Mouvement>();
		StringBuilder query=new StringBuilder(200);
		
		try {

			query.append( makeSelectBasicBean() );
			query.append( String.format(" WHERE %s.%s=?", getAlias(), Field.EXERCICE.getColumnName() ));
			
			lstMouvement = (List<Mouvement>)getJdbcTemplate().query(query.toString(), rowMapper, exercice);
			
		} catch(EmptyResultDataAccessException t) {
			throw new NotFoundException();
			
		} catch(Throwable t) {
			t.printStackTrace();
			
		} finally {
//				close(con, psmt, rs, isInitiateurCon, false);
		}
		
		return lstMouvement;
		
	}
	

//	/********************** (override) *****************************
//	 * @see com.dev1.gestionbudget.mouvement.MouvementDaoInterface#addMouvement(com.dev1.gestionbudget.model.bean.Mouvement)
//	 ***************************************************************/
//	@Override
//	public void addMouvement(Mouvement pMouvement) throws Exception {
//		
//		String sql = "INSERT INTO customer " + "(CUST_ID, NAME, AGE) VALUES (?, ?, ?)";
//		getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {
//			public void setValues(PreparedStatement ps, int i) throws SQLException {
//				Customer customer = customers.get(i);
//				ps.setLong(1, customer.getCustId());
//				ps.setString(2, customer.getName());
//				ps.setInt(3, customer.getAge());
//			}
//			
//			public int getBatchSize() {
//				return customers.size();
//			}
//		});
//	}

}
