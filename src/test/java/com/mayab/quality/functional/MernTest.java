package com.mayab.quality.functional;

import java.util.regex.Pattern;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.*;
import org.junit.jupiter.api.Assertions;
import org.junit.runners.MethodSorters;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
//import org.apache.commons.io.FileUtils;
import java.io.File;
import java.time.Duration;

public class MernTest {
	private WebDriver driver;
	private String baseUrl;
	private boolean acceptNextAlert = true;
	private StringBuffer verificationErrors = new StringBuffer();
	JavascriptExecutor js;
	
	
	@Before
	public void setUp() throws Exception {
		//System.setProperty("webdriver.chrome.driver", "");
		driver = new ChromeDriver();
		baseUrl = "https://www.google.com/";
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(60));
		js = (JavascriptExecutor) driver;
		}
	
	@Test
	public void createRecordTest() throws Exception {
	  driver.get("https://mern-crud-mpfr.onrender.com/");
	  driver.findElement(By.xpath("//div[@id='root']/div/div[2]/button")).click();
	  driver.findElement(By.name("name")).click();
	  driver.findElement(By.name("name")).clear();
	  driver.findElement(By.name("name")).sendKeys("Melanie");
	  driver.findElement(By.name("email")).click();
	  driver.findElement(By.name("email")).clear();
	  driver.findElement(By.name("email")).sendKeys("mel@email.com");
	  driver.findElement(By.name("age")).click();
	  driver.findElement(By.name("age")).clear();
	  driver.findElement(By.name("age")).sendKeys("22");
	  driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Gender'])[2]/following::div[1]")).click();
	  driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Male'])[2]/following::div[1]")).click();
	  driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Woah!'])[1]/following::button[1]")).click();
	  pause(2000);
	  
	  String usuario = driver.findElement(By.xpath("/html/body/div[2]/div/div[2]/table/tbody/tr[1]/td[1]")).getText();
	  assertEquals(usuario,"Melanie");
	  }
	
	@Test
	public void existingEmailTest() throws Exception {
		createRecord("TestingEmail","melanie@email.com");		
		
		pause(2000);
		
		driver.findElement(By.xpath("//div[@id='root']/div/div[2]/button")).click();
	    driver.findElement(By.name("name")).click();
	    driver.findElement(By.name("name")).clear();
	    driver.findElement(By.name("name")).sendKeys("TestingEmail");
	    driver.findElement(By.name("email")).click();
	    driver.findElement(By.name("email")).clear();
	    driver.findElement(By.name("email")).sendKeys("melanie@email.com");
	    driver.findElement(By.name("age")).click();
	    driver.findElement(By.name("age")).clear();
	    driver.findElement(By.name("age")).sendKeys("22");
	    driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Gender'])[2]/following::div[1]")).click();
	    driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Male'])[2]/following::div[1]")).click();
	    driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Woah!'])[1]/following::button[1]")).click();
	    pause(2000);
		
		
		String error = driver.findElement(By.xpath("/html/body/div[3]/div/div[2]/form/div[5]/div/p")).getText();
		assertEquals(error,"That email is already taken.");
		}
	
	@Test
	public void modifyAge() throws Exception {
		createRecord("Usr Modify", "modify@email.com");
		pause(2000);
		driver.get("https://mern-crud-mpfr.onrender.com/");
	    driver.findElement(By.xpath("//div[@id='root']/div/div[2]/table/tbody/tr/td[5]/button")).click();
	    pause(2000);
	    driver.findElement(By.name("age")).click();
	    driver.findElement(By.name("age")).clear();
	    driver.findElement(By.name("age")).sendKeys("100");
	    pause(2000);
	    driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Woah!'])[1]/following::button[1]")).click();
	    pause(1000);
	    driver.findElement(By.xpath("//i")).click();
	
	    
	    String edad = driver.findElement(By.xpath("//*[@id=\"root\"]/div/div[2]/table/tbody/tr[1]/td[3]")).getText();
	    assertEquals(edad,"100");
	    }
	
	@Test
	public void delete() throws Exception {
		createRecord("To be eliminated","eliminated@email.com");
		pause(2000);
		
		driver.get("https://mern-crud-mpfr.onrender.com/");
	    driver.findElement(By.xpath("//div[@id='root']/div/div[2]/table/tbody/tr/td[5]/button[2]")).click();
	    pause(2000);
	    driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='To Be Eliminated'])[2]/following::button[1]")).click();
	    pause(2000);	    
	    
	    String borrado = driver.findElement(By.xpath("/html/body/div[2]/div/div[2]/table/tbody/tr[1]/td[1]")).getText();
	    assertNotEquals(borrado,"To be eliminated");
	    }
	
	
	@Test
	public void searchName() throws Exception {
		createRecord("Search 1", "search@email.com");
		createRecord("Example", "search2@email.com");
		createRecord("Example", "search3@email.com");
		createRecord("Example", "search4@email.com");
		
		String encontrado = null;
		
		driver.get("https://mern-crud-mpfr.onrender.com/");
		pause(2000);
		List<WebElement> rows = driver.findElements(By.xpath("/html/body/div[2]/div/div[2]/table/tbody/tr"));
		
		for (int i = 1; i <= rows.size(); i++) {
			String nombre = driver.findElement(By.xpath("/html/body/div[2]/div/div[2]/table/tbody/tr[" + i + "]/td[1]")).getText();
			pause(500);
			if (nombre.equals("Search 1")) {
				encontrado = nombre;
				break;
			}
		}
		
		assertNotNull(encontrado);
		assertEquals("Search 1", encontrado);
		
	}
	
	@Test
	public void searchAll() throws Exception{
		createRecord("Record 1", "record1@email.com");
		createRecord("Record 2", "record2@email.com");
		createRecord("Record 3", "record3@email.com");
		createRecord("Record 4", "record4@email.com");
		
		List<String> nombres = new ArrayList<>();
	    List<String> correos = new ArrayList<>();
	    
	    driver.get("https://mern-crud-mpfr.onrender.com/");
	    pause(2000);
	    
	    List<WebElement> rows = driver.findElements(By.xpath("/html/body/div[2]/div/div[2]/table/tbody/tr"));
	    
	    
	    for (int i = 1; i <= rows.size(); i++) {
	        String nombre = driver.findElement(By.xpath("/html/body/div[2]/div/div[2]/table/tbody/tr[" + i + "]/td[1]")).getText();
	        String correo = driver.findElement(By.xpath("/html/body/div[2]/div/div[2]/table/tbody/tr[" + i + "]/td[2]")).getText();
	        nombres.add(nombre);
	        correos.add(correo);
	    }
	    
	    assertTrue(nombres.contains("Record 1"));
	    assertTrue(nombres.contains("Record 2"));
	    assertTrue(nombres.contains("Record 3"));
	    assertTrue(nombres.contains("Record 4"));

	    assertTrue(correos.contains("record1@email.com"));
	    assertTrue(correos.contains("record2@email.com"));
	    assertTrue(correos.contains("record3@email.com"));
	    assertTrue(correos.contains("record4@email.com"));
	}
	
	private void pause(long mils) {
		try {
			Thread.sleep(mils);
			}catch(Exception e){
				e.printStackTrace();
				}
		}
	
	private void createRecord(String nombre, String email) {
		driver.get("https://mern-crud-mpfr.onrender.com/");
		driver.findElement(By.xpath("//div[@id='root']/div/div[2]/button")).click();
		driver.findElement(By.name("name")).click();
		driver.findElement(By.name("name")).clear();
		driver.findElement(By.name("name")).sendKeys(nombre);
		driver.findElement(By.name("email")).click();
		driver.findElement(By.name("email")).clear();
		driver.findElement(By.name("email")).sendKeys(email);
		driver.findElement(By.name("age")).click();
		driver.findElement(By.name("age")).clear();
		driver.findElement(By.name("age")).sendKeys("19");
		driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Gender'])[2]/following::div[1]")).click();
		driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Male'])[2]/following::div[1]")).click();
		driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Woah!'])[1]/following::button[1]")).click();
		driver.findElement(By.xpath("//i")).click();
	}
  
  @After
  public void tearDown() throws Exception {
    driver.quit();
    String verificationErrorString = verificationErrors.toString();
    if (!"".equals(verificationErrorString)) {
      fail(verificationErrorString);
    }
  }

  private boolean isElementPresent(By by) {
    try {
      driver.findElement(by);
      return true;
    } catch (NoSuchElementException e) {
      return false;
    }
  }

  private boolean isAlertPresent() {
    try {
      driver.switchTo().alert();
      return true;
    } catch (NoAlertPresentException e) {
      return false;
    }
  }

  private String closeAlertAndGetItsText() {
    try {
      Alert alert = driver.switchTo().alert();
      String alertText = alert.getText();
      if (acceptNextAlert) {
        alert.accept();
      } else {
        alert.dismiss();
      }
      return alertText;
    } finally {
      acceptNextAlert = true;
    }
  }
}