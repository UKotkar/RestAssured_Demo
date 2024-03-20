package pk_spreecom;

import org.json.simple.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class AddressWithLabels {

	// Global Variable
	String outh_token;

	@Test(dataProvider = "addressWithLabel", dataProviderClass = Spreecom_TestData.class, priority = 1, enabled = true)
	public void addSameAddressLable(String fName, String lName, String address1, String city, String zipcode,
			String phone, String state, String country, String label) {
		JSONObject newAddress = new JSONObject();
		newAddress.put("firstname", fName);
		newAddress.put("lastname", lName);
		newAddress.put("address1", address1);
		newAddress.put("city", city);
		newAddress.put("zipcode", zipcode);
		newAddress.put("phone", phone);
		newAddress.put("state_name", state);
		newAddress.put("country_iso", country);
		newAddress.put("label", label);
		JSONObject body = new JSONObject();
		body.put("address", newAddress);
		Response response = RestAssured.given().auth().oauth2(Util_Functions.oAuth_Token()).body(body)
				.contentType(ContentType.JSON)
				.post("https://demo.spreecommerce.org/api/v2/storefront/account/addresses").then().extract().response();
		int statusCode = response.getStatusCode();
		Assert.assertEquals(200, statusCode);
		// check label Work exist
		JsonPath jsonPathEvaluator = response.getBody().jsonPath();
		String actLabel = jsonPathEvaluator.get("data.attributes.label").toString();
		Assert.assertEquals(actLabel, label);

		// Add same address with same label
		response = RestAssured.given().auth().oauth2(Util_Functions.oAuth_Token()).body(body)
				.contentType(ContentType.JSON)
				.post("https://demo.spreecommerce.org/api/v2/storefront/account/addresses").then().extract().response();
		statusCode = response.getStatusCode();
		Assert.assertEquals(422, statusCode);
		jsonPathEvaluator = response.getBody().jsonPath();
		String generalErr = jsonPathEvaluator.get("error").toString();
		Assert.assertEquals(generalErr, "Address name has already been taken");
		String specificErr = jsonPathEvaluator.get("errors.label").toString();
		Assert.assertEquals(specificErr, "[has already been taken]");
	}
}
