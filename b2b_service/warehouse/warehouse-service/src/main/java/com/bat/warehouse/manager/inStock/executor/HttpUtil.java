package com.bat.warehouse.manager.inStock.executor;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

public class HttpUtil {

	private static final Logger log = LoggerFactory.getLogger(HttpUtil.class);
	
	public static <T> T postJson(String url, Object requestObject, Type responseType) {
		Gson gson = new GsonBuilder().create();
		String requestJson = gson.toJson(requestObject);
		String responseJson = postJson(url, requestJson);
		return gson.fromJson(responseJson, responseType);
	}

    /**
     * 正常
     * @param url
     * @param requestObject
     * @param responseClass
     * @param <T>
     * @return
     */
    public static <T> T postForm(String url, Object requestObject, Class<T> responseClass) {
        Gson gson = new GsonBuilder().create();
        String requestJson = gson.toJson(requestObject);
        String responseJson = postForm(url, requestJson);
        return gson.fromJson(responseJson, responseClass);
    }

	//post请求增加token参数
	public static <T> T postJson(String url,String token, Object requestObject, Class<T> responseClass) {
		Gson gson = new GsonBuilder().create();
		String requestJson = gson.toJson(requestObject);
		String responseJson = postJson(url,token,requestJson);
		return gson.fromJson(responseJson, responseClass);
	}

	//同步B2B
	public static <T> T postJson(String url,Long distributorId,String orderSource,long timestamp,String sign, Object requestObject, Class<T> responseClass) {
		Gson gson = new GsonBuilder().create();
		String requestJson = gson.toJson(requestObject);
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Content-Type", "application/json");
		headers.put("Accept", "application/json");
		headers.put("distributorId",String.valueOf(distributorId));
		headers.put("orderSource",orderSource);
		headers.put("timestamp",String.valueOf(timestamp));
		headers.put("sign",sign);
		log.info("同步b2b--request--"+url+"--"+requestJson);
		String responseJson = request(url, requestJson, "POST", headers);
		log.info("同步b2b--response--"+url+"--"+responseJson);
		return gson.fromJson(responseJson, responseClass);
	}

	//put请求，带token
    public static <T> T putJson(String url,String token, Object requestObject, Class<T> responseClass) {
        Gson gson = new GsonBuilder().create();
        String requestJson = gson.toJson(requestObject);
        String responseJson = putJson(url,token,requestJson);
        return gson.fromJson(responseJson, responseClass);
    }

    //put请求，不带token
    public static <T> T putJson(String url, Object requestObject, Class<T> responseClass) {
        Gson gson = new GsonBuilder().create();
        String requestJson = gson.toJson(requestObject);
        String responseJson = putJson(url,requestJson);
        return gson.fromJson(responseJson, responseClass);
    }

    public static <T> T getJson(String url, Object requestObject,Class<T> responseClass) {
		Gson gson = new GsonBuilder().create();
		String requestJson = gson.toJson(requestObject);
		String responseJson = getJson(url,requestJson);
		return gson.fromJson(responseJson, responseClass);
	}

    public static String putJson(String url,String token, String requestJson) {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json");
        headers.put("Accept", "application/json");
        headers.put("Platform","diy-app");//头参数
        headers.put("Authorization",token);//增加token
        return request(url, requestJson, "PUT", headers);
    }

    public static String putJson(String url,String requestJson) {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json");
        headers.put("Accept", "application/json");
        headers.put("Platform","diy-app");//头参数
        return request(url, requestJson, "PUT", headers);
    }

    //delete请求
    public static <T> T deleteJson(String url,String token, Object requestObject, Class<T> responseClass) {
        Gson gson = new GsonBuilder().create();
        String requestJson = gson.toJson(requestObject);
        String responseJson = deleteJson(url,token,requestJson);
        return gson.fromJson(responseJson, responseClass);
    }

    public static String deleteJson(String url,String token, String requestJson) {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json");
        headers.put("Accept", "application/json");
        headers.put("Platform","diy-app");//头参数
        headers.put("Authorization",token);//增加token
        return request(url, requestJson, "DELETE", headers);
    }
	
	public static <T> T postJson(String url, Object requestObject, Type responseType, Map<String, String> headers) {
		Gson gson = new GsonBuilder().create();
		String requestJson = gson.toJson(requestObject);
		String responseJson = request(url, requestJson, "POST", headers);
		return gson.fromJson(responseJson, responseType);
	}
	
	public static <T> T getJson(String url, Type responseType) {
		Gson gson = new GsonBuilder().create();
		String responseJson = getJson(url);
		return gson.fromJson(responseJson, responseType);
	}


	//方法改造
	public static <T> T getJson(String url,String token, Class<T> responseClass) {
		Gson gson = new GsonBuilder().create();
		String responseJson = getJsonToken(url,token);
		return gson.fromJson(responseJson, responseClass);
	}
	
	public static <T> T getJson(String url, Class<T> responseClass) {
		Gson gson = new GsonBuilder().create();
		String responseJson = getJson(url);
		log.info("response--"+responseJson);
		return gson.fromJson(responseJson, responseClass);
	}
	
	public static String postJson(String url, String requestJson) {
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Content-Type", "application/json");
		headers.put("Accept", "application/json");
		headers.put("Platform","diy-app");//头参数
		return request(url, requestJson, "POST", headers);
	}

	public static String postForm(String url, String requestJson) {
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Content-Type", "multipart/form-data");
		headers.put("Accept", "multipart/form-data");
		headers.put("Platform","diy-app");//头参数
		return request(url, requestJson, "POST", headers);
	}

	public static String postJson(String url,String token, String requestJson) {
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Content-Type", "application/json");
		headers.put("Accept", "application/json");
		headers.put("Platform","diy-app");//头参数
		headers.put("Authorization",token);//增加token
		return request(url, requestJson, "POST", headers);
	}

	public static String getJson(String url) {
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Content-Type", "application/json");
		headers.put("Accept", "application/json");
		headers.put("Platform","diy-app");//头参数
		return request(url, null, "GET", headers);
	}
	public static String getJson(String url,String requestJson) {
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Content-Type", "application/json");
		headers.put("Accept", "application/json");
		headers.put("Platform","diy-app");//头参数
		return request(url, requestJson, "GET", headers);
	}

	//方法改造，增加token参数
	public static String getJsonToken(String url,String token) {
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Content-Type", "application/json");
		headers.put("Accept", "application/json");
		headers.put("Platform","diy-app");//头参数
		headers.put("Authorization",token);
		return request(url, null, "GET", headers);
	}
	
	public static String request(String url, String requestJson, String requestMethod, Map<String, String> headers) {
		HttpURLConnection connection = null;
		String reponseObject = "";
		int responseCode = 0;
		try {

			URL requestUrl = new URL(url);
			connection = (HttpURLConnection) requestUrl.openConnection();
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setRequestMethod(requestMethod);
			connection.setUseCaches(false);
			connection.setInstanceFollowRedirects(true);
			if (headers != null) {
				Iterator<Map.Entry<String, String>> entries = headers.entrySet().iterator();
				while (entries.hasNext()) {
					Map.Entry<String, String> entry = entries.next(); 
					connection.setRequestProperty(entry.getKey(), entry.getValue());
				}
			}
			connection.setConnectTimeout(15000); //连接10S超时
			connection.setReadTimeout(30000); //读取30S超时
			connection.connect();
			

			// 将请求的对象转成json数据，并且提交
			if (requestJson != null) {
				if (requestJson != null && !requestJson.isEmpty()) {
					OutputStream outputStream = connection.getOutputStream();
					outputStream.write(requestJson.getBytes("UTF-8"));
					outputStream.close();
				}
			}
			
			if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
				responseCode = connection.getResponseCode();
				throw new Exception("请求失败url:"+ url +",status code:" + connection.getResponseCode() + ",error:" + connection.getResponseMessage());
			}

			try (InputStream inputStream = connection.getInputStream();
					InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
					BufferedReader bufferedReader = new BufferedReader(inputStreamReader);) {
				String str;
				StringBuilder buffer = new StringBuilder();
				while ((str = bufferedReader.readLine()) != null) {
					buffer.append(str);
				}
				
				reponseObject = buffer.toString();
			}

		} catch (Exception e) {
			if (responseCode != 0) {
				reponseObject = String.format("{\"responseCode\":%d}", responseCode);
			}
		} finally {

			if (connection != null)
				connection.disconnect();
		}

		return reponseObject;
	}
	
	public static List<String> requestTxtFile(String url, Map<String, String> headers) {
		HttpURLConnection connection = null;
		List<String> strList = new ArrayList<String>();
		int responseCode = 0;
		try {

			URL requestUrl = new URL(url);
			connection = (HttpURLConnection) requestUrl.openConnection();
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setRequestMethod("GET");
			connection.setUseCaches(false);
			connection.setInstanceFollowRedirects(true);
			if (headers != null) {
				Iterator<Map.Entry<String, String>> entries = headers.entrySet().iterator();
				while (entries.hasNext()) {
					Map.Entry<String, String> entry = entries.next(); 
					connection.setRequestProperty(entry.getKey(), entry.getValue());
				}
			}
			connection.setConnectTimeout(15000); //连接10S超时
			connection.setReadTimeout(30000); //读取30S超时
			connection.connect();

			
			if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
				responseCode = connection.getResponseCode();
				throw new Exception("请求失败url:"+ url +",status code:" + connection.getResponseCode() + ",error:" + connection.getResponseMessage());
			}

			try (InputStream inputStream = connection.getInputStream();
					InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
					BufferedReader bufferedReader = new BufferedReader(inputStreamReader);) {
				String str;
				StringBuilder buffer = new StringBuilder();
				while ((str = bufferedReader.readLine()) != null) {
					strList.add(str);
				}
			}

		} catch (Exception e) {

		} finally {
			if (connection != null)
				connection.disconnect();
		}

		return strList;
	}

	/**
	 *
	 * @param url
	 * @param requestObject
	 * @param responseClass
	 * @param <T>
	 * @return
	 * @throws IOException
	 *//*
	public static <T> T postByApplicationJson(String url, Object requestObject, Class<T> responseClass)  {
		String params = JSON.toJSONString(requestObject);
		String result = null;// 将二进制流转为String
		try {
			byte[] requestBytes = params.getBytes("utf-8"); // 将参数转为二进制流
			HttpClient httpClient = new HttpClient();// 客户端实例化
			PostMethod postMethod = new PostMethod(url);
			//设置请求头Authorization
			//postMethod.setRequestHeader("Authorization", "Basic " + authorization);
			// 设置请求头 Content-Type
			postMethod.setRequestHeader("Content-Type", "application/json");
			InputStream inputStream = new ByteArrayInputStream(requestBytes, 0,
					requestBytes.length);
			RequestEntity requestEntity = new InputStreamRequestEntity(inputStream,
					requestBytes.length, "application/json; charset=utf-8"); // 请求体
			postMethod.setRequestEntity(requestEntity);
			log.info("request--"+url+"-参数为：-"+params);
			httpClient.executeMethod(postMethod);// 执行请求
			InputStream soapResponseStream = postMethod.getResponseBodyAsStream();// 获取返回的流
			byte[] datas = null;
			try {
				datas = readInputStream(soapResponseStream);// 从输入流中读取数据
			} catch (Exception e) {
				e.printStackTrace();
			}
			result = new String(datas, "UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("发送http请求异常");
		}
		// 打印返回结果
		log.info("--response--"+url+"--"+result);
		 JSON json = JSON.parseObject(result);
		 return JSONObject.toJavaObject(json,responseClass);
	}*/
	/**
	   * 从输入流中读取数据
	   *
	   * @param inStream
	   * @return
	   * @throws Exception
	   */
	public static byte[] readInputStream(InputStream inStream) throws Exception {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = inStream.read(buffer)) != -1) {
		  outStream.write(buffer, 0, len);
		}
		byte[] data = outStream.toByteArray();
		outStream.close();
		inStream.close();
		return data;
	}

	/*public static void main(String[] args) throws IOException {
		ExchangeRxOrderDTO exchangeRxOrderDTO = new ExchangeRxOrderDTO();
		ExchangeCodeOrderDTO orderDTO = new ExchangeCodeOrderDTO();
		orderDTO.setPictureId(12L);
		orderDTO.setMaterialId(10L);
		orderDTO.setModelId(4l);
		List<String> stringList = new ArrayList<>();
		stringList.add("1234");
		orderDTO.setSecretCodeList(stringList);
		List<ExchangeCodeOrderDTO> dtoList = new ArrayList<>();
		dtoList.add(orderDTO);
		exchangeRxOrderDTO.setItemList(dtoList);
		//ResponseBaseBean responseBaseBean = postByApplicationJson("http://localhost:9997/rx/exchangeCard/checkCode",exchangeRxOrderDTO,ResponseBaseBean.class);
		ResponseBaseBean responseBaseBean2 =postJson("http://localhost:9997/rx/exchangeCard/checkCode",exchangeRxOrderDTO,ResponseBaseBean.class);
		System.out.println(responseBaseBean2);
	}*/

	//含有请求头的
	public static <T> T postJsonWithHeader(String url, String paramJson, Map<String, Object> headerMap,Class<T> responseClass) {
		Gson gson = new GsonBuilder().create();

		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Content-Type", "application/json");
		headers.put("Accept", "application/json");
		if(headerMap !=null){
			Iterator<Map.Entry<String, Object>> iterator =headerMap.entrySet().iterator();
			while (iterator.hasNext()){
				Map.Entry<String, Object> entry = iterator.next();
				headers.put(entry.getKey(),String.valueOf(entry.getValue()));
			}
		}
		log.info("带请求头--request--"+url+"--"+paramJson);
		String responseJson = request(url, paramJson, "POST", headers);
		log.info("带请求头--response--"+url+"--"+responseJson);
		return gson.fromJson(responseJson, responseClass);
	}

	public static String doGet(String url) {
		CloseableHttpClient httpClient = null;
		CloseableHttpResponse response = null;
		String result = "";
		try {
			// 通过址默认配置创建一个httpClient实例
			httpClient = HttpClients.createDefault();
			// 创建httpGet远程连接实例
			HttpGet httpGet = new HttpGet(url);
			// 设置请求头信息，鉴权
			httpGet.setHeader("Authorization", "Bearer da3efcbf-0845-4fe3-8aba-ee040be542c0");
			// 设置配置请求参数
			RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(3500000)// 连接主机服务超时时间
					.setConnectionRequestTimeout(3500000)// 请求超时时间
					.setSocketTimeout(6000000)// 数据读取超时时间
					.build();
			// 为httpGet实例设置配置
			httpGet.setConfig(requestConfig);
			// 执行get请求得到返回对象
			response = httpClient.execute(httpGet);
			// 通过返回对象获取返回数据
			org.apache.http.HttpEntity entity = response.getEntity();
			// 通过EntityUtils中的toString方法将结果转换为字符串
			result = EntityUtils.toString(entity);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// 关闭资源
			if (null != response) {
				try {
					response.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (null != httpClient) {
				try {
					httpClient.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}
}
