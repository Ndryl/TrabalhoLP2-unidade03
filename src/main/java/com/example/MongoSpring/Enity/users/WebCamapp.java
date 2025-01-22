package com.example.MongoSpring.Enity.users;
import javafx.embed.swing.SwingFXUtils;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import nu.pattern.OpenCV;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.videoio.VideoCapture;
import org.opencv.imgcodecs.Imgcodecs;
import javafx.scene.image.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class WebCamapp extends Application {

    private VideoCapture videoCapture;
    private CascadeClassifier faceDetector;
    private ImageView imageView;

    @Override
    public void start(Stage primaryStage) {
        // Inicializa o OpenCV
        OpenCV.loadShared();

        // Certifica-se de que o arquivo de cascata está disponível
        String cascadeFileName = "haarcascade_frontalface_alt.xml";
        File cascadeFile = new File(cascadeFileName);
        if (!cascadeFile.exists()) {
            createCascadeFile(cascadeFile);
        }

        // Configuração do VideoCapture e do detector de faces
        videoCapture = new VideoCapture(0); // 0 indica a câmera padrão
        faceDetector = new CascadeClassifier(cascadeFile.getAbsolutePath()); // Caminho do arquivo Haarcascade
        imageView = new ImageView();

        // Criação da cena
        StackPane root = new StackPane(imageView);
        Scene scene = new Scene(root, 800, 600);

        primaryStage.setTitle("Detecção de Faces com OpenCV");
        primaryStage.setScene(scene);
        primaryStage.show();

        // Inicia a captura e processamento de vídeo
        new Thread(this::processVideo).start();
    }

    private void createCascadeFile(File cascadeFile) {
        try (InputStream inputStream = getClass().getResourceAsStream("/haarcascade_frontalface_alt.xml");
             FileOutputStream outputStream = new FileOutputStream(cascadeFile)) {
            if (inputStream == null) {
                throw new RuntimeException("Arquivo haarcascade_frontalface_alt.xml não está embutido no JAR.");
            }
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            System.out.println("Arquivo haarcascade_frontalface_alt.xml criado com sucesso!");
        } catch (Exception e) {
            throw new RuntimeException("Erro ao criar o arquivo haarcascade_frontalface_alt.xml.", e);
        }
    }

    private void processVideo() {
        Mat frame = new Mat();

        while (videoCapture.isOpened()) {
            videoCapture.read(frame); // Lê o frame da câmera

            if (!frame.empty()) {
                // Detecta faces no frame
                MatOfRect faces = new MatOfRect();
                faceDetector.detectMultiScale(frame, faces);

                // Desenha retângulos em torno das faces detectadas
                for (Rect rect : faces.toArray()) {
                    Imgproc.rectangle(frame,
                            new Point(rect.x, rect.y),
                            new Point(rect.x + rect.width, rect.y + rect.height),
                            new Scalar(0, 255, 0), 2);
                }

                // Converte o frame para exibição no JavaFX
                BufferedImage bufferedImage = matToBufferedImage(frame);
                if (bufferedImage != null) {
                    Image fxImage = SwingFXUtils.toFXImage(bufferedImage, null);
                    imageView.setImage(fxImage);
                }
            }
        }
    }

    private BufferedImage matToBufferedImage(Mat mat) {
        try {
            MatOfByte buffer = new MatOfByte();
            Imgcodecs.imencode(".png", mat, buffer);
            return javax.imageio.ImageIO.read(new java.io.ByteArrayInputStream(buffer.toArray()));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void stop() {
        if (videoCapture != null) {
            videoCapture.release(); // Libera a câmera
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
