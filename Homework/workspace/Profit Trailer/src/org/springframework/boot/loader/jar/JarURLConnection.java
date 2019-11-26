package org.springframework.boot.loader.jar;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilePermission;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.net.URLStreamHandler;
import java.security.Permission;
import org.springframework.boot.loader.data.RandomAccessData;
import org.springframework.boot.loader.data.RandomAccessData.ResourceAccess;
import org.springframework.boot.loader.data.RandomAccessDataFile;




















final class JarURLConnection
  extends java.net.JarURLConnection
{
  private static ThreadLocal<Boolean> useFastExceptions = new ThreadLocal();
  
  private static final FileNotFoundException FILE_NOT_FOUND_EXCEPTION = new FileNotFoundException("Jar file or entry not found");
  

  private static final IllegalStateException NOT_FOUND_CONNECTION_EXCEPTION = new IllegalStateException(FILE_NOT_FOUND_EXCEPTION);
  
  private static final String SEPARATOR = "!/";
  private static final URL EMPTY_JAR_URL;
  
  static
  {
    try
    {
      EMPTY_JAR_URL = new URL("jar:", null, 0, "file:!/", new URLStreamHandler()
      {
        protected URLConnection openConnection(URL u)
          throws IOException
        {
          return null;
        }
      });
    }
    catch (MalformedURLException ex) {
      throw new IllegalStateException(ex);
    }
  }
  
  private static final JarEntryName EMPTY_JAR_ENTRY_NAME = new JarEntryName("");
  

  private static final String READ_ACTION = "read";
  
  private static final JarURLConnection NOT_FOUND_CONNECTION = notFound();
  
  private final JarFile jarFile;
  
  private Permission permission;
  
  private URL jarFileUrl;
  
  private final JarEntryName jarEntryName;
  
  private JarEntry jarEntry;
  
  private JarURLConnection(URL url, JarFile jarFile, JarEntryName jarEntryName)
    throws IOException
  {
    super(EMPTY_JAR_URL);
    this.url = url;
    this.jarFile = jarFile;
    this.jarEntryName = jarEntryName;
  }
  
  public void connect() throws IOException
  {
    if (jarFile == null) {
      throw FILE_NOT_FOUND_EXCEPTION;
    }
    if ((!jarEntryName.isEmpty()) && (jarEntry == null)) {
      jarEntry = jarFile.getJarEntry(getEntryName());
      if (jarEntry == null) {
        throwFileNotFound(jarEntryName, jarFile);
      }
    }
    connected = true;
  }
  
  public JarFile getJarFile() throws IOException
  {
    connect();
    return jarFile;
  }
  
  public URL getJarFileURL()
  {
    if (jarFile == null) {
      throw NOT_FOUND_CONNECTION_EXCEPTION;
    }
    if (jarFileUrl == null) {
      jarFileUrl = buildJarFileUrl();
    }
    return jarFileUrl;
  }
  
  private URL buildJarFileUrl() {
    try {
      String spec = jarFile.getUrl().getFile();
      if (spec.endsWith("!/")) {
        spec = spec.substring(0, spec.length() - "!/".length());
      }
      if (spec.indexOf("!/") == -1) {
        return new URL(spec);
      }
      return new URL("jar:" + spec);
    }
    catch (MalformedURLException ex) {
      throw new IllegalStateException(ex);
    }
  }
  
  public JarEntry getJarEntry() throws IOException
  {
    if ((jarEntryName == null) || (jarEntryName.isEmpty())) {
      return null;
    }
    connect();
    return jarEntry;
  }
  
  public String getEntryName()
  {
    if (jarFile == null) {
      throw NOT_FOUND_CONNECTION_EXCEPTION;
    }
    return jarEntryName.toString();
  }
  
  public InputStream getInputStream() throws IOException
  {
    if (jarFile == null) {
      throw FILE_NOT_FOUND_EXCEPTION;
    }
    if ((jarEntryName.isEmpty()) && 
      (jarFile.getType() == JarFile.JarFileType.DIRECT)) {
      throw new IOException("no entry name specified");
    }
    connect();
    

    InputStream inputStream = jarEntryName.isEmpty() ? jarFile.getData().getInputStream(RandomAccessData.ResourceAccess.ONCE) : jarFile.getInputStream(jarEntry);
    if (inputStream == null) {
      throwFileNotFound(jarEntryName, jarFile);
    }
    return inputStream;
  }
  
  private void throwFileNotFound(Object entry, JarFile jarFile) throws FileNotFoundException
  {
    if (Boolean.TRUE.equals(useFastExceptions.get())) {
      throw FILE_NOT_FOUND_EXCEPTION;
    }
    
    throw new FileNotFoundException("JAR entry " + entry + " not found in " + jarFile.getName());
  }
  
  public int getContentLength()
  {
    if (jarFile == null) {
      return -1;
    }
    try {
      if (jarEntryName.isEmpty()) {
        return jarFile.size();
      }
      JarEntry entry = getJarEntry();
      return entry == null ? -1 : (int)entry.getSize();
    }
    catch (IOException ex) {}
    return -1;
  }
  
  public Object getContent()
    throws IOException
  {
    connect();
    return jarEntryName.isEmpty() ? jarFile : super.getContent();
  }
  
  public String getContentType()
  {
    return jarEntryName == null ? null : jarEntryName.getContentType();
  }
  
  public Permission getPermission() throws IOException
  {
    if (jarFile == null) {
      throw FILE_NOT_FOUND_EXCEPTION;
    }
    if (permission == null)
    {
      permission = new FilePermission(jarFile.getRootJarFile().getFile().getPath(), "read");
    }
    return permission;
  }
  
  static void setUseFastExceptions(boolean useFastExceptions) {
    useFastExceptions.set(Boolean.valueOf(useFastExceptions));
  }
  
  static JarURLConnection get(URL url, JarFile jarFile) throws IOException {
    String spec = extractFullSpec(url, jarFile.getPathFromRoot());
    
    int index = 0;
    int separator; while ((separator = spec.indexOf("!/", index)) > 0) {
      String entryName = spec.substring(index, separator);
      JarEntry jarEntry = jarFile.getJarEntry(entryName);
      if (jarEntry == null) {
        return notFound(jarFile, JarEntryName.get(entryName));
      }
      jarFile = jarFile.getNestedJarFile(jarEntry);
      index += separator + "!/".length();
    }
    JarEntryName jarEntryName = JarEntryName.get(spec, index);
    if ((Boolean.TRUE.equals(useFastExceptions.get())) && 
      (!jarEntryName.isEmpty()) && 
      (!jarFile.containsEntry(jarEntryName.toString()))) {
      return NOT_FOUND_CONNECTION;
    }
    
    return new JarURLConnection(url, jarFile, jarEntryName);
  }
  
  private static String extractFullSpec(URL url, String pathFromRoot) {
    String file = url.getFile();
    int separatorIndex = file.indexOf("!/");
    if (separatorIndex < 0) {
      return "";
    }
    int specIndex = separatorIndex + "!/".length() + pathFromRoot.length();
    return file.substring(specIndex);
  }
  
  private static JarURLConnection notFound() {
    try {
      return notFound(null, null);
    }
    catch (IOException ex) {
      throw new IllegalStateException(ex);
    }
  }
  
  private static JarURLConnection notFound(JarFile jarFile, JarEntryName jarEntryName) throws IOException
  {
    if (Boolean.TRUE.equals(useFastExceptions.get())) {
      return NOT_FOUND_CONNECTION;
    }
    return new JarURLConnection(null, jarFile, jarEntryName);
  }
  

  static class JarEntryName
  {
    private final String name;
    
    private String contentType;
    

    JarEntryName(String spec)
    {
      name = decode(spec);
    }
    
    private String decode(String source) {
      if ((source.isEmpty()) || (source.indexOf('%') < 0)) {
        return source;
      }
      ByteArrayOutputStream bos = new ByteArrayOutputStream(source.length());
      write(source, bos);
      
      return AsciiBytes.toString(bos.toByteArray());
    }
    
    private void write(String source, ByteArrayOutputStream outputStream) {
      int length = source.length();
      for (int i = 0; i < length; i++) {
        int c = source.charAt(i);
        if (c > 127) {
          try {
            String encoded = URLEncoder.encode(String.valueOf((char)c), "UTF-8");
            
            write(encoded, outputStream);
          }
          catch (UnsupportedEncodingException ex) {
            throw new IllegalStateException(ex);
          }
        }
        else {
          if (c == 37) {
            if (i + 2 >= length)
            {
              throw new IllegalArgumentException("Invalid encoded sequence \"" + source.substring(i) + "\"");
            }
            
            c = decodeEscapeSequence(source, i);
            i += 2;
          }
          outputStream.write(c);
        }
      }
    }
    
    private char decodeEscapeSequence(String source, int i) {
      int hi = Character.digit(source.charAt(i + 1), 16);
      int lo = Character.digit(source.charAt(i + 2), 16);
      if ((hi == -1) || (lo == -1))
      {
        throw new IllegalArgumentException("Invalid encoded sequence \"" + source.substring(i) + "\"");
      }
      return (char)((hi << 4) + lo);
    }
    
    public String toString()
    {
      return name;
    }
    
    public boolean isEmpty() {
      return name.isEmpty();
    }
    
    public String getContentType() {
      if (contentType == null) {
        contentType = deduceContentType();
      }
      return contentType;
    }
    
    private String deduceContentType()
    {
      String type = isEmpty() ? "x-java/jar" : null;
      type = type != null ? type : URLConnection.guessContentTypeFromName(toString());
      type = type != null ? type : "content/unknown";
      return type;
    }
    
    public static JarEntryName get(String spec) {
      return get(spec, 0);
    }
    
    public static JarEntryName get(String spec, int beginIndex) {
      if (spec.length() <= beginIndex) {
        return JarURLConnection.EMPTY_JAR_ENTRY_NAME;
      }
      return new JarEntryName(spec.substring(beginIndex));
    }
  }
}
