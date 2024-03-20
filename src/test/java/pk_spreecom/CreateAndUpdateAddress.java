package pk_spreecom;

import java.io.IOException;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;

import bsh.ParseException;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class CreateAndUpdateAddress {
	String ID;

	@Test(priority = 1)
	public void Create_Address() throws IOException, ParseException, org.json.simple.parser.ParseException {

		Response response = RestAssured.given().auth().oauth2(Util_Functions.oAuth_Token()).contentType(ContentType.JSON)
				.body(Util_Functions.readFile("CreateAddress.json"))
				.post("https://demo.spreecommerce.org/api/v2/storefront/account/addresses").then().extract().response();
		response.getBody().prettyPrint();

		// Now let us print the body of the message to see what response
		// we have recieved from the server
		String responseBody = response.getBody().asString();
		// System.out.println("Response Body is => " + responseBody);
		// Status Code Validation
		int statusCode = response.getStatusCode();
		System.out.println("Status code is =>  " + statusCode);
		Assert.assertEquals(200, statusCode);

		Map<String, String> id_create = response.jsonPath().getJsonObject("data");
		ID = id_create.get("id");

	}

	@Test(priority = 2)
	public void Update_Address() throws IOException, ParseException, org.json.simple.parser.ParseException {

		Response response = RestAssured.given().auth().oauth2(Util_Functions.oAuth_Token()).contentType(ContentType.JSON)
				.body(Util_Functions.readFile("UpdateAddress.json"))
				.patch("https://demo.spreecommerce.org/api/v2/storefront/account/addresses/" + ID).then().extract()
				.response();
		response.getBody().prettyPrint();

		// Now let us print the body of the message to see what response
		// we have recieved from the server
		String responseBody = response.getBody().asString();
		// System.out.println("Response Body is => " + responseBody);
		// Status Code Validation
		int statusCode = response.getStatusCode();
		System.out.println("Status code is =>  " + statusCode);
		Assert.assertEquals(200, statusCode);

		Map<String, String> default_billing_address = response.jsonPath().getJsonObject("data.attributes");
		String firstName = default_billing_address.get("firstname");
		Assert.assertEquals(firstName, "USK");

		String lastName = default_billing_address.get("lastname");
		Assert.assertEquals(lastName, "Kotkar");

		String addressOne = default_billing_address.get("address1");
		Assert.assertEquals(addressOne, "Jayanagar, Bangalor");

		String addressTwo = default_billing_address.get("address2");
		Assert.assertEquals(addressTwo, "4th T Block 19th Cross");
	}

}
