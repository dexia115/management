package com.operate.tools;

public class JsonObj {
	
	private boolean success;
	
	private String message;
	
	private Object resultObject;
	
	public static JsonObj newSuccessJsonObj(String message){
		JsonObj jsonObj = new JsonObj();
		jsonObj.setSuccess(true);
		jsonObj.setMessage(message);
		return jsonObj;
	}
	
	public static JsonObj newSuccessJsonObj(String message,Object resultObject){
		JsonObj jsonObj = new JsonObj();
		jsonObj.setSuccess(true);
		jsonObj.setMessage(message);
		jsonObj.setResultObject(resultObject);
		return jsonObj;
	}
	
	public static JsonObj newErrorJsonObj(String message){
		JsonObj jsonObj = new JsonObj();
		jsonObj.setSuccess(false);
		jsonObj.setMessage(message);
		return jsonObj;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getResultObject() {
		return resultObject;
	}

	public void setResultObject(Object resultObject) {
		this.resultObject = resultObject;
	}

}
