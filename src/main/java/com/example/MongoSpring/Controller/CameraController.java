package com.example.MongoSpring.Controller;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

@RestController
@RequestMapping("/process-image")
public class CameraController {

    static {
        // Carregar a biblioteca OpenCV
        nu.pattern.OpenCV.loadLocally();
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] processImage(@RequestBody ImageRequest imageRequest) {
        try {
            // Decodificar a imagem base64 recebida do front-end
            byte[] decodedBytes = java.util.Base64.getDecoder().decode(imageRequest.getImage().split(",")[1]);
            Mat image = Imgcodecs.imdecode(new MatOfByte(decodedBytes), Imgcodecs.IMREAD_COLOR);

            // Processar a imagem (detectar objeto e desenhar quadrado)
            Mat processedImage = detectObject(image);

            // Codificar a imagem para enviar de volta ao front-end
            MatOfByte buffer = new MatOfByte();
            Imgcodecs.imencode(".jpg", processedImage, buffer);
            return buffer.toArray();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private Mat detectObject(Mat image) {
        // Exemplo: Detectar bordas com Canny e desenhar um quadrado verde
        Imgproc.cvtColor(image, image, Imgproc.COLOR_BGR2GRAY);
        Imgproc.Canny(image, image, 100, 200);
        Imgproc.cvtColor(image, image, Imgproc.COLOR_GRAY2BGR);

        // Desenhar um quadrado verde (substituir com lógica de detecção real)
        Imgproc.rectangle(image, new Point(50, 50), new Point(200, 200), new Scalar(0, 255, 0), 3);
        return image;
    }

    public static class ImageRequest {
        private String image;

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }
    }
}
