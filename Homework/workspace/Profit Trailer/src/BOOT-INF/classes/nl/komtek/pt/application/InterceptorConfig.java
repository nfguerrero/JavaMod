package nl.komtek.pt.application;

import nl.komtek.pt.interceptors.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class InterceptorConfig extends WebMvcConfigurerAdapter
{
  @org.springframework.beans.factory.annotation.Autowired
  LoginInterceptor loginInterceptor;
  
  public InterceptorConfig() {}
  
  public void addInterceptors(InterceptorRegistry registry)
  {
    registry.addInterceptor(loginInterceptor).addPathPatterns(new String[] { "/**" }).excludePathPatterns(new String[] { "/login/**", "/error/**" });
  }
}
