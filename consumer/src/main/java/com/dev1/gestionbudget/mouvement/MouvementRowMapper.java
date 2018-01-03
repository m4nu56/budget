package com.dev1.gestionbudget.mouvement;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.inject.Named;

import org.springframework.jdbc.core.RowMapper;

import com.dev1.gestionbudget.model.bean.Mouvement;
import com.dev1.gestionbudget.mouvement.MouvementDao.Field;

@Named
public class MouvementRowMapper implements RowMapper<Mouvement> {

	@Override
	public Mouvement mapRow(ResultSet rs, int rwNumber) throws SQLException {
		Mouvement mouvement = new Mouvement();
		mouvement.setId(		rs.getLong(		Field.ID.getAliasColumnName())			);
		mouvement.setDate(		rs.getDate(		Field.DATE.getAliasColumnName())		);
		mouvement.setLibelle(	rs.getString(	Field.LIBELLE.getAliasColumnName())		);
		mouvement.setMontant(	rs.getDouble(	Field.MONTANT.getAliasColumnName())		);
		mouvement.setExercice(	rs.getInt(		Field.EXERCICE.getAliasColumnName())	);
		mouvement.setMois(		rs.getInt(		Field.MOIS.getAliasColumnName())		);
		return mouvement;
	}

}
