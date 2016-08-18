package com.proj.suiteTRANSMITTALS.workflows;

import java.util.Hashtable;

import org.openqa.selenium.WebDriver;

import com.frw.Constants.Constants_FRMWRK;
import com.proj.Constants.Constants;
import com.proj.Constants.Constants_Workflow;
import com.proj.navigations.Navigations_Fulcrum;
import com.proj.suiteTRANSMITTALS.TestSuiteBase;
import com.proj.suiteTRANSMITTALS.pages.MyInboxAndActionRequiredPage_Fulcrum;
import com.proj.suiteTRANSMITTALS.pages.MySentPage;
import com.proj.suiteTRANSMITTALS.pages.Transmittals_EntryPage;
import com.proj.suiteTRANSMITTALS.reusables.TransmittalsGridUtil;
import com.proj.util.CustomExceptions;
import com.proj.utilFulcrum.ApplicationMethods;
import com.report.reporter.Reporting;

public class Workflows extends TestSuiteBase{
	static String getResult;

	/**
	 * Initiation of Transmittal
	 * @author shaikka
	 * @param driver
	 * @param url
	 * @param worflow_l1
	 * @param data
	 * @throws Throwable
	 */
	public static Hashtable<String,String> Level1_Initaite_Transmittal(WebDriver driver,String url,String worflow_l1,Hashtable<String,String>data) throws Throwable{
		Hashtable<String,String>transmittalData=new Hashtable<String,String>();
		Navigations_Fulcrum.Transmittals.navigateToNewTransmittal(driver);

		transmittalData=Transmittals_EntryPage.createAndSendTransmittalRecord(ApplicationMethods.getSiteName(url),driver, refID,testcaseName,worflow_l1,  data);
		System.out.println("Done.."+transmittalData);

		if (transmittalData.size()==0 || transmittalData.get("Tramsmittals-Subject").equals(Constants_FRMWRK.False)){
			logsObj.log("Due to above error in the test case"+testcaseName+" hence quiting ,cannot continue further steps of the testcase");
			CustomExceptions.Exit(testcaseName, "Failure during New Transmittal entry for the test case"+testcaseName, "Please refer the above error details for more information");
		}

		getResult=MySentPage.validate_TxComplete_StatusAndStatus(driver, worflow_l1, transmittalData);

		if(getResult.equalsIgnoreCase(Constants_FRMWRK.False)){
			CustomExceptions.Exit(testcaseName, worflow_l1+"- Failure", "Unable to continue the test due to above error ");
		}
		return transmittalData;
	}


	public static WebDriver Level2_Validate_And_DownloadFiles(String siteName,String validationPage,WebDriver driver,String refid,String testcasename,String workflow_l2,String condition,String workflow_end,String url,String browsername,String username2,String password2,Hashtable<String,String>transmittalData,Hashtable<String,String>testData,int userIteration ) throws Throwable{
		workflow_l2=workflow_l2+condition+" - validate & Download"+workflow_end;
		ApplicationMethods.logOutFromApplicationAndcloseBrowser(driver,refid,testcasename);
		driver=ApplicationMethods.launchBrowserAndlogIntoApplication(browsername, url, username2, password2,refID);
		MyInboxAndActionRequiredPage_Fulcrum.validate_TxComplete_StatusAndStatus(driver,validationPage, workflow_l2, transmittalData,testData);
		TransmittalsGridUtil.searchSubjectAndOpenRecord(driver,validationPage, workflow_l2+"-Download", transmittalData.get("Tramsmittals-Subject"));
		Transmittals_EntryPage.verifyAttachedFilesAndClick(siteName,driver,testcasename,refid,workflow_l2,testData);
		return driver;		
	}

	public static WebDriver Level2_Validate_OR_Submit_OR_ApproveOrReject_OR_Forward_OR_ReplyAll_Transmittal(String siteName,String validationPage,WebDriver driver,String refid,String testcasename,String workflow_l2,String condition,String workflow_end,String url,String browsername,String username2,String password2,Hashtable<String,String>transmittalData,Hashtable<String,String>testData,int userIteration ) throws Throwable{
		transmittalData.put("Tramsmittals-Level2-Reciever", String.valueOf(userIteration));
		if(testData.get(Constants_Workflow.Fulcrum_WorkFlow_Data_Condition).equalsIgnoreCase(Constants_Workflow.Fulcrum_WorkFlow_Corresponce)){
			workflow_l2=workflow_l2+condition+" & validate"+workflow_end;
			ApplicationMethods.logOutFromApplicationAndcloseBrowser(driver,refid,testcasename);

			driver=ApplicationMethods.launchBrowserAndlogIntoApplication(browsername, url, username2, password2,refID);

			getResult=MyInboxAndActionRequiredPage_Fulcrum.validate_TxComplete_StatusAndStatus(driver,validationPage, workflow_l2, transmittalData,testData);
			if(getResult.equalsIgnoreCase(Constants_FRMWRK.False)){
				CustomExceptions.Exit(testcaseName, workflow_l2+"- Failure", "Unable to continue the test due to above error ");
			}
			MyInboxAndActionRequiredPage_Fulcrum.validate_TransmittalID(driver, validationPage, workflow_l2, transmittalData);
		}


		else if ((!testData.get(Constants_Workflow.Fulcrum_WorkFlow_Data_Condition).equalsIgnoreCase(Constants_Workflow.Fulcrum_WorkFlow_Corresponce)) && testData.get("To").contains(Constants.delimiter_data)&&(!transmittalData.get("Tramsmittals-Level2-Reciever").equalsIgnoreCase(Constants_Workflow.level2_reciever_first_count) && testData.get("Action-Level2").equals("Rejected"))){
			workflow_l2=workflow_l2+condition+" & "+testData.get("Action-Level2")+workflow_end;
			ApplicationMethods.logOutFromApplicationAndcloseBrowser(driver,refid,testcasename);

			driver=ApplicationMethods.launchBrowserAndlogIntoApplication(browserName, url, username2, password2,refID);

			getResult=MyInboxAndActionRequiredPage_Fulcrum.validate_TxComplete_StatusAndStatus(driver,validationPage, workflow_l2, transmittalData,testData);
		}
		else if(!testData.get(Constants_Workflow.Fulcrum_WorkFlow_Data_Condition).equalsIgnoreCase(Constants_Workflow.Fulcrum_WorkFlow_Corresponce) && (testData.get("Action-Level2").equals("Approved")|| testData.get("Action-Level2").equals("Rejected"))){
			String actionRequiredCount_before="";
			String actionRequiredCount_after;
			workflow_l2=workflow_l2+condition+" & "+testData.get("Action-Level2")+workflow_end;
			ApplicationMethods.logOutFromApplicationAndcloseBrowser(driver,refid,testcasename);

			driver=ApplicationMethods.launchBrowserAndlogIntoApplication(browserName, url, username2, password2,refID);
			if(validationPage.equalsIgnoreCase(Constants_Workflow.page_actionRequired)){
				actionRequiredCount_before=Navigations_Fulcrum.navigateToActionRequiredAndGetCount(driver, refID, testcaseName, workflow_l2);	
			}
			
			
			
			MyInboxAndActionRequiredPage_Fulcrum.validate_TxComplete_StatusAndStatus(driver,validationPage, workflow_l2, transmittalData,testData);

			Transmittals_EntryPage.verifyAttachedFiles(siteName,driver, testcasename, refID, workflow_l2, testData);

			Transmittals_EntryPage.clickCompleteAction(driver, workflow_l2);
			Transmittals_EntryPage.editAndSubmitTransmittalRecord(ApplicationMethods.getSiteName(url),driver, refID, testcaseName, workflow_l2, transmittalData,testData.get("Action-Level2"));

			validateRecordinActionRequiredPageAfterSubmission(driver, validationPage, workflow_l2, transmittalData.get("Tramsmittals-Subject"));

			getResult=MyInboxAndActionRequiredPage_Fulcrum.validate_TxComplete_StatusAndStatus(driver,Constants_Workflow.page_myInbox, workflow_l2, transmittalData,testData.get("Action-Level2"));
			if(getResult.equalsIgnoreCase(Constants_FRMWRK.False)){
				CustomExceptions.Exit(testcaseName, workflow_l2+"- Failure", "Unable to continue the test due to above error ");
			}
			if(validationPage.equalsIgnoreCase(Constants_Workflow.page_actionRequired)){
				actionRequiredCount_after=Navigations_Fulcrum.navigateToActionRequiredAndGetCount(driver, refID, testcaseName, workflow_l2);
				MyInboxAndActionRequiredPage_Fulcrum.validateActionRequiredCount(driver, refid, testcasename, workflow_l2, actionRequiredCount_before, actionRequiredCount_after);
			}
			
		}
		else if((!testData.get(Constants_Workflow.Fulcrum_WorkFlow_Data_Condition).equalsIgnoreCase(Constants_Workflow.Fulcrum_WorkFlow_Corresponce)) && (testData.get("Action-Level2").equals("Forward"))){
			workflow_l2=workflow_l2+" "+condition+" & "+testData.get("Action-Level2")+workflow_end;
			ApplicationMethods.logOutFromApplicationAndcloseBrowser(driver,refid,testcasename);

			driver=ApplicationMethods.launchBrowserAndlogIntoApplication(browserName, url, username2, password2,refID);

			MyInboxAndActionRequiredPage_Fulcrum.validate_TxComplete_StatusAndStatus(driver,validationPage, workflow_l2, transmittalData,testData);

			Transmittals_EntryPage.verifyAttachedFiles(siteName,driver, testcasename, refID, workflow_l2, testData);

			Transmittals_EntryPage.forwardAndSendTransmittalRecord(ApplicationMethods.getSiteName(url),driver,testcasename, workflow_l2, testData);

			validateRecordinActionRequiredPageAfterSubmission(driver, validationPage, workflow_l2, transmittalData.get("Tramsmittals-Subject"));

			getResult=MyInboxAndActionRequiredPage_Fulcrum.validate_TxComplete_StatusAndStatus(driver,Constants_Workflow.page_myInbox, workflow_l2, transmittalData,testData.get("Action-Level2"));
			if(getResult.equalsIgnoreCase(Constants_FRMWRK.False)){
				CustomExceptions.Exit(testcaseName, workflow_l2+"- Failure", "Unable to continue the test due to above error ");
			}
			
		}
		else if((!testData.get(Constants_Workflow.Fulcrum_WorkFlow_Data_Condition).equalsIgnoreCase(Constants_Workflow.Fulcrum_WorkFlow_Corresponce)) && (testData.get("Action-Level2").equals("ReplyAll"))){
			workflow_l2=workflow_l2+" "+condition+" & "+testData.get("Action-Level2")+workflow_end;
			ApplicationMethods.logOutFromApplicationAndcloseBrowser(driver,refid,testcasename);

			driver=ApplicationMethods.launchBrowserAndlogIntoApplication(browserName, url, username2, password2,refID);

			MyInboxAndActionRequiredPage_Fulcrum.validate_TxComplete_StatusAndStatus(driver,validationPage, workflow_l2, transmittalData,testData);

			//Transmittals_EntryPage.verifyAttachedFiles(siteName,driver, workflow_l2, refID, workflow_l2, testData);

			Transmittals_EntryPage.replyAllAndSendTransmittalRecord(ApplicationMethods.getSiteName(url),driver, workflow_l2, testData);

			validateRecordinActionRequiredPageAfterSubmission(driver, validationPage, workflow_l2, transmittalData.get("Tramsmittals-Subject"));

			getResult=MyInboxAndActionRequiredPage_Fulcrum.validate_TxComplete_StatusAndStatus(driver,Constants_Workflow.page_myInbox, workflow_l2, transmittalData,testData.get("Action-Level2"));
			if(getResult.equalsIgnoreCase(Constants_FRMWRK.False)){
				CustomExceptions.Exit(testcaseName, workflow_l2+"- Failure", "Unable to continue the test due to above error ");
			}
		}
		//Delegate

		else if((!testData.get(Constants_Workflow.Fulcrum_WorkFlow_Data_Condition).equalsIgnoreCase(Constants_Workflow.Fulcrum_WorkFlow_Corresponce)) && (testData.get("Action-Level2").equals("Delegate"))){
			workflow_l2=workflow_l2+" "+condition+" & "+testData.get("Action-Level2")+workflow_end;
			ApplicationMethods.logOutFromApplicationAndcloseBrowser(driver,refid,testcasename);

			driver=ApplicationMethods.launchBrowserAndlogIntoApplication(browserName, url, username2, password2,refID);

			MyInboxAndActionRequiredPage_Fulcrum.validate_TxComplete_StatusAndStatus(driver,validationPage, workflow_l2, transmittalData,testData);
			Transmittals_EntryPage.clickCompleteAction(driver, workflow_l2);

			//Transmittals_EntryPage.verifyAttachedFiles(siteName,driver, workflow_l2, refID, workflow_l2, testData);

			Transmittals_EntryPage.delegateAndSendTransmittalRecord(ApplicationMethods.getSiteName(url),driver,testcasename, workflow_l2, testData);
			Transmittals_EntryPage.editAndSubmitTransmittalRecord(ApplicationMethods.getSiteName(url),driver, refid, testcasename, workflow_l2, transmittalData,testData.get("Action-Level2"));

			validateRecordinActionRequiredPageAfterSubmission(driver, validationPage, workflow_l2, transmittalData.get("Tramsmittals-Subject"));

			getResult=MyInboxAndActionRequiredPage_Fulcrum.validate_TxComplete_StatusAndStatus(driver,Constants_Workflow.page_myInbox, workflow_l2, transmittalData,testData.get("Action-Level2"));
			if(getResult.equalsIgnoreCase(Constants_FRMWRK.False)){
				CustomExceptions.Exit(testcaseName, workflow_l2+"- Failure", "Unable to continue the test due to above error ");
			}

		}
		else if(testData.get(Constants_Workflow.Fulcrum_WorkFlow_Data_Condition).equalsIgnoreCase(Constants_Workflow.Fulcrum_WorkFlow_ConsultantAdvice)||testData.get(Constants_Workflow.Fulcrum_WorkFlow_Data_Condition).equalsIgnoreCase(Constants_Workflow.Fulcrum_WorkFlow_ChangeNote)){
			workflow_l2=workflow_l2+condition+" & Submit"+workflow_end;
			ApplicationMethods.logOutFromApplicationAndcloseBrowser(driver,refid,testcasename);

			driver=ApplicationMethods.launchBrowserAndlogIntoApplication(browserName, url, username2, password2,refID);
			
			getResult=MyInboxAndActionRequiredPage_Fulcrum.validate_TxComplete_StatusAndStatus(driver, validationPage,workflow_l2, transmittalData,testData);
			if(getResult.equalsIgnoreCase(Constants_FRMWRK.False)){
				CustomExceptions.Exit(testcaseName, workflow_l2+"- Failure", "Unable to continue the test due to above error ");
			}
			Transmittals_EntryPage.verifyAttachedFiles(siteName,driver, testcasename, refID, workflow_l2, testData);

			Transmittals_EntryPage.clickCompleteAction(driver, workflow_l2);
			Transmittals_EntryPage.editAndSubmitTransmittalRecord(ApplicationMethods.getSiteName(url),driver, refID, testcaseName, workflow_l2, transmittalData,testData.get("Action-Level2"));

			validateRecordinActionRequiredPageAfterSubmission(driver, validationPage, workflow_l2, transmittalData.get("Tramsmittals-Subject"));

			getResult=MyInboxAndActionRequiredPage_Fulcrum.validate_TxComplete_StatusAndStatus(driver,Constants_Workflow.page_myInbox, workflow_l2, transmittalData,testData.get("Action-Level2"));
			if(getResult.equalsIgnoreCase(Constants_FRMWRK.False)){
				CustomExceptions.Exit(testcaseName, workflow_l2+"- Failure", "Unable to continue the test due to above error ");
			}			

		}
		//Overdue

		else if(!testData.get(Constants_Workflow.Fulcrum_WorkFlow_Data_Condition).equalsIgnoreCase(Constants_Workflow.Fulcrum_WorkFlow_Corresponce) && testData.get("Action-Level2").equals("Overdue")){
			workflow_l2=workflow_l2+condition+" & Submit"+workflow_end;
			ApplicationMethods.logOutFromApplicationAndcloseBrowser(driver,refid,testcasename);

			driver=ApplicationMethods.launchBrowserAndlogIntoApplication(browserName, url, username2, password2,refID);
			getResult=MyInboxAndActionRequiredPage_Fulcrum.validate_TxComplete_StatusAndStatus(driver, validationPage,workflow_l2, transmittalData,testData);
			if(getResult.equalsIgnoreCase(Constants_FRMWRK.False)){
				CustomExceptions.Exit(testcaseName, workflow_l2+"- Failure", "Unable to continue the test due to above error ");
			}

			Transmittals_EntryPage.verifyAttachedFiles(siteName,driver, testcasename, refID, workflow_l2, testData);

			Transmittals_EntryPage.clickCompleteAction(driver, workflow_l2);
			Transmittals_EntryPage.editAndSubmitTransmittalRecord(ApplicationMethods.getSiteName(url),driver, refID, testcaseName, workflow_l2, transmittalData,testData.get("Action-Level2"));

			validateRecordinActionRequiredPageAfterSubmission(driver, validationPage, workflow_l2, transmittalData.get("Tramsmittals-Subject"));

			getResult=MyInboxAndActionRequiredPage_Fulcrum.validate_TxComplete_StatusAndStatus(driver,Constants_Workflow.page_myInbox, workflow_l2, transmittalData,testData.get("Action-Level2"));
			if(getResult.equalsIgnoreCase(Constants_FRMWRK.False)){
				CustomExceptions.Exit(testcaseName, workflow_l2+"- Failure", "Unable to continue the test due to above error ");
			}

		}




		return driver;
	}
	/*
	public static WebDriver Level3_ValidateForwarded_OR_ValidateReplyAll_And_ApproveOrReject_Transmittal(String siteName,String validationPage,WebDriver driver,String refid,String testcasename,String workflow_l3,String condition,String workflow_end,String url,String browsername,String username1,String password1,Hashtable<String,String>transmittalData,Hashtable<String,String>testData ) throws Throwable{

		if(!testData.get("Action-Level2").isEmpty()&& testData.get("Action-Level2").equalsIgnoreCase("Forward")||testData.get("Action-Level2").equalsIgnoreCase("Delegate")){
			workflow_l3=workflow_l3+condition+" & Submit"+workflow_end;
			ApplicationMethods.logOutFromApplicationAndcloseBrowser(driver,refid,testcasename);
			driver=ApplicationMethods.launchBrowserAndlogIntoApplication(browsername, url, username1, password1,refID);
			String subj=transmittalData.get("Tramsmittals-Subject");
			if (testData.get("Action-Level2").equalsIgnoreCase("Forward")){
				subj="FW: "+subj;
			}
			transmittalData.put("Tramsmittals-Subject", subj);
			MyInboxAndActionRequiredPage_FluidTx.validate_TxComplete_StatusAndStatus(driver,validationPage ,workflow_l3, transmittalData,testData);
			Transmittals_EntryPage.verifyAttachedFiles(siteName,driver, testcasename, refID, workflow_l3, testData);

			Transmittals_EntryPage.switchToTramsmittalEditFrame(driver, refID, testcasename, workflow_l3);
			Transmittals_EntryPage.clickCompleteAction(driver, workflow_l3);
			Transmittals_EntryPage.editAndSubmitTransmittalRecord(ApplicationMethods.getSiteName(url),driver, refID, testcaseName, workflow_l3, transmittalData,testData.get("Action-Level3"));
			getResult=MyInboxAndActionRequiredPage_FluidTx.validate_TxComplete_StatusAndStatus(driver, Constants_Workflow.page_myInbox,workflow_l3, transmittalData,testData.get("Action-Level3"));
			if(getResult.equalsIgnoreCase(Constants_FRMWRK.False)){
				CustomExceptions.Exit(testcaseName, workflow_l3+"- Failure", "Unable to continue the test due to above error ");
			}

		}
		else if(!testData.get("Action-Level2").isEmpty()&& testData.get("Action-Level2").equalsIgnoreCase("ReplyAll")){
			workflow_l3=workflow_l3+condition+" & Submit"+workflow_end;
			ApplicationMethods.logOutFromApplicationAndcloseBrowser(driver,refid,testcasename);
			driver=ApplicationMethods.launchBrowserAndlogIntoApplication(browsername, url, username1, password1,refID);
			String subj=transmittalData.get("Tramsmittals-Subject");
			subj="RE: "+subj;
			transmittalData.put("Tramsmittals-Subject", subj);
			MyInboxAndActionRequiredPage_FluidTx.validate_TxComplete_StatusAndStatus(driver,validationPage ,workflow_l3, transmittalData,testData);
			//Transmittals_EntryPage.verifyAttachedFiles(siteName,driver, workflow_l3, refID, workflow_l3, testData);

			Transmittals_EntryPage.switchToTramsmittalEditFrame(driver, refID, testcaseName, workflow_l3);
			Transmittals_EntryPage.clickCompleteAction(driver, workflow_l3);
			Transmittals_EntryPage.editAndSubmitTransmittalRecord(ApplicationMethods.getSiteName(url),driver, refID, testcaseName, workflow_l3, transmittalData,testData.get("Action-Level3"));
			getResult=MyInboxAndActionRequiredPage_FluidTx.validate_TxComplete_StatusAndStatus(driver, Constants_Workflow.page_myInbox,workflow_l3, transmittalData,testData.get("Action-Level3"));
			if(getResult.equalsIgnoreCase(Constants_FRMWRK.False)){
				CustomExceptions.Exit(testcaseName, workflow_l3+"- Failure", "Unable to continue the test due to above error ");
			}

		}
		return driver;
	}
*/


	/**
	 * Initiation of Transmittal from Document Register
	 * @author shaikka
	 * @param driver
	 * @param url
	 * @param worflow_l1
	 * @param data
	 * @throws Throwable
	 */
/*	public static Hashtable<String,String> Level1_Initaite_Transmittal_FromDocumentRegister(WebDriver driver,String url,String worflow_l1,Hashtable<String,String>data) throws Throwable{

		Hashtable<String,String>transmittalData=new Hashtable<String,String>();
		DocumentRegistryPage.selectADocument(driver, worflow_l1, data);
		WaitUtil.pause(Constants_TimeOuts.Save_TimeOut);
		ApplicationMethods.waitForOverlayToDisappear(driver);		
		DocumentRegistryPage.sendTransmittal(driver, worflow_l1);
		WaitUtil.pause(Constants_TimeOuts.Save_TimeOut);
		ApplicationMethods.waitForOverlayToDisappear(driver);		
		Transmittals_EntryPage.attachdocument(ApplicationMethods.getSiteName(url),driver, refID,testcaseName, worflow_l1, data);

		transmittalData=Transmittals_EntryPage.createAndSendTransmittalRecord(ApplicationMethods.getSiteName(url),driver, refID,testcaseName,worflow_l1,  data);
		System.out.println("Done.."+transmittalData);

		if (transmittalData.size()==0 || transmittalData.get("Tramsmittals-Subject").equals(Constants_FRMWRK.False)){
			logsObj.log("Due to above error in the test case"+testcaseName+" hence quiting ,cannot continue further steps of the testcase");
			CustomExceptions.Exit(testcaseName, "Failure during New Transmittal entry for the test case"+testcaseName, "Please refer the above error details for more information");
		}

		getResult=MySentPage.validate_TxComplete_StatusAndStatus(driver, worflow_l1, transmittalData);

		if(getResult.equalsIgnoreCase(Constants_FRMWRK.False)){
			CustomExceptions.Exit(testcaseName, worflow_l1+"- Failure", "Unable to continue the test due to above error ");
		}
		return transmittalData;
	}
*/

	private static void validateRecordinActionRequiredPageAfterSubmission(WebDriver driver,String validationPage,String workflow,String subject) throws Throwable{
		if(validationPage.equalsIgnoreCase(Constants_Workflow.page_actionRequired)||(validationPage.equalsIgnoreCase(Constants_Workflow.page_actionsOverdue))) {
			if (validationPage.equalsIgnoreCase(Constants_Workflow.page_actionRequired))
			{
				Navigations_Fulcrum.navigateToActionRequired(driver);
			}
			else
			{
				Navigations_Fulcrum.navigateToActionsOverdue(driver);
			}
			getResult=TransmittalsGridUtil.searchSubject(driver, validationPage, workflow, subject);
			if(getResult.equalsIgnoreCase(Constants_FRMWRK.False)){
				Reporting.logStep(driver, workflow+"- Verification of Action performed record in Action Required page", "The record "+subject+" is not listed", Constants_FRMWRK.Pass);
			}
			else if (getResult.equalsIgnoreCase(Constants_FRMWRK.True)){
				Reporting.logStep(driver, workflow+"- Verification of Submission record in Action Required page", "The record "+subject+" is listed", Constants_FRMWRK.Fail);
			}
			else{
				Reporting.logStep(driver, workflow+"- Verification of Submission record in Action Required page", "Coulde not validate The record "+subject+" listed due to above error", Constants_FRMWRK.Fail);
			}
		}
	}


/*
	public static WebDriver Level2_Close_Cancel_Transmittal(String siteName,String validationPage,WebDriver driver,String refid,String testcasename,String workflow_l2,String condition,String workflow_end,String url,String browsername,String username2,String password2,Hashtable<String,String>transmittalData,Hashtable<String,String>testData ) throws Throwable{

		if(testData.get(Constants_Workflow.FluidTX_WorkFlow_Data_Condition).equalsIgnoreCase(Constants_Workflow.FluidTX_WorkFlow_IssuedForReview)||testData.get(Constants_Workflow.FluidTX_WorkFlow_Data_Condition).equalsIgnoreCase(Constants_Workflow.FluidTX_WorkFlow_RequestForInformation)){
			workflow_l2=workflow_l2+condition+workflow_end;
			ApplicationMethods.logOutFromApplicationAndcloseBrowser(driver,refid,testcasename);

			driver=ApplicationMethods.launchBrowserAndlogIntoApplication(browserName, url, username2, password2,refID);

			MyInboxAndActionRequiredPage_FluidTx.validate_TxComplete_StatusAndStatus(driver, validationPage,workflow_l2, transmittalData,testData);

			Transmittals_EntryPage.verifyAttachedFiles(siteName,driver, testcasename, refID, workflow_l2, testData);

			if(testData.get("RecieverRole").equalsIgnoreCase(Constants_Workflow.role_admin)){
				if (testData.get("Action-Level2").equalsIgnoreCase("CANCEL")){
					getResult=Transmittals_EntryPage.checkCancelIsEnabled(driver, workflow_l2, refID, testcaseName);
					if(getResult.equalsIgnoreCase(Constants_FRMWRK.True)){
						Transmittals_EntryPage.clickToolbarCancel(driver, workflow_l2);
						getResult=PopupUtil.validatePopup(driver, refid, testcasename, workflow_l2, Constants_Messages.transmittal_cancel, Constants.ok);
						if(getResult.equalsIgnoreCase(Constants_FRMWRK.False)){
							CustomExceptions.Exit(testcasename, workflow_l2+" Cancel popup Failure", "Due to above failure cannot continue further execution..");
						}
						else{
							ApplicationMethods.closeAllDialogs(driver, refid, testcasename);
						}
					}
				}
				else if (testData.get("Action-Level2").equalsIgnoreCase("CLOSE")){
					getResult=Transmittals_EntryPage.checkCloseTransmittalIsEnabled(driver, workflow_l2, refID, testcaseName);
					if(getResult.equalsIgnoreCase(Constants_FRMWRK.True)){
						Transmittals_EntryPage.clickCloseTransmittal(driver, workflow_l2);
						getResult=PopupUtil.validatePopup(driver, refid, testcasename, workflow_l2, Constants_Messages.transmittal_closeTransmittal, Constants.ok);
						if(getResult.equalsIgnoreCase(Constants_FRMWRK.False)){
							CustomExceptions.Exit(testcasename, workflow_l2+" Close Transmittal popup Failure", "Due to above failure cannot continue further execution..");
						}
						else{
							ApplicationMethods.closeAllDialogs(driver, refid, testcasename);
						}
					}
				}
				validateRecordinActionRequiredPageAfterSubmission(driver, validationPage, workflow_l2, transmittalData.get("Tramsmittals-Subject"));

				getResult=MyInboxAndActionRequiredPage_FluidTx.validate_CaC_TxComplete_StatusAndStatus(driver, Constants_Workflow.page_myInbox, workflow_l2, transmittalData, testData.get("Action-Level2"));
				if(getResult.equalsIgnoreCase(Constants_FRMWRK.False)){
					CustomExceptions.Exit(testcaseName, workflow_l2+"- Failure", "Unable to continue the test due to above error ");
				}
			}
			else{
				if (testData.get("Action-Level2").equalsIgnoreCase("CANCEL")){
					getResult=Transmittals_EntryPage.checkCancelIsDisabled(driver, workflow_l2, refID, testcaseName);
				}
				else if (testData.get("Action-Level2").equalsIgnoreCase("CLOSE")){
					getResult=Transmittals_EntryPage.checkCloseTransmittalIsDisabled(driver, workflow_l2, refID, testcaseName);
				}

				ApplicationMethods.closeAllDialogs(driver, refid, testcasename);

			}



		}
		else if(testData.get(Constants_Workflow.FluidTX_WorkFlow_Data_Condition).equalsIgnoreCase(Constants_Workflow.FluidTX_WorkFlow_IssuedForApproval)){
			workflow_l2=workflow_l2+condition+" & "+testData.get("Action-Level2")+workflow_end;
			ApplicationMethods.logOutFromApplicationAndcloseBrowser(driver,refid,testcasename);

			driver=ApplicationMethods.launchBrowserAndlogIntoApplication(browserName, url, username2, password2,refID);

			MyInboxAndActionRequiredPage_FluidTx.validate_TxComplete_StatusAndStatus(driver,validationPage, workflow_l2, transmittalData,testData);

			Transmittals_EntryPage.verifyAttachedFiles(siteName,driver, testcasename, refID, workflow_l2, testData);

			if(testData.get("RecieverRole").equalsIgnoreCase(Constants_Workflow.role_admin)){
				if (testData.get("Action-Level2").equalsIgnoreCase("CANCEL")){
					getResult=Transmittals_EntryPage.checkCancelIsEnabled(driver, workflow_l2, refID, testcaseName);
					if(getResult.equalsIgnoreCase(Constants_FRMWRK.True)){
						Transmittals_EntryPage.clickToolbarCancel(driver, workflow_l2);
						getResult=PopupUtil.validatePopup(driver, refid, testcasename, workflow_l2, Constants_Messages.transmittal_cancel, Constants.ok);
						if(getResult.equalsIgnoreCase(Constants_FRMWRK.False)){
							CustomExceptions.Exit(testcasename, workflow_l2+" Cancel popup Failure", "Due to above failure cannot continue further execution..");
						}
						else{
							ApplicationMethods.closeAllDialogs(driver, refid, testcasename);
						}
						validateRecordinActionRequiredPageAfterSubmission(driver, validationPage, workflow_l2, transmittalData.get("Tramsmittals-Subject"));

						getResult=MyInboxAndActionRequiredPage_FluidTx.validate_CaC_TxComplete_StatusAndStatus(driver, Constants_Workflow.page_myInbox, workflow_l2, transmittalData, testData.get("Action-Level2"));
						if(getResult.equalsIgnoreCase(Constants_FRMWRK.False)){
							CustomExceptions.Exit(testcaseName, workflow_l2+"- Failure", "Unable to continue the test due to above error ");
						}
					}
				}
				else if (testData.get("Action-Level2").equalsIgnoreCase("CLOSE")){

					getResult=Transmittals_EntryPage.checkCloseTransmittalIsDisabled(driver, workflow_l2, refid, testcasename);
					if(getResult.equalsIgnoreCase(Constants_FRMWRK.False)){
						ApplicationMethods.closeAllDialogs(driver, refid, testcasename);
						CustomExceptions.Exit(testcasename, workflow_l2+" Close Transmittal popup Failure", "Due to above failure cannot continue further execution..");
					}else{
						ApplicationMethods.closeAllDialogs(driver, refid, testcasename);
					}
				}

			}

			else{
				if (testData.get("Action-Level2").equalsIgnoreCase("CANCEL")){
					getResult=Transmittals_EntryPage.checkCancelIsDisabled(driver, workflow_l2, refid, testcasename);
				}
				else if (testData.get("Action-Level2").equalsIgnoreCase("CLOSE")){
					getResult=Transmittals_EntryPage.checkCloseTransmittalIsDisabled(driver, workflow_l2, refid, testcasename);
				}

				ApplicationMethods.closeAllDialogs(driver, refid, testcasename);

			}		

		}

		return driver;
	}*/
}
