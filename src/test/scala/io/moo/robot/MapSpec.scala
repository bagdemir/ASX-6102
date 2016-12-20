package io.moo.robot

import akka.actor.{ActorSystem, Props}
import org.scalatest.{FlatSpec, Matchers}
/**
  * Created by bagdemir on 08/12/2016.
  */
class MapSpec extends FlatSpec with Matchers {
  val system = ActorSystem("World")
  val world = new World(system, system.actorOf(Props(new UserController(new Robot))))

  val terrain =
    """oooxooo
      | ooox
      | oxxxxx
      | o
      | o""".stripMargin

  "The size of the Map" should "be calculated correctly" in {
      val map = Map(terrain, world)
      map.loadTerrain()
      assert(map.calculateSize === (7, 4))
  }
}
