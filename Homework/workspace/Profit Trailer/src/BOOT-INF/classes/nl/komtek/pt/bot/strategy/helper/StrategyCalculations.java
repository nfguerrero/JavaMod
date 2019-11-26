package nl.komtek.pt.bot.strategy.helper;

import eu.verdelhan.ta4j.Decimal;
import eu.verdelhan.ta4j.TimeSeries;
import eu.verdelhan.ta4j.indicators.simple.ClosePriceIndicator;
import eu.verdelhan.ta4j.indicators.trackers.EMAIndicator;
import eu.verdelhan.ta4j.indicators.trackers.SMAIndicator;
import java.util.Calendar;
import nl.komtek.pt.models.BuyMonitoringData;
import nl.komtek.pt.models.DCAMonitoringData;
import nl.komtek.pt.models.MonitoringData;
import nl.komtek.pt.services.ProfitTrailerService;
import nl.komtek.pt.utils.Util;
import nl.komtek.pt.utils.a.e;
import org.springframework.beans.factory.annotation.Autowired;

@org.springframework.stereotype.Component
public class StrategyCalculations
{
  @Autowired
  private ProfitTrailerService profitTrailerService;
  @Autowired
  private StrategyRunnerHelper runnerHelper;
  
  public StrategyCalculations() {}
  
  public boolean a(e paramE, String paramString, double paramDouble, Double paramDouble1, Double paramDouble2, boolean paramBoolean, MonitoringData paramMonitoringData)
  {
    int i = Util.c(e.INDICATORS, "SMA_period");
    int j = Util.c(e.INDICATORS, "SMA_1");
    int k = Util.c(e.INDICATORS, "SMA_2");
    int m = j > k ? j : k;
    Calendar localCalendar = b.a();
    localCalendar.add(5, -1);
    
    TimeSeries localTimeSeries = c.a(paramString, i, m);
    if (localTimeSeries.getTickCount() == 0) {
      return false;
    }
    ClosePriceIndicator localClosePriceIndicator = new ClosePriceIndicator(localTimeSeries);
    SMAIndicator localSMAIndicator1 = new SMAIndicator(localClosePriceIndicator, j);
    SMAIndicator localSMAIndicator2 = new SMAIndicator(localClosePriceIndicator, k);
    double d1 = ((Decimal)localSMAIndicator1.getValue(localClosePriceIndicator.getTimeSeries().getEnd())).toDouble();
    double d2 = ((Decimal)localSMAIndicator2.getValue(localClosePriceIndicator.getTimeSeries().getEnd())).toDouble();
    double d3 = d1 < d2 ? d1 : d2;
    
    double d4;
    boolean bool1;
    if (paramBoolean) {
      d4 = (d2 / d1 - 1.0D) * 100.0D;
      bool1 = d4 > paramDouble1.doubleValue();
      if ((paramDouble2.doubleValue() > 0.0D) && (d4 > paramDouble2.doubleValue())) {
        bool1 = false;
      }
      if (paramDouble1.doubleValue() <= 0.0D) {
        d4 = (d2 / d1 - 1.0D) * 100.0D;
        bool1 = d4 <= paramDouble1.doubleValue();
        if ((paramDouble2.doubleValue() < 0.0D) && (d4 < paramDouble2.doubleValue())) {
          bool1 = false;
        }
      }
    } else {
      d4 = (paramDouble / d3 - 1.0D) * 100.0D;
      bool1 = d4 <= paramDouble1.doubleValue();
    }
    
    String str = Boolean.toString(bool1);
    boolean bool2 = runnerHelper.l(paramE, paramString);
    boolean bool3 = runnerHelper.a(paramE, paramString, d4, paramDouble1.doubleValue(), (paramBoolean) && (paramDouble1.doubleValue() > 0.0D));
    if (bool2) {
      if (bool1) {
        str = str + " trailing...";
      }
      bool1 = bool3;
      paramDouble1 = Double.valueOf(profitTrailerService.getTrailingBuy(paramString));
    }
    if ((paramMonitoringData instanceof BuyMonitoringData)) {
      ((BuyMonitoringData)paramMonitoringData).setCurrentValue(d4);
      paramMonitoringData.setTriggerValue(paramDouble1.doubleValue());
      ((BuyMonitoringData)paramMonitoringData).setPositive(str);
    } else if ((paramMonitoringData instanceof DCAMonitoringData)) {
      ((DCAMonitoringData)paramMonitoringData).setLowbb(d4);
      ((DCAMonitoringData)paramMonitoringData).setTriggerbb(paramDouble1.doubleValue());
      ((DCAMonitoringData)paramMonitoringData).setPositive(str);
    }
    
    return bool1;
  }
  

  public boolean b(e paramE, String paramString, double paramDouble, Double paramDouble1, Double paramDouble2, boolean paramBoolean, MonitoringData paramMonitoringData)
  {
    int i = Util.c(e.INDICATORS, "EMA_period");
    int j = Util.c(e.INDICATORS, "EMA_1");
    int k = Util.c(e.INDICATORS, "EMA_2");
    int m = j > k ? j : k;
    
    Calendar localCalendar = b.a();
    localCalendar.add(5, -1);
    
    TimeSeries localTimeSeries = c.a(paramString, i, m);
    if (localTimeSeries.getTickCount() == 0) {
      return false;
    }
    ClosePriceIndicator localClosePriceIndicator = new ClosePriceIndicator(localTimeSeries);
    EMAIndicator localEMAIndicator1 = new EMAIndicator(localClosePriceIndicator, j);
    EMAIndicator localEMAIndicator2 = new EMAIndicator(localClosePriceIndicator, k);
    
    double d1 = ((Decimal)localEMAIndicator1.getValue(localClosePriceIndicator.getTimeSeries().getEnd())).toDouble();
    double d2 = ((Decimal)localEMAIndicator2.getValue(localClosePriceIndicator.getTimeSeries().getEnd())).toDouble();
    double d3 = d1 < d2 ? d1 : d2;
    
    double d4;
    boolean bool1;
    if (paramBoolean) {
      d4 = (d2 / d1 - 1.0D) * 100.0D;
      bool1 = d4 > paramDouble1.doubleValue();
      if ((paramDouble2.doubleValue() > 0.0D) && (d4 > paramDouble2.doubleValue())) {
        bool1 = false;
      }
      if (paramDouble1.doubleValue() <= 0.0D) {
        d4 = (d2 / d1 - 1.0D) * 100.0D;
        bool1 = d4 <= paramDouble1.doubleValue();
        if ((paramDouble2.doubleValue() < 0.0D) && (d4 < paramDouble2.doubleValue())) {
          bool1 = false;
        }
      }
    } else {
      d4 = (paramDouble / d3 - 1.0D) * 100.0D;
      bool1 = d4 <= paramDouble1.doubleValue();
    }
    
    String str = Boolean.toString(bool1);
    boolean bool2 = runnerHelper.l(paramE, paramString);
    boolean bool3 = runnerHelper.a(paramE, paramString, d4, paramDouble1.doubleValue(), (paramBoolean) && (paramDouble1.doubleValue() > 0.0D));
    if (bool2) {
      if (bool1) {
        str = str + " trailing...";
      }
      bool1 = bool3;
      paramDouble1 = Double.valueOf(profitTrailerService.getTrailingBuy(paramString));
    }
    
    if ((paramMonitoringData instanceof BuyMonitoringData)) {
      ((BuyMonitoringData)paramMonitoringData).setCurrentValue(d4);
      ((BuyMonitoringData)paramMonitoringData).setPositive(str);
      paramMonitoringData.setTriggerValue(paramDouble1.doubleValue());
    } else if ((paramMonitoringData instanceof DCAMonitoringData)) {
      ((DCAMonitoringData)paramMonitoringData).setLowbb(d4);
      ((DCAMonitoringData)paramMonitoringData).setTriggerbb(paramDouble1.doubleValue());
      ((DCAMonitoringData)paramMonitoringData).setPositive(str);
    }
    
    return bool1;
  }
  
  public boolean a(String paramString, Double paramDouble1, Double paramDouble2, MonitoringData paramMonitoringData)
  {
    int i = Util.c(e.INDICATORS, "SMA_period");
    int j = Util.c(e.INDICATORS, "SMA_1");
    int k = Util.c(e.INDICATORS, "SMA_2");
    int m = Util.c(e.INDICATORS, "SMA_cross_candles");
    int n = j > k ? j : k;
    
    TimeSeries localTimeSeries = c.a(paramString, i, n);
    if (localTimeSeries.getTickCount() == 0) {
      return false;
    }
    ClosePriceIndicator localClosePriceIndicator = new ClosePriceIndicator(localTimeSeries);
    SMAIndicator localSMAIndicator1 = new SMAIndicator(localClosePriceIndicator, j);
    SMAIndicator localSMAIndicator2 = new SMAIndicator(localClosePriceIndicator, k);
    double d1 = ((Decimal)localSMAIndicator1.getValue(localClosePriceIndicator.getTimeSeries().getEnd())).toDouble();
    double d2 = ((Decimal)localSMAIndicator2.getValue(localClosePriceIndicator.getTimeSeries().getEnd())).toDouble();
    int i1 = localClosePriceIndicator.getTimeSeries().getEnd() - m;
    double d3 = ((Decimal)localSMAIndicator1.getValue(i1)).toDouble();
    double d4 = ((Decimal)localSMAIndicator2.getValue(i1)).toDouble();
    double d5 = (d2 / d1 - 1.0D) * 100.0D;
    double d6 = (d4 / d3 - 1.0D) * 100.0D;
    
    boolean bool = false;
    if ((paramDouble1.doubleValue() > 0.0D) && (d5 > paramDouble1.doubleValue()) && (d6 < 0.0D)) {
      bool = true;
      if ((paramDouble2.doubleValue() > 0.0D) && (d5 > paramDouble2.doubleValue())) {
        bool = false;
      }
    }
    
    if ((paramDouble1.doubleValue() <= 0.0D) && (d5 < paramDouble1.doubleValue()) && (d6 > 0.0D)) {
      bool = true;
      if ((paramDouble2.doubleValue() < 0.0D) && (d5 < paramDouble2.doubleValue())) {
        bool = false;
      }
    }
    
    if ((paramMonitoringData instanceof BuyMonitoringData)) {
      ((BuyMonitoringData)paramMonitoringData).setCurrentValue(d5);
      paramMonitoringData.setTriggerValue(paramDouble1.doubleValue());
    } else if ((paramMonitoringData instanceof DCAMonitoringData)) {
      ((DCAMonitoringData)paramMonitoringData).setLowbb(d5);
      ((DCAMonitoringData)paramMonitoringData).setTriggerbb(paramDouble1.doubleValue());
    }
    return bool;
  }
  
  public boolean b(String paramString, Double paramDouble1, Double paramDouble2, MonitoringData paramMonitoringData)
  {
    int i = Util.c(e.INDICATORS, "EMA_period");
    int j = Util.c(e.INDICATORS, "EMA_1");
    int k = Util.c(e.INDICATORS, "EMA_2");
    int m = Util.c(e.INDICATORS, "EMA_cross_candles");
    int n = j > k ? j : k;
    
    TimeSeries localTimeSeries = c.a(paramString, i, n);
    if (localTimeSeries.getTickCount() == 0) {
      return false;
    }
    ClosePriceIndicator localClosePriceIndicator = new ClosePriceIndicator(localTimeSeries);
    EMAIndicator localEMAIndicator1 = new EMAIndicator(localClosePriceIndicator, j);
    EMAIndicator localEMAIndicator2 = new EMAIndicator(localClosePriceIndicator, k);
    
    double d1 = ((Decimal)localEMAIndicator1.getValue(localClosePriceIndicator.getTimeSeries().getEnd())).toDouble();
    double d2 = ((Decimal)localEMAIndicator2.getValue(localClosePriceIndicator.getTimeSeries().getEnd())).toDouble();
    int i1 = localClosePriceIndicator.getTimeSeries().getEnd() - m;
    double d3 = ((Decimal)localEMAIndicator1.getValue(i1)).toDouble();
    double d4 = ((Decimal)localEMAIndicator2.getValue(i1)).toDouble();
    double d5 = (d2 / d1 - 1.0D) * 100.0D;
    double d6 = (d4 / d3 - 1.0D) * 100.0D;
    
    boolean bool = false;
    if ((paramDouble1.doubleValue() > 0.0D) && (d5 > paramDouble1.doubleValue()) && (d6 < 0.0D)) {
      bool = true;
      if ((paramDouble2.doubleValue() > 0.0D) && (d5 > paramDouble2.doubleValue())) {
        bool = false;
      }
    }
    
    if ((paramDouble1.doubleValue() <= 0.0D) && (d5 < paramDouble1.doubleValue()) && (d6 > 0.0D)) {
      bool = true;
      if ((paramDouble2.doubleValue() < 0.0D) && (d5 < paramDouble2.doubleValue())) {
        bool = false;
      }
    }
    

    if ((paramMonitoringData instanceof BuyMonitoringData)) {
      ((BuyMonitoringData)paramMonitoringData).setCurrentValue(d5);
      ((BuyMonitoringData)paramMonitoringData).setPositive(String.valueOf(bool));
      paramMonitoringData.setTriggerValue(paramDouble1.doubleValue());
    } else if ((paramMonitoringData instanceof DCAMonitoringData)) {
      ((DCAMonitoringData)paramMonitoringData).setLowbb(d5);
      ((DCAMonitoringData)paramMonitoringData).setTriggerbb(paramDouble1.doubleValue());
      ((DCAMonitoringData)paramMonitoringData).setPositive(String.valueOf(bool));
    }
    return bool;
  }
}
