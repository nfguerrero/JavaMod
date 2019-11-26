package nl.komtek.pt.schedules;

import com.cf.client.c;
import com.cf.data.map.generic.Exchange;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import nl.komtek.a.a;
import nl.komtek.pt.services.ProfitTrailerService;
import nl.komtek.pt.utils.Util;
import nl.komtek.pt.utils.a.e;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;




@Component
public class DefaultScheduledTasks
{
  public DefaultScheduledTasks() {}
  
  private static String a = Util.a(e.PAIRS, "MARKET");
  private final Logger b = LogManager.getLogger(DefaultScheduledTasks.class);
  @Autowired
  private ProfitTrailerService profitTrailerService;
  @Autowired
  private CacheManager cacheManager;
  private String c = Util.a("trading.exchange");
  private String d = Util.b("default_apiKey", "-");
  private static LocalDateTime e = LocalDateTime.now();
  
  @Scheduled(initialDelay=350L, fixedDelay=11000L)
  public void fetchData() throws InterruptedException {
    a = Util.a(e.PAIRS, "MARKET");
    
    if (ProfitTrailerService.getActiveExchange().equals(Exchange.BINANCE)) {
      if (LocalDateTime.now().isAfter(e)) {
        cacheManager.getCache("completeBalances").removeAll();
        cacheManager.getCache("openOrders").removeAll();
        cacheManager.getCache("balances").removeAll();
        cacheManager.getCache("tradeHistory").removeAll();
        e = LocalDateTime.now().plusMinutes(10L);
      }
      return;
    }
    
    b();
    TimeUnit.MILLISECONDS.sleep(500L);
    d();
    TimeUnit.MILLISECONDS.sleep(500L);
    a();
    TimeUnit.MILLISECONDS.sleep(500L);
    c();
  }
  
  private void a()
  {
    try {
      profitTrailerService.getCompleteBalancesScheduled(a);
    } catch (Exception localException) {
      b.error(localException, localException);
    }
  }
  
  private void b() {
    try {
      Map localMap1 = profitTrailerService.b(a, "ALL");
      Map localMap2 = profitTrailerService.h("ALL");
      
      if (localMap1.size() != localMap2.size()) {
        cacheManager.getCache("tradeHistory").removeAll();
        return;
      }
      
      if ((localMap1.size() > 0) && (localMap2.size() > 0) && 
        (localMap1.hashCode() != localMap2.hashCode())) {
        cacheManager.getCache("tradeHistory").removeAll();
      }
    }
    catch (Exception localException) {
      b.error(localException, localException);
    }
  }
  
  private void c() {
    try {
      profitTrailerService.getOpenOrdersScheduled("ALL", a);
    } catch (Exception localException) {
      b.error(localException, localException);
    }
  }
  
  private void d() {
    try {
      profitTrailerService.getBalancesScheduled(a);
    } catch (Exception localException) {
      b.error(localException, localException);
    }
  }
  
  @PostConstruct
  public void startup() {
    String str = "-";
    if ((StringUtils.isNotBlank(d)) && (!d.equals("-"))) {
      str = d;
    }
    JsonObject localJsonObject = a.a(StringUtils.trim(str), StringUtils.trim(c));
    if (!localJsonObject.get(c.g()).getAsBoolean()) {
      System.exit(1);
    }
  }
}
