package org.springframework.boot.loader.jar;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.CodeSigner;
import java.security.cert.Certificate;
import java.util.jar.Attributes;
import java.util.jar.Manifest;






















class JarEntry
  extends java.util.jar.JarEntry
  implements FileHeader
{
  private Certificate[] certificates;
  private CodeSigner[] codeSigners;
  private final JarFile jarFile;
  private long localHeaderOffset;
  
  JarEntry(JarFile jarFile, CentralDirectoryFileHeader header)
  {
    super(header.getName().toString());
    this.jarFile = jarFile;
    localHeaderOffset = header.getLocalHeaderOffset();
    setCompressedSize(header.getCompressedSize());
    setMethod(header.getMethod());
    setCrc(header.getCrc());
    setSize(header.getSize());
    setExtra(header.getExtra());
    setComment(header.getComment().toString());
    setSize(header.getSize());
    setTime(header.getTime());
  }
  
  public boolean hasName(String name, String suffix)
  {
    return (getName().length() == name.length() + suffix.length()) && 
      (getName().startsWith(name)) && (getName().endsWith(suffix));
  }
  



  URL getUrl()
    throws MalformedURLException
  {
    return new URL(jarFile.getUrl(), getName());
  }
  
  public Attributes getAttributes() throws IOException
  {
    Manifest manifest = jarFile.getManifest();
    return manifest == null ? null : manifest.getAttributes(getName());
  }
  
  public Certificate[] getCertificates()
  {
    if ((jarFile.isSigned()) && (certificates == null)) {
      jarFile.setupEntryCertificates(this);
    }
    return certificates;
  }
  
  public CodeSigner[] getCodeSigners()
  {
    if ((jarFile.isSigned()) && (codeSigners == null)) {
      jarFile.setupEntryCertificates(this);
    }
    return codeSigners;
  }
  
  void setCertificates(java.util.jar.JarEntry entry) {
    certificates = entry.getCertificates();
    codeSigners = entry.getCodeSigners();
  }
  
  public long getLocalHeaderOffset()
  {
    return localHeaderOffset;
  }
}
