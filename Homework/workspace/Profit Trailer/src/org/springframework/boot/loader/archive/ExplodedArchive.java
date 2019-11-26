package org.springframework.boot.loader.archive;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.jar.Manifest;





















public class ExplodedArchive
  implements Archive
{
  private static final Set<String> SKIPPED_NAMES = new HashSet(
    Arrays.asList(new String[] { ".", ".." }));
  

  private final File root;
  

  private final boolean recursive;
  
  private File manifestFile;
  
  private Manifest manifest;
  

  public ExplodedArchive(File root)
  {
    this(root, true);
  }
  







  public ExplodedArchive(File root, boolean recursive)
  {
    if ((!root.exists()) || (!root.isDirectory())) {
      throw new IllegalArgumentException("Invalid source folder " + root);
    }
    this.root = root;
    this.recursive = recursive;
    manifestFile = getManifestFile(root);
  }
  
  private File getManifestFile(File root) {
    File metaInf = new File(root, "META-INF");
    return new File(metaInf, "MANIFEST.MF");
  }
  
  public URL getUrl() throws MalformedURLException
  {
    return root.toURI().toURL();
  }
  
  public Manifest getManifest() throws IOException
  {
    if ((manifest == null) && (manifestFile.exists())) {
      FileInputStream inputStream = new FileInputStream(manifestFile);
      try {
        manifest = new Manifest(inputStream);
        

        inputStream.close(); } finally { inputStream.close();
      }
    }
    return manifest;
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
    return new FileEntryIterator(root, recursive);
  }
  
  protected Archive getNestedArchive(Archive.Entry entry) throws IOException {
    File file = ((FileEntry)entry).getFile();
    return file.isDirectory() ? new ExplodedArchive(file) : new JarFileArchive(file);
  }
  
  public String toString()
  {
    try
    {
      return getUrl().toString();
    }
    catch (Exception ex) {}
    return "exploded archive";
  }
  



  private static class FileEntryIterator
    implements Iterator<Archive.Entry>
  {
    private final Comparator<File> entryComparator = new EntryComparator(null);
    
    private final File root;
    
    private final boolean recursive;
    
    private final Deque<Iterator<File>> stack = new LinkedList();
    private File current;
    
    FileEntryIterator(File root, boolean recursive)
    {
      this.root = root;
      this.recursive = recursive;
      stack.add(listFiles(root));
      current = poll();
    }
    
    public boolean hasNext()
    {
      return current != null;
    }
    
    public Archive.Entry next()
    {
      if (current == null) {
        throw new NoSuchElementException();
      }
      File file = current;
      if ((file.isDirectory()) && ((recursive) || 
        (file.getParentFile().equals(root)))) {
        stack.addFirst(listFiles(file));
      }
      current = poll();
      
      String name = file.toURI().getPath().substring(root.toURI().getPath().length());
      return new ExplodedArchive.FileEntry(name, file);
    }
    
    private Iterator<File> listFiles(File file) {
      File[] files = file.listFiles();
      if (files == null) {
        return Collections.emptyList().iterator();
      }
      Arrays.sort(files, entryComparator);
      return Arrays.asList(files).iterator();
    }
    
    private File poll() {
      while (!stack.isEmpty()) {
        while (((Iterator)stack.peek()).hasNext()) {
          File file = (File)((Iterator)stack.peek()).next();
          if (!ExplodedArchive.SKIPPED_NAMES.contains(file.getName())) {
            return file;
          }
        }
        stack.poll();
      }
      return null;
    }
    
    public void remove()
    {
      throw new UnsupportedOperationException("remove");
    }
    
    private static class EntryComparator
      implements Comparator<File>
    {
      private EntryComparator() {}
      
      public int compare(File o1, File o2)
      {
        return o1.getAbsolutePath().compareTo(o2.getAbsolutePath());
      }
    }
  }
  


  private static class FileEntry
    implements Archive.Entry
  {
    private final String name;
    
    private final File file;
    

    FileEntry(String name, File file)
    {
      this.name = name;
      this.file = file;
    }
    
    public File getFile() {
      return file;
    }
    
    public boolean isDirectory()
    {
      return file.isDirectory();
    }
    
    public String getName()
    {
      return name;
    }
  }
}
