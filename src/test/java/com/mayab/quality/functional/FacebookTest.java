package com.mayab.quality.functional;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.time.Duration;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import io.github.bonigarcia.wdm.WebDriverManager;

public class FacebookTest {
	private static WebDriver driver;
	  private String baseUrl;
	  private boolean acceptNextAlert = true;
	  private StringBuffer verificationErrors = new StringBuffer();
	  JavascriptExecutor js;
  
	  @Before
  public void setUp() throws Exception {
	    WebDriverManager.chromedriver().setup();
	    driver = new ChromeDriver();
	    baseUrl = "https://www.google.com/";
	    driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(60));
	    js = (JavascriptExecutor) driver;
	  }


	  @Test
	  public void loginwithwrongcredentials() throws Exception {
	    driver.get("https://www.facebook.com/");
	    driver.findElement(By.id("email")).clear();
	    driver.findElement(By.id("email")).sendKeys("12345");
	    driver.findElement(By.id("pass")).click();
	    driver.findElement(By.id("pass")).clear();
	    driver.findElement(By.id("pass")).sendKeys("abcd");
	    driver.findElement(By.name("login")).click();
	    driver.get("https://www.facebook.com/login/?privacy_mutation_token=eyJ0eXBlIjowLCJjcmVhdGlvbl90aW1lIjoxNzMxNjMwMzg3LCJjYWxsc2l0ZV9pZCI6MzgxMjI5MDc5NTc1OTQ2fQ%3D%3D&next");
	    takeScreenShot("foto1") ;
	    pause(3000);
	  

	    //assertion for verify any error releated to password , account or email
        assertTrue("Account error message not displayed", driver.findElement(By.xpath("//*[contains(text(),'correo') or contains(text(),'cuenta') or contains(text(), 'contraseña')]")).isDisplayed());
        
  
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
	  private void pause(int milliseconds) {
	        try {
	            Thread.sleep(milliseconds); 
	        } catch (InterruptedException e) {
	            System.out.println("Pause interrupted: " + e.getMessage());
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
	  public void takeScreenShot(String fileName) 
	    	throws IOException{
	    	{
	    		File file = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
	    		FileUtils.copyFile(file,new File("maven/src/screenshots/"+fileName+".jpeg"));
	    	}
	  }
}