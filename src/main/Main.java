
package main;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        // create the game window
        JFrame window = new JFrame();
        window.setTitle("JavaGameV2Woo!");

        window.setResizable(false);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        GameWindow gw = new GameWindow();

        window.add(gw);
        window.pack();

        gw.initGameThread();
        window.setLocationRelativeTo(null);
        window.setVisible(true);
    }
}
