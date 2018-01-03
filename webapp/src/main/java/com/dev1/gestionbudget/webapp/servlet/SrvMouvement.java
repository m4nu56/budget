package com.dev1.gestionbudget.webapp.servlet;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.dev1.gestionbudget.metier.service.MouvementSvc;
import com.dev1.gestionbudget.model.bean.Mouvement;
import com.dev1.gestionbudget.model.exception.NotFoundException;

@Controller
public class SrvMouvement {
	
	@Inject
	private MouvementSvc mouvementSvc;

	@RequestMapping("/mouvement")
    public Mouvement getMouvement(@RequestParam(value="id") Integer pId) throws NotFoundException {

    	 Mouvement vMouvement = mouvementSvc.getBeanById(pId);
         return vMouvement;

    }
	
	@RequestMapping("/mouvement/{exercice}")
	public String getLstMouvement(Model model, @PathVariable("exercice") int exercice) throws NotFoundException {
		
		List<Mouvement> lstMouvement = mouvementSvc.getLstMouvement(exercice);
		model.addAttribute("lstMouvement", lstMouvement);
//		return lstMouvement;
		return "mouvement";
		
	}
    
    @Path("/create")
    public void createMouvement(@QueryParam(value="montant") Double pMontant, @QueryParam(value="libelle") String pLibelle) throws Throwable {

    	 Mouvement mouvement = new Mouvement();
    	 
    	 mouvement.setDate(new Date());
    	 mouvement.setMontant(pMontant);
    	 mouvement.setLibelle(pLibelle);
    	 mouvement.setExercice(2012);
    	 
    	 mouvementSvc.create(mouvement);

    }

}
