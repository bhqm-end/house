package com.mooc.house.api.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.AvailabilityFilteringRule;
import com.netflix.loadbalancer.IPing;
import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.PingUrl;

public class NewRuleConfig {
	
	@Autowired
	private IClientConfig ribbonClientConfig;

	/*
	* IPing这个bean每隔十秒会发送一个ping命令
	* 我们可以指定一个ping命令所指向端点的path
	*
	* */
	@Bean
	public IPing ribbonPing(IClientConfig config){
		//指定一个url路径，每次进行健康检查的时候，就发送这个url请求
		return new PingUrl(false,"/health");
	}
	
	@Bean
	public IRule ribbonRule(IClientConfig config){
//		return new RandomRule();
		//Ribbon在服务调用结束后会对当前调用的结果的成功或失败做记录，
		// 使用AvailabilityFilteringRule后，我们下一次进行负载均衡的时候就会首先选择上一次成功的服务器，
		return new AvailabilityFilteringRule();
	}

}
