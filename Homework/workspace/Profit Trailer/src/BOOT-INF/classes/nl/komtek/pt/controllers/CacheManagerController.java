package nl.komtek.pt.controllers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map.Entry;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.statistics.StatisticsGateway;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping({"/cache/**"})
public class CacheManagerController
{
  @org.springframework.beans.factory.annotation.Autowired
  private CacheManager cacheManager;
  @Value("${connection.publicUrl:}")
  private String publicUrl;
  
  public CacheManagerController() {}
  
  @ResponseBody
  @RequestMapping
  public String stats()
  {
    StringBuilder localStringBuilder = new StringBuilder("");
    for (String str : cacheManager.getCacheNames()) {
      Cache localCache = cacheManager.getCache(str);
      StatisticsGateway localStatisticsGateway = localCache.getStatistics();
      localStringBuilder.append(String.format("%s: %s objects, %s hits, %s misses, disk size %s KB, heap size %s KB, offheap size %s KB <br>", new Object[] { str, 
      
        Long.valueOf(localStatisticsGateway.getSize()), 
        Long.valueOf(localStatisticsGateway.cacheHitCount()), 
        Long.valueOf(localStatisticsGateway.cacheMissCount()), 
        Long.valueOf(localStatisticsGateway.getLocalDiskSizeInBytes() / 1000L), 
        Long.valueOf(localStatisticsGateway.getLocalHeapSizeInBytes() / 1000L), 
        Long.valueOf(localStatisticsGateway.getLocalOffHeapSizeInBytes() / 1000L) }));
      
      if (str.equals("ticker")) {
        localStringBuilder.append(String.format("Ticker last update time: %s <br>", new Object[] {
          LocalDateTime.ofInstant(java.time.Instant.ofEpochMilli(localCache.get(localCache.getKeys().get(0)).getLastUpdateTime()), 
          java.time.ZoneId.systemDefault()) }));
      }
    }
    return localStringBuilder.toString();
  }
  
  @ResponseBody
  @RequestMapping({"/clearAll"})
  public String clear() {
    cacheManager.clearAll();
    return "OK";
  }
  
  @ResponseBody
  @RequestMapping({"/clearTradeHistory"})
  public String clearTradehistory() {
    cacheManager.getCache("tradeHistory").removeAll();
    return "OK";
  }
  
  public int sort(Map.Entry entry1, Map.Entry entry2) {
    int i = StringUtils.indexOf(entry1.getKey().toString(), "_");
    LocalDateTime localLocalDateTime1 = LocalDateTime.parse(entry1.getKey().toString().substring(0, i), DateTimeFormatter.ofPattern("dd-MM-uuuu HH:mm:ss"));
    
    int j = StringUtils.indexOf(entry2.getKey().toString(), "_");
    LocalDateTime localLocalDateTime2 = LocalDateTime.parse(entry2.getKey().toString().substring(0, j), DateTimeFormatter.ofPattern("dd-MM-uuuu HH:mm:ss"));
    
    return localLocalDateTime1.compareTo(localLocalDateTime2);
  }
  
  public boolean isRecent(String key, int seconds) {
    int i = StringUtils.indexOf(key, "_");
    LocalDateTime localLocalDateTime = LocalDateTime.parse(key.substring(0, i), DateTimeFormatter.ofPattern("dd-MM-uuuu HH:mm:ss"));
    return localLocalDateTime.isAfter(LocalDateTime.now().minusSeconds(seconds));
  }
}
