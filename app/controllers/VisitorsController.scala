package controllers

import anorm.Column
import javax.inject._
import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api.i18n.I18nSupport
import models.Visitors
import repositories.VisitorsRepository
/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's Visitors page.
 */

@Singleton
class VisitorsController @Inject()(visitorsService: VisitorsRepository, val cc: ControllerComponents) extends AbstractController(cc) with I18nSupport {

  val Home = Redirect(routes.VisitorsController.index)

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
    Ok(views.html.index2(visitorsService.all(), visitorsForm))
  }

  def create = Action { implicit request =>
    visitorsForm.bindFromRequest.fold(
      errors => BadRequest(views.html.index2(visitorsService.all(), errors)),
      visitors => {
        visitorsService.create(visitors)
        Home.flashing("success" -> "Visitors %s has been created".format(visitors.ID_pokoju))
      }
    )
  }

  def edit(ID_goscia: Long) = Action { implicit request =>
    val avisitors = visitorsService.getById(ID_goscia)
    val visitors = avisitors.get
    Ok(views.html.edit2(visitors.ID_goscia.get, visitorsForm.fill(visitors)))
  }

  def update(ID_goscia: Long) = Action { implicit request =>
    visitorsForm.bindFromRequest.fold(
      formWithErrors => {
        BadRequest(views.html.edit2(ID_goscia, formWithErrors))
      },
      visitors => {
        visitorsService.update(ID_goscia, visitors)
        Redirect(routes.VisitorsController.index)co
      }
    )
  }

  def delete(ID_goscia: Long) = Action { implicit request: Request[AnyContent] =>
    visitorsService.delete(ID_goscia)
    Redirect(routes.VisitorsController.index)
  }
}