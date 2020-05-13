package com.mooc.house.api.config;

import java.nio.charset.Charset;
import java.util.Arrays;

import org.apache.http.client.HttpClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter4;
//自动配置
@Configuration
public class RestAutoConfig {
	//将spring自动配置的类分成几组
	public static class RestTemplateConfig {
		/*
		* 支持负载均衡的RestTemplate
		* */
		@Bean
		@LoadBalanced //spring 对restTemplate bean进行定制，加入loadbalance拦截器进行ip:port（服务名到ip端口）的替换
		RestTemplate lbRestTemplate(HttpClient httpclient) {
			//让RestTemplate底层实现使用httpclient
			RestTemplate template = new RestTemplate(new HttpComponentsClientHttpRequestFactory(httpclient));
		    //restTemplate发送请求和接收因为可能有中文所以 响应的序列化编码格式设置为utf-8
			template.getMessageConverters().add(0,new StringHttpMessageConverter(Charset.forName("utf-8")));
		    //反序列化
			template.getMessageConverters().add(1,new FastJsonHttpMessageConvert5());
		    return template;
		}
		
		@Bean
		//直连的RestTemplate
		RestTemplate directRestTemplate(HttpClient httpclient) {
			RestTemplate template = new RestTemplate(new HttpComponentsClientHttpRequestFactory(httpclient));
		    template.getMessageConverters().add(0,new StringHttpMessageConverter(Charset.forName("utf-8")));
		    template.getMessageConverters().add(1,new FastJsonHttpMessageConvert5());
		    return template;
		}

		 //FastJsonHttpMessageConvert4有一个bug，默认支持MediaType为ALL，spring在处理MediaType为ALL默认会使用字节流处理，而不是识别为json
		 //我们来创建一个FastJsonHttpMessageConvert5来覆盖原有的，避免BUG
		 public static class FastJsonHttpMessageConvert5 extends FastJsonHttpMessageConverter4{
	          
	          static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");
	          
	          public FastJsonHttpMessageConvert5(){
	            setDefaultCharset(DEFAULT_CHARSET);
	            setSupportedMediaTypes(Arrays.asList(MediaType.APPLICATION_JSON,new MediaType("application","*+json")));
	          }

	        }
	}

}
