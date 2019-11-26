package nl.komtek.pt.models;

import java.io.Serializable;

public class GainMonitoringData extends MonitoringData implements Serializable
{
  private double combinedProfit;
  
  public GainMonitoringData() {}
  
  public void setCombinedProfit(double combinedProfit)
  {
    this.combinedProfit = combinedProfit;
  }
  
  public double getCombinedProfit() {
    return combinedProfit;
  }
}
