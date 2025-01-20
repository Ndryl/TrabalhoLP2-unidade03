package com.example.MongoSpring.Enity;

import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;

public class ImageUtils {
    // Método para carregar uma imagem do disco
    public static Mat loadImage(String imagePath) {
        return Imgcodecs.imread(imagePath); // Corrigido para usar imread
    }

    // Método para salvar uma imagem no disco
    public static void saveImage(Mat imageMatrix, String targetPath) {
        Imgcodecs.imwrite(targetPath, imageMatrix);
    }
    // Converte um Mat (OpenCV) para um array de bytes
    public static byte[] matToBytes(Mat mat) {
        MatOfByte buffer = new MatOfByte();
        Imgcodecs.imencode(".jpg", mat, buffer); // Codifica o Mat como JPEG
        return buffer.toArray();
    }


}
