package com.team2;
import com.mongodb.client.FindIterable;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;
import org.bson.Document;
import com.mongodb.client.MongoCollection;
import org.bson.conversions.Bson;
import org.bson.json.JsonMode;
import org.bson.json.JsonWriterSettings;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


import static com.mongodb.client.model.Filters.*;

public class Database extends Parser{
//    public static void main( String[] args ) {
//       //  try (MongoClient mongoClient= new MongoClient("localhost" ,8888)){
//        try (MongoClient mongoClient= new MongoClient("localhost" ,27020)){
//            MongoDatabase database= mongoClient.getDatabase("YAML_Database");
//            MongoCollection<Document> collection= database.getCollection("YAML_info");
//            collection.drop();
//            InsertDocument(collection);
//            InsertManyDocuments(collection);
//            System.out.println("Collection YAML_info selected successfully");
//            RetrieveManyDocuments(collection);
//            UpdateDocument(collection);
//            UpdateManyDocuments(collection);
//            DeleteDocument(collection);
//            DeleteManyDocuments(collection);
//
//        }
//    }

    public static void InsertDocument(MongoCollection<Document> collection,Document document){

        collection.insertOne(document);
        System.out.println("Document inserted successfully");
    }
    public static void InsertManyDocuments(MongoCollection<Document> collection,Document document){
        List <Document> list = new ArrayList<>();

        for (int i =0;i<=3;i++){
            Document doc = new Document("name", "TestingInserts");
            list.add(doc);

        }

        collection.insertMany(list);

    }

    public  static Document RetrieveSingleDocument(MongoCollection<Document> collection, String id){
        Bson filter = Filters.eq("title", id);
//    Bson filter = Filters.eq("title", id);
        //get it by title
        Document result = collection.find(filter).first();
        if(result==null){
            //throw an exception
            System.out.println("No Documents found with the title:" + id);
            return null;
        }
        return result;
    }
//    public static String RetrieveSingleDocument(MongoCollection<Document> collection){
//        Bson filter = Filters.eq("_id", "your_document_id");
//        Document result = collection.find(filter).first();
//
//        List<Document> documentList = new ArrayList<>();
//        documentList.add(result);
//
//        // Create a JSON writer settings to format the JSON
//        JsonWriterSettings settings = JsonWriterSettings.builder()
//                .outputMode(JsonMode.RELAXED) // You can choose the output mode as needed
//                .build();
//
//        // Convert the list of documents to a JSON array string
//        String jsonArray = documentList.stream()
//                .map(document -> document.toJson(settings))
//                .collect(Collectors.joining(",", "[", "]"));
//
//        System.out.println("Retrieved All Documents Successfully");
//        return jsonArray;    }


    public static String RetrieveManyDocuments(MongoCollection<Document> collection) {
        FindIterable<Document> retrievedDocuments = collection.find();
        List<Document> documentList = new ArrayList<>();

        for (Document retrievedDocument : retrievedDocuments) {
            documentList.add(retrievedDocument);
        }

        if (documentList.isEmpty()) {
            System.out.println("No documents found in the collection.");
            return "[]"; // Return an empty JSON array in this case
        }

        // Create a JSON writer settings to format the JSON
        JsonWriterSettings settings = JsonWriterSettings.builder()
                .outputMode(JsonMode.RELAXED) // You can choose the output mode as needed
                .build();

        // Convert the list of documents to a JSON array string
        String jsonArray = documentList.stream()
                .map(document -> document.toJson(settings))
                .collect(Collectors.joining(",", "[", "]"));

        System.out.println("Retrieved All Documents Successfully");
        return jsonArray;
    }


    //Updates Done
    public static void UpdateDocument(MongoCollection<Document> collection){
        collection.updateOne(Filters.eq("name", "Sam"), Updates.set("name","Harry"));
    }

    public static void UpdateManyDocuments(MongoCollection<Document> collection){

        Bson filterBy = Filters.eq("name", "David");

        Bson updateTo = Updates.set("name", "Julia");

        collection.updateMany(filterBy, updateTo);
    }

    public static void DeleteDocument(MongoCollection<Document> collection){
        collection.deleteOne(Filters.eq("name", "Sam"));
        System.out.println("Document deleted successfully...");
    }

    public static void DeleteManyDocuments(MongoCollection<Document> collection){
        //will need to be parameters taken in later
        Bson query= eq("name", "Julia");
        collection.deleteMany(query);

    }
    public static byte[] EncodeFile(MultipartFile jsonObject) throws IOException {
        byte[] fileBytes = jsonObject.getBytes();
        byte[] base64Encoded = Base64.getEncoder().encode(fileBytes);
        return base64Encoded;
    }

    public static MultipartFile DecodeFile(byte[] encoded) throws IOException {
        byte[] decodedBytes = Base64.getDecoder().decode(encoded);

        return new MockMultipartFile("file.json", "file.json", "application/json", decodedBytes);
    }

    public static void WriteYAMLFileToVolume(MultipartFile file) {
        String filePath = "/volume/openapi.yaml";
        try {
// Convert MultipartFile to a byte array
            byte[] bytes = file.getBytes();

// Write the byte array to the file
            File Writtenfile = new File(filePath);
            try (BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(Writtenfile))) {
                stream.write(bytes);
            }

            System.out.println("File 'openapi.yaml' created or overwritten successfully.");
        } catch (IOException e) {
            System.err.println("Failed to write 'openapi.yaml' file: " + e.getMessage());
        }
    }

    public static void WriteYAMLStringToVolume(String json) {
        String filePath = "/volume/openapi.yaml";

        String yamlString = convertJsonToYaml(json);

        MultipartFile file = convertYamlToMultipartFile(yamlString);

        try {
// Convert MultipartFile to a byte array
            byte[] bytes = file.getBytes();

// Write the byte array to the file
            File Writtenfile = new File(filePath);
            try (BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(Writtenfile))) {
                stream.write(bytes);
            }

            System.out.println("File 'openapi.yaml' created or overwritten successfully.");
        } catch (IOException e) {
            System.err.println("Failed to write 'openapi.yaml' file: " + e.getMessage());
        }

    }
}

