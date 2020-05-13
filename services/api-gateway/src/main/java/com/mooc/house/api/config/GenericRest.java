package com.mooc.house.api.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * ：1）url: 请求地址；
 *      2）method: 请求类型(如：POST,PUT,DELETE,GET)；
 *      3）requestEntity: 请求实体，封装请求头，请求内容
 *      4）responseType: 响应类型，根据服务接口的返回类型决定
 *      5）uriVariables: url中参数变量值
 *
 * RestTemplate的2次包装
 * 既支持直连又支持服务发现的调用
 * 直连：直接指定IP地址进行请求，
 * 负载均衡：传递服务名， 需要ribbon来进行ip:port对应来查找服务
 */
@Service
public class GenericRest {
	//负载均衡
	@Autowired
	private RestTemplate lbRestTemplate;
	//直连
	@Autowired
	private RestTemplate directRestTemplate;

	//设置一个直连的flag ，当我们的url是以 direct：// 开头的就认定是一个直连请求， 否则就是一个支持负载均衡的请求
	private static final String directFlag = "direct://";


	// ParameterizedTypeReference<T>：支持反序列化的参数
	public <T> ResponseEntity<T> post(String url,Object reqBody,ParameterizedTypeReference<T> responseType){
		//判断url的类型然后返回不同的RestTemplate
		RestTemplate template = getRestTemplate(url);
		//把directFlag移除掉，因为他只是一种标识
		url = url.replace(directFlag, "");
		/*
		new HttpEntity(reqBody)：对reqBody进行包装
		*/
		//发起一个post请求，利用exchange来指定请求的方式，
		return template.exchange(url, HttpMethod.POST,new HttpEntity(reqBody),responseType);
	}
	
	public <T> ResponseEntity<T> get(String url,ParameterizedTypeReference<T> responseType){
		RestTemplate template = getRestTemplate(url);
		url = url.replace(directFlag, "");
		return template.exchange(url, HttpMethod.GET,HttpEntity.EMPTY,responseType);
	}

	private RestTemplate getRestTemplate(String url) {
		//判断请求，是否为直连或者负载均衡的请求，其实就是判断是否是服务名直接请求，还是服务的IP进行请求。
		if (url.contains(directFlag)) {
			//直连请求
			return directRestTemplate;
		}else {
			//支持负载均衡的请求
			return lbRestTemplate;
		}
	}

}
