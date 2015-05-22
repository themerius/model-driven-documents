package eu.themerius.dsl

trait DocElemGrammar {
  val URI = "localhost"
  def / (id: String) = {
    println(URI + "/§/" + id)
    this
  }
  def - (dType: String) = {
    println(dType)
    this
  }
  def -- (model: String) = {
    println(model.replaceFirst("\n", "").replaceAll("  ", "").trim)
    println
    this
  }
}

trait DocElemDSL {
  val Section = "Section"
  val Paragraph = "Paragraph"
  val TOC = "TOC"
  object * extends DocElemGrammar
}

object TestDoc extends DocElemDSL {
  * / "aaaaaz" - TOC -- """
    - aaaaaa
    -- aaaaab
  """

  * / "aaaaaa" - Section -- """
    Introduction
  """

  * / "aaaaab" - Paragraph -- """
    Hello World!
  """
}

//Document

// ID Generation Algorithms:
// ID besteht aus UUID + Inizial-Zeit?
// - http://kvz.io/blog/2009/06/10/create-short-ids-with-php-like-youtube-or-tinyurl/
// - http://hashids.org
// - http://instagram-engineering.tumblr.com/post/10853187575/sharding-ids-at-instagram
