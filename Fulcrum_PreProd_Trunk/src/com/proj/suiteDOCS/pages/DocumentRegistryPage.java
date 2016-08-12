package com.proj.suiteDOCS.pages;

import java.util.Hashtable;

import org.openqa.selenium.WebDriver;

import com.frw.util.PageLoadWaitUtil;
import com.frw.util.WaitUtil;
import com.proj.Constants.Constants_TimeOuts;
import com.proj.navigations.Navigations_Fulcrum;
import com.proj.suiteDOCS.TestSuiteBase;
import com.proj.suiteDOCS.reusables.DocumentRegisterGridUtil;
import com.proj.utilFulcrum.ToolbarsUtil;

public class DocumentRegistryPage extends TestSuiteBase{
	
	public static void selectADocument(WebDriver driver,String workflow,Hashtable<String,String>data) throws Throwable{
		Navigations_Fulcrum.navigateToDocumentsAndFileStorage(driver);
		DocumentRegisterGridUtil.searchDocumentAndTickRecord(driver_DOCS, workflow, data.get("AttachDocumentName"));		
	}

	
	public static void sendTransmittal(WebDriver driver,String workFlow) throws Throwable{
		try{
			PageLoadWaitUtil.waitForPageToLoad(driver);
		}catch (Exception ex){
			
		}		
		ToolbarsUtil.clickItems(driver, workFlow);
		WaitUtil.pause(Constants_TimeOuts.Save_TimeOut);
		ToolbarsUtil.Items.clickSendTranmittals(driver, workFlow);
	}
}
