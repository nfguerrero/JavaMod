package org.springframework.boot.loader.jar;

import java.io.File;
import java.io.IOException;
import java.lang.ref.SoftReference;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.net.URLStreamHandler;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;





























public class Handler
  extends URLStreamHandler
{
  private static final String JAR_PROTOCOL = "jar:";
  private static final String FILE_PROTOCOL = "file:";
  private static final String SEPARATOR = "!/";
  private static final String[] FALLBACK_HANDLERS = { "sun.net.www.protocol.jar.Handler" };
  
  private static final Method OPEN_CONNECTION_METHOD;
  
  static
  {
    Method method = null;
    try {
      method = URLStreamHandler.class.getDeclaredMethod("openConnection", new Class[] { URL.class });
    }
    catch (Exception localException) {}
    


    OPEN_CONNECTION_METHOD = method;
  }
  



  private static SoftReference<Map<File, JarFile>> rootFileCache = new SoftReference(null);
  

  private final Logger logger = Logger.getLogger(getClass().getName());
  
  private final JarFile jarFile;
  private URLStreamHandler fallbackHandler;
  
  public Handler()
  {
    this(null);
  }
  
  public Handler(JarFile jarFile) {
    this.jarFile = jarFile;
  }
  
  protected URLConnection openConnection(URL url) throws IOException
  {
    if (jarFile != null) {
      return JarURLConnection.get(url, jarFile);
    }
    try {
      return JarURLConnection.get(url, getRootJarFileFromUrl(url));
    }
    catch (Exception ex) {
      return openFallbackConnection(url, ex);
    }
  }
  
  private URLConnection openFallbackConnection(URL url, Exception reason) throws IOException
  {
    try {
      return openConnection(getFallbackHandler(), url);
    }
    catch (Exception ex) {
      if ((reason instanceof IOException)) {
        logger.log(Level.FINEST, "Unable to open fallback handler", ex);
        throw ((IOException)reason);
      }
      logger.log(Level.WARNING, "Unable to open fallback handler", ex);
      if ((reason instanceof RuntimeException)) {
        throw ((RuntimeException)reason);
      }
      throw new IllegalStateException(reason);
    }
  }
  
  private URLStreamHandler getFallbackHandler() {
    if (fallbackHandler != null) {
      return fallbackHandler;
    }
    for (String handlerClassName : FALLBACK_HANDLERS) {
      try {
        Class<?> handlerClass = Class.forName(handlerClassName);
        fallbackHandler = ((URLStreamHandler)handlerClass.newInstance());
        return fallbackHandler;
      }
      catch (Exception localException) {}
    }
    

    throw new IllegalStateException("Unable to find fallback handler");
  }
  
  private URLConnection openConnection(URLStreamHandler handler, URL url) throws Exception
  {
    if (OPEN_CONNECTION_METHOD == null) {
      throw new IllegalStateException("Unable to invoke fallback open connection method");
    }
    
    OPEN_CONNECTION_METHOD.setAccessible(true);
    return (URLConnection)OPEN_CONNECTION_METHOD.invoke(handler, new Object[] { url });
  }
  
  protected void parseURL(URL context, String spec, int start, int limit)
  {
    if (spec.toLowerCase().startsWith("jar:")) {
      setFile(context, getFileFromSpec(spec.substring(start, limit)));
    }
    else {
      setFile(context, getFileFromContext(context, spec.substring(start, limit)));
    }
  }
  
  private String getFileFromSpec(String spec) {
    int separatorIndex = spec.lastIndexOf("!/");
    if (separatorIndex == -1) {
      throw new IllegalArgumentException("No !/ in spec '" + spec + "'");
    }
    try {
      new URL(spec.substring(0, separatorIndex));
      return spec;
    }
    catch (MalformedURLException ex) {
      throw new IllegalArgumentException("Invalid spec URL '" + spec + "'", ex);
    }
  }
  
  private String getFileFromContext(URL context, String spec) {
    String file = context.getFile();
    if (spec.startsWith("/")) {
      return trimToJarRoot(file) + "!/" + spec.substring(1);
    }
    if (file.endsWith("/")) {
      return file + spec;
    }
    int lastSlashIndex = file.lastIndexOf('/');
    if (lastSlashIndex == -1) {
      throw new IllegalArgumentException("No / found in context URL's file '" + file + "'");
    }
    
    return file.substring(0, lastSlashIndex + 1) + spec;
  }
  
  private String trimToJarRoot(String file) {
    int lastSeparatorIndex = file.lastIndexOf("!/");
    if (lastSeparatorIndex == -1) {
      throw new IllegalArgumentException("No !/ found in context URL's file '" + file + "'");
    }
    
    return file.substring(0, lastSeparatorIndex);
  }
  
  private void setFile(URL context, String file) {
    setURL(context, "jar:", null, -1, null, null, file, null, null);
  }
  
  protected int hashCode(URL u)
  {
    return hashCode(u.getProtocol(), u.getFile());
  }
  
  private int hashCode(String protocol, String file) {
    int result = protocol == null ? 0 : protocol.hashCode();
    int separatorIndex = file.indexOf("!/");
    if (separatorIndex == -1) {
      return result + file.hashCode();
    }
    String source = file.substring(0, separatorIndex);
    String entry = canonicalize(file.substring(separatorIndex + 2));
    try {
      result += new URL(source).hashCode();
    }
    catch (MalformedURLException ex) {
      result += source.hashCode();
    }
    result += entry.hashCode();
    return result;
  }
  
  protected boolean sameFile(URL u1, URL u2)
  {
    if ((!u1.getProtocol().equals("jar")) || (!u2.getProtocol().equals("jar"))) {
      return false;
    }
    int separator1 = u1.getFile().indexOf("!/");
    int separator2 = u2.getFile().indexOf("!/");
    if ((separator1 == -1) || (separator2 == -1)) {
      return super.sameFile(u1, u2);
    }
    String nested1 = u1.getFile().substring(separator1 + "!/".length());
    String nested2 = u2.getFile().substring(separator2 + "!/".length());
    if (!nested1.equals(nested2)) {
      String canonical1 = canonicalize(nested1);
      String canonical2 = canonicalize(nested2);
      if (!canonical1.equals(canonical2)) {
        return false;
      }
    }
    String root1 = u1.getFile().substring(0, separator1);
    String root2 = u2.getFile().substring(0, separator2);
    try {
      return super.sameFile(new URL(root1), new URL(root2));
    }
    catch (MalformedURLException localMalformedURLException) {}
    

    return super.sameFile(u1, u2);
  }
  
  private String canonicalize(String path) {
    return path.replace("!/", "/");
  }
  
  public JarFile getRootJarFileFromUrl(URL url) throws IOException {
    String spec = url.getFile();
    int separatorIndex = spec.indexOf("!/");
    if (separatorIndex == -1) {
      throw new MalformedURLException("Jar URL does not contain !/ separator");
    }
    String name = spec.substring(0, separatorIndex);
    return getRootJarFile(name);
  }
  
  private JarFile getRootJarFile(String name) throws IOException {
    try {
      if (!name.startsWith("file:")) {
        throw new IllegalStateException("Not a file URL");
      }
      String path = name.substring("file:".length());
      File file = new File(URLDecoder.decode(path, "UTF-8"));
      Map<File, JarFile> cache = (Map)rootFileCache.get();
      JarFile result = cache == null ? null : (JarFile)cache.get(file);
      if (result == null) {
        result = new JarFile(file);
        addToRootFileCache(file, result);
      }
      return result;
    }
    catch (Exception ex) {
      throw new IOException("Unable to open root Jar file '" + name + "'", ex);
    }
  }
  




  static void addToRootFileCache(File sourceFile, JarFile jarFile)
  {
    Map<File, JarFile> cache = (Map)rootFileCache.get();
    if (cache == null) {
      cache = new ConcurrentHashMap();
      rootFileCache = new SoftReference(cache);
    }
    cache.put(sourceFile, jarFile);
  }
  






  public static void setUseFastConnectionExceptions(boolean useFastConnectionExceptions)
  {
    JarURLConnection.setUseFastExceptions(useFastConnectionExceptions);
  }
}
