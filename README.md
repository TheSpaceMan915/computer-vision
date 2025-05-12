# Computer Vision Project

This project is a modular, multi-assignment exploration of computer vision techniques using Java and OpenCV. Across six Maven-based modules, it demonstrates image filtering, transformation, segmentation, and object detection with full support for logging, testing, and visual debugging. Designed for learning and experimentation, each assignment builds on the last to explore advanced processing pipelines — from flipping and rotating images to detecting shapes using Canny edges and contour analysis.

## 📁 Project Structure

### `assignment1`

- Java-based Maven module
- Contains `src/main/java` and `src/test/java`
- Uses OpenCV for image processing tasks
- Logging via Log4j2

### `assignment2`

- Java-based Maven module
- Handles channel nullification and image configuration
- Includes environment-based configuration and logging

### `assignment3`

- Implements image concatenation, filtering, transformation
- Tests on real-world countryside and urban images
- Applies filters and edge detection (Sobel, Laplacian)

### `assignment4`

- Demonstrates morphological filtering (erosion/dilation)
- Evaluates kernels of various shapes and sizes
- Visualizes filter effects using original image variants

### `assignment5`

- Image segmentation and object detection
- Flood fill, rectangle detection, pyramid-based scaling
- Edge analysis with debug visual outputs

### `assignment6`

- Demonstrates object detection
- Applies Canny algorithm to detect object edges
- Saves each detection stage in a debug directory

## 🔧 Technologies Used

- Java 17+
- OpenCV (native bindings)
- JavaFX (optional for GUI/visuals)
- Apache Maven (build and dependency management)
- Log4j2 (logging)
- JUnit 5 (unit testing)

## 📸 Features Implemented

- ✅ Image Filtering (Gaussian, Median, Bilateral, Normalized Box)
- ✅ Image Transformations (flip, rotate, translate, resize, perspective)
- ✅ Image Segmentation (flood fill, thresholding, image pyramids)
- ✅ Object Detection (Canny, contour detection, rectangle validation)
- ✅ Utilities (ImageIO, configuration loading, logging infrastructure)

## 🧪 Test Structure

- Tests are located in `src/test/java` for each module
- Each service has a corresponding test class
- Test resources configured separately under `src/test/resources`
- Output and intermediate files saved under `processed/` or `rectangle-detection/` folders

## 📂 Image Assets

- `resources/images/original` — Raw input images
- `resources/images/processed` — Filtered/transformed outputs
- Intermediate files:
    - `1. grayscale.jpg`, `2. denoised.jpg`, `3. hist_equalized.jpg`...
    - Help debug each processing stage visually

## 🚀 How to Build & Run

```bash
# Navigate to desired assignment
cd assignment3

# Build
mvn clean install

# Run (example for a main class)
java -cp target/classes app.App