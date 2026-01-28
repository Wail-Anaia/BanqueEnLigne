package banque.modele.operations;

import banque.modele.exceptions.*;
import banque.modele.abstractions.CompteBancaire;
import java.time.LocalDateTime;

public class Virement {
    private String id;
    private CompteBancaire source;
    private CompteBancaire destination;
    private double montant;
    private LocalDateTime date;
    private String libelle;
    private boolean execute;
    private String motifRejet;
    
    public Virement(CompteBancaire source, CompteBancaire destination, double montant, String libelle) {
        this.id = "VIR" + System.currentTimeMillis();
        this.source = source;
        this.destination = destination;
        this.montant = montant;
        this.libelle = libelle;
        this.date = LocalDateTime.now();
        this.execute = false;
    }
    
    public void executer() throws SoldeInsuffisantException, PlafondDepasseException {
        if (montant > CompteBancaire.PLAFOND_VIREMENT) {
            this.motifRejet = "Plafond de virement dépassé";
            throw new PlafondDepasseException(CompteBancaire.PLAFOND_VIREMENT, montant);
        }
        
        source.effectuerVirementInterne(destination, montant, libelle);
        this.execute = true;
    }
    
    public String getId() { return id; }
    public CompteBancaire getSource() { return source; }
    public CompteBancaire getDestination() { return destination; }
    public double getMontant() { return montant; }
    public LocalDateTime getDate() { return date; }
    public String getLibelle() { return libelle; }
    public boolean isExecute() { return execute; }
    public String getMotifRejet() { return motifRejet; }
}