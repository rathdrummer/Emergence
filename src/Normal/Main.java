package Normal;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Main extends JFrame {

    private Backend controller;
    private Player player;
    private List<Box> boxes = new ArrayList<>();

    public Main(){
        player = new Player("glen");

        boxes.add(new Box(200,200, 30, 30));
        boxes.add(new Box(200,230, 30, 30));
        boxes.add(new Box(200,260, 30, 30));
        boxes.add(new Box(230,260, 30, 30));
        boxes.add(new Box(260,260, 30, 30));
        boxes.add(new Box(290,260, 30, 30));
        boxes.add(new Box(290,230, 30, 30));

        //creating two listener that calls our update and render functions and sending them to the controller
        this.controller = new Backend(e -> update(), this::render);
        initUI();
    }

    private void initUI() {
        add(controller);
        getContentPane().setPreferredSize(new Dimension(640, 480));
        setResizable(false);
        pack();

        setTitle("Innkeepers tale");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    public void update() {
        //inputs
        if (controller.isPressed(Keys.UP)) player.incrementDY(false);
        if (controller.isPressed(Keys.DOWN)) player.incrementDY(true);
        if (controller.isPressed(Keys.LEFT)) player.incrementDX(false);
        if (controller.isPressed(Keys.RIGHT)) player.incrementDX(true);

        // Updates
        player.update((List<Collision>) (List<?>) boxes);
    }

    public void run() {
        controller.run();
    }

    public void render(Graphics2D g2d) {

        player.paint(g2d);
        boxes.forEach(b -> b.draw(g2d));
    }

    public static void main(String[] args) throws InterruptedException{
        Main m = new Main();

        /*Some piece of code*/
        m.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
            }
        });

        m.setVisible(true);
        //runs the program
        m.run();

    }
}
