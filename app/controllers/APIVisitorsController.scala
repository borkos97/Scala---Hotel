package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api.i18n.I18nSupport
import models.Visitors
import repositories.VisitorsRepository
import play.api.libs.json.Json


/**
 * Takes HTTP requests and produces JSON.
 */
@Singleton
class APIVisitorsController @Inject()(visitorsService: VisitorsRepository, val cc: ControllerComponents) extends AbstractController(cc) with I18nSupport {

  private val logger = Logger(getClass)

  val visitorsForm = Form(
    mapping(
      "ID_goscia" -> ignored(None: Option[Long]),
      "ID_pokoju" -> number,
      "Data_zam" -> nonEmptyText,
      "Data_zwol" -> nonEmptyText,
      "Imie" -> nonEmptyText,
      "Nazwisko" -> nonEmptyText,
      "Numer_tel" -> nonEmptyText
    )(Visitors.apply)(Visitors.unapply)
  )

  def index = Action { implicit request: Request[AnyContent] =>
    logger.trace("index: ")
    val visitors = visitorsService.all
    Ok(Json.toJson(visitors))
  }

  def show(ID_pokoju: Long) = Action { implicit request: Request[AnyContent] =>
    logger.trace("index: ")
    val visitors = visitorsService.getById(ID_pokoju)
    Ok(Json.toJson(visitors.get))
  }

  def create = Action { implicit request =>
    visitorsForm.bindFromRequest.fold(
      errors => BadRequest(errors.errorsAsJson),
      visitors => {
        visitorsService.create(visitors)
        Ok(Json.toJson(visitors))
      }
    )
  }

  def delete(ID_pokoju: Long) = Action { implicit request: Request[AnyContent] =>
    visitorsService.delete(ID_pokoju)
    Ok("DELETED")
  }

}