package banque.modele.exceptions;

public class SoldeInsuffisantException extends Exception {
    public SoldeInsuffisantException(String message) {
        super(message);
    }
    
    public SoldeInsuffisantException(double solde, double montant) {
        super(String.format("Solde insuffisant. Solde actuel: %.2f €, Montant demandé: %.2f €", solde, montant));
    }
}