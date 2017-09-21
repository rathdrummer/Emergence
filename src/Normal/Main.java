package Normal;

import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class Main extends JFrame {

    private boolean debug = false;
    private final Clip clip;
    private Backend controller;
    private Player player;
    private Camera cam;
    private List<Thing> things = new ArrayList<>();
    private List<Particle> particles = new ArrayList<>();
    private HashMap<Keys, Integer> pressedForHowlong = new HashMap<>();


    // Stack of all the drawable objects to be shown on screen, in order.
    public List<Drawable> drawables = Collections.synchronizedList(new LinkedList<Drawable>()); // synchronized



    // Camera offset - lets the camera follow the player or anything else
    private double xOffset = 0;
    private double yOffset = 0;
    private boolean isPressed = false; //get rid of soon

    /*
    // Maybe we will need to store the size of the area so we know when to stop scrolling with the camera
    private Area area;
    */

    public Main(){
        init();

        //creating two listener that calls our update and render functions and sending them to the controller
        this.controller = new Backend(e -> update(), this::render);
        initUI();

        clip = null;
        /**
        clip = Sound.playMusic("forest-flute");
        clip.loop(Clip.LOOP_CONTINUOUSLY);
        Sound.playMusic("forest-strings").loop(Clip.LOOP_CONTINUOUSLY);
         */
    }

    private void init() {
        player = new Player(400, 600);
        cam = new Camera(player);

        for (int i = 0; i < 256; i++) {
            int column = i % 16;
            int row = i / 16;

            addThing(new Tile("tiles", 50 * column, 50 * row, 50, 50, 1, 1));

            //addThing(new Tile("tiles", 50 * row, 50 * column, 50, 50, 1, 3)); //looks really ugly now

            if (Math.random() < 0.15) {
                //addThing(new Tile("tiles", 50 * column, 50 * row, 50, 50, 4 + (int) Math.round(3 * Math.random())));
                addThing(new Tile("tiles", 50 * column, 50 * row, 50, 50, 2, 0));
            }
        }

        Stepper stepper = new Stepper(200, 200, 45, 30);
        addThing(new Stub(stepper.getX(), stepper.getY()));
        addThing(new Stub(stepper.getX(), stepper.down()));
        addThing(new Stub(stepper.getX(), stepper.down()));
        addThing(new Stub(stepper.getX(), stepper.down()));
        addThing(new Stub(stepper.right(), stepper.getY()));
        addThing(new Stub(stepper.right(), stepper.getY()));
        addThing(new Stub(stepper.right(), stepper.getY()));
        addThing(new Stub(stepper.right(), stepper.getY()));
        addThing(new Stub(stepper.getX(), stepper.up()));
        addThing(new Stub(stepper.getX(), stepper.up()));
        addThing(new Stub(stepper.getX(), stepper.up()));
        addThing(new Stub(stepper.getX(), stepper.up()));
        addThing(new Stub(stepper.right(), stepper.getY()));
        addThing(new Stub(stepper.right(), stepper.getY()));
        addThing(new Stub(stepper.right(), stepper.getY()));
        addThing(new Stub(stepper.right(), stepper.getY()));

        addThing(new Bush(stepper.right(), stepper.down()));


        addThing(player);

        addThing(new Baddy(100,100));
        addThing(new Baddy(150,100));
        addThing(new Baddy(100,150));
        addThing(new Baddy(300,500));

        addThing(new Baddy(400,500));

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

    public boolean justPressed(Keys key) {
        return pressedForHowlong.get(key) == 1;
    }

    public boolean justReleased(Keys key) {
        return pressedForHowlong.get(key) == -1;
    }

    public void update() {


        //press and release functionality
        controller.getKeys().forEach((k,isPressed) -> {

            Integer key = pressedForHowlong.get(k);

            if (key == null) {
                pressedForHowlong.put(k, 0);
                key = pressedForHowlong.get(k);;
            }

            if (isPressed) {
                pressedForHowlong.put(k, key + 1);
            }
            else {
                if (key <= 0) {
                    pressedForHowlong.put(k, 0);
                }
                else pressedForHowlong.put(k, -1); // we use this for release
            }
        });

        if (justPressed(Keys.RESTART)) {
            restart();
        }


        //inputs
        player.input(controller.isPressed(Keys.RIGHT),controller.isPressed(Keys.UP),
                controller.isPressed(Keys.LEFT),controller.isPressed(Keys.DOWN), justPressed(Keys.SHOOT)
            ,justPressed(Keys.PICKUP));



        if (controller.isPressed(Keys.DEBUG)) {
            if (!isPressed) {
                debug = !debug;
                isPressed=true;
            }
        }
        else {
            isPressed = false;
        }

        /*if (controller.isPressed(Keys.SOUND)) {

            if (!isPressed) {
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
        }*/


        // Updates
        List<Drawable> buffer = new ArrayList<>();

        particles.forEach(Particle::update);
        particles.removeIf(Drawable::toRemove);

        for (Thing t : things) {
            for (Drawable newThing : t.tick(things)) {
                buffer.add(newThing);
            }
        }

        for (Drawable newThing : buffer) {
            addThing(newThing);
        }

        things.removeIf(Drawable::toRemove);

        cam.update(this.getContentPane().getWidth(), this.getContentPane().getHeight());
        updateOffset(cam.x()-this.getContentPane().getWidth()/2,cam.y()-this.getContentPane().getHeight()/2); // Follow the player
    }

    private void restart() {
        things.clear();
        drawables.clear();
        init();
    }

    public void run() {
        controller.run();
    }

    public void render(Graphics2D g2d) {

        // Apply camera offset while drawing

        try {
            drawables.sort((o1, o2) -> { // Comparison method violates its general contract
                int result = o2.getDrawDepth() - o1.getDrawDepth();
                if (result == 0) {
                    return (int) (o1.yC() - o2.yC());
                } else return result;
            });
        } catch (IllegalArgumentException e) {
            System.err.println("Desynchronization problem");
        }

        for (Iterator<Drawable> iterator = drawables.iterator(); iterator.hasNext(); ) {
            Drawable d = iterator.next();

            if (d instanceof Thing){ //shadow code
                Thing t = (Thing) d;
                Collision c = t.getCollision();

                if (t.z() < 0) {

                    Color shadowColor = new Color(0,0,0,100);
                    g2d.setColor(shadowColor);
                    g2d.fillOval((int) (c.getX() - xOffset),(int) (c.getY() -  yOffset), c.getWidth(), c.getHeight());
                }
            }

            if (!d.isInvisible()) {
                g2d.drawImage(d.getImage(),
                        (int) (d.x() - xOffset),
                        (int) (d.y() - yOffset + d.z()),
                        (int) d.width(),
                        (int) d.height(),
                        null);
            }

            if (d.toRemove()) iterator.remove();

            if (debug) {
                if (d instanceof Thing) {
                    Collision c = ((Thing) d).getCollision();
                    g2d.drawRect(c.getX() - (int) xOffset, c.getY() - (int) yOffset, c.getWidth(), c.getHeight());
                }
            }

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
        drawables.add(d);
        if (d instanceof Thing) {
            things.add((Thing) d);
        } else if (d instanceof Particle){
            particles.add((Particle) d);
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
