package banque.modele.implementations;

import banque.modele.abstractions.CompteBancaire;
import banque.modele.exceptions.SoldeInsuffisantException;
import banque.modele.exceptions.DecouvertDepasseException;
import banque.modele.operations.TypeTransaction;

public class CompteEpargne extends CompteBancaire {
    private static final double TAUX_INTERET_ANNUEL = 0.02;
    private static final double RETRAIT_MINIMUM = 100.0;
    private static final double PLAFOND_DEPOT = 50000.0;
    
    public CompteEpargne(String numeroCompte, String clientId) {
        super(numeroCompte, clientId);
    }
    
    @Override
    public void deposer(double montant) {
        if (montant <= 0) {
            throw new IllegalArgumentException("Le montant doit être positif");
        }
        
        if (solde + montant > PLAFOND_DEPOT) {
            throw new IllegalArgumentException(
                String.format("Plafond de dépôt dépassé. Plafond: %.2f €", PLAFOND_DEPOT));
        }
        
        super.deposer(montant);
    }
    
    @Override
    public void retirer(double montant) throws SoldeInsuffisantException {
        // Pour CompteEpargne, pas de DecouvertDepasseException
        if (montant < RETRAIT_MINIMUM) {
            throw new IllegalArgumentException(
                String.format("Retrait minimum non respecté. Minimum: %.2f €", RETRAIT_MINIMUM));
        }
        
        if (montant > solde) {
            throw new SoldeInsuffisantException(
                String.format("Solde insuffisant pour retirer %.2f €", montant));
        }
        
        this.solde -= montant;
        ajouterTransaction(TypeTransaction.RETRAIT, -montant, "Retrait compte épargne");
    }
    
    @Override
    public void calculerInterets() {
        double interets = solde * (TAUX_INTERET_ANNUEL / 12);
        this.solde += interets;
        ajouterTransaction(TypeTransaction.INTERETS, interets, 
            "Intérêts crédités mensuellement");
    }
    
    @Override
    public double getDecouvertAutorise() {
        return 0; // Pas de découvert pour l'épargne
    }
    
    @Override
    public double getTauxInteret() {
        return TAUX_INTERET_ANNUEL;
    }
    
    // Méthodes spécifiques
    public double simulerInterets(int mois) {
        double tauxMensuel = TAUX_INTERET_ANNUEL / 12;
        return solde * Math.pow(1 + tauxMensuel, mois) - solde;
    }
}