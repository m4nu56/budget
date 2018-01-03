package com.dev1.generic.model.dao.fieldDb;

/*************************************************************************
 * 
 * Permet de géré le champs JSON
 * 
 * Exemple :
 * Champ json d'une table : json_sample = { "activite": { "libelle" : { "en_US": "TRY", "fr_FR": "TEST" } } }
 * 
 *  new  AliasFieldJsonDb( "X", Field.JSON_SAMPLE, "activite", "libelle", Language.FR.name() );
 *  getColumNameJsonKey Donnera	: json_sample->'activite'->'libelle'->>'fr_FR'
 *   
 *************************************************************************/
public class AliasFieldJsonDb extends AliasFieldDb {
	
	private static final long serialVersionUID = 1L;

	private String[] tbKeyJson;

	/**************** (Contructeur) ******************************************
	 * @param alias
	 * @param fieldDb
	 * @param keyJson
	 *************************************************************************/
	public AliasFieldJsonDb( String alias, FieldDb fieldDb, String ... tbKeyJson ) {
		super( alias, fieldDb);
		this.tbKeyJson = tbKeyJson;
	}

	/**************** (Contructeur) ******************************************
	 * @param fieldDb
	 * @param keyJson
	 *************************************************************************/
	public AliasFieldJsonDb( FieldDb fieldDb, String keyJson) {
		this( fieldDb.getAlias(), fieldDb, keyJson);
	}

	/*************************************************************************
	 * 
	 *************************************************************************/
	public String[] getTbKeyJson() { return tbKeyJson; }
	public void setTbKeyJson(String ... tbKeyJson) { this.tbKeyJson = tbKeyJson; }


	public String getColumNameJsonKey( ) {
		return getColumNameJsonKey( getColumnName(), this.tbKeyJson );
	}

	public String getColumNameJsonKey( String columnName ) {
		return getColumNameJsonKey( columnName, this.tbKeyJson );
	}
		
	/*************************************************************************
	 * 
	 * Mise en forme du nom d'une colonne Json avec la key
	 * Exemple :
	 * Champs de la table json => json_sample = { "activite": { "libelle" : { "en_US": "TRY", "fr_FR": "TEST" } } }
	 * Donnera : json_sample->'activite'->'libelle'->>'fr_FR'
	 * 
	 * @param columnName
	 * @param tableKeyJson
	 * @return
	 *************************************************************************/
	public static String getColumNameJsonKey( String columnName, String ... tableKeyJson ) {
		StringBuilder buff = new StringBuilder();
		buff.append( columnName );
		if (tableKeyJson != null && tableKeyJson.length > 0) {
			for(int i=0; i < tableKeyJson.length; i++ ) {
				// Sur le dernier on pointe sur la value de la clé "->>" sinon sur la clé "->" 
				buff.append( (i == tableKeyJson.length-1) ? "->>'" : "->'").append( tableKeyJson[i] ).append("'");
			}
		}
		return buff.toString();
	}
}
