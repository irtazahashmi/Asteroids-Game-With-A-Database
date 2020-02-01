package gui.controllers;

import database.JdbcDao;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

@SuppressWarnings({"PMD.BeanMembersShouldSerialize",
        "PMD.DataflowAnomalyAnalysis"})
public class Register extends Controller implements Initializable {

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    @FXML
    AnchorPane anchor;
    @FXML
    Label warning;
    @FXML
    Label warning2;
    @FXML
    Label warning3;
    @FXML
    TextField user;
    @FXML
    PasswordField pass;
    @FXML
    PasswordField confpass;

    @FXML
    Label controlsDisplay;

    @FXML
    Pane controls;
    @FXML
    Pane vol;
    @FXML
    Pane quit;
    @FXML
    Pane mid;
    @FXML
    Pane bot;

    /**
     * Initializes the register page.
     * @param location url for the file to be loaded.
     * @param resources required field for initialization.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //all these set the text of hidden warning upon opening this window.
        //I put them here so that we can alter them if we change the requirements.
        warning.setText("Password must be between 8-16"
                + "characters,\nand contain at least one number");
        warning.setOpacity(0.0);
        warning2.setText("Passwords don't match");
        warning2.setOpacity(0.0);
        warning3.setText("That username is unavailable");
        warning3.setOpacity(0.0);
        controlsDisplay.setText("W: Accelerate\nS: Decelerate\nA: Steer left\nD: Steer left\nSpacebar: Shoot lasers\nP: Pause");
        controlsDisplay.setOpacity(0.0);

        //this is a constructor for the
        //glowing at the bottom of the screen.
        int rad = 200;
        int offset = 100;


        //this method draws all the stars on the screen.
        Welcome.stars(anchor);


        //timeline tht makes the glowing increase over time
        //adds a movement/dynamic effect to the screen.
        Welcome.screenGlow(anchor, rad, offset);

        drawDropMenu(anchor, controls, vol, quit);


    }

    /**
     * Registering a new user with the given username.
     * and password, if password and username are valid.
     * @param e
     * @throws IOException input exception.
     * @throws SQLException exception caused by database error.
     */
    @FXML
    void register(MouseEvent e) throws IOException, SQLException {
        //checks if the strings are valid inputs and fulfill our requirements.
        if (validUsername() && validPassword()) {
            String username = user.getText();
            String password = pass.getText();

            JdbcDao jdbcDao = new JdbcDao();
            jdbcDao.insertUser(username,password);

            warning.setOpacity(0.0);
            warning3.setOpacity(0.0);
            warning2.setOpacity(0.0);


        }
    }

    /**
     * checks if the string inputted as a password actually contains a number char.
     * @return whether it contains a number.
     */
    private Boolean containsNum() {
        String p = pass.getText();
        if (p.contains("0") || p.contains("1") || p.contains("2")
                || p.contains("3") || p.contains("4") || p.contains("5")
                || p.contains("6") || p.contains("7") || p.contains("8")
                || p.contains("9")) {
            return true;
        }
        return false;
    }

    /**
     * checks is the inputted string is a valid password according to our requirements.
     * @return true if the input is valid.
     */
    private Boolean validPassword() {

        if (pass.getText().length() > 7 && confpass.getText().length() < 17) {
            if (containsNum()) {
                if (pass.getText().equals(confpass.getText())) {
                    return true;
                } else {
                    System.out.println("Passwords don't match");
                    //makes the warning visible.
                    warning2.setOpacity(1.0);
                    return false;
                }
            } else {
                System.out.println("No number");
                //makes the warning visible.
                warning.setOpacity(1.0);
                return false;
            }
        } else {
            System.out.println("Wrong length");
            //makes the warning visible.
            warning.setOpacity(1.0);
            return false;
        }
    }

    @FXML
    void combine(MouseEvent event) {
        Timeline tm = animate(false, mid, controls, bot, vol, quit);
        tm.play();
    }

    @FXML
    void fix(MouseEvent event) {
        controlsDisplay.setOpacity(0.0);
        Timeline tm = animate(true, mid, controls, bot, vol, quit);

        tm.play();
    }

    @FXML
    void displayControls(MouseEvent event) {
        display(controlsDisplay);
    }

    @FXML
    void mute(MouseEvent event) {
        setVolume(this.volume);
        muteSound(vol);
    }

    /**
     * checking if the username is valid according to our requirements.
     * @return true if the user is valid.
     */
    private Boolean validUsername() {
        String username = user.getText();
        JdbcDao jdbcDao = new JdbcDao();
        if (!jdbcDao.checkUsername(username)) {
            warning3.setOpacity(1.0);
            return false;
        }
        return true;
    }

    @FXML
    void goBack(MouseEvent mouseEvent) throws IOException{
        URL url = new File("src/main/java/gui/resources/welcome.fxml").toURI().toURL();
        Parent root = FXMLLoader.load(url);

        openWindow(root, mouseEvent);
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
}
