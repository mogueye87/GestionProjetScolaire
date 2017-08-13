package GestionScolaire.metier.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import GestionScolaire.metier.dao.EmploiDuTempsClasseDao;
import GestionScolaire.metier.dao.MatiereProfesseurDao;
import GestionScolaire.metier.dao.PersonneDao;
import GestionScolaire.metier.model.EmploiDuTempsClasse;
import GestionScolaire.metier.model.Personne;
import GestionScolaire.metier.model.Professeur;
import GestionScolaire.metier.model.ProfesseurMatiere;


@Transactional
@Repository
public class PersonneDaoJpa implements PersonneDao{
	
	@PersistenceContext
	private EntityManager em;
	
	@Autowired
	private MatiereProfesseurDao matiereProfesseurDao;
	
	@Autowired
	private EmploiDuTempsClasseDao emploiDuTempsClasseDao;

	@Override
	public Personne find(Long id) {
		return em.find(Personne.class, id);
		
	}

	@Override
	public List<Personne> findAll() {
		Query query = em.createQuery("from Personne p");
		return query.getResultList();
	}

	@Override
	public void create(Personne personne) {
		em.persist(personne);
		
	}
	
	//Un objet récupéré en base étant déjà managé, la methode update est utilisée pour merger cet objet
	@Override
	public Personne update(Personne personne) {
		return em.merge(personne);
	}

	@Override
	public void delete(Personne personne) {
		//Cas où l'objet personne à supprimer est un professeur (il faut supprimer les éventuelles relations avec Matiere)
				if (personne instanceof Professeur){
					for (ProfesseurMatiere profMat : ((Professeur) personne).getProfMatieres()) {
						matiereProfesseurDao.delete(profMat);
					}
					for (EmploiDuTempsClasse eDTClasse : ((Professeur) personne).getEmploiDuTempsClasses()){
						emploiDuTempsClasseDao.delete(eDTClasse);
					}
				em.remove(em.merge(personne));
				}
				else em.remove(personne);
		
	}

	@Override
	public void delete(Long id) {
		Personne personne = find(id);
		//Cas où l'objet personne à supprimer est un professeur (il faut supprimer les éventuelles relations avec Matiere)
		if (personne instanceof Professeur){
			for (ProfesseurMatiere profMat : ((Professeur) personne).getProfMatieres()) {
				matiereProfesseurDao.delete(profMat);
			}
			for (EmploiDuTempsClasse eDTClasse : ((Professeur) personne).getEmploiDuTempsClasses()){
				emploiDuTempsClasseDao.delete(eDTClasse);
			}
		em.remove(em.merge(personne));
		}
		//Cas où l'objet personne à supprimer est un utilisateur 
		else em.remove(personne);
	}

	@Override
	public Professeur find(String name) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
	

}