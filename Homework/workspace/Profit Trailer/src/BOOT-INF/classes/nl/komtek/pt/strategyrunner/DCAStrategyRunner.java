package nl.komtek.pt.strategyrunner;

import com.cf.data.model.generic.CompleteBalance;
import com.cf.data.model.generic.OrderResult;
import com.cf.data.model.generic.Ticker;
import eu.verdelhan.ta4j.Decimal;
import eu.verdelhan.ta4j.TimeSeries;
import eu.verdelhan.ta4j.indicators.trackers.bollinger.BollingerBandsLowerIndicator;
import eu.verdelhan.ta4j.indicators.trackers.bollinger.BollingerBandsUpperIndicator;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import nl.komtek.pt.bot.strategy.helper.StrategyCalculations;
import nl.komtek.pt.bot.strategy.helper.StrategyRunnerHelper;
import nl.komtek.pt.models.AverageCalculator;
import nl.komtek.pt.models.BBAndTimeSeries;
import nl.komtek.pt.models.DCAMonitoringData;
import nl.komtek.pt.models.OriginalBBData;
import nl.komtek.pt.models.ProfitTrailerData;
import nl.komtek.pt.services.ProfitTrailerService;
import nl.komtek.pt.utils.Util;
import nl.komtek.pt.utils.a.c;
import nl.komtek.pt.utils.a.d;
import nl.komtek.pt.utils.a.e;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;







@Component
public class DCAStrategyRunner
{
  private final Logger a = LogManager.getLogger(DCAStrategyRunner.class);
  
  @Autowired
  private ProfitTrailerService profitTrailerService;
  
  public DCAStrategyRunner() {}
  
  @Scheduled(initialDelay=500L, fixedDelay=5000L)
  public void checkDCAStrategy()
  {
    profitTrailerService.e("DCA-HeartBeat", "DCA Heartbeat");
    
    ArrayList localArrayList = new ArrayList();
    
    String str1 = Util.a(e.DCA, "buy_strategy");
    Double localDouble1 = Double.valueOf(Util.d(e.DCA, "buy_value"));
    Double localDouble2 = Double.valueOf(Util.d(e.DCA, "buy_trigger"));
    
    String str2 = Util.a(e.DCA, "sell_strategy");
    Double localDouble3 = Double.valueOf(Util.d(e.DCA, "sell_value"));
    Double localDouble4 = Double.valueOf(Util.d(e.DCA, "sell_trigger"));
    
    Double localDouble5 = Double.valueOf(Util.d(e.DCA, "max_cost"));
    
    String str3 = Util.a(e.PAIRS, "MARKET");
    
    boolean bool1 = Boolean.parseBoolean(Util.b("buy_immediateOrCancel"));
    boolean bool2 = Boolean.parseBoolean(Util.b("buy_fillOrKill"));
    
    int i = runnerHelper.getTotaltradingPairs();
    
    if ((ProfitTrailerService.lastTotal > 0) && (i == 0)) {
      ProfitTrailerService.a("APIPROBLEMS", "API Problems?", 10, a);
      return;
    }
    ProfitTrailerService.lastTotal = i;
    try
    {
      for (String str4 : profitTrailerService.getAllCurrencyPairs())
      {
        if (!StrategyRunnerHelper.K(str4))
        {


          ProfitTrailerService.a(a);
          
          String str5 = Util.c(str4);
          String str6 = Util.d(str4);
          if (str3.contains(str5))
          {

            Map localMap1 = profitTrailerService.getCompleteBalances(str5);
            CompleteBalance localCompleteBalance = (CompleteBalance)localMap1.get(str6);
            
            if ((localCompleteBalance != null) && (!dust) && (runnerHelper.a(str4)) && 
            


              (runnerHelper.w(str4)) && 
              


              (!runnerHelper.x(str4)))
            {


              if ((!bool1) && (!bool2)) {
                ProfitTrailerService.a("IOCDISABLED", "You can't DCA with immediateOrCancel and fillOrKill false", 10, a);
                
                break;
              }
              
              Ticker localTicker = (Ticker)profitTrailerService.getTickers().get(str4);
              if ((localTicker != null) && (highestBid != null) && (lowestAsk != null) && 
                (lowestAsk.doubleValue() > 0.0D) && (highestBid.doubleValue() > 0.0D))
              {

                if (available.doubleValue() > 0.0D) {
                  DCAMonitoringData localDCAMonitoringData = runnerHelper.B(str4);
                  Map localMap2 = profitTrailerService.a(Util.c(str4), str4);
                  
                  List localList = profitTrailerService.a(str4, "-1", localMap2);
                  
                  AverageCalculator localAverageCalculator = AverageCalculator.calculate(str4, localList, localCompleteBalance, localTicker, profitTrailerService
                  



                    .getExchangeInfo(str4));
                  
                  localDCAMonitoringData.setMarket(str4);
                  localDCAMonitoringData.setBuyStrategy(str1);
                  localDCAMonitoringData.setSellStrategy(str2);
                  localDCAMonitoringData.setCurrentPrice(highestBid.doubleValue());
                  localDCAMonitoringData.setPercChange(runnerHelper.a(str4, localTicker).doubleValue());
                  localDCAMonitoringData.setAverageCalculator(localAverageCalculator);
                  
                  double d = runnerHelper.y(str4);
                  localDCAMonitoringData.setVolume(d);
                  
                  a(str1, localDouble1, localDouble2, localDouble5, str4, localTicker, localAverageCalculator, localDCAMonitoringData);
                  a(str2, localDouble3, localDouble4, localDouble5.doubleValue(), str4, localTicker, localCompleteBalance, localAverageCalculator, localDCAMonitoringData);
                  localArrayList.add(localDCAMonitoringData);
                  Util.getProfitTrailerData().backupDCAdata(localDCAMonitoringData);
                } }
            } } } }
      Util.getProfitTrailerData().setDcaLogData(localArrayList);
    } catch (Exception localException) {
      a.error("An error occured", localException);
    }
  }
  

  @Autowired
  private StrategyRunnerHelper runnerHelper;
  @Autowired
  private StrategyCalculations strategyCalculations;
  private void a(String paramString1, Double paramDouble1, Double paramDouble2, double paramDouble, String paramString2, Ticker paramTicker, CompleteBalance paramCompleteBalance, AverageCalculator paramAverageCalculator, DCAMonitoringData paramDCAMonitoringData)
  {
    BigDecimal localBigDecimal1 = lowestAsk;
    nl.komtek.pt.utils.a.a localA = runnerHelper.a();
    double d1 = profitTrailerService.getBalanceForMarket(Util.c(paramString2));
    double d2 = paramAverageCalculator.getTotalAmount() * localBigDecimal1.doubleValue();
    double d3 = paramAverageCalculator.getTotalCost() + d2;
    double d4 = paramAverageCalculator.getAvgCost();
    boolean bool1 = false;
    double d5 = StrategyRunnerHelper.a(paramAverageCalculator.getAvgPrice(), highestBid
      .doubleValue(), paramAverageCalculator
      .getFee());
    
    paramDCAMonitoringData.setProfit(d5);
    d localD = d.valueOf(paramString1);
    switch (DCAStrategyRunner.1.a[localD.ordinal()]) {
    case 1: 
      bool1 = Util.a(paramString2, lowestAsk.doubleValue(), paramDouble1
        .doubleValue(), d5, paramDouble2.doubleValue(), paramAverageCalculator.getFee(), paramAverageCalculator.getAvgPrice(), paramDCAMonitoringData);
      
      break;
    case 2: 
      bool1 = d5 > paramDouble1.doubleValue();
      boolean bool2 = runnerHelper.k(e.DCA, paramString2);
      boolean bool3 = runnerHelper.a(e.DCA, paramString2, d5, paramDouble1.doubleValue());
      if (bool2) {
        bool1 = bool3;
        paramDouble1 = Double.valueOf(profitTrailerService.getTrailingProfit(paramString2));
      }
      paramDCAMonitoringData.setTriggerValue(paramDouble1.doubleValue());
    }
    
    
    if ((!bool1) && (runnerHelper.a(e.DCA, paramString2))) {
      double d6 = runnerHelper.b(e.DCA, paramString2);
      bool1 = d5 < d6;
      boolean bool4 = a(paramDouble, d3, d2, d1);
      
      int i = (localA.equals(nl.komtek.pt.utils.a.a.AVGCOST)) && (a(localA, paramDouble, paramAverageCalculator.getTotalCost(), d4, d1)) ? 1 : 0;
      
      if ((!a(paramDCAMonitoringData)) && ((bool4) || (i != 0)))
      {
        bool1 = false;
      } else if (bool1) {
        paramDCAMonitoringData.setSellStrategy("STOPLOSS");
      }
    }
    
    if (bool1) {
      BigDecimal localBigDecimal2 = highestBid;
      OrderResult localOrderResult = profitTrailerService.a(localD, paramString2, localBigDecimal2, originalAvailable, c.NORMAL);
      if (resultingTrades.size() > 0) {
        runnerHelper.a(paramDCAMonitoringData, localOrderResult);
        runnerHelper.D(paramString2);
      }
    }
  }
  




  private void a(String paramString1, Double paramDouble1, Double paramDouble2, Double paramDouble3, String paramString2, Ticker paramTicker, AverageCalculator paramAverageCalculator, DCAMonitoringData paramDCAMonitoringData)
  {
    double d1 = runnerHelper.a(paramAverageCalculator.getAvgPrice(), lowestAsk.doubleValue());
    double d2 = runnerHelper.i(e.DCA, paramString2);
    paramDCAMonitoringData.setBuyProfit(d1);
    d localD = d.valueOf(paramString1);
    boolean bool1; switch (DCAStrategyRunner.1.a[localD.ordinal()]) {
    case 3: 
      bool1 = a(paramString2, lowestAsk.doubleValue(), paramDouble1.doubleValue(), d1, paramDouble2
        .doubleValue(), paramDCAMonitoringData);
      break;
    case 1: 
      bool1 = b(paramString2, paramAverageCalculator.getAvgPrice(), paramDouble1.doubleValue(), d1, paramDouble2
        .doubleValue(), paramDCAMonitoringData);
      break;
    case 4: 
      bool1 = d1 < paramDouble2.doubleValue();
      boolean bool2 = runnerHelper.l(e.DCA, paramString2);
      boolean bool3 = runnerHelper.a(e.DCA, paramString2, d1, paramDouble1.doubleValue(), false);
      if ((bool1) && (bool2)) {
        paramDCAMonitoringData.setPositive(" trailing...");
        paramDouble2 = Double.valueOf(profitTrailerService.getTrailingBuy(paramDCAMonitoringData.getMarket()));
        paramDCAMonitoringData.setTriggerbb(paramDouble2.doubleValue());
        
        bool1 = bool3;
        paramDouble2 = Double.valueOf(profitTrailerService.getTrailingBuy(paramString2));
        paramDCAMonitoringData.setTriggerbb(paramDouble2.doubleValue());
      }
      break;
    case 5: 
      bool1 = a(d1, paramDCAMonitoringData);
      break;
    case 6: 
      bool1 = strategyCalculations.a(e.DCA, paramString2, lowestAsk.doubleValue(), paramDouble1, Double.valueOf(d2), false, paramDCAMonitoringData);
      if (d1 > paramDouble2.doubleValue()) {
        bool1 = false;
        paramDCAMonitoringData.setPositive(Boolean.toString(bool1));
      }
      break;
    case 7: 
      bool1 = strategyCalculations.b(e.DCA, paramString2, lowestAsk.doubleValue(), paramDouble1, Double.valueOf(d2), false, paramDCAMonitoringData);
      if (d1 > paramDouble2.doubleValue()) {
        bool1 = false;
        paramDCAMonitoringData.setPositive(Boolean.toString(bool1));
      }
      break;
    case 8: 
      bool1 = strategyCalculations.a(e.DCA, paramString2, lowestAsk.doubleValue(), paramDouble1, Double.valueOf(d2), true, paramDCAMonitoringData);
      if (d1 > paramDouble2.doubleValue()) {
        bool1 = false;
        paramDCAMonitoringData.setPositive(Boolean.toString(bool1));
      }
      break;
    case 9: 
      bool1 = strategyCalculations.b(e.DCA, paramString2, lowestAsk.doubleValue(), paramDouble1, Double.valueOf(d2), true, paramDCAMonitoringData);
      if (d1 > paramDouble2.doubleValue()) {
        bool1 = false;
        paramDCAMonitoringData.setPositive(Boolean.toString(bool1));
      }
      break;
    case 10: 
      bool1 = strategyCalculations.a(paramString2, paramDouble1, Double.valueOf(d2), paramDCAMonitoringData);
      if (d1 > paramDouble2.doubleValue()) {
        bool1 = false;
        paramDCAMonitoringData.setPositive(Boolean.toString(bool1));
      }
      break;
    case 11: 
      bool1 = strategyCalculations.b(paramString2, paramDouble1, Double.valueOf(d2), paramDCAMonitoringData);
      if (d1 > paramDouble2.doubleValue()) {
        bool1 = false;
        paramDCAMonitoringData.setPositive(Boolean.toString(bool1));
      }
      break;
    case 2: default: 
      return;
    }
    
    if (a(paramDCAMonitoringData)) {
      return;
    }
    
    if (paramDCAMonitoringData.getVolume() < runnerHelper.j(e.DCA, paramString2)) {
      a.debug("Volume of {} is too low to buy {}", Double.valueOf(paramDCAMonitoringData.getVolume()), paramString2);
      return;
    }
    
    if (bool1)
    {
      if (Util.d(e.DCA, "max_buy_spread") > 0.0D) {
        double d3 = Util.d(e.DCA, "max_buy_spread");
        BigDecimal localBigDecimal2 = lowestAsk;
        BigDecimal localBigDecimal3 = highestBid;
        MathContext localMathContext = new MathContext(6, RoundingMode.HALF_UP);
        BigDecimal localBigDecimal4 = localBigDecimal2.divide(localBigDecimal3, localMathContext).subtract(BigDecimal.ONE).multiply(BigDecimal.valueOf(100L));
        if (localBigDecimal4.doubleValue() > d3) {
          String str1 = String.format("%s spread is too big - current spread:%f , max spread: %f", new Object[] { paramString2, Double.valueOf(localBigDecimal4.doubleValue()), Double.valueOf(d3) });
          ProfitTrailerService.a("DCA-SPREAD", str1, 1, a);
          return;
        }
      }
      
      BigDecimal localBigDecimal1 = lowestAsk;
      nl.komtek.pt.utils.a.a localA = runnerHelper.a();
      double d4 = profitTrailerService.getBalanceForMarket(Util.c(paramString2));
      double d5 = runnerHelper.b(paramString2, d4);
      double d6 = paramAverageCalculator.getTotalAmount() * localBigDecimal1.doubleValue();
      double d7 = paramAverageCalculator.getTotalCost() + d6;
      double d8 = paramAverageCalculator.getAvgCost();
      
      if (a(paramString2, d4, d5, d7, d8)) {
        return;
      }
      
      OrderResult localOrderResult = new OrderResult();
      if (a(paramDouble3.doubleValue(), d7, d6, d4))
      {
        if (!runnerHelper.a(e.DCA, paramString2, paramAverageCalculator.getTotalAmount(), localBigDecimal1)) {
          ProfitTrailerService.a("DCA-orderbook" + paramString2, 
            String.format("There is not enough volume to make a successfull DCA %s", new Object[] { paramString2 }), 1, a);
          

          return;
        }
        double d9 = paramAverageCalculator.getTotalAmount();
        if (localD.equals(d.ANDERSON)) {
          int i = paramDCAMonitoringData.getBoughtTimes();
          String str2 = String.format("buy_trigger_amount_%d", new Object[] { Integer.valueOf(i + 1) });
          double d10 = Util.b(e.DCA, str2, "100");
          d9 = d9 * d10 / 100.0D;
        }
        localOrderResult = profitTrailerService.a(localD, paramString2, localBigDecimal1, BigDecimal.valueOf(d9));
      }
      else if (a(localA, paramDouble3.doubleValue(), paramAverageCalculator.getTotalCost(), d8, d4))
      {
        if (!runnerHelper.a(e.DCA, paramString2, paramAverageCalculator.getTotalAmount(), localBigDecimal1)) {
          ProfitTrailerService.a("DCA-orderbook" + paramString2, 
            String.format("There is not enough volume to make a successfull DCA %s", new Object[] { paramString2 }), 1, a);
          

          return;
        }
        localOrderResult = profitTrailerService.a(localD, paramString2, localBigDecimal1, BigDecimal.valueOf(d8 / localBigDecimal1.doubleValue()));
      }
      

      a.debug("{} result even if nothing happened {}", paramString2, localOrderResult);
      if ((resultingTrades.size() > 0) && (!orderNumber.equals(paramDCAMonitoringData.getLastOrderNumber()))) {
        a.info("{}, mc {}, ctc {}, dbc {}, bb {}", paramString2, paramDouble3, Double.valueOf(d6), Double.valueOf(d7), Double.valueOf(d4));
        paramDCAMonitoringData.setBoughtTimes(paramDCAMonitoringData.getBoughtTimes() + 1);
        paramDCAMonitoringData.setLastOrderNumber(orderNumber);
        paramDCAMonitoringData.setUnfilledAmount(amountUnfilled.doubleValue());
        if (localD.equals(d.ANDERSON)) {
          a.info("DCA - Anderson bought -- currency pair {} - CP {} - BT -", paramString2, Double.valueOf(d1), Double.valueOf(paramDCAMonitoringData.getTriggerbb()));
        } else {
          a.info("DCA - {} bought -- currency pair {} - CP {} - BT {} - amount {}", localD
            .toString(), paramString2, Double.valueOf(d1), paramDouble2, Double.valueOf(paramAverageCalculator.getTotalAmount()));
        }
        Util.getProfitTrailerData().addDCABuyOrder(NumberUtils.toInt(orderNumber));
      }
    }
  }
  
  private boolean a(String paramString, double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4) {
    if ((paramDouble2 > 0.0D) && (paramDouble1 < paramDouble2 + paramDouble3) && (paramDouble1 < paramDouble2 + paramDouble4)) {
      String str = String.format("You are not allowed to buy %s. Buy will exceed min buy balance %s. BAL: %.8f - AVGC: %s", new Object[] { paramString, 
        Double.valueOf(paramDouble2), Double.valueOf(paramDouble1), Double.valueOf(paramDouble4) });
      ProfitTrailerService.a("minBuyBalance", str, 5, a);
      return true;
    }
    return false;
  }
  



  private boolean a(String paramString, double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, DCAMonitoringData paramDCAMonitoringData)
  {
    BBAndTimeSeries localBBAndTimeSeries = nl.komtek.pt.bot.strategy.helper.a.a(paramString);
    TimeSeries localTimeSeries = localBBAndTimeSeries.getTimeSeries();
    if (localTimeSeries.getTickCount() == 0) {
      return false;
    }
    BollingerBandsLowerIndicator localBollingerBandsLowerIndicator = localBBAndTimeSeries.getBBLow();
    Decimal localDecimal1 = nl.komtek.pt.bot.strategy.helper.a.a(localTimeSeries, localTimeSeries.getEnd());
    Object localObject = nl.komtek.pt.bot.strategy.helper.a.a((Decimal)localBollingerBandsLowerIndicator.getValue(localTimeSeries.getEnd()), localDecimal1, paramDouble2);
    boolean bool1 = (((Decimal)localObject).isGreaterThanOrEqual(Decimal.valueOf(paramDouble1))) && (paramDouble3 <= paramDouble4);
    String str = Boolean.toString(bool1);
    boolean bool2 = runnerHelper.l(e.DCA, paramString);
    
    OriginalBBData localOriginalBBData = profitTrailerService.getOriginalBBTrigger(paramString);
    if ((localOriginalBBData.getOriginalTrigger() <= 0.0D) && (bool1) && (bool2)) {
      localOriginalBBData = new OriginalBBData();
      localOriginalBBData.setOriginalBBValue(((Decimal)localBollingerBandsLowerIndicator.getValue(localTimeSeries.getEnd())).toDouble());
      localOriginalBBData.setOriginalTrigger(((Decimal)localObject).toDouble());
      localOriginalBBData.setOriginalWidth(localDecimal1.toDouble());
      localOriginalBBData = profitTrailerService.updateOriginalBBTrigger(paramString, localOriginalBBData);
      a.info(String.format("LOWBB trailing started - setting original trigger %.8f for %s", new Object[] {
        Util.a(localOriginalBBData.getOriginalTrigger()), paramString }));
    }
    

    double d = nl.komtek.pt.bot.strategy.helper.a.a((Decimal)localBollingerBandsLowerIndicator.getValue(localTimeSeries.getEnd()), localDecimal1, (Decimal)localObject);
    if (localOriginalBBData.getOriginalTrigger() > 0.0D) {
      d = nl.komtek.pt.bot.strategy.helper.a.a((Decimal)localBollingerBandsLowerIndicator.getValue(localTimeSeries.getEnd()), localDecimal1, (Decimal)localObject);
    }
    
    Decimal localDecimal2 = Decimal.valueOf(Util.a(profitTrailerService.getTrailingBuy(paramString)).doubleValue());
    if ((bool2) && ((bool1) || (localDecimal2.isGreaterThan(Decimal.ZERO))) && (paramDouble3 <= paramDouble4))
    {
      if (bool1) {
        str = str + " trailing...";
      }
      bool1 = runnerHelper.a(e.DCA, paramString, paramDouble1, ((Decimal)localObject)
        .toDouble(), localOriginalBBData.getOriginalTrigger());
      localObject = localDecimal2;
    }
    
    paramDCAMonitoringData.setLowbb(((Decimal)localBollingerBandsLowerIndicator.getValue(localTimeSeries.getEnd())).toDouble());
    paramDCAMonitoringData.setTriggerbb(((Decimal)localObject).toDouble());
    paramDCAMonitoringData.setPositive(str);
    return bool1;
  }
  

  private boolean b(String paramString, double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, DCAMonitoringData paramDCAMonitoringData)
  {
    BBAndTimeSeries localBBAndTimeSeries = nl.komtek.pt.bot.strategy.helper.a.b(paramString);
    TimeSeries localTimeSeries = localBBAndTimeSeries.getTimeSeries();
    if (localTimeSeries.getTickCount() == 0) {
      return false;
    }
    BollingerBandsUpperIndicator localBollingerBandsUpperIndicator = localBBAndTimeSeries.getBBHigh();
    
    Decimal localDecimal1 = nl.komtek.pt.bot.strategy.helper.a.a(localTimeSeries, localTimeSeries.getEnd());
    Decimal localDecimal2 = nl.komtek.pt.bot.strategy.helper.a.b((Decimal)localBollingerBandsUpperIndicator.getValue(localTimeSeries.getEnd()), localDecimal1, paramDouble2);
    boolean bool = (localDecimal2.isLessThanOrEqual(Decimal.valueOf(paramDouble1))) && (paramDouble3 <= paramDouble4);
    
    paramDCAMonitoringData.setHighbb(((Decimal)localBollingerBandsUpperIndicator.getValue(localTimeSeries.getEnd())).toDouble());
    paramDCAMonitoringData.setTriggerbb(localDecimal2.toDouble());
    paramDCAMonitoringData.setPositive(Boolean.toString(bool));
    
    return bool;
  }
  

  private boolean a(double paramDouble, DCAMonitoringData paramDCAMonitoringData)
  {
    int i = paramDCAMonitoringData.getBoughtTimes();
    int j = Util.c(e.DCA, "max_buy_times");
    String str1 = String.format("buy_trigger_%d", new Object[] { Integer.valueOf(i + 1) });
    double d = Util.b(e.DCA, str1, "0");
    if ((j == 0) && (d == 0.0D)) {
      ProfitTrailerService.a("ANDERSON-BT", String.format("Buy trigger %s not specified", new Object[] { Integer.valueOf(i + 1) }), 5, a);
      return false;
    }
    
    boolean bool1 = (d < 0.0D) && (paramDouble < d);
    String str2 = Boolean.toString(bool1);
    
    boolean bool2 = runnerHelper.l(e.DCA, paramDCAMonitoringData.getMarket());
    boolean bool3 = runnerHelper.a(e.DCA, paramDCAMonitoringData.getMarket(), paramDouble, d, false);
    if ((bool1) && (bool2)) {
      str2 = str2 + " trailing...";
      bool1 = bool3;
      d = profitTrailerService.getTrailingBuy(paramDCAMonitoringData.getMarket());
    }
    
    paramDCAMonitoringData.setTriggerbb(d);
    paramDCAMonitoringData.setPositive(str2);
    return bool1;
  }
  
  private boolean a(DCAMonitoringData paramDCAMonitoringData) {
    int i = paramDCAMonitoringData.getBoughtTimes();
    int j = Util.c(e.DCA, "max_buy_times");
    return (j > 0) && (i >= j);
  }
  
  private boolean a(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4)
  {
    return ((paramDouble1 == 0.0D) || (paramDouble2 < paramDouble1)) && (paramDouble3 <= paramDouble4);
  }
  

  private boolean a(nl.komtek.pt.utils.a.a paramA, double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4)
  {
    return (paramA.equals(nl.komtek.pt.utils.a.a.AVGCOST)) && ((paramDouble1 == 0.0D) || (paramDouble2 + paramDouble3 < paramDouble1)) && (paramDouble3 <= paramDouble4);
  }
}
