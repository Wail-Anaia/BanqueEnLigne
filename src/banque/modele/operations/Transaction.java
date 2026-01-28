package banque.modele.operations;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Transaction implements Serializable {
    private String id;
    private TypeTransaction type;
    private double montant;
    private String description;
    private LocalDateTime date;
    private String compteSource;
    private String compteDestination;
    
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    
    public Transaction(TypeTransaction type, double montant, String description) {
        this.id = generateId();
        this.type = type;
        this.montant = montant;
        this.description = description;
        this.date = LocalDateTime.now();
    }
    
    public Transaction(TypeTransaction type, double montant, String description, 
                      String compteSource, String compteDestination) {
        this(type, montant, description);
        this.compteSource = compteSource;
        this.compteDestination = compteDestination;
    }
    
    private String generateId() {
        return "TXN" + System.currentTimeMillis() + (int)(Math.random() * 1000);
    }
    
    public String getId() { return id; }
    public TypeTransaction getType() { return type; }
    public double getMontant() { return montant; }
    public String getDescription() { return description; }
    public LocalDateTime getDate() { return date; }
    public String getCompteSource() { return compteSource; }
    public String getCompteDestination() { return compteDestination; }
    
    public String getDateFormatee() {
        return date.format(FORMATTER);
    }
    
    @Override
    public String toString() {
        return String.format("%s | %s | %.2f € | %s", 
            getDateFormatee(), type.getLibelle(), montant, description);
    }
}