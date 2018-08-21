package com.example

import scala.io.Source
import java.io.PrintWriter
import java.io.File
import scala.collection.mutable.Stack
import scala.collection.immutable.ListMap

object Main {
  def main(args: Array[String]): Unit = {
    val fileName = args(0) //full path to the file we want to reverse
    val basePath = args(1) //base path where to use for put the answer
    val maxBytes = args(2).toInt // max byte to load to memory 
    var numOfFiles = 1

    var wordsCountMap = Map.empty[String, Int]
    var linesStack = Stack.empty[String]
    var stackSizeInByte = 0;

    for (line <- Source.fromFile(fileName).getLines().filter(!_.isEmpty())) {
      val reverse = line.reverse
      val reverseInBytes = reverse.getBytes

      //count words in order to find 5 most frequent words.
      val countWord = countWordsInLine(reverse)
      wordsCountMap = mergeMaps(wordsCountMap, countWord)

      //write the reverse file
      //Assumption : There is no single line that is bigger than max size to load to memory since the program will never work.
      stackSizeInByte += reverse.getBytes.size
      if (stackSizeInByte < maxBytes) {
        linesStack.push(reverse)
      } else {
        FileWriterUtils.writeIntermidFile(linesStack, numOfFiles.toString(), basePath)
        numOfFiles += 1
        stackSizeInByte = reverse.getBytes.size;
        linesStack.clear()
        linesStack.push(reverse)
      }

    }
    FileWriterUtils.writeIntermidFile(linesStack, numOfFiles.toString(), basePath)
    FileWriterUtils.writeFinalFile(basePath)
    PrettyPrintUtil.print(wordsCountMap, 5)
  }

  

  /* Get single line and counts only word.
   * Assumption : this including numbers, i consider it as part of the test unlike special characters like '{'
  */
  def countWordsInLine(line: String): Map[String, Int] = {
    line
      .split("\\W+")
      .foldLeft(Map.empty[String, Int]) {
        (count, word) => count + (word -> (count.getOrElse(word, 0) + 1))
      }
  }

  /*
   * Aggregate the words count.
   */
  def mergeMaps(mergeTo: Map[String, Int], mergeFrom: Map[String, Int]): Map[String, Int] = {
    mergeTo ++ mergeFrom.map { case (word, count) => word -> (count + mergeTo.getOrElse(word, 0)) }
  }
}