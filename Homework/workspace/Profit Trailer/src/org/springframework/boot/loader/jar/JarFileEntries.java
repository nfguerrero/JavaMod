package org.springframework.boot.loader.jar;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import org.springframework.boot.loader.data.RandomAccessData;
import org.springframework.boot.loader.data.RandomAccessData.ResourceAccess;










































class JarFileEntries
  implements CentralDirectoryVisitor, Iterable<JarEntry>
{
  private static final long LOCAL_FILE_HEADER_SIZE = 30L;
  private static final String SLASH = "/";
  private static final String NO_SUFFIX = "";
  protected static final int ENTRY_CACHE_SIZE = 25;
  private final JarFile jarFile;
  private final JarEntryFilter filter;
  private RandomAccessData centralDirectoryData;
  private int size;
  private int[] hashCodes;
  private int[] centralDirectoryOffsets;
  private int[] positions;
  private final Map<Integer, FileHeader> entriesCache = Collections.synchronizedMap(new LinkedHashMap(16, 0.75F, true)
  {

    protected boolean removeEldestEntry(Map.Entry<Integer, FileHeader> eldest)
    {

      if (jarFile.isSigned()) {
        return false;
      }
      return size() >= 25;
    }
  });
  










  JarFileEntries(JarFile jarFile, JarEntryFilter filter)
  {
    this.jarFile = jarFile;
    this.filter = filter;
  }
  

  public void visitStart(CentralDirectoryEndRecord endRecord, RandomAccessData centralDirectoryData)
  {
    int maxSize = endRecord.getNumberOfRecords();
    this.centralDirectoryData = centralDirectoryData;
    hashCodes = new int[maxSize];
    centralDirectoryOffsets = new int[maxSize];
    positions = new int[maxSize];
  }
  
  public void visitFileHeader(CentralDirectoryFileHeader fileHeader, int dataOffset)
  {
    AsciiBytes name = applyFilter(fileHeader.getName());
    if (name != null) {
      add(name, fileHeader, dataOffset);
    }
  }
  
  private void add(AsciiBytes name, CentralDirectoryFileHeader fileHeader, int dataOffset)
  {
    hashCodes[size] = name.hashCode();
    centralDirectoryOffsets[size] = dataOffset;
    positions[size] = size;
    size += 1;
  }
  
  public void visitEnd()
  {
    sort(0, size - 1);
    int[] positions = this.positions;
    this.positions = new int[positions.length];
    for (int i = 0; i < size; i++) {
      this.positions[positions[i]] = i;
    }
  }
  
  private void sort(int left, int right)
  {
    if (left < right) {
      int pivot = hashCodes[(left + (right - left) / 2)];
      int i = left;
      int j = right;
      while (i <= j) {
        while (hashCodes[i] < pivot) {
          i++;
        }
        while (hashCodes[j] > pivot) {
          j--;
        }
        if (i <= j) {
          swap(i, j);
          i++;
          j--;
        }
      }
      if (left < j) {
        sort(left, j);
      }
      if (right > i) {
        sort(i, right);
      }
    }
  }
  
  private void swap(int i, int j) {
    swap(hashCodes, i, j);
    swap(centralDirectoryOffsets, i, j);
    swap(positions, i, j);
  }
  
  private void swap(int[] array, int i, int j) {
    int temp = array[i];
    array[i] = array[j];
    array[j] = temp;
  }
  
  public Iterator<JarEntry> iterator()
  {
    return new EntryIterator(null);
  }
  
  public boolean containsEntry(String name) {
    return getEntry(name, FileHeader.class, true) != null;
  }
  
  public JarEntry getEntry(String name) {
    return (JarEntry)getEntry(name, JarEntry.class, true);
  }
  
  public InputStream getInputStream(String name, RandomAccessData.ResourceAccess access) throws IOException
  {
    FileHeader entry = getEntry(name, FileHeader.class, false);
    return getInputStream(entry, access);
  }
  
  public InputStream getInputStream(FileHeader entry, RandomAccessData.ResourceAccess access) throws IOException
  {
    if (entry == null) {
      return null;
    }
    InputStream inputStream = getEntryData(entry).getInputStream(access);
    if (entry.getMethod() == 8) {
      inputStream = new ZipInflaterInputStream(inputStream, (int)entry.getSize());
    }
    return inputStream;
  }
  
  public RandomAccessData getEntryData(String name) throws IOException {
    FileHeader entry = getEntry(name, FileHeader.class, false);
    if (entry == null) {
      return null;
    }
    return getEntryData(entry);
  }
  

  private RandomAccessData getEntryData(FileHeader entry)
    throws IOException
  {
    RandomAccessData data = jarFile.getData();
    byte[] localHeader = Bytes.get(data
      .getSubsection(entry.getLocalHeaderOffset(), 30L));
    long nameLength = Bytes.littleEndianValue(localHeader, 26, 2);
    long extraLength = Bytes.littleEndianValue(localHeader, 28, 2);
    return data.getSubsection(entry.getLocalHeaderOffset() + 30L + nameLength + extraLength, entry
      .getCompressedSize());
  }
  
  private <T extends FileHeader> T getEntry(String name, Class<T> type, boolean cacheEntry)
  {
    int hashCode = AsciiBytes.hashCode(name);
    T entry = getEntry(hashCode, name, "", type, cacheEntry);
    if (entry == null) {
      hashCode = AsciiBytes.hashCode(hashCode, "/");
      entry = getEntry(hashCode, name, "/", type, cacheEntry);
    }
    return entry;
  }
  
  private <T extends FileHeader> T getEntry(int hashCode, String name, String suffix, Class<T> type, boolean cacheEntry)
  {
    int index = getFirstIndex(hashCode);
    while ((index >= 0) && (index < size) && (hashCodes[index] == hashCode)) {
      T entry = getEntry(index, type, cacheEntry);
      if (entry.hasName(name, suffix)) {
        return entry;
      }
      index++;
    }
    return null;
  }
  
  private <T extends FileHeader> T getEntry(int index, Class<T> type, boolean cacheEntry)
  {
    try
    {
      FileHeader cached = (FileHeader)entriesCache.get(Integer.valueOf(index));
      
      FileHeader entry = cached != null ? cached : CentralDirectoryFileHeader.fromRandomAccessData(centralDirectoryData, centralDirectoryOffsets[index], filter);
      

      if ((CentralDirectoryFileHeader.class.equals(entry.getClass())) && 
        (type.equals(JarEntry.class))) {
        entry = new JarEntry(jarFile, (CentralDirectoryFileHeader)entry);
      }
      if ((cacheEntry) && (cached != entry)) {
        entriesCache.put(Integer.valueOf(index), entry);
      }
      return entry;
    }
    catch (IOException ex) {
      throw new IllegalStateException(ex);
    }
  }
  
  private int getFirstIndex(int hashCode) {
    int index = Arrays.binarySearch(hashCodes, 0, size, hashCode);
    if (index < 0) {
      return -1;
    }
    while ((index > 0) && (hashCodes[(index - 1)] == hashCode)) {
      index--;
    }
    return index;
  }
  
  public void clearCache() {
    entriesCache.clear();
  }
  
  private AsciiBytes applyFilter(AsciiBytes name) {
    return filter == null ? name : filter.apply(name);
  }
  


  private class EntryIterator
    implements Iterator<JarEntry>
  {
    private int index = 0;
    
    private EntryIterator() {}
    
    public boolean hasNext() { return index < size; }
    

    public JarEntry next()
    {
      if (!hasNext()) {
        throw new NoSuchElementException();
      }
      int entryIndex = positions[index];
      index += 1;
      return (JarEntry)JarFileEntries.this.getEntry(entryIndex, JarEntry.class, false);
    }
  }
}
