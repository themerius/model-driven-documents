package eu.themerius.util

import scala.xml.XML
import play.api.Play
import play.api.Play.current

import com.tinkerpop.blueprints.impls.orient.OrientGraphFactory

import scala.collection.JavaConverters._
import scala.collection.JavaConversions._

case class DocElem(id: String, dtype: String, model: String)
// mtype für modell typ

object DocElemXmlReader {
  val factory = new OrientGraphFactory("plocal:/tmp/modd-devl").setupPool(1,10)
  def getGraph = factory.getTx

  def readXml = {
    Play.resourceAsStream("public/docs/About.xml") match {
      case Some(file) => {
        val xml = XML.load(file)
        val docElems = xml \\ "modd" \\ "docelems" \\ "docelem"
        docElems.map(
          d => DocElem(d \ "id" text, d \ "type" text, (d \ "model")(0).child.mkString)
        )
      }
      case None => Nil
    }
  }

  def toDatabase = {
    val docElems = readXml
    println(docElems)
    val g = getGraph
    docElems.map { d =>
      val v = g.addVertex(
        "class:DocElem",
        "type", d.dtype,
        "model", d.model,
        "uuid", d.id
      )
    }
    g.commit()
  }
}
