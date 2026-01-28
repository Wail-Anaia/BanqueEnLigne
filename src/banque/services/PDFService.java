package banque.services;

import banque.modele.abstractions.CompteBancaire;
import banque.modele.operations.Transaction;
import banque.modele.utilisateur.Client;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class PDFService {
    
    public void genererReleveMensuel(CompteBancaire compte, Client client, 
                                     int mois, int annee, String cheminFichier) {
        try (FileWriter writer = new FileWriter(cheminFichier)) {
            
            // En-tête
            writer.write("========================================\n");
            writer.write("           RELEVÉ BANCAIRE\n");
            writer.write("========================================\n\n");
            
            // Informations client
            writer.write("Client: " + client.getNomComplet() + "\n");
            writer.write("Email: " + client.getEmail() + "\n\n");
            
            // Informations compte
            writer.write("Compte: " + compte.getNumeroCompte() + "\n");
            writer.write("IBAN: " + compte.getIban() + "\n");
            writer.write("Type: " + (compte instanceof banque.modele.implementations.CompteCourant ? 
                "Compte Courant" : "Compte Épargne") + "\n");
            writer.write("Période: " + getMoisNom(mois) + " " + annee + "\n\n");
            
            // Solde
            double soldeInitial = calculerSoldeInitial(compte, mois, annee);
            writer.write("Solde initial: " + String.format("%.2f", soldeInitial) + " €\n");
            writer.write("Solde final: " + String.format("%.2f", compte.getSolde()) + " €\n\n");
            
            // Transactions
            writer.write("========================================\n");
            writer.write("          LISTE DES TRANSACTIONS\n");
            writer.write("========================================\n\n");
            
            List<Transaction> transactions = filtrerParMois(compte.getHistorique(), mois, annee);
            
            if (transactions.isEmpty()) {
                writer.write("Aucune transaction pour cette période.\n");
            } else {
                writer.write(String.format("%-20s %-15s %-10s %s\n", 
                    "Date", "Type", "Montant", "Description"));
                writer.write(String.format("%-20s %-15s %-10s %s\n", 
                    "--------------------", "---------------", "----------", "------------------------"));
                
                for (Transaction t : transactions) {
                    String signe = t.getMontant() >= 0 ? "+" : "";
                    writer.write(String.format("%-20s %-15s %-10s %s\n",
                        t.getDateFormatee(),
                        t.getType().getLibelle(),
                        signe + String.format("%.2f", t.getMontant()) + " €",
                        t.getDescription()));
                }
            }
            
            writer.write("\n========================================\n");
            writer.write("           RÉSUMÉ DU MOIS\n");
            writer.write("========================================\n\n");
            
            double totalDepots = transactions.stream()
                .filter(t -> t.getMontant() > 0)
                .mapToDouble(Transaction::getMontant)
                .sum();
            
            double totalRetraits = transactions.stream()
                .filter(t -> t.getMontant() < 0)
                .mapToDouble(Transaction::getMontant)
                .sum();
            
            writer.write("Total des dépôts: " + String.format("%.2f", totalDepots) + " €\n");
            writer.write("Total des retraits: " + String.format("%.2f", Math.abs(totalRetraits)) + " €\n");
            writer.write("Nombre d'opérations: " + transactions.size() + "\n\n");
            
            writer.write("Date d'édition: " + 
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")) + "\n");
            
            writer.write("\n========================================\n");
            writer.write("   Ce document a valeur probante\n");
            writer.write("   Banque en Ligne - Service Client\n");
            writer.write("========================================\n");
            
            System.out.println("Relevé généré: " + cheminFichier);
            
        } catch (IOException e) {
            System.err.println("Erreur lors de la génération du relevé: " + e.getMessage());
        }
    }
    
    public void genererReleveAnnuel(CompteBancaire compte, Client client, 
                                    int annee, String cheminFichier) {
        try (FileWriter writer = new FileWriter(cheminFichier)) {
            
            writer.write("========================================\n");
            writer.write("       RELEVÉ ANNUEL BANCAIRE\n");
            writer.write("========================================\n\n");
            
            writer.write("Client: " + client.getNomComplet() + "\n");
            writer.write("Compte: " + compte.getNumeroCompte() + "\n");
            writer.write("Année: " + annee + "\n\n");
            
            writer.write("========================================\n");
            writer.write("          SYNTHÈSE MENSUELLE\n");
            writer.write("========================================\n\n");
            
            double totalDepotsAnnuel = 0;
            double totalRetraitsAnnuel = 0;
            int totalOperationsAnnuel = 0;
            
            for (int mois = 1; mois <= 12; mois++) {
                List<Transaction> transactionsMois = filtrerParMois(compte.getHistorique(), mois, annee);
                
                double depotsMois = transactionsMois.stream()
                    .filter(t -> t.getMontant() > 0)
                    .mapToDouble(Transaction::getMontant)
                    .sum();
                
                double retraitsMois = transactionsMois.stream()
                    .filter(t -> t.getMontant() < 0)
                    .mapToDouble(Transaction::getMontant)
                    .sum();
                
                writer.write(getMoisNom(mois) + ":\n");
                writer.write("  Dépôts: " + String.format("%.2f", depotsMois) + " €\n");
                writer.write("  Retraits: " + String.format("%.2f", Math.abs(retraitsMois)) + " €\n");
                writer.write("  Opérations: " + transactionsMois.size() + "\n\n");
                
                totalDepotsAnnuel += depotsMois;
                totalRetraitsAnnuel += retraitsMois;
                totalOperationsAnnuel += transactionsMois.size();
            }
            
            writer.write("========================================\n");
            writer.write("            TOTAL ANNUEL\n");
            writer.write("========================================\n\n");
            
            writer.write("Total dépôts annuel: " + String.format("%.2f", totalDepotsAnnuel) + " €\n");
            writer.write("Total retraits annuel: " + String.format("%.2f", Math.abs(totalRetraitsAnnuel)) + " €\n");
            writer.write("Nombre total d'opérations: " + totalOperationsAnnuel + "\n");
            writer.write("Solde moyen: " + String.format("%.2f", compte.getSoldeMoyen12Mois()) + " €\n");
            writer.write("Solde actuel: " + String.format("%.2f", compte.getSolde()) + " €\n\n");
            
            writer.write("Date d'édition: " + 
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")) + "\n");
            
            System.out.println("Relevé annuel généré: " + cheminFichier);
            
        } catch (IOException e) {
            System.err.println("Erreur lors de la génération du relevé annuel: " + e.getMessage());
        }
    }
    
    private List<Transaction> filtrerParMois(List<Transaction> transactions, int mois, int annee) {
        return transactions.stream()
            .filter(t -> t.getDate().getMonthValue() == mois && t.getDate().getYear() == annee)
            .toList();
    }
    
    private double calculerSoldeInitial(CompteBancaire compte, int mois, int annee) {
        List<Transaction> transactionsMois = filtrerParMois(compte.getHistorique(), mois, annee);
        double totalMois = transactionsMois.stream()
            .mapToDouble(Transaction::getMontant)
            .sum();
        return compte.getSolde() - totalMois;
    }
    
    private String getMoisNom(int mois) {
        String[] noms = {"", "Janvier", "Février", "Mars", "Avril", "Mai", "Juin",
                        "Juillet", "Août", "Septembre", "Octobre", "Novembre", "Décembre"};
        return noms[mois];
    }
}