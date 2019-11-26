package nl.komtek.pt.services;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.TelegramBotAdapter;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import nl.komtek.pt.models.SellMonitoringData;
import nl.komtek.pt.utils.Util;
import nl.komtek.pt.utils.a.e;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;





@Component
public class TelegramService
{
  private final Logger a = LogManager.getLogger(TelegramService.class);
  @Autowired
  private ProfitTrailerService profitTrailerService;
  
  public TelegramService() {}
  
  public void a(String paramString)
  {
    b(String.format("%s - %s", new Object[] { getSender(), paramString }));
  }
  
  private void a(String paramString1, String paramString2, String paramString3) {
    TelegramBot localTelegramBot = TelegramBotAdapter.build(StringUtils.trim(paramString1));
    boolean bool = Boolean.parseBoolean(Util.b("telegram.disableNotification", "true"));
    


    SendMessage localSendMessage = (SendMessage)new SendMessage(StringUtils.trim(paramString2), paramString3).parseMode(ParseMode.HTML).disableWebPagePreview(true).disableNotification(bool);
    localTelegramBot.execute(localSendMessage, new TelegramService.1(this));
  }
  











  private void b(String paramString)
  {
    String str1 = Util.a("telegram.botToken");
    String str2 = Util.a("telegram.chatId");
    
    String str3 = Util.a("telegram.botToken2");
    String str4 = Util.a("telegram.chatId2");
    

    boolean bool = Boolean.parseBoolean(Util.b("testMode"));
    if (bool) {
      a.info("TEST MODE - Telegram message sent - {}", paramString);
      return;
    }
    
    if ((StringUtils.isNotBlank(str1)) && (StringUtils.isNotBlank(str2))) {
      a(str1, str2, paramString);
    }
    if ((StringUtils.isNotBlank(str3)) && (StringUtils.isNotBlank(str4))) {
      a(str3, str4, paramString);
    }
  }
  
  public void a(String paramString, double paramDouble1, double paramDouble2) {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(getSender() + "\n")
      .append(String.format("Just bought: %s", new Object[] { paramString }) + "\n")
      .append(String.format("Cost: %.8f", new Object[] {Double.valueOf(paramDouble1) }) + "\n")
      .append(String.format("Rate: %.8f", new Object[] {Double.valueOf(paramDouble2) }));
    
    b(localStringBuilder.toString());
  }
  
  public void a(SellMonitoringData paramSellMonitoringData) {
    String str1 = Util.a(e.PAIRS, "MARKET");
    double d = Util.a(paramSellMonitoringData.getCurrentPrice() * paramSellMonitoringData.getSoldAmount()).doubleValue();
    String str2 = String.format("Cost: %.8f", new Object[] { Double.valueOf(d) });
    if (paramSellMonitoringData.getBoughtTimes() > 0) {
      str2 = String.format("Cost: %.8f (%d)", new Object[] { Double.valueOf(d), Integer.valueOf(paramSellMonitoringData.getBoughtTimes()) });
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(getSender() + "\n")
      .append(String.format("Just sold: %s", new Object[] {paramSellMonitoringData.getMarket() }) + "\n")
      .append(String.format("Sell strat: %s", new Object[] {paramSellMonitoringData.getSellStrategy() }) + "\n")
      .append(String.format("%s", new Object[] { str2 }) + "\n")
      .append(String.format("Rate: %.8f", new Object[] {Double.valueOf(paramSellMonitoringData.getCurrentPrice()) }) + "\n")
      .append(String.format("Profit: %.2f%%", new Object[] {Double.valueOf(paramSellMonitoringData.getProfit()) }) + "\n")
      .append(String.format("Profit %s: %.8f", new Object[] { str1, Double.valueOf(paramSellMonitoringData.getProfitBTC()) }));
    

    b(localStringBuilder.toString());
  }
  
  private String getSender() {
    String str1 = Util.a("server.sitename");
    String str2 = str1;
    if ((str1 == null) || (str1.equals("-"))) {
      String str3 = Util.a(e.PAIRS, "MARKET");
      str2 = ProfitTrailerService.getActiveExchange() + " " + str3;
    }
    return str2;
  }
}
