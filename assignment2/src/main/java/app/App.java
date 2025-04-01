package app;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

import org.opencv.core.Mat;

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
//        Create an image viewer
        Path origImagePath = Paths.get(config.getProperty(Constants.IMAGE_DIR_PATH), config.getProperty(Constants.ORIG_IMAGE_NAME));
        URI origImageURI = origImagePath.toUri();
        Image origImage = new Image(origImageURI.toString());
        ImageView origImageView = new ImageView(origImage);
        origImageView.setFitWidth(300);
        origImageView.setPreserveRatio(true);

//        Create a container for the images
        HBox imageContainer = new HBox();
        imageContainer.getChildren().add(origImageView);

//        Create a button and make it expand equally
        Button startButton = new Button("Start");
        HBox.setHgrow(startButton, Priority.ALWAYS);
        startButton.setMaxWidth(Double.MAX_VALUE);

//        Run the image processing
        startButton.setOnAction(event -> {
            Mat image = imageService.readImage(origImagePath.toString());
            Mat nullifiedImage = imageService.nullifyChannel(image, Channel.RED);
            Path nullifiedImagePath = Paths.get(config.getProperty(Constants.IMAGE_DIR_PATH), config.getProperty(Constants.PROCESSED_IMAGE_NAME));
            boolean isSaved = imageService.writeImage(nullifiedImage, nullifiedImagePath.toString());
            if (isSaved) {
//                Show the processed image
                URI nullifiedImageURI = nullifiedImagePath.toUri();
                Image processedImage = new Image(nullifiedImageURI.toString());
                ImageView processedImageView = new ImageView(processedImage);
                processedImageView.setFitWidth(300);
                processedImageView.setPreserveRatio(true);
                imageContainer.getChildren().add(processedImageView);

//                Show an alert
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Image Saved");
                alert.setContentText("The image was successfully saved at " + nullifiedImagePath);
                alert.showAndWait();
            }
        });
//        Create a container for the button
        HBox buttonContainer = new HBox();
        buttonContainer.getChildren().add(startButton);

//        Create the main pain for the scene
        BorderPane root = new BorderPane();
        root.setCenter(imageContainer);
        root.setBottom(buttonContainer);
        Scene scene = new Scene(root, 600, 430);
        primaryStage.setScene(scene);
        primaryStage.setTitle("JavaFX App");
        primaryStage.show();
    }
}
