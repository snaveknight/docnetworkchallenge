package com.example.template.controller;

import com.example.template.data.MyClassJDBCDAO;
import com.example.template.model.MyClass;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
public class TestRestController {

    @Autowired
    MyClassJDBCDAO jdbcDAO;

    @RequestMapping("/v1/retrieve")
    public List<MyClass> controllerMethod(Model model) {

        List<MyClass> dbData = jdbcDAO.getMyClassData();
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            HttpEntity<String> httpEntity = new HttpEntity<>(new HttpHeaders());
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> googleApi = restTemplate.exchange("https://www.googleapis.com/books/v1/volumes?q=it", HttpMethod.GET, httpEntity, String.class);
            System.out.println(googleApi);
            JsonNode projectNode = objectMapper.readTree(googleApi.getBody());
            String nodePath = projectNode.path("items").path(0).path("volumeInfo").path("authors").path(0).asText();
            System.out.println(nodePath);
            int upperBound = projectNode.path("items").size();
            for (int i = 0; i <= upperBound; i ++) {
                System.out.println(projectNode.path("items").path(i).path("volumeInfo").path("authors").path(0).asText());
            }
        }
        catch (Exception e) {

        }
        return dbData;
    }

}
