package Normal;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Main extends JFrame {

    private Backend controller;
    private Player player;
    private List<Collision> collisions = new ArrayList<>();

    // Stack of all the drawable objects to be shown on screen, in order.
    public LinkedList<Drawable> objectStack = new LinkedList<>();

    public Main(){
        player = new Player("circle.png");



        addThing(new Background("car-road-rug.jpg"));
        addThing(new Box(200,200, 30, 30));
        addThing(new Box(200,230, 30, 30));
        addThing(new Box(200,260, 30, 30));
        addThing(new Box(230,260, 30, 30));
        addThing(new Box(260,260, 30, 30));
        addThing(new Box(290,260, 30, 30));
        addThing(new Box(290,230, 30, 30));
        addThing(player);

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
        player.update(collisions);
    }

    public void run() {
        controller.run();
    }

    public void render(Graphics2D g2d) {

        for (Drawable d : objectStack){
            g2d.drawImage(d.getImage(), (int)d.x(), (int)d.y(), (int)d.width(), (int)d.height(), null) ;
        }

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

    private void addThing(Drawable thing){
        objectStack.add(thing);
        if (thing instanceof Collision) {
            collisions.add((Collision) thing);
        }
    }
}
