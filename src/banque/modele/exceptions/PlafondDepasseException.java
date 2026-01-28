package banque.modele.exceptions;

public class PlafondDepasseException extends Exception {
    public PlafondDepasseException(String message) {
        super(message);
    }
    
    public PlafondDepasseException(double plafond, double montant) {
        super(String.format("Plafond de virement dépassé. Plafond: %.2f €, Montant: %.2f €", 
            plafond, montant));
    }
}