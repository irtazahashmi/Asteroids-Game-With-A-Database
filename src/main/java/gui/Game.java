package gui;

import game.AsteroidFactory;
import game.GameObject;
import game.Rocket;
import game.TimeHandler;
import gui.controllers.Main;
import gui.controllers.Welcome;
import java.awt.BorderLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.swing.JFrame;

/**
 * The main game class to run the application.
 */
@SuppressWarnings({"PMD.MissingSerialVersionUID", "PMD.BeanMembersShouldSerialize",
        "PMD.DataflowAnomalyAnalysis","Duplicates"})
public class Game extends JFrame implements KeyListener {

    /**
     * The GameScene instance.
     */
    private GameScene gameScene;

    /**
     * The TimeHandler instance.
     */
    private TimeHandler timeHandler;

    /**
     * The Random instance.
     */
    private Random randomInstance;

    /**
     * List of existing objects in the game.
     */
    public static List<GameObject> existingObjects;

    /**
     * List of non-existing objects in the game.
     */
    List<GameObject> nonExistingObjects;

    /**
     * The player instance.
     */
    public static Rocket playerRocket;

    /**
     * The death time.
     */
    private int deathTime;

    /**
     * Shows the level number when the player has completed
     * the previous level.
     */
    private int showLevelNumberTime;

    /**
     * The restart time.
     */
    public static int restartTime;

    /**
     * The player score.
     */
    private int playerScore;

    /**
     * The player lives.
     */
    private int playerLives;

    /**
     * Current level.
     */
    private int currentLevel;

    /**
     * Is the game over.
     */
    public static boolean isTheGameOver;

    /**
     * Does the game need to be restarted.
     */
    public static boolean haveToRestartGame;

    public void addScore(int score) {
        this.playerScore += score;
    }

    public void registerEntity(GameObject entity) {
        nonExistingObjects.add(entity);
    }

    public boolean checkGameOver() {
        return isTheGameOver;
    }

    public boolean isPlayerInvulnerable() {
        return (deathTime > GameLogic.TIME_OF_BEING_INVULNERABLE_AFTER_DEATH);
    }

    public boolean canDrawPlayer() {
        return (deathTime <= GameLogic.TIME_FOR_RESPAWNING);
    }

    public int getPlayerScore() {
        return playerScore;
    }

    public int getPlayerLives() {
        return playerLives;
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    public boolean isPaused() {
        return timeHandler.isPaused();
    }

    public boolean isShowingLevel() {
        return (showLevelNumberTime > 0);
    }

    public Random getRandomInstance() {
        return randomInstance;
    }

    public List<GameObject> getExistingObjects() {
        return existingObjects;
    }

    public Rocket getPlayerRocket() {
        return playerRocket;
    }

    /**
     * Main method to run the application.
     * @param args run application
     */
    public static void main(String[] args) {
        Main.main(args);
        if (Welcome.isLoginSuccessful) {
            Game game = new Game();
            game.startGame();
        }
    }

    /**
     * Game constructor.
     */
    public Game() {
        super("Asteroids Game by Group 59");
        setLayout(new BorderLayout());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        add(this.gameScene = new GameScene(this), BorderLayout.CENTER);
        setGameControls();
        GameLogic.playSoundEffect("src/main/java/game/media/backgroundMusic.wav");
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * Setting the game controls.
     */
    private void setGameControls() {
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                Game.this.keyReleased(e);
            }

            @Override
            public void keyPressed(KeyEvent e) {
                Game.this.keyPressed(e);
            }
        });
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W:
                playerRocket.setThrusting(false);
                break;
            case KeyEvent.VK_A:
                playerRocket.setRotateLeft(false);
                break;
            case KeyEvent.VK_D:
                playerRocket.setRotateRight(false);
                break;
            case KeyEvent.VK_SPACE:
                playerRocket.setFiring(false);
                break;
            default:
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {

        switch (e.getKeyCode()) {
            case KeyEvent.VK_W:
                if (!GameLogic.shouldRestartGame()) {
                    playerRocket.setThrusting(true);
                }
                break;
            case KeyEvent.VK_A:
                if (!GameLogic.shouldRestartGame()) {
                    playerRocket.setRotateLeft(true);
                }
                break;
            case KeyEvent.VK_D:
                if (!GameLogic.shouldRestartGame()) {
                    playerRocket.setRotateRight(true);
                }
                break;
            case KeyEvent.VK_SPACE:
                if (!GameLogic.shouldRestartGame()) {
                    playerRocket.setFiring(true);
                    GameLogic.playMissileSoundEffect("src/main/java/game/media/gunShot.wav");
                }
                break;
            case KeyEvent.VK_P:
                if (!GameLogic.shouldRestartGame()) {
                    pauseGame();
                }
                break;
            default:
                GameLogic.shouldRestartGame();
                break;
        }
    }


    /**
     * Starting the game.
     */
    public void startGame() {
        newGame();
        resetGame();
        this.timeHandler = new TimeHandler(GameLogic.FRAMES_PER_SECOND);
        threading();
    }

    /**
     * Adding threading for the game.
     */
    public void threading() {
        while (true) {
            timeHandler.update();
            for (int i = 0; i < 5 && timeHandler.isCycleGone(); i++) {
                updateGame();
            }
            gameScene.repaint();
            long startTime = System.nanoTime();
            long changeInTime = GameLogic.FRAME_TIME - (System.nanoTime() - startTime);
            if (changeInTime > 0) {
                try {
                    Thread.sleep(changeInTime / 1000000L, (int) changeInTime % 1000000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Initializing the new game.
     */
    public void newGame() {
        this.randomInstance = new Random();
        this.existingObjects = new CopyOnWriteArrayList<>();
        this.nonExistingObjects = new ArrayList<>();
        this.playerRocket = new Rocket();
    }

    /**
     * Updating the game.
     */
    private void updateGame() {
        existingObjects.addAll(nonExistingObjects);
        nonExistingObjects.clear();
        if (restartTime > 0) {
            this.restartTime--;
        }
        if (showLevelNumberTime > 0) {
            this.showLevelNumberTime--;
        }
        if (isTheGameOver && haveToRestartGame) {
            resetGame();
        }
        increaseLevelNumberIfRequired();
        handlePlayerDeath();
        if (showLevelNumberTime == 0) {
            buildGame();
        }
    }

    /**
     * Building the basics of game.
     */
    private void buildGame() {
        for (GameObject gameObject : existingObjects) {
            gameObject.updateGame(this);
        }
        for (int i = 0; i < existingObjects.size(); i++) {
            GameObject objectA = existingObjects.get(i);
            for (int j = i + 1; j < existingObjects.size(); j++) {
                GameObject objectB = existingObjects.get(j);
                if (i != j && objectA.didCollide(objectB) && ((!objectA.equals(playerRocket)
                        && !(objectB.equals(playerRocket)))
                        || deathTime <= GameLogic.TIME_OF_BEING_INVULNERABLE_AFTER_DEATH)) {
                    objectA.collisionHandler(this, objectB);
                    objectB.collisionHandler(this, objectA);
                }
            }
        }
        existingObjects.removeIf(GameObject::needsRemoval);
    }

    /**
     * Handle death if player has died.
     */
    private void handlePlayerDeath() {
        if (deathTime > 0) {
            this.deathTime--;
            switch (deathTime) {
                case GameLogic.TIME_FOR_RESPAWNING:
                    playerRocket.reset();
                    playerRocket.setFiringAllowed(false);
                    break;
                case GameLogic.TIME_OF_BEING_INVULNERABLE_AFTER_DEATH:
                    playerRocket.setFiringAllowed(true);
                    break;
                default:
            }
        }
    }

    /**
     * Increase the level if player has destroyed all asteroids.
     */
    private void increaseLevelNumberIfRequired() {
        if (!isTheGameOver && GameLogic.areAllAsteroidsDestroyed()) {
            this.currentLevel++;
            this.showLevelNumberTime = GameLogic.MAXIMUM_LEVEL;
            resetGameObjectList();
            playerRocket.reset();
            playerRocket.setFiringAllowed(true);
            for (int i = 0; i < currentLevel + GameLogic.INCREASE_ASTEROIDS_BY; i++) {
                registerEntity(AsteroidFactory.createAsteroid(null, null, randomInstance));
            }
        }
    }

    /**
     * Reset the game.
     */
    public void resetGame() {
        this.playerScore = 0;
        this.currentLevel = 0;
        this.playerLives = 3;
        this.deathTime = 0;
        this.isTheGameOver = false;
        this.haveToRestartGame = false;
        resetGameObjectList();
    }

    /**
     * Reset the objects in the game.
     */
    private void resetGameObjectList() {
        nonExistingObjects.clear();
        existingObjects.clear();
        existingObjects.add(playerRocket);
    }

    /**
     * Pausing the game.
     */
    public void pauseGame(){
        timeHandler.setPaused(!timeHandler.isPaused());
    }

    /**
     * The method reducing the lives of player and checking the game
     * is over or not.
     */
    public void playerDies() {
        this.playerLives--;
        if (playerLives == 0) {
            this.isTheGameOver = true;
            this.restartTime = GameLogic.RESET_TIME;
            this.deathTime = Integer.MAX_VALUE;
        } else {
            this.deathTime = GameLogic.TIME_AFTER_DEATH;
        }
        playerRocket.setFiringAllowed(false);
    }

    @Override
    public void keyTyped(KeyEvent e) {
        //DO NOTHING!
    }
}
