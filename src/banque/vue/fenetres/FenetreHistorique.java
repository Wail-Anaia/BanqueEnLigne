package banque.vue.fenetres;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
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
        super((JFrame) null, "Historique des transactions", true);
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

        /* ===================== COMBO COMPTES ===================== */
        comboComptes = new JComboBox<>();
        comboComptes.setFont(new Font("Arial", Font.PLAIN, 12));

        for (CompteBancaire compte : client.getComptes()) {
            String type = compte instanceof banque.modele.implementations.CompteCourant
                    ? "Courant" : "Épargne";

            comboComptes.addItem(String.format(
                    "%s (%s) - %.2f €",
                    compte.getNumeroCompte(),
                    type,
                    compte.getSolde()
            ));
        }

        /* ===================== TABLE HISTORIQUE ===================== */
        String[] colonnes = {"Date", "Type", "Montant", "Solde après", "Description"};
        DefaultTableModel modele = new DefaultTableModel(colonnes, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tableHistorique = new JTable(modele);
        tableHistorique.setRowHeight(25);
        tableHistorique.setFillsViewportHeight(true);
        tableHistorique.setAutoCreateRowSorter(true);

        styliserHeader(tableHistorique);

        /* ===================== INFOS ===================== */
        labelInfo = new JLabel("Sélectionnez un compte pour voir son historique");
        labelInfo.setFont(new Font("Arial", Font.PLAIN, 12));

        /* ===================== BOUTON FERMER ===================== */
        boutonFermer = new JButton("Fermer");
        boutonFermer.setBackground(new Color(108, 117, 125));
        boutonFermer.setForeground(Color.BLACK);
        boutonFermer.setFocusPainted(false);
        boutonFermer.setFont(new Font("Arial", Font.BOLD, 12));
    }

    /* ===================== STYLE HEADER ===================== */
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

    private void configurerLayout() {

        setLayout(new BorderLayout(10, 10));

        JPanel panelControle = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panelControle.setBackground(new Color(240, 240, 240));
        panelControle.add(new JLabel("Compte:"));
        panelControle.add(comboComptes);
        panelControle.add(labelInfo);

        add(panelControle, BorderLayout.NORTH);

        add(new JScrollPane(tableHistorique), BorderLayout.CENTER);

        JPanel panelBas = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panelBas.setBackground(new Color(240, 240, 240));

        JLabel info = new JLabel("Historique des 12 derniers mois");
        info.setFont(new Font("Arial", Font.ITALIC, 12));

        panelBas.add(info);
        panelBas.add(boutonFermer);

        add(panelBas, BorderLayout.SOUTH);
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

        labelInfo.setText(String.format(
                "Compte %s - %d transactions",
                compte.getNumeroCompte(),
                compte.getHistorique().size()
        ));

        List<Transaction> transactions = compte.getHistorique12Mois();

        DefaultTableModel modele = (DefaultTableModel) tableHistorique.getModel();
        modele.setRowCount(0);

        double soldeCumule = 0;

        for (Transaction t : transactions) {
            soldeCumule += t.getMontant();
            modele.addRow(new Object[]{
                    t.getDateFormatee(),
                    t.getType().getLibelle(),
                    String.format("%.2f €", t.getMontant()),
                    String.format("%.2f €", soldeCumule),
                    t.getDescription()
            });
        }

        // Trier par date décroissante
        tableHistorique.getRowSorter().toggleSortOrder(0);
        tableHistorique.getRowSorter().toggleSortOrder(0);
    }
}
