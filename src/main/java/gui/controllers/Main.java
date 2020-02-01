package gui.controllers;

import java.io.File;
import java.net.URL;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

@SuppressWarnings("Duplicates")
public class Main extends Application {

    /**
     * initializes the page.
     * @param stage the paramter which contains all the elements of th page.
     * @throws Exception
     */
    @Override
    public void start(Stage stage) throws Exception {

        //params for dimensions of window.
        int width = 800;
        int height = 600;

        Group group = new Group();


        URL url = new File("src/main/java/gui/resources/welcome.fxml").toURI().toURL();
        Parent root = FXMLLoader.load(url);

        group.getChildren().add(root);
        Scene scene = new Scene(group, width, height);

        stage.setScene(scene);

        //stage.getIcons().add(new Image("gui/resources/jet-plane.png"));
        stage.setTitle("Asteroids Game by Group 59");

        stage.show();
    }


    /**
     * Just to begin running the program to open the first JavaFX page.
     * @param args usual main argument.
     */
    public static void main(String[] args) {
        launch(args);
    }
}
