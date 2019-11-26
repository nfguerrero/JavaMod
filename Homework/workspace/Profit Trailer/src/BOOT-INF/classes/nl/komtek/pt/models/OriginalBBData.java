package nl.komtek.pt.models;

import java.io.Serializable;

public class OriginalBBData implements Serializable {
  private double originalTrigger;
  private double originalWidth;
  private double originalBBValue;
  
  public OriginalBBData() {}
  
  public double getOriginalTrigger() { return originalTrigger; }
  
  public void setOriginalTrigger(double originalTrigger)
  {
    this.originalTrigger = originalTrigger;
  }
  
  public double getOriginalWidth() {
    return originalWidth;
  }
  
  public void setOriginalWidth(double originalWidth) {
    this.originalWidth = originalWidth;
  }
  
  public void setOriginalBBValue(double originalBBValue) {
    this.originalBBValue = originalBBValue;
  }
  
  public double getOriginalBBValue() {
    return originalBBValue;
  }
}
