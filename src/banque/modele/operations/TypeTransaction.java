package banque.modele.operations;

public enum TypeTransaction {
    DEPOT("Dépôt"),
    RETRAIT("Retrait"),
    VIREMENT_EMIS("Virement émis"),
    VIREMENT_RECU("Virement reçu"),
    INTERETS("Intérêts"),
    FRAIS("Frais bancaires"),
    PRELEVEMENT("Prélèvement");
    
    private final String libelle;
    
    TypeTransaction(String libelle) {
        this.libelle = libelle;
    }
    
    public String getLibelle() {
        return libelle;
    }
}