package com.qa.rest.tests;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import junit.framework.Assert;

public class TC_GETandDelete {
	
	
	@BeforeClass
	public void setup() {
		//Configuring the base URI & base Path
		RestAssured.baseURI="http://dummy.restapiexample.com";
		RestAssured.basePath="/api/v1";
	}
	
//	Below method perform "get all employee detail",in this method i am testing get functionality of the api
   @Test(priority = 1)
	public void test_getAllEmployeeDetails() {
		
		given()
		.when()
			.get("/employees")
		.then()
			.statusCode(200)
		.log().all();
		
		
	}
	
//   Below method perform "get specific employee detail by ID", here i am testing get api with specific id.
	@Test(priority = 2)
	public void test_getSpecificEmployeeDetail() {
		given()
		.when()
		      .get("/employee/1")
		.then()
			.statusCode(200)
			.body("data.id", equalTo(1))
			.body("data.employee_name", equalTo("Tiger Nixon"))
			.body("data.employee_salary", equalTo(320800))
			.body("data.employee_age", equalTo(61))
		.log().all();
	}
	
//	Below method perform "delete operation" for specific given id, here i am testing the delete functionality to do that first i am validating what exist
//	at id = 2 then i will test delete functionality with passing id=2 and at last i will test get functionality id =2 that should be different from first get operation.
	
	@Test(priority = 3)
	public void test_getSpecificEmployeeDetailForDelete() {
		given()
		.when()
		      .get("/employee/2")
		.then()
			.statusCode(200)
			.body("data.id", equalTo(2))
			.body("data.employee_name", equalTo("Garrett Winters"))
			.body("data.employee_salary", equalTo(170750))
			.body("data.employee_age", equalTo(63))
		.log().all();
	}
	@Test(priority = 3)
	public void test_deleteEmployeeDetail() {
		Response res = 
		given()
		.when()
		      .delete("/delete/2")
		.then()
		      .statusCode(200)
		      .body("data", equalTo("2"))
		      .body("status", equalTo("success"))
		      .body("message", equalTo("Successfully! Record has been deleted"))
		      .log().body()
		      .extract().response();
		String jsonString = res.asString();
		
		//can be done in this way as well to store it in response & then compare all attributes values using assert
		Assert.assertEquals(jsonString.contains("Successfully! Record has been deleted"), true);
	}

	// This test case will fail as delete functionality of the api is broken, 
	// though in above test case it is giving that the record id=2 successfully deleted but as per below test case you can see it is not deleted
	@Test(priority = 3)
	public void test_getSpecificEmployeeDetailForValidating() {
		given()
		.when()
	      .get("/employee/2")
	.then()
		.statusCode(200)
		.body("data.id", is(not(equalTo(2))))
		.body("data.employee_name", is(not(equalTo("Garrett Winters"))))
		.body("data.employee_salary", is(not(equalTo(170750))))
		.body("data.employee_age", is(not(equalTo(63))))
	.log().all();
		      
	}

}
