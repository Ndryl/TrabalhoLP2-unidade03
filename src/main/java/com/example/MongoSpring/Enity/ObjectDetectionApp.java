package com.example.MongoSpring.Enity;


import java.io.IOException;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.videoio.VideoCapture;
public class ObjectDetectionApp {

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME); // Carregar a biblioteca nativa do OpenCV
    }

    public static void main(String[] args) {
        // Caminho para o arquivo de cascata Haar
        String cascadeFile = "haarcascade_frontalface_default.xml";

        // Carrega o classificador Haar
        CascadeClassifier faceDetector = new CascadeClassifier(cascadeFile);

        if (faceDetector.empty()) {
            System.out.println("Erro ao carregar o arquivo de cascata.");
            return;
        }

        // Inicia captura de vídeo
        VideoCapture videoCapture = new VideoCapture(0); // 0 para webcam padrão

        if (!videoCapture.isOpened()) {
            System.out.println("Erro ao acessar a câmera.");
            return;
        }

        Mat frame = new Mat();

        while (true) {
    videoCapture.read(frame);

    if (frame.empty()) {
        System.out.println("Nenhum quadro capturado.");
        break;
    }

    MatOfRect faces = new MatOfRect();
    faceDetector.detectMultiScale(frame, faces);

    for (Rect rect : faces.toArray()) {
        Imgproc.rectangle(frame, new Point(rect.x, rect.y),
                new Point(rect.x + rect.width, rect.y + rect.height),
                new Scalar(0, 255, 0), 2);
    }

    Imgcodecs.imwrite("frame.jpg", frame);

    try {
        if (System.in.available() > 0) break;
    } catch (IOException e) {
        e.printStackTrace();
        break; // Opcional: encerra o loop em caso de erro
    }
}


        videoCapture.release();
    }
}
