package com.team2;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static java.util.Arrays.asList;

// Note: All these get mapping are only for the base code. Will need to update to appropriate mapping when we have
// implementation
@Controller
@RequestMapping("/api")
public class DatabaseController extends Database{

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
    // Note: String return above. May need to change this to whatever object type we have
    // TODO: I think the MongoDB connection code can go in here (or create another class and tie it into this one to
    //  be cleaner)
    MongoClient mongoClient= new MongoClient("localhost" ,8888);
    MongoDatabase database = mongoClient.getDatabase("YAML_Database");
    MongoCollection<Document> collection = database.getCollection("YAML_info");

    Document microserviceDocument = new Document("_id", new ObjectId())
            .append("name", "YourMicroserviceName")
            .append("category", "Database")
            .append("leadEngineer", new Document("name", "EngineerName").append("email", "engineer@email.com"))
            .append("description", "Description of your microservice")
            .append("dateCreated", "2023-09-07") // Replace with the actual date
            .append("version", "1.0")
            .append("dependencies", asList("Dependency1", "Dependency2"))
            .append("openapi", "YourOpenAPISpecification");

    private static JsonNode createObjectNode(String title, String category, String leadengname, String leadengemail,
                                             String description, String date, String version, String dependencies, String openapispec) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("title", title);
        objectNode.put("category", category);
        objectNode.put("leadengname", leadengname);
        objectNode.put("leadengemail", leadengemail);
        objectNode.put("description", description);
        objectNode.put("date", date);
        objectNode.put("version", version);
        objectNode.put("dependencies", dependencies);
        objectNode.put("openapispec", openapispec);
        return objectNode;
    }

    // TODO: We may want to change these from GetMapping and to the appropriate mapping i.e. delete, post etc...

    @GetMapping("/get/{id}")
    public ResponseEntity<Map<String, String>> get(@PathVariable String id) {

        //fetch from database document where id is name of service and version
        //title =id
        //retrieve many /single
        //i'll use this for now

        Document retrieved =RetrieveSingleDocument(collection,id);
        Map<String, String> responseData = new HashMap<>();

        if (retrieved ==null){
            responseData.put("Response","File with the title: "+id+" not found.");
            return new ResponseEntity<>(responseData, HttpStatus.NOT_FOUND);
        }
        else{
            String yaml =convertJsonToYaml(retrieved.toJson());
            responseData.put("file",yaml);

            System.out.println("All is ok for now");
            return new ResponseEntity<>(responseData, HttpStatus.OK);
        }



    }

    @PostMapping("/upload")
    public ResponseEntity<Map<String, String>> put(@RequestParam("file") MultipartFile file){


        //TODO: Accept yaml file.
        try {
            //TODO: Parse out info section for DB.

            // Example parsing functions

            JsonNode jsonObject =  Parser.convertYamlToJson(file);
            byte [] encoded =Database.EncodeFile(file);
            System.out.println(encoded);
            MultipartFile decoded =Database.DecodeFile(encoded);
            System.out.println(jsonObject);
            System.out.println(decoded);
            String title= String.valueOf(jsonObject.get("info").get("title"));
            String category = String.valueOf(jsonObject.get("info").get("x-category"));
            String description = String.valueOf(jsonObject.get("info").get("description"));
            String xname = String.valueOf(jsonObject.get("info").get("contact").get("x-name"));
            String email = String.valueOf(jsonObject.get("info").get("contact").get("email"));
            String date = String.valueOf(jsonObject.get("info").get("x-dateCreated"));
            String version = String.valueOf(jsonObject.get("info").get("version"));
            List<String> depend = List.of(String.valueOf(jsonObject.get("x-dependencies")));
            String api= "temp";


            // System.out.println(jsonObject.get("info").get("x-category"));


            Document microDocument = new Document("_id", new ObjectId())
                    .append("title",title)
                    .append("x-category",category)
                    .append("description",description)
                    .append("contact", new Document("x-name", xname).append("email", email))
                    .append("x-dateCreated",date)
                    .append("version",version)
                    .append("x-dependencies", depend)
                    .append("openapi",api);
            this.InsertDocument(collection,microDocument);
            System.out.println(microDocument);

            // Call the newInsert method
           // databaseController.newInsert(collection,microserviceDocument);
            //Document microserviseDocument = new Document("_id", new ObjectId());
            //microserviseDocument.append("openapi", jsonObject.get("openapi"));
//                    .append("info", asList(new Document("title",jsonObject.get("info").get("title")),
//                                    new Document(("x-category"),jsonObject.get("info").get("x-category")),
//                                    new Document(("description"),jsonObject.get("info").get("description")),
//                                    new Document(("contact"),asList(
//                                            new Document("x-name",jsonObject.get("info").get("x-name")),
//                                            new Document("email",jsonObject.get("info").get("email"))
//                                    )
//
//                                    ),
//                                    new Document("dateCreated",jsonObject.get("info").get("x-dateCreated")),
//                                    new Document("version",jsonObject.get("info").get("version"))
//
//                            )
//
//                    )
//                    .append("externalDocs",jsonObject.get("tags"))
//                    .append("paths",jsonObject.get("paths"))
//                    .append("file", encoded);

//            System.out.println("We got here!");
            Map<String, String> response = new HashMap<>();
            response.put("message", "File uploaded successfully");
            CacheControl cacheControl = CacheControl.noStore().mustRevalidate().cachePrivate().maxAge(0, TimeUnit.SECONDS);
            return ResponseEntity.ok().cacheControl(cacheControl).body(response);


        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }

        //TODO: Implement put info section into DB. Id will be contained in the body.

        //return ResponseEntity.ok("Put endpoint called for DB");
    }


    @GetMapping("/update/{id}")
    public ResponseEntity<String> update(@PathVariable String id){
//    public ResponseEntity<String> update(@PathVariable String id, @RequestBody String updates){
        //TODO: Implement put from DB. Id is the service name. updates is the changed to make (probably will need to be
        // changed from String. Uncomment second function name to include body with changed to PacthMapping
        return ResponseEntity.ok("Put endpoint called for DB for id: " + id);
    }

    @GetMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable String id){
        //TODO: Implement delete from DB.
        // TODO: Change to delete mapping

        return ResponseEntity.ok("Delete endpoint called for DB id: " + id);
    }

    @GetMapping("/new")
    public ResponseEntity<Resource> getNewJson() throws IOException {
        // Load the new.json file as a Resource
        Resource fileResource = new ClassPathResource("static/new.json");

        // Set cache control headers to prevent caching
        CacheControl cacheControl = CacheControl.noStore().mustRevalidate().cachePrivate().maxAge(0, TimeUnit.SECONDS);

        return ResponseEntity.ok()
                .cacheControl(cacheControl)
                .contentType(MediaType.APPLICATION_JSON)
                .body(fileResource);
    }

    @GetMapping("/getALL")
    public ResponseEntity<String> getAll (){

        String documentList= RetrieveManyDocuments(collection);
        System.out.println(documentList);
        if (documentList.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }else {
            return ResponseEntity.ok(documentList);
        }
    }
}
