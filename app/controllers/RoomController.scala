package controllers

import anorm.Column
import javax.inject._
import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api.i18n.I18nSupport
import models.Room
import repositories.RoomRepository
/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's Room page.
 */

@Singleton
class RoomController @Inject()(roomService: RoomRepository, val cc: ControllerComponents) extends AbstractController(cc) with I18nSupport {

  val Home = Redirect(routes.RoomController.index)

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
    Ok(views.html.index(roomService.all(), roomForm))
  }

  def create = Action { implicit request =>
    roomForm.bindFromRequest.fold(
      errors => BadRequest(views.html.index(roomService.all(), errors)),
      room => {
        roomService.create(room)
        Home.flashing("success" -> "Room %s has been created".format(room.nazwa_pokoju))
      }
    )
  }

  def edit(ID_pokoju: Long) = Action { implicit request =>
    val aroom = roomService.getById(ID_pokoju)
    val room = aroom.get
    Ok(views.html.edit(room.ID_pokoju.get, roomForm.fill(room)))
  }

  def update(ID_pokoju: Long) = Action { implicit request =>
    roomForm.bindFromRequest.fold(
      formWithErrors => {
        BadRequest(views.html.edit(ID_pokoju, formWithErrors))
      },
      room => {
        roomService.update(ID_pokoju, room)
        Redirect(routes.RoomController.index)
      }
    )
  }

  def delete(ID_pokoju: Long) = Action { implicit request: Request[AnyContent] =>
    roomService.delete(ID_pokoju)
    Redirect(routes.RoomController.index)
  }
}