package org.springframework.boot.loader.data;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;






























public class RandomAccessDataFile
  implements RandomAccessData
{
  private static final int DEFAULT_CONCURRENT_READS = 4;
  private final File file;
  private final FilePool filePool;
  private final long offset;
  private final long length;
  
  public RandomAccessDataFile(File file)
  {
    this(file, 4);
  }
  







  public RandomAccessDataFile(File file, int concurrentReads)
  {
    if (file == null) {
      throw new IllegalArgumentException("File must not be null");
    }
    if (!file.exists()) {
      throw new IllegalArgumentException("File must exist");
    }
    this.file = file;
    filePool = new FilePool(file, concurrentReads);
    offset = 0L;
    length = file.length();
  }
  






  private RandomAccessDataFile(File file, FilePool pool, long offset, long length)
  {
    this.file = file;
    filePool = pool;
    this.offset = offset;
    this.length = length;
  }
  



  public File getFile()
  {
    return file;
  }
  
  public InputStream getInputStream(RandomAccessData.ResourceAccess access) throws IOException
  {
    return new DataInputStream(access);
  }
  
  public RandomAccessData getSubsection(long offset, long length)
  {
    if ((offset < 0L) || (length < 0L) || (offset + length > this.length)) {
      throw new IndexOutOfBoundsException();
    }
    return new RandomAccessDataFile(file, filePool, this.offset + offset, length);
  }
  

  public long getSize()
  {
    return length;
  }
  
  public void close() throws IOException {
    filePool.close();
  }
  

  private class DataInputStream
    extends InputStream
  {
    private RandomAccessFile file;
    
    private int position;
    
    DataInputStream(RandomAccessData.ResourceAccess access)
      throws IOException
    {
      if (access == RandomAccessData.ResourceAccess.ONCE) {
        file = new RandomAccessFile(file, "r");
        file.seek(offset);
      }
    }
    
    public int read() throws IOException
    {
      return doRead(null, 0, 1);
    }
    
    public int read(byte[] b) throws IOException
    {
      return read(b, 0, b == null ? 0 : b.length);
    }
    
    public int read(byte[] b, int off, int len) throws IOException
    {
      if (b == null) {
        throw new NullPointerException("Bytes must not be null");
      }
      return doRead(b, off, len);
    }
    







    public int doRead(byte[] b, int off, int len)
      throws IOException
    {
      if (len == 0) {
        return 0;
      }
      int cappedLen = cap(len);
      if (cappedLen <= 0) {
        return -1;
      }
      RandomAccessFile file = this.file;
      try {
        if (file == null) {
          file = filePool.acquire();
          file.seek(offset + position); }
        int rtn;
        if (b == null) {
          rtn = file.read();
          moveOn(rtn == -1 ? 0 : 1);
          return rtn;
        }
        
        return (int)moveOn(file.read(b, off, cappedLen));
      }
      finally
      {
        if ((this.file == null) && (file != null)) {
          filePool.release(file);
        }
      }
    }
    
    public long skip(long n) throws IOException
    {
      return n <= 0L ? 0L : moveOn(cap(n));
    }
    
    public void close() throws IOException
    {
      if (file != null) {
        file.close();
      }
    }
    





    private int cap(long n)
    {
      return (int)Math.min(length - position, n);
    }
    




    private long moveOn(int amount)
    {
      position += amount;
      return amount;
    }
  }
  


  static class FilePool
  {
    private final File file;
    

    private final int size;
    
    private final Semaphore available;
    
    private final Queue<RandomAccessFile> files;
    

    FilePool(File file, int size)
    {
      this.file = file;
      this.size = size;
      available = new Semaphore(size);
      files = new ConcurrentLinkedQueue();
    }
    
    public RandomAccessFile acquire() throws IOException {
      available.acquireUninterruptibly();
      RandomAccessFile file = (RandomAccessFile)files.poll();
      if (file != null) {
        return file;
      }
      return new RandomAccessFile(this.file, "r");
    }
    
    public void release(RandomAccessFile file) {
      files.add(file);
      available.release();
    }
    
    public void close() throws IOException {
      available.acquireUninterruptibly(size);
      try {
        RandomAccessFile pooledFile = (RandomAccessFile)files.poll();
        while (pooledFile != null) {
          pooledFile.close();
          pooledFile = (RandomAccessFile)files.poll();
        }
        

        available.release(size); } finally { available.release(size);
      }
    }
  }
}
