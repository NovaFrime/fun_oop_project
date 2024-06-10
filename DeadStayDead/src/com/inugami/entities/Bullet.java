package com.inugami.entities;
import java.awt.*;

public class Bullet {
    private int x, y;
    private int speed;
    private int radius;
    private float alpha;

    public Bullet(int x, int y) {
        this.x = x;
        this.y = y;
        this.speed = 5;
        this.radius = 5;
        this.alpha = 1.0f;
    }

    public void update() {
        y -= speed;
        alpha -= 10f;
        if (alpha < 0) alpha = 0;
    }

    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        g2d.setColor(Color.YELLOW);
        g2d.fillOval(x - radius / 2, y - radius / 2, radius, radius);
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
    }

    public boolean isCollidingWith(Zombie zombie) {
        int zombieX = zombie.getX();
        int zombieY = zombie.getY();
        int zombieSize = zombie.getSize();
        int distance = (int) Math.sqrt(Math.pow(zombieX - x, 2) + Math.pow(zombieY - y, 2));
        return distance < (zombieSize / 2 + radius / 2);
    }

    public boolean isOnScreen(int screenWidth, int screenHeight) {
        return y > 0 && y < screenHeight && alpha > 0;
    }
}
