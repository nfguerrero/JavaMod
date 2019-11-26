package nl.komtek.pt.bot.strategy.helper;

import eu.verdelhan.ta4j.Decimal;
import eu.verdelhan.ta4j.Tick;
import eu.verdelhan.ta4j.TimeSeries;
import eu.verdelhan.ta4j.indicators.simple.ClosePriceIndicator;
import eu.verdelhan.ta4j.indicators.statistics.StandardDeviationIndicator;
import eu.verdelhan.ta4j.indicators.trackers.SMAIndicator;
import eu.verdelhan.ta4j.indicators.trackers.bollinger.BollingerBandsLowerIndicator;
import eu.verdelhan.ta4j.indicators.trackers.bollinger.BollingerBandsMiddleIndicator;
import eu.verdelhan.ta4j.indicators.trackers.bollinger.BollingerBandsUpperIndicator;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import nl.komtek.pt.models.BBAndTimeSeries;
import nl.komtek.pt.utils.Util;
import nl.komtek.pt.utils.a.e;
import org.joda.time.DateTime;


public class a
{
  public a() {}
  
  public static BBAndTimeSeries a(String paramString)
  {
    int i = Util.c(e.INDICATORS, "BB_period");
    int j = Util.c(e.INDICATORS, "BB_sma");
    double d = Util.b(e.INDICATORS, "BB_std", "2");
    TimeSeries localTimeSeries = c.a(paramString, i, j);
    ClosePriceIndicator localClosePriceIndicator = new ClosePriceIndicator(localTimeSeries);
    BollingerBandsLowerIndicator localBollingerBandsLowerIndicator = a(localClosePriceIndicator, j, d);
    return new BBAndTimeSeries(localBollingerBandsLowerIndicator, localTimeSeries);
  }
  
  public static BBAndTimeSeries b(String paramString) {
    int i = Util.c(e.INDICATORS, "BB_period");
    int j = Util.c(e.INDICATORS, "BB_sma");
    double d = Util.b(e.INDICATORS, "BB_std", "2");
    
    TimeSeries localTimeSeries = c.a(paramString, i, j);
    ClosePriceIndicator localClosePriceIndicator = new ClosePriceIndicator(localTimeSeries);
    BollingerBandsUpperIndicator localBollingerBandsUpperIndicator = b(localClosePriceIndicator, j, d);
    return new BBAndTimeSeries(localBollingerBandsUpperIndicator, localTimeSeries);
  }
  
  public static BBAndTimeSeries a(String paramString, int paramInt)
  {
    int i = Util.c(e.INDICATORS, "BB_period");
    int j = Util.c(e.INDICATORS, "BB_sma");
    double d = Util.b(e.INDICATORS, "BB_std", "2");
    
    TimeSeries localTimeSeries = c.a(paramString, i, j);
    ClosePriceIndicator localClosePriceIndicator = new ClosePriceIndicator(localTimeSeries);
    Decimal localDecimal = a(localClosePriceIndicator, j, d, paramInt);
    return new BBAndTimeSeries(localDecimal, localTimeSeries);
  }
  
  public static Decimal a(TimeSeries paramTimeSeries, int paramInt) {
    int i = Util.c(e.INDICATORS, "BB_sma");
    double d = Util.b(e.INDICATORS, "BB_std", "2");
    
    ClosePriceIndicator localClosePriceIndicator = new ClosePriceIndicator(paramTimeSeries);
    return a(localClosePriceIndicator, i, d, paramInt);
  }
  
  private static BollingerBandsLowerIndicator a(ClosePriceIndicator paramClosePriceIndicator, int paramInt, double paramDouble) {
    SMAIndicator localSMAIndicator = new SMAIndicator(paramClosePriceIndicator, paramInt);
    StandardDeviationIndicator localStandardDeviationIndicator = new StandardDeviationIndicator(paramClosePriceIndicator, paramInt);
    BollingerBandsMiddleIndicator localBollingerBandsMiddleIndicator = new BollingerBandsMiddleIndicator(localSMAIndicator);
    return new BollingerBandsLowerIndicator(localBollingerBandsMiddleIndicator, localStandardDeviationIndicator, Decimal.valueOf(paramDouble));
  }
  
  private static BollingerBandsUpperIndicator b(ClosePriceIndicator paramClosePriceIndicator, int paramInt, double paramDouble) {
    SMAIndicator localSMAIndicator = new SMAIndicator(paramClosePriceIndicator, paramInt);
    StandardDeviationIndicator localStandardDeviationIndicator = new StandardDeviationIndicator(paramClosePriceIndicator, paramInt);
    BollingerBandsMiddleIndicator localBollingerBandsMiddleIndicator = new BollingerBandsMiddleIndicator(localSMAIndicator);
    return new BollingerBandsUpperIndicator(localBollingerBandsMiddleIndicator, localStandardDeviationIndicator, Decimal.valueOf(paramDouble));
  }
  
  private static Decimal a(ClosePriceIndicator paramClosePriceIndicator, int paramInt1, double paramDouble, int paramInt2) {
    SMAIndicator localSMAIndicator = new SMAIndicator(paramClosePriceIndicator, paramInt1);
    StandardDeviationIndicator localStandardDeviationIndicator = new StandardDeviationIndicator(paramClosePriceIndicator, paramInt1);
    BollingerBandsMiddleIndicator localBollingerBandsMiddleIndicator = new BollingerBandsMiddleIndicator(localSMAIndicator);
    BollingerBandsLowerIndicator localBollingerBandsLowerIndicator = new BollingerBandsLowerIndicator(localBollingerBandsMiddleIndicator, localStandardDeviationIndicator, Decimal.valueOf(paramDouble));
    BollingerBandsUpperIndicator localBollingerBandsUpperIndicator = new BollingerBandsUpperIndicator(localBollingerBandsMiddleIndicator, localStandardDeviationIndicator, Decimal.valueOf(paramDouble));
    return ((Decimal)localBollingerBandsUpperIndicator.getValue(paramInt2)).minus((Decimal)localBollingerBandsLowerIndicator.getValue(paramInt2));
  }
  
  public static Decimal a(Decimal paramDecimal1, Decimal paramDecimal2, double paramDouble) {
    Decimal localDecimal = Decimal.ZERO;
    if (paramDouble != 0.0D) {
      localDecimal = paramDecimal2.multipliedBy(Decimal.valueOf(Math.abs(paramDouble) / 100.0D));
    }
    
    if (paramDouble > 0.0D) {
      return paramDecimal1.plus(localDecimal);
    }
    return paramDecimal1.minus(localDecimal);
  }
  
  public static double a(Decimal paramDecimal1, Decimal paramDecimal2, Decimal paramDecimal3)
  {
    Decimal localDecimal = paramDecimal3.minus(paramDecimal1);
    return localDecimal.dividedBy(paramDecimal2).multipliedBy(Decimal.HUNDRED).toDouble();
  }
  
  public static Decimal b(Decimal paramDecimal1, Decimal paramDecimal2, double paramDouble)
  {
    Decimal localDecimal = Decimal.ZERO;
    if (paramDouble != 0.0D) {
      localDecimal = paramDecimal2.multipliedBy(Decimal.valueOf(Math.abs(paramDouble) / 100.0D));
    }
    if (paramDouble > 0.0D) {
      return paramDecimal1.minus(localDecimal);
    }
    return paramDecimal1.plus(localDecimal);
  }
  
  public static Decimal c(String paramString)
  {
    TimeSeries localTimeSeries = c.a(paramString, 300L, 288);
    Decimal localDecimal = Decimal.ZERO;
    LocalDateTime localLocalDateTime = LocalDateTime.now(ZoneId.of("UTC")).minusDays(1L);
    for (int i = 0; i < localTimeSeries.getTickCount(); i++) {
      Tick localTick = localTimeSeries.getTick(i);
      if (localTick.getEndTime().getMillis() >= localLocalDateTime.toEpochSecond(ZoneOffset.UTC) * 1000L)
      {

        localDecimal = localDecimal.plus(localTimeSeries.getTick(i).getVolume()); }
    }
    return localDecimal;
  }
}
