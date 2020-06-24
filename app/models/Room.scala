package models

import anorm._
import play.api.libs.json._


case class Room(
                 ID_pokoju: Option[Long] = None,
                 nazwa_pokoju: String,
                 cena: Int,
                 Il_os: Int,
                 miasto: String,
                 lazienka: Boolean,
                 opis: String)

object Room {
  implicit def toParameters: ToParameterList[Room] = Macro.toParameters[Room]

  /**
   * Mapping to write a Room out as a JSON value.
   */
  implicit val implicitWrites = new Writes[Room] {
    def writes(room: Room): JsValue = {
      Json.obj(
        "ID_pokoju" -> room.ID_pokoju,
        "nazwa_pokoju" -> room.nazwa_pokoju,
        "cena" -> room.cena,
        "Il_os" -> room.Il_os,
        "miasto" -> room.miasto,
        "lazienka" -> room.lazienka,
        "opis" -> room.opis
      )
    }
  }
}

class RoomId private (val underlying: Int) extends AnyVal {
  override def toString: String = underlying.toString
}

object RoomId {
  def apply(raw: String): RoomId = {
    require(raw != null)
    new RoomId(Integer.parseInt(raw))
  }
}