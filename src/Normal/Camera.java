package Normal;

import java.awt.*;

/**
 * An object that the camera can look at making it trail behind the player.
 * Not too complicated, the further from the player, the more it accelerates.
 */
public class Camera {

    private static final double RATE = 0.1;
    private static final double STOP_THRESHOLD = 0.1;
    private double x ;
    private double y ;
    private double dx;
    private double dy;

    private static int shakeAmount = 0;

    private Drawable friend;

    public Camera(Drawable friend){
        this.friend = friend;
        x = friend.xC();
        y = friend.yC();
        dx = 0;
        dy = 0;
    }


    private double clamp(double val, double min, double max){
        return Math.max(min, Math.min(max, val));
    }

    public void update(int width, int height){

        dx = RATE*(friend.xC()-x);
        dy = RATE*(friend.yC()-y);

        if (Math.abs(friend.xC()-x) > STOP_THRESHOLD) x += dx;
        else dx = 0;

        if (Math.abs(friend.yC()-y) > STOP_THRESHOLD) y += dy;
        else dy = 0;


        Vector shake = Vector.getComponents(Math.random() * shakeAmount, Math.random() * 2 * Math.PI);

        x = clamp(x + shake.x, width/2, 1000);
        y = clamp(y + shake.y, height/2, 1000);

        shakeAmount = Math.max(0, shakeAmount - 1);
    }

    public double x() {
        return x;
    }

    public double y() {
        return y;
    }

    public static void shake(int intensity){
        shakeAmount = intensity;
    }




}
