package controllers

import play.api._
import play.api.mvc._

import play.api.Play.current

import scala.xml._

import com.tinkerpop.blueprints.impls.orient.OrientGraph
import com.tinkerpop.blueprints.impls.orient.OrientGraphFactory

import scala.collection.JavaConverters._
import scala.collection.JavaConversions._

object TemplateMappingDocElem {
  // TODO: in future this should be loaded from database

  // TODO: make a home for all database related things
  val factory = new OrientGraphFactory("plocal:/tmp/modd-devl").setupPool(1,10)
  def graph = factory.getTx

  // type mapped onto id
  def mapping (tpe: String) = tpe match {
    case "Paragraph" => "templ-001"
    case "Section" => "templ-002"
    case other => "templ-001"
  }

  // public interface
  def getForType (tpe: String) = {
    graph.query.has("uuid", mapping(tpe)).vertices.toList(0)
  }
}

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
    val dVertex = graph.query.has("uuid", uuid).vertices.toList(0)
    val dModel = dVertex.getProperty("model").asInstanceOf[String]
    val dType = dVertex.getProperty("type").asInstanceOf[String]

    // Load a template from a Template DocumentElement
    val tmplDocElem = TemplateMappingDocElem.getForType(dType)
    val tmplStr = tmplDocElem.getProperty("model").asInstanceOf[String]
    val tmplXml = scala.xml.XML.loadString(tmplStr)

    // Load Model/Attributes of the Target DocumentElement
    var attrs = Map("model" -> dModel)

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
