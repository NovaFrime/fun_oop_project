package com.inugami.main;

import com.inugami.entities.Bullet;
import com.inugami.entities.HeavyZombie;
import com.inugami.entities.RunnerZombie;
import com.inugami.entities.Zombie;
import com.inugami.states.GameState;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import java.util.Random;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.AudioInputStream;
import javax.swing.Timer;
import java.util.TimerTask;

public class GamePanel extends JPanel implements ActionListener, MouseListener, MouseMotionListener {
    private Timer timer;
    private List<Zombie> zombies;
    private List<Bullet> bullets;
    private int mouseX, mouseY;
    private int score;
    private int wave;
    private BufferedImage backgroundImage, crosshairImage, gunHandImage;
    private GameState gameState;
    private Random random;
    private Clip gunshotSound, zombieSound;
    private BufferedImage[] gunHandFrames;
    private int gunHandFrameIndex;
    private TimerTask gunAnimationTask;
    private boolean isShooting;
    private int ammo;
    private final int maxAmmo = 10;
    private int reloadTime;
    private int reloadTimer;
    private boolean powerUpActive;
    private int powerUpDuration;
    private int powerUpTimer;

    public GamePanel() {
        setPreferredSize(new Dimension(800, 600));
        addMouseListener(this);
        addMouseMotionListener(this);
        zombies = new ArrayList<>();
        bullets = new ArrayList<>();
        gameState = GameState.MENU;
        loadImages();
        loadSounds();
        random = new Random();
        gunHandFrameIndex = 0;
        isShooting = false;
        ammo = maxAmmo;
        reloadTime = 250;
        reloadTimer = 0;
        powerUpActive = true;
        powerUpDuration = 10000;
        powerUpTimer = 0;
    }

    public void startGame() {
        gameState = GameState.PLAYING;
        wave = 1;
        score = 0;
        initializeZombies();
        timer = new Timer(16, this);
        timer.start();
    }

    private void loadImages() {
        try {
            backgroundImage = ImageIO.read(getClass().getResource("/background.jpg"));
            crosshairImage = ImageIO.read(getClass().getResource("/player/crosshair.png"));
            gunHandFrames = new BufferedImage[3];
            gunHandFrames[0] = ImageIO.read(getClass().getResource("/player/gun_idle.png"));
            gunHandFrames[1] = ImageIO.read(getClass().getResource("/player/gun_shot_1.png"));
            gunHandFrames[2] = ImageIO.read(getClass().getResource("/player/gun_shot_2.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadSounds() {
        try {
            gunshotSound = AudioSystem.getClip();
            AudioInputStream gunshotInputStream = AudioSystem.getAudioInputStream(getClass().getResource("/sound/gunshot.wav"));
            gunshotSound.open(gunshotInputStream);

            zombieSound = AudioSystem.getClip();
            AudioInputStream zombieInputStream = AudioSystem.getAudioInputStream(getClass().getResource("/sound/zombie.wav"));
            zombieSound.open(zombieInputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initializeZombies() {
        zombies.clear();
        for (int i = 0; i < wave * 5; i++) {
            zombies.add(createRandomZombie());
        }
    }

    private Zombie createRandomZombie() {
        int x, y;
        do {
            x = random.nextInt(800);
            y = random.nextInt(20);
        } while (Math.abs(x - 400) < 100);

        int type = random.nextInt(3);
        return switch (type) {
            case 1 -> new RunnerZombie(x, y);
            case 2 -> new HeavyZombie(x, y);
            default -> new Zombie(x, y);
        };
    }

    private void updateGameState() {
        for (Zombie zombie : zombies) {
            zombie.update();
        }
        for (Bullet bullet : bullets) {
            bullet.update();
        }
        checkCollisions();
        removeOffscreenBullets();
        checkWaveCompletion();
        checkGameOver();
        updateReload();
    }

    private void checkCollisions() {
        Iterator<Bullet> bulletIterator = bullets.iterator();
        while (bulletIterator.hasNext()) {
            Bullet bullet = bulletIterator.next();
            Iterator<Zombie> zombieIterator = zombies.iterator();
            while (zombieIterator.hasNext()) {
                Zombie zombie = zombieIterator.next();
                if (bullet.isCollidingWith(zombie)) {
                    zombieIterator.remove();
                    bulletIterator.remove();
                    score += 10;
                    break;
                }
            }
        }
    }

    private void removeOffscreenBullets() {
        bullets.removeIf(bullet -> !bullet.isOnScreen(getWidth(), getHeight()));
    }

    private void checkWaveCompletion() {
        if (zombies.isEmpty()) {
            wave++;
            initializeZombies();
        }
    }

    private void checkGameOver() {
        for (Zombie zombie : zombies) {
            if (zombie.getY() > getHeight()) {
                timer.stop();
                gameState = GameState.GAME_OVER;
                JOptionPane.showMessageDialog(this, "Game Over! Score: " + score);
                Game.showMenu();
            }
        }
    }

    private void drawGame(Graphics g) {
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), null);
        for (Zombie zombie : zombies) {
            zombie.draw(g);
        }
        for (Bullet bullet : bullets) {
            bullet.draw(g);
        }
        g.setColor(Color.WHITE);
        g.drawString("Score: " + score, 10, 20);
        g.drawString("Wave: " + wave, 10, 40);
        g.drawString("Ammo: " + ammo + "/" + maxAmmo, 10, 60);

        g.drawImage(gunHandFrames[gunHandFrameIndex], mouseX - gunHandFrames[gunHandFrameIndex].getWidth() / 2, mouseY - gunHandFrames[gunHandFrameIndex].
                getHeight() / 2, null);

        g.drawImage(crosshairImage, mouseX - crosshairImage.getWidth() / 2, mouseY - crosshairImage.getHeight() / 2, null);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (gameState == GameState.PLAYING) {
            drawGame(g);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (gameState == GameState.PLAYING) {
            updateGameState();
            repaint();
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (gameState == GameState.PLAYING) {
            mouseX = e.getX();
            mouseY = e.getY();
            repaint();
        }
    }
    @Override
    public void mousePressed(MouseEvent e) {
        if (gameState == GameState.PLAYING && ammo > 0) {
            shoot(mouseX, mouseY);
            isShooting = true;
            gunHandFrameIndex = 2;
            if (powerUpActive) {
                gunAnimationTask = new TimerTask() {
                    @Override
                    public void run() {
                        shoot(mouseX, mouseY);
                    }
                };
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (gameState == GameState.PLAYING && ammo > 0) {
            isShooting = false;
            gunHandFrameIndex = 0;
            if (powerUpActive) {
                gunAnimationTask.cancel();
            }
        }
    }

    private void shoot(int x, int y) {
        if (ammo > 0) {
            bullets.add(new Bullet(x, y));
            playSound(gunshotSound);
            ammo--;
        }
        if (ammo == 0) {
            reloadTimer = reloadTime;
        }
    }

    private void updateReload() {
        if (reloadTimer > 0) {
            reloadTimer -= 16;
            if (reloadTimer <= 0) {
                ammo = maxAmmo;
            }
        }
    }

    private void playSound(Clip sound) {
        if (sound.isRunning()) {
            sound.stop();
        }
        sound.setFramePosition(0);
        sound.start();
    }

    @Override
    public void mouseClicked(MouseEvent e) {}
    @Override
    public void mouseEntered(MouseEvent e) {}
    @Override
    public void mouseExited(MouseEvent e) {}
    @Override
    public void mouseDragged(MouseEvent e) {}

}
