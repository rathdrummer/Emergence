package Normal;

import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Main extends JFrame {

    private final Clip clip;
    private Backend controller;
    private Player player;
    private CameraFollower cam;
    private List<Thing> things = new ArrayList<>();


    // Stack of all the drawable objects to be shown on screen, in order.
    public LinkedList<Drawable> objectStack = new LinkedList<>();

    // Camera offset - lets the camera follow the player or anything else
    private double xOffset = 0;
    private double yOffset = 0;
    private boolean isPressed = false; //get rid of soon

    /*
    // Maybe we will need to store the size of the area so we know when to stop scrolling with the camera
    private Area area;
    */

    public Main(){
        player = new Player(100, 200);
        cam = new CameraFollower(player);

        for (int i = 0; i < 256; i++) {
            int column = i % 16;
            int row = i / 16;

            addThing(new Tile("tiles", 50 * column, 50 * row, 50, 50, 2));
            if (Math.random() < 0.15) {
                addThing(new Tile("tiles", 50 * column, 50 * row, 50, 50, 4 + (int) Math.round(3 * Math.random())));
            }
        }

        Stepper stepper = new Stepper(200, 200, 50);
        addThing(new Box(stepper.getX(), stepper.getY()));
        addThing(new Box(stepper.getX(), stepper.down()));
        addThing(new Box(stepper.getX(), stepper.down()));
        addThing(new Box(stepper.getX(), stepper.down()));
        addThing(new Box(stepper.right(), stepper.getY()));
        addThing(new Box(stepper.right(), stepper.getY()));
        addThing(new Box(stepper.right(), stepper.getY()));
        addThing(new Box(stepper.right(), stepper.getY()));
        addThing(new Box(stepper.getX(), stepper.up()));
        addThing(new Box(stepper.getX(), stepper.up()));
        addThing(new Box(stepper.getX(), stepper.up()));
        addThing(new Box(stepper.getX(), stepper.up()));
        addThing(new Box(stepper.right(), stepper.getY()));
        addThing(new Box(stepper.right(), stepper.getY()));
        addThing(new Box(stepper.right(), stepper.getY()));
        addThing(new Box(stepper.right(), stepper.getY()));


        addThing(player);

        addThing(new Blob(100,100));
        addThing(new Blob(150,100));
        addThing(new Blob(100,150));
        addThing(new Blob(300,500));



        //creating two listener that calls our update and render functions and sending them to the controller
        this.controller = new Backend(e -> update(), this::render);
        initUI();

        clip = null;
        //Todo, this casts an error , RIFFInvalidDateException: Chunk size too big
        //On further inspection i find that I cannot play the file even externally so it is probably a fault of the file
        //clip = Sound.playMusic("forest-flute");
        //clip.loop(Clip.LOOP_CONTINUOUSLY);
        //Sound.playMusic("forest-strings").loop(Clip.LOOP_CONTINUOUSLY);
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
        player.input(controller.isPressed(Keys.RIGHT),controller.isPressed(Keys.UP),
                controller.isPressed(Keys.LEFT),controller.isPressed(Keys.DOWN));

        if (controller.isPressed(Keys.SOUND)) {

            if (!isPressed && clip != null) {
                isPressed = true;
                long time = System.currentTimeMillis();
                FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                float range = gainControl.getMaximum() - gainControl.getMinimum();
                float gain = (range * 0.7f) + gainControl.getMinimum();
                gainControl.setValue(gain);

            }
        }
        else {
            isPressed = false;
        }

        // Updates
        things.forEach(t -> t.tick(things));

        cam.update(this.getContentPane().getWidth(), this.getContentPane().getHeight());
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

            /*
            if (!(d instanceof Tile)) {
                g2d.fillOval((int) (d.xC() - 2 - xOffset), (int) (d.yC() - 2 - yOffset), 4, 4);
                if (d instanceof Thing) {
                    Thing thing = (Thing) d;
                    Collision c = thing.getCollision();
                    g2d.drawRect(c.getX() - (int) xOffset, c.getY() - (int) yOffset, c.getWidth(), c.getHeight());
                }
            }
             */
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
