package org.springframework.boot.loader.jar;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.jar.JarInputStream;
import java.util.jar.Manifest;
import java.util.zip.ZipEntry;
import org.springframework.boot.loader.data.RandomAccessData;
import org.springframework.boot.loader.data.RandomAccessData.ResourceAccess;
import org.springframework.boot.loader.data.RandomAccessDataFile;


































public class JarFile
  extends java.util.jar.JarFile
{
  private static final String MANIFEST_NAME = "META-INF/MANIFEST.MF";
  private static final String PROTOCOL_HANDLER = "java.protocol.handler.pkgs";
  private static final String HANDLERS_PACKAGE = "org.springframework.boot.loader";
  private static final AsciiBytes META_INF = new AsciiBytes("META-INF/");
  
  private static final AsciiBytes SIGNATURE_FILE_EXTENSION = new AsciiBytes(".SF");
  

  private final RandomAccessDataFile rootFile;
  

  private final String pathFromRoot;
  
  private final RandomAccessData data;
  
  private final JarFileType type;
  
  private URL url;
  
  private JarFileEntries entries;
  
  private SoftReference<Manifest> manifest;
  
  private boolean signed;
  

  public JarFile(File file)
    throws IOException
  {
    this(new RandomAccessDataFile(file));
  }
  



  JarFile(RandomAccessDataFile file)
    throws IOException
  {
    this(file, "", file, JarFileType.DIRECT);
  }
  








  private JarFile(RandomAccessDataFile rootFile, String pathFromRoot, RandomAccessData data, JarFileType type)
    throws IOException
  {
    this(rootFile, pathFromRoot, data, null, type);
  }
  
  private JarFile(RandomAccessDataFile rootFile, String pathFromRoot, RandomAccessData data, JarEntryFilter filter, JarFileType type)
    throws IOException
  {
    super(rootFile.getFile());
    this.rootFile = rootFile;
    this.pathFromRoot = pathFromRoot;
    CentralDirectoryParser parser = new CentralDirectoryParser();
    entries = ((JarFileEntries)parser.addVisitor(new JarFileEntries(this, filter)));
    parser.addVisitor(centralDirectoryVisitor());
    this.data = parser.parse(data, filter == null);
    this.type = type;
  }
  
  private CentralDirectoryVisitor centralDirectoryVisitor() {
    new CentralDirectoryVisitor()
    {
      public void visitStart(CentralDirectoryEndRecord endRecord, RandomAccessData centralDirectoryData) {}
      




      public void visitFileHeader(CentralDirectoryFileHeader fileHeader, int dataOffset)
      {
        AsciiBytes name = fileHeader.getName();
        if ((name.startsWith(JarFile.META_INF)) && 
          (name.endsWith(JarFile.SIGNATURE_FILE_EXTENSION))) {
          signed = true;
        }
      }
      

      public void visitEnd() {}
    };
  }
  

  protected final RandomAccessDataFile getRootJarFile()
  {
    return rootFile;
  }
  
  RandomAccessData getData() {
    return data;
  }
  
  public Manifest getManifest() throws IOException
  {
    Manifest manifest = this.manifest == null ? null : (Manifest)this.manifest.get();
    if (manifest == null) { InputStream inputStream;
      if (type == JarFileType.NESTED_DIRECTORY) {
        manifest = new JarFile(getRootJarFile()).getManifest();
      }
      else {
        inputStream = getInputStream("META-INF/MANIFEST.MF", RandomAccessData.ResourceAccess.ONCE);
        
        if (inputStream == null)
          return null;
      }
      try {
        manifest = new Manifest(inputStream);
        

        inputStream.close(); } finally { inputStream.close();
      }
    }
    

    return manifest;
  }
  
  public Enumeration<java.util.jar.JarEntry> entries()
  {
    final Iterator<JarEntry> iterator = entries.iterator();
    new Enumeration()
    {
      public boolean hasMoreElements()
      {
        return iterator.hasNext();
      }
      
      public java.util.jar.JarEntry nextElement()
      {
        return (java.util.jar.JarEntry)iterator.next();
      }
    };
  }
  

  public JarEntry getJarEntry(String name)
  {
    return (JarEntry)getEntry(name);
  }
  
  public boolean containsEntry(String name) {
    return entries.containsEntry(name);
  }
  
  public ZipEntry getEntry(String name)
  {
    return entries.getEntry(name);
  }
  
  public synchronized InputStream getInputStream(ZipEntry ze) throws IOException
  {
    return getInputStream(ze, RandomAccessData.ResourceAccess.PER_READ);
  }
  
  public InputStream getInputStream(ZipEntry ze, RandomAccessData.ResourceAccess access) throws IOException
  {
    if ((ze instanceof JarEntry)) {
      return entries.getInputStream((JarEntry)ze, access);
    }
    return getInputStream(ze == null ? null : ze.getName(), access);
  }
  
  InputStream getInputStream(String name, RandomAccessData.ResourceAccess access) throws IOException {
    return entries.getInputStream(name, access);
  }
  





  public synchronized JarFile getNestedJarFile(ZipEntry entry)
    throws IOException
  {
    return getNestedJarFile((JarEntry)entry);
  }
  



  public synchronized JarFile getNestedJarFile(JarEntry entry)
    throws IOException
  {
    try
    {
      return createJarFileFromEntry(entry);
    }
    catch (Exception ex)
    {
      throw new IOException("Unable to open nested jar file '" + entry.getName() + "'", ex);
    }
  }
  
  private JarFile createJarFileFromEntry(JarEntry entry) throws IOException {
    if (entry.isDirectory()) {
      return createJarFileFromDirectoryEntry(entry);
    }
    return createJarFileFromFileEntry(entry);
  }
  
  private JarFile createJarFileFromDirectoryEntry(JarEntry entry) throws IOException {
    final AsciiBytes sourceName = new AsciiBytes(entry.getName());
    JarEntryFilter filter = new JarEntryFilter()
    {
      public AsciiBytes apply(AsciiBytes name)
      {
        if ((name.startsWith(sourceName)) && (!name.equals(sourceName))) {
          return name.substring(sourceName.length());
        }
        return null;
      }
      
    };
    return new JarFile(rootFile, pathFromRoot + "!/" + entry
    
      .getName().substring(0, sourceName.length() - 1), data, filter, JarFileType.NESTED_DIRECTORY);
  }
  
  private JarFile createJarFileFromFileEntry(JarEntry entry) throws IOException
  {
    if (entry.getMethod() != 0)
    {
      throw new IllegalStateException("Unable to open nested entry '" + entry.getName() + "'. It has been compressed and nested jar files must be stored without compression. Please check the mechanism used to create your executable jar file");
    }
    

    RandomAccessData entryData = entries.getEntryData(entry.getName());
    return new JarFile(rootFile, pathFromRoot + "!/" + entry.getName(), entryData, JarFileType.NESTED_JAR);
  }
  

  public int size()
  {
    return (int)data.getSize();
  }
  
  public void close() throws IOException
  {
    super.close();
    rootFile.close();
  }
  




  public URL getUrl()
    throws MalformedURLException
  {
    if (url == null) {
      Handler handler = new Handler(this);
      String file = rootFile.getFile().toURI() + pathFromRoot + "!/";
      file = file.replace("file:////", "file://");
      url = new URL("jar", "", -1, file, handler);
    }
    return url;
  }
  
  public String toString()
  {
    return getName();
  }
  
  public String getName()
  {
    return rootFile.getFile() + pathFromRoot;
  }
  
  boolean isSigned() {
    return signed;
  }
  

  void setupEntryCertificates(JarEntry entry)
  {
    try
    {
      JarInputStream inputStream = new JarInputStream(getData().getInputStream(RandomAccessData.ResourceAccess.ONCE));
      try {
        java.util.jar.JarEntry certEntry = inputStream.getNextJarEntry();
        while (certEntry != null) {
          inputStream.closeEntry();
          if (entry.getName().equals(certEntry.getName())) {
            setCertificates(entry, certEntry);
          }
          setCertificates(getJarEntry(certEntry.getName()), certEntry);
          certEntry = inputStream.getNextJarEntry();
        }
      }
      finally {
        inputStream.close();
      }
    }
    catch (IOException ex) {
      throw new IllegalStateException(ex);
    }
  }
  
  private void setCertificates(JarEntry entry, java.util.jar.JarEntry certEntry) {
    if (entry != null) {
      entry.setCertificates(certEntry);
    }
  }
  
  public void clearCache() {
    entries.clearCache();
  }
  
  protected String getPathFromRoot() {
    return pathFromRoot;
  }
  
  JarFileType getType() {
    return type;
  }
  



  public static void registerUrlProtocolHandler()
  {
    String handlers = System.getProperty("java.protocol.handler.pkgs", "");
    System.setProperty("java.protocol.handler.pkgs", handlers + "|" + "org.springframework.boot.loader");
    
    resetCachedUrlHandlers();
  }
  



  private static void resetCachedUrlHandlers()
  {
    try
    {
      URL.setURLStreamHandlerFactory(null);
    }
    catch (Error localError) {}
  }
  





  static enum JarFileType
  {
    DIRECT,  NESTED_DIRECTORY,  NESTED_JAR;
    
    private JarFileType() {}
  }
}
