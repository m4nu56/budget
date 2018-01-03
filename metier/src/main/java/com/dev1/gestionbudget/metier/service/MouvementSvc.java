package com.dev1.gestionbudget.metier.service;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.transaction.annotation.Transactional;

import com.dev1.generic.utils.formulaire.CRUD;
import com.dev1.gestionbudget.model.bean.Mouvement;
import com.dev1.gestionbudget.model.exception.NotFoundException;
import com.dev1.gestionbudget.mouvement.MouvementDao;

@Transactional
@Named
public class MouvementSvc implements MouvementSvcInterface {

	@Inject
	private MouvementDao mouvementDao;

	@Override
	public Mouvement getBeanById(Integer pId) throws NotFoundException {
		if (pId < 1) {
            throw new NotFoundException("Projet non trouvé : ID=" + pId);
		}
		
		return mouvementDao.getBeanById(pId);
	}

	@Override
	public void create(Mouvement pMouvement) throws Throwable {
		if(pMouvement==null)
			throw new Exception("Impossible d'insérer un mouvement null");

		mouvementDao.crud(pMouvement, CRUD.CREATE);
	}

	@Override
	public List<Mouvement> getLstMouvement(int exercice) throws NotFoundException {
		if(exercice < 1) {
			throw new NotFoundException("Impossible de remonter les mouvements de l'exercice " + exercice);
		}
		
		return mouvementDao.getLstMouvement(exercice);
		
	}
	
	
}
