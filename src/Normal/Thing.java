package Normal;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.awt.*;

/**
 * The overarching class for all things in-game.
 */
public abstract class Thing extends Drawable{

    protected double x = 0;
    protected double y = 0;
    protected double dx = 0;
    protected double dy = 0;
    protected double width = 400;
    protected double height = 100;
    protected double acceleration = 1;
    protected Thing owner;

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

    public Thing(Sprite sprite, Vector vector){
        this(sprite, vector.x, vector.y);
    }

    public Thing(Sprite sprite, double x, double y){
        width = sprite.getWidth();
        height = sprite.getHeight();
        this.x = x;
        this.y = y;
        collision = new Collision(x,y,width,height);
        setSprite(sprite, new AnimationType(AnimationEnum.Normal));
    }

    public Vector centreOnLeftCorner(){
        return new Vector(x-width/2, y-height/2);
    }

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
        return changeSpriteTo(animationType, 0);
    }

    private boolean changeSpriteTo(AnimationType animationType, int index) {
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

    public void setSpeeds(Vector v) {
        this.dx = v.x;
        this.dy = v.y;
    }

    public void setPosition(Vector v) {
        this.x = v.x;
        this.y = v.y;

    }
    /*
        Wrapper for the update so we do not have to write duplicate code,
        Here we put code that all "Things" should run, like update their sprite and such.
    */
    public final List<Thing> tick(List<Thing> things) {
        List<Thing> newThings = update(things);

        if (animate) {
            Sprite sprite = getSprite();
            sprite.update();
            if (this instanceof Player) {
                System.out.println(sprite.getImageIndex());
            }

        }

        if (newThings == null) {
            newThings = new ArrayList<>();
        }

        if (alive && hp <= 0) this.alive = false;

        return newThings;
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

    public abstract List<Thing> update(List<Thing> list);

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

    protected void handleCollisions(List<Thing> things, boolean stopWhenHitWall, boolean applyKnockBack) {
        Collision c = Collision.speedBox(x,y,width, height, dx, dy);


        List<Thing> allPossibleCollisions = c.collidesWithThing(things, this);

        allPossibleCollisions.removeIf(thing -> this.owner == thing || this == thing.owner);

            //x direction
        int newDX = (int) dx;
        int oldSign = (int) Math.signum(dx);

        c = Collision.speedBox(x, y, width, height, newDX, 0);
        List<Thing> allXCollisions = c.collidesWithThing(allPossibleCollisions, this);
        allXCollisions.forEach(t -> t.harm(0, new Vector(dx, 0)));
        if (stopWhenHitWall && !allXCollisions.isEmpty()) {
            dx = 0;
            newDX = 0;
        }


        while(!allXCollisions.isEmpty() && newDX != 0) {
            newDX -= Math.signum(dx);
            if (Math.signum(newDX) != oldSign) {
                newDX = 0;
            }
            c = Collision.speedBox(x,y,width,height, newDX, 0);
            allXCollisions = c.collidesWithThing(allXCollisions, this);
        }

        x += newDX;

        //y direction
        int newDY = (int) dy;
        oldSign = (int) Math.signum(dy);

        c = Collision.speedBox(x, y, width, height, 0, newDY);
        List<Thing> allYCollisions = c.collidesWithThing(allPossibleCollisions, this);
        allXCollisions.forEach(t -> t.harm(0, new Vector(0, dy)));

        if (stopWhenHitWall && !allYCollisions.isEmpty()) {
            dy = 0;
            newDY = 0;
        }

        while(!allYCollisions.isEmpty() && newDY != 0) {
            newDY -= Math.signum(dy);
            if (Math.signum(newDY) != oldSign) {
                newDY = 0;
            }
            c = Collision.speedBox(x,y,width,height, 0, newDY);
            allYCollisions = c.collidesWithThing(allYCollisions, this);
        }


        y += newDY;

        collision.updatePosition(x,y);
    }

    public void setOwner(Thing owner) {
        this.owner = owner;
    }

    public void harm(int damage, Vector knockback){
        this.hp-=damage;
        dx+=knockback.x;
        dy+=knockback.y;
    }

}
