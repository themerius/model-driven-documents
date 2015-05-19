package controllers

import play.api._
import play.api.mvc._

object Application extends Controller {

  def index = Action {
    new eu.themerius.util.AString("Test")
    Ok(views.html.main("Index")("Your new application is ready. Yay"))
  }

}

// http://www.scala-js.org
// http://lihaoyi.github.io/hands-on-scala-js/#IntegratingClient-Server
