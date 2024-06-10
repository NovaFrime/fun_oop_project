package com.inugami.entities;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;

public class RunnerZombie extends Zombie {
    public RunnerZombie(int x, int y) {
        super(x, y);
        this.speed = 2;
        this.size = 40;
        loadFrames();
    }

    @Override
    protected void loadFrames() {
        try {
            frames = new BufferedImage[3];
            frames[0] = ImageIO.read(getClass().getResource("/zombie/runner_zombie_1.png"));
            frames[1] = ImageIO.read(getClass().getResource("/zombie/runner_zombie_2.png"));
            frames[2] = ImageIO.read(getClass().getResource("/zombie/runner_zombie_3.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
