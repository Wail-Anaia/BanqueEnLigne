package banque.controleurs;

import banque.modele.abstractions.CompteBancaire;
import banque.modele.implementations.CompteCourant;
import banque.modele.implementations.CompteEpargne;
import banque.modele.exceptions.SoldeInsuffisantException;
import java.util.ArrayList;
import java.util.List;

public class CompteControleur {
    private List<CompteBancaire> comptes;
    
    public CompteControleur() {
        this.comptes = new ArrayList<>();
        initialiserDonneesTest();
    }
    
    private void initialiserDonneesTest() {
        // Comptes pour jean.dupont@email.com (C001)
        CompteCourant cc1 = new CompteCourant("CC001", "C001", 500.0);
        cc1.deposer(1500.0);
        
        CompteEpargne ce1 = new CompteEpargne("CE001", "C001");
        ce1.deposer(3000.0);
        
        // Comptes pour marie.martin@email.com (C002)
        CompteCourant cc2 = new CompteCourant("CC002", "C002", 1000.0);
        cc2.deposer(2500.0);
        
        CompteEpargne ce2 = new CompteEpargne("CE002", "C002");
        ce2.deposer(5000.0);
        
        // Ajouter des transactions de test (sans déclencher d'exceptions)
        ajouterTransactionsTest(cc1);
        ajouterTransactionsTest(cc2);
        ajouterTransactionsTest(ce1);
        ajouterTransactionsTest(ce2);
        
        comptes.add(cc1);
        comptes.add(ce1);
        comptes.add(cc2);
        comptes.add(ce2);
    }
    
    private void ajouterTransactionsTest(CompteBancaire compte) {
        // Ajouter des transactions de test sans risquer des exceptions
        try {
            // Dépôts sûrs
            for (int i = 0; i < 3; i++) {
                compte.deposer(100 * (i + 1));
            }
            
            // Retraits uniquement si le solde est suffisant
            if (compte.getSolde() > 200) {
                try {
                    compte.retirer(50);
                } catch (Exception e) {
                    // Ignorer si retrait impossible
                }
            }
            
            if (compte.getSolde() > 100) {
                try {
                    compte.retirer(30);
                } catch (Exception e) {
                    // Ignorer si retrait impossible
                }
            }
            
        } catch (Exception e) {
            // Ignorer toutes les exceptions pendant l'initialisation des tests
            System.out.println("Erreur lors de l'ajout de transactions de test: " + e.getMessage());
        }
    }
    
    public List<CompteBancaire> getComptesParClient(String clientId) {
        List<CompteBancaire> resultat = new ArrayList<>();
        for (CompteBancaire compte : comptes) {
            if (compte.getClientId().equals(clientId)) {
                resultat.add(compte);
            }
        }
        return resultat;
    }
    
    public CompteBancaire creerCompteCourant(String clientId) {
        String numero = "CC" + String.format("%03d", getProchainNumeroCompte());
        CompteCourant compte = new CompteCourant(numero, clientId);
        comptes.add(compte);
        return compte;
    }
    
    public CompteBancaire creerCompteEpargne(String clientId) {
        String numero = "CE" + String.format("%03d", getProchainNumeroCompte());
        CompteEpargne compte = new CompteEpargne(numero, clientId);
        comptes.add(compte);
        return compte;
    }
    
    private int getProchainNumeroCompte() {
        return comptes.size() + 1;
    }
    
    public List<CompteBancaire> getTousLesComptes() {
        return new ArrayList<>(comptes);
    }
    
    public CompteBancaire getCompteParNumero(String numeroCompte) {
        for (CompteBancaire compte : comptes) {
            if (compte.getNumeroCompte().equals(numeroCompte)) {
                return compte;
            }
        }
        return null;
    }
}