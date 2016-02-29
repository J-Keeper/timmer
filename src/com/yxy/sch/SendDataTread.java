package com.yxy.sch;

import net.sf.json.JSON;
import net.sf.json.JSONObject;

public class SendDataTread implements Runnable {
	
	
	
	var md5Key = "";
	
	var id = 0;
	
	var sendAt = null;
	
	var onMessageCallback;
	
	function defaultOnMessageCallback(output) {
		console(output);
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub

	}
	
	
	public void sendData(String param){
		String[] args =param.split("|");
		String serviceName =args[0];
		String jsons =JSONObject
		String 
	}
	
	
	function send(serviceName, input, callback) {
		var pkg = {"n":serviceName, "s":"1001", "p":input, "i":id};
		var json = JSON.stringify(pkg);
		var md5Value = md5(md5Key + json);
		pkg["m"] = md5Value;
		json = JSON.stringify(pkg);
		if(callback == null) {
			onMessageCallback = defaultOnMessageCallback;
		}else {
			onMessageCallback = callback;
		}
		console.log(json);
		ws.send(json);
	}

}
