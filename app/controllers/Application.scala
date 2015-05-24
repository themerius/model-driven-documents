package controllers

import play.api._
import play.api.mvc._

import play.api.Play.current

import scala.xml.XML

import com.tinkerpop.blueprints.impls.orient.OrientGraphFactory

import scala.collection.JavaConverters._
import scala.collection.JavaConversions._

object Application extends Controller {
  val factory = new OrientGraphFactory("plocal:/tmp/modd-devl").setupPool(1,10)
  def graph = factory.getTx

  def index = Action {
    eu.themerius.util.DocElemXmlReader.toDatabase
    //Graph.init
    //val query = Graph.graph.query.has("type", "Paragraph").vertices.toList
    //Ok(views.html.main("Index")(s"Your new application is ready. ${query.map(_.getProperty("model").asInstanceOf[String])}"))
    Ok(views.html.main("Index")(s"Your new application is ready."))

  }

  def docElem(uuid: String) = Action {
    val res = graph.query.has("uuid", uuid).vertices.toList
    val model = res.map(_.getProperty("model").asInstanceOf[String])
    Ok(views.html.main(s"DocElem ${uuid}")(model(0)))
  }

}

// http://www.scala-js.org
// http://lihaoyi.github.io/hands-on-scala-js/#IntegratingClient-Server
