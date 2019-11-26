package org.springframework.boot.loader.jar;

import java.nio.charset.Charset;
























final class AsciiBytes
{
  private static final Charset UTF_8 = Charset.forName("UTF-8");
  

  private final byte[] bytes;
  

  private final int offset;
  
  private final int length;
  
  private String string;
  
  private int hash;
  

  AsciiBytes(String string)
  {
    this(string.getBytes(UTF_8));
    this.string = string;
  }
  




  AsciiBytes(byte[] bytes)
  {
    this(bytes, 0, bytes.length);
  }
  






  AsciiBytes(byte[] bytes, int offset, int length)
  {
    if ((offset < 0) || (length < 0) || (offset + length > bytes.length)) {
      throw new IndexOutOfBoundsException();
    }
    this.bytes = bytes;
    this.offset = offset;
    this.length = length;
  }
  
  public int length() {
    return length;
  }
  
  public boolean startsWith(AsciiBytes prefix) {
    if (this == prefix) {
      return true;
    }
    if (length > length) {
      return false;
    }
    for (int i = 0; i < length; i++) {
      if (bytes[(i + offset)] != bytes[(i + offset)]) {
        return false;
      }
    }
    return true;
  }
  
  public boolean endsWith(AsciiBytes postfix) {
    if (this == postfix) {
      return true;
    }
    if (length > length) {
      return false;
    }
    for (int i = 0; i < length; i++) {
      if (bytes[(offset + (length - 1) - i)] != bytes[(offset + (length - 1) - i)])
      {
        return false;
      }
    }
    return true;
  }
  
  public AsciiBytes substring(int beginIndex) {
    return substring(beginIndex, length);
  }
  
  public AsciiBytes substring(int beginIndex, int endIndex) {
    int length = endIndex - beginIndex;
    if (offset + length > bytes.length) {
      throw new IndexOutOfBoundsException();
    }
    return new AsciiBytes(bytes, offset + beginIndex, length);
  }
  
  public AsciiBytes append(String string) {
    if ((string == null) || (string.isEmpty())) {
      return this;
    }
    return append(string.getBytes(UTF_8));
  }
  
  public AsciiBytes append(AsciiBytes asciiBytes) {
    if ((asciiBytes == null) || (asciiBytes.length() == 0)) {
      return this;
    }
    return append(bytes);
  }
  
  public AsciiBytes append(byte[] bytes) {
    if ((bytes == null) || (bytes.length == 0)) {
      return this;
    }
    byte[] combined = new byte[length + bytes.length];
    System.arraycopy(this.bytes, offset, combined, 0, length);
    System.arraycopy(bytes, 0, combined, length, bytes.length);
    return new AsciiBytes(combined);
  }
  
  public String toString()
  {
    if (string == null) {
      string = new String(bytes, offset, length, UTF_8);
    }
    return string;
  }
  
  public int hashCode()
  {
    int hash = this.hash;
    if ((hash == 0) && (bytes.length > 0)) {
      for (int i = offset; i < offset + length; i++) {
        int b = bytes[i];
        if (b < 0) {
          b &= 0x7F;
          
          int excess = 128;
          int limit; if (b < 96) {
            int limit = 1;
            excess += 4096;
          }
          else if (b < 112) {
            int limit = 2;
            excess += 401408;
          }
          else {
            limit = 3;
            excess += 29892608;
          }
          for (int j = 0; j < limit; j++) {
            b = (b << 6) + (bytes[(++i)] & 0xFF);
          }
          b -= excess;
        }
        if (b <= 65535) {
          hash = 31 * hash + b;
        }
        else {
          hash = 31 * hash + ((b >> 10) + 55232);
          hash = 31 * hash + ((b & 0x3FF) + 56320);
        }
      }
      this.hash = hash;
    }
    return hash;
  }
  
  public boolean equals(Object obj)
  {
    if (obj == null) {
      return false;
    }
    if (this == obj) {
      return true;
    }
    if (obj.getClass().equals(AsciiBytes.class)) {
      AsciiBytes other = (AsciiBytes)obj;
      if (length == length) {
        for (int i = 0; i < length; i++) {
          if (bytes[(offset + i)] != bytes[(offset + i)]) {
            return false;
          }
        }
        return true;
      }
    }
    return false;
  }
  
  static String toString(byte[] bytes) {
    return new String(bytes, UTF_8);
  }
  
  public static int hashCode(String string)
  {
    return string.hashCode();
  }
  
  public static int hashCode(int hash, String string) {
    for (int i = 0; i < string.length(); i++) {
      hash = 31 * hash + string.charAt(i);
    }
    return hash;
  }
}
