package org.springframework.boot.loader.jar;

import org.springframework.boot.loader.data.RandomAccessData;

abstract interface CentralDirectoryVisitor
{
  public abstract void visitStart(CentralDirectoryEndRecord paramCentralDirectoryEndRecord, RandomAccessData paramRandomAccessData);
  
  public abstract void visitFileHeader(CentralDirectoryFileHeader paramCentralDirectoryFileHeader, int paramInt);
  
  public abstract void visitEnd();
}
