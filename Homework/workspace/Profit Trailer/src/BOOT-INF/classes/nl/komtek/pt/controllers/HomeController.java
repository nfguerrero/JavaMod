package nl.komtek.pt.controllers;

import java.util.concurrent.TimeUnit;
import nl.komtek.pt.utils.Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;



@Controller
public class HomeController
{
  public HomeController() {}
  
  @RequestMapping(value={"/"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
  public String home()
  {
    return "redirect:/monitoring";
  }
  







  @RequestMapping(value={"possible-buys-log", "pairs-log", "dca-log", "pending-log", "sales-log", "config", "dust-log"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
  public String pages()
  {
    return "redirect:/monitoring";
  }
  
  @RequestMapping({"stop"})
  public String stop() {
    if (StringUtils.isBlank(Util.a("server.password"))) {
      return "redirect:/monitoring";
    }
    
    if (Boolean.valueOf(Util.a("server.demo")).booleanValue()) {
      return "redirect:/monitoring";
    }
    
    Util.saveBlocked = true;
    try {
      TimeUnit.MILLISECONDS.sleep(2000L);
      System.exit(1);
    }
    catch (InterruptedException localInterruptedException) {}
    return "redirect:/monitoring";
  }
}
