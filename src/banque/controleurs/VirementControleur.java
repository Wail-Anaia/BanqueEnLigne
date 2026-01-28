package banque.controleurs;

import banque.modele.abstractions.CompteBancaire;
import banque.modele.exceptions.*;
import banque.modele.operations.Virement;

public class VirementControleur {
    
    public Virement creerVirement(CompteBancaire source, CompteBancaire destination, 
                                  double montant, String libelle) {
        return new Virement(source, destination, montant, libelle);
    }
    
    public void executerVirement(Virement virement) 
            throws SoldeInsuffisantException, PlafondDepasseException {
        virement.executer();
    }
    
    public boolean verifierPlafond(double montant) {
        return montant <= CompteBancaire.PLAFOND_VIREMENT;
    }
    
    public String getPlafondVirement() {
        return String.format("%.2f €", CompteBancaire.PLAFOND_VIREMENT);
    }
    
    public String getFraisVirementExterne() {
        return String.format("%.2f €", CompteBancaire.FRAIS_VIREMENT_EXTERNE);
    }
}