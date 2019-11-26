package nl.komtek.pt.models;

import com.google.gson.Gson;
import java.io.Serializable;
import nl.komtek.pt.utils.Util;



public class DCAMonitoringData
  extends MonitoringData
  implements Serializable
{
  private double BBLow;
  private double BBTrigger;
  private String positive = Boolean.toString(false);
  private double highbb;
  private int boughtTimes;
  private double buyProfit;
  private String lastOrderNumber = "";
  
  public DCAMonitoringData() {}
  
  public void setLowbb(double BBLow) { this.BBLow = BBLow; }
  
  public double getLowbb()
  {
    return Util.a(BBLow).doubleValue();
  }
  
  public void setTriggerbb(double BBTrigger) {
    this.BBTrigger = BBTrigger;
  }
  
  public double getTriggerbb() {
    return Util.a(BBTrigger).doubleValue();
  }
  
  public void setPositive(String positive) {
    this.positive = positive;
  }
  
  public String getPositive() {
    return positive;
  }
  
  public void setHighbb(double highbb) {
    this.highbb = highbb;
  }
  
  public double getHighbb() {
    return highbb;
  }
  
  public int getBoughtTimes() {
    return boughtTimes;
  }
  
  public void setBoughtTimes(int boughtTimes) {
    this.boughtTimes = boughtTimes;
  }
  
  public double getBuyProfit() {
    return buyProfit;
  }
  
  public void setBuyProfit(double buyProfit) {
    this.buyProfit = buyProfit;
  }
  
  public String getLastOrderNumber() {
    return lastOrderNumber;
  }
  
  public void setLastOrderNumber(String lastOrderNumber) {
    this.lastOrderNumber = lastOrderNumber;
  }
  
  public boolean equals(Object o)
  {
    if (this == o) {
      return true;
    }
    if ((o == null) || (getClass() != o.getClass())) {
      return false;
    }
    
    DCAMonitoringData localDCAMonitoringData = (DCAMonitoringData)o;
    
    if (Double.compare(BBLow, BBLow) != 0) {
      return false;
    }
    if (Double.compare(BBTrigger, BBTrigger) != 0) {
      return false;
    }
    if (positive != positive) {
      return false;
    }
    if (Double.compare(highbb, highbb) != 0) {
      return false;
    }
    if (boughtTimes != boughtTimes) {
      return false;
    }
    if (Double.compare(buyProfit, buyProfit) != 0) {
      return false;
    }
    return lastOrderNumber == null ? true : lastOrderNumber != null ? lastOrderNumber.equals(lastOrderNumber) : false;
  }
  

  private double unfilledAmount;
  public int hashCode()
  {
    long l = Double.doubleToLongBits(BBLow);
    int i = (int)(l ^ l >>> 32);
    l = Double.doubleToLongBits(BBTrigger);
    i = 31 * i + (int)(l ^ l >>> 32);
    i = 31 * i + positive.hashCode();
    l = Double.doubleToLongBits(highbb);
    i = 31 * i + (int)(l ^ l >>> 32);
    i = 31 * i + boughtTimes;
    l = Double.doubleToLongBits(buyProfit);
    i = 31 * i + (int)(l ^ l >>> 32);
    i = 31 * i + (lastOrderNumber != null ? lastOrderNumber.hashCode() : 0);
    return i;
  }
  
  public double getUnfilledAmount() {
    return unfilledAmount;
  }
  
  public void setUnfilledAmount(double unfilledAmount) {
    this.unfilledAmount = unfilledAmount;
  }
  
  public String toString()
  {
    return new Gson().toJson(this);
  }
}
