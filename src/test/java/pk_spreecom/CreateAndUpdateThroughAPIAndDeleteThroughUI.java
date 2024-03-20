package pk_spreecom;

import java.io.IOException;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import bsh.ParseException;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class CreateAndUpdateThroughAPIAndDeleteThroughUI {
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

	@Test(priority = 3)
	public void deleteAddress_UI() throws InterruptedException {
		// Launch the Browser
		// WebDriverManager.chromedriver().setup();
		FirefoxDriver driver = new FirefoxDriver();
		driver.manage().window().maximize();
		// Enter the URL
		driver.get("https://demo.spreecommerce.org/login");
		// Click on Icon
		/*
		 * driver.findElement(By.xpath("//button[@id='account-button']//*[name()='svg']"
		 * )).click();
		 * 
		 * //click on login driver.findElement(By.xpath("//a[.='LOGIN']")).click();
		 */
		Thread.sleep(3000);
		driver.findElement(By.xpath("//input[@id='spree_user_email']")).sendKeys("usk@spee.com");
		Thread.sleep(3000);
		driver.findElement(By.xpath("//input[@id='spree_user_password']")).sendKeys("Rest#24");
		driver.findElement(By.xpath("//input[@name='commit']")).click();
		Thread.sleep(3000);

		// Delete User from Search List

		// driver.findElementByXPath("//a[text()='"+ExpUserName+"']/parent::td/preceding-sibling::td/input").click();
		driver.findElement(
				By.xpath("//span[contains(text(),'USK')]//parent::address//parent::div//parent::div/div[2]/a[2]"))
				.click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//a[@id='delete-address-popup-confirm']")).click();
		// driver.findElement(By.xpath("//a[@data-hook='remove_address']")).click();
		driver.findElement(By.xpath("//span[normalize-space()='Address has been successfully removed.']"))
				.isDisplayed();

		System.out.println("Test Success");
		driver.quit();
	}
}
