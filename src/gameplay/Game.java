package gameplay;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import gui.Window;
import objects.GameObject;
import objects.Obstacle;
import objects.Player;
import objects.Shot;
import objects.Virus;

/**
 * Responsible for handling game states, screen display and game objects
 */
public class Game {
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static final double CENTER_X = (double) WIDTH / 2;
    private static final double CENTER_Y = (double) HEIGHT / 2;
    private static final int BUFFER = 5;
    private static final int PLAYER_SPEED = 10;
    private static final Random RANDOM = new Random();

    private Window window;
    private Player player;
    private List<GameObject> gameObjects;
    private GameState state;

    public Game() {
        window = new Window("MainWindow", WIDTH, HEIGHT);
        player = new Player(50, HEIGHT / 2);
        gameObjects = new ArrayList<>();
        state = GameState.WELCOME;
    }

    public void startGame() {
        populateGame();
        window.open();

        while (window.isOpen()) {
            showScreen();
            window.refreshAndClear(20);
        }
    }

    void showScreen() {
        drawBackground();

        switch (state) {
            case WELCOME:
                if (window.isKeyPressed("enter")) {
                    state = GameState.PLAYING;
                }
                break;
            case PLAYING:
                if (player.getLives() == 0) {
                    state = GameState.GAMEOVER;
                    break;
                }
                drawGame();
                animate();
                updateGameObjects();
                break;
            case GAMEOVER:
                if (window.isKeyPressed("enter")) {
                    state = GameState.PLAYING;
                    player = new Player(50, HEIGHT / 2);
                    reset();
                }
                break;
        }
    }

    void drawBackground() {
        window.setColor(254, 79, 96);
        window.setBold(true);
        window.setFontSize(32);

        switch (state) {
            case WELCOME:
                window.drawImageCentered("resources/images/welcome.png", CENTER_X, CENTER_Y);
                window.drawStringCentered("Press enter to start", CENTER_X, (double) HEIGHT - 80);
                break;
            case PLAYING:
                window.drawImageCentered("resources/images/background.png", CENTER_X, CENTER_Y);
                window.drawStringCentered("Score: " + player.getScore() + "  |  Lives: " + player.getLives(), CENTER_X,
                        50);
                break;
            case GAMEOVER:
                window.drawImageCentered("resources/images/gameover.png", CENTER_X, CENTER_Y);
                window.drawStringCentered("Press enter to restart", CENTER_X, (double) HEIGHT - 80);
        }
    }

    void populateGame() {
        int xCoord = WIDTH / 2;

        for (int i = 0; i < 6; i++) {
            addRandomObstacle(xCoord);
            xCoord = xCoord + player.getWidth() * 2 + RANDOM.nextInt(100);
        }
    }

    void addRandomObstacle(int x) {
        int h = RANDOM.nextInt(HEIGHT - player.getHeight() * 2);
        boolean isTop = RANDOM.nextInt(2) == 0;
        int y = isTop ? h / 2 : HEIGHT - h / 2;

        gameObjects.add(new Obstacle(x, y, h));

        if (RANDOM.nextInt(4) < 3) {
            gameObjects.add(new Virus(x, isTop ? h + 50 : HEIGHT - (h + 50)));
        }
    }

    void drawGame() {
        player.draw(window);

        for (GameObject obj : gameObjects) {
            obj.draw(window);
        }

        for (Shot shot : player.getShotsList()) {
            shot.draw(window);
        }
    }

    void animate() {
        for (GameObject obj : gameObjects) {
            obj.move(-1, 0);

            if (obj instanceof Obstacle && player.intersects(obj)) {
                player.move(-1, 0);

                if (player.getLeft() < 0) {
                    Sound.playSound(Sound.SQUASH);
                    player.reduceLife();
                    reset();
                }
            }
        }

        for (Shot shot : player.getShotsList()) {
            shot.move(10, 0);
        }

        movePlayer();
    }

    void movePlayer() {
        if (window.isKeyPressed("up") && player.getTop() > BUFFER && !isObstructed(0, -PLAYER_SPEED)) {
            player.move(0, -PLAYER_SPEED);
        }

        if (window.isKeyPressed("down") && player.getBottom() < (HEIGHT + BUFFER) && !isObstructed(0, PLAYER_SPEED)) {
            player.move(0, PLAYER_SPEED);
        }

        if (window.isKeyPressed("left") && player.getLeft() > BUFFER && !isObstructed(-PLAYER_SPEED, 0)) {
            player.move(-PLAYER_SPEED, 0);
        }

        if (window.isKeyPressed("right") && player.getRight() < (WIDTH - BUFFER) && !isObstructed(PLAYER_SPEED, 0)) {
            player.move(PLAYER_SPEED, 0);
        }

        if (window.isKeyPressed("space") && player.getTimeBetweenShots() > 200) {
            player.shoot();
            Sound.playSound(Sound.LASER);
        }
    }

    boolean isObstructed(int dx, int dy) {
        for (GameObject obj : gameObjects) {
            if (obj instanceof Obstacle && player.isObstructed(dx, dy, obj)) {
                return true;
            }
        }

        return false;
    }

    void updateGameObjects() {
        Collection<GameObject> objectsToRemove = new ArrayList<>();
        updateRandomObstacles(objectsToRemove);
        updateViruses(objectsToRemove);
        player.updateShots(gameObjects, WIDTH);
        gameObjects.removeAll(objectsToRemove);
    }

    void updateRandomObstacles(Collection<GameObject> objectsToRemove) {
        GameObject firstObject = gameObjects.get(0);
        GameObject secondObject = gameObjects.get(1);

        if (firstObject instanceof Obstacle && firstObject.getLeft() < 0) {
            objectsToRemove.add(firstObject);
            if (secondObject instanceof Virus) {
                objectsToRemove.add(secondObject);
            }
            addRandomObstacle(WIDTH);
        }
    }

    void updateViruses(Collection<GameObject> objectsToRemove) {
        for (GameObject obj : gameObjects) {
            if (obj instanceof Virus) {
                if (player.intersects(obj)) {
                    Sound.playSound(Sound.GRUNT);
                    objectsToRemove.add(obj);
                    player.reduceLife();
                }
                for (Shot shot : player.getShotsList()) {
                    if (shot.intersects(obj)) {
                        player.increaseScore();
                        objectsToRemove.add(obj);
                    }
                }
            }
        }
    }

    void reset() {
        gameObjects = new ArrayList<>();
        player.reset(50, HEIGHT / 2);
        populateGame();
    }
}
