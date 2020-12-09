package objects;

import gui.Window;

/**
 * laser shot that can be fired by a player in order to obliterate enemies
 */
public class Shot extends GameObject {

    public Shot(int x, int y) {
        super(x, y, 30, 10);
    }

    @Override
    public void draw(Window window) {
        window.drawImageCentered("resources/images/laser.png", getX(), getY());
    }

}
