package banque.services;

import banque.modele.abstractions.CompteBancaire;
import banque.modele.operations.Transaction;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HistoriqueService {
    
    public Map<String, Object> genererRapportMensuel(CompteBancaire compte, int mois, int annee) {
        Map<String, Object> rapport = new HashMap<>();
        
        List<Transaction> transactionsMois = filtrerParMois(compte.getHistorique(), mois, annee);
        
        double totalDepots = 0;
        double totalRetraits = 0;
        
        for (Transaction t : transactionsMois) {
            if (t.getMontant() > 0) {
                totalDepots += t.getMontant();
            } else {
                totalRetraits += t.getMontant();
            }
        }
        
        double soldeDebut = estimerSoldeDebutMois(compte, mois, annee);
        double soldeFin = soldeDebut + totalDepots + totalRetraits;
        
        rapport.put("mois", mois);
        rapport.put("annee", annee);
        rapport.put("soldeDebut", soldeDebut);
        rapport.put("soldeFin", soldeFin);
        rapport.put("totalDepots", totalDepots);
        rapport.put("totalRetraits", Math.abs(totalRetraits));
        rapport.put("nombreOperations", transactionsMois.size());
        rapport.put("transactions", transactionsMois);
        
        return rapport;
    }
    
    private List<Transaction> filtrerParMois(List<Transaction> transactions, int mois, int annee) {
        List<Transaction> resultat = new ArrayList<>();
        
        for (Transaction transaction : transactions) {
            LocalDateTime date = transaction.getDate();
            if (date.getMonthValue() == mois && date.getYear() == annee) {
                resultat.add(transaction);
            }
        }
        
        return resultat;
    }
    
    private double estimerSoldeDebutMois(CompteBancaire compte, int mois, int annee) {
        List<Transaction> transactions = compte.getHistorique();
        
        double soldeEstime = 0;
        for (Transaction transaction : transactions) {
            LocalDateTime date = transaction.getDate();
            if (date.getYear() < annee || (date.getYear() == annee && date.getMonthValue() < mois)) {
                soldeEstime += transaction.getMontant();
            }
        }
        
        return soldeEstime;
    }
}