package org.springframework.boot.loader;

import java.lang.reflect.Method;





























public class MainMethodRunner
{
  private final String mainClassName;
  private final String[] args;
  
  public MainMethodRunner(String mainClass, String[] args)
  {
    mainClassName = mainClass;
    this.args = (args == null ? null : (String[])args.clone());
  }
  
  public void run() throws Exception
  {
    Class<?> mainClass = Thread.currentThread().getContextClassLoader().loadClass(mainClassName);
    Method mainMethod = mainClass.getDeclaredMethod("main", new Class[] { [Ljava.lang.String.class });
    mainMethod.invoke(null, new Object[] { args });
  }
}
