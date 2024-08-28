package com.team2.App;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.team2.Parser;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ParserTest {
    @Test
    public void checkParserCorrect() throws IOException {
        // Mock the a Yaml file data
        String yamlContent = "person:\n  name: John Doe\n  age: 30\n  city: New York";
        MockMultipartFile yamlFile = new MockMultipartFile("file", "YAML.yaml", "text/yaml", yamlContent.getBytes());

        // Call our conversion method
        JsonNode jsonNode = Parser.convertYamlToJson(yamlFile);

        // Define the expected JSON
        String expectedJsonContent = "{\"person\":{\"name\":\"John Doe\",\"age\":30,\"city\":\"New York\"}}";
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode expectedJsonNode = objectMapper.readTree(expectedJsonContent);

        // Assert that conversion is correct
        assertEquals(expectedJsonNode, jsonNode);
    }
}
