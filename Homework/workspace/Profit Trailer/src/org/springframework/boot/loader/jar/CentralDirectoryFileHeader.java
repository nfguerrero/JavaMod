package org.springframework.boot.loader.jar;

import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import org.springframework.boot.loader.data.RandomAccessData;

























final class CentralDirectoryFileHeader
  implements FileHeader
{
  private static final AsciiBytes SLASH = new AsciiBytes("/");
  
  private static final byte[] NO_EXTRA = new byte[0];
  
  private static final AsciiBytes NO_COMMENT = new AsciiBytes("");
  
  private byte[] header;
  
  private int headerOffset;
  
  private AsciiBytes name;
  
  private byte[] extra;
  
  private AsciiBytes comment;
  
  private long localHeaderOffset;
  

  CentralDirectoryFileHeader() {}
  

  CentralDirectoryFileHeader(byte[] header, int headerOffset, AsciiBytes name, byte[] extra, AsciiBytes comment, long localHeaderOffset)
  {
    this.header = header;
    this.headerOffset = headerOffset;
    this.name = name;
    this.extra = extra;
    this.comment = comment;
    this.localHeaderOffset = localHeaderOffset;
  }
  
  void load(byte[] data, int dataOffset, RandomAccessData variableData, int variableOffset, JarEntryFilter filter)
    throws IOException
  {
    header = data;
    headerOffset = dataOffset;
    long nameLength = Bytes.littleEndianValue(data, dataOffset + 28, 2);
    long extraLength = Bytes.littleEndianValue(data, dataOffset + 30, 2);
    long commentLength = Bytes.littleEndianValue(data, dataOffset + 32, 2);
    localHeaderOffset = Bytes.littleEndianValue(data, dataOffset + 42, 4);
    
    dataOffset += 46;
    if (variableData != null) {
      data = Bytes.get(variableData.getSubsection(variableOffset + 46, nameLength + extraLength + commentLength));
      
      dataOffset = 0;
    }
    name = new AsciiBytes(data, dataOffset, (int)nameLength);
    if (filter != null) {
      name = filter.apply(name);
    }
    extra = NO_EXTRA;
    comment = NO_COMMENT;
    if (extraLength > 0L) {
      extra = new byte[(int)extraLength];
      System.arraycopy(data, (int)(dataOffset + nameLength), extra, 0, extra.length);
    }
    
    if (commentLength > 0L) {
      comment = new AsciiBytes(data, (int)(dataOffset + nameLength + extraLength), (int)commentLength);
    }
  }
  
  public AsciiBytes getName()
  {
    return name;
  }
  
  public boolean hasName(String name, String suffix)
  {
    return this.name.equals(new AsciiBytes(name + suffix));
  }
  
  public boolean isDirectory() {
    return name.endsWith(SLASH);
  }
  
  public int getMethod()
  {
    return (int)Bytes.littleEndianValue(header, headerOffset + 10, 2);
  }
  
  public long getTime() {
    long date = Bytes.littleEndianValue(header, headerOffset + 14, 2);
    long time = Bytes.littleEndianValue(header, headerOffset + 12, 2);
    return decodeMsDosFormatDateTime(date, time).getTimeInMillis();
  }
  







  private Calendar decodeMsDosFormatDateTime(long date, long time)
  {
    int year = (int)(date >> 9 & 0x7F) + 1980;
    int month = (int)(date >> 5 & 0xF) - 1;
    int day = (int)(date & 0x1F);
    int hours = (int)(time >> 11 & 0x1F);
    int minutes = (int)(time >> 5 & 0x3F);
    int seconds = (int)(time << 1 & 0x3E);
    return new GregorianCalendar(year, month, day, hours, minutes, seconds);
  }
  
  public long getCrc() {
    return Bytes.littleEndianValue(header, headerOffset + 16, 4);
  }
  
  public long getCompressedSize()
  {
    return Bytes.littleEndianValue(header, headerOffset + 20, 4);
  }
  
  public long getSize()
  {
    return Bytes.littleEndianValue(header, headerOffset + 24, 4);
  }
  
  public byte[] getExtra() {
    return extra;
  }
  
  public AsciiBytes getComment() {
    return comment;
  }
  
  public long getLocalHeaderOffset()
  {
    return localHeaderOffset;
  }
  
  public CentralDirectoryFileHeader clone()
  {
    byte[] header = new byte[46];
    System.arraycopy(this.header, headerOffset, header, 0, header.length);
    return new CentralDirectoryFileHeader(header, 0, name, header, comment, localHeaderOffset);
  }
  
  public static CentralDirectoryFileHeader fromRandomAccessData(RandomAccessData data, int offset, JarEntryFilter filter)
    throws IOException
  {
    CentralDirectoryFileHeader fileHeader = new CentralDirectoryFileHeader();
    byte[] bytes = Bytes.get(data.getSubsection(offset, 46L));
    fileHeader.load(bytes, 0, data, offset, filter);
    return fileHeader;
  }
}
