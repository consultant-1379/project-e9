package com.team2.App;

import com.team2.SwaggerController;
import org.junit.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.servlet.view.RedirectView;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class SwaggerControllerTest {

    @Autowired
    private SwaggerController swaggerController;

    @Mock
    private MockHttpServletRequest request;

    @Mock
    private MockHttpServletResponse response;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testRedirectToSwagger(){
        // This test method does NOT test that the swagger editor is opened or anything, just verifies redirect and url
        RedirectView redirectView = swaggerController.get("service:1.1/openapi.yaml");

        // Verify the endpoint returns the Redirect
        assertEquals(RedirectView.class, redirectView.getClass());

        // Verify the redirect url is correct
        assertEquals(swaggerController.getSwaggerEditorUrl()+"/?url=/volume/openapi.yaml",
                redirectView.getUrl());
    }


}
