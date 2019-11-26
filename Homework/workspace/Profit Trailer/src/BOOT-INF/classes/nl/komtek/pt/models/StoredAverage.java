package nl.komtek.pt.models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class StoredAverage implements Serializable
{
  private String currencyPair = "";
  private LocalDateTime lastBoughtDate = LocalDateTime.now();
  private String lastOrderId = "";
  private double lastAveragePrice;
  private double boughtPrice;
  private BigDecimal lastTotalAmount = BigDecimal.ZERO;
  private boolean overridden = false;
  
  StoredAverage(String currencyPair) {
    this.currencyPair = currencyPair;
  }
  
  public String getCurrencyPair() {
    return currencyPair;
  }
  
  public void setCurrencyPair(String currencyPair) {
    this.currencyPair = currencyPair;
  }
  
  public LocalDateTime getLastBoughtDate() {
    return lastBoughtDate;
  }
  
  public void setLastBoughtDate(LocalDateTime lastBoughtDate) {
    this.lastBoughtDate = lastBoughtDate;
  }
  
  public String getLastOrderId() {
    return lastOrderId;
  }
  
  public void setLastOrderId(String lastOrderId) {
    this.lastOrderId = lastOrderId;
  }
  
  public double getLastAveragePrice() {
    return lastAveragePrice;
  }
  
  public void setLastAveragePrice(double lastAveragePrice) {
    this.lastAveragePrice = lastAveragePrice;
  }
  
  public BigDecimal getLastTotalAmount() {
    return lastTotalAmount;
  }
  
  public void setLastTotalAmount(BigDecimal lastTotalAmount) {
    this.lastTotalAmount = lastTotalAmount;
  }
  
  public double getBoughtPrice() {
    return boughtPrice;
  }
  
  public void setBoughtPrice(double boughtPrice) {
    this.boughtPrice = boughtPrice;
  }
  
  public boolean isOverridden() {
    return overridden;
  }
  
  public void setOverridden(boolean overridden) {
    this.overridden = overridden;
  }
  
  public void addAmount(BigDecimal newAmount) {
    lastTotalAmount = lastTotalAmount.add(newAmount);
  }
  
  public void removeAmount(BigDecimal newAmount) {
    lastTotalAmount = lastTotalAmount.subtract(newAmount);
    if (lastTotalAmount.doubleValue() < 0.0D) {
      lastTotalAmount = BigDecimal.ZERO;
    }
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder("StoredAverage{");
    localStringBuilder.append("currencyPair='").append(currencyPair).append('\'');
    localStringBuilder.append(", lastBoughtDate=").append(lastBoughtDate);
    localStringBuilder.append(", lastOrderId='").append(lastOrderId).append('\'');
    localStringBuilder.append(", lastAveragePrice=").append(lastAveragePrice);
    localStringBuilder.append(", boughtPrice=").append(boughtPrice);
    localStringBuilder.append(", lastTotalAmount=").append(lastTotalAmount);
    localStringBuilder.append(", overridden=").append(overridden);
    localStringBuilder.append('}');
    return localStringBuilder.toString();
  }
}
