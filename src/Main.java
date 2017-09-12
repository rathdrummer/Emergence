import javax.swing.*;

public class Main extends Thread{

    public static final int FPS = 60;
    private View view;
    private Input input;

    public Main(){
        this.view = new View();
        this.input = new Input();
        this.view.addKeyListener(input);
        // Used when run() was looped in the View
        //this.view.start();
    }


    public static void main(String[] args) throws InterruptedException{
        Main m = new Main();


        Background bg = new Background("car-road-rug.jpg");
        m.view.objectStack.add(bg);
        Player me = new Player("circle.png");
        m.view.objectStack.add(me);



        long time;
        double timeElapsed;
        double tickTime = 1000000000D/FPS;

        while (true){
            time = System.nanoTime();

            if (m.input.isPressed("up")) me.incrementDY(false);
            if (m.input.isPressed("down")) me.incrementDY(true);
            if (m.input.isPressed("left")) me.incrementDX(false);
            if (m.input.isPressed("right")) me.incrementDX(true);

            // Updates

            me.update();

            m.view.repaint();
            timeElapsed = System.nanoTime()-time;
            if (timeElapsed < tickTime) Thread.sleep((long)(tickTime-timeElapsed)/1000000);
        }

    }
}
