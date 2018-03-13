package com.qushihan.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.resource.VersionResourceResolver;

@Configuration
public class MyWebConfig extends WebMvcConfigurerAdapter {
    // SpringBoot重写addResourceHandlers解决resources下面静态资源无法访问
    @Override // 对静态资源进行处理，否则boot是把所有静态资源进行拦截
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/").resourceChain(true).addResolver(
                new VersionResourceResolver().addFixedVersionStrategy("1.10", "/**/*.js").addContentVersionStrategy("/**"));
    }
}
