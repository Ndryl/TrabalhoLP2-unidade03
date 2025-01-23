package com.example.MongoSpring.Enity.users;

import nu.pattern.OpenCV;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.videoio.VideoCapture;

import javax.swing.*;
import java.awt.*;
import java.awt.Point;
import java.awt.image.BufferedImage;

public class WebCamApp {

    private VideoCapture videoCapture;
    private CascadeClassifier faceDetector;
    private JFrame frame;
    private JLabel label;
    private volatile boolean isRunning = true; // Control for video thread

    public WebCamApp() {
        // Initialize OpenCV
        OpenCV.loadLocally();
        

        // Initialize resources
        initializeCascadeClassifier();
        initializeCamera();

        // Setup Swing UI
        setupUI();

        // Start video processing in a separate thread
        new Thread(this::processVideo).start();
    }

    private void setupUI() {
        frame = new JFrame("Face Detection with OpenCV");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        label = new JLabel();
        frame.getContentPane().add(label, BorderLayout.CENTER);

        frame.setVisible(true);
    }
    private void createDefaultCascadeFile(String outputPath) throws Exception {
        String defaultCascadeContent = """
            <?xml version="1.0"?>
            <opencv_storage>
            <cascade>
                <!-- Coloque o conteúdo do Haar Cascade aqui -->
            </cascade>
            </opencv_storage>
        """;
    
        // Criar os diretórios, se não existirem
        java.nio.file.Path path = java.nio.file.Paths.get(outputPath);
        java.nio.file.Files.createDirectories(path.getParent());
    
        // Escrever o conteúdo no arquivo
        java.nio.file.Files.writeString(path, defaultCascadeContent);
    
        System.out.println("Arquivo criado em: " + path.toAbsolutePath());
    }
    
    

    private void initializeCascadeClassifier() {
        String fileName = "haarcascade_frontalface_alt.xml";
        String resourcePath = "src/main/resources/" + fileName;
        var resource = getClass().getClassLoader().getResource(fileName);
    
        if (resource == null) {
            System.out.println("Arquivo não encontrado. Criando um novo arquivo padrão...");
    
            try {
                // Criar o arquivo Haar Cascade padrão
                createDefaultCascadeFile(resourcePath);
    
                // Obter o caminho absoluto do arquivo criado
                resource = getClass().getClassLoader().getResource(fileName);
                if (resource == null) {
                    throw new RuntimeException("Falha ao criar e carregar o arquivo Haar Cascade.");
                }
            } catch (Exception e) {
                throw new RuntimeException("Erro ao criar o arquivo Haar Cascade.", e);
            }
        }
    
        String cascadeFileName = resourcePath;
        faceDetector = new CascadeClassifier(cascadeFileName);
    
        if (faceDetector.empty()) {
            throw new RuntimeException("Falha ao carregar o classificador. Caminho: " + cascadeFileName);
        }
    
        System.out.println("Classificador Haar Cascade carregado com sucesso.");
    }
    
    
    

    private void initializeCamera() {
        videoCapture = new VideoCapture(0); // Default camera
        if (!videoCapture.isOpened()) {
            throw new RuntimeException("Unable to open camera.");
        }
    }

    private void processVideo() {
    Mat frameMat = new Mat(); // Corrigir a declaração de Mat
    while (isRunning && videoCapture.isOpened()) {
        try {
            videoCapture.read(frameMat); // Ler o quadro da câmera

            if (!frameMat.empty()) {
                MatOfRect faces = new MatOfRect();
                faceDetector.detectMultiScale(frameMat, faces);

                for (Rect rect : faces.toArray()) {
                    Imgproc.rectangle(frameMat,
        new org.opencv.core.Point(rect.x, rect.y),
        new org.opencv.core.Point(rect.x + rect.width, rect.y + rect.height),
        new Scalar(0, 255, 0), 2);

                }

                BufferedImage bufferedImage = matToBufferedImage(frameMat);
                if (bufferedImage != null) {
                    ImageIcon imageIcon = new ImageIcon(bufferedImage);
                    SwingUtilities.invokeLater(() -> label.setIcon(imageIcon)); // Atualizar a interface com segurança
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    frameMat.release(); // Libera o recurso de Mat
}


    public void stop() {
        isRunning = false; // Signal the video thread to stop
        if (videoCapture != null) {
            videoCapture.release(); // Release the camera
        }
        frame.dispose(); // Close the Swing window
    }

    // Utility method to convert Mat to BufferedImage
    private BufferedImage matToBufferedImage(Mat mat) {
        int type = mat.channels() > 1 ? BufferedImage.TYPE_3BYTE_BGR : BufferedImage.TYPE_BYTE_GRAY;
        BufferedImage image = new BufferedImage(mat.cols(), mat.rows(), type);
        byte[] data = new byte[mat.cols() * mat.rows() * (int) mat.elemSize()];
        mat.get(0, 0, data);
        image.getRaster().setDataElements(0, 0, mat.cols(), mat.rows(), data);
        return image;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new WebCamApp()); // Start the application
    }
}
