package com.example.MongoSpring.Controller;

import org.opencv.core.*;
import org.opencv.dnn.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

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
            byte[] decodedBytes = Base64.getDecoder().decode(imageRequest.getImage().split(",")[1]);
            Mat image = Imgcodecs.imdecode(new MatOfByte(decodedBytes), Imgcodecs.IMREAD_COLOR);

            // Detectar objetos na imagem
            Mat processedImage = detectObjects(image);

            // Codificar a imagem para enviar de volta ao front-end
            MatOfByte buffer = new MatOfByte();
            Imgcodecs.imencode(".jpg", processedImage, buffer);
            return buffer.toArray();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private Mat detectObjects(Mat image) throws IOException {
        // Caminhos para os arquivos do modelo YOLO
        String modelConfiguration = "/home/jo-o-fontes/Downloads/yolo/yolov3.cfg";
        String modelWeights = "/home/jo-o-fontes/Downloads/yolo/yolov3.weights";
        String classNamesFile = "/home/jo-o-fontes/Downloads/yolo/coco.names";

        // Carregar o modelo YOLO
        Net net = Dnn.readNetFromDarknet(modelConfiguration, modelWeights);

        // Carregar os nomes das classes
        List<String> classNames = Files.readAllLines(Paths.get(classNamesFile));

        // Preparar a imagem para a detecção
        Mat blob = Dnn.blobFromImage(image, 1 / 255.0, new Size(416, 416), new Scalar(0, 0, 0), true, false);
        net.setInput(blob);

        // Realizar a detecção
        List<Mat> result = new ArrayList<>();
        List<String> outBlobNames = getOutputNames(net);
        net.forward(result, outBlobNames);

        // Processar os resultados
        float confThreshold = 0.5f;
        for (Mat level : result) {
            for (int i = 0; i < level.rows(); i++) {
                Mat row = level.row(i);
                Mat scores = row.colRange(5, level.cols());
                Core.MinMaxLocResult mm = Core.minMaxLoc(scores);
                float confidence = (float) mm.maxVal;
                Point classIdPoint = mm.maxLoc;
                if (confidence > confThreshold) {
                    int centerX = (int) (row.get(0, 0)[0] * image.cols());
                    int centerY = (int) (row.get(0, 1)[0] * image.rows());
                    int width = (int) (row.get(0, 2)[0] * image.cols());
                    int height = (int) (row.get(0, 3)[0] * image.rows());
                    int left = centerX - width / 2;
                    int top = centerY - height / 2;

                    // Desenhar o retângulo ao redor do objeto detectado
                    Imgproc.rectangle(image, new Point(left, top), new Point(left + width, top + height), new Scalar(0, 255, 0), 2);

                    // Adicionar o nome da classe
                    String label = classNames.get((int) classIdPoint.x);
                    Imgproc.putText(image, label, new Point(left, top - 10), Imgproc.FONT_HERSHEY_SIMPLEX, 0.5, new Scalar(0, 255, 0), 2);
                }
            }
        }

        return image;
    }

    private List<String> getOutputNames(Net net) {
        List<String> names = new ArrayList<>();
        List<Integer> outLayers = net.getUnconnectedOutLayers().toList();
        List<String> layersNames = net.getLayerNames();
        for (Integer i : outLayers) {
            names.add(layersNames.get(i - 1));
        }
        return names;
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