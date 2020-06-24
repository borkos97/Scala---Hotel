package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api.i18n.I18nSupport
import models.Room
import repositories.RoomRepository
import play.api.libs.json.Json


/**
 * Takes HTTP requests and produces JSON.
 */
@Singleton
class APIRoomController @Inject()(roomService: RoomRepository, val cc: ControllerComponents) extends AbstractController(cc) with I18nSupport {

  private val logger = Logger(getClass)

  val roomForm = Form(
    mapping(
      "ID_pokoju" -> ignored(None: Option[Long]),
      "nazwa_pokoju" -> nonEmptyText,
      "cena" -> number,
      "Il_os" -> number,
      "miasto" -> nonEmptyText,
      "lazienka" -> boolean,
      "opis" -> nonEmptyText
    )(Room.apply)(Room.unapply)
  )

  def index = Action { implicit request: Request[AnyContent] =>
    logger.trace("index: ")
    val rooms = roomService.all
    Ok(Json.toJson(rooms))
  }

  def show(ID_pokoju: Long) = Action { implicit request: Request[AnyContent] =>
    logger.trace("index: ")
    val room = roomService.getById(ID_pokoju)
    Ok(Json.toJson(room.get))
  }

  def create = Action { implicit request =>
    roomForm.bindFromRequest.fold(
      errors => BadRequest(errors.errorsAsJson),
      room => {
        roomService.create(room)
        Ok(Json.toJson(room))
      }
    )
  }

  def delete(ID_pokoju: Long) = Action { implicit request: Request[AnyContent] =>
    roomService.delete(ID_pokoju)
    Ok("DELETED")
  }

}