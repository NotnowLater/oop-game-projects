package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyInputHandler implements KeyListener {

    public  boolean upPressed, downPressed, leftPressed, rightPressed, confirm, cancel, menu;

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
        if (kc == KeyEvent.VK_X) {
            gW.player.shoot();
        }

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
    }
}
