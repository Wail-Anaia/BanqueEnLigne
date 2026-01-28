package banque.controleurs;

import banque.modele.utilisateur.Client;
import java.util.HashMap;
import java.util.Map;

public class AuthControleur {
    private Map<String, Client> clients;
    private Client clientConnecte;
    private CompteControleur compteControleur;
    
    public AuthControleur() {
        this.clients = new HashMap<>();
        this.compteControleur = new CompteControleur();
        initialiserDonneesTest();
    }
    
    private void initialiserDonneesTest() {
        // Créer des clients de test
        Client client1 = new Client("C001", "Dupont", "Jean", "jean.dupont@email.com", "password123");
        Client client2 = new Client("C002", "Martin", "Marie", "marie.martin@email.com", "password456");
        
        // Ajouter des comptes aux clients
        for (banque.modele.abstractions.CompteBancaire compte : compteControleur.getTousLesComptes()) {
            if (compte.getClientId().equals("C001")) {
                client1.ajouterCompte(compte);
            } else if (compte.getClientId().equals("C002")) {
                client2.ajouterCompte(compte);
            }
        }
        
        clients.put("jean.dupont@email.com", client1);
        clients.put("marie.martin@email.com", client2);
    }
    
    public boolean authentifier(String email, String motDePasse) {
        Client client = clients.get(email);
        if (client != null && client.verifierMotDePasse(motDePasse)) {
            this.clientConnecte = client;
            return true;
        }
        return false;
    }
    
    public void deconnecter() {
        this.clientConnecte = null;
    }
    
    public boolean inscrireClient(String nom, String prenom, String email, String motDePasse) {
        if (clients.containsKey(email)) {
            return false;
        }
        
        String id = "C" + String.format("%03d", clients.size() + 1);
        Client nouveauClient = new Client(id, nom, prenom, email, motDePasse);
        
        // Créer un compte courant par défaut pour le nouveau client
        banque.modele.abstractions.CompteBancaire nouveauCompte = 
            compteControleur.creerCompteCourant(id);
        nouveauClient.ajouterCompte(nouveauCompte);
        
        clients.put(email, nouveauClient);
        return true;
    }
    
    public Client getClientConnecte() {
        return clientConnecte;
    }
    
    public boolean estConnecte() {
        return clientConnecte != null;
    }
}