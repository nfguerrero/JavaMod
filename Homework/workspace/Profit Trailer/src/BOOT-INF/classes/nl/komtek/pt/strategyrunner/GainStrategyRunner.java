package nl.komtek.pt.strategyrunner;

import com.cf.data.model.generic.CompleteBalance;
import com.cf.data.model.generic.OrderResult;
import com.cf.data.model.generic.Ticker;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import nl.komtek.pt.bot.strategy.helper.StrategyRunnerHelper;
import nl.komtek.pt.models.AverageCalculator;
import nl.komtek.pt.models.GainMonitoringData;
import nl.komtek.pt.models.ProfitTrailerData;
import nl.komtek.pt.services.ProfitTrailerService;
import nl.komtek.pt.utils.Util;
import nl.komtek.pt.utils.a.c;
import nl.komtek.pt.utils.a.d;
import nl.komtek.pt.utils.a.e;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;





@Component
public class GainStrategyRunner
{
  private final Logger a = LogManager.getLogger(GainStrategyRunner.class);
  @Autowired
  private ProfitTrailerService profitTrailerService;
  @Autowired
  private StrategyRunnerHelper runnerHelper;
  
  public GainStrategyRunner() {}
  
  @Scheduled(initialDelay=550L, fixedDelay=5000L)
  public void checkSellStrategy() {
    try { ArrayList localArrayList = new ArrayList();
      String str1 = Util.a(e.PAIRS, "MARKET");
      
      for (String str2 : profitTrailerService.getAllCurrencyPairs())
      {
        if ((!StrategyRunnerHelper.K(str2)) && 
        


          (!runnerHelper.x(str2)) && 
          


          (runnerHelper.w(str2)))
        {


          d localD = runnerHelper.A(str2);
          
          double d1 = runnerHelper.z(str2);
          String str3 = Util.c(str2);
          String str4 = Util.d(str2);
          if (str1.contains(str3))
          {

            Ticker localTicker = (Ticker)profitTrailerService.getTickers().get(str2);
            if ((localTicker != null) && (highestBid != null) && (lowestAsk != null) && 
              (lowestAsk.doubleValue() > 0.0D) && (highestBid.doubleValue() > 0.0D))
            {

              Map localMap1 = profitTrailerService.getCompleteBalances(str3);
              CompleteBalance localCompleteBalance = (CompleteBalance)localMap1.get(str4);
              
              if ((localCompleteBalance != null) && (!dust) && (available.doubleValue() > 0.0D))
              {
                if (!runnerHelper.a(str2))
                {


                  GainMonitoringData localGainMonitoringData = new GainMonitoringData();
                  Map localMap2 = profitTrailerService.a(Util.c(str2), str2);
                  
                  List localList = profitTrailerService.a(str2, "-1", localMap2);
                  
                  AverageCalculator localAverageCalculator = AverageCalculator.calculate(str2, localList, localCompleteBalance, localTicker, profitTrailerService
                  



                    .getExchangeInfo(str2));
                  
                  a(str2, localD, Double.valueOf(d1), localTicker, localCompleteBalance, localAverageCalculator, localGainMonitoringData);
                  
                  double d2 = runnerHelper.y(str2);
                  localGainMonitoringData.setVolume(d2);
                  localArrayList.add(localGainMonitoringData);
                } }
            } } } }
      Util.getProfitTrailerData().setGainLogData(localArrayList);
    } catch (Exception localException) {
      a.error("An error occured", localException);
    }
  }
  



  private void a(String paramString, d paramD, Double paramDouble, Ticker paramTicker, CompleteBalance paramCompleteBalance, AverageCalculator paramAverageCalculator, GainMonitoringData paramGainMonitoringData)
  {
    double d1 = StrategyRunnerHelper.a(paramAverageCalculator.getAvgPrice(), highestBid
      .doubleValue(), paramAverageCalculator
      .getFee());
    boolean bool1;
    switch (GainStrategyRunner.1.a[paramD.ordinal()]) {
    case 1: 
      bool1 = d1 >= paramDouble.doubleValue();
      boolean bool2 = runnerHelper.k(e.PAIRS, paramString);
      boolean bool3 = runnerHelper.a(e.PAIRS, paramString, d1, paramDouble.doubleValue());
      if (bool2) {
        bool1 = bool3;
        paramDouble = Double.valueOf(profitTrailerService.getTrailingProfit(paramString));
      }
      paramGainMonitoringData.setTriggerValue(paramDouble.doubleValue());
      break;
    case 2: 
      double d3 = Util.d(e.PAIRS, "ALL_min_profit");
      bool1 = Util.a(paramString, highestBid.doubleValue(), paramDouble
        .doubleValue(), d1, d3, paramAverageCalculator.getFee(), paramAverageCalculator.getAvgPrice(), paramGainMonitoringData);
      
      break;
    default: 
      bool1 = false;
    }
    
    BigDecimal localBigDecimal = highestBid;
    paramGainMonitoringData.setMarket(paramString);
    paramGainMonitoringData.setCurrentPrice(localBigDecimal.doubleValue());
    paramGainMonitoringData.setPercChange(runnerHelper.a(paramString, paramTicker).doubleValue());
    paramGainMonitoringData.setProfit(d1);
    paramGainMonitoringData.setAverageCalculator(paramAverageCalculator);
    paramGainMonitoringData.setSellStrategy(paramD.name());
    
    if ((!bool1) && (runnerHelper.a(e.PAIRS, paramString))) {
      d2 = runnerHelper.b(e.PAIRS, paramString);
      bool1 = d1 < d2;
      if (bool1) {
        paramGainMonitoringData.setSellStrategy("STOPLOSS");
      }
    }
    
    double d2 = profitTrailerService.getBalanceForMarket(ProfitTrailerService.getActiveMarket());
    if (d2 <= 0.0D) {
      return;
    }
    
    if (bool1) {
      OrderResult localOrderResult = profitTrailerService.a(paramD, paramString, localBigDecimal, originalAvailable, c.NORMAL);
      if (resultingTrades.size() > 0) {
        runnerHelper.a(paramGainMonitoringData, localOrderResult);
      }
    }
  }
}
