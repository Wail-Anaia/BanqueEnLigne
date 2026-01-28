package banque.modele.utilisateur;

import banque.modele.abstractions.CompteBancaire;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Client implements Serializable {
    private String id;
    private String nom;
    private String prenom;
    private String email;
    private String telephone;
    private String adresse;
    private String motDePasse;
    private List<CompteBancaire> comptes;
    
    public Client(String id, String nom, String prenom, String email, String motDePasse) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.motDePasse = motDePasse;
        this.comptes = new ArrayList<>();
    }
    
    public void ajouterCompte(CompteBancaire compte) {
        this.comptes.add(compte);
    }
    
    public CompteBancaire getCompteParNumero(String numeroCompte) {
        for (CompteBancaire compte : comptes) {
            if (compte.getNumeroCompte().equals(numeroCompte)) {
                return compte;
            }
        }
        return null;
    }
    
    public List<CompteBancaire> getComptesCourants() {
        List<CompteBancaire> comptesCourants = new ArrayList<>();
        for (CompteBancaire compte : comptes) {
            if (compte instanceof banque.modele.implementations.CompteCourant) {
                comptesCourants.add(compte);
            }
        }
        return comptesCourants;
    }
    
    public List<CompteBancaire> getComptesEpargne() {
        List<CompteBancaire> comptesEpargne = new ArrayList<>();
        for (CompteBancaire compte : comptes) {
            if (compte instanceof banque.modele.implementations.CompteEpargne) {
                comptesEpargne.add(compte);
            }
        }
        return comptesEpargne;
    }
    
    public double getSoldeTotal() {
        double total = 0;
        for (CompteBancaire compte : comptes) {
            total += compte.getSolde();
        }
        return total;
    }
    
    public String getId() { return id; }
    public String getNom() { return nom; }
    public String getPrenom() { return prenom; }
    public String getNomComplet() { return prenom + " " + nom; }
    public String getEmail() { return email; }
    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }
    public String getAdresse() { return adresse; }
    public void setAdresse(String adresse) { this.adresse = adresse; }
    public String getMotDePasse() { return motDePasse; }
    public List<CompteBancaire> getComptes() { return comptes; }
    
    public boolean verifierMotDePasse(String motDePasse) {
        return this.motDePasse.equals(motDePasse);
    }
}