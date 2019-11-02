package com.hongshan.bdp.arrivalinspect.base

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.Directives.{complete, get, path}
import akka.stream.ActorMaterializer

import scala.io.StdIn

/**
  * handle client requests as a RESTFUL server
  */
object RouteWebServer {

  def main(args: Array[String]): Unit = {

    getMethod()
  }

  /**
    * Routing DSL for HTTP servers
    * http get 方式访问
    */
  def getMethod(): Unit ={
    //隐式变量
    implicit val system = ActorSystem("api-server")
    implicit val materializer = ActorMaterializer()
    implicit val executionContext = system.dispatcher

    val route = path("hello"){
      get{
        complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, "<h1> Say hello to akka-http</h1>"))
      }
    }

    val bingdingFuture = Http().bindAndHandle(route, "192.168.187.200", 8080)

    println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")

    StdIn.readLine() // let it run until user presses return
    bingdingFuture
      .flatMap(_.unbind()) // trigger unbinding from the port
      .onComplete(_ => system.terminate()) //and shutdown when done
  }

}
