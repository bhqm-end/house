package com.mooc.house.hsrv.config;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;

// WebMvcConfigurerAdapter是Spring内部的一种配置方式
//采用JavaBean的形式来代替传统的xml配置文件形式进行针对框架个性化定制
@Configuration
public class FastjsonConfig extends  WebMvcConfigurerAdapter {


    /*
    * configureMessageConverters：信息转换器
    * 消息内容转换配置
    * 配置fastJson返回json转换
    * */
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        //调用父类的配置
        super.configureMessageConverters(converters);
        //创建fastJson消息转换器
        FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();
        //创建配置类
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        fastConverter.setFastJsonConfig(fastJsonConfig);
        //将fastjson添加到视图消息转换器列表内
        converters.add(fastConverter);
    }
}
