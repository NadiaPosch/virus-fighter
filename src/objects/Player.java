package objects;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import gui.Window;

/**
 * player with 5 lives who can fire shots in order to obliterate enemies and
 * score points
 */
public class Player extends GameObject {
    private static final int POINTS = 50;
    private static final int MAX_LIVES = 3;

    private int score;
    private int lives;
    private List<Shot> shots;
    private long lastShotFired;

    public Player(int x, int y) {
        super(x, y, 40, 60);
        score = 0;
        lives = MAX_LIVES;
        shots = new ArrayList<>();
        lastShotFired = 0;
    }

    @Override
    public void draw(Window window) {
        window.drawImageCentered("resources/images/ironman.png", getX(), getY());
    }

    public int getScore() {
        return score;
    }

    public void increaseScore() {
        score = score + POINTS;
    }

    public int getLives() {
        return lives;
    }

    public void reduceLife() {
        lives--;
    }

    public void reset() {
        shots = new ArrayList<>();
        lives = MAX_LIVES;
        score = 0;
    }

    public void reset(int x, int y) {
        shots = new ArrayList<>();
        setX(x);
        setY(y);
    }

    public boolean isObstructed(int dx, int dy, GameObject other) {
        return new Player(getX() + dx, getY() + dy).intersects(other);
    }

    public void shoot() {
        lastShotFired = System.currentTimeMillis();
        shots.add(new Shot(getX(), getY()));
    }

    public long getTimeBetweenShots() {
        return System.currentTimeMillis() - lastShotFired;
    }

    public List<Shot> getShotsList() {
        return shots;
    }

    public void updateShots(List<GameObject> objects, int width) {
        Collection<Shot> shotsToRemove = new ArrayList<>();

        for (Shot shot : shots) {
            if (shot.getRight() > width) {
                shotsToRemove.add(shot);
            }
            for (GameObject obj : objects) {
                if (shot.intersects(obj)) {
                    shotsToRemove.add(shot);
                }
            }
        }

        shots.removeAll(shotsToRemove);
    }

}