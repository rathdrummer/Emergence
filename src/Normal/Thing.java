package Normal;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.List;
import java.awt.*;

/**
 * The overarching class for all things in-game.
 */
public abstract class Thing implements Drawable{

    protected double x = 0;
    protected double y = 0;
    protected double dx = 0;
    protected double dy = 0;
    protected double width = 400;
    protected double height = 100;
    protected double acceleration = 1;

    protected HashMap<AnimationType, Sprite> sprites = new HashMap<>();
    protected AnimationType currentSprite = null;
    protected Collision collision = null;

    protected double weight = 10;

    protected boolean alive = false;

    protected double strength = 10;
    protected double intelligence = 10;
    protected double hp;
    protected double maxHP = 10;
    protected double armor = 0;

    protected double flammability = 1;
    protected double frictionAcc=0.5;
    protected double maxSpeed=7;
    private boolean animate = true;


    public Thing(Collision c){ this.collision = c;}

    public Collision getCollision() {
        return collision;
    }

    public double x() {
        return x;
    }

    public double y() {
        return y;
    }

    public double dx() {
        return dx;
    }

    public void setDx(double dx) {
        this.dx = dx;
    }

    public double dy() {
        return dy;
    }

    public void setDy(double dy) {
        this.dy = dy;
    }

    public double width() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double height() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double weight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public double strength() {
        return strength;
    }

    public void setStrength(double strength) {
        this.strength = strength;
    }

    public double intelligence() {
        return intelligence;
    }

    public void setIntelligence(double intelligence) {
        this.intelligence = intelligence;
    }

    public double maxHP() {
        return maxHP;
    }

    public void setMaxHP(double maxHP) {
        this.maxHP = maxHP;
    }

    public double armor() {
        return armor;
    }

    public void setArmor(double armor) {
        this.armor = armor;
    }

    public double flammability() {
        return flammability;
    }

    public void setFlammability(double flammability) {
        this.flammability = flammability;
    }

    public double hp(){return hp;}

    public void setHP(double hp){this.hp = hp;}

    public Image getImage(){
        return getSprite().getImage();
    }

    public void setSprite(Sprite sprite, AnimationType ae){
        addSprite(sprite, ae);
        currentSprite = ae;
    }

    public void setSprite(String fileName, AnimationType ae){
        Sprite sprite = new Sprite(fileName);
        addSprite(sprite, ae);
        currentSprite = ae;
    }

    public void setSprite(BufferedImage image, AnimationType ae){
        Sprite sprite = new Sprite(image);
        addSprite(sprite, ae);
        currentSprite = ae;
    }

    /**
     * Use this to switch between animations
     * @param animationType
     * @return
     */
    public boolean changeSpriteTo(AnimationType animationType) {
        if (animationType.equals(currentSprite)) {
            return true;
        }
        getSprite().setImageIndex(0); // restarts the old sprite
        return changeSpriteTo(animationType, 0);
    }

    private boolean changeSpriteTo(AnimationType animationType, int index) {
        if (!animationType.equals(currentSprite)) {
            getSprite().setImageIndex(0); // resets the old sprite
        }

        Sprite spr = sprites.get(animationType);
        if (spr != null) {
            currentSprite = animationType;
            spr.setImageIndex(index);
            return true;
        }
        else {
            return false;
        }
    }

    /*
        Wrapper for the update so we do not have to write duplicate code,
        Here we put code that all "Things" should run, like update their sprite and such.
    */
    public final void tick(List<Thing> things) {
        update(things);

        if (animate) {
            Sprite sprite = getSprite();
            sprite.update();
            if (this instanceof Player) {
                System.out.println(sprite.getImageIndex());
            }

        }
    }

    public void addSprite(String fileName, AnimationType at) {

        Sprite sprite = new Sprite(Library.loadImage(fileName));

        sprites.put(at, sprite);
        // if it is the first one added, set as current sprite
        if (sprites.size() == 1 && currentSprite == null) {
            currentSprite = at;
        }
    }

    protected void pauseAnimation() {
        animate = false;
    }

    protected void playAnimation() {
        animate = true;
    }



    public void addSprite(Sprite sprite, AnimationType at) {
        sprites.put(at, sprite);
        // if it is the first one added, set as current sprite
        if (sprites.size() == 1 && currentSprite == null) {
            currentSprite = at;
        }
    }

    public abstract void update(List<Thing> list);

    public double clamp(double val, double range){
        return Math.max(-range, Math.min(range, val));
    }

    public Vector clamp2(double x, double y, double max){
        double hyp = Math.sqrt(Math.pow(x,2)+Math.pow(y,2));
        if (hyp > max) {
            double ratio = hyp / max;
            x /= ratio;
            y /= ratio;
        }
        return new Vector(x,y);
    }

    public Sprite getSprite() {
        return sprites.get(currentSprite);
    }

    protected class Vector {

        public final double x;
        public final double y;

        Vector(double x, double y){
            this.x = x;
            this.y = y;
        }
    }

    protected void updateSpeed(){
        Vector v = clamp2(dx,dy,maxSpeed);
        dx = friction(v.x);
        dy = friction(v.y);
    }

    public double friction(double val){
        if (Math.abs(val) <= frictionAcc){
            val = 0;
        } else {
            val-=Math.signum(val)* frictionAcc;
        }

        return val;
    }

    protected void handleCollisions(List<Thing> things, boolean stopWhenHitWall) {
        Collision c = Collision.speedBox(xC(),yC(),width, height, dx, dy);


        List<Thing> allPossibleCollisions = c.collidesWithThing(things, this);

        //x direction
        int newDX = (int) dx;
        int oldSign = (int) Math.signum(dx);

        c = Collision.speedBox(xC(), yC(), width, height, newDX, 0);
        List<Thing> allXCollisions = c.collidesWithThing(allPossibleCollisions, this);
        if (stopWhenHitWall && !allXCollisions.isEmpty()) {
            dx = 0;
            newDX = 0;
        }


        while(!allXCollisions.isEmpty() && newDX != 0) {
            newDX -= Math.signum(dx);
            if (Math.signum(newDX) != oldSign) {
                newDX = 0;
            }
            c = Collision.speedBox(xC(),yC(),width,height, newDX, 0);
            allXCollisions = c.collidesWithThing(allXCollisions, this);
        }

        x += newDX;

        //y direction
        int newDY = (int) dy;
        oldSign = (int) Math.signum(dy);

        c = Collision.speedBox(xC(), yC(), width, height, 0, newDY);
        List<Thing> allYCollisions = c.collidesWithThing(allPossibleCollisions, this);

        if (stopWhenHitWall && !allYCollisions.isEmpty()) {
            dy = 0;
            newDY = 0;
        }

        while(!allYCollisions.isEmpty() && newDY != 0) {
            newDY -= Math.signum(dy);
            if (Math.signum(newDY) != oldSign) {
                newDY = 0;
            }
            c = Collision.speedBox(xC(),yC(),width,height, 0, newDY);
            allYCollisions = c.collidesWithThing(allYCollisions, this);
        }


        y += newDY;

        collision.updatePosition(xC(),yC());
    }

}
