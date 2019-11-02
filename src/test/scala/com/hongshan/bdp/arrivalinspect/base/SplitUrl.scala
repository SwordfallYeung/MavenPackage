package com.hongshan.bdp.arrivalinspect.base

import scala.collection.mutable.ListBuffer

object SplitUrl {

  def main(args: Array[String]): Unit = {

    calculateTwo
  }

  def calculateTwo(): Unit ={
    val partition = "(p_provincecode=510000::p_date=2019-05-01::p_hour=1,2,3::p_region=10001,10002::p_min=24)"
    val keyItemArray = partition.replace("(", "").replace(")", "").split("::")
    println(keyItemArray.toList)

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

    for (key <- keyResult){
      println(key)
    }
  }

  def calculateOne(): Unit ={
    val partition = "(p_provincecode=510000::p_date=2019-05-01::p_hour=1,2,3::p_region=100001,10002::p_min=24)"
    val partitionSubArray = partition.replace("(", "").replace(")", "").split("::")

    val array = new Array[Array[String]](partitionSubArray.size)
    var index = 0
    for (ps <- partitionSubArray){
      val keyValue = ps.split("=")
      keyValue(0)
      val valueArray = keyValue(1).split(",").map(x => keyValue(0) + "=" + x)
      array(index) = valueArray
      index += 1
    }

    //val arr = Array(Array("p_provincecode=510000"), Array("p_date=2019-05-01"), Array("p_hour=1", "p_hour=2", "p_hour=3"), Array("p_region=100001", "p_region=10002"), Array("p_min=24"))
    val resultArr = doExchange(array)

    for (a <- resultArr){
      println(a)
    }
  }

  /**
    *  执行组合排列的函数
     * @param partitionSubArray
    */
  def doExchange(arr: Array[Array[String]]): Array[String] ={

    var len = arr.size

    // 当数组大于等于2个的时候
    if (len >= 2){
      // 第一个数组的长度
      val len1 = arr(0).size
      // 第二个数组的长度
      val len2 = arr(1).size
      // 2个数组产生的组合数
      val lenBoth = len1 * len2
      // 声明一个新数组，做数据缓存
      val items = new Array[String](lenBoth)
      // 声明新数组的索引
      var index = 0
      // 2层循环，将组合放到新数组中
      for (i <- 0 until len1){
        for (j <- 0 until len2){
          items(index) = arr(0)(i) + "," + arr(1)(j)
          index += 1
        }
      }

      // 将新组合的数组并到原数组中
      var newArr = new Array[Array[String]](len - 1)
      for (i <- 2 until arr.length){
        newArr(i - 1) = arr(i)
      }
      newArr(0) = items
      //执行回调
      return doExchange(newArr)
    }else{
      return arr(0)
    }
  }
}
