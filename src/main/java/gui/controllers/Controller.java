package gui.controllers;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.Bloom;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static javafx.scene.paint.Color.WHITE;


@SuppressWarnings({
        "PMD.DataflowAnomalyAnalysis", "PMD.BeanMembersShouldSerialize", "PMD.AvoidLiteralsInIfCondition"})

public abstract class Controller {

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    int volume = 1;

    void openWindow(Parent root, MouseEvent mouseEvent) throws IOException {

        Scene scene = new Scene(root, 800, 600);
        Stage window = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();

        window.setScene(scene);

        window.setScene(scene);
        window.show();
    }

    protected static void screenGlow(AnchorPane anchor, int rad, int offset) {
        Circle c = new Circle(anchor.getPrefWidth() / 2, anchor.getPrefHeight() + rad, rad);
        c.setFill(WHITE);
        //adding a gaussian blur to make it look like a star.
        //this part is seen on screen.
        c.setEffect(new GaussianBlur(50 + offset));

        //drawing the circle of white below the viewable screen
        c.setOpacity(0);
        anchor.getChildren().add(c);

        Timeline tm = new Timeline();

        tm.getKeyFrames().addAll(
                new KeyFrame(Duration.seconds(5),
                        new KeyValue(c.opacityProperty(), 1),
                        new KeyValue(c.scaleXProperty(), 3),
                        new KeyValue(c.translateYProperty(), -20)));



        tm.play();
    }

    public void display(Label label){
        label.setOpacity(1.0);
    }

    /**.
     * constructs a star (white circle).
     * @param anc the pane theyll be drawn in.
     * @param width the x pos on screen.
     * @param height the y pos on screen.
     * @param r the radius of the star.
     * @param n the number of stars to be drawn.
     * @param circles the array list it'll be added to.
     */
    private static void drawCircle(AnchorPane anc, int width,
                                   int height, double r, int n, ArrayList<Circle> circles) {

        for (int i = 0; i < n; i++) {

            double xx = Math.random() * width;
            double yy = Math.random() * height;

            double rr = Math.random();

            Circle c = new Circle(xx, yy, r + rr);
            c.setFill(WHITE);

            GaussianBlur gaussianBlur = new GaussianBlur();
            gaussianBlur.setRadius(r * 5);
            c.setEffect(gaussianBlur);

            circles.add(c);
            anc.getChildren().add(c);

        }

    }

    /**
     * This method draws the stars in the opening screen.
     */
    public static void stars(AnchorPane anchor) {

        ArrayList<Circle> fore = new ArrayList<>();
        ArrayList<Circle> mid = new ArrayList<>();
        ArrayList<Circle> back = new ArrayList<>();

        drawCircle(anchor, (int)anchor.getPrefWidth(), (int)anchor.getPrefHeight() * 10,
                1, 50, fore);
        drawCircle(anchor, (int)anchor.getPrefWidth(), (int)anchor.getPrefHeight() * 10,
                0.5, 50 * 5, mid);
        drawCircle(anchor, (int)anchor.getPrefWidth(), (int)anchor.getPrefHeight() * 10,
                0.0, 50 * 10, back);

        for (int i = 0; i < fore.size(); i++) {
            Timeline tm = new Timeline();

            tm.getKeyFrames().addAll(
                    new KeyFrame(Duration.seconds(90),
                            new KeyValue(fore.get(i).translateYProperty(), -1000)));
            tm.play();
        }
        for (int i = 0; i < mid.size(); i++) {
            Timeline tm = new Timeline();

            tm.getKeyFrames().addAll(
                    new KeyFrame(Duration.seconds(160),
                            new KeyValue(mid.get(i).translateYProperty(), -1000)));
            tm.play();
        }
        for (int i = 0; i < back.size(); i++) {
            Timeline tm = new Timeline();

            tm.getKeyFrames().addAll(
                    new KeyFrame(Duration.seconds(450),
                            new KeyValue(back.get(i).translateYProperty(), -1000)));
            tm.play();
        }
    }

    /**
     * animating the drop menu.
     * @param anchor the page containing the menu.
     * @param controls the button for the controls.
     * @param vol the button to mute or unmute.
     * @param quit the button to go back.
     */
    static void drawDropMenu(AnchorPane anchor, Pane controls, Pane vol, Pane quit){
        File c = new File("src/main/java/gui/resources/gamepad.png");
        BackgroundImage controller = new BackgroundImage(new Image(c.toURI().toString(),19,23,false,true),
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);
        controls.setBackground(new Background(controller));

        File v = new File("src/main/java/gui/resources/volume-on.png");
        BackgroundImage volume = new BackgroundImage(new Image(v.toURI().toString(),18,15,true,true),
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);
        vol.setBackground(new Background(volume));

        File d = new File("src/main/java/gui/resources/delete.png");
        BackgroundImage x = new BackgroundImage(new Image(d.toURI().toString(),15,15,true,true),
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);
        quit.setBackground(new Background(x));

        Rectangle rect = new Rectangle(50, 40);
        rect.setLayoutX(10);
        rect.setLayoutY(-30);
        anchor.getChildren().addAll(rect);
    }

    /**
     * Method to mute the sound.
     * @param vol
     */
    public void muteSound(Pane vol){
        if(this.volume == 1) {
            File file = new File("src/main/java/gui/resources/volume-off.png");
            BackgroundImage volume = new BackgroundImage(new Image(file.toURI().toString(), 18, 15, true, true),
                    BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                    BackgroundSize.DEFAULT);
            vol.setBackground(new Background(volume));
            this.volume = 0;
        } else {
            File file = new File("src/main/java/gui/resources/volume-on.png");
            BackgroundImage volume = new BackgroundImage(new Image(file.toURI().toString(), 18, 15, true, true),
                    BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                    BackgroundSize.DEFAULT);
            vol.setBackground(new Background(volume));
            this.volume = 1;
        }
    }

    /**
     * animating the timeline.
     * @param zero
     * @param mid
     * @param controls
     * @param bot
     * @param vol
     * @param quit
     * @return
     */
    Timeline animate(boolean zero, Pane mid, Pane controls, Pane bot, Pane vol, Pane quit) {
        int a = -10;
        int b = -20;
        int c = 30;
        int d = 55;
        int e = 80;

        if (zero) {
            a = 0;
            b = 0;
            c = 0;
            d = 0;
            e = 0;
        }

        Timeline tm = new Timeline();

        tm.getKeyFrames().addAll(
                new KeyFrame(Duration.seconds(0.15),
                        new KeyValue(mid.translateYProperty(), a),
                        new KeyValue(bot.translateYProperty(), b),
                        new KeyValue(controls.translateYProperty(), c),
                        new KeyValue(vol.translateYProperty(), d),
                        new KeyValue(quit.translateYProperty(), e)));

        return tm;
    }


    public void lightBtn(Button btn) {

        Glow glow = new Glow();
        Bloom bloom = new Bloom();

        glow.setInput(bloom);
        glow.setLevel(1);

        btn.setEffect(glow);
    }


    public void darkenBtn(Button btn) {

        Glow glow = new Glow();
        Bloom bloom = new Bloom();

        glow.setInput(bloom);
        glow.setLevel(0);

        btn.setEffect(glow);
    }
}
