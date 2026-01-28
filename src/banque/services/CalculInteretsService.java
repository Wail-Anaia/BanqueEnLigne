package banque.services;

import banque.modele.abstractions.CompteBancaire;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class CalculInteretsService {
    private Timer timer;
    private List<CompteBancaire> comptes;
    
    public CalculInteretsService(List<CompteBancaire> comptes) {
        this.comptes = comptes;
        this.timer = new Timer(true);
    }
    
    public void demarrer() {
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                calculerInteretsMensuels();
            }
        }, getDelaiProchainMois(), 30L * 24 * 60 * 60 * 1000);
    }
    
    public void arreter() {
        timer.cancel();
    }
    
    private void calculerInteretsMensuels() {
        System.out.println("Calcul des intérêts mensuels en cours...");
        
        for (CompteBancaire compte : comptes) {
            try {
                compte.calculerInterets();
                System.out.println("Intérêts calculés pour le compte: " + compte.getNumeroCompte());
            } catch (Exception e) {
                System.err.println("Erreur lors du calcul des intérêts pour le compte " + 
                    compte.getNumeroCompte() + ": " + e.getMessage());
            }
        }
        
        System.out.println("Calcul des intérêts terminé.");
    }
    
    private long getDelaiProchainMois() {
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.add(java.util.Calendar.MONTH, 1);
        calendar.set(java.util.Calendar.DAY_OF_MONTH, 1);
        calendar.set(java.util.Calendar.HOUR_OF_DAY, 0);
        calendar.set(java.util.Calendar.MINUTE, 0);
        calendar.set(java.util.Calendar.SECOND, 0);
        calendar.set(java.util.Calendar.MILLISECOND, 0);
        
        return calendar.getTimeInMillis() - System.currentTimeMillis();
    }
    
    public void calculerInteretsPourCompte(CompteBancaire compte) {
        compte.calculerInterets();
    }
}