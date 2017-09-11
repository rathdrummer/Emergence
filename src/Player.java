import javax.swing.*;
import java.awt.*;

/**
 * Represents the player
 */
public class Player extends JPanel {
    public int x ;
    public int y ;
    public String name ;

    public Player(String name){
        this.name = name;
        this.x = 0;
        this.y = 0;
    }

    @Override
    public void paint(Graphics g){
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.fillOval(x, y, 30, 30);
    }

    public void incrementX(boolean direction){
        if (direction) x++;
        else x--;
    }

    public void incrementY(boolean direction){
        if (direction) y++;
        else y--;
    }

    public void incrementX() {
        incrementX(true);
    }

    public void incrementY() {
        incrementY(true);
    }
}
