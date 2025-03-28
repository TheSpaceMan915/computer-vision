package app;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;

/*
* A JavaFX app used to display images.
*/
public class App extends Application {

    private final OpenCVLoader loader = new OpenCVLoader();

    private final Config config = new Config();

    private final ImageService imageService = new ImageService();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
//        Create image viewers
        Path origImagePath = Paths.get(config.getProperty(Constants.IMAGE_DIR_PATH), config.getProperty(Constants.ORIG_IMAGE_NAME));
        URI origImageURI = origImagePath.toUri();

        Image origImage = new Image(origImageURI.toString());
        ImageView origImageView = new ImageView(origImage);
        origImageView.setFitWidth(600);
        origImageView.setPreserveRatio(true);

        Image processedImage = new Image(origImageURI.toString());
        ImageView processedImageView = new ImageView(processedImage);
        processedImageView.setFitWidth(600);
        processedImageView.setPreserveRatio(true);

//        Create a container for the images
        HBox hbox = new HBox();
        hbox.getChildren().add(origImageView);
        hbox.getChildren().add(processedImageView);

//        Create the main pain for the scene
        BorderPane root = new BorderPane();
        root.setCenter(hbox);
        Scene scene = new Scene(root, 1200, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("JavaFX App");
        primaryStage.show();
    }
}
