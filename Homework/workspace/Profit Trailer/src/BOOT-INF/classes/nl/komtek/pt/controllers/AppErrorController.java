package nl.komtek.pt.controllers;

import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping({"/error/**"})
public class AppErrorController implements ErrorController
{
  public AppErrorController() {}
  
  @ResponseBody
  @RequestMapping
  public String error()
  {
    return "Oops something went wrong, please refresh";
  }
  
  public String getErrorPath()
  {
    return "/error";
  }
}
