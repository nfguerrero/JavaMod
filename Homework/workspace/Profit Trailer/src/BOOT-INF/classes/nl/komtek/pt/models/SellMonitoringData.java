package nl.komtek.pt.models;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import nl.komtek.pt.utils.Util;

public class SellMonitoringData
  extends MonitoringData implements Serializable
{
  private double soldAmount;
  private LocalDateTime soldDate;
  private int boughtTimes;
  
  public SellMonitoringData() {}
  
  public double getTrueCost()
  {
    return averageCalculator.getAvgPrice() * soldAmount;
  }
  
  public double getTotalAmount() {
    return averageCalculator.getTotalAmount();
  }
  
  public double getCurrentValue() {
    return soldAmount * currentPrice;
  }
  
  public void setSoldDate(LocalDateTime soldTime) {
    soldDate = soldTime;
  }
  
  public String getDate() {
    if (soldDate == null) {
      return "";
    }
    
    String str = Util.a("server.timeZoneOffset");
    OffsetDateTime localOffsetDateTime = soldDate.atOffset(ZoneOffset.UTC);
    if (str != null) {
      localOffsetDateTime = localOffsetDateTime.withOffsetSameInstant(ZoneOffset.of(str));
    }
    return localOffsetDateTime.format(Util.getDateFormatter());
  }
  
  public double getSoldAmount() {
    return soldAmount;
  }
  
  public void setSoldAmount(double soldAmount) {
    this.soldAmount = soldAmount;
  }
  
  public LocalDateTime getDateValue() {
    if (soldDate == null) {
      return soldDate;
    }
    
    String str = Util.a("server.timeZoneOffset");
    OffsetDateTime localOffsetDateTime = soldDate.atOffset(ZoneOffset.UTC);
    if (str != null) {
      localOffsetDateTime = localOffsetDateTime.withOffsetSameInstant(ZoneOffset.of(str));
    }
    return localOffsetDateTime.toLocalDateTime();
  }
  
  public void setBoughtTimes(int boughtTimes) {
    this.boughtTimes = boughtTimes;
  }
  
  public int getBoughtTimes() {
    return boughtTimes;
  }
  
  public double getProfitBTC() {
    double d = getCurrentValue() * (1.0D - getAverageCalculator().getFee() / 100.0D);
    
    return d - getAveragePrice() * getSoldAmount();
  }
}
