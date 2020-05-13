package com.mooc.house.api;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mooc.house.api.config.NewRuleConfig;


/*
* 	@EnableDiscoveryClient和@EnableEurekaClient共同点就是：都是能够让注册中心能够发现，扫描到改服务。
	不同点：@EnableEurekaClient只适用于Eureka作为注册中心，@EnableDiscoveryClient 可以是其他注册中心。
* */

@SpringBootApplication
@EnableDiscoveryClient
/* 添加此注解启用断路器Hystrix */
@EnableCircuitBreaker
@Controller
/*指定在调用user时我们使用自己定义的NewRuleConfig规则来实现负载均衡访问服务*/
//@RibbonClient(name="user",configuration=NewRuleConfig.class)
public class ApiGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiGatewayApplication.class, args);
	}


	@Autowired
	private DiscoveryClient discoveryClient;


	@RequestMapping("index1")
	@ResponseBody
	public List<ServiceInstance> getReister(){
	  	//获取用户服务的所有实例
		return discoveryClient.getInstances("user");
	}

}	
	
	
