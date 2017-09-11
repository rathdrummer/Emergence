import javax.swing.*;

/**
 * Handles the window and UI
 */
public class View extends Thread{

    private JFrame jFrame;

    public View(){
        jFrame =  new JFrame("Test Window");
        jFrame.setSize(600,400);
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        jFrame.setVisible(true);
        
    }

    public void run(){
        try{
            while (true){
                jFrame.repaint();
                Thread.sleep(10);
            }
        }catch (InterruptedException e){
            System.out.println("Error: View thread interrupted");
            System.exit(-1);
        }
    }

    /**
     * Use to add things to the jFrame
     */
    public java.awt.Component add(java.awt.Component c){
        return jFrame.add(c);
    }

    public void addKeyListener(Input input) {
        jFrame.addKeyListener(input);
    }
}
