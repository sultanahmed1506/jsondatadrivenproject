//Parameterize, Read data from JSON file (Data Driven)
package jsondatadrivenproject;

import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;

public class DDTestUsingJSON {

	WebDriver driver;

	@BeforeClass
	void setup() {
		WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.MILLISECONDS);
	}

	@AfterClass
	void tearDown() {
		driver.close();
	}

	@Test(dataProvider = "dp")
	void login(String data) {

		String users[] = data.split(",");
		driver.get("http://demo.guru99.com/V1/index.php");
		driver.findElement(By.name("uid")).sendKeys(users[0]); // userame
		driver.findElement(By.name("password")).sendKeys(users[1]); // password
		driver.findElement(By.name("btnLogin")).click();

		String acc_title = driver.getTitle();
		String exp_title = "GTPL Bank Manager HomePage";

		Assert.assertEquals(acc_title, exp_title);

	}

	@DataProvider(name = "dp")
	public String[] readJson() throws ParseException, IOException {
		JSONParser jsonParser = new JSONParser();
		FileReader reader = new FileReader(".\\jsonfiles\\testdata.json");
		Object obj = jsonParser.parse(reader);
		JSONObject userloginsJsonobj = (JSONObject) obj;
		JSONArray userLoginsArray = (JSONArray) userloginsJsonobj.get("userlogins");

		String arr[] = new String[userLoginsArray.size()];
		for (int i = 0; i < userLoginsArray.size(); i++) {
			JSONObject users = (JSONObject) userLoginsArray.get(i);
			String user = (String) users.get("username");
			String pwd = (String) users.get("password");
			arr[i] = user + "," + pwd;
		}
		return arr;
	}
}
