package org.springframework.boot.loader;

import java.util.ArrayList;
import java.util.List;
import java.util.jar.Attributes;
import java.util.jar.Manifest;
import org.springframework.boot.loader.archive.Archive;
import org.springframework.boot.loader.archive.Archive.Entry;
import org.springframework.boot.loader.archive.Archive.EntryFilter;





















public abstract class ExecutableArchiveLauncher
  extends Launcher
{
  private final Archive archive;
  
  public ExecutableArchiveLauncher()
  {
    try
    {
      archive = createArchive();
    }
    catch (Exception ex) {
      throw new IllegalStateException(ex);
    }
  }
  
  protected ExecutableArchiveLauncher(Archive archive) {
    this.archive = archive;
  }
  
  protected final Archive getArchive() {
    return archive;
  }
  
  protected String getMainClass() throws Exception
  {
    Manifest manifest = archive.getManifest();
    String mainClass = null;
    if (manifest != null) {
      mainClass = manifest.getMainAttributes().getValue("Start-Class");
    }
    if (mainClass == null) {
      throw new IllegalStateException("No 'Start-Class' manifest entry specified in " + this);
    }
    
    return mainClass;
  }
  
  protected List<Archive> getClassPathArchives()
    throws Exception
  {
    List<Archive> archives = new ArrayList(archive.getNestedArchives(new Archive.EntryFilter()
    {
      public boolean matches(Archive.Entry entry)
      {
        return isNestedArchive(entry);
      }
      
    }));
    postProcessClassPathArchives(archives);
    return archives;
  }
  
  protected abstract boolean isNestedArchive(Archive.Entry paramEntry);
  
  protected void postProcessClassPathArchives(List<Archive> archives)
    throws Exception
  {}
}
