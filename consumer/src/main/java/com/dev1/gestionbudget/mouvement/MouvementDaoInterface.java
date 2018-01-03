package com.dev1.gestionbudget.mouvement;

import java.util.List;

import com.dev1.generic.model.dao.StandardDaoInterface;
import com.dev1.gestionbudget.model.bean.Mouvement;
import com.dev1.gestionbudget.model.exception.NotFoundException;

public interface MouvementDaoInterface extends StandardDaoInterface {

//	public Mouvement getMouvementId(Integer pId) throws NotFoundException;
//	public void addMouvement(Mouvement pMouvement) throws Exception;
	
	public List<Mouvement> getLstMouvement(int exercice) throws NotFoundException;
	
}
