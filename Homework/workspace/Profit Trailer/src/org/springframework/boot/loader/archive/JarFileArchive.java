package org.springframework.boot.loader.archive;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.jar.JarEntry;
import java.util.jar.Manifest;
import org.springframework.boot.loader.data.RandomAccessData.ResourceAccess;
import org.springframework.boot.loader.jar.JarFile;
























public class JarFileArchive
  implements Archive
{
  private static final String UNPACK_MARKER = "UNPACK:";
  private static final int BUFFER_SIZE = 32768;
  private final JarFile jarFile;
  private URL url;
  private File tempUnpackFolder;
  
  public JarFileArchive(File file)
    throws IOException
  {
    this(file, null);
  }
  
  public JarFileArchive(File file, URL url) throws IOException {
    this(new JarFile(file));
    this.url = url;
  }
  
  public JarFileArchive(JarFile jarFile) {
    this.jarFile = jarFile;
  }
  
  public URL getUrl() throws MalformedURLException
  {
    if (url != null) {
      return url;
    }
    return jarFile.getUrl();
  }
  
  public Manifest getManifest() throws IOException
  {
    return jarFile.getManifest();
  }
  
  public List<Archive> getNestedArchives(Archive.EntryFilter filter) throws IOException
  {
    List<Archive> nestedArchives = new ArrayList();
    for (Archive.Entry entry : this) {
      if (filter.matches(entry)) {
        nestedArchives.add(getNestedArchive(entry));
      }
    }
    return Collections.unmodifiableList(nestedArchives);
  }
  
  public Iterator<Archive.Entry> iterator()
  {
    return new EntryIterator(jarFile.entries());
  }
  
  protected Archive getNestedArchive(Archive.Entry entry) throws IOException {
    JarEntry jarEntry = ((JarFileEntry)entry).getJarEntry();
    if (jarEntry.getComment().startsWith("UNPACK:")) {
      return getUnpackedNestedArchive(jarEntry);
    }
    try {
      JarFile jarFile = this.jarFile.getNestedJarFile(jarEntry);
      return new JarFileArchive(jarFile);
    }
    catch (Exception ex)
    {
      throw new IllegalStateException("Failed to get nested archive for entry " + entry.getName(), ex);
    }
  }
  
  private Archive getUnpackedNestedArchive(JarEntry jarEntry) throws IOException {
    String name = jarEntry.getName();
    if (name.lastIndexOf("/") != -1) {
      name = name.substring(name.lastIndexOf("/") + 1);
    }
    File file = new File(getTempUnpackFolder(), name);
    if ((!file.exists()) || (file.length() != jarEntry.getSize())) {
      unpack(jarEntry, file);
    }
    return new JarFileArchive(file, file.toURI().toURL());
  }
  
  private File getTempUnpackFolder() {
    if (tempUnpackFolder == null) {
      File tempFolder = new File(System.getProperty("java.io.tmpdir"));
      tempUnpackFolder = createUnpackFolder(tempFolder);
    }
    return tempUnpackFolder;
  }
  
  private File createUnpackFolder(File parent) {
    int attempts = 0;
    while (attempts++ < 1000) {
      String fileName = new File(jarFile.getName()).getName();
      
      File unpackFolder = new File(parent, fileName + "-spring-boot-libs-" + UUID.randomUUID());
      if (unpackFolder.mkdirs()) {
        return unpackFolder;
      }
    }
    throw new IllegalStateException("Failed to create unpack folder in directory '" + parent + "'");
  }
  
  private void unpack(JarEntry entry, File file) throws IOException
  {
    InputStream inputStream = jarFile.getInputStream(entry, RandomAccessData.ResourceAccess.ONCE);
    try {
      OutputStream outputStream = new FileOutputStream(file);
      try {
        byte[] buffer = new byte[32768];
        int bytesRead = -1;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
          outputStream.write(buffer, 0, bytesRead);
        }
        

      }
      finally {}
    }
    finally
    {
      inputStream.close();
    }
  }
  
  public String toString()
  {
    try {
      return getUrl().toString();
    }
    catch (Exception ex) {}
    return "jar archive";
  }
  

  private static class EntryIterator
    implements Iterator<Archive.Entry>
  {
    private final Enumeration<JarEntry> enumeration;
    

    EntryIterator(Enumeration<JarEntry> enumeration)
    {
      this.enumeration = enumeration;
    }
    
    public boolean hasNext()
    {
      return enumeration.hasMoreElements();
    }
    
    public Archive.Entry next()
    {
      return new JarFileArchive.JarFileEntry((JarEntry)enumeration.nextElement());
    }
    
    public void remove()
    {
      throw new UnsupportedOperationException("remove");
    }
  }
  

  private static class JarFileEntry
    implements Archive.Entry
  {
    private final JarEntry jarEntry;
    

    JarFileEntry(JarEntry jarEntry)
    {
      this.jarEntry = jarEntry;
    }
    
    public JarEntry getJarEntry() {
      return jarEntry;
    }
    
    public boolean isDirectory()
    {
      return jarEntry.isDirectory();
    }
    
    public String getName()
    {
      return jarEntry.getName();
    }
  }
}
