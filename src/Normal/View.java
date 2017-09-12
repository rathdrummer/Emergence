package Normal;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;

/**
 * Handles the window and UI
 */
public class View extends JFrame{

    private JPanel panel;

    private LinkedList<Drawable> objectStack = new LinkedList<>();

    public View(){

        this.panel = new JPanel(){
            @Override
            public void paintComponent(Graphics g){
                super.paintComponent(g);
                // Loop over the list of drawables and draw them
                for (Drawable d : objectStack){
                    g.drawImage(d.getImage(), (int)d.x(), (int)d.y(), (int)d.width(), (int)d.height(), null) ;
                }
            }
        };

        this.setTitle("Emergence");
        this.setSize(600,400);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.objectStack = new LinkedList<>();
        this.add(panel);
        this.setVisible(true);


    }

    public void run(){
        try{
            while (true){
                this.repaint();
                Thread.sleep(10);
            }
        }catch (InterruptedException e){
            System.out.println("Error: Normal.View thread interrupted");
            System.exit(-1);
        }
    }

}
