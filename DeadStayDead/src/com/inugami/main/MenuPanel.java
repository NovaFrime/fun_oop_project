package com.inugami.main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuPanel extends JPanel {
    private JButton startButton;

    public MenuPanel() {
        setPreferredSize(new Dimension(800, 600));
        setLayout(new BorderLayout());
        JLabel title = new JLabel("Dead Stay Dead", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 36));
        add(title, BorderLayout.NORTH);

        startButton = new JButton("Start Game");
        startButton.setFont(new Font("Arial", Font.BOLD, 24));
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Game.startGame();
            }
        });
        add(startButton, BorderLayout.CENTER);
    }
}
