package banque.vue.fenetres;

import javax.swing.*;
import java.awt.*;
import banque.controleurs.AuthControleur;

public class FenetreConnexion extends JPanel {
    private FenetrePrincipale parent;
    private AuthControleur authControleur;
    
    private JTextField champEmail;
    private JPasswordField champMotDePasse;
    private JButton boutonConnexion;
    private JButton boutonInscription;
    
    public FenetreConnexion(FenetrePrincipale parent, AuthControleur authControleur) {
        this.parent = parent;
        this.authControleur = authControleur;
        
        initialiserComposants();
        configurerLayout();
        configurerActions();
    }
    
    private void initialiserComposants() {
        champEmail = new JTextField(20);
        champMotDePasse = new JPasswordField(20);
        boutonConnexion = new JButton("Se connecter");
        boutonInscription = new JButton("S'inscrire");
        
        // CHANGEMENT ICI : Texte noir, fond coloré
        boutonConnexion.setBackground(new Color(0, 123, 255)); // Bleu
        boutonConnexion.setForeground(Color.BLACK); // Texte noir
        boutonConnexion.setFocusPainted(false);
        boutonConnexion.setFont(new Font("Arial", Font.BOLD, 12));
        
        boutonInscription.setBackground(new Color(108, 117, 125)); // Gris
        boutonInscription.setForeground(Color.BLACK); // Texte noir
        boutonInscription.setFocusPainted(false);
        boutonInscription.setFont(new Font("Arial", Font.BOLD, 12));
    }
    
    private void configurerLayout() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel titre = new JLabel("Banque en Ligne", JLabel.CENTER);
        titre.setFont(new Font("Arial", Font.BOLD, 24));
        titre.setForeground(new Color(0, 51, 102)); // Bleu foncé
        add(titre, gbc);
        
        gbc.gridy = 1;
        JLabel sousTitre = new JLabel("Connectez-vous à votre compte", JLabel.CENTER);
        sousTitre.setFont(new Font("Arial", Font.PLAIN, 14));
        sousTitre.setForeground(new Color(51, 51, 51)); // Gris foncé
        add(sousTitre, gbc);
        
        gbc.gridwidth = 1;
        gbc.gridy = 2;
        gbc.gridx = 0;
        JLabel labelEmail = new JLabel("Email:");
        labelEmail.setFont(new Font("Arial", Font.BOLD, 12));
        add(labelEmail, gbc);
        
        gbc.gridx = 1;
        champEmail.setFont(new Font("Arial", Font.PLAIN, 12));
        add(champEmail, gbc);
        
        gbc.gridy = 3;
        gbc.gridx = 0;
        JLabel labelMdp = new JLabel("Mot de passe:");
        labelMdp.setFont(new Font("Arial", Font.BOLD, 12));
        add(labelMdp, gbc);
        
        gbc.gridx = 1;
        champMotDePasse.setFont(new Font("Arial", Font.PLAIN, 12));
        add(champMotDePasse, gbc);
        
        gbc.gridwidth = 2;
        gbc.gridy = 4;
        gbc.gridx = 0;
        
        JPanel panelBoutons = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        panelBoutons.setOpaque(false);
        panelBoutons.add(boutonConnexion);
        panelBoutons.add(boutonInscription);
        
        add(panelBoutons, gbc);
        
        gbc.gridy = 5;
        JLabel infoTest = new JLabel(
            "<html><div style='text-align: center;'>" +
            "Comptes de test:<br>" +
            "• wail.anaia@gmail.com / password123<br>" +
            "• yasser.oussahel@gmail.com / password456" +
            "</div></html>", 
            JLabel.CENTER
        );
        infoTest.setFont(new Font("Arial", Font.ITALIC, 10));
        infoTest.setForeground(Color.GRAY);
        add(infoTest, gbc);
    }
    
    private void configurerActions() {
        boutonConnexion.addActionListener(e -> seConnecter());
        boutonInscription.addActionListener(e -> ouvrirInscription());
        champMotDePasse.addActionListener(e -> seConnecter());
    }
    
    private void seConnecter() {
        String email = champEmail.getText().trim();
        String motDePasse = new String(champMotDePasse.getPassword());
        
        if (email.isEmpty() || motDePasse.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Veuillez remplir tous les champs",
                "Erreur",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (authControleur.authentifier(email, motDePasse)) {
            parent.afficherTableauBord();
            champEmail.setText("");
            champMotDePasse.setText("");
        } else {
            JOptionPane.showMessageDialog(this,
                "Email ou mot de passe incorrect",
                "Erreur d'authentification",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void ouvrirInscription() {
        JDialog dialog = new JDialog((Frame)SwingUtilities.getWindowAncestor(this), "Inscription", true);
        dialog.setSize(400, 400);
        dialog.setLocationRelativeTo(this);
        
        JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JTextField champNom = new JTextField();
        JTextField champPrenom = new JTextField();
        JTextField champEmailInscription = new JTextField();
        JPasswordField champMdpInscription = new JPasswordField();
        JPasswordField champMdpConfirmation = new JPasswordField();
        
        panel.add(new JLabel("Nom:"));
        panel.add(champNom);
        panel.add(new JLabel("Prénom:"));
        panel.add(champPrenom);
        panel.add(new JLabel("Email:"));
        panel.add(champEmailInscription);
        panel.add(new JLabel("Mot de passe:"));
        panel.add(champMdpInscription);
        panel.add(new JLabel("Confirmation:"));
        panel.add(champMdpConfirmation);
        
        JButton boutonValider = new JButton("S'inscrire");
        // CHANGEMENT ICI : Texte noir
        boutonValider.setBackground(new Color(40, 167, 69)); // Vert
        boutonValider.setForeground(Color.BLACK); // Texte noir
        boutonValider.setFont(new Font("Arial", Font.BOLD, 12));
        
        boutonValider.addActionListener(e -> {
            String nom = champNom.getText().trim();
            String prenom = champPrenom.getText().trim();
            String email = champEmailInscription.getText().trim();
            String mdp = new String(champMdpInscription.getPassword());
            String mdpConf = new String(champMdpConfirmation.getPassword());
            
            if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty() || mdp.isEmpty()) {
                JOptionPane.showMessageDialog(dialog,
                    "Tous les champs sont obligatoires",
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (!mdp.equals(mdpConf)) {
                JOptionPane.showMessageDialog(dialog,
                    "Les mots de passe ne correspondent pas",
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (authControleur.inscrireClient(nom, prenom, email, mdp)) {
                JOptionPane.showMessageDialog(dialog,
                    "Inscription réussie! Vous pouvez maintenant vous connecter.",
                    "Succès",
                    JOptionPane.INFORMATION_MESSAGE);
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog,
                    "Cet email est déjà utilisé",
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            }
        });
        
        panel.add(new JLabel());
        panel.add(boutonValider);
        
        dialog.add(panel);
        dialog.setVisible(true);
    }
}