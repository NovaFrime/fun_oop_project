package com.inugami.main;
import javax.swing.*;
import java.awt.*;

public class Game {
    private static JFrame frame;
    private static GamePanel gamePanel;
    private static MenuPanel menuPanel;

    public static void main(String[] args) {
        frame = new JFrame("Dead Stay Dead");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLayout(new CardLayout());

        gamePanel = new GamePanel();
        menuPanel = new MenuPanel();

        frame.add(menuPanel, "Menu");
        frame.add(gamePanel, "Game");

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        showMenu();
    }

    public static void showMenu() {
        ((CardLayout) frame.getContentPane().getLayout()).show(frame.getContentPane(), "Menu");
    }

    public static void startGame() {
        ((CardLayout) frame.getContentPane().getLayout()).show(frame.getContentPane(), "Game");
        gamePanel.startGame();
    }
}
