package org.springframework.boot.loader.jar;

abstract interface FileHeader
{
  public abstract boolean hasName(String paramString1, String paramString2);
  
  public abstract long getLocalHeaderOffset();
  
  public abstract long getCompressedSize();
  
  public abstract long getSize();
  
  public abstract int getMethod();
}
