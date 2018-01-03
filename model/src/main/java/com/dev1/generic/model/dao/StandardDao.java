package com.dev1.generic.model.dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Array;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.dev1.generic.model.dao.fieldDb.FieldDb;
import com.dev1.generic.model.dao.fieldDb.FieldDbKeyValue;
import com.dev1.generic.utils.formulaire.CRUD;
import com.dev1.gestionbudget.model.exception.NotFoundException;

public abstract class StandardDao<E> extends JdbcDaoSupport {

	@Inject
	protected
    RowMapper<E> rowMapper;
	
	public static final String SCHEMA_PUBLIC = "public";
	protected String schemaCompte = null;		// schema eventuel spécifique du user (à ajouter dans les requetes)
	
	public static final int MODE_NONE  = 0;
	public static final int MODE_SELECT  = 1;
	public static final int MODE_INSERT  = 2;
	public static final int MODE_UPDATE  = 4;
	public static final int MODE_SEL_INSERT = MODE_INSERT | MODE_SELECT;
	public static final int MODE_SEL_UPDATE = MODE_UPDATE | MODE_SELECT;
	public static final int MODE_SEL_INS_UP  = MODE_SEL_INSERT | MODE_UPDATE | MODE_SELECT;
	public static final int MODE_INS_UP = MODE_SEL_INSERT | MODE_UPDATE;
	
	public abstract  FieldDb[] getFields();
	protected abstract String getTableName();
	protected abstract String getSequenceName();	// devra retourner le nom de la sequence associée à la clé primaire auto générée. Si retourne null, le nom de la séquence sera par défaut : tableName + "_id_seq"
	public abstract String getAlias();
//	public abstract E buildBeanBasic( ResultSet rs, String beanPrefix, boolean isSubBuild) throws Throwable;	

	/*****************************************************************************
	 * Construit un select "basic" monotable sans where
	 * 
	 * Si isOnlyIds == true ne met que le field ID de la table courante
	 * 		Sinon par default (Si pas Override) la liste des Fields de type MODEZ_SELECT de la table courante 
	 * 
	 * 
	 * @param isOnlyIds
	 * @return
	 * @throws Throwable
	 *****************************************************************************/
	protected String makeSelectBasicBean() throws Throwable {
		StringBuilder query = new StringBuilder(200);
		query.append("SELECT ");
		query.append( SELECT_Fields(getAlias()) );
		query.append(FROM( getTableName(), getAlias()));
		return query.toString();
	}
	
	/**************************************************************************************
	 * @param alias
	 * @return
	 **************************************************************************************/
	public String SELECT_Fields(String alias) {
		return SELECT_Fields(alias, getFields());	
	}

	/***************************************************************************
	 * Construit une String contenant tous les champs de la table tableName 
	 * sous la forme "[tableName].[field] as [getTableName()]_[field], ... " 
	 * @param tableFields
	 * @param identifiantTable
	 * @param prefixRs
	 * @return 
	 ***************************************************************************/
	protected  String SELECT_Fields(FieldDb ... tableFields) {
		StringBuilder buff = new StringBuilder();
		int i=0;
		if (tableFields.length == 0 ) tableFields = getFields();
		for(FieldDb field : tableFields) {
			if ( field.isModeWrite( MODE_SELECT ) ) {
				if (i++ > 0) buff.append(", ");
				buff.append(" ").append( SELECT_OneField( field ) );
			}
		}
		if (i > 0) buff.append(" ");
		return buff.toString();
	}
	
	/**********************************************************************
	 * Construit une String contenant tous les champs de la table tableName 
	 * sous la forme "[tableName].[field] as [getTableName()]_[field], ... " 
	 * @param tableFields
	 * @param identifiantTable
	 * @param prefixRs
	 * @return 
	 **********************************************************************/
	protected static String SELECT_Fields(String aliasTable , FieldDb ... tableFields) {
		ArrayList<String> fields = new ArrayList<String>(tableFields.length);
		
		for(FieldDb field : tableFields) {
			if ((field.getModeWrite() & MODE_SELECT) == MODE_SELECT) {
				fields.add( SELECT_OneField(aliasTable , field.getColumnName() ) );
			}
		}
		return StringUtils.join(fields, ", ") + " ";
	}
	
	/***********************************************************
	 * @param aliasTable
	 * @param field
	 * @return
	 ***********************************************************/
	protected static String SELECT_OneField(String aliasTable , FieldDb field ) {
		return String.format("%s.%s as %s_%s", aliasTable, field.getColumnName(), aliasTable, field.getColumnName() );
	}
	
	/***************************************************************************
	 * @param field
	 * @return
	 ***************************************************************************/
	protected static String SELECT_OneField(FieldDb field ) {
		return SELECT_OneField(field.getAlias(), field.getColumnName());
	}
	
	/**********************************************************************
	 * Construit une String contenant tous les champs de la table tableName 
	 * sous la forme "[tableName].[field] as [getTableName()]_[field], ... " 
	 * @param tableFields
	 * @param identifiantTable
	 * @param prefixRs
	 * @return 
	 **********************************************************************/
	protected static String SELECT_OneField(String aliasTable , String field ) {
		return String.format("%s.%s as %s_%s", aliasTable, field, aliasTable, field);
	}
	
	/***************************************************************************
	 * Construit une clause FROM de la forme "FROM [tableName] [alias] "
	 * @param tableName
	 * @param alias
	 * @return
	 ***************************************************************************/
	protected static String FROM(String tableName, String alias) {
		return String.format(" \r\nFROM %s %s ", tableName, alias);
	}
	
	/**************************************************************************************
	 * @param id
	 * @return
	 * @throws Throwable
	 **************************************************************************************/
	public E getBeanById(long id) throws NotFoundException { 
		// Recherche du field ID
		for (FieldDb f : this.getFields()) {
			if ("id".equalsIgnoreCase(f.getColumnName())) {
				return getBeanByUniqueField(f, id);
			}
		}
		throw new NotFoundException("Field ID introuvable pour la table " + getTableName());
//		throw new GenericMetierException("STANDARDSVC_ID_FIELD_NOT_FOUND" );
	}
	
	/**************************************************************************************
	 * @param refField
	 * @param value
	 * @return
	 * @throws Throwable
	 **************************************************************************************/
	public E getBeanByUniqueField( FieldDb refField, Object value ) throws NotFoundException {
		return genericGetBeanByUniqueField( refField, value );
	}
	
	/**************************************************************************************
	 * @param refField
	 * @param value
	 * @return
	 * @throws Throwable
	 **************************************************************************************/
	private E genericGetBeanByUniqueField( FieldDb refField, Object value ) throws NotFoundException {
		
		E bean = null;
		StringBuilder query=new StringBuilder(200);
		
//		boolean isInitiateurCon=(con==null);
//		if (isInitiateurCon) {con=getConnexion();}

		try {

			query.append( makeSelectBasicBean() );
			query.append( String.format(" WHERE %s.%s=?", getAlias(), refField.getColumnName() ));
			
			bean = (E)getJdbcTemplate().queryForObject(query.toString(), new Object[]{value}, rowMapper);
			
		} catch(EmptyResultDataAccessException t) {
			throw new NotFoundException();
			
		} catch(Throwable t) {
			t.printStackTrace();
			
		} finally {
//			close(con, psmt, rs, isInitiateurCon, false);
		}
		
		return bean;
	}
	
	
	/***************************************************************************
	 * Sauvegarde le bean en base de données. Cette méthode appel create() si bean.getId() == 0, 
	 * sinon elle appel update(). Ensuite, elle appel saveChildren(). 
	 * 
	 * Nota Bene : S'il y a une erreur lors du CREATE des enfants, le bean parent et tous les enfants pour 
	 * lesquels le CREATE à fonctionné, auront un id différent de 0. Il est de la responsabilité des classes
	 * filles de remettre les id à 0.
	 * @param con
	 * @param bean
	 * @throws Throwable
	 ***************************************************************************/
	public void crud(E bean, CRUD crud) throws Throwable {
//		boolean isInitiateurCon = (con == null);
//		if (isInitiateurCon) con = getConnexion();
		
//		boolean isMyTransaction = setTransactionOn(con, isInitiateurCon);
			
		try{
			switch(crud){
				case CREATE : create(bean); break;
//				case UPDATE : update(bean); break;
//				case DELETE : delete(bean); break;
			}
			
			crudChildren(bean, crud);
			
//			if (isMyTransaction) con.commit();
		}catch(Throwable t){
//			if (isMyTransaction) con.rollback();
			throw t;
		}finally{
			try {
//				if (isMyTransaction) con.setAutoCommit(true);
			} catch (Exception e) {}
			// --- fermeture de la connexion si on en est l'initiateur
//			if (isInitiateurCon) {try{con.close();}catch(Throwable et) {}}
		}
	}
	
	protected abstract int fillPsmtWithBean(PreparedStatement psmt, E bean, CRUD crud, int pos) throws SQLException;
	
	/*******************************************************************************************************
	 * Appelée par crud(). Redéfinir pour faire l'INSERT si besoin d'un traitement spécifique.
	 * @param con
	 * @param isInitiateurCon
	 * @param mouvement
	 * @throws Throwable
	 *******************************************************************************************************/
	protected void create(E bean) throws Exception {

		// INSERT
		try {
			
			String sql = getINSERTQuery();
			getJdbcTemplate().update(sql, new PreparedStatementSetter() {
				
				public void setValues(PreparedStatement psmt) throws SQLException {
					fillPsmtWithBean(psmt, bean, CRUD.CREATE, 1);
				}
				
			});
			
		} catch (SQLException e) {
			throw e;
			
		} catch (Exception e) {
			throw e;
			
		} finally {
//			close(con, psmt, rs, isInitiateurCon, isMyTransaction);			
		}
	}
	
	/***************************************************************************
	 * Construit une requête SQL de type INSERT. 
	 * <psmt> = <con>.prepareStatement(<query>, Statement.RETURN_GENERATED_KEYS) et 
	 * <psmt>.getGeneratedKeys();
	 * @param tableName
	 * @param fullFields
	 * @return
	 ***************************************************************************/
	protected String getINSERTQuery() throws Exception {
		return getINSERTQuery(getTableName(), this.getFields());
	}
	
	protected String getINSERTQuery(String tableName) throws Exception {
		return getINSERTQuery(tableName, this.getFields());
	}
	
	/***************************************************************************
	 * Construit une requête SQL de type INSERT. 
	 * <psmt> = <con>.prepareStatement(<query>, Statement.RETURN_GENERATED_KEYS) et 
	 * <psmt>.getGeneratedKeys();
	 * @param tableName
	 * @param fullFields
	 * @return
	 ***************************************************************************/
	protected String getINSERTQuery(String tablename,FieldDb[] tblFields ) throws Exception {
		List<String> lstFields = getLstColumnName(tblFields,MODE_INSERT);
		StringBuffer query = new StringBuffer(200);
		query.append("INSERT INTO ").append(tablename)
		     .append("(").append(StringUtils.join(lstFields.toArray(), ", ", 0, lstFields.size())).append(") ")
			 .append("\r\nVALUES(").append(StringUtils.join(Collections.nCopies(lstFields.size(), "?").toArray(), ", ", 0, lstFields.size()))
			 .append(")");
		return getQueryWithSchemaUser(query.toString());
	}
	
	/*******************************************************************
	 * Renvoie la requete formattée avec le nom du Schéma spécifique
	 * @param query
	 * @return
	 *******************************************************************/
	public String getQueryWithSchemaUser(String query) {
		return String.format(query, (schemaCompte==null || schemaCompte.isEmpty()) ? "" : schemaCompte+".");
	}
	
	/*******************************************************************************************************
	 * 
	 * Construit la Liste des noms des colonnes du FieldDB courant ( via getFields() )     
	 * Pour ceux possédant le mode passé en argument
	 *   
	 * @param tableName
	 * @param fullFields
	 * @return
	 *******************************************************************************************************/
	protected List<String> getLstColumnName(FieldDb[] tblFields,int  modeReadWrite) {
		
		List<String> tblField=new ArrayList<String>();
		for ( FieldDb fdb : tblFields )
			if ( fdb.isModeWrite( modeReadWrite ) )
				tblField.add( fdb.getColumnName() );
		return tblField;
	}
	
	/***************************************************************************
	 * Appelée par save() après la sauvegarde du bean. Redéfinir pour effectuer la 
	 * gestion des beans enfants.
	 * @param con
	 * @param isInitiateurCon
	 * @param bean
	 * @throws Throwable
	 ***************************************************************************/
	protected void crudChildren(E bean, CRUD crud) throws Throwable {};
	
	/***************************************************************************
	 * @param pos
	 * @param psmt
	 * @param fieldToWrite
	 * @param value
	 * @throws Exception
	 ***************************************************************************/
	protected void fillPsmtData( int pos, PreparedStatement psmt, FieldDb fieldToWrite, Object value) throws SQLException {

		if (value == null) {
			if(fieldToWrite.getTypeSql()==Types.BLOB) { // http://stackoverflow.com/questions/34128144/unable-to-insert-null-in-bytea-field-in-postgres
				psmt.setNull(pos, Types.OTHER);
			} else {
				psmt.setNull(pos, fieldToWrite.getTypeSql());
			}
		}
		else {
			switch( fieldToWrite.getTypeSql() ) {
				case Types.SMALLINT:
					short valShort;
					if (value instanceof Integer) 
						valShort=((Integer)value).shortValue();
					else
						valShort=((Short)value).shortValue();
					psmt.setShort(pos,valShort);	
					break;
				
				case Types.DATE: 		psmt.setDate(pos, new java.sql.Date( ((Date) value).getTime() ) ); 					break;
				case Types.BIGINT:		psmt.setLong(pos,((Long) value).longValue()	);										break; // pas 'Integer' // peut casser des trucs
				case Types.INTEGER:		psmt.setInt(pos, ((Integer) value).intValue());										break;
				case Types.NUMERIC:		psmt.setDouble(pos,((Double) value).doubleValue());									break;
				case Types.DOUBLE:		psmt.setDouble(pos,((Double) value).doubleValue());									break;
				case Types.VARCHAR:		psmt.setString(pos,(String) value);													break;
				case Types.CHAR:		psmt.setString(pos, ((String) value).substring(0, 1));								break;
				case Types.BOOLEAN: 	psmt.setBoolean(pos,((Boolean) value).booleanValue()); 								break;
				case Types.TIMESTAMP:   psmt.setTimestamp(pos, new java.sql.Timestamp(((Date) value).getTime())); 			break;
				case Types.BLOB: 
					if(value instanceof String){
						psmt.setString(pos, (String)value);
					}else{
						try {
							psmt.setBinaryStream(pos, (InputStream) value,
									((InputStream) value).available());
						} catch (IOException e) {
							e.printStackTrace();
							throw new SQLException("IOException: Impossible de valoriser le psmt avec le binaryStream");
						}
					}
					break;
				case Types.BINARY: 		psmt.setBytes(pos, (byte[]) value); 												break;
				case Types.ARRAY:

					Object obj = ((Object[]) value)[0];
					String typeArray; 

						 if (obj instanceof Integer)typeArray="integer";
					else if (obj instanceof Long)	typeArray="bigint";
					else if (obj instanceof Boolean)typeArray="boolean";
					else if (obj instanceof Double)	typeArray="numeric";
					else if (obj instanceof String)	typeArray="varchar";
					else if (obj instanceof Date)	typeArray="timestamp";
					else throw new SQLException("Types.SQL sur Array non définie: " + obj.getClass().getName() );

					Array array=psmt.getConnection().createArrayOf(typeArray, (Object[]) value);
					psmt.setArray(pos, array);
					array.free();
					break;

				case Types.OTHER:
//					if ( value instanceof Point ) {
//						psmt.setObject(pos,new PGgeometry((Point)value));
//					} 
//					else if (value instanceof Dev10Geometry) {
//						psmt.setObject(
//								pos, 
//								new GenericGeometryDao(this).getGeometryFromWKT( con, ((Dev10Geometry)value).getWktDef(), ((Dev10Geometry)value).getSrid() ) 
//						);
//					} 
//					else if (value instanceof PGgeometry) {
//						psmt.setObject(pos, value );
//					}
//					else 
						if (value instanceof String ) {	// Cas Json (PB : grille instanceof String)
// Voir Si instanceof JsonObject -> psmt.setObject(pos, new JsonParser().parse((String) value).getAsJsonObject(), Types.OTHER );
						psmt.setObject(pos, value, Types.OTHER );
					}
					else throw new SQLException("Types.OTHER non définie: " + value.getClass().getName() );
					break;

				default:
					throw new SQLException("Types.SQL non définie: " + fieldToWrite.getTypeSql() );
			}

		}
	}	

	/********************************************************************************
	 * @param f
	 * @param tblFieldVal
	 * @return
	 * @throws Exception
	 ********************************************************************************/
	public Object rechercheValue(FieldDb f,FieldDbKeyValue[] tblFieldVal ) throws SQLException {
		for (FieldDbKeyValue kv : tblFieldVal) {
			if (kv.getKey().equals(f)) return kv.getValue(); 
		}
		throw new SQLException("FIELD_NOT_FOUND_ON_FILLPSMT: " + f.getColumnName());
	}
}
