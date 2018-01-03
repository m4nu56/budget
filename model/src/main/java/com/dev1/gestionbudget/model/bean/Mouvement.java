package com.dev1.gestionbudget.model.bean;

import java.util.Date;

public class Mouvement {

	private long id;
	private Date date;
	private String libelle;
	private double montant;
	private int exercice, mois;
	
	
	
    public Mouvement(Integer pId) {
    	this.id=pId;
	}



	/**
	 * 
	 */
	public Mouvement() {
	}



	public long getId() {
		return id;
	}



	public void setId(long id) {
		this.id = id;
	}



	public Date getDate() {
		return date;
	}



	public void setDate(Date date) {
		this.date = date;
	}



	public String getLibelle() {
		return libelle;
	}



	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}



	public double getMontant() {
		return montant;
	}



	public void setMontant(double montant) {
		this.montant = montant;
	}



	public int getExercice() {
		return exercice;
	}



	public void setExercice(int exercice) {
		this.exercice = exercice;
	}



	public int getMois() {
		return mois;
	}



	public void setMois(int mois) {
		this.mois = mois;
	}



	// ==================== Méthodes ====================
    public String toString() {
        final StringBuilder vStB = new StringBuilder(this.getClass().getSimpleName());
        final String vSEP = ", ";
        vStB.append(" {")
            .append("id=").append(id)
            .append(vSEP).append("date=\"").append(date).append('"')
            .append(vSEP).append("libelle=").append(libelle)
            .append(vSEP).append("montant=").append(montant)
            .append(vSEP).append("exerice=").append(exercice)
            .append(vSEP).append("mois=").append(mois)
            .append("}");
        return vStB.toString();
    }
	
}
