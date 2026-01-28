package banque.modele.abstractions;

import banque.modele.exceptions.*;
import banque.modele.operations.Transaction;
import banque.modele.operations.TypeTransaction;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public abstract class CompteBancaire implements Serializable {
    protected String numeroCompte;
    protected String iban;
    protected double solde;
    protected String clientId;
    protected LocalDateTime dateCreation;
    protected List<Transaction> historique;
    
    public static final double DECOUVERT_AUTORISE = 1000.0;
    public static final double PLAFOND_VIREMENT = 5000.0;
    public static final double FRAIS_VIREMENT_EXTERNE = 1.50;
    
    public CompteBancaire(String numeroCompte, String clientId) {
        this.numeroCompte = numeroCompte;
        this.clientId = clientId;
        this.solde = 0.0;
        this.dateCreation = LocalDateTime.now();
        this.historique = new ArrayList<>();
        genererIBAN();
    }
    
    public abstract void calculerInterets();
    public abstract double getDecouvertAutorise();
    public abstract double getTauxInteret();
    
    public void deposer(double montant) {
        if (montant <= 0) {
            throw new IllegalArgumentException("Le montant doit être positif");
        }
        this.solde += montant;
        ajouterTransaction(TypeTransaction.DEPOT, montant, "Dépôt sur le compte");
    }
    
    public abstract void retirer(double montant) 
        throws SoldeInsuffisantException, DecouvertDepasseException;
    
    public void effectuerVirementInterne(CompteBancaire destination, double montant, String libelle) 
            throws SoldeInsuffisantException, PlafondDepasseException, DecouvertDepasseException {
        if (montant > PLAFOND_VIREMENT) {
            throw new PlafondDepasseException("Plafond de virement dépassé");
        }
        
        this.retirer(montant);
        destination.deposer(montant);
        
        ajouterTransaction(TypeTransaction.VIREMENT_EMIS, -montant, 
            "Virement vers " + destination.getNumeroCompte() + " - " + libelle);
        destination.ajouterTransaction(TypeTransaction.VIREMENT_RECU, montant, 
            "Virement de " + this.numeroCompte + " - " + libelle);
    }
    
    public void effectuerVirementExterne(String ibanDestinataire, String nomDestinataire, 
                                        double montant, String libelle) 
            throws SoldeInsuffisantException, PlafondDepasseException, DecouvertDepasseException {
        if (montant > PLAFOND_VIREMENT) {
            throw new PlafondDepasseException("Plafond de virement dépassé");
        }
        
        double montantAvecFrais = montant + FRAIS_VIREMENT_EXTERNE;
        
        // Vérifier si le solde est suffisant (avec frais)
        if (montantAvecFrais > solde + getDecouvertAutorise()) {
            throw new SoldeInsuffisantException("Solde insuffisant pour ce virement externe");
        }
        
        // Retirer le montant + frais
        this.retirer(montantAvecFrais);
        
        // Enregistrer les transactions
        ajouterTransaction(TypeTransaction.VIREMENT_EMIS, -montant, 
            "Virement externe vers " + nomDestinataire + " (" + ibanDestinataire + ") - " + libelle);
        ajouterTransaction(TypeTransaction.FRAIS, -FRAIS_VIREMENT_EXTERNE, 
            "Frais de virement externe vers " + ibanDestinataire);
    }
    
    protected void ajouterTransaction(TypeTransaction type, double montant, String description) {
        Transaction transaction = new Transaction(type, montant, description);
        historique.add(transaction);
        nettoyerHistorique();
    }
    
    private void nettoyerHistorique() {
        LocalDateTime limite = LocalDateTime.now().minusMonths(12);
        List<Transaction> historiqueFiltre = new ArrayList<>();
        
        for (Transaction t : historique) {
            if (!t.getDate().isBefore(limite)) {
                historiqueFiltre.add(t);
            }
        }
        
        historique = historiqueFiltre;
    }
    
    private void genererIBAN() {
        String banqueCode = "30002";
        String guichetCode = "00550";
        String numeroCompteComplet = String.format("%011d", Math.abs(numeroCompte.hashCode()));
        String cle = "76";
        
        this.iban = "FR" + cle + banqueCode + guichetCode + numeroCompteComplet + "00";
    }
    
    public List<Transaction> getHistorique12Mois() {
        LocalDateTime dateLimite = LocalDateTime.now().minusMonths(12);
        List<Transaction> historiqueFiltre = new ArrayList<>();
        
        for (Transaction t : historique) {
            if (t.getDate().isAfter(dateLimite)) {
                historiqueFiltre.add(t);
            }
        }
        
        return historiqueFiltre;
    }
    
    public double getSoldeMoyen12Mois() {
        if (historique.isEmpty()) return solde;
        return solde * 0.95;
    }
    
    public String getNumeroCompte() { return numeroCompte; }
    public String getIban() { return iban; }
    public double getSolde() { return solde; }
    public List<Transaction> getHistorique() { return new ArrayList<>(historique); }
    public LocalDateTime getDateCreation() { return dateCreation; }
    public String getClientId() { return clientId; }
    
    @Override
    public String toString() {
        return String.format("Compte %s - Solde: %.2f € - IBAN: %s", 
            numeroCompte, solde, iban);
    }
}