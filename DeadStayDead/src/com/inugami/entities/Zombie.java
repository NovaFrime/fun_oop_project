package com.inugami.entities;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.AudioInputStream;

public class Zombie {
    protected int x, y;
    protected int size;
    protected int speed;
    protected int health;
    protected BufferedImage[] frames;
    protected int currentFrame;
    protected int frameDelay;
    protected Clip zombieSound;

    public Zombie(int x, int y) {
        this.x = x;
        this.y = y;
        this.size = 50;
        this.speed = 1;
        this.health = 1;
        loadFrames();
        currentFrame = 0;
        frameDelay = 0;
        loadSound();
        playSound(zombieSound);
    }

    protected void loadFrames() {
        try {
            frames = new BufferedImage[3];
            frames[0] = ImageIO.read(getClass().getResource("/zombie/zombie1.png"));
            frames[1] = ImageIO.read(getClass().getResource("/zombie/zombie2.png"));
            frames[2] = ImageIO.read(getClass().getResource("/zombie/zombie3.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void loadSound() {
        try {
            zombieSound = AudioSystem.getClip();
            AudioInputStream zombieInputStream = AudioSystem.getAudioInputStream(getClass().getResource("/sound/zombie.wav"));
            zombieSound.open(zombieInputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void playSound(Clip sound) {
        if (sound.isRunning()) {
            sound.stop();
        }
        sound.setFramePosition(0);
        sound.start();
    }

    public void update() {
        size += 0.5;
        y += speed;
        frameDelay++;
        if (frameDelay % 10 == 0) {
            currentFrame = (currentFrame + 1) % frames.length;
        }
    }

    public void draw(Graphics g) {
        g.drawImage(frames[currentFrame], x - size / 2, y - size / 2, size, size, null);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getSize() {
        return size;
    }
}
