package ru.yandex.practicum;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.Map;

import static io.restassured.RestAssured.given;

public abstract class BaseHttpClient {

    private final RequestSpecification baseRequestSpec;

    public BaseHttpClient() {
        this.baseRequestSpec = new RequestSpecBuilder()
                .setBaseUri(URL.getHost())
                .addHeader("Content-Type", "application/json")
                .setRelaxedHTTPSValidation()
                .addFilter(new RequestLoggingFilter())
                .addFilter(new ResponseLoggingFilter())
                .build();
    }

    protected Response doGetRequest(String path){
        return given()
                .spec(baseRequestSpec)
                .get(path)
                .then()
                .extract()
                .response();
    }

    protected Response doGetRequest(String path, Map<String,Integer> params){
        return given()
                .spec(baseRequestSpec)
                .params(params)
                .get(path)
                .then()
                .extract()
                .response();
    }

    protected Response doPostRequest (String path, Object body){
        return given()
                .spec(baseRequestSpec)
                .body(body)
                .post(path)
                .then()
                .extract()
                .response();
    }

    protected Response doDeleteRequest (String path) {
        return given()
                .spec(baseRequestSpec)
                .delete(path)
                .then()
                .extract()
                .response();
    }
}