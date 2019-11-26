package org.springframework.boot.loader.jar;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;






















class ZipInflaterInputStream
  extends InflaterInputStream
{
  private boolean extraBytesWritten;
  private int available;
  
  ZipInflaterInputStream(InputStream inputStream, int size)
  {
    super(inputStream, new Inflater(true), getInflaterBufferSize(size));
    available = size;
  }
  
  public int available() throws IOException
  {
    if (available < 0) {
      return super.available();
    }
    return available;
  }
  
  public int read(byte[] b, int off, int len) throws IOException
  {
    int result = super.read(b, off, len);
    if (result != -1) {
      available -= result;
    }
    return result;
  }
  
  protected void fill() throws IOException
  {
    try {
      super.fill();
    }
    catch (EOFException ex) {
      if (extraBytesWritten) {
        throw ex;
      }
      len = 1;
      buf[0] = 0;
      extraBytesWritten = true;
      inf.setInput(buf, 0, len);
    }
  }
  
  private static int getInflaterBufferSize(long size) {
    size += 2L;
    size = size > 65536L ? 8192L : size;
    size = size <= 0L ? 4096L : size;
    return (int)size;
  }
}
