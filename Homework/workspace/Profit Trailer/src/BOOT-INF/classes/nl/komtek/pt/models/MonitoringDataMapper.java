package nl.komtek.pt.models;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;







public class MonitoringDataMapper
{
  private final Gson gson;
  private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
  




  public MonitoringDataMapper()
  {
    gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new MonitoringDataMapper.1(this)).create();
  }
  
  public List<DCAMonitoringData> mapDCAmonitoringData(String dcaMonitoringData) {
    DCAMonitoringData[] arrayOfDCAMonitoringData = (DCAMonitoringData[])gson.fromJson(dcaMonitoringData, [Lnl.komtek.pt.models.DCAMonitoringData.class);
    return Arrays.asList(arrayOfDCAMonitoringData);
  }
}
