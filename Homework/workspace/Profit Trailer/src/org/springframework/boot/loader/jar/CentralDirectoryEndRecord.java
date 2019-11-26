package org.springframework.boot.loader.jar;

import java.io.IOException;
import org.springframework.boot.loader.data.RandomAccessData;






































class CentralDirectoryEndRecord
{
  private static final int MINIMUM_SIZE = 22;
  private static final int MAXIMUM_COMMENT_LENGTH = 65535;
  private static final int MAXIMUM_SIZE = 65557;
  private static final int SIGNATURE = 101010256;
  private static final int COMMENT_LENGTH_OFFSET = 20;
  private static final int READ_BLOCK_SIZE = 256;
  private byte[] block;
  private int offset;
  private int size;
  
  CentralDirectoryEndRecord(RandomAccessData data)
    throws IOException
  {
    block = createBlockFromEndOfData(data, 256);
    size = 22;
    offset = (block.length - size);
    while (!isValid()) {
      size += 1;
      if (size > block.length) {
        if ((size >= 65557) || (size > data.getSize())) {
          throw new IOException("Unable to find ZIP central directory records after reading " + size + " bytes");
        }
        
        block = createBlockFromEndOfData(data, size + 256);
      }
      offset = (block.length - size);
    }
  }
  
  private byte[] createBlockFromEndOfData(RandomAccessData data, int size) throws IOException
  {
    int length = (int)Math.min(data.getSize(), size);
    return Bytes.get(data.getSubsection(data.getSize() - length, length));
  }
  
  private boolean isValid() {
    if ((block.length < 22) || 
      (Bytes.littleEndianValue(block, offset + 0, 4) != 101010256L)) {
      return false;
    }
    
    long commentLength = Bytes.littleEndianValue(block, offset + 20, 2);
    
    return size == 22L + commentLength;
  }
  






  public long getStartOfArchive(RandomAccessData data)
  {
    long length = Bytes.littleEndianValue(block, offset + 12, 4);
    long specifiedOffset = Bytes.littleEndianValue(block, offset + 16, 4);
    long actualOffset = data.getSize() - size - length;
    return actualOffset - specifiedOffset;
  }
  





  public RandomAccessData getCentralDirectory(RandomAccessData data)
  {
    long offset = Bytes.littleEndianValue(block, this.offset + 16, 4);
    long length = Bytes.littleEndianValue(block, this.offset + 12, 4);
    return data.getSubsection(offset, length);
  }
  



  public int getNumberOfRecords()
  {
    long numberOfRecords = Bytes.littleEndianValue(block, offset + 10, 2);
    if (numberOfRecords == 65535L) {
      throw new IllegalStateException("Zip64 archives are not supported");
    }
    return (int)numberOfRecords;
  }
}
