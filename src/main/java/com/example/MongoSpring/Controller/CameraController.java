package com.example.MongoSpring.Controller;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Base64;

@RestController
@RequestMapping("/api/opencv")
public class CameraController {

    @PostMapping("/detect")
    public byte[] detectObject(@RequestBody FrameRequest request) {
        try {
            // Decodificar a imagem em base64 para Mat
            byte[] imageBytes = Base64.getDecoder().decode(request.getImage().split(",")[1]);
            BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(imageBytes));
            Mat frame = bufferedImageToMat(bufferedImage);

            // Detectar objeto (aqui, um exemplo de detecção de cor)
            Scalar lowerBound = new Scalar(0, 100, 100); // Limite inferior (HSV)
            Scalar upperBound = new Scalar(10, 255, 255); // Limite superior (HSV)

            Mat hsvFrame = new Mat();
            Imgproc.cvtColor(frame, hsvFrame, Imgproc.COLOR_BGR2HSV);
            Mat mask = new Mat();
            Core.inRange(hsvFrame, lowerBound, upperBound, mask);

            // Encontrar contornos do objeto
            java.util.List<MatOfPoint> contours = new java.util.ArrayList<>();
            Imgproc.findContours(mask, contours, new Mat(), Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

            // Desenhar quadrados ao redor dos objetos detectados
            for (MatOfPoint contour : contours) {
                Rect rect = Imgproc.boundingRect(contour);
                Imgproc.rectangle(frame, rect, new Scalar(0, 255, 0), 3); // Quadrado verde
            }

            // Converter o frame processado de volta para byte[]
            MatOfByte buffer = new MatOfByte();
            Imgcodecs.imencode(".png", frame, buffer);

            return buffer.toArray(); // Retornar o frame processado
        } catch (Exception e) {
            throw new RuntimeException("Erro ao processar a imagem", e);
        }
    }

    // Converter BufferedImage para Mat
    private Mat bufferedImageToMat(BufferedImage bi) {
        Mat mat = new Mat(bi.getHeight(), bi.getWidth(), CvType.CV_8UC3);
        int[] data = ((java.awt.image.DataBufferInt) bi.getRaster().getDataBuffer()).getData();
        mat.put(0, 0, data);
        return mat;
    }
}


class FrameRequest {
    private String image;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
