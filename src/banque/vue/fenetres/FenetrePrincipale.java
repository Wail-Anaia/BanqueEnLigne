package banque.vue.fenetres;

import javax.swing.*;
import java.awt.*;
import banque.controleurs.AuthControleur;

public class FenetrePrincipale extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private FenetreConnexion fenetreConnexion;
    private FenetreTableauBord fenetreTableauBord;
    private AuthControleur authControleur;
    
    public FenetrePrincipale() {
        setTitle("Banque en Ligne");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        
        // Style de la fenêtre
        getContentPane().setBackground(new Color(240, 240, 240));
        
        authControleur = new AuthControleur();
        
        initialiserComposants();
        configurerLayout();
        
        cardLayout.show(mainPanel, "CONNEXION");
    }
    
    private void initialiserComposants() {
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        mainPanel.setBackground(new Color(240, 240, 240));
        
        fenetreConnexion = new FenetreConnexion(this, authControleur);
        fenetreTableauBord = new FenetreTableauBord(this, authControleur);
        
        mainPanel.add(fenetreConnexion, "CONNEXION");
        mainPanel.add(fenetreTableauBord, "TABLEAU_BORD");
        
        add(mainPanel);
    }
    
    private void configurerLayout() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(new Color(70, 130, 180));
        
        JMenu menuFichier = new JMenu("Fichier");
        menuFichier.setForeground(Color.WHITE);
        menuFichier.setFont(new Font("Arial", Font.BOLD, 12));
        
        JMenuItem itemQuitter = new JMenuItem("Quitter");
        itemQuitter.setForeground(Color.BLACK);
        itemQuitter.addActionListener(e -> System.exit(0));
        menuFichier.add(itemQuitter);
        
        JMenu menuAide = new JMenu("Aide");
        menuAide.setForeground(Color.WHITE);
        menuAide.setFont(new Font("Arial", Font.BOLD, 12));
        
        JMenuItem itemAPropos = new JMenuItem("À propos");
        itemAPropos.setForeground(Color.BLACK);
        itemAPropos.addActionListener(e -> afficherAPropos());
        menuAide.add(itemAPropos);
        
        menuBar.add(menuFichier);
        menuBar.add(menuAide);
        
        setJMenuBar(menuBar);
    }
    
    public void afficherTableauBord() {
        fenetreTableauBord.actualiserDonnees();
        cardLayout.show(mainPanel, "TABLEAU_BORD");
    }
    
    public void afficherConnexion() {
        cardLayout.show(mainPanel, "CONNEXION");
    }
    
    private void afficherAPropos() {
        JOptionPane.showMessageDialog(this,
            "Banque en Ligne v1.0\n" +
            "Application de gestion bancaire\n" +
            "Comptes Courant/Épargne - Virements - Historique 12 mois\n" +
            "© 2024 - Tous droits réservés",
            "À propos",
            JOptionPane.INFORMATION_MESSAGE);
    }
}