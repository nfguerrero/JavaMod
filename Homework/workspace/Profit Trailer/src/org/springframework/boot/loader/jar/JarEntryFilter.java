package org.springframework.boot.loader.jar;

abstract interface JarEntryFilter
{
  public abstract AsciiBytes apply(AsciiBytes paramAsciiBytes);
}
