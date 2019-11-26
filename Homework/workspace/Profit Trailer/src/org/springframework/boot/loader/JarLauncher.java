package org.springframework.boot.loader;

import org.springframework.boot.loader.archive.Archive;
import org.springframework.boot.loader.archive.Archive.Entry;
























public class JarLauncher
  extends ExecutableArchiveLauncher
{
  static final String BOOT_INF_CLASSES = "BOOT-INF/classes/";
  static final String BOOT_INF_LIB = "BOOT-INF/lib/";
  
  public JarLauncher() {}
  
  protected JarLauncher(Archive archive)
  {
    super(archive);
  }
  
  protected boolean isNestedArchive(Archive.Entry entry)
  {
    if (entry.isDirectory()) {
      return entry.getName().equals("BOOT-INF/classes/");
    }
    return entry.getName().startsWith("BOOT-INF/lib/");
  }
  
  public static void main(String[] args) throws Exception {
    new JarLauncher().launch(args);
  }
}
