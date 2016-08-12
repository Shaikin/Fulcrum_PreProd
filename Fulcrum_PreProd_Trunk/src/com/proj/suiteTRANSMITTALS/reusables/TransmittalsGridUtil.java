package com.proj.suiteTRANSMITTALS.reusables;

import org.openqa.selenium.WebDriver;

import com.proj.objectRepository.ObjRepository;
import com.proj.suiteTRANSMITTALS.TestSuiteBase;
import com.proj.suiteTRANSMITTALS.pages.Transmittals_EntryPage;
import com.proj.utilFulcrum.WebTableUtil;

public class TransmittalsGridUtil extends TestSuiteBase{


	static String res="";
	static String input="";
	static WebDriver dr; //Remove this after getting new logStep method


	/**
	 * Searches for the subject record in the grid and tick the record 
	 * @author Shaik
	 * @param driver
	 * @param workflow
	 * @param subject
	 * @throws Throwable 
	 */
	public static String searchSubjectAndTickRecord(WebDriver driver,String workflow,String subject) throws Throwable{
		return WebTableUtil.searchforDataInsearchColumnAndTickInactionableColumn(driver, testcaseName, workflow+" My Sent - Subject", ObjRepository.container_transmittals, subject, 4, 1);

	}
	/**
	 * Search the record with Subject and Open
	 * @author shaikka
	 * @param driver
	 * @param page
	 * @param workflow
	 * @param subject
	 * @return
	 * @throws Throwable 
	 */
	public static String searchSubjectAndOpenRecord(WebDriver driver,String page,String workflow,String subject) throws Throwable{
		String flag="";
		flag=WebTableUtil.searchforDataInsearchColumnAndClickInactionableLinkColumn(driver, testcaseName, workflow+" "+page+" - Subject", ObjRepository.container_transmittals, subject,"Link",subject, 4, 4);
		Transmittals_EntryPage.waitInvisiblilityofWorkingTitle(driver);
		return flag;
	}
	/**
	 * Searches for the subject record in the grid and validates the  TxComplete_Status of the record 
	 * @author shaik
	 * @param driver
	 * @param workflow
	 * @param subject
	 * @param TxComplete_Status
	 * @throws Throwable 
	 */
	public static String searchSubjectAndCheck_TxComplete_Status(WebDriver driver,String page,String workflow,String subject,String TxComplete_Status) throws Throwable{
		return WebTableUtil.searchforDataInsearchColumnAndValidateDataInactionableColumn(driver, testcaseName, workflow+" "+page+" - TxComplete_Status", ObjRepository.container_transmittals, subject, TxComplete_Status, 4, 13);
	}
	/**
	 * Searches for the subject record in the grid and validates the status of the record 
	 * @author shaik
	 * @param driver
	 * @param workflow
	 * @param subject
	 * @param status
	 * @throws Throwable 
	 */
	public static String searchSubjectAndCheck_Status(WebDriver driver,String page,String workflow,String subject,String status) throws Throwable{
		return WebTableUtil.searchforDataInsearchColumnAndValidateDataInactionableColumn(driver, testcaseName, workflow+" "+page+" - Status", ObjRepository.container_transmittals, subject, status, 4, 14);
	}

	public static String searchSubject(WebDriver driver,String page,String workflow,String subject) throws Throwable{
		return WebTableUtil.searchforDataInsearchColumn_WOR(driver, testcaseName, workflow+" "+page+" - Subject", ObjRepository.container_transmittals, subject, 4);
		
	}
	public static String searchSubjectAndGetTransmittalID(WebDriver driver,String page,String workflow,String subject) throws Throwable{
		return WebTableUtil.searchforDataInsearchColumnAndFetchDataInactionableColumn_WOR(driver, testcaseName, workflow+" "+page+" - Transmittal ID", ObjRepository.container_transmittals, subject, "text", 4, 3);
		 
	}
	

}
