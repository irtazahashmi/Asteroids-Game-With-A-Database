package gui.controllers;

import gui.GameScene;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

@SuppressWarnings({"PMD.BeanMembersShouldSerialize", "PMD.AvoidLiteralsInIfCondition"})
public class Homepage extends Controller implements Initializable {


    @FXML
    public TextField nickname;

    @FXML
    AnchorPane anchor;

    @FXML
    Pane drop, top, mid, bot;

    @FXML
    Pane controls, vol, quit;

    @FXML
    Label controlsDisplay;

    public static boolean click;


    public void setNickname(){
            GameScene.nickname = this.nickname.getText();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
       drawDropMenu(anchor, controls, vol, quit);

        controlsDisplay.setText("W: Accelerate\nS: Decelerate\nA: Steer left\nD: Steer left\nSpacebar: Shoot lasers\nP: Pause");
        controlsDisplay.setOpacity(0.0);
    }

    @FXML
    void lightUp(MouseEvent event) {

        Button btn = (Button) event.getSource();
        lightBtn(btn);
    }

    @FXML
    void darken(MouseEvent event) {

        Button btn = (Button) event.getSource();
        darkenBtn(btn);

    }

    @FXML
    void combine(MouseEvent event) {
        Timeline tm = animate(false, mid, controls, bot, vol, quit);
        tm.play();
    }

    @FXML
    void displayControls(MouseEvent event) {
        display(controlsDisplay);
    }

    @FXML
    void fix(MouseEvent event) {
        Timeline tm = animate(true, mid, controls, bot, vol, quit);
        tm.play();
    }

    @FXML
    void mute(MouseEvent event){
        muteSound(vol);
    }


    public void clickStart(MouseEvent mouseEvent) {
        click = true;
        setNickname();
        Platform.exit();
    }

    public void clickHighScore(MouseEvent mouseEvent) throws SQLException {
        HighScoreBoard.getBoard();
    }

    public TextField getNickname() {
        return nickname;
    }

    public void setNickname(TextField nickname) {
        this.nickname = nickname;
    }
}
