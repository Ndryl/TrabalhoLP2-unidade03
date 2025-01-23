package com.example.MongoSpring.Controller;


import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import com.example.MongoSpring.Enity.QuantityUpdateRequest;

import org.opencv.core.Mat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.MongoSpring.Enity.ImageUtils;
import com.example.MongoSpring.Enity.MyObject;
import com.example.MongoSpring.Enity.Product;
import com.example.MongoSpring.service.ObjectService;
@RestController
@RequestMapping("/api/object")
public class ObjectController {
    @Autowired
    private ObjectService ObjectService;
    @GetMapping("/ObjectsFromUser/{idUser}")
    public List<MyObject>getObjects(@PathVariable String idUser){
        return ObjectService.getObjectById(idUser).map(List::of).orElse(List.of());
    }
    @PostMapping("/insert")
    public MyObject saveObject(@RequestBody MyObject myObject) {
        return ObjectService.saveObject(myObject); // Certifique-se de usar o nome correto de 'ObjectService' com "O" maiúsculo
    }
    @PostMapping("/insertWithImage")
    public MyObject saveObjectWithImage(@RequestBody Map<String, String> requestData) {
        // Extrair dados do corpo da solicitação
        String imagePath = requestData.get("imagePath");
        String userId = requestData.get("userId");
        String nameObject = requestData.get("nameObject");

        // Verificar se os dados são válidos
        if (imagePath == null || userId == null || nameObject == null) {
            throw new IllegalArgumentException("Todos os campos são obrigatórios!");
        }

        // Converter a imagem base64 para bytes
        byte[] imageBytes = ImageUtils.base64ToBytes    (imagePath);

        // Criar o objeto e salvar
        MyObject myObject = new MyObject(null, LocalDate.now(), imageBytes, userId, nameObject);
        return ObjectService.saveObject(myObject);
    }





    @DeleteMapping("/delete/{idObject}")
    public void deleteObject(@PathVariable String idObject){
        ObjectService.deleteObject(idObject);
    }   
    @PutMapping("/update/{idObject}")
    public MyObject updatedObject(@PathVariable String id, MyObject newObject){
        return ObjectService.updateObject(id, newObject);
    }

}
