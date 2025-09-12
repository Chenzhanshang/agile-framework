package com.agile.framework.util;

import com.agile.framework.exception.FrameworkException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;

@Slf4j
public class JsonUtil {
	private static ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

	/**
	 * 	对象转为json字符串
	 * @param object 对象
	 * @return
	 */
	public static String encode(Object object) {
		try {
			if (null == object) {
				return null;
			}
			return mapper.writeValueAsString(object);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * 	json字符串转为对象
	 * @param jsonStr json字符串
	 * @param clazz 对象类型
	 * @return
	 */
	public static <T> T decode(String jsonStr, Class<T> clazz) {
		try {
			if (null == jsonStr) {
				return null;
			}
			return mapper.readValue(jsonStr, clazz);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * 	json字符串转为对象
	 * @param jsonStr json字符串
	 * @param typeReference 带泛型的对象类型
	 * @return
	 */
	public static <T> T decode(String jsonStr, TypeReference<T> typeReference) {
		try {
			return mapper.readValue(jsonStr, typeReference);
		} catch (IOException e) {
			throw FrameworkException.of("解析json失败");
		}
	}

	/**
	 * 	json字符数组转为List
	 * @param jsonStr json字符串
	 * @param clazz 对象类型
	 * @return
	 */
	public static <T> List<T> decodeList(String jsonStr, Class<T> clazz) {
		try {
			if (null == jsonStr) {
				return null;
			}
			return mapper.readValue(jsonStr, mapper.getTypeFactory().constructParametricType(List.class, clazz));
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * 合并json
	 * 相同key情况 参与合并者 会覆盖 合并者
	 * @param nodeJsonStr 合并者
	 * @param toMergeJsonStr 参与合并者
	 * @return
	 */
	public static String merge(String nodeJsonStr, String toMergeJsonStr) {
		try {
			JsonNode node = mapper.readTree(nodeJsonStr);
	        ObjectReader reader = mapper.readerForUpdating(node);
			node = reader.readValue(mapper.readTree(toMergeJsonStr));
			return mapper.writeValueAsString(node);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * 转换类型
	 * @param object 对象
	 * @param clazz 转换类型
	 * @return
	 */
	public static <T> T convert(Object object, Class<T> clazz) {
		try {
			return mapper.convertValue(object, clazz);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * 将json字符串转成紧凑的json字符串
	 * @param json 字符串
	 * @return 返回紧凑的字符串，报错不处理，返回源字符串
	 */
	public static String convertToCompact(String json) {
		if(json == null || json.isEmpty() || "{}".equals(json)) {
			return json;
		}
		String compactJson = null;
		try {
			// 创建 ObjectMapper 实例
			ObjectMapper objectMapper = new ObjectMapper();
			// 解析 JSON 字符串并转换为对象
			Object jsonObject = objectMapper.readValue(json, Object.class);
			// 将对象转换为紧凑的 JSON 字符串
			compactJson = objectMapper.writeValueAsString(jsonObject);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return json;
		}
		return compactJson;
	}


}
