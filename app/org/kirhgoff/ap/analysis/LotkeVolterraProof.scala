package org.kirhgoff.ap.analysis

/**
 * Created by Kirill Lastovirya (kirill.lastovirya@gmail.com) aka kirhgoff on 31/05/15.
 */

import slick.driver.H2Driver.api._

object LotkeVolterraProof {
  def main(a:Array[String]) {
    val db = Database.forConfig("h2mem1")
    try {

      class GameStepResults(tag: Tag) extends Table[(Int, Int, Int)](tag, "LOTKE_VOLTERRA") {
        def id = column[Int]("STEP_ID", O.PrimaryKey) // This is the primary key column
        def hunters = column[Int]("HUNTERS")
        def preys = column[Int]("PREYS")
        // Every table needs a * projection with the same type as the table's type parameter
        def * = (id, hunters, preys)
      }
      val results = TableQuery[GameStepResults]
      val setup = DBIO.seq(
        results.schema.create,
        results +=(101, 1, 10),
        results +=(49, 2, 8),
        results +=(150, 3, 5)
      )

      val setupFuture = db.run(setup)
      println("Results:")
//      db.run(results.result).map(_.foreach {
//        case (name, supID, price, sales, total) =>
//          println("  " + name + "\t" + supID + "\t" + price + "\t" + sales + "\t" + total)
//      })
// Why not let the database do the string conversion and concatenation?
    val q1 = for(r <- results)
      yield LiteralColumn("  ") ++ r.id.asColumnOf[Int] ++
    "\t" ++ r.hunters.asColumnOf[Int] ++
    "\t" ++ r.price.asColumnOf[Int]
      db.stream(q1.result).foreach(println)  }
}



