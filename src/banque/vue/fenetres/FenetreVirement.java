package banque.vue.fenetres;

import javax.swing.*;
import java.awt.*;
import banque.modele.utilisateur.Client;
import banque.modele.abstractions.CompteBancaire;
import banque.modele.exceptions.*;
import java.util.List;

public class FenetreVirement extends JDialog {
    private FenetreTableauBord parent;
    private Client client;
    
    private JComboBox<String> comboSource;
    private JComboBox<String> comboDestination;
    private JTextField champMontant;
    private JTextField champLibelle;
    private JButton boutonValider;
    private JButton boutonAnnuler;
    
    public FenetreVirement(FenetreTableauBord parent, Client client) {
        super((JFrame)SwingUtilities.getWindowAncestor(parent), "Effectuer un virement", true);
        this.parent = parent;
        this.client = client;
        
        setSize(500, 400);
        setLocationRelativeTo(parent);
        
        initialiserComposants();
        configurerLayout();
        configurerActions();
    }
    
    private void initialiserComposants() {
        comboSource = new JComboBox<>();
        comboSource.setFont(new Font("Arial", Font.PLAIN, 12));
        for (CompteBancaire compte : client.getComptes()) {
            comboSource.addItem(String.format("%s - %.2f €", 
                compte.getNumeroCompte(), compte.getSolde()));
        }
        
        comboDestination = new JComboBox<>();
        comboDestination.setFont(new Font("Arial", Font.PLAIN, 12));
        for (CompteBancaire compte : client.getComptes()) {
            comboDestination.addItem(String.format("%s - %.2f €", 
                compte.getNumeroCompte(), compte.getSolde()));
        }
        
        champMontant = new JTextField(15);
        champMontant.setFont(new Font("Arial", Font.PLAIN, 12));
        
        champLibelle = new JTextField(20);
        champLibelle.setFont(new Font("Arial", Font.PLAIN, 12));
        
        // CHANGEMENT ICI : Texte noir
        boutonValider = new JButton("Valider le virement");
        boutonValider.setBackground(new Color(40, 167, 69)); // Vert
        boutonValider.setForeground(Color.BLACK); // Texte noir
        boutonValider.setFocusPainted(false);
        boutonValider.setFont(new Font("Arial", Font.BOLD, 12));
        
        boutonAnnuler = new JButton("Annuler");
        boutonAnnuler.setBackground(new Color(108, 117, 125)); // Gris
        boutonAnnuler.setForeground(Color.BLACK); // Texte noir
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
        JLabel titre = new JLabel("Nouveau virement", JLabel.CENTER);
        titre.setFont(new Font("Arial", Font.BOLD, 18));
        titre.setForeground(new Color(0, 51, 102));
        panelPrincipal.add(titre, gbc);
        
        gbc.gridwidth = 1;
        gbc.gridy = 1;
        gbc.gridx = 0;
        JLabel labelSource = new JLabel("Compte source:");
        labelSource.setFont(new Font("Arial", Font.BOLD, 12));
        panelPrincipal.add(labelSource, gbc);
        
        gbc.gridx = 1;
        panelPrincipal.add(comboSource, gbc);
        
        gbc.gridy = 2;
        gbc.gridx = 0;
        JLabel labelDest = new JLabel("Compte destination:");
        labelDest.setFont(new Font("Arial", Font.BOLD, 12));
        panelPrincipal.add(labelDest, gbc);
        
        gbc.gridx = 1;
        panelPrincipal.add(comboDestination, gbc);
        
        gbc.gridy = 3;
        gbc.gridx = 0;
        JLabel labelMontant = new JLabel("Montant (€):");
        labelMontant.setFont(new Font("Arial", Font.BOLD, 12));
        panelPrincipal.add(labelMontant, gbc);
        
        gbc.gridx = 1;
        panelPrincipal.add(champMontant, gbc);
        
        gbc.gridy = 4;
        gbc.gridx = 0;
        JLabel labelLibelle = new JLabel("Libellé:");
        labelLibelle.setFont(new Font("Arial", Font.BOLD, 12));
        panelPrincipal.add(labelLibelle, gbc);
        
        gbc.gridx = 1;
        panelPrincipal.add(champLibelle, gbc);
        
        gbc.gridwidth = 2;
        gbc.gridy = 5;
        gbc.gridx = 0;
        JLabel infoPlafond = new JLabel(
            String.format("Plafond de virement: %.2f €", CompteBancaire.PLAFOND_VIREMENT),
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
        boutonValider.addActionListener(e -> effectuerVirement());
        boutonAnnuler.addActionListener(e -> dispose());
        
        comboSource.addActionListener(e -> verifierComptes());
        comboDestination.addActionListener(e -> verifierComptes());
    }
    
    private void verifierComptes() {
        if (comboSource.getSelectedIndex() == comboDestination.getSelectedIndex()) {
            JOptionPane.showMessageDialog(this,
                "Le compte source et le compte destination doivent être différents",
                "Erreur",
                JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private void effectuerVirement() {
        try {
            int indexSource = comboSource.getSelectedIndex();
            int indexDestination = comboDestination.getSelectedIndex();
            
            if (indexSource == indexDestination) {
                JOptionPane.showMessageDialog(this,
                    "Le compte source et le compte destination doivent être différents",
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            List<CompteBancaire> comptes = client.getComptes();
            CompteBancaire source = comptes.get(indexSource);
            CompteBancaire destination = comptes.get(indexDestination);
            
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
                libelle = "Virement interne";
            }
            
            source.effectuerVirementInterne(destination, montant, libelle);
            
            JOptionPane.showMessageDialog(this,
                String.format("Virement de %.2f € effectué avec succès!", montant),
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
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Une erreur est survenue: " + e.getMessage(),
                "Erreur",
                JOptionPane.ERROR_MESSAGE);
        }
    }
}