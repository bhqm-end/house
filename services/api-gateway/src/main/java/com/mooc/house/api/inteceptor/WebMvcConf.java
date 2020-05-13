package com.mooc.house.api.inteceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/*
* spring拦截器的步骤
* 实现HandlerInterceptor接口，编写拦截器
* 将拦截器加入WebMvcConfig并绑定url path pattern
*
* 继承WebMvcConfigurerAdapter来进行springMVC高度定制化
*
* */
@Configuration
public class WebMvcConf extends WebMvcConfigurerAdapter {
  //把定义好的拦截器注入进来
  @Autowired
  private AuthInterceptor authInterceptor;
  
  @Autowired
  private AuthActionInterceptor authActionInterceptor;


  @Override
  //实现addInterceptors方法可以完成拦截器顺序的管控和拦截器所映射的urlmapping
  public void addInterceptors(InterceptorRegistry registry) {
    //addPathPatterns方法意味着我们要拦截哪些
    //除了静态资源都拦截
    registry.addInterceptor(authInterceptor).excludePathPatterns("/static").addPathPatterns("/**");
    //注册authActionInterceptor拦截器，这里添加的是拦截需要登录的请求
    registry
        .addInterceptor(authActionInterceptor)
         .addPathPatterns("/house/toAdd")
        .addPathPatterns("/accounts/profile").addPathPatterns("/accounts/profileSubmit")
        .addPathPatterns("/house/bookmarked").addPathPatterns("/house/del")
        .addPathPatterns("/house/ownlist").addPathPatterns("/house/add")
        .addPathPatterns("/house/toAdd").addPathPatterns("/agency/agentMsg")
        .addPathPatterns("/comment/leaveComment").addPathPatterns("/comment/leaveBlogComment");
    
    super.addInterceptors(registry);
  }
  /*
  * CORS定义：
  *   CORS是一个W3C标准，全称是“跨域资源共享”
  *   通过添加Response,Header实现
  *   与Jsonp对比，Jsonp只支持GET，
  *   CORS需要较新浏览器支持
  *
  * CORS全局支持
  *   覆盖WebMvcConfigureAdapter的addCorsMappings方法，开启对CORS的全局支持，
  * CORS方法级支持
  *   在Controller方法体添加注解@CrossOrigin
  * */
  //覆盖WebMvcConfigureAdapter的addCorsMappings方法，开启对CORS的全局支持，
  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/**")//拦截所有的url
        .allowedOrigins("*")// 放行哪些原始域，比如"http://domain1.com,https://domain2.com"
        .allowCredentials(true)// 是否发送Cookie信息
        .allowedMethods("GET", "POST", "PUT", "DELETE") // 放行哪些原始域(请求方式)
        .allowedHeaders("*");// 放行哪些原始域(头部信息)
    super.addCorsMappings(registry);
  }
  

}
