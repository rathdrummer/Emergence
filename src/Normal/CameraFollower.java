package Normal;

import java.awt.*;

/**
 * An object that the camera can look at making it trail behind the player.
 * Not too complicated, the further from the player, the more it accelerates.
 */
public class CameraFollower {

    private static final double RATE = 0.1;
    private static final double STOP_THRESHOLD = 0.1;
    private double x ;
    private double y ;
    private double dx;
    private double dy;

    private Drawable friend;

    public CameraFollower(Drawable friend){
        this.friend = friend;
        x = friend.xC();
        y = friend.yC();
        dx = 0;
        dy = 0;
    }


    public void update(){

        dx = RATE*(friend.xC()-x);
        dy = RATE*(friend.yC()-y);

        if (Math.abs(friend.xC()-x) > STOP_THRESHOLD) x += dx;
        else dx = 0;

        if (Math.abs(friend.yC()-y) > STOP_THRESHOLD) y += dy;
        else dy = 0;
    }

    public double x() {
        return x;
    }

    public double y() {
        return y;
    }


}
