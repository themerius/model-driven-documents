package eu.themerius.util

import scala.math.abs
import scala.util.Random

class AString(str: String) {
  val indexIdMap = str.zipWithIndex.map {
    case (char, idx) => (idx, genId) -> char
  }.toMap

  val idMap = indexIdMap.map(row => row._1._2 ->  row._2)
  val indexMap = indexIdMap.map(row => row._1)

  val length = str.length

  def genId = abs(Random.nextInt)

  override def toString = {
    for (idx <- 0 until length) yield {
      val id = indexMap(idx)
      idMap(id)
    }
  }.mkString
}

// Markup ist auch nur eine Annotaiton. Es hat genau so eine Position.
// Jedoch wird Markup direkt im Text selbst festgehalten.
// Markup ist daher auf eine valide geschachtelte eine Baumstruktur gebunden.
// Reine Annotationen brauchen nur eine Position (z.B. in einem Text)
// und sind also nicht an diese Restriktion gebuden.
// Reine Annotationen können also in verschidenen Layern beliebig positioniert werden.
// Eine Markup-Annotaiton hat nur ein Layer, z.B. eben die des Textes.
// Das heißt wenn z.B. an ein und der selben Stelle unterschiedliche Markups
// gelten sollen, müssen diese im selben Layer (oder Ebene) ineinander
// verschachtelt werden. Sie können dann also nicht getrennt voneinander existieren.

// Das ist ein Beispieltext.

// Das <i>ist</i> ein Beispieltext.
// Das <i><b>ist</b></i> ein Beispieltext.

// Im gleichen Layer oder in verschiedenen Layer:
// 4-6: italic
// 4-6: bold

// Weitere Möglichkeit, aber eingeschränkt auf Token-Ebene:
// (Teil-Wörter oder ähnliches wird schwieriger); (Tokenizer muss für Modell explizit existieren, damit annotiert werden kann)
// <id1>Das</id1> <id2>ist</id2> <id3>ein</id3> <id4>Beispieltext.</id4>
// Annotation:
// #id2: italic
