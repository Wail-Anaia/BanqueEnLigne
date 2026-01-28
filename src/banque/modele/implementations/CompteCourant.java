package banque.modele.implementations;

import banque.modele.abstractions.CompteBancaire;
import banque.modele.exceptions.DecouvertDepasseException;
import banque.modele.exceptions.SoldeInsuffisantException;
import banque.modele.operations.TypeTransaction;

public class CompteCourant extends CompteBancaire {
    private double decouvertAutorise;
    private double fraisDecouvert;
    
    public CompteCourant(String numeroCompte, String clientId) {
        super(numeroCompte, clientId);
        this.decouvertAutorise = DECOUVERT_AUTORISE;
        this.fraisDecouvert = 0.10;
    }
    
    public CompteCourant(String numeroCompte, String clientId, double decouvertAutorise) {
        super(numeroCompte, clientId);
        this.decouvertAutorise = decouvertAutorise;
        this.fraisDecouvert = 0.10;
    }
    
    @Override
    public void retirer(double montant) throws SoldeInsuffisantException, DecouvertDepasseException {
        if (montant <= 0) {
            throw new IllegalArgumentException("Le montant doit être positif");
        }
        
        double soldeApresRetrait = solde - montant;
        
        if (soldeApresRetrait < -decouvertAutorise) {
            throw new DecouvertDepasseException(
                String.format("Découvert autorisé dépassé. Découvert max: %.2f €", decouvertAutorise));
        }
        
        // Appliquer des frais si on passe en découvert
        if (solde >= 0 && soldeApresRetrait < 0) {
            double frais = Math.abs(soldeApresRetrait) * fraisDecouvert / 365;
            soldeApresRetrait -= frais;
            ajouterTransaction(TypeTransaction.FRAIS, -frais, "Frais de découvert");
        }
        
        this.solde = soldeApresRetrait;
        ajouterTransaction(TypeTransaction.RETRAIT, -montant, "Retrait au guichet");
    }
    
    @Override
    public void calculerInterets() {
        // Pas d'intérêts créditeurs pour les comptes courants
        // Mais calcul des frais de découvert
        if (solde < 0) {
            double frais = Math.abs(solde) * fraisDecouvert / 30; // Frais mensuels
            this.solde -= frais;
            ajouterTransaction(TypeTransaction.FRAIS, -frais, 
                "Frais mensuels de découvert");
        }
    }
    
    @Override
    public double getDecouvertAutorise() {
        return decouvertAutorise;
    }
    
    @Override
    public double getTauxInteret() {
        return 0.0; // Pas de taux d'intérêt créditeur
    }
    
    // Méthodes spécifiques
    public void modifierDecouvert(double nouveauDecouvert) {
        this.decouvertAutorise = nouveauDecouvert;
    }
    
    public double getFraisDecouvert() {
        return fraisDecouvert;
    }
    
    public void setFraisDecouvert(double fraisDecouvert) {
        this.fraisDecouvert = fraisDecouvert;
    }
}