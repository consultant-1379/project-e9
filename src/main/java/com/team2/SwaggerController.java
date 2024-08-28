package com.team2;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/api")
public class SwaggerController {

    @Value("${swaggerEditorUrl}")
    private String swaggerEditorUrl;


    @GetMapping("{service_details}/openapi.yaml")
    public RedirectView get(@PathVariable String service_details) {
        // Construct the URL to open Swagger Editor with the Swagger YAML file
        String swaggerEditorOpenUrl = swaggerEditorUrl + "/?url=/volume/openapi.yaml";


        // Redirect to the Swagger Editor URL
        return new RedirectView(swaggerEditorOpenUrl);

        // TODO: add in post mappings to post to DB (May be better to use the post mappings in the DB controller already)
    }

    public String getSwaggerEditorUrl() {
        return this.swaggerEditorUrl;
    }
}
