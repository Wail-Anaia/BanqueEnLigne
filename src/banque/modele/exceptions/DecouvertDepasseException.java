package banque.modele.exceptions;

public class DecouvertDepasseException extends Exception {
    public DecouvertDepasseException(String message) {
        super(message);
    }
    
    public DecouvertDepasseException(double decouvertAutorise, double montant) {
        super(String.format("Découvert dépassé. Découvert autorisé: %.2f €, Montant demandé: %.2f €", 
            decouvertAutorise, montant));
    }
}