public class Main extends Thread{

    public static final int FPS = 60;
    private View view;
    private Input input;

    public Main(){
        this.view = new View();
        this.input = new Input();
        this.view.addKeyListener(input);
        this.view.start();
    }


    public static void main(String[] args) throws InterruptedException{
        Main m = new Main();

        Player me = new Player("Me");
        m.view.add(me);
        long time;
        double timeElapsed;
        double tickTime = 1000000000D/FPS;

        while (true){
            time = System.nanoTime();

            if (m.input.isPressed("up")) me.incrementY(false);
            if (m.input.isPressed("down")) me.incrementY();
            if (m.input.isPressed("left")) me.incrementX(false);
            if (m.input.isPressed("right")) me.incrementX();

            timeElapsed = System.nanoTime()-time;
            if (timeElapsed < tickTime) Thread.sleep((long)(tickTime-timeElapsed)/1000000);
        }

    }
}
