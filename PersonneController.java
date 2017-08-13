package aubay.GestionScolaire.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import GestionScolaire.metier.dao.EtablissementDao;
import GestionScolaire.metier.dao.MatiereDao;
import GestionScolaire.metier.dao.MatiereProfesseurDao;
import GestionScolaire.metier.dao.PersonneDao;
import GestionScolaire.metier.dao.ProfesseurDao;
import GestionScolaire.metier.dao.UserDao;
import GestionScolaire.metier.model.Civilite;
import GestionScolaire.metier.model.Personne;
import GestionScolaire.metier.model.Professeur;
import GestionScolaire.metier.model.ProfesseurMatiere;
import GestionScolaire.metier.model.User;


@Controller
@RequestMapping("/personne")
public class PersonneController {
	
	@Autowired
	private PersonneDao personneDao;
	
	@Autowired
	private MatiereProfesseurDao matiereProfesseurDao;
	
	@Autowired
	private MatiereDao matiereDao;
	
	@Autowired
	private EtablissementDao etablissementDao;
	
	@Autowired
	private ProfesseurDao professeurDao;
	
	@Autowired
	private UserDao userDao;
	
	@RequestMapping("/listProf")
	public String listProf(Model model){
		List<Professeur> professeurs =professeurDao.findAll();
		
		
		
		
		model.addAttribute("professeurs", professeurs);
		
		
		return "professeur/professeurs";
	}
	
	@RequestMapping("/listUser")
	public String listUser(Model model){
		List<User> users =userDao.findAll();
		
		model.addAttribute("users", users);
		
		return "user/users";
	}

	@RequestMapping("/addProf")
	public String addProf(Model model){
		
		model.addAttribute("personne", (Personne) (new Professeur()));
		
		//Envoi de la liste des matières à la JSP
		model.addAttribute("matieres", matiereDao.findAll());
		//Envoi des valeurs de Civilite à la JSP
		model.addAttribute("civilites", Civilite.values());
		
		return "professeur/professeurEdit";
	}
	
	@RequestMapping("/addUser")
	public String addUser(Model model){
		model.addAttribute("user", new User());
		//Envoi de la liste des établissements à la JSP
		model.addAttribute("etablissements", etablissementDao.findAll());
		
		
		return "user/users";
	}
	
	/*@RequestMapping("/addEleve")
	public String addEleve(Model model){
		model.addAttribute("eleve", new Eleve());
		model.addAttribute("civilites", Civilite.values());
		
		return "personne/eleve";
	}*/
		
		
	@RequestMapping("/edit")
	public String edit(@RequestParam(name = "id", required = true) Long id, Model model){
		
		Personne personne = personneDao.find(id);
		model.addAttribute("personne", personne);
		if (personne instanceof Professeur){
			//Envoi de la liste des matières à la JSP
			//model.addAttribute("matiereProfesseurs", matiereProfesseurDao.findAll());
			//Envoi des valeurs de Civilite à la JSP
			model.addAttribute("civilites", Civilite.values());
			
			//Envoi à la JSP correspondant à l'édition d'un professeur
			return "professeur/professeurEdit";
		}
		else {
			//Envoi à la JSP correspondant à l'édition d'un utilisateur
			return "user/users";
		}
	}
	
	@RequestMapping(value="/saveProf", method=RequestMethod.POST)
	public String saveProf(@ModelAttribute("personne") Professeur personne) {
		//Création d'un nouvel objet Professeur
		if (personne.getId() == null){
			
			personneDao.create(personne);
//			for (int i=0; i < matieres.size(); i++ ){
//				matiereProfesseurDao.create(matiereProfs[i]);
//			}
			 //création d'une relation
		}
		//Mise à jour d'un objet Professeur existant
		else{
			personneDao.update(personne);
		}
		
		return "redirect:listProf";
		
	}
	
	@RequestMapping(value="/saveUser", method=RequestMethod.POST)
	public String saveUser(@ModelAttribute("user") User user){
		//Création d'un nouvel objet User
		if (user.getId() == null){
			personneDao.create(user);
		}
		//Mise à jour d'un objet Professeur existant
		else{
			personneDao.update(user);
		}
		
		return "redirect:listUser";
		
	}
	
	/*@RequestMapping(value="/saveEleve", method=RequestMethod.POST)
	public String save(@ModelAttribute("eleve") Eleve eleve){
		if (eleve.getId() == null){
			personneDao.create(eleve);
		}
		else{
			personneDao.update(eleve);
		}
		
		return "redirect:list";
		
	}*/
	
	
	@RequestMapping(value="/deleteProf", method=RequestMethod.GET)
	public String deleteProf(@RequestParam(name = "id", required = true) Long id){
		Personne personne = personneDao.find(id);
		personneDao.delete(personne);
		
		return "forward:listProf";
	}
	
	@RequestMapping(value="/deleteUser", method=RequestMethod.GET)
	public String deleteUser(@RequestParam(name = "id", required = true) Long id){
		Personne personne = personneDao.find(id);
		personneDao.delete(personne);
		
		return "forward:listUser";
	}
	
}
