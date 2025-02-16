package com.eggmeonina.scrumble.common.util;

import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import lombok.experimental.UtilityClass;

/**
 * json 타입의 String을 json object로 변환한다.
 * NotificationMessage의 placeholder와 json object의 key 값을 매칭하여 치환한다.
 *
 * ex)
 * json    : {"userName":"테스트 유저","squadName":"스크럼블"}
 * message : {userName}님이 {squadName} 스쿼드에 참여하였습니다
 * result  : 테스트 유저님이 스크럼블 스쿼드에 참여하였습니다
 */
@UtilityClass
public class JsonObjectConverter {

	private Gson gson = new Gson();

	/**
	 * object를 json 데이터 형태를 가진 String으로 변경한다.
	 * @param o
	 * @return
	 */
	public String objectToJsonString(Object o){
		return gson.toJson(o);
	}

	public Object JsonToObject(String json, Class<?> clazz){
		return gson.fromJson(json, clazz);
	}

	/**
	 * json 타입을 HashMap으로 변환하여 message의 placeholder와 치환한다.
	 * @param message
	 * @param jsonData
	 * @return
	 */
	public String jsonToNotificationMessage(String message, String jsonData){
		JsonObject jsonObject = stringJsonDataToJson(jsonData);
		StringBuilder sb = new StringBuilder(message);

		for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
			String placeholder = "{" + entry.getKey() + "}";
			int index = sb.indexOf(placeholder);

			String value = entry.getValue().getAsString();
			while(index != -1){
				sb.replace(index, index + placeholder.length(), value);
				index = sb.indexOf(placeholder, index + value.length());
			}
		}
		return sb.toString();
	}

	private JsonObject stringJsonDataToJson(String jsonData){
		return new Gson().fromJson(jsonData, JsonObject.class);
	}
}
