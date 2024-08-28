package com.team2;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class Parser {
    // Function to convert Yaml file to Json Object
    public static JsonNode convertYamlToJson(MultipartFile yamlFile) throws IOException {
        //Create and map Yaml file to Yaml data
        ObjectMapper yamlMapper = new ObjectMapper(new YAMLFactory());
        Map<String, Object> yamlData = yamlMapper.readValue(yamlFile.getInputStream(), Map.class);

        //Create map for Json and map Yaml to a string in Json format
        ObjectMapper jsonMapper = new ObjectMapper();
        String jsonString = jsonMapper.writeValueAsString(yamlData);
        //Create Json object from Json string
        JsonNode jsonObject = jsonMapper.readTree(jsonString);


        // return Json Object
        return jsonObject;
    }


    public static String convertJsonToYaml(String jsonContent) {
        try {
            // Create a SnakeYAML instance
            Yaml yaml = new Yaml();

            // Load the JSON string as an Object
            Object jsonObject = yaml.load(jsonContent);

            // Create custom DumperOptions to format the YAML
            DumperOptions dumperOptions = new DumperOptions();
            dumperOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);

            // Create a Yaml instance with custom DumperOptions
            Yaml yamlConverter = new Yaml(dumperOptions);

            // Convert the JSON Object to YAML String
            String yamlString = yamlConverter.dump(jsonObject);

            return yamlString;
        } catch (Exception e) {
            throw new RuntimeException("Failed to convert JSON to YAML: " + e.getMessage(), e);
        }
    }

    public static MultipartFile convertYamlToMultipartFile(String yamlContent) {
        // Create a byte array from the YAML content
        byte[] yamlBytes = yamlContent.getBytes(StandardCharsets.UTF_8);

        // Create a MultipartFile instance with .yaml extension
        MultipartFile multipartFile = new MockMultipartFile(
                "openapi.yaml", // Filename with .yaml extension
                "openapi.yaml", // Original filename (same as filename)
                "application/x-yaml", // Content type for YAML
                yamlBytes
        );
        return multipartFile;
    }
}

