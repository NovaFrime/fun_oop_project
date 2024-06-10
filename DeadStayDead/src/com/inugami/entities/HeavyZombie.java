package com.inugami.entities;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;

public class HeavyZombie extends Zombie {
    public HeavyZombie(int x, int y) {
        super(x, y);
        this.speed = 1;
        this.size = 60;
        this.health = 2;
        loadFrames();
    }

    @Override
    protected void loadFrames() {
        try {
            frames = new BufferedImage[3];
            frames[0] = ImageIO.read(getClass().getResource("/zombie/tank_zombie.png"));
            frames[1] = ImageIO.read(getClass().getResource("/zombie/tank_zombie_2.png"));
            frames[2] = ImageIO.read(getClass().getResource("/zombie/tank_zombie_3.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update() {
        super.update();
        if (health > 1) {
            size += 0.5;
        }
    }
}
