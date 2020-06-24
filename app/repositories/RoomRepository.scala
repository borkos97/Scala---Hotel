package repositories

import javax.inject.Inject
import scala.concurrent.ExecutionContext
import anorm._
import anorm.SqlParser._
import play.api.db._
import models.Room


@javax.inject.Singleton
class RoomRepository @Inject()(database: Database)(implicit ec: ExecutionContext) {

  private val room = {
      get[Option[Long]]("id_pokoju") ~
      get[String]("nazwa_pokoju") ~
      get[Int]("cena") ~
      get[Int]("il_os") ~
      get[String]("miasto") ~
      get[Int]("lazienka") ~
      get[String]("opis") map {
      case id_pokoju ~ nazwa_pokoju ~ cena ~ il_os ~ miasto ~ lazienka ~ opis
      => Room(id_pokoju, nazwa_pokoju, cena, il_os, miasto, lazienka == 1, opis)
    }
  }

  private val DB = database

  def getById(ID_pokoju: Long): Option[Room] = {
    DB.withConnection { implicit c =>
      SQL("select * from room where ID_pokoju = {ID_pokoju}").on('ID_pokoju -> ID_pokoju).as(room.singleOpt)
    }
  }


  def all(): List[Room] = DB.withConnection { implicit c =>
    SQL("select * from room").as(room *)
  }

  def create(room: Room) {
    DB.withConnection { implicit c =>
      SQL("""
        insert into room (ID_pokoju, nazwa_pokoju, cena, Il_os, miasto, lazienka, opis)
        values ({ID_pokoju}, {nazwa_pokoju}, {cena}, {Il_os}, {miasto}, {lazienka}, {opis}
        )
        """).bind(room).executeInsert()
    }
  }

  def update(ID_pokoju: Long, room: Room) {
    DB.withConnection { implicit c =>
      SQL("""
        UPDATE room SET ID_pokoju={ID_pokoju}, nazwa_pokoju={nazwa_pokoju}, cena={cena}, Il_os={Il_os},
        miasto={miasto}, lazienka={lazienka}, opis={opis}
        WHERE ID_pokoju = {ID_pokoju}
        """).bind(room.copy(ID_pokoju = Some(ID_pokoju))).executeUpdate()
    }
  }

  def delete(ID_pokoju: Long) {
    DB.withConnection { implicit c =>
      SQL("""
        delete from room where ID_pokoju = {ID_pokoju}
        """).on('ID_pokoju -> ID_pokoju).executeUpdate()
    }
  }

}