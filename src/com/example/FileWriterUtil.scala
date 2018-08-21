package com.example

import java.io.PrintWriter
import scala.collection.mutable.Stack
import java.io.File
import scala.io.Source
import java.nio.file.Files
import java.nio.file.Paths

object FileWriterUtils  {
  
  def writeIntermidFile(lines: Stack[String], fileName: String, basePath : String): Unit = {
    val path = basePath+"\\Files"
    if (!Files.exists(Paths.get(path)))
      Files.createDirectory(Paths.get(path))
    val fileFullName = path + fileName + ".txt"
    val writer = new PrintWriter(new File(fileFullName))
    lines.foreach((line) => writer.append(line).append("\n"))
    writer.close
  }
  
  def writeFinalFile(basePath : String): Unit = {
    val path = basePath+"\\Files\\"
    val writer = new PrintWriter(new File(path + "final.txt"))
    val partFiles = getFilesPartNames(path)
    
    partFiles
    .foreach(f => {Source
                  .fromFile(f)
                  .getLines()
                  .foreach(l => writer.append(l).append("\n"))
             writer.flush()
                  })
     writer.close()
     partFiles.take(partFiles.size-1).foreach(_.delete())
  }
  
  private def getFilesPartNames(dir: String): List[File] = {
    val d = new File(dir)
    if (d.exists() && d.isDirectory()) {
      d.listFiles().filter(_.isFile()).sorted.reverse.toList
    } else {
      List[File]()
    }
  }
  
}