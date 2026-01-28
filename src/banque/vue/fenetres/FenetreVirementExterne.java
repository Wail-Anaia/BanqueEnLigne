package banque.vue.fenetres;

import javax.swing.*;
import java.awt.*;
import banque.modele.utilisateur.Client;
import banque.modele.abstractions.CompteBancaire;
import banque.modele.exceptions.*;
import java.util.List;

public class FenetreVirementExterne extends JDialog {
    private FenetreTableauBord parent;
    private Client client;
    
    private JComboBox<String> comboSource;
    private JTextField champIbanDestinataire;
    private JTextField champNomDestinataire;
    private JTextField champMontant;
    private JTextField champLibelle;
    private JLabel labelFrais;
    private JLabel labelTotal;
    private JButton boutonValider;
    private JButton boutonAnnuler;
    
    public FenetreVirementExterne(FenetreTableauBord parent, Client client) {
        super((JFrame)SwingUtilities.getWindowAncestor(parent), "Virement externe", true);
        this.parent = parent;
        this.client = client;
        
        setSize(600, 500);
        setLocationRelativeTo(parent);
        
        initialiserComposants();
        configurerLayout();
        configurerActions();
        
        mettreAJourFraisEtTotal();
    }
    
    private void initialiserComposants() {
        comboSource = new JComboBox<>();
        comboSource.setFont(new Font("Arial", Font.PLAIN, 12));
        for (CompteBancaire compte : client.getComptes()) {
            comboSource.addItem(String.format("%s - %.2f €", 
                compte.getNumeroCompte(), compte.getSolde()));
        }
        
        champIbanDestinataire = new JTextField(25);
        champIbanDestinataire.setFont(new Font("Arial", Font.PLAIN, 12));
        
        champNomDestinataire = new JTextField(20);
        champNomDestinataire.setFont(new Font("Arial", Font.PLAIN, 12));
        
        champMontant = new JTextField(15);
        champMontant.setFont(new Font("Arial", Font.PLAIN, 12));
        
        champLibelle = new JTextField(20);
        champLibelle.setFont(new Font("Arial", Font.PLAIN, 12));
        
        labelFrais = new JLabel("Frais: 0,00 €");
        labelFrais.setFont(new Font("Arial", Font.BOLD, 12));
        labelFrais.setForeground(Color.RED);
        
        labelTotal = new JLabel("Total (montant + frais): 0,00 €");
        labelTotal.setFont(new Font("Arial", Font.BOLD, 12));
        labelTotal.setForeground(new Color(0, 100, 0));
        
        boutonValider = new JButton("Effectuer le virement");
        boutonValider.setBackground(new Color(40, 167, 69));
        boutonValider.setForeground(Color.BLACK);
        boutonValider.setFocusPainted(false);
        boutonValider.setFont(new Font("Arial", Font.BOLD, 12));
        
        boutonAnnuler = new JButton("Annuler");
        boutonAnnuler.setBackground(new Color(108, 117, 125));
        boutonAnnuler.setForeground(Color.BLACK);
        boutonAnnuler.setFocusPainted(false);
        boutonAnnuler.setFont(new Font("Arial", Font.BOLD, 12));
    }
    
    private void configurerLayout() {
        setLayout(new BorderLayout(10, 10));
        
        JPanel panelPrincipal = new JPanel(new GridBagLayout());
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panelPrincipal.setBackground(new Color(240, 240, 240));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel titre = new JLabel("Virement vers un autre compte bancaire", JLabel.CENTER);
        titre.setFont(new Font("Arial", Font.BOLD, 18));
        titre.setForeground(new Color(0, 51, 102));
        panelPrincipal.add(titre, gbc);
        
        gbc.gridwidth = 1;
        gbc.gridy = 1;
        gbc.gridx = 0;
        JLabel labelSource = new JLabel("Votre compte:");
        labelSource.setFont(new Font("Arial", Font.BOLD, 12));
        panelPrincipal.add(labelSource, gbc);
        
        gbc.gridx = 1;
        panelPrincipal.add(comboSource, gbc);
        
        gbc.gridy = 2;
        gbc.gridx = 0;
        JLabel labelIban = new JLabel("IBAN du destinataire:");
        labelIban.setFont(new Font("Arial", Font.BOLD, 12));
        panelPrincipal.add(labelIban, gbc);
        
        gbc.gridx = 1;
        panelPrincipal.add(champIbanDestinataire, gbc);
        
        gbc.gridy = 3;
        gbc.gridx = 0;
        JLabel labelNom = new JLabel("Nom du destinataire:");
        labelNom.setFont(new Font("Arial", Font.BOLD, 12));
        panelPrincipal.add(labelNom, gbc);
        
        gbc.gridx = 1;
        panelPrincipal.add(champNomDestinataire, gbc);
        
        gbc.gridy = 4;
        gbc.gridx = 0;
        JLabel labelMontant = new JLabel("Montant (€):");
        labelMontant.setFont(new Font("Arial", Font.BOLD, 12));
        panelPrincipal.add(labelMontant, gbc);
        
        gbc.gridx = 1;
        panelPrincipal.add(champMontant, gbc);
        
        gbc.gridy = 5;
        gbc.gridx = 0;
        JLabel labelLibelle = new JLabel("Libellé:");
        labelLibelle.setFont(new Font("Arial", Font.BOLD, 12));
        panelPrincipal.add(labelLibelle, gbc);
        
        gbc.gridx = 1;
        panelPrincipal.add(champLibelle, gbc);
        
        gbc.gridy = 6;
        gbc.gridx = 0;
        JLabel labelInfoFrais = new JLabel("Frais de virement:");
        labelInfoFrais.setFont(new Font("Arial", Font.BOLD, 12));
        panelPrincipal.add(labelInfoFrais, gbc);
        
        gbc.gridx = 1;
        panelPrincipal.add(labelFrais, gbc);
        
        gbc.gridy = 7;
        gbc.gridx = 0;
        JLabel labelInfoTotal = new JLabel("Total débité:");
        labelInfoTotal.setFont(new Font("Arial", Font.BOLD, 12));
        panelPrincipal.add(labelInfoTotal, gbc);
        
        gbc.gridx = 1;
        panelPrincipal.add(labelTotal, gbc);
        
        gbc.gridwidth = 2;
        gbc.gridy = 8;
        gbc.gridx = 0;
        JLabel infoPlafond = new JLabel(
            String.format("Plafond de virement: %.2f € | Frais fixes: %.2f €", 
                CompteBancaire.PLAFOND_VIREMENT, CompteBancaire.FRAIS_VIREMENT_EXTERNE),
            JLabel.CENTER
        );
        infoPlafond.setFont(new Font("Arial", Font.ITALIC, 12));
        infoPlafond.setForeground(Color.BLUE);
        panelPrincipal.add(infoPlafond, gbc);
        
        add(panelPrincipal, BorderLayout.CENTER);
        
        JPanel panelBoutons = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panelBoutons.setBackground(new Color(240, 240, 240));
        panelBoutons.add(boutonValider);
        panelBoutons.add(boutonAnnuler);
        
        add(panelBoutons, BorderLayout.SOUTH);
    }
    
    private void configurerActions() {
        boutonValider.addActionListener(e -> effectuerVirementExterne());
        boutonAnnuler.addActionListener(e -> dispose());
        
        champMontant.addActionListener(e -> mettreAJourFraisEtTotal());
        
        javax.swing.event.DocumentListener documentListener = new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent e) { mettreAJourFraisEtTotal(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { mettreAJourFraisEtTotal(); }
            public void insertUpdate(javax.swing.event.DocumentEvent e) { mettreAJourFraisEtTotal(); }
        };
        
        champMontant.getDocument().addDocumentListener(documentListener);
    }
    
    private void mettreAJourFraisEtTotal() {
        try {
            String montantText = champMontant.getText().trim();
            if (!montantText.isEmpty()) {
                double montant = Double.parseDouble(montantText);
                double frais = CompteBancaire.FRAIS_VIREMENT_EXTERNE;
                double total = montant + frais;
                
                labelFrais.setText(String.format("Frais: %.2f €", frais));
                labelTotal.setText(String.format("Total (montant + frais): %.2f €", total));
            } else {
                labelFrais.setText("Frais: 0,00 €");
                labelTotal.setText("Total (montant + frais): 0,00 €");
            }
        } catch (NumberFormatException e) {
            labelFrais.setText("Frais: 0,00 €");
            labelTotal.setText("Total (montant + frais): 0,00 €");
        }
    }
    
    private void effectuerVirementExterne() {
        try {
            int indexSource = comboSource.getSelectedIndex();
            if (indexSource < 0) {
                JOptionPane.showMessageDialog(this,
                    "Veuillez sélectionner un compte source",
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            List<CompteBancaire> comptes = client.getComptes();
            CompteBancaire source = comptes.get(indexSource);
            
            String ibanDestinataire = champIbanDestinataire.getText().trim();
            if (ibanDestinataire.isEmpty() || ibanDestinataire.length() < 20) {
                JOptionPane.showMessageDialog(this,
                    "IBAN invalide. Un IBAN doit contenir au moins 20 caractères",
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            String nomDestinataire = champNomDestinataire.getText().trim();
            if (nomDestinataire.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "Veuillez saisir le nom du destinataire",
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            String montantText = champMontant.getText().trim();
            if (montantText.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "Veuillez saisir un montant",
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            double montant;
            try {
                montant = Double.parseDouble(montantText);
                if (montant <= 0) {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                    "Montant invalide. Veuillez saisir un nombre positif",
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            String libelle = champLibelle.getText().trim();
            if (libelle.isEmpty()) {
                libelle = "Virement externe";
            }
            
            int confirmation = JOptionPane.showConfirmDialog(this,
                String.format("Confirmez-vous le virement de %.2f € vers %s?\n" +
                            "Frais: %.2f €\nTotal débité: %.2f €",
                    montant, nomDestinataire, 
                    CompteBancaire.FRAIS_VIREMENT_EXTERNE,
                    montant + CompteBancaire.FRAIS_VIREMENT_EXTERNE),
                "Confirmation de virement",
                JOptionPane.YES_NO_OPTION);
            
            if (confirmation != JOptionPane.YES_OPTION) {
                return;
            }
            
            source.effectuerVirementExterne(ibanDestinataire, nomDestinataire, montant, libelle);
            
            JOptionPane.showMessageDialog(this,
                String.format("Virement externe de %.2f € effectué avec succès!\n" +
                            "Destinataire: %s\nIBAN: %s",
                    montant, nomDestinataire, ibanDestinataire),
                "Succès",
                JOptionPane.INFORMATION_MESSAGE);
            
            parent.actualiserDonnees();
            dispose();
            
        } catch (SoldeInsuffisantException e) {
            JOptionPane.showMessageDialog(this,
                e.getMessage(),
                "Solde insuffisant",
                JOptionPane.ERROR_MESSAGE);
        } catch (PlafondDepasseException e) {
            JOptionPane.showMessageDialog(this,
                e.getMessage(),
                "Plafond dépassé",
                JOptionPane.ERROR_MESSAGE);
        } catch (DecouvertDepasseException e) {
            JOptionPane.showMessageDialog(this,
                e.getMessage(),
                "Découvert dépassé",
                JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Une erreur est survenue: " + e.getMessage(),
                "Erreur",
                JOptionPane.ERROR_MESSAGE);
        }
    }
}