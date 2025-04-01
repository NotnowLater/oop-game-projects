package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

public class KeyInputHandler implements KeyListener , Processable{

    public  boolean upPressed, downPressed, leftPressed, rightPressed, confirm, cancel, menu, quit;

    public KeyJustPressedProcessor confirmJustPressed = new KeyJustPressedProcessor();
    public KeyJustPressedProcessor cancelJustPressed = new KeyJustPressedProcessor();
    public KeyJustPressedProcessor upJustPressed = new KeyJustPressedProcessor();
    public KeyJustPressedProcessor downJustPressed = new KeyJustPressedProcessor();
    public KeyJustPressedProcessor leftJustPressed = new KeyJustPressedProcessor();
    public KeyJustPressedProcessor rightJustPressed = new KeyJustPressedProcessor();
    public KeyJustPressedProcessor menuJustPressed = new KeyJustPressedProcessor();
    public KeyJustPressedProcessor quitJustPressed = new KeyJustPressedProcessor();

    public GameWindow gW;

    public KeyInputHandler (GameWindow gW){
        this.gW = gW;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int kc = e.getKeyCode();
        if (kc == KeyEvent.VK_UP){
            upPressed = true;
        }
        if (kc == KeyEvent.VK_DOWN){
            downPressed = true;
        }
        if (kc == KeyEvent.VK_LEFT){
            leftPressed = true;
        }
        if (kc == KeyEvent.VK_RIGHT){
            rightPressed = true;
        }
        if (kc == KeyEvent.VK_Z){
            confirm = true;
        }
        if (kc == KeyEvent.VK_X) {
            cancel = true;
        }
        if (kc == KeyEvent.VK_C){
            menu = true;
        }
        if (kc == KeyEvent.VK_Q){
            quit = true;
        }
//        if (kc == KeyEvent.VK_X) {
//            gW.player.shoot();
//        }

    }

    @Override
    public void keyReleased(KeyEvent e) {
        int kc = e.getKeyCode();
        if (kc == KeyEvent.VK_UP){
            upPressed = false;
        }
        if (kc == KeyEvent.VK_DOWN){
            downPressed = false;
        }
        if (kc == KeyEvent.VK_LEFT){
            leftPressed = false;
        }
        if (kc == KeyEvent.VK_RIGHT){
            rightPressed = false;
        }
        if (kc == KeyEvent.VK_Z){
            confirm = false;
        }
        if (kc == KeyEvent.VK_X){
            cancel = false;
        }
        if (kc == KeyEvent.VK_SPACE){
            cancel = false;
        }
        if (kc == KeyEvent.VK_C){
            menu = false;
        }
        if (kc == KeyEvent.VK_Q){
            quit = false;
        }
    }

    @Override
    public void process() {
        confirmJustPressed.setPressed(confirm);
        confirmJustPressed.process();

        cancelJustPressed.setPressed(cancel);
        cancelJustPressed.process();

        upJustPressed.setPressed(upPressed);
        upJustPressed.process();

        downJustPressed.setPressed(downPressed);
        downJustPressed.process();

        leftJustPressed.setPressed(leftPressed);
        leftJustPressed.process();

        rightJustPressed.setPressed(rightPressed);
        rightJustPressed.process();

        menuJustPressed.setPressed(menu);
        menuJustPressed.process();

        quitJustPressed.setPressed(quit);
        quitJustPressed.process();
    }
}
