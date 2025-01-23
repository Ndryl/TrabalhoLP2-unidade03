package com.example.MongoSpring.Enity;

import java.io.InputStream;
import java.util.Base64;
import java.util.List;

import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.objdetect.Objdetect;
import org.opencv.videoio.VideoCapture;

import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import java.io.ByteArrayInputStream;
import javafx.stage.*;
import nu.pattern.OpenCV;

public class ImageUtils {

    // Método para carregar uma imagem do disco
    public static Mat loadImage(String imagePath) {
        return Imgcodecs.imread(imagePath);
    }

    // Método para salvar uma imagem no disco
    public static void saveImage(Mat imageMatrix, String targetPath) {
        Imgcodecs.imwrite(targetPath, imageMatrix);
    }

    // Converte um Mat (OpenCV) para um array de bytes
    public static byte[] matToBytes(Mat mat) {
        MatOfByte buffer = new MatOfByte();
        Imgcodecs.imencode(".jpg", mat, buffer); // Use uma extensão válida
        return buffer.toArray();
    }

    // Método para detectar rostos em uma imagem
    public static Mat detectFace(Mat loadedImage) {
        if (loadedImage.empty()) {
            throw new IllegalArgumentException("Imagem não foi carregada corretamente.");
        }

        CascadeClassifier cascadeClassifier = new CascadeClassifier();
        String cascadePath = ImageUtils.class.getClassLoader()
                .getResource("haarcascades/haarcascade_frontalface_alt.xml")
                .getPath();

        if (!cascadeClassifier.load(cascadePath)) {
            throw new RuntimeException("Falha ao carregar o classificador de faces em: " + cascadePath);
        }

        MatOfRect facesDetected = new MatOfRect();
        int minFaceSize = Math.round(loadedImage.rows() * 0.1f);

        cascadeClassifier.detectMultiScale(
                loadedImage,
                facesDetected,
                1.1,
                3,
                Objdetect.CASCADE_SCALE_IMAGE,
                new Size(minFaceSize, minFaceSize),
                new Size()
        );

        for (Rect face : facesDetected.toList()) {
            Imgproc.rectangle(
                    loadedImage,
                    new Point(face.x, face.y),
                    new Point(face.x + face.width, face.y + face.height),
                    new Scalar(0, 255, 0),
                    2
            );
        }

        return loadedImage;
    }
    public static byte[] base64ToBytes(String base64Image) {
        String base64Data = base64Image.split(",")[1]; // Remove o cabeçalho "data:image/jpeg;base64,"
        return Base64.getDecoder().decode(base64Data);
    }

    // Converte Mat para Image (necessário para exibir no JavaFX)
    public static Image mat2Img(Mat mat) {
        MatOfByte buffer = new MatOfByte();
        Imgcodecs.imencode(".jpg", mat, buffer); // Extensão válida
        return new Image(new ByteArrayInputStream(buffer.toArray()));
    }

    // Método para capturar e exibir a imagem da câmera
    public static void startVideoCapture(Stage stage) {
        OpenCV.loadShared();
        VideoCapture capture = new VideoCapture(0);
        ImageView imageView = new ImageView();
        HBox hbox = new HBox(imageView);
        Scene scene = new Scene(hbox);
        stage.setScene(scene);
        stage.show();

        new AnimationTimer() {
            @Override
            public void handle(long l) {
                if (!capture.isOpened()) {
                    stop();
                }
                Mat mat = new Mat();
                if (capture.read(mat)) {
                    imageView.setImage(mat2Img(mat));
                }
            }
        }.start();
    }
}