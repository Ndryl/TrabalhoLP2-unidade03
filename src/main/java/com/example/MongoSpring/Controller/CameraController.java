package com.example.MongoSpring.Controller;

import io.github.cdimascio.dotenv.Dotenv;
import org.opencv.core.*;
import org.opencv.dnn.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import com.example.MongoSpring.Enity.ImageUtils;
import com.example.MongoSpring.Enity.MyObject;
import com.example.MongoSpring.service.ObjectService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
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
    @Autowired
    private ObjectService objectService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] processImageAndSave(@RequestBody ImageRequest imageRequest) {
        try {
            // Decodificar a imagem base64 recebida
            byte[] decodedBytes = Base64.getDecoder().decode(imageRequest.getImage().split(",")[1]);
            Mat image = Imgcodecs.imdecode(new MatOfByte(decodedBytes), Imgcodecs.IMREAD_COLOR);

            // Processar a imagem
            Mat processedImage = detectObjects(image);

            // Converter Mat para byte[]
            byte[] processedImageBytes = ImageUtils.matToBytes(processedImage);

            // Criar e salvar um MyObject com a imagem processada
            MyObject myObject = new MyObject();
            myObject.setPhoto(processedImageBytes);
            myObject.setUserId(""); // Sem usuário
            myObject.setDate(LocalDate.now());
            objectService.saveObject(myObject); // Usar a instância do serviço injetada

            // Retornar a imagem processada ao cliente
            MatOfByte buffer = new MatOfByte();
            Imgcodecs.imencode(".jpg", processedImage, buffer);
            return buffer.toArray();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    


    private Mat detectObjects(Mat image) throws IOException {
        Dotenv dotenv = Dotenv.configure().load();
        // Caminhos para os arquivos do modelo YOLO
        String modelConfiguration = dotenv.get("MODEL_CONFIGURATION");
        String modelWeights = dotenv.get("MODEL_WEIGHTS");
        String classNamesFile = dotenv.get("CLASS_NAMES_FILE");
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
        float nmsThreshold = 0.4f;
        List<Rect> boxes = new ArrayList<>();
        List<Float> confidences = new ArrayList<>();
        List<Integer> classIds = new ArrayList<>();

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

                    boxes.add(new Rect(left, top, width, height));
                    confidences.add(confidence);
                    classIds.add((int) classIdPoint.x);
                }
            }
        }

        // Converter para os formatos esperados
        List<Rect2d> rect2dList = new ArrayList<>();
        for (Rect rect : boxes) {
            rect2dList.add(new Rect2d(rect.x, rect.y, rect.width, rect.height));
        }
        MatOfRect2d matOfBoxes = new MatOfRect2d();
        matOfBoxes.fromList(rect2dList);

        MatOfFloat matOfConfidences = new MatOfFloat();
        matOfConfidences.fromList(confidences);

        // Aplicar Non-Maximum Suppression ( Evitar reduncancias nos retângulos)
        MatOfInt indices = new MatOfInt();
        Dnn.NMSBoxes(matOfBoxes, matOfConfidences, confThreshold, nmsThreshold, indices);


        int[] indicesArray = indices.toArray();
        for (int idx : indicesArray) {
            Rect box = boxes.get(idx);
            Imgproc.rectangle(image, box.tl(), box.br(), new Scalar(0, 128, 0), 2);

            // Adicionar o nome da classe
            String label = classNames.get(classIds.get(idx));
            Imgproc.putText(image, label, new Point(box.x, box.y - 10), Imgproc.FONT_HERSHEY_SIMPLEX, 1.0, new Scalar(0, 128, 0), 2);
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