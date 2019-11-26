package nl.komtek.pt.models;

import com.google.gson.Gson;
import java.io.Serializable;
import nl.komtek.pt.utils.Util;



public class BuyMonitoringData
  extends MonitoringData
  implements Serializable
{
  private double BBLow;
  private double BBTrigger;
  private String positive = Boolean.toString(false);
  private double BBHigh;
  
  public BuyMonitoringData() {}
  
  public void setLowbb(double BBLow) { this.BBLow = BBLow; }
  
  private double currentValue;
  public double getLowbb() {
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
    BBHigh = highbb;
  }
  
  public double getHighbb() {
    return BBHigh;
  }
  
  public String toString()
  {
    return new Gson().toJson(this);
  }
  
  public void setCurrentValue(double currentValue) {
    if (Double.isNaN(currentValue)) {
      currentValue = 0.0D;
    }
    this.currentValue = currentValue;
  }
  
  public double getCurrentValue() {
    return Util.c(currentValue).doubleValue();
  }
}
