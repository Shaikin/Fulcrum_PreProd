package com.proj.objectRepository;

public class ObjRepository {

	// Login page objects	
	public static String textbox_login_UserName="logon:username:input";
	public static String textbox_login_Password="logon:userPassword:input";	
	public static String button_login_LoginBeforeCredentials=".//*[@class='realme_button_padding']";
	public static String button_login_Login="logon:logon";	
	
	//Homepage
	public static String homePage_element="inplaceSearchDiv_WPQ1_lsinput";
	
	//Overlays
	public static String heading_working=".//*[@title='Working on it...']";
	public static String overlay_working=".//*[text()='Working on it...']";

	//logout objects
	//public static String link_user="zz5_Menu";
	public static String menu_logout="zz5_Menu";
	public static String link_user="//*[@title='Open Menu']";
	public static String link_signOut="//*[@class='ms-core-menu-title' and text()='Sign Out']";

	//frame objects	
	public static String frame_single=".//iframe[contains(@id,'DlgFrame')]";	
	public static String frame_double="(.//iframe[contains(@id,'DlgFrame')])[2]";
	public static String frame_list_pattern="(.//iframe[contains(@id,'DlgFrame')])[framelist]";
	public static String frame_documentList=".//iframe[contains(@class,'doc-lib-view-frame')]";
	//choice objects
	public static String choice_select=".//*[@title='Add the highlighted item to this field']";
	public static String choice_ok=".//*[contains(@id,'OkButton')]";
	public static String choice_prvpage=".//*[@title='Previous Page']";
	
	
	//popup Objects
	
	//autosuggest text box item objects
	public static String js_autosuggest_input="/../input[2]";
	public static String js_autosuggest_items="/../div/ul/li/a/div";
	public static String js_dropdown_items="//div[@class='option']/div";

	//Grid Container
	public static String container_subMenu="//*[@class='ui popup inverted right center']";
	public static String container_transmittals="Transmittals";
	public static String container_documentRegister="Document Register";
	public static String container_transmittalFiles="transmittalFiles";
	public static String container_supportingDocumentFiles="supportingDocumentFiles";
	public static String grid_nextButton=".//*[@id='pagingWPQ2next']/a";
	public static String container_uploadedOrNewDocuments="Documents";
	
	//close
	public static String icon_close="//*[@title='Close dialog']";	
	//Cookies popup
	


	//session timeout objects



	//Icons

}
