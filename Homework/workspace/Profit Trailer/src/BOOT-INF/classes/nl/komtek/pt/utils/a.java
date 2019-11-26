package nl.komtek.pt.utils;

import java.util.Properties;
import org.apache.commons.lang3.StringUtils;

public class a
  extends Properties
{
  public a() {}
  
  public Object put(Object key, Object value)
  {
    String str = ((String)key).toLowerCase();
    Object localObject = value;
    if ((value instanceof String)) {
      localObject = StringUtils.removeAll(String.valueOf(value), "[^ -~]");
      localObject = StringUtils.trim(String.valueOf(localObject));
    }
    return super.put(str, localObject);
  }
  
  public Object remove(Object key) {
    String str = ((String)key).toLowerCase();
    return super.remove(str);
  }
  
  public String getProperty(String key) {
    String str1 = key.toLowerCase();
    String str2 = super.getProperty(str1);
    String str3 = StringUtils.removeAll(str2, "[^ -~]");
    return StringUtils.trim(str3);
  }
  
  public String getProperty(String key, String defaultValue) {
    String str = key.toLowerCase();
    return super.getProperty(str, defaultValue);
  }
}
