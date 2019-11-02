package com.swordfall.restserver.base

import java.util

import akka.Done
import akka.actor.ActorSystem
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import akka.http.scaladsl.server.Directives.{post, _}
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, StatusCodes}
import com.hongshan.bdp.base.LogSupport
import com.hongshan.bdp.database.entity.DatabaseConnection
import com.hongshan.bdp.datamanager.common.KeyManager
import com.hongshan.bdp.datamanager.entity.{DataBoard, DataBoardManager}

import scala.io.StdIn

object WebServer extends KeyManager with LogSupport{

  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()
  implicit val executionContext = system.dispatcher

  //PropertyConfig.loadProperties()
  val ip = "0.0.0.0"
  val port = 8080

  def main(args: Array[String]): Unit = {
    updateDataStatus()
  }

  /**
    * REST GET 方式访问
    */
  def updateDataStatus(): Unit ={

    try {
      //get的路由
      val route: Route = get{
        path("api" / "updateDataStatus"){
          //传入参数
          parameters("tableType".as[String], "dbName".as[String], "tableName".as[String], "partition".as[String], "flag".as[Boolean]){ (tableType, dbName, tableName, partition, flag) =>
            //println("tableType:" + tableType + ", dbName:" + dbName + ", tableName:" + tableName + ", partition:" + partition)
            val partitionKeyList = CommonUtils.combination(partition)

            /**
              * 数据操作，数据表翻牌，更新pg数据库
              *
              * 调用datamanager的api接口updateDataStatusByKey
              */
            var result = new StringBuilder
            result.append("[")
            val headKey = getHeadKey(tableType, dbName, tableName)
            for (partitionKey <- partitionKeyList){
              val key = headKey + partitionKey
              //println(key)
              val resultTuple = DataBoardManager().updateDataStatusByKey(key, flag)
              if (resultTuple._1){
                //数据翻牌成功
                result.append("{ \"result\" : \"success\", \"msg\": \"" + key + "\" },")
              }else{
                //数据翻牌失败
                result.append("{ \"result\" : \"failure\", \"msg\" : \"" + resultTuple._2 + "\" },")
              }
            }

            val resultString = result.substring(0, result.lastIndexOf(",")) + "]"
            //println(resultString)

            //val resultString = "success"
            logInfo(s"HTTP Api UpdateDataStatus end, result: " + resultString)

            //返回结果
            complete(HttpEntity(ContentTypes.`application/json`, resultString))
          }
        }
      }

      // http绑定并运行
      val bindingFuture = Http().bindAndHandle(route, ip, port)

    }catch {
      case e: Exception => {
        logInfo("restserver have a exception, " + e.getMessage)
      }
    }
  }
}
