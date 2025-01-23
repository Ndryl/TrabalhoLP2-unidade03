package com.example.MongoSpring.Controller;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

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

            // Processar a imagem (detectar objeto e desenhar quadrado)
            Mat processedImage = imagemProcessada(image);
            Mat processedImageDefinit = detectObject(processedImage);

            // Codificar a imagem para enviar de volta ao front-end
            MatOfByte buffer = new MatOfByte();
            Imgcodecs.imencode(".jpg", processedImageDefinit, buffer);
            return buffer.toArray();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    private List<Rect> mergeIntersectingRectangles(List<Rect> rectangles) {
        List<Rect> mergedRectangles = new ArrayList<>();
        boolean[] visited = new boolean[rectangles.size()];
    
        for (int i = 0; i < rectangles.size(); i++) {
            if (visited[i]) continue;
    
            Rect baseRect = rectangles.get(i);
            visited[i] = true;
    
            for (int j = i + 1; j < rectangles.size(); j++) {
                if (visited[j]) continue;
    
                Rect compareRect = rectangles.get(j);
    
                // Verificar se os dois retângulos se interceptam
                if (rectanglesIntersect(baseRect, compareRect)) {
                    // Unir os retângulos em um único retângulo maior
                    baseRect = unionRectangles(baseRect, compareRect);
                    visited[j] = true; // Marcar o segundo retângulo como visitado
                }
            }
    
            mergedRectangles.add(baseRect);
        }
    
        return mergedRectangles;
    }
    
    private boolean rectanglesIntersect(Rect rect1, Rect rect2) {
        return rect1.x < rect2.x + rect2.width &&
               rect1.x + rect1.width > rect2.x &&
               rect1.y < rect2.y + rect2.height &&
               rect1.y + rect1.height > rect2.y;
    }
    
    private Rect unionRectangles(Rect rect1, Rect rect2) {
        int x = Math.min(rect1.x, rect2.x);
        int y = Math.min(rect1.y, rect2.y);
        int width = Math.max(rect1.x + rect1.width, rect2.x + rect2.width) - x;
        int height = Math.max(rect1.y + rect1.height, rect2.y + rect2.height) - y;
        return new Rect(x, y, width, height);
    }
    
    private Mat detectObject(Mat image) {
        // Converter a imagem para escala de cinza
        Mat grayImage = new Mat();
        Imgproc.cvtColor(image, grayImage, Imgproc.COLOR_BGR2GRAY);
    
        // Aplicar um filtro para suavizar a imagem
        Imgproc.GaussianBlur(grayImage, grayImage, new Size(5, 5), 0);
    
        // Detectar bordas com Canny
        Mat edges = new Mat();
        Imgproc.Canny(grayImage, edges, 100, 200);
    
        // Encontrar contornos na imagem
        List<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(edges, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
    
        // Criar uma lista para armazenar os retângulos delimitadores
        List<Rect> rectangles = new ArrayList<>();
        for (MatOfPoint contour : contours) {
            Rect boundingRect = Imgproc.boundingRect(contour);
            rectangles.add(boundingRect);
        }
    
        // Unir retângulos que têm interseções
        rectangles = mergeIntersectingRectangles(rectangles);
    
        // Desenhar os retângulos restantes na imagem original
        for (Rect rect : rectangles) {
            Imgproc.rectangle(image,
                              rect.tl(), // Top-left corner
                              rect.br(), // Bottom-right corner
                              new Scalar(0, 255, 0), // Cor (verde)
                              2); // Espessura da linha
        }
    
        return image;
    }
    

    private Mat imagemProcessada(Mat image) {
        // Converter a imagem para escala de cinza
        Mat grayImage = new Mat();
        Imgproc.cvtColor(image, grayImage, Imgproc.COLOR_BGR2GRAY);

        // Detectar bordas com Canny
        Mat edges = new Mat();
        Imgproc.cvtColor(image, image, Imgproc.COLOR_BGR2GRAY);
        Imgproc.Canny(image, image, 100, 200);
        Imgproc.cvtColor(image, image, Imgproc.COLOR_GRAY2BGR);

        // Encontrar contornos na imagem
        List<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(edges, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

        // Criar uma lista para armazenar os retângulos delimitadores
        List<Rect> rectangles = new ArrayList<>();
        for (MatOfPoint contour : contours) {
            Rect boundingRect = Imgproc.boundingRect(contour);
            rectangles.add(boundingRect);
        }

        // Filtrar os retângulos (remover menores ou que tenham arestas em comum)
        rectangles = filterRectangles(rectangles);

        // Desenhar os retângulos restantes na imagem original
        for (Rect rect : rectangles) {
            Imgproc.rectangle(image,
                    rect.tl(), // Top-left corner
                    rect.br(), // Bottom-right corner
                    new Scalar(0, 255, 0), // Cor (verde)
                    2); // Espessura da linha
        }

        return image;
    }

    private boolean isRectangleContained(Rect small, Rect large) {
        // Verificar se todas as arestas do retângulo pequeno estão dentro do grande
        return (small.x >= large.x &&
                small.y >= large.y &&
                small.x + small.width <= large.x + large.width &&
                small.y + small.height <= large.y + large.height);
    }

    private List<Rect> filterRectangles(List<Rect> rectangles) {
        // Ordenar os retângulos por área em ordem decrescente
        rectangles.sort((r1, r2) -> Double.compare(r2.area(), r1.area()));

        // Lista para armazenar os retângulos filtrados
        List<Rect> filteredRectangles = new ArrayList<>();

        for (int i = 0; i < rectangles.size(); i++) {
            Rect current = rectangles.get(i);
            boolean isContained = false;

            // Verificar se o retângulo atual está contido em algum outro retângulo maior
            for (int j = 0; j < i; j++) {
                Rect larger = rectangles.get(j);
                if (isRectangleContained(current, larger)) {
                    isContained = true;
                    break;
                }
            }

            // Adicionar o retângulo se ele não estiver contido em outro maior
            if (!isContained) {
                filteredRectangles.add(current);
            }
        }

        return filteredRectangles;
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
