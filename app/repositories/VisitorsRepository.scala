package repositories

import javax.inject.Inject
import scala.concurrent.ExecutionContext
import anorm._
import anorm.SqlParser._
import play.api.db._
import models.Visitors


@javax.inject.Singleton
class VisitorsRepository @Inject()(database: Database)(implicit ec: ExecutionContext) {

  private val visitors = {
    get[Option[Long]]("id_goscia") ~
      get[Int]("id_pokoju") ~
      get[String]("data_zam") ~
      get[String]("data_zwol") ~
      get[String]("imie") ~
      get[String]("nazwisko") ~
      get[String]("numer_tel") map {
      case id_goscia ~ id_pokoju ~ data_zam ~ data_zwol ~ imie ~ nazwisko ~ numer_tel
      => Visitors(id_goscia, id_pokoju, data_zam, data_zwol, imie, nazwisko, numer_tel)
    }
  }

  private val DB = database

  def getById(ID_goscia: Long): Option[Visitors] = {
    DB.withConnection { implicit c =>
      SQL("select * from visitors where ID_goscia = {ID_goscia}").on('ID_goscia -> ID_goscia).as(visitors.singleOpt)
    }
  }


  def all(): List[Visitors] = DB.withConnection { implicit c =>
    SQL("select * from visitors").as(visitors *)
  }

  def create(visitors: Visitors) {
    DB.withConnection { implicit c =>
      SQL("""
        insert into visitors (ID_goscia, ID_pokoju, Data_zam, Data_zwol, Imie, Nazwisko, Numer_tel)
        values ({ID_goscia}, {ID_pokoju}, {Data_zam}, {Data_zwol}, {Imie}, {Nazwisko}, {Numer_tel}
        )
        """).bind(visitors).executeInsert()
    }
  }

  def update(ID_goscia: Long, visitors: Visitors) {
    DB.withConnection { implicit c =>
      SQL("""
        UPDATE visitors SET ID_goscia={ID_goscia}, ID_pokoju={ID_pokoju}, Data_zam={Data_zam}, Data_zwol={Data_zwol},
        Imie={Imie}, Nazwisko={Nazwisko}, Numer_tel={Numer_tel}
        WHERE ID_goscia = {ID_goscia}
        """).bind(visitors.copy(ID_goscia = Some(ID_goscia))).executeUpdate()
    }
  }

  def delete(ID_goscia: Long) {
    DB.withConnection { implicit c =>
      SQL("""
        delete from visitors where ID_goscia = {ID_goscia}
        """).on('ID_goscia -> ID_goscia).executeUpdate()
    }
  }

}