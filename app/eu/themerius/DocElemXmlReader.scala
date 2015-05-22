package eu.themerius.util

import scala.xml.XML
import play.api.Play
import play.api.Play.current

object DocElemXmlReader {
  def readXml = {
    Play.resourceAsStream("public/docs/About.xml") match {
      case Some(file) => {
        val xml = XML.load(file)
        println(xml \\ "modd" \\ "docelems" \\ "docelem" \ "model" map (_.text))
      }
      case None => println("Not found")
    }
  }
}
