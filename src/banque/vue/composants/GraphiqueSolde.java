package banque.vue.composants;

import java.awt.*;
import javax.swing.*;

public class GraphiqueSolde extends JPanel {
    
    public GraphiqueSolde() {
        setPreferredSize(new Dimension(300, 200));
        setBorder(BorderFactory.createTitledBorder("Évolution du solde"));
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        
        int width = getWidth();
        int height = getHeight();
        
        g2d.setColor(Color.LIGHT_GRAY);
        g2d.fillRect(0, 0, width, height);
        
        g2d.setColor(Color.BLUE);
        int[] points = {50, 80, 60, 100, 70, 90, 80, 120};
        for (int i = 0; i < points.length - 2; i += 2) {
            g2d.drawLine(points[i], height - points[i+1], points[i+2], height - points[i+3]);
        }
    }
}