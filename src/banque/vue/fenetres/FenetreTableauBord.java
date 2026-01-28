package banque.vue.fenetres;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import banque.controleurs.AuthControleur;
import banque.modele.utilisateur.Client;
import banque.modele.abstractions.CompteBancaire;
import banque.modele.operations.Transaction;
import java.util.List;

public class FenetreTableauBord extends JPanel {
    private FenetrePrincipale parent;
    private AuthControleur authControleur;
    private Client client;
    
    private JLabel labelBienvenue;
    private JLabel labelSoldeTotal;
    private JTable tableComptes;
    private JTable tableTransactions;
    private JButton boutonDeconnexion;
    private JButton boutonVirementInterne;
    private JButton boutonVirementExterne;
    private JButton boutonHistorique;
    private JButton boutonRelevesPDF;
    
    public FenetreTableauBord(FenetrePrincipale parent, AuthControleur authControleur) {
        this.parent = parent;
        this.authControleur = authControleur;
        this.client = authControleur.getClientConnecte();
        
        initialiserComposants();
        configurerLayout();
        configurerActions();
    }
    
    private void initialiserComposants() {
        labelBienvenue = new JLabel();
        labelBienvenue.setFont(new Font("Arial", Font.BOLD, 16));
        labelBienvenue.setForeground(new Color(0, 51, 102));
        
        labelSoldeTotal = new JLabel("Solde total: 0,00 €");
        labelSoldeTotal.setFont(new Font("Arial", Font.BOLD, 16));
        labelSoldeTotal.setForeground(new Color(0, 100, 0));
        
        String[] colonnesComptes = {"Numéro", "Type", "Solde", "IBAN"};
        DefaultTableModel modeleComptes = new DefaultTableModel(colonnesComptes, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableComptes = new JTable(modeleComptes);
        tableComptes.setRowHeight(25);
        tableComptes.getTableHeader().setReorderingAllowed(false);
        tableComptes.getTableHeader().setBackground(new Color(70, 130, 180));
        tableComptes.getTableHeader().setForeground(Color.WHITE);
        tableComptes.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        
        String[] colonnesTransactions = {"Date", "Type", "Montant", "Description"};
        DefaultTableModel modeleTransactions = new DefaultTableModel(colonnesTransactions, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableTransactions = new JTable(modeleTransactions);
        tableTransactions.setRowHeight(25);
        tableTransactions.getTableHeader().setReorderingAllowed(false);
        tableTransactions.getTableHeader().setBackground(new Color(70, 130, 180));
        tableTransactions.getTableHeader().setForeground(Color.WHITE);
        tableTransactions.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        
        boutonDeconnexion = new JButton("Déconnexion");
        boutonDeconnexion.setBackground(new Color(220, 53, 69));
        boutonDeconnexion.setForeground(Color.BLACK);
        boutonDeconnexion.setFocusPainted(false);
        boutonDeconnexion.setFont(new Font("Arial", Font.BOLD, 12));
        
        boutonVirementInterne = new JButton("Virement interne");
        boutonVirementInterne.setBackground(new Color(0, 123, 255));
        boutonVirementInterne.setForeground(Color.BLACK);
        boutonVirementInterne.setFocusPainted(false);
        boutonVirementInterne.setFont(new Font("Arial", Font.BOLD, 12));
        
        boutonVirementExterne = new JButton("Virement externe");
        boutonVirementExterne.setBackground(new Color(23, 162, 184));
        boutonVirementExterne.setForeground(Color.BLACK);
        boutonVirementExterne.setFocusPainted(false);
        boutonVirementExterne.setFont(new Font("Arial", Font.BOLD, 12));
        
        boutonHistorique = new JButton("Historique complet");
        boutonHistorique.setBackground(new Color(108, 117, 125));
        boutonHistorique.setForeground(Color.BLACK);
        boutonHistorique.setFocusPainted(false);
        boutonHistorique.setFont(new Font("Arial", Font.BOLD, 12));
        
        boutonRelevesPDF = new JButton("Générer relevé PDF");
        boutonRelevesPDF.setBackground(new Color(255, 193, 7));
        boutonRelevesPDF.setForeground(Color.BLACK);
        boutonRelevesPDF.setFocusPainted(false);
        boutonRelevesPDF.setFont(new Font("Arial", Font.BOLD, 12));
    }
    
    private void configurerLayout() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setBackground(new Color(240, 240, 240));
        
        JPanel panelEnTete = new JPanel(new BorderLayout());
        panelEnTete.setOpaque(false);
        panelEnTete.add(labelBienvenue, BorderLayout.WEST);
        panelEnTete.add(labelSoldeTotal, BorderLayout.EAST);
        add(panelEnTete, BorderLayout.NORTH);
        
        JPanel panelCentral = new JPanel(new GridLayout(1, 2, 20, 0));
        panelCentral.setOpaque(false);
        
        JPanel panelComptes = new JPanel(new BorderLayout());
        panelComptes.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(70, 130, 180), 2),
            "Mes comptes",
            javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
            javax.swing.border.TitledBorder.DEFAULT_POSITION,
            new Font("Arial", Font.BOLD, 14),
            new Color(0, 51, 102)
        ));
        panelComptes.setBackground(Color.WHITE);
        panelComptes.add(new JScrollPane(tableComptes), BorderLayout.CENTER);
        
        JPanel panelTransactions = new JPanel(new BorderLayout());
        panelTransactions.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(70, 130, 180), 2),
            "Transactions récentes",
            javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
            javax.swing.border.TitledBorder.DEFAULT_POSITION,
            new Font("Arial", Font.BOLD, 14),
            new Color(0, 51, 102)
        ));
        panelTransactions.setBackground(Color.WHITE);
        panelTransactions.add(new JScrollPane(tableTransactions), BorderLayout.CENTER);
        
        panelCentral.add(panelComptes);
        panelCentral.add(panelTransactions);
        
        add(panelCentral, BorderLayout.CENTER);
        
        JPanel panelBoutons = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        panelBoutons.setOpaque(false);
        panelBoutons.add(boutonVirementInterne);
        panelBoutons.add(boutonVirementExterne);
        panelBoutons.add(boutonHistorique);
        panelBoutons.add(boutonRelevesPDF);
        panelBoutons.add(boutonDeconnexion);
        
        add(panelBoutons, BorderLayout.SOUTH);
    }
    
    private void configurerActions() {
        boutonDeconnexion.addActionListener(e -> {
            authControleur.deconnecter();
            parent.afficherConnexion();
        });
        
        boutonVirementInterne.addActionListener(e -> ouvrirFenetreVirementInterne());
        boutonVirementExterne.addActionListener(e -> ouvrirFenetreVirementExterne());
        boutonHistorique.addActionListener(e -> ouvrirFenetreHistorique());
        boutonRelevesPDF.addActionListener(e -> ouvrirFenetreRelevesPDF());
    }
    
    public void actualiserDonnees() {
        client = authControleur.getClientConnecte();
        if (client == null) return;
        
        labelBienvenue.setText("Bienvenue, " + client.getNomComplet());
        double soldeTotal = client.getSoldeTotal();
        labelSoldeTotal.setText(String.format("Solde total: %.2f €", soldeTotal));
        
        DefaultTableModel modeleComptes = (DefaultTableModel) tableComptes.getModel();
        modeleComptes.setRowCount(0);
        
        for (CompteBancaire compte : client.getComptes()) {
            String type = compte instanceof banque.modele.implementations.CompteCourant ? 
                "Compte Courant" : "Compte Épargne";
            modeleComptes.addRow(new Object[]{
                compte.getNumeroCompte(),
                type,
                String.format("%.2f €", compte.getSolde()),
                compte.getIban()
            });
        }
        
        DefaultTableModel modeleTransactions = (DefaultTableModel) tableTransactions.getModel();
        modeleTransactions.setRowCount(0);
        
        int compteur = 0;
        for (CompteBancaire compte : client.getComptes()) {
            List<Transaction> transactions = compte.getHistorique();
            for (int i = Math.max(0, transactions.size() - 5); i < transactions.size(); i++) {
                if (compteur >= 10) break;
                Transaction t = transactions.get(i);
                modeleTransactions.addRow(new Object[]{
                    t.getDateFormatee(),
                    t.getType().getLibelle(),
                    String.format("%.2f €", t.getMontant()),
                    t.getDescription()
                });
                compteur++;
            }
            if (compteur >= 10) break;
        }
    }
    
    private void ouvrirFenetreVirementInterne() {
        if (client.getComptes().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Vous devez avoir au moins un compte pour effectuer un virement",
                "Aucun compte",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        FenetreVirement fenetreVirement = new FenetreVirement(this, client);
        fenetreVirement.setVisible(true);
    }
    
    private void ouvrirFenetreVirementExterne() {
        if (client.getComptes().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Vous devez avoir au moins un compte pour effectuer un virement",
                "Aucun compte",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        FenetreVirementExterne fenetreVirementExterne = new FenetreVirementExterne(this, client);
        fenetreVirementExterne.setVisible(true);
    }
    
    private void ouvrirFenetreHistorique() {
        if (client.getComptes().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Vous n'avez aucun compte",
                "Aucun compte",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        FenetreHistorique fenetreHistorique = new FenetreHistorique(client);
        fenetreHistorique.setVisible(true);
    }
    
    private void ouvrirFenetreRelevesPDF() {
        if (client.getComptes().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Vous n'avez aucun compte",
                "Aucun compte",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        FenetreRelevesPDF fenetreRelevesPDF = new FenetreRelevesPDF(client);
        fenetreRelevesPDF.setVisible(true);
    }
}