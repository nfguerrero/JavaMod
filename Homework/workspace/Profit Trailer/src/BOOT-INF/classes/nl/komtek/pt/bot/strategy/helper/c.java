package nl.komtek.pt.bot.strategy.helper;

import com.cf.data.map.generic.Exchange;
import com.cf.data.model.generic.ChartData;
import eu.verdelhan.ta4j.Tick;
import eu.verdelhan.ta4j.TimeSeries;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import nl.komtek.pt.services.ProfitTrailerService;
import nl.komtek.pt.utils.Util;
import org.joda.time.DateTime;

public class c
{
  private static final String a = Util.a("trading.exchange");
  
  public c() {}
  
  public static void setProfitTrailerService(ProfitTrailerService profitTrailerService) { b = profitTrailerService; }
  
  private static ProfitTrailerService b;
  public static TimeSeries a(String paramString, long paramLong, int paramInt) {
    int i = paramInt * 2;
    LocalDateTime localLocalDateTime1 = LocalDateTime.now(ZoneId.of("UTC")).minusSeconds(paramLong * i);
    
    long l = paramLong;
    if ((Exchange.valueOf(a.toUpperCase()).equals(Exchange.BITTREX)) && (paramLong == 900L)) {
      l = 300L;
    }
    
    List localList1 = b.a(paramString, l, localLocalDateTime1
      .toEpochSecond(ZoneOffset.UTC), i);
    
    List localList2 = b.a(
      String.valueOf(localLocalDateTime1.toEpochSecond(ZoneOffset.UTC)), localList1);
    

    ArrayList localArrayList = new ArrayList();
    Object localObject1 = null;
    Object localObject2 = null;
    for (ChartData localChartData1 : localList2) {
      if ((Exchange.valueOf(a.toUpperCase()).equals(Exchange.BITTREX)) && (paramLong == 900L))
      {
        LocalDateTime localLocalDateTime2 = LocalDateTime.ofInstant(Instant.ofEpochMilli(date * 1000L), ZoneId.of("UTC"));
        if ((localLocalDateTime2.getMinute() == 0) || 
          (localLocalDateTime2.getMinute() == 15) || 
          (localLocalDateTime2.getMinute() == 30) || 
          (localLocalDateTime2.getMinute() == 45)) {
          localObject1 = localChartData1;
          localObject2 = null;
        }
        
        if ((localLocalDateTime2.getMinute() == 55) || 
          (localLocalDateTime2.getMinute() == 10) || 
          (localLocalDateTime2.getMinute() == 25) || 
          (localLocalDateTime2.getMinute() == 40)) {
          localObject2 = localChartData1;
        }
        
        if ((localObject1 != null) && (localObject2 != null)) {
          ChartData localChartData2 = new ChartData(date, high, low, open, close, volume, quoteVolume, weightedAverage);
          


          localArrayList.add(a(localChartData2));
        }
      } else {
        localArrayList.add(a(localChartData1));
      }
    }
    
    return new TimeSeries(localArrayList);
  }
  
  private static Tick a(ChartData paramChartData) {
    return new Tick(new DateTime(date * 1000L), open, high, low, close, volume
    
      .doubleValue());
  }
}
