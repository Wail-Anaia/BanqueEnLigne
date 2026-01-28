package banque.vue.composants;

import javax.swing.*;
import java.awt.*;
import banque.modele.abstractions.CompteBancaire;

public class PanneauCompte extends JPanel {
    private CompteBancaire compte;
    
    public PanneauCompte(CompteBancaire compte) {
        this.compte = compte;
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        
        JLabel labelNumero = new JLabel("Compte: " + compte.getNumeroCompte());
        JLabel labelSolde = new JLabel(String.format("Solde: %.2f €", compte.getSolde()));
        JLabel labelType = new JLabel(compte instanceof banque.modele.implementations.CompteCourant ? 
            "Compte Courant" : "Compte Épargne");
        
        JPanel panelInfo = new JPanel(new GridLayout(3, 1));
        panelInfo.add(labelNumero);
        panelInfo.add(labelSolde);
        panelInfo.add(labelType);
        
        add(panelInfo, BorderLayout.CENTER);
    }
}