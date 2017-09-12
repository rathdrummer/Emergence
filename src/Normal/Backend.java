package Normal; /**
 * Created by oskar on 2017-09-12.
 * This classes has some inputs and outputs
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

/**
 * The object that controls all graphics and all things seen on screen
 */

public class Backend extends JPanel implements ActionListener, MouseListener {

    private boolean running = false;
    private ReentrantLock lock = new ReentrantLock();
    private final int fps = 60;

    private Map<Integer,Keys> keyDictionary;
    private Map<Keys,Boolean> commandIsPressedTable;

    private boolean mouseIsPressed = false;

    private ActionListener updateListener;
    private DrawListener renderListener;

    public Backend(ActionListener updateListener, DrawListener renderListener) {
        addKeyListener(new TAdapter());
        addMouseListener(this);
        setFocusable(true); // so that we may click on this
        setBackground(Color.WHITE);

        this.updateListener = updateListener;
        this.renderListener = renderListener;

        keyDictionary = new HashMap<>();
        commandIsPressedTable = new HashMap<>();
        initKeys();
    }

    public void tick(){
        updateListener.actionPerformed(null); // we do not need to send any information
    }


    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        lock.lock();
        try {
            renderListener.onRender((Graphics2D) g);

        } finally {
            lock.unlock();
        }

        Toolkit.getDefaultToolkit().sync();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //for buttons and such
    }

    @Override
    public void mouseClicked(MouseEvent event) {
        //just for the actual click event, mouse down often works fine
    }

    @Override
    public void mousePressed(MouseEvent event) {
        if (SwingUtilities.isLeftMouseButton(event)) {
            //event has x and y
            mouseIsPressed = true;
        } else if (SwingUtilities.isRightMouseButton(event)) {
            //if we want right click
        }
    }

    @Override
    public void mouseReleased(MouseEvent event) {
        if (SwingUtilities.isLeftMouseButton(event)) {
            mouseIsPressed = false;
        }
    }

    @Override
    public void mouseEntered(MouseEvent event) {

    }

    @Override
    public void mouseExited(MouseEvent event) {

    }

    private void addKey(Integer i, Keys name){
        keyDictionary.put(i,name);
        commandIsPressedTable.put(name,false);
    }

    /**
     * Run to add all the keys to the hashtables.
     */
    private void initKeys(){
        addKey(38,Keys.UP);
        addKey(37,Keys.LEFT);
        addKey(40,Keys.DOWN);
        addKey(39,Keys.RIGHT);
    }

    /**
     * This handles keyboard input
     */
    private class TAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {
            Keys key = keyDictionary.get(e.getKeyCode());
            commandIsPressedTable.replace(key, true);
        }

        @Override
        public void keyReleased(KeyEvent e) {
            Keys key = keyDictionary.get(e.getKeyCode());
            commandIsPressedTable.replace(key, false);
        }
    }

    public boolean isPressed(Keys key){
        return commandIsPressedTable.get(key);
    }

    //Frame rate back end, this we will never have to touch
    public synchronized void run(){
        long lastTime = System.nanoTime();
        double nsPerTick = 1000000000D / fps;

        long secondTimer = System.currentTimeMillis();
        double delta = 0;
        long now;
        running = true;

        while(running){
            now = System.nanoTime();
            delta += (now - lastTime) / nsPerTick;
            lastTime = now;

            while(delta >=1){
                lock.lock();
                try {
                    tick();
                }finally {
                    lock.unlock();
                }
                delta -= 1;
            }

            try {
                Thread.sleep(2); //to make the loop slighlty slower, will not affect anything

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            repaint();

            if (System.currentTimeMillis() - secondTimer > 1000){
                secondTimer += 1000;
            }
        }
    }
}