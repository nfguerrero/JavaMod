package nl.komtek.pt.schedules;

import com.cf.client.c;
import com.cf.data.map.generic.Exchange;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import nl.komtek.a.a;
import nl.komtek.pt.services.ProfitTrailerService;
import nl.komtek.pt.utils.Util;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
public class AllScheduledTasks
{
  public AllScheduledTasks() {}
  
  private final Logger a = LogManager.getLogger(AllScheduledTasks.class);
  private String b = Util.a("trading.exchange");
  @Value("${default_apiKey:-}")
  private String defaultApiKey;
  @Autowired
  private ProfitTrailerService profitTrailerService;
  private static int c;
  private static boolean d;
  @Autowired
  private CacheManager cacheManager;
  private static LocalDateTime e = ;
  
  @Scheduled(initialDelay=100L, fixedDelay=3000L)
  public void fetchData() throws InterruptedException {
    if (!d) {
      c();
      d = true;
    }
    
    a();
    
    if (ProfitTrailerService.getActiveExchange().equals(Exchange.BINANCE)) {
      if (LocalDateTime.now().isAfter(e)) {
        cacheManager.getCache("24hVolume").removeAll();
        e = LocalDateTime.now().plusMinutes(5L);
      }
      return;
    }
    
    TimeUnit.MILLISECONDS.sleep(1000L);
    b();
  }
  
  private void a() {
    try {
      profitTrailerService.getTickerScheduled();
    } catch (Exception localException) {
      a.error(localException, localException);
    }
  }
  
  private void b() {
    try {
      profitTrailerService.get24hVolumeScheduled("ALL");
    } catch (Exception localException) {
      a.error(localException, localException);
    }
  }
  
  @Scheduled(fixedDelay=3600000L)
  public void killSwitch() {
    String str = "-";
    if ((StringUtils.isNotBlank(defaultApiKey)) && (!defaultApiKey.equals("-"))) {
      str = defaultApiKey;
    }
    
    JsonObject localJsonObject = a.a(StringUtils.trim(str), b);
    if ((!localJsonObject.get(c.g()).getAsBoolean()) && (localJsonObject.has("expiresOn"))) {
      c += 1;
    }
    if (c >= 3) {
      System.exit(1);
      a.error("YOUR LICENSE HAS BEEN DISABLED");
    }
  }
  
  private static void c() {
    int i = NumberUtils.toInt(Util.a("server.port"));
    boolean bool1 = Boolean.valueOf(Util.a("server.disableBrowser")).booleanValue();
    boolean bool2 = StringUtils.isNotBlank(Util.a("server.ssl.key-store"));
    
    if (bool1) {
      return;
    }
    
    String str1 = System.getProperty("os.name").toLowerCase();
    String str2 = "http";
    if (bool2) {
      str2 = "https";
    }
    
    String str3 = String.format("%s://localhost:%s", new Object[] { str2, Integer.valueOf(i) });
    try {
      Runtime localRuntime;
      if (str1.contains("win")) {
        localRuntime = Runtime.getRuntime();
        localRuntime.exec("rundll32 url.dll,FileProtocolHandler " + str3);
      } else if (str1.contains("mac")) {
        localRuntime = Runtime.getRuntime();
        localRuntime.exec("open " + str3);
      } else if ((str1.contains("nix")) || (!str1.contains("nux"))) {}
      return;
    }
    catch (Exception localException) {}
  }
}
