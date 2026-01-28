package banque.vue.fenetres;

import banque.modele.abstractions.CompteBancaire;
import banque.modele.utilisateur.Client;
import banque.services.PDFService;
import java.awt.*;
import java.io.File;
import java.time.LocalDateTime;
import javax.swing.*;

public class FenetreRelevesPDF extends JDialog {
    private Client client;
    private PDFService pdfService;
    
    private JComboBox<String> comboComptes;
    private JComboBox<String> comboMois;
    private JComboBox<String> comboAnnee;
    private JRadioButton radioMensuel;
    private JRadioButton radioAnnuel;
    private JTextField champCheminFichier;
    private JButton boutonParcourir;
    private JButton boutonGenerer;
    private JButton boutonAnnuler;
    
    public FenetreRelevesPDF(Client client) {
        super((JFrame)null, "Générer un relevé PDF", true);
        this.client = client;
        this.pdfService = new PDFService();
        
        setSize(600, 450);
        setLocationRelativeTo(null);
        
        initialiserComposants();
        configurerLayout();
        configurerActions();
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
        
        comboMois = new JComboBox<>(new String[]{
            "Janvier", "Février", "Mars", "Avril", "Mai", "Juin",
            "Juillet", "Août", "Septembre", "Octobre", "Novembre", "Décembre"
        });
        comboMois.setFont(new Font("Arial", Font.PLAIN, 12));
        comboMois.setSelectedIndex(LocalDateTime.now().getMonthValue() - 1);
        
        int anneeCourante = LocalDateTime.now().getYear();
        comboAnnee = new JComboBox<>();
        comboAnnee.setFont(new Font("Arial", Font.PLAIN, 12));
        for (int i = anneeCourante; i >= anneeCourante - 5; i--) {
            comboAnnee.addItem(String.valueOf(i));
        }
        comboAnnee.setSelectedIndex(0);
        
        radioMensuel = new JRadioButton("Relevé mensuel", true);
        radioMensuel.setFont(new Font("Arial", Font.PLAIN, 12));
        radioMensuel.setBackground(new Color(240, 240, 240));
        
        radioAnnuel = new JRadioButton("Relevé annuel");
        radioAnnuel.setFont(new Font("Arial", Font.PLAIN, 12));
        radioAnnuel.setBackground(new Color(240, 240, 240));
        
        ButtonGroup groupeType = new ButtonGroup();
        groupeType.add(radioMensuel);
        groupeType.add(radioAnnuel);
        
        champCheminFichier = new JTextField(25);
        champCheminFichier.setFont(new Font("Arial", Font.PLAIN, 12));
        champCheminFichier.setText(System.getProperty("user.home") + File.separator + "releve_banque.txt");
        
        boutonParcourir = new JButton("Parcourir...");
        boutonParcourir.setBackground(new Color(108, 117, 125));
        boutonParcourir.setForeground(Color.BLACK);
        boutonParcourir.setFont(new Font("Arial", Font.BOLD, 12));
        
        boutonGenerer = new JButton("Générer le relevé");
        boutonGenerer.setBackground(new Color(40, 167, 69));
        boutonGenerer.setForeground(Color.BLACK);
        boutonGenerer.setFont(new Font("Arial", Font.BOLD, 12));
        
        boutonAnnuler = new JButton("Annuler");
        boutonAnnuler.setBackground(new Color(108, 117, 125));
        boutonAnnuler.setForeground(Color.BLACK);
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
        JLabel titre = new JLabel("Génération de relevé bancaire", JLabel.CENTER);
        titre.setFont(new Font("Arial", Font.BOLD, 18));
        titre.setForeground(new Color(0, 51, 102));
        panelPrincipal.add(titre, gbc);
        
        gbc.gridwidth = 1;
        gbc.gridy = 1;
        gbc.gridx = 0;
        JLabel labelCompte = new JLabel("Compte:");
        labelCompte.setFont(new Font("Arial", Font.BOLD, 12));
        panelPrincipal.add(labelCompte, gbc);
        
        gbc.gridx = 1;
        panelPrincipal.add(comboComptes, gbc);
        
        gbc.gridy = 2;
        gbc.gridx = 0;
        JPanel panelType = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
        panelType.setOpaque(false);
        panelType.add(radioMensuel);
        panelType.add(radioAnnuel);
        panelPrincipal.add(panelType, gbc);
        
        gbc.gridy = 3;
        gbc.gridx = 0;
        JLabel labelMois = new JLabel("Mois:");
        labelMois.setFont(new Font("Arial", Font.BOLD, 12));
        panelPrincipal.add(labelMois, gbc);
        
        gbc.gridx = 1;
        panelPrincipal.add(comboMois, gbc);
        
        gbc.gridy = 4;
        gbc.gridx = 0;
        JLabel labelAnnee = new JLabel("Année:");
        labelAnnee.setFont(new Font("Arial", Font.BOLD, 12));
        panelPrincipal.add(labelAnnee, gbc);
        
        gbc.gridx = 1;
        panelPrincipal.add(comboAnnee, gbc);
        
        gbc.gridy = 5;
        gbc.gridx = 0;
        JLabel labelFichier = new JLabel("Fichier de sortie:");
        labelFichier.setFont(new Font("Arial", Font.BOLD, 12));
        panelPrincipal.add(labelFichier, gbc);
        
        gbc.gridx = 1;
        JPanel panelFichier = new JPanel(new BorderLayout(5, 0));
        panelFichier.setOpaque(false);
        panelFichier.add(champCheminFichier, BorderLayout.CENTER);
        panelFichier.add(boutonParcourir, BorderLayout.EAST);
        panelPrincipal.add(panelFichier, gbc);
        
        gbc.gridwidth = 2;
        gbc.gridy = 6;
        gbc.gridx = 0;
        JLabel info = new JLabel(
            "Note: Le relevé sera généré au format texte (.txt) pour simplifier",
            JLabel.CENTER
        );
        info.setFont(new Font("Arial", Font.ITALIC, 11));
        info.setForeground(Color.GRAY);
        panelPrincipal.add(info, gbc);
        
        add(panelPrincipal, BorderLayout.CENTER);
        
        JPanel panelBoutons = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panelBoutons.setBackground(new Color(240, 240, 240));
        panelBoutons.add(boutonGenerer);
        panelBoutons.add(boutonAnnuler);
        
        add(panelBoutons, BorderLayout.SOUTH);
    }
    
    private void configurerActions() {
        boutonGenerer.addActionListener(e -> genererReleve());
        boutonAnnuler.addActionListener(e -> dispose());
        boutonParcourir.addActionListener(e -> choisirFichier());
        
        radioAnnuel.addActionListener(e -> {
            comboMois.setEnabled(!radioAnnuel.isSelected());
        });
    }
    
    private void choisirFichier() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setSelectedFile(new File(champCheminFichier.getText()));
        fileChooser.setDialogTitle("Enregistrer le relevé");
        
        int result = fileChooser.showSaveDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File fichier = fileChooser.getSelectedFile();
            champCheminFichier.setText(fichier.getAbsolutePath());
        }
    }
    
    private void genererReleve() {
        try {
            if (client.getComptes().isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "Vous n'avez aucun compte",
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            int indexCompte = comboComptes.getSelectedIndex();
            if (indexCompte < 0) {
                JOptionPane.showMessageDialog(this,
                    "Veuillez sélectionner un compte",
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            String cheminFichier = champCheminFichier.getText().trim();
            if (cheminFichier.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "Veuillez spécifier un fichier de sortie",
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            CompteBancaire compte = client.getComptes().get(indexCompte);
            int annee = Integer.parseInt((String) comboAnnee.getSelectedItem());
            
            if (radioMensuel.isSelected()) {
                int mois = comboMois.getSelectedIndex() + 1;
                pdfService.genererReleveMensuel(compte, client, mois, annee, cheminFichier);
                
                JOptionPane.showMessageDialog(this,
                    String.format("Relevé mensuel généré avec succès!\n" +
                                "Période: %s %d\nFichier: %s",
                        comboMois.getSelectedItem(), annee, cheminFichier),
                    "Succès",
                    JOptionPane.INFORMATION_MESSAGE);
            } else {
                pdfService.genererReleveAnnuel(compte, client, annee, cheminFichier);
                
                JOptionPane.showMessageDialog(this,
                    String.format("Relevé annuel généré avec succès!\n" +
                                "Année: %d\nFichier: %s",
                        annee, cheminFichier),
                    "Succès",
                    JOptionPane.INFORMATION_MESSAGE);
            }
            
            dispose();
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Erreur lors de la génération du relevé: " + e.getMessage(),
                "Erreur",
                JOptionPane.ERROR_MESSAGE);
        }
    }
}