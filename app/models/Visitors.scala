package models

import anorm._
import play.api.libs.json._


case class Visitors(
                 ID_goscia: Option[Long] = None,
                 ID_pokoju: Int,
                 Data_zam: String,
                 Data_zwol: String,
                 Imie: String,
                 Nazwisko: String,
                 Numer_tel: String)

object Visitors {
  implicit def toParameters: ToParameterList[Visitors] = Macro.toParameters[Visitors]

  /**
   * Mapping to write a Visitors out as a JSON value.
   */
  implicit val implicitWrites = new Writes[Visitors] {
    def writes(visitors: Visitors): JsValue = {
      Json.obj(
        "ID_goscia" -> visitors.ID_goscia,
        "ID_pokoju" -> visitors.ID_pokoju,
        "Data_zam" -> visitors.Data_zam,
        "Data_zwol" -> visitors.Data_zwol,
        "Imie" -> visitors.Imie,
        "Nazwisko" -> visitors.Nazwisko,
        "Numer_tel" -> visitors.Numer_tel
      )
    }
  }
}

class VisitorsId private (val underlying: Int) extends AnyVal {
  override def toString: String = underlying.toString
}

object VisitorsId {
  def apply(raw: String): VisitorsId = {
    require(raw != null)
    new VisitorsId(Integer.parseInt(raw))
  }
}