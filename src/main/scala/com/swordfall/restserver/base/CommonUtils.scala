package com.swordfall.restserver.base

import scala.collection.mutable.ListBuffer

object CommonUtils {

  /**
    * 排列组合
    */
  def combination(partition: String): List[String] ={
    //val partition = "(p_provincecode=510000::p_date=2019-05-01::p_hour=1,2,3::p_region=10001,10002::p_min=24)"
    val keyItemArray = partition.replace("(", "").replace(")", "").split("::")

    val keyItemList = new ListBuffer[List[String]]

    for (keyItem <- keyItemArray){
      if (keyItem.contains(",")){
        val keyValueArray = keyItem.split("=")
        val valuesList = keyValueArray(1).split(",").map(keyValueArray(0) + "=" + _)
        keyItemList += valuesList.toList
      }else{
        keyItemList += List(keyItem)
      }
    }

    val keyResult = keyItemList.reduceLeft((x: List[String], y: List[String]) => for (x1 <- x; y1 <- y) yield x1 + "," + y1)

    return keyResult
  }
}
