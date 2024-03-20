package pk_spreecom;

import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.testng.Assert;
import org.testng.annotations.Test;

import bsh.ParseException;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class CreateAnAddressUsingFile {

	// Global Variable

	@Test
	public void createAddress() throws IOException, ParseException, org.json.simple.parser.ParseException {

		// Create json object of JSONParser class to parse the JSON data
		JSONParser jsonparser = new JSONParser();
		// Create object for FileReader class, which help to load and read JSON file
		FileReader reader = new FileReader(".\\Test Data\\CreateAddress.json");
		// Returning/assigning to Java Object
		Object obj = jsonparser.parse(reader);
		// Convert Java Object to JSON Object, JSONObject is typecast here
		JSONObject prodjsonobj = (JSONObject) obj;

		String bearerToken = "g1b3bfotlLg674AHl_OUZ3EMYkV8SR2tYVu-BI1_ivs";
		Response response = RestAssured.given().auth().oauth2(bearerToken).contentType(ContentType.JSON)
				.body(prodjsonobj).post("https://demo.spreecommerce.org/api/v2/storefront/account/addresses").then()
				.extract().response();
		response.getBody().prettyPrint();

		// Now let us print the body of the message to see what response
		// we have received from the server
		String responseBody = response.getBody().asString();
		System.out.println("Response Body is =>  " + responseBody);
		// Status Code Validation
		int statusCode = response.getStatusCode();
		System.out.println("Status code is =>  " + statusCode);
		Assert.assertEquals(200, statusCode);

		JsonPath jsonPathEvaluator = response.getBody().jsonPath();
		String fname = jsonPathEvaluator.get("data.attributes.firstname").toString();
		System.out.println("First Name is =>  " + fname);
		Assert.assertEquals(fname, "Ujjwala");
		// VErify that Token Type is Bearer or not
		String Lname = jsonPathEvaluator.get("data.attributes.lastname").toString();
		// String ExpTokenType = "Bearer";
		Assert.assertEquals(Lname, "K");
	}
}
