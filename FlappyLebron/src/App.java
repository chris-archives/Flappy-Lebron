import java.awt.Component;

import javax.swing.*;

public class App {
    public static void main(String[] args) throws Exception {
        int boardWidth =360;
        int boardHeight = 640;

        JFrame frame = new JFrame("Flappy Lebron");
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        FlappyLebron flappylebron = new FlappyLebron();
        frame.add(flappylebron);
        frame.pack();
        flappylebron.requestFocus();
        frame.setVisible(true);
    }
}
