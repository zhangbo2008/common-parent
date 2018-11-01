package com.uetty.common.tool.core.json.gson;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class GsonUtil {

	public static String toJson(Object a) {
		return new Gson().toJson(a).toString();
	}
	
	public static <T> List<T> fromJsonArray(String jsonArr, Class<T> clz) {
		//Json的解析类对象
	    JsonParser parser = new JsonParser();
	    //将JSON的String 转成一个JsonArray对象
	    JsonArray jsonArray = parser.parse(jsonArr).getAsJsonArray();

	    Gson gson = new Gson();
	    List<T> list = new ArrayList<T>();
	    //加强for循环遍历JsonArray
	    for (JsonElement user : jsonArray) {
	        //使用GSON，直接转成Bean对象
	        T t = gson.fromJson(user, clz);
	        list.add(t);
	    }
	    return list;
	}
	
	public static <T> T fromJsonObject(String jsonObj, Class<T> clz) {
		return new Gson().fromJson(jsonObj, clz);
	}
}
