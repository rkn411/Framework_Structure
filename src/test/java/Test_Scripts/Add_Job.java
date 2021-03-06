package Test_Scripts;

import java.io.FileOutputStream;
import java.io.IOException;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import com.aventstack.extentreports.ExtentTest;

import Locators.All_Locators;
import multiScreenShot.MultiScreenShot;

public class Add_Job {

	public static void AddJob(WebDriver driver, XSSFSheet Sfile, XSSFCell cell, ExtentTest setup, String FilePath, XSSFWorkbook WBfile, MultiScreenShot multiScreens) throws InterruptedException, IOException {

		All_Locators.AdminTab(driver).click();
		All_Locators.JobTab(driver).click();
		All_Locators.JobTitlesTab(driver).click();
		Thread.sleep(10000);

		//Looping in the excel for data 
		for (int i=0; i<=Sfile.getLastRowNum(); i++) {
			try {
				All_Locators.AddJob(driver).click();
				setup.info("Adding Job");
				Thread.sleep(5000);
			} catch (Exception e) {
				multiScreens.multiScreenShot(driver);
			}


			//Import & send data for job title from excel
			All_Locators.JobTitle(driver).clear();
			cell = Sfile.getRow(i).getCell(0);
			cell.setCellType(CellType.STRING);
			All_Locators.JobTitle(driver).sendKeys(cell.getStringCellValue());


			//Verifying for duplicate job title
			String AlertMessagBlock = driver.findElement(By.className("help-block")).getText();
			String AlertMessg = "Already exists";
			String message;

			if (AlertMessagBlock.equalsIgnoreCase(AlertMessg)) {
				message = "Job Title Already exit";
				multiScreens.multiScreenShot(driver);
				setup.fail("Job Title Already exit");			
				All_Locators.CancelJob(driver).click();
				Thread.sleep(5000);
			}else {
				//Import & send data for job description from excel
				All_Locators.JobDescription(driver).clear();
				cell = Sfile.getRow(i).getCell(1);
				cell.setCellType(CellType.STRING);
				All_Locators.JobDescription(driver).sendKeys(cell.getStringCellValue());

				//Submit job
				All_Locators.SaveJob(driver).click();
				message = "Job Submitted";
				setup.pass("Job Submitted");			
				Thread.sleep(5000);
			}

			// Create cell in excel where message needs to be written.
			Sfile.getRow(i).createCell(2).setCellValue(message);

			// Specify the file in which message needs to be written.
			FileOutputStream fileOutput = new FileOutputStream(FilePath);

			//write message into excel
			WBfile.write(fileOutput);
			Thread.sleep(5000);
		}
		//End For Loop
	}
}
