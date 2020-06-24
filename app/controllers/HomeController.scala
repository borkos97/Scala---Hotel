package controllers

import javax.inject._
import play.api.mvc._
import play.api.i18n.I18nSupport
import repositories.RoomRepository

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(taskService: RoomRepository, val cc: ControllerComponents) extends AbstractController(cc) with I18nSupport {


  def index = Action { implicit request: Request[AnyContent] =>
    Redirect(routes.RoomController.index)
  }

}