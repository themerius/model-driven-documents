package controllers

import play.api._
import play.api.mvc._

import play.api.Play.current

import scala.xml.XML

import com.tinkerpop.blueprints.impls.orient.OrientGraph

import scala.collection.JavaConverters._
import scala.collection.JavaConversions._

object Application extends Controller {
  def index = Action {
    eu.themerius.util.DocElemXmlReader.readXml
    Graph.init
    val query = Graph.graph.query.has("type", "Test").vertices.toList
    Ok(views.html.main("Index")(s"Your new application is ready. ${query.map(_.getProperty("model").asInstanceOf[String])}"))
  }

}

object Graph {
  val graph = new OrientGraph("plocal:/tmp/modd-devl")
  var ifinit = false

  def init = if (!ifinit) {
    val docelem = graph.addVertex("class:DocElem", "type", "TEST")
    docelem.setProperty("type", "Test")
    docelem.setProperty("model", "Serialized Model!")
    docelem.setProperty("uri", "aaaa")
    graph.commit()
    docelem
    ifinit = true
  }
}

// http://www.scala-js.org
// http://lihaoyi.github.io/hands-on-scala-js/#IntegratingClient-Server
