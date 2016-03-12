package com.xyz.kjy.constant;


public class Constants {
	public static final String URL="http://119.29.159.154:8080";
	public static final String CustomersURI=URL+"/dispatchClient/getAll.json";
	public static final String LoginURI=URL+"/doLogin.json";
	public static final String ChangePwdURI=URL+"/user/doPasswordChange.json";
	public static final String DispatchInfoURI=URL+"/dispatch/getDispatchInfo.json";
	public static final String DispatchStartURI=URL+"/dispatch/doStart.json";
	public static final String DispatchOverURI=URL+"/dispatch/doEnd.json";
	public static final String LogoutURI=URL+"/doLogout.json";
	public static final String SendMessURI=URL+"/dispatchRecord/doAdd.json";
	public static final String AllCarURI=URL+"/car/getAll.json";
	
//	public static final String Cookie="cookie";
	public static final String UserIsLogin="isLogin";
	public static final String UserName="userName";
	
	public static final String DispatchIsDoing="isDispatching"; 
	public static final String DispatchCar="dispatchCar";
	public static final String DispatchStarttime="dispatchStarttime";
//	public static final String UserPass="password";
//	public static final String ResponseStatus="responseStatus";
//	public static final String ResponseData="responseData";
	public static final String BeingLogin="正在登录";
	public static final String BeingLogout="正在退出";
	public static final String BeingChangePwd="正在修改密码";
	public static final String BeingLoad="正在加载";
	public static final String BeingUpdateCustomer="正在更新商家信息";
	public static final String BeingStartDispatch="正在创建一次配送";
	public static final String BeingOverDispatch="正在结束该次配送";
	

	
}
