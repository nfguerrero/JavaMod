package nl.komtek.pt.models;

import nl.komtek.pt.utils.Util;

public class MonitoringData implements java.io.Serializable
{
  protected String market;
  protected double profit;
  protected AverageCalculator averageCalculator;
  protected AverageCalculator combinedAverageCalculator;
  protected double currentPrice;
  protected String sellStrategy;
  protected String buyStrategy;
  protected double volume;
  protected double triggerValue;
  protected double percChange;
  
  public MonitoringData() {}
  
  public String getMarket() {
    return market;
  }
  
  public void setMarket(String market) {
    this.market = market;
  }
  
  public void setProfit(double profit) {
    this.profit = profit;
  }
  
  public double getProfit() {
    return Util.c(profit).doubleValue();
  }
  
  public void setAverageCalculator(AverageCalculator averageCalculator) {
    this.averageCalculator = averageCalculator;
  }
  
  public void setCombinedAverageCalculator(AverageCalculator combinedAverageCalculator) {
    this.combinedAverageCalculator = averageCalculator;
  }
  
  public AverageCalculator getAverageCalculator() {
    return averageCalculator;
  }
  
  public double getAveragePrice() {
    return averageCalculator.getAvgPrice();
  }
  
  public double getCombinedAveragePrice() {
    return Util.a(combinedAverageCalculator.getAvgPrice()).doubleValue();
  }
  
  public double getTotalTrueCost() {
    return averageCalculator.getTotalCost();
  }
  
  public double getTotalAmount() {
    return averageCalculator.getTotalAmount();
  }
  
  public double getCurrentValue() {
    return averageCalculator.getTotalAmount() * currentPrice;
  }
  
  public void setCurrentPrice(double currentPrice) {
    this.currentPrice = currentPrice;
  }
  
  public double getCurrentPrice() {
    return Util.a(currentPrice).doubleValue();
  }
  
  public void setSellStrategy(String sellStrategy) {
    this.sellStrategy = sellStrategy;
  }
  
  public String getSellStrategy() {
    return sellStrategy;
  }
  
  public void setBuyStrategy(String buyStrategy) {
    this.buyStrategy = buyStrategy;
  }
  
  public String getBuyStrategy() {
    return buyStrategy;
  }
  
  public double getVolume() {
    return Math.round(volume);
  }
  
  public void setVolume(double volume) {
    this.volume = volume;
  }
  
  public void setTriggerValue(double triggerValue) {
    this.triggerValue = triggerValue;
  }
  
  public double getTriggerValue() {
    return Util.c(triggerValue).doubleValue();
  }
  
  public String getDate() {
    return averageCalculator.getFirstBoughtDateFormatted();
  }
  
  public double getPercChange() {
    return percChange;
  }
  
  public void setPercChange(double percChange) {
    this.percChange = percChange;
  }
}
