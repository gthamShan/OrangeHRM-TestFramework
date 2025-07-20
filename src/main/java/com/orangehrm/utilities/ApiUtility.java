package com.orangehrm.utilities;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class ApiUtility {

	public static void sendGetRequest(String endpointUrl) {
		// Implementation for sending a GET request to the specified URL
		// This could use libraries like HttpClient, OkHttp, etc.
		RestAssured.get(endpointUrl);
	}

	public static void sendPostRequest(String endpointUrl, String requestBody) {
		// Implementation for sending a POST request to the specified URL with the given
		// body
		// This could use libraries like HttpClient, OkHttp, etc.
		RestAssured.given().headers("Content-Type", "application/json").body(requestBody).post(endpointUrl);
	}

	public static boolean validateStatusCode(Response response, int expectedStatusCode) {
		return response.getStatusCode() == expectedStatusCode;
	}

	public static String getJsonValue(Response response, String jsonPath) {
		// Implementation for extracting a value from the JSON response using the
		// specified JSON path
		// This could use libraries like JsonPath, Jackson, etc.
		return response.jsonPath().getString(jsonPath);
	}
}
