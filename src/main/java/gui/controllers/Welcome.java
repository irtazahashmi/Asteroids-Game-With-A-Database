package gui.controllers;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import database.JdbcDao;

/**
 * The welcome page for our game.
 */
@SuppressWarnings({"Duplicates","PMD.AssignmentInOperand", "PMD.BeanMembersShouldSerialize"})
public class Welcome extends Controller implements Initializable {

    //the background pane that contains everything.
    @FXML
    AnchorPane anchor;

    @FXML
    TextField user;
    @FXML
    PasswordField pass;

    @FXML
    Button log;
    @FXML
    Button reg;

    /**
     * Checks whether login was successful.
     */
    public static boolean isLoginSuccessful = false;

    /**
     * Initializes the welcome page.
     * @param location url for the file to be loaded
     * @param resources required field for initialization
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //this is a constructor for the
        //glowing at the bottom of the screen.
        int rad = 200;
        int offset = 100;


        //this method draws all the stars on the screen.
        stars(anchor);


        //timeline tht makes the glowing increase over time
        //adds a movement/dynamic effect to the screen.
        screenGlow(anchor, rad, offset);

    }



    /**
     * this is the hovering effect method.
     * scales up the button.
     * repostiions them to maintain order on the page.
     * (no overlapping of elements).
     * @param scalar the amount of scaling to be done.
     * @param duration the length of the animation.
     * @param transX the x translation.
     * @param transY the y translation.
     * @param btn the button the animation should be applied to.
     */
    @FXML
    void scale(double scalar, double duration, double transX, double transY, Button btn) {

        Timeline tm = new Timeline();

        tm.getKeyFrames().addAll(
                new KeyFrame(Duration.seconds(duration),
                        new KeyValue(btn.scaleXProperty(), scalar),
                        new KeyValue(btn.scaleYProperty(), scalar),
                        new KeyValue(btn.translateXProperty(), transX),
                        new KeyValue(btn.translateYProperty(), transY)));

        tm.play();
    }

    /**
     * remove the added hovering effect when.
     * the user hovers their mouse over the login button.
     * @param mouseEvent the users mouse cursor location.
     */
    @FXML
    void enterLog(MouseEvent mouseEvent) {
        scale(1.2, 0.1, -17, 3.5, log);
    }

    /**
     * remove the added hovering effect when.
     * the user hovers their mouse over the login button.
     * @param mouseEvent the users mouse cursor location.
     */
    @FXML
    void exitLog(MouseEvent mouseEvent) {
        scale(1, 0.06, 0, 0, log);
    }

    /**
     * add hovering effect when.
     * the user hovers their mouse over the registration button.
     * @param mouseEvent the users mouse cursor location.
     */
    @FXML
    void enterReg(MouseEvent mouseEvent) {
        scale(1.2, 0.1, 17, 3.5, reg);
    }

    /**
     * remove the added hovering effect when.
     * the user hovers their mouse over the registration button.
     * @param mouseEvent the users mouse cursor location.
     */
    @FXML
    void exitReg(MouseEvent mouseEvent) {
        scale(1, 0.06, 0, 0, reg);
    }

    /**
     * to retrieve the login info from the input fields.
     */
    @FXML
    void sendLogin() {
        String name = user.getText();
        String password = pass.getText();

        System.out.println(name + password);
    }

    /**
     * when the user clicks the register button.
     * it opens the registration page for them.
     * @param mouseEvent the users click.
     * @throws IOException required input exception.
     */
    @FXML
    void openReg(MouseEvent mouseEvent) throws IOException {
        //path to registration page fxml page.
        URL url = new File("src/main/java/gui/resources/register.fxml").toURI().toURL();
        Parent root = FXMLLoader.load(url);

        openWindow(root, mouseEvent);

    }

    /**
     * when the login is clicked.
     * checks validity of inputs and opens new page.
     * @param mouseEvent the users click.
     */
    @FXML
    void clickLog(MouseEvent mouseEvent) throws IOException {
        Stage stage = (Stage) log.getScene().getWindow();
        stage.close();

        String username = user.getText();
        String password = pass.getText();

        JdbcDao jdbcDao = new JdbcDao();

        if(jdbcDao.login(username, password, isLoginSuccessful)) {
            URL url = new File("src/main/java/gui/resources/homepage.fxml").toURI().toURL();
            Parent root = FXMLLoader.load(url);

            openWindow(root, mouseEvent);
        }
    }
}
