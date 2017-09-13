package Normal;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Main extends JFrame {

    private Backend controller;
    private Player player;
    private CameraFollower cam;
    private List<Thing> things = new ArrayList<>();

    // Stack of all the drawable objects to be shown on screen, in order.
    public LinkedList<Drawable> objectStack = new LinkedList<>();

    // Camera offset - lets the camera follow the player or anything else
    private double xOffset = 0;
    private double yOffset = 0;

    /*
    // Maybe we will need to store the size of the area so we know when to stop scrolling with the camera
    private Area area;
    */

    public Main(){
        player = new Player("circle.png");

        cam = new CameraFollower(player);


        addThing(new Area("car-road-rug.jpg"));
        addThing(new Box(200,200, 30, 30));
        addThing(new Box(200,230, 30, 30));
        addThing(new Box(200,260, 30, 30));
        addThing(new Box(230,260, 30, 30));
        addThing(new Box(260,260, 30, 30));
        addThing(new Box(290,260, 30, 30));
        addThing(new Box(290,230, 30, 30));
        addThing(player);
        addThing(new Blob(100,100));
        addThing(new Blob(150,100));
        addThing(new Blob(100,150));
        addThing(new Blob(300,500));




        //creating two listener that calls our update and render functions and sending them to the controller
        this.controller = new Backend(e -> update(), this::render);
        initUI();
        Sound.play("crow");
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
        things.forEach(t -> t.update(things));

        cam.update();
        updateOffset(cam.x()-this.getContentPane().getWidth()/2,cam.y()-this.getContentPane().getHeight()/2); // Follow the player
    }

    public void run() {
        controller.run();
    }

    public void render(Graphics2D g2d) {

        // Apply camera offset while drawing
        for (Drawable d : objectStack){
            g2d.drawImage(d.getImage(),
                    (int)(d.x()-xOffset),
                    (int)(d.y()-yOffset),
                    (int)d.width(),
                    (int)d.height(),
                    null) ;
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

    private void addThing(Drawable d){
        objectStack.add(d);
        if (d instanceof Thing) {
            things.add((Thing) d);
        }
    }


    private void updateOffset(double xOffset,double yOffset){
            this.xOffset = xOffset;
            this.yOffset = yOffset;

    }

    /**
     * Moves the camera offset so the camera is looking at the centre of a drawable
     */
    private void updateOffset(Drawable drawable){
        updateOffset(drawable.xC()-this.getContentPane().getWidth()/2,drawable.yC()-this.getContentPane().getHeight()/2);
    }

}
