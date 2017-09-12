package Normal;

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
    protected double width = 1;
    protected double height = 1;

    protected Image image;
    protected Collision collision;

    protected double weight = 10;

    protected boolean alive = false;

    protected double strength = 10;
    protected double intelligence = 10;
    protected double hp;
    protected double maxHP = 10;
    protected double armor = 0;

    protected double flammability = 1;


    public Thing(Collision c){ this.collision = c;}

    public Collision getCollsion() {
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
        return image;
    }

    public void setImage(Image image){this.image = image;}

    public abstract void update(List<Thing> list);
}
