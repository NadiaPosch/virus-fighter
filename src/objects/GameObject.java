package objects;

import gui.Window;

/**
 * abstract superclass for all game objects that handles position, movement and
 * intersection between objects; display (e.g. draw() method) needs to be
 * defined by subclass
 */
public abstract class GameObject {
    private int x;
    private int y;
    private int width;
    private int height;

    protected GameObject(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public abstract void draw(Window window);

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getTop() {
        return y - height / 2;
    }

    public int getRight() {
        return x + width / 2;
    }

    public int getBottom() {
        return y + height / 2;
    }

    public int getLeft() {
        return x - width / 2;
    }

    public void move(int dx, int dy) {
        x += dx;
        y += dy;
    }

    public boolean intersects(GameObject other) {
        return x - width / 2 < other.x + other.width / 2 && x + width / 2 > other.x - other.width / 2
                && y - height / 2 < other.y + other.height / 2 && y + height / 2 > other.y - other.height / 2;
    }

}
