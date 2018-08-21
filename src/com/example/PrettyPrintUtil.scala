package com.example

import scala.collection.immutable.ListMap

object PrettyPrintUtil {
  
  def print(wordsCount : Map[String, Int], topNToPrint : Int) : Unit = {
    val orderedList = ListMap(wordsCount.toSeq.sortWith(_._2 > _._2): _*).take(topNToPrint)

    orderedList foreach(x => printf("Word : '%s' \t\t Count : %d \n", x._1,x._2))
    
  }
  
}