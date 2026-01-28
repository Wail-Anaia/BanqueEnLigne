package banque.vue.fenetres;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import banque.modele.utilisateur.Client;
import banque.modele.abstractions.CompteBancaire;
import banque.modele.operations.Transaction;
import java.util.List;

public class FenetreHistorique extends JDialog {
    private Client client;
    
    private JComboBox<String> comboComptes;
    private JTable tableHistorique;
    private JLabel labelInfo;
    private JButton boutonFermer;
    
    public FenetreHistorique(Client client) {
        super((JFrame)null, "Historique des transactions", true);
        this.client = client;
        
        setSize(800, 600);
        setLocationRelativeTo(null);
        
        initialiserComposants();
        configurerLayout();
        configurerActions();
        
        if (!client.getComptes().isEmpty()) {
            chargerHistorique(0);
        }
    }
    
    private void initialiserComposants() {
        comboComptes = new JComboBox<>();
        comboComptes.setFont(new Font("Arial", Font.PLAIN, 12));
        for (CompteBancaire compte : client.getComptes()) {
            String type = compte instanceof banque.modele.implementations.CompteCourant ? 
                "Courant" : "Épargne";
            comboComptes.addItem(String.format("%s (%s) - %.2f €", 
                compte.getNumeroCompte(), type, compte.getSolde()));
        }
        
        String[] colonnes = {"Date", "Type", "Montant", "Solde après", "Description"};
        DefaultTableModel modele = new DefaultTableModel(colonnes, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableHistorique = new JTable(modele);
        tableHistorique.setRowHeight(25);
        tableHistorique.getTableHeader().setReorderingAllowed(false);
        tableHistorique.getTableHeader().setBackground(new Color(70, 130, 180));
        tableHistorique.getTableHeader().setForeground(Color.WHITE);
        tableHistorique.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        tableHistorique.setAutoCreateRowSorter(true);
        
        labelInfo = new JLabel("Sélectionnez un compte pour voir son historique");
        labelInfo.setFont(new Font("Arial", Font.PLAIN, 12));
        
        // CHANGEMENT ICI : Bouton avec texte noir
        boutonFermer = new JButton("Fermer");
        boutonFermer.setBackground(new Color(108, 117, 125)); // Gris
        boutonFermer.setForeground(Color.BLACK); // Texte noir
        boutonFermer.setFocusPainted(false);
        boutonFermer.setFont(new Font("Arial", Font.BOLD, 12));
    }
    
    private void configurerLayout() {
        setLayout(new BorderLayout(10, 10));
        
        JPanel panelControle = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panelControle.setBackground(new Color(240, 240, 240));
        panelControle.add(new JLabel("Compte:"));
        panelControle.add(comboComptes);
        panelControle.add(labelInfo);
        
        add(panelControle, BorderLayout.NORTH);
        
        JScrollPane scrollPane = new JScrollPane(tableHistorique);
        add(scrollPane, BorderLayout.CENTER);
        
        JPanel panelInfo = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panelInfo.setBackground(new Color(240, 240, 240));
        JLabel info = new JLabel("Historique des 12 derniers mois");
        info.setFont(new Font("Arial", Font.ITALIC, 12));
        panelInfo.add(info);
        panelInfo.add(boutonFermer);
        
        add(panelInfo, BorderLayout.SOUTH);
    }
    
    private void configurerActions() {
        comboComptes.addActionListener(e -> {
            int index = comboComptes.getSelectedIndex();
            if (index >= 0) {
                chargerHistorique(index);
            }
        });
        
        boutonFermer.addActionListener(e -> dispose());
    }
    
    private void chargerHistorique(int indexCompte) {
        CompteBancaire compte = client.getComptes().get(indexCompte);
        
        labelInfo.setText(String.format("Compte %s - %d transactions", 
            compte.getNumeroCompte(), compte.getHistorique().size()));
        
        List<Transaction> transactions = compte.getHistorique12Mois();
        
        DefaultTableModel modele = (DefaultTableModel) tableHistorique.getModel();
        modele.setRowCount(0);
        
        double soldeCumule = 0;
        
        for (Transaction t : transactions) {
            soldeCumule += t.getMontant();
            modele.addRow(new Object[]{
                t.getDateFormatee(),
                t.getType().getLibelle(),
                t.getMontant(),
                String.format("%.2f €", soldeCumule),
                t.getDescription()
            });
        }
        
        tableHistorique.getRowSorter().toggleSortOrder(0);
        tableHistorique.getRowSorter().toggleSortOrder(0);
    }
}