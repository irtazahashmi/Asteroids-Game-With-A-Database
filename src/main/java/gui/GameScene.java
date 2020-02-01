package gui;

import database.JdbcDao;
import game.GameObject;
import game.Vector;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

/**
 * The GameScene class that holds the Game instance.
 */
@SuppressWarnings({"PMD.DataflowAnomalyAnalysis", "PMD.MissingSerialVersionUID",
        "PMD.BeanMembersShouldSerialize", "PMD.AvoidLiteralsInIfCondition"})
public class GameScene extends JPanel {

    /**
     * The size of the game screen.
     */
    public static final int SCREEN_SIZE = 700;

    public static String nickname;
    /**
     * The game instance.
     */
    private Game game;

    /**
     * The constructor method for GameScene.
     * @param game current
     */
    public GameScene(Game game) {
        this.game = game;
        Dimension dimension = new Dimension(SCREEN_SIZE, SCREEN_SIZE);
        setPreferredSize(dimension);
    }

    /**
     * Paint the game.
     * @param graphics current graphics.
     */
    @Override
    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D graphics2D = (Graphics2D) graphics;
        init(graphics2D);
        setBackgroundImage(graphics2D);
        AffineTransform identity = graphics2D.getTransform();
        drawObjects(graphics2D, identity);
        showGameScore(graphics);
        showGameInformation(graphics2D);
        drawLives(graphics2D);
    }

    /**
     * Initialize the game.
     * @param graphics2D the current graphics.
     */
    private void init(Graphics2D graphics2D) {
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        graphics2D.setColor(Color.WHITE);
    }

    /**
     * Set the background image for the game.
     * @param graphics2D the current graphics.
     */
    private void setBackgroundImage(Graphics2D graphics2D) {
        BufferedImage backgroundImage = null;
        try {
            backgroundImage = ImageIO.read(
                    new File("src/main/java/game/media/backgroundImage.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        graphics2D.drawImage(backgroundImage, 0, 0, this);
    }

    /**
     * Show the game score.
     * @param graphics the current graphics.
     */
    private void showGameScore(Graphics graphics) {
        if (!game.checkGameOver()) {
            graphics.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 20));
            graphics.drawString("Player Score: " + game.getPlayerScore(), 10, 30);
        }
    }

    /**
     * Show the game stats.
     * @param graphics2D the current graphics.
     */
    private void showGameInformation(Graphics2D graphics2D) {
       if(game.checkGameOver()){
           JdbcDao jdbcDao = new JdbcDao();
           jdbcDao.insertHighScore(this.nickname, game.getPlayerScore());

       }
        if (game.checkGameOver()) {

            drawText("Game Over!", new Font(Font.DIALOG, Font.ITALIC, 45),
                    graphics2D, -25);

            drawText("Final Score: " + game.getPlayerScore(),
                    new Font(Font.MONOSPACED, Font.PLAIN, 45), graphics2D, 30);

            drawText("Tap the spacebar to restart",
                    new Font(Font.MONOSPACED, Font.PLAIN, 20), graphics2D, 65);
        } else if (game.isPaused()) {
            drawText("Game Paused",
                    new Font(Font.MONOSPACED, Font.ITALIC, 45), graphics2D, -25);
        } else if (game.isShowingLevel()) {
            drawText("Level " + game.getCurrentLevel(),
                    new Font(Font.MONOSPACED, Font.PLAIN, 45), graphics2D, -25);
        }

        graphics2D.translate(15, 30);
        graphics2D.scale(0.85, 0.85);
    }

    /**
     * Draw the number of lives.
     * @param graphics2D the current graphics.
     */
    private void drawLives(Graphics2D graphics2D) {
        for (int i = 0; i < game.getPlayerLives(); i++) {
            BufferedImage loadingImage = null;
            try {
                loadingImage = ImageIO.read(new File("src/main/java/game/media/rocket.png"));
            } catch (IOException e) {
                e.printStackTrace();
            }

            Image rocket = loadingImage.getScaledInstance(40, 40, Image.SCALE_SMOOTH);
            graphics2D.drawImage(rocket, 5, 10, this);
            graphics2D.translate(30, 0);
        }
    }

    /**
     * Draw text.
     * @param graphics2D the current graphics.
     */
    private void drawText(String text, Font font, Graphics2D graphics2D, int y) {
        graphics2D.setFont(font);
        graphics2D.drawString(text, SCREEN_SIZE / 2 - graphics2D.getFontMetrics()
                .stringWidth(text) / 2, SCREEN_SIZE / 2 + y);
    }

    /**
     * Rotating and drawing game Objects.
     * @param graphics2D the current graphics.
     */
    private void rotateObjects(Graphics2D graphics2D, GameObject gameObject,
                               double positionX, double positionY) {

        graphics2D.translate(positionX, positionY);
        double rotation = gameObject.getObjectRotation();
        if (rotation != 0.0f) {
            graphics2D.rotate(gameObject.getObjectRotation());
        }
        gameObject.drawObject(graphics2D, game);
    }

    /**
     * Drawing the game objects to the game scene.
     * @param graphics2D he current graphics.
     * @param identity the affine transformation.
     */
    private void drawObjects(Graphics2D graphics2D, AffineTransform identity) {
        game.getExistingObjects().stream().filter(gameObject -> gameObject != game.getPlayerRocket() || game.canDrawPlayer()).forEach(gameObject -> {
            Vector gameObjectPosition = gameObject.getObjectPosition();
            rotateObjects(graphics2D, gameObject, gameObjectPosition.axis1,
                    gameObjectPosition.axis2);
            graphics2D.setTransform(identity);
            double radius = gameObject.getObjectRadius();
            double positionX = updatePositionX(gameObjectPosition, radius);
            double positionY = updatePositionY(gameObjectPosition, radius);
            if (positionX != gameObjectPosition.axis1
                    || positionY != gameObjectPosition.axis2) {
                rotateObjects(graphics2D, gameObject, positionX, positionY);
                graphics2D.setTransform(identity);
            }
        });
    }

    /**
     * Updating the position X.
     * @param gameObjectPosition the position of object
     * @param radius of the game object
     * @return
     */
    private double updatePositionX(Vector gameObjectPosition, double radius) {
        if (gameObjectPosition.axis1 < radius) {
            return gameObjectPosition.axis1 + SCREEN_SIZE;
        } else if (gameObjectPosition.axis1 > SCREEN_SIZE - radius) {
            return gameObjectPosition.axis1 - SCREEN_SIZE;
        } else {
            return gameObjectPosition.axis1;
        }
    }

    /**
     * Updating the position Y.
     * @param gameObjectPosition the position of object
     * @param radius of the game object
     * @return
     */
    private double updatePositionY(Vector gameObjectPosition, double radius) {
        if (gameObjectPosition.axis2 < radius) {
            return gameObjectPosition.axis2 + SCREEN_SIZE;
        } else if (gameObjectPosition.axis2 > SCREEN_SIZE - radius) {
            return gameObjectPosition.axis2 - SCREEN_SIZE;
        } else {
            return gameObjectPosition.axis2;
        }
    }

}
