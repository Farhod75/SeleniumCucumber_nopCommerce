package stepDefinitions;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import junit.framework.Assert;
import pageObjects.AddcustomerPage;
import pageObjects.LoginPage;
import pageObjects.SearchCustomerPage;

public class Steps extends BaseClass {
	
	@Before
	public void setup() throws IOException
	{
		logger=Logger.getLogger("SeleniumCucumber");// Added logger
		PropertyConfigurator.configure("Log4j.properties");// Added logger
		
		//Reading properties
		configProp=new Properties();
		FileInputStream configPropFile=new FileInputStream("config.properties");
		configProp.load(configPropFile);
						
		String br=configProp.getProperty("browser");
		
		if(br.equals("chrome"))
		{
			
	   System.setProperty("webdriver.chrome.driver", configProp.getProperty("chromepath"));
	   driver=new ChromeDriver();
		}
		else if (br.equals("firefox")) {
			System.setProperty("webdriver.gecko.driver", configProp.getProperty("firefoxpath"));
			driver=new FirefoxDriver();
		}
		else if (br.equals("ie")) {
			System.setProperty("webdriver.ie.driver", configProp.getProperty("iepath"));
			driver=new InternetExplorerDriver();
		}
		
		
	   logger.info("******* Launching browser *****");
	}
		
	@Given("User Launch Chrome browser")
	public void user_launch_chrome_browser() {
	   		
	   lp=new LoginPage(driver);
	}

	@When("User opens URL {string}")
	public void user_opens_url(String url) {
		logger.info("******* Opening URL *****");
	    driver.get(url);
	    driver.manage().window().maximize();
	}

	@When("User enters Email as {string} and Password as {string}")
	public void user_enters_email_as_and_password_as(String email, String password) {
		logger.info("******* Providing login details *****");
	    lp.setUserName(email);
	    lp.setPassword(password);
	}

	@When("Click on Login")
	public void click_on_login() throws InterruptedException {
		logger.info("******* Started login *****");
	    lp.clickLogin();
	    Thread.sleep(3000);
	}

	@Then("Page Title should be {string}")
	public void page_title_should_be(String title) throws InterruptedException {
	    if (driver.getPageSource().contains("Login was unseccessful.")) {
	    	driver.close();
	    	logger.info("******* Login passed *****");
	    	Assert.assertTrue(false);
	    }else {
	    	logger.info("******* Login failed *****");
	    	Assert.assertEquals(title, driver.getTitle());
	    }
	    Thread.sleep(3000);
	}

	@When("User click on Log out link")
	public void user_click_on_log_out_link() throws InterruptedException {
		logger.info("******* Click logout link *****");
	    lp.clickLogout();
	    Thread.sleep(3000);
	}

	@Then("close browser")
	public void close_browser() {
		logger.info("******* Closing browser *****");
	    driver.quit();
	}

	
	// Customer feature step definitions...
	@Then("User can view Dashboard")
	public void user_can_view_dashboard() {
	   addCust=new AddcustomerPage(driver);
	   Assert.assertEquals("Dashboard / nopCommerce administration", addCust.getPageTitle());
	}
	@When("User click on customers Menu")
	public void user_click_on_customers_menu() throws InterruptedException {
		Thread.sleep(3000);
		addCust.clickOnCustomersMenu();
	}
	@When("click on customers Menu Item")
	public void click_on_customers_menu_item() throws InterruptedException {
		Thread.sleep(2000);
		addCust.clickOnCustomersMenuItem();
	}
	@When("click on Add new button")
	public void click_on_add_new_button() throws InterruptedException { 
		addCust.clickOnAddnew();
		Thread.sleep(2000);
	}
	
	@Then("User can view Add a new customer page")
	public void user_can_view_add_a_new_customer_page() {
		Assert.assertEquals("Add a new customer / nopCommerce administration", addCust.getPageTitle() );
	}//Add a new customer / nopCommerce administration
  
	
	@When("User enter customer info")
	public void user_enter_customer_info() throws InterruptedException {
		logger.info("******* Adding new customer *****");
		logger.info("******* Providing customer details *****");
		
		
	    String email=randomstring()+"@gmail.com";
	    addCust.setEmail(email);
	    addCust.setPassword("test123");
	    //Registered - default
	    // The customer cannot be in both 'Guests' and 'Registered' customer roles
	    // Add the customer to 'Guests' or'Registered' customer roles
	    
	    addCust.setCustomerRoles("Guest");
	    Thread.sleep(3000);
	    
	    addCust.setManagerOfVendor("Vendor 2");
	    addCust.setGender("Male");
	    addCust.setFirstName("Pavan");
	    addCust.setLastName("Kumar");
	    addCust.setDob("7/05/1985");//Format: D/MM/YYYY
	    addCust.setCompanyName("infosys");
	    addCust.setAdminContent("This is for testing....");
	     
	    
	}
	@When("click on Save button")
	public void click_on_save_button() throws InterruptedException {
		logger.info("******* Saving customer data *****");
		addCust.clickOnSave();
		Thread.sleep(2000);
	}
	@Then("User can view confirmation message {string}")
	public void user_can_view_confirmation_message(String msg) {
	    Assert.assertTrue(driver.findElement(By.tagName("body")).getText()
	    		.contains("The new customer has been added successfully."));
	}
	
	
	//steps for searching a customer using Email ID.......
	@When("Enter customer Email")
	public void enter_customer_email() {
		logger.info("******* Searching customer by email id *****");
		
		searchCust= new SearchCustomerPage(driver);
	    searchCust.setEmail("brenda_lindgren@nopCommerce.com");
	}
	@When("Click on search button")
	public void click_on_search_button() throws InterruptedException {
		searchCust.clickSearch();
		Thread.sleep(3000);
	}
	@Then("User sould found Email in the search table")
	public void user_sould_found_email_in_the_search_table()  {
		boolean status=searchCust.searchCustomerByEmail("brenda_lindgren@nopCommerce.com");
		
		Assert.assertEquals(true, status);
	}

	//Steps foe searching a customer by using First Name & Last Name
	
	
	
	@When("Enter customer FirstName")
	public void enter_customer_first_name() {
		logger.info("******* Searching customer by Name *****");
		
		searchCust= new SearchCustomerPage(driver); 
		searchCust.setFirstName("Brenda");
	}
	@When("Enter customer LastName")
	public void enter_customer_last_name() {
		searchCust.setLastName("Lindgren");
	}
	@Then("User should found Name in the Search table")
	public void user_should_found_name_in_the_search_table() {
		boolean status=searchCust.searchCustomerByName("Brenda Lindgren");
		Assert.assertEquals(true, status);
	}
	




	
	
	
}
