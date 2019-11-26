package nl.komtek.pt.interceptors;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nl.komtek.pt.utils.Util;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

@Component
public class LoginInterceptor extends org.springframework.web.servlet.handler.HandlerInterceptorAdapter
{
  public LoginInterceptor() {}
  
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception
  {
    int i = NumberUtils.toInt(Util.a("server.port"));
    String str1 = Util.a("server.password");
    Cookie[] arrayOfCookie = request.getCookies();
    Cookie localCookie = WebUtils.getCookie(request, "_pbpw" + i);
    
    String str2 = Util.e(str1);
    if (str2 == null) {
      return true;
    }
    
    if ((arrayOfCookie == null) || (localCookie == null) || (!StringUtils.equals(str2, localCookie.getValue()))) {
      response.sendRedirect("/login");
      return false;
    }
    
    return true;
  }
}
