package com.wesovi.ep.user.db.document

import scala.language.existentials
import org.specs2.mutable.Specification
import org.specs2.specification.Fragments
import org.specs2.specification.Step
import com.github.simplyscala.MongoEmbedDatabase
import com.github.simplyscala.MongodProps
import com.typesafe.config.ConfigFactory
import org.specs2.mutable.SpecificationWithJUnit

trait BaseDocumentSpec extends SpecificationWithJUnit  with MongoEmbedDatabase{

  private val config =  ConfigFactory.load()

  lazy val mongoPort:Int = config.getInt("mongodb.port")

  var mongoProcess: MongodProps = null

  val spec = this


  /**
   * Start embedded mongo before test execution.
   */
  def initializeMongoDatabase() = {
    mongoProcess = mongoStart(port = mongoPort)
  }

  def stopMongoDatabase()={
    println("mongo is gonna be stopped.")
    mongoStop(mongoProcess)
  }

  override def map(fragments: =>Fragments) =
    Step(beforeAll) ^ fragments ^ Step(afterAll)

  protected def beforeAll()
  protected def afterAll()

}

