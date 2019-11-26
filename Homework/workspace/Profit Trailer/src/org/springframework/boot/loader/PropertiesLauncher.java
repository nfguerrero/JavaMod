package org.springframework.boot.loader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.lang.reflect.Constructor;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.jar.Attributes;
import java.util.jar.Manifest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.boot.loader.archive.Archive;
import org.springframework.boot.loader.archive.Archive.Entry;
import org.springframework.boot.loader.archive.Archive.EntryFilter;
import org.springframework.boot.loader.archive.ExplodedArchive;
import org.springframework.boot.loader.archive.JarFileArchive;
import org.springframework.boot.loader.util.SystemPropertyUtils;


















































































public class PropertiesLauncher
  extends Launcher
{
  private static final String DEBUG = "loader.debug";
  public static final String MAIN = "loader.main";
  public static final String PATH = "loader.path";
  public static final String HOME = "loader.home";
  public static final String ARGS = "loader.args";
  public static final String CONFIG_NAME = "loader.config.name";
  public static final String CONFIG_LOCATION = "loader.config.location";
  public static final String SET_SYSTEM_PROPERTIES = "loader.system";
  private static final Pattern WORD_SEPARATOR = Pattern.compile("\\W+");
  
  private final File home;
  
  private List<String> paths = new ArrayList();
  
  private final Properties properties = new Properties();
  private Archive parent;
  
  public PropertiesLauncher()
  {
    try {
      home = getHomeDirectory();
      initializeProperties();
      initializePaths();
      parent = createArchive();
    }
    catch (Exception ex) {
      throw new IllegalStateException(ex);
    }
  }
  
  protected File getHomeDirectory() {
    try {
      return new File(getPropertyWithDefault("loader.home", "${user.dir}"));
    }
    catch (Exception ex) {
      throw new IllegalStateException(ex);
    }
  }
  
  private void initializeProperties() throws Exception, IOException {
    List<String> configs = new ArrayList();
    String[] names; if (getProperty("loader.config.location") != null) {
      configs.add(getProperty("loader.config.location"));
    }
    else
    {
      names = getPropertyWithDefault("loader.config.name", "loader,application").split(",");
      for (String name : names) {
        configs.add("file:" + getHomeDirectory() + "/" + name + ".properties");
        configs.add("classpath:" + name + ".properties");
        configs.add("classpath:BOOT-INF/classes/" + name + ".properties");
      }
    }
    for (String config : configs) {
      InputStream resource = getResource(config);
      if (resource != null) {
        debug("Found: " + config);
        try {
          properties.load(resource);
        }
        finally {
          resource.close();
        }
        for (Object key : Collections.list(properties.propertyNames())) {
          if ((config.endsWith("application.properties")) && 
            (((String)key).startsWith("loader."))) {
            warn("Use of application.properties for PropertiesLauncher is deprecated");
          }
          String text = properties.getProperty((String)key);
          
          String value = SystemPropertyUtils.resolvePlaceholders(properties, text);
          if (value != null) {
            properties.put(key, value);
          }
        }
        if ("true".equals(getProperty("loader.system"))) {
          debug("Adding resolved properties to System properties");
          for (Object key : Collections.list(properties.propertyNames())) {
            String value = properties.getProperty((String)key);
            System.setProperty((String)key, value);
          }
        }
        
        return;
      }
      
      debug("Not found: " + config);
    }
  }
  
  private InputStream getResource(String config) throws Exception
  {
    if (config.startsWith("classpath:")) {
      return getClasspathResource(config.substring("classpath:".length()));
    }
    config = stripFileUrlPrefix(config);
    if (isUrl(config)) {
      return getURLResource(config);
    }
    return getFileResource(config);
  }
  
  private String stripFileUrlPrefix(String config) {
    if (config.startsWith("file:")) {
      config = config.substring("file:".length());
      if (config.startsWith("//")) {
        config = config.substring(2);
      }
    }
    return config;
  }
  
  private boolean isUrl(String config) {
    return config.contains("://");
  }
  
  private InputStream getClasspathResource(String config) {
    while (config.startsWith("/")) {
      config = config.substring(1);
    }
    config = "/" + config;
    debug("Trying classpath: " + config);
    return getClass().getResourceAsStream(config);
  }
  
  private InputStream getFileResource(String config) throws Exception {
    File file = new File(config);
    debug("Trying file: " + config);
    if (file.canRead()) {
      return new FileInputStream(file);
    }
    return null;
  }
  
  private InputStream getURLResource(String config) throws Exception {
    URL url = new URL(config);
    if (exists(url)) {
      URLConnection con = url.openConnection();
      try {
        return con.getInputStream();
      }
      catch (IOException ex)
      {
        if ((con instanceof HttpURLConnection)) {
          ((HttpURLConnection)con).disconnect();
        }
        throw ex;
      }
    }
    return null;
  }
  
  private boolean exists(URL url) throws IOException
  {
    URLConnection connection = url.openConnection();
    try {
      connection.setUseCaches(connection
        .getClass().getSimpleName().startsWith("JNLP"));
      HttpURLConnection httpConnection; if ((connection instanceof HttpURLConnection)) {
        httpConnection = (HttpURLConnection)connection;
        httpConnection.setRequestMethod("HEAD");
        int responseCode = httpConnection.getResponseCode();
        boolean bool; if (responseCode == 200) {
          return true;
        }
        if (responseCode == 404) {
          return false;
        }
      }
      return connection.getContentLength() >= 0 ? 1 : 0;
    }
    finally {
      if ((connection instanceof HttpURLConnection)) {
        ((HttpURLConnection)connection).disconnect();
      }
    }
  }
  
  private void initializePaths() throws Exception {
    String path = getProperty("loader.path");
    if (path != null) {
      paths = parsePathsProperty(path);
    }
    debug("Nested archive paths: " + paths);
  }
  
  private List<String> parsePathsProperty(String commaSeparatedPaths) {
    List<String> paths = new ArrayList();
    for (String path : commaSeparatedPaths.split(",")) {
      path = cleanupPath(path);
      
      path = "".equals(path) ? "/" : path;
      paths.add(path);
    }
    if (paths.isEmpty()) {
      paths.add("lib");
    }
    return paths;
  }
  
  protected String[] getArgs(String... args) throws Exception {
    String loaderArgs = getProperty("loader.args");
    if (loaderArgs != null) {
      String[] defaultArgs = loaderArgs.split("\\s+");
      String[] additionalArgs = args;
      args = new String[defaultArgs.length + additionalArgs.length];
      System.arraycopy(defaultArgs, 0, args, 0, defaultArgs.length);
      System.arraycopy(additionalArgs, 0, args, defaultArgs.length, additionalArgs.length);
    }
    
    return args;
  }
  
  protected String getMainClass() throws Exception
  {
    String mainClass = getProperty("loader.main", "Start-Class");
    if (mainClass == null) {
      throw new IllegalStateException("No 'loader.main' or 'Start-Class' specified");
    }
    
    return mainClass;
  }
  
  protected ClassLoader createClassLoader(List<Archive> archives) throws Exception
  {
    Set<URL> urls = new LinkedHashSet(archives.size());
    for (Archive archive : archives) {
      urls.add(archive.getUrl());
    }
    
    Object loader = new LaunchedURLClassLoader((URL[])urls.toArray(new URL[0]), getClass().getClassLoader());
    debug("Classpath: " + urls);
    String customLoaderClassName = getProperty("loader.classLoader");
    if (customLoaderClassName != null) {
      loader = wrapWithCustomClassLoader((ClassLoader)loader, customLoaderClassName);
      debug("Using custom class loader: " + customLoaderClassName);
    }
    return loader;
  }
  

  private ClassLoader wrapWithCustomClassLoader(ClassLoader parent, String loaderClassName)
    throws Exception
  {
    Class<ClassLoader> loaderClass = Class.forName(loaderClassName, true, parent);
    try
    {
      return (ClassLoader)loaderClass.getConstructor(new Class[] { ClassLoader.class }).newInstance(new Object[] { parent });
    }
    catch (NoSuchMethodException localNoSuchMethodException)
    {
      try
      {
        return 
          (ClassLoader)loaderClass.getConstructor(new Class[] { [Ljava.net.URL.class, ClassLoader.class }).newInstance(new Object[] { new URL[0], parent });
      }
      catch (NoSuchMethodException localNoSuchMethodException1) {}
    }
    
    return (ClassLoader)loaderClass.newInstance();
  }
  
  private String getProperty(String propertyKey) throws Exception {
    return getProperty(propertyKey, null, null);
  }
  
  private String getProperty(String propertyKey, String manifestKey) throws Exception {
    return getProperty(propertyKey, manifestKey, null);
  }
  
  private String getPropertyWithDefault(String propertyKey, String defaultValue) throws Exception
  {
    return getProperty(propertyKey, null, defaultValue);
  }
  
  private String getProperty(String propertyKey, String manifestKey, String defaultValue) throws Exception
  {
    if (manifestKey == null) {
      manifestKey = propertyKey.replace('.', '-');
      manifestKey = toCamelCase(manifestKey);
    }
    String property = SystemPropertyUtils.getProperty(propertyKey);
    if (property != null) {
      String value = SystemPropertyUtils.resolvePlaceholders(properties, property);
      
      debug("Property '" + propertyKey + "' from environment: " + value);
      return value;
    }
    if (properties.containsKey(propertyKey)) {
      String value = SystemPropertyUtils.resolvePlaceholders(properties, properties
        .getProperty(propertyKey));
      debug("Property '" + propertyKey + "' from properties: " + value);
      return value;
    }
    try {
      if (home != null)
      {
        Manifest manifest = new ExplodedArchive(home, false).getManifest();
        if (manifest != null) {
          String value = manifest.getMainAttributes().getValue(manifestKey);
          if (value != null) {
            debug("Property '" + manifestKey + "' from home directory manifest: " + value);
            
            return SystemPropertyUtils.resolvePlaceholders(properties, value);
          }
        }
      }
    }
    catch (IllegalStateException localIllegalStateException) {}
    



    Manifest manifest = createArchive().getManifest();
    if (manifest != null) {
      String value = manifest.getMainAttributes().getValue(manifestKey);
      if (value != null) {
        debug("Property '" + manifestKey + "' from archive manifest: " + value);
        return SystemPropertyUtils.resolvePlaceholders(properties, value);
      }
    }
    return defaultValue == null ? defaultValue : 
      SystemPropertyUtils.resolvePlaceholders(properties, defaultValue);
  }
  
  protected List<Archive> getClassPathArchives() throws Exception
  {
    List<Archive> lib = new ArrayList();
    for (String path : paths) {
      for (Archive archive : getClassPathArchives(path)) {
        if ((archive instanceof ExplodedArchive))
        {
          List<Archive> nested = new ArrayList(archive.getNestedArchives(new ArchiveEntryFilter(null)));
          nested.add(0, archive);
          lib.addAll(nested);
        }
        else {
          lib.add(archive);
        }
      }
    }
    addNestedEntries(lib);
    return lib;
  }
  
  private List<Archive> getClassPathArchives(String path) throws Exception {
    String root = cleanupPath(stripFileUrlPrefix(path));
    List<Archive> lib = new ArrayList();
    File file = new File(root);
    if (!"/".equals(root)) {
      if (!isAbsolutePath(root)) {
        file = new File(home, root);
      }
      if (file.isDirectory()) {
        debug("Adding classpath entries from " + file);
        Archive archive = new ExplodedArchive(file, false);
        lib.add(archive);
      }
    }
    Archive archive = getArchive(file);
    if (archive != null) {
      debug("Adding classpath entries from archive " + archive.getUrl() + root);
      lib.add(archive);
    }
    List<Archive> nestedArchives = getNestedArchives(root);
    if (nestedArchives != null) {
      debug("Adding classpath entries from nested " + root);
      lib.addAll(nestedArchives);
    }
    return lib;
  }
  
  private boolean isAbsolutePath(String root)
  {
    return (root.contains(":")) || (root.startsWith("/"));
  }
  
  private Archive getArchive(File file) throws IOException {
    String name = file.getName().toLowerCase();
    if ((name.endsWith(".jar")) || (name.endsWith(".zip"))) {
      return new JarFileArchive(file);
    }
    return null;
  }
  
  private List<Archive> getNestedArchives(String path) throws Exception {
    Archive parent = this.parent;
    String root = path;
    if (((!root.equals("/")) && (root.startsWith("/"))) || 
      (parent.getUrl().equals(home.toURI().toURL())))
    {
      return null;
    }
    if (root.contains("!")) {
      int index = root.indexOf("!");
      File file = new File(home, root.substring(0, index));
      if (root.startsWith("jar:file:")) {
        file = new File(root.substring("jar:file:".length(), index));
      }
      parent = new JarFileArchive(file);
      root = root.substring(index + 1, root.length());
      while (root.startsWith("/")) {
        root = root.substring(1);
      }
    }
    if (root.endsWith(".jar")) {
      File file = new File(home, root);
      if (file.exists()) {
        parent = new JarFileArchive(file);
        root = "";
      }
    }
    if ((root.equals("/")) || (root.equals("./")) || (root.equals(".")))
    {
      root = "";
    }
    Archive.EntryFilter filter = new PrefixMatchingArchiveFilter(root, null);
    List<Archive> archives = new ArrayList(parent.getNestedArchives(filter));
    if ((("".equals(root)) || (".".equals(root))) && (!path.endsWith(".jar")) && (parent != this.parent))
    {


      archives.add(parent);
    }
    return archives;
  }
  

  private void addNestedEntries(List<Archive> lib)
  {
    try
    {
      lib.addAll(parent.getNestedArchives(new Archive.EntryFilter()
      {
        public boolean matches(Archive.Entry entry)
        {
          if (entry.isDirectory()) {
            return entry.getName().equals("BOOT-INF/classes/");
          }
          return entry.getName().startsWith("BOOT-INF/lib/");
        }
      }));
    }
    catch (IOException localIOException) {}
  }
  


  private String cleanupPath(String path)
  {
    path = path.trim();
    
    if (path.startsWith("./")) {
      path = path.substring(2);
    }
    if ((path.toLowerCase().endsWith(".jar")) || (path.toLowerCase().endsWith(".zip"))) {
      return path;
    }
    if (path.endsWith("/*")) {
      path = path.substring(0, path.length() - 1);


    }
    else if ((!path.endsWith("/")) && (!path.equals("."))) {
      path = path + "/";
    }
    
    return path;
  }
  
  public static void main(String[] args) throws Exception {
    PropertiesLauncher launcher = new PropertiesLauncher();
    args = launcher.getArgs(args);
    launcher.launch(args);
  }
  
  public static String toCamelCase(CharSequence string) {
    if (string == null) {
      return null;
    }
    StringBuilder builder = new StringBuilder();
    Matcher matcher = WORD_SEPARATOR.matcher(string);
    int pos = 0;
    while (matcher.find()) {
      builder.append(capitalize(string.subSequence(pos, matcher.end()).toString()));
      pos = matcher.end();
    }
    builder.append(capitalize(string.subSequence(pos, string.length()).toString()));
    return builder.toString();
  }
  
  private static String capitalize(String str) {
    return Character.toUpperCase(str.charAt(0)) + str.substring(1);
  }
  
  private void debug(String message) {
    if (Boolean.getBoolean("loader.debug")) {
      log(message);
    }
  }
  
  private void warn(String message) {
    log("WARNING: " + message);
  }
  
  private void log(String message)
  {
    System.out.println(message);
  }
  


  private static final class PrefixMatchingArchiveFilter
    implements Archive.EntryFilter
  {
    private final String prefix;
    

    private final PropertiesLauncher.ArchiveEntryFilter filter = new PropertiesLauncher.ArchiveEntryFilter(null);
    
    private PrefixMatchingArchiveFilter(String prefix) {
      this.prefix = prefix;
    }
    
    public boolean matches(Archive.Entry entry)
    {
      if (entry.isDirectory()) {
        return entry.getName().equals(prefix);
      }
      return (entry.getName().startsWith(prefix)) && (filter.matches(entry));
    }
  }
  

  private static final class ArchiveEntryFilter
    implements Archive.EntryFilter
  {
    private static final String DOT_JAR = ".jar";
    
    private static final String DOT_ZIP = ".zip";
    

    private ArchiveEntryFilter() {}
    
    public boolean matches(Archive.Entry entry)
    {
      return (entry.getName().endsWith(".jar")) || (entry.getName().endsWith(".zip"));
    }
  }
}
