
package main;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        // create the game window

        JFrame window = new JFrame();
        window.setTitle("Element Mann");

        window.setResizable(false);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        GameWindow gw = new GameWindow(window);

        if (args.length > 0){
            if (args[0].equals("-debug")){
                gw.setDebugEnable(true);
            }
        }

        window.add(gw);
        window.pack();

        gw.initGameThread();
        window.setLocationRelativeTo(null);
        window.setVisible(true);
    }
}
