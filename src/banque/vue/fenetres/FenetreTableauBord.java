package banque.vue.fenetres;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
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

        /* ===================== TABLE COMPTES ===================== */
        String[] colonnesComptes = {"Numéro", "Type", "Solde", "IBAN"};
        DefaultTableModel modeleComptes = new DefaultTableModel(colonnesComptes, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tableComptes = new JTable(modeleComptes);
        tableComptes.setRowHeight(25);
        tableComptes.setFillsViewportHeight(true);
        styliserHeader(tableComptes);

        /* ===================== TABLE TRANSACTIONS ===================== */
        String[] colonnesTransactions = {"Date", "Type", "Montant", "Description"};
        DefaultTableModel modeleTransactions = new DefaultTableModel(colonnesTransactions, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tableTransactions = new JTable(modeleTransactions);
        tableTransactions.setRowHeight(25);
        tableTransactions.setFillsViewportHeight(true);
        styliserHeader(tableTransactions);

        /* ===================== BOUTONS ===================== */
        boutonDeconnexion = creerBouton("Déconnexion", new Color(220, 53, 69));
        boutonVirementInterne = creerBouton("Virement interne", new Color(0, 123, 255));
        boutonVirementExterne = creerBouton("Virement externe", new Color(23, 162, 184));
        boutonHistorique = creerBouton("Historique complet", new Color(108, 117, 125));
        boutonRelevesPDF = creerBouton("Générer relevé PDF", new Color(255, 193, 7));
    }

    /* ===================== STYLE HEADER TABLE ===================== */
    private void styliserHeader(JTable table) {
        JTableHeader header = table.getTableHeader();
        header.setReorderingAllowed(false);

        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable table, Object value, boolean isSelected,
                    boolean hasFocus, int row, int column) {

                JLabel label = (JLabel) super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column);

                label.setBackground(new Color(70, 130, 180));
                label.setForeground(Color.WHITE);
                label.setFont(new Font("Arial", Font.BOLD, 12));
                label.setHorizontalAlignment(SwingConstants.CENTER);
                label.setOpaque(true);

                return label;
            }
        });
    }

    private JButton creerBouton(String texte, Color couleur) {
        JButton bouton = new JButton(texte);
        bouton.setBackground(couleur);
        bouton.setForeground(Color.BLACK);
        bouton.setFocusPainted(false);
        bouton.setFont(new Font("Arial", Font.BOLD, 12));
        return bouton;
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

        panelCentral.add(creerPanel("Mes comptes", tableComptes));
        panelCentral.add(creerPanel("Transactions récentes", tableTransactions));

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

    private JPanel creerPanel(String titre, JTable table) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(70, 130, 180), 2),
                titre,
                javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                javax.swing.border.TitledBorder.DEFAULT_POSITION,
                new Font("Arial", Font.BOLD, 14),
                new Color(0, 51, 102)
        ));
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        return panel;
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
        labelSoldeTotal.setText(String.format("Solde total: %.2f €", client.getSoldeTotal()));

        DefaultTableModel modeleComptes = (DefaultTableModel) tableComptes.getModel();
        modeleComptes.setRowCount(0);

        for (CompteBancaire compte : client.getComptes()) {
            String type = compte.getClass().getSimpleName().replace("Compte", "Compte ");
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
            JOptionPane.showMessageDialog(this, "Aucun compte disponible", "Erreur", JOptionPane.WARNING_MESSAGE);
            return;
        }
        new FenetreVirement(this, client).setVisible(true);
    }

    private void ouvrirFenetreVirementExterne() {
        if (client.getComptes().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Aucun compte disponible", "Erreur", JOptionPane.WARNING_MESSAGE);
            return;
        }
        new FenetreVirementExterne(this, client).setVisible(true);
    }

    private void ouvrirFenetreHistorique() {
        new FenetreHistorique(client).setVisible(true);
    }

    private void ouvrirFenetreRelevesPDF() {
        new FenetreRelevesPDF(client).setVisible(true);
    }
}
