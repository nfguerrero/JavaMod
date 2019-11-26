package nl.komtek.pt.controllers;

import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import nl.komtek.pt.utils.Util;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping({"/login/**"})
public class LoginController
{
  @Value("${server.demo:false}")
  boolean demo;
  
  public LoginController() {}
  
  @RequestMapping(method={org.springframework.web.bind.annotation.RequestMethod.GET})
  public String showForm(ModelMap modelMap)
  {
    String str = Util.a("server.password");
    if (str == null) {
      return "redirect:/";
    }
    if (demo) {
      modelMap.put("demoPassword", Util.b("server.password", ""));
    }
    return "login";
  }
  
  @RequestMapping(method={org.springframework.web.bind.annotation.RequestMethod.POST})
  public String login(@org.springframework.web.bind.annotation.RequestParam String password, HttpServletResponse response) throws NoSuchAlgorithmException
  {
    String str = Util.a("server.password");
    int i = NumberUtils.toInt(Util.a("server.port"));
    if (StringUtils.equals(str, password)) {
      Cookie localCookie = new Cookie("_pbpw" + i, password);
      localCookie.setHttpOnly(true);
      localCookie.setMaxAge((int)TimeUnit.DAYS.toSeconds(7L));
      localCookie.setValue(Util.e(password));
      response.addCookie(localCookie);
      return "redirect:/";
    }
    return "login";
  }
}
