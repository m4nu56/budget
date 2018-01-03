package com.dev1.gestionbudget.metier.service;

import java.util.List;

import com.dev1.gestionbudget.model.bean.Mouvement;
import com.dev1.gestionbudget.model.exception.NotFoundException;

/**
 * Manager des beans du package Mouvement
 */
public interface MouvementSvcInterface {
	
	public Mouvement getBeanById(Integer pId) throws NotFoundException;
	public void create(Mouvement pMouvement) throws Throwable;
	public List<Mouvement> getLstMouvement(int exercice) throws NotFoundException;

	
}
