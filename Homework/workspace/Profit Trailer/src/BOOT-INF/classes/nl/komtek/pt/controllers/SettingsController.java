package nl.komtek.pt.controllers;

import com.google.gson.Gson;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import nl.komtek.pt.services.ProfitTrailerService;
import nl.komtek.pt.services.TelegramService;
import nl.komtek.pt.strategyrunner.NormalStrategyRunner;
import nl.komtek.pt.utils.Util;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;



@Controller
@RequestMapping({"/settings/**"})
public class SettingsController
{
  private final Logger logger = LogManager.getLogger(NormalStrategyRunner.class);
  @Autowired
  private TelegramService telegramService;
  @Autowired
  private ResourceLoader resourceLoader;
  String[] a = { "trading/DCA.properties", "trading/PAIRS.properties", "trading/INDICATORS.properties", "configuration.properties", "trading/hotconfig" };
  


  public SettingsController() {}
  


  @RequestMapping(value={"/sellOnlyMode"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
  public String sellOnlyMode(@RequestParam boolean enabled, @RequestParam String type)
  {
    if (StringUtils.isBlank(Util.a("server.password"))) {
      logger.info("Please set a password!");
      return "redirect:/monitoring";
    }
    
    if (Boolean.valueOf(Util.a("server.demo")).booleanValue()) {
      return "redirect:/monitoring";
    }
    
    ProfitTrailerService.setGlobalSellOnlyMode(enabled);
    ProfitTrailerService.setGlobalSellOnlyModePriceRise(false);
    ProfitTrailerService.setGlobalSellOnlyModePriceDrop(false);
    ProfitTrailerService.setGlobalSellOnlyModeConsecutiveBuy(false);
    
    if (((enabled) && (type.equals("rise"))) || (type.equals("drop"))) {
      ProfitTrailerService.setGlobalSellOnlyModePriceRise(type.equals("rise"));
      ProfitTrailerService.setGlobalSellOnlyModePriceDrop(type.equals("drop"));
      ProfitTrailerService.setGlobalSellOnlyModeConsecutiveBuy(false);
    }
    
    return "redirect:/monitoring";
  }
  
  @RequestMapping(value={"/overrideSellOnlyMode"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
  public String overrideSellOnlyMode(@RequestParam Optional<Boolean> enabled) {
    if (StringUtils.isBlank(Util.a("server.password"))) {
      logger.info("Please set a password!");
      return "redirect:/monitoring";
    }
    
    if (Boolean.valueOf(Util.a("server.demo")).booleanValue()) {
      return "redirect:/monitoring";
    }
    
    ProfitTrailerService.setGlobalSellOnlyModeManualOverride((Boolean)enabled.orElse(null));
    return "redirect:/monitoring";
  }
  
  @RequestMapping(value={"/telegramTestMessage"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
  public String TelegramTestMessage() {
    if (StringUtils.isBlank(Util.a("server.password"))) {
      logger.info("Please set a password!");
      return "redirect:/monitoring";
    }
    
    if (Boolean.valueOf(Util.a("server.demo")).booleanValue()) {
      return "redirect:/monitoring";
    }
    
    telegramService.a("Your telegram is setup correctly");
    return "redirect:/monitoring";
  }
  
  @CrossOrigin
  @ResponseBody
  @RequestMapping({"/load"})
  public String loadSetting(@RequestParam String fileName) {
    if (!Boolean.valueOf(Util.a("server.enableConfig")).booleanValue()) {
      return "";
    }
    
    if (StringUtils.isBlank(Util.a("server.password"))) {
      return "[\"Please setup a password in application.properties\"]";
    }
    
    if (!Arrays.asList(a).contains(fileName)) {
      return "";
    }
    
    Resource localResource = resourceLoader.getResource("file:" + fileName);
    if (localResource.exists()) {
      try
      {
        Gson localGson = new Gson();
        byte[] arrayOfByte = Files.readAllBytes(localResource.getFile().toPath());
        String str = new String(arrayOfByte, StandardCharsets.UTF_8);
        Object localObject = Arrays.asList(str.split("\\r?\\n"));
        localObject = new ArrayList((Collection)localObject);
        return localGson.toJson(localObject);
      }
      catch (IOException localIOException1) {}
    }
    return "[]";
  }
  
  @CrossOrigin
  @ResponseBody
  @RequestMapping(value={"/save"}, method={org.springframework.web.bind.annotation.RequestMethod.POST})
  public void saveSetting(@RequestParam String fileName, @RequestBody String saveData) {
    if (!Boolean.valueOf(Util.a("server.enableConfig")).booleanValue()) {
      return;
    }
    
    if (StringUtils.isBlank(Util.a("server.password"))) {
      return;
    }
    
    if (Boolean.valueOf(Util.a("server.demo")).booleanValue()) {
      return;
    }
    
    if (!Arrays.asList(a).contains(fileName)) {
      return;
    }
    
    if (fileName.equalsIgnoreCase("trading/HOTCONFIG")) {
      Util.setHotConfig(saveData);
      return;
    }
    
    Resource localResource = resourceLoader.getResource("file:" + fileName);
    if (localResource.exists()) {
      try {
        Files.write(localResource.getFile().toPath(), saveData.getBytes(StandardCharsets.UTF_8), new OpenOption[0]);
      }
      catch (IOException localIOException) {}
    }
  }
}
