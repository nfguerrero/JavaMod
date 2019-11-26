package nl.komtek.pt.models;

import eu.verdelhan.ta4j.Decimal;
import eu.verdelhan.ta4j.TimeSeries;
import eu.verdelhan.ta4j.indicators.CachedIndicator;
import eu.verdelhan.ta4j.indicators.trackers.bollinger.BollingerBandsLowerIndicator;

public class BBAndTimeSeries
{
  private final TimeSeries timeSeries;
  private CachedIndicator cachedIndicator;
  private Decimal bbWidth;
  
  public BBAndTimeSeries(CachedIndicator bb, TimeSeries timeSeries)
  {
    this.timeSeries = timeSeries;
    cachedIndicator = bb;
  }
  
  public BBAndTimeSeries(Decimal bbWidth, TimeSeries timeSeries) {
    this.timeSeries = timeSeries;
    this.bbWidth = bbWidth;
  }
  
  public TimeSeries getTimeSeries() {
    return timeSeries;
  }
  
  public BollingerBandsLowerIndicator getBBLow() {
    return (BollingerBandsLowerIndicator)cachedIndicator;
  }
  
  public eu.verdelhan.ta4j.indicators.trackers.bollinger.BollingerBandsUpperIndicator getBBHigh() {
    return (eu.verdelhan.ta4j.indicators.trackers.bollinger.BollingerBandsUpperIndicator)cachedIndicator;
  }
  
  public Decimal getBBWidth() {
    return bbWidth;
  }
}
