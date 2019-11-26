package nl.komtek.pt.schedules;

import nl.komtek.pt.services.ProfitTrailerService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;




@Component
@ConditionalOnProperty(name={"trading.exchange"}, havingValue="BLADIBLA")
public class PublicServerScheduledTasks
{
  private final Logger a = LogManager.getLogger(PublicServerScheduledTasks.class);
  @Autowired
  private ProfitTrailerService profitTrailerService;
  
  public PublicServerScheduledTasks() {}
  
  @Scheduled(initialDelay=100L, fixedDelay=5000L)
  public void fetchChartData() {}
}
