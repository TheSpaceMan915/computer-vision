package app;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

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
        Path origImagePath = Paths.get(config.getProperty(Constants.IMAGE_DIR_PATH) + config.getProperty(Constants.ORIG_IMAGE_NAME));

//        TODO: Check if it is a good way to convert the path
        Image image = new Image(origImagePath.toUri().toString());
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(600);
        imageView.setPreserveRatio(true);

//        Create a container for the images
        HBox hbox = new HBox();
        hbox.getChildren().add(imageView);

//        Create the main pain for the scene
        BorderPane root = new BorderPane();
        root.setCenter(hbox);
        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
