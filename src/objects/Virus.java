package objects;

import gui.Window;

/**
 * virus that reduces a player's lives in case of contact and gets obliterated
 * if hit by a shot
 */
public class Virus extends GameObject {

    public Virus(int x, int y) {
        super(x, y, 50, 50);
    }

    @Override
    public void draw(Window window) {
        window.drawImageCentered("resources/images/redVirus.png", getX(), getY());
    }

}
