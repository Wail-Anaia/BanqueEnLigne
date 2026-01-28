package banque.modele.exceptions;

public class CompteInexistantException extends Exception {
    public CompteInexistantException(String message) {
        super(message);
    }
    
    public CompteInexistantException(String numeroCompte, String iban) {
        super(String.format("Compte inexistant. Numéro: %s, IBAN: %s", numeroCompte, iban));
    }
}