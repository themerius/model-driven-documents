package controllers

import play.api._
import play.api.mvc._

import play.api.Play.current

import scala.xml._

import com.tinkerpop.blueprints.impls.orient.OrientGraph

import scala.collection.JavaConverters._
import scala.collection.JavaConversions._

object Application extends Controller {
  val graph = new OrientGraph("plocal:/tmp/modd-devl")

  def index = Action {
    eu.themerius.util.DocElemXmlReader.toDatabase
    //Graph.init
    //val query = Graph.graph.query.has("type", "Paragraph").vertices.toList
    //Ok(views.html.main("Index")(s"Your new application is ready. ${query.map(_.getProperty("model").asInstanceOf[String])}"))
    Ok(views.html.main("Index")("Your new application is ready."))
  }

  def docElem(uuid: String) = Action {
    val res = graph.query.has("uuid", uuid).vertices.toList
    val model = res.map(_.getProperty("model").asInstanceOf[String])

    // Load a template from a Template DocumentElement
    val tmplDocElem = graph.query.has("uuid", "templ-001").vertices.toList(0)
    val tmplStr = tmplDocElem.getProperty("model").asInstanceOf[String]
    val tmplXml = scala.xml.XML.loadString(tmplStr)

    // Load Model/Attributes of the Target DocumentElement
    var attrs = Map("model" -> model(0))

    // Apply Template on the Target
    val tmplXmlComplete = new scala.xml.transform.RewriteRule {
       override def transform(n: Node): Seq[Node] = n match {
         case <ref>{key}</ref> => <span>{scala.xml.Unparsed(attrs(key.text))}</span>
         case elem: Elem => elem copy (child = elem.child flatMap (this transform))
         case other => other
       }
     } transform tmplXml

    // Place applied template into the view-grid
    Ok(views.html.main(s"DocElem ${uuid}")(tmplXmlComplete(0).child.mkString))
  }
}

// http://www.scala-js.org
// http://lihaoyi.github.io/hands-on-scala-js/#IntegratingClient-Server
