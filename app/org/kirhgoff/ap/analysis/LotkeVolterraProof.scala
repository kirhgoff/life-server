package org.kirhgoff.ap.analysis

/**
 * Created by Kirill Lastovirya (kirill.lastovirya@gmail.com) aka kirhgoff on 31/05/15.
 */

import slick.driver.H2Driver.api._
import scala.concurrent.ExecutionContext.Implicits.global

object LotkeVolterraProof {
  def main(a:Array[String]) {
    val db = Database.forConfig("h2mem1")
    try {

      class GameStepResults(tag: Tag) extends Table[(String, Int, Int)](tag, "LOTKE_VOLTERRA") {
        def id = column[String]("STEP_ID", O.PrimaryKey) // This is the primary key column
        def hunters = column[Int]("HUNTERS")
        def preys = column[Int]("PREYS")
        // Every table needs a * projection with the same type as the table's type parameter
        def * = (id, hunters, preys)
      }
      val results = TableQuery[GameStepResults]

      val setup = DBIO.seq(
        results.schema.create,

        results +=("1", 1, 10),
        results +=("2", 2, 8),
        results +=("3", 3, 5)
      )

      val setupFuture = db.run(setup)
//      println("Results:")
//      db.run(results.result).map(_.foreach {
//        case (name, supID, price, sales, total) =>
//          println("  " + name + "\t" + supID + "\t" + price + "\t" + sales + "\t" + total)
//      })

      // val q1 = for(r <- results)
      //   yield LiteralColumn("  ") ++ r.id.asColumnOf[String] ++
      // "\t" ++ r.hunters.asColumnOf[Int] ++
      // "\t" ++ r.price.asColumnOf[Int]
      //   db.stream(q1.result).foreach(println)  
    } finally db.close
  }
}



