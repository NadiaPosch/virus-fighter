package objects;

import gui.Window;

/**
 * obstacle that resembles a white wall
 */
public class Obstacle extends GameObject {

    public Obstacle(int x, int y, int h) {
        super(x, y, 20, h);
    }

    @Override
    public void draw(Window window) {
        window.setColor(250, 250, 250);
        window.fillRect(getX() - (double) getWidth() / 2, getY() - (double) getHeight() / 2, getWidth(), getHeight());
    }
}
