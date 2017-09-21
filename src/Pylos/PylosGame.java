package Pylos;

import Normal.Backend;
import org.junit.Assert;

import javax.swing.*;
import java.awt.*;

/**
 * Created by oskar on 2017-09-20.
 * This classes has some inputs and outputs
 */
public class PylosGame extends JFrame {


    private final Backend controller;
    private Level bottom;
    private Level second;
    private Level third;
    private Level top;

    private Button decline = new Button(0, 50, 50, 40, "decline");

    private Button restart = new Button(0, 50, 50, 40, "restart");

    private int mouseX = 0;
    private int mouseY = 0;
    private boolean isPressed = false;
    private boolean tookFromPile = false;

    private int xOffset = 100;
    private int yOffset = 100;
    private int scale = 50;
    private OwnedBy turn = OwnedBy.P1;
    private ClickState clickState = ClickState.Normal;
    private int combo = 0;
    private Pile p1Pile;
    private Pile p2Pile;
    private Cell heldBall = null;
    private boolean clickAble = false;
    private OwnedBy winner = OwnedBy.NoPlayer;

    public PylosGame() {
        this.controller = new Backend(e -> update(), this::render);
        controller.setMouseListener(this::updateMouse);

        init();

        bottom.setAbove(second);
        second.setAbove(third);
        third.setAbove(top);
        top.setAbove(null);

        initUI();
    }

    public void init() {

        p1Pile = new Pile(OwnedBy.P1, 400, 100);
        p2Pile = new Pile(OwnedBy.P2, 400, 300);

        bottom = new Level(4, null, 0);
        second = new Level(3, bottom, scale/2);
        third = new Level(2, second, scale);
        top = new Level(1, third, (int) (scale * 1.5));

        clickState = ClickState.Normal;
        turn = OwnedBy.P1;
        tookFromPile = false;
        heldBall = null;
        combo = 0;
        winner = OwnedBy.NoPlayer;
    }

    private void updateMouse(int x, int y) {
        mouseX = x;
        mouseY = y;
        isPressed = true;
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

    public Pile click(Pile pile, int mouseX, int mouseY) {
        if (mouseX > pile.getX() && mouseX < pile.getX() + scale && mouseY > pile.getY() && mouseY < pile.getY() + scale ) {
            return pile;
        }
        else return null;
    }

    public boolean click(int mouseX, int mouseY) {
        if (winner != OwnedBy.NoPlayer) {
            if (restart.isClicked(mouseX, mouseY)) {
                init();
            }
        }

        if (declineIsActive()) {
            if (decline.isClicked(mouseX, mouseY)) {
                return true;
            }
        }

        int mouseXAltered = mouseX - xOffset;
        int mouseYAltered = mouseY - yOffset;

        Pile pile;
        if (turn == OwnedBy.P1) {
            pile = click(p1Pile, mouseX, mouseY);
        } else {
            pile = click(p2Pile, mouseX, mouseY);
        }

        Cell clicked = clickedCell(mouseXAltered, mouseYAltered);

        Cell took = takeBallFrom(mouseXAltered, mouseYAltered);


        if (clickState == ClickState.Lift) {

            if (heldBall == null) {
                //choose one that is not blocked or from pile
                if (took == null) {
                    if (pile != null) {
                        if (pile.hasLeft()) pile.take();
                        heldBall = new Cell(-1, -1, bottom, turn); // dummy cell, not to be added directly
                        tookFromPile = true;
                    }
                }
                else {
                    System.out.println("picked up ball from field");
                    took.setCellState(OwnedBy.NoPlayer);
                    heldBall = took;
                    tookFromPile = false;
                }
            }
            else {
                if (clicked == null) {
                    if (pile != null && tookFromPile) { // putting back into pile
                        heldBall = null;
                        pile.addBack();
                        clickState = ClickState.Lift;
                    }
                }
                else {
                    System.out.println("Place at level: " +  clicked.getZ() + ". From level: " + heldBall.getZ());
                    if (clicked.getZ() < heldBall.getZ()) {
                        clicked.setCellState(turn);
                        heldBall = null;
                        return true;
                    } else if (clicked == heldBall) { // putting it back
                        clicked.setCellState(turn);
                        heldBall = null;
                    }
                    else {
                        System.err.println("The ball has to be lifted up a level");
                    }
                }
            }
            return false;
        }

        if (clickState == ClickState.Combo) {
            if (took != null) {
                took.setCellState(OwnedBy.NoPlayer);
                Pile p;
                if (turn == OwnedBy.P1) {
                    p = p1Pile;
                }
                else p = p2Pile;

                p.addBack();

                combo--;
            }

            if (combo == 0) {
                return true;
            }
            else return false;
        }

        if (clickState == ClickState.Normal && clicked != null) {
            clicked.setCellState(turn);
            if (turn == OwnedBy.P1) {
                p1Pile.take();
            }
            else {
                p2Pile.take();
            }

            Level level = clicked.getLevel();

            boolean row    = level.getSize() > 2 && level.colorRow(clicked.getY(), turn);
            boolean column = level.getSize() > 2 && level.colorColumn(clicked.getX(), turn);
            boolean square = level.getSize() > 2 && level.colorSquare(clicked.getX(), clicked.getY(), turn);

            //System.out.println("row: " + row +", column: " + column + ", square: " + square);

            if (row || column || square) {
                clickState = ClickState.Combo;
                combo = 2;
                return false;
            } else if (level.normalSquare(clicked.getX(), clicked.getY())) {
                clickState = ClickState.Lift;
                return false;
            }
        }

        return clicked != null;
    }

    private Cell takeBallFrom(int mouseX, int mouseY) {

        Cell clicked = bottom.takeCandidate(mouseX, mouseY, scale, turn);
        if (clicked == null) {
            clicked = second.takeCandidate(mouseX, mouseY, scale, turn);
            if (clicked == null) {
                clicked = third.takeCandidate(mouseX, mouseY, scale, turn);
                if (clicked == null) {
                    clicked = top.takeCandidate(mouseX, mouseY, scale, turn);
                }
            }
        }
        return clicked;
    }

    private Cell clickedCell(int mouseX, int mouseY) {
        Cell clicked = bottom.addCandidate(mouseX, mouseY, scale);
        if (clicked == null) {
            clicked = second.addCandidate(mouseX, mouseY, scale);
            if (clicked == null) {
                clicked = third.addCandidate(mouseX, mouseY, scale);
                if (clicked == null) {
                    clicked = top.addCandidate(mouseX,mouseY, scale);
                }
            }
        }
        return clicked;
    }

    private void toggleTurn() {
        Assert.assertTrue(heldBall == null);
        clickState = ClickState.Normal;
        if (turn == OwnedBy.P1) turn = OwnedBy.P2;
        else turn = OwnedBy.P1;
        tookFromPile = false;

        if (!p1Pile.hasLeft()) {
            winner = OwnedBy.P2;
        }
        else if (!p2Pile.hasLeft()) {
            winner = OwnedBy.P1;
        }
    }

    public void run() {
        controller.run();
    }

    private void render(Graphics2D graphics2D) {
        for (Cell[] cells : bottom.getCells()) {
            for (Cell c : cells) {
                draw(graphics2D, c, bottom.getOffset());
            }
        }

        for (Cell[] cells : second.getCells()) {
            for (Cell c : cells) {
                draw(graphics2D, c, second.getOffset());
            }
        }

        for (Cell[] cells : third.getCells()) {
            for (Cell c : cells) {
                draw(graphics2D, c, third.getOffset());
            }
        }

        for (Cell[] cells : top.getCells()) {
            for (Cell c : cells) {
                draw(graphics2D, c, top.getOffset());
            }
        }

        drawPile(graphics2D, p1Pile, Color.BLUE);
        drawPile(graphics2D, p2Pile, Color.RED);

        //decline button
        if (declineIsActive()) {
            drawButton(graphics2D, decline);
        }

        //restart button
        if (winner != OwnedBy.NoPlayer) {
            drawButton(graphics2D, restart);
        }

        if (heldBall != null) {
            Point position = controller.getMousePosition();
            if (position != null) {
                drawBall(graphics2D, (int) controller.getMousePosition().getX() - scale/2, (int) controller.getMousePosition().getY() - scale/2);
            }
        }

        graphics2D.setColor(Color.BLACK);
        drawState(graphics2D, 100, 20);
    }

    private void drawButton(Graphics2D graphics2D, Button button) {

        graphics2D.setColor(Color.BLACK);
        graphics2D.drawRect(button.getX(), button.getY(), button.getWidth(), button.getHeight());
        graphics2D.drawString(button.getDisplay(),button.getX() + 4, button.getY() + button.getHeight()/2);
    }

    private void drawBall(Graphics2D g2d, int x, int y) {
        if (turn == OwnedBy.P1) g2d.setColor(Color.BLUE);
        else g2d.setColor(Color.RED);

        g2d.fillOval(x, y, scale, scale);
    }

    private void drawPile(Graphics2D graphics2D, Pile p, Color c) {

        int scale = this.scale;
        if (turn != p.getOwner()) {
            scale *= 0.6;
        }

        graphics2D.setColor(c);

        if (heldBall != null && tookFromPile && turn == p.getOwner()) {
            graphics2D.fillOval(p.getX(), p.getY(), scale, scale);
        }
        else {
            if (clickState == ClickState.Lift && turn == p.getOwner()) {
                graphics2D.fillRect(p.getX(), p.getY(), scale, scale);
            }
            else {
                graphics2D.drawRect(p.getX(), p.getY(), scale, scale);
            }
        }

        graphics2D.drawString(Integer.toString(p.size()), p.getX() + scale + 10, p.getY());
    }

    private boolean declineIsActive() {
        return clickState == ClickState.Lift || clickState == ClickState.Combo;
    }

    private void drawState(Graphics2D g2d, int x, int y) {
        switch(clickState) {
            case Combo:
                g2d.drawString("Remove one or two of your pieces", x, y);
                break;
            case Lift:
                g2d.drawString("Lift up one of your pieces from the board or your pile", x, y);
                break;
            case Normal:default:
                g2d.drawString("Place down one of you pieces on the board", x, y);
                break;
        }
    }

    private void draw(Graphics2D g2d, Cell c, int offset) {
        if (!c.getLevel().isSolid(c.getX(), c.getY())) {
            return;
        }
        if (c.getCellState() == OwnedBy.NoPlayer) {
            g2d.setColor(Color.GRAY);
            g2d.drawOval(offset + xOffset + c.getX() * scale, offset + yOffset + c.getY() * scale, scale, scale);
        }
        else {
            if (c.getCellState() == OwnedBy.P1) g2d.setColor(Color.BLUE);
            else if (c.getCellState() == OwnedBy.P2) g2d.setColor(Color.RED);
            g2d.fillOval(offset + xOffset + c.getX() * scale, offset + yOffset + c.getY() * scale, scale, scale);


            g2d.setColor(Color.WHITE);
            g2d.drawOval(offset + xOffset + c.getX() * scale, offset + yOffset + c.getY() * scale, scale, scale);
        }

    }

    private void update() {

        if (isPressed) {
            if (clickAble) {
                if (click(mouseX, mouseY)) {
                    toggleTurn();
                }
                isPressed = false;
                clickAble = false;
            }
        }
        else clickAble = true;

    }

    public static void main(String[] args) throws InterruptedException{
        PylosGame m = new PylosGame();

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
}
