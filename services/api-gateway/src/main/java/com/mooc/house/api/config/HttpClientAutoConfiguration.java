package com.mooc.house.api.config;

import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.zalando.logbook.httpclient.LogbookHttpRequestInterceptor;
import org.zalando.logbook.httpclient.LogbookHttpResponseInterceptor;

@Configuration
//HttpClient.java存在的情况下，才加载HttpClientAutoConfiguration
@ConditionalOnClass({HttpClient.class})
//将HttpClientProperties作为一个bean注入spring容器中
@EnableConfigurationProperties(HttpClientProperties.class)
public class HttpClientAutoConfiguration {

	private final HttpClientProperties properties;
	
	public HttpClientAutoConfiguration(HttpClientProperties properties){
		this.properties = properties;
	}
	
	//打印请求的
	@Autowired
	private LogbookHttpRequestInterceptor logbookHttpRequestInterceptor;
	//打印响应的
	@Autowired
	private LogbookHttpResponseInterceptor logbookHttpResponseInterceptor;
	
	/**
	 * httpclient bean 的定义
	 * @return
	 */
	@Bean
	@ConditionalOnMissingBean(HttpClient.class)
	public HttpClient httpClient() {
		RequestConfig requestConfig = RequestConfig.custom()
				.setConnectTimeout(properties.getConnectTimeOut())
				.setSocketTimeout(properties.getSocketTimeOut()).build();// 构建requestConfig
		HttpClient client = HttpClientBuilder.create().setDefaultRequestConfig(requestConfig)
				.setUserAgent(properties.getAgent())      	//发送的是哪个agent，一般浏览器都有一个useragent的header头
				.setMaxConnPerRoute(properties.getMaxConnPerRoute())//每一个服务节点最大的连接数
				.setMaxConnTotal(properties.getMaxConnTotaol())  //最大连接数50
				/*.addInterceptorFirst(logbookHttpRequestInterceptor)
				.addInterceptorFirst(logbookHttpResponseInterceptor)*/
				.build();
		return client;
	}
}
