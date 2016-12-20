package io.moo.robot

import java.awt.Point
import javafx.application.Platform
import javafx.event.EventHandler
import javafx.scene.Node
import javafx.scene.input.MouseEvent
import javafx.scene.layout.Pane

import akka.actor.{Actor, ActorRef, ActorSystem, Props}

/**
  * The world of the game domain maintains the game artifacts.
  *
  * @author Erhan Bagdemir
  */
class World(system: ActorSystem, p1Controller: ActorRef) extends Pane {

  // TODO Load maps from flat files.
  val terrain =
  """oooxooo
    | ooox
    | oxxxxxxxxxxxxxoo
    | oooxooooox
    | oooxooooox
    | oooxxxxooo
    | o""".stripMargin

  /** objectStore actor keeps accounts of world objects. */
  val objectStore = system.actorOf(Props(new ObjectStore))

  /** graph representation of the scene. */
  val terrainMesh = new TerrainGraph(WorldConfiguration.width, WorldConfiguration.height)

  /** Map of the world. */
  val map = Map(terrain, this)

  map.loadTerrain()

  /** Forwards the mouse event to the stash. */
  setOnMouseClicked(new EventHandler[MouseEvent]() {
    override def handle(event: MouseEvent) = p1Controller ! Move(new Point(event.getX.toInt, event.getY.toInt), 0)
  })

  def getVertexBy(x: Int, y: Int) = terrainMesh.getVertexBy(x, y)
  def getVertexPositionBy(x: Int, y: Int) = terrainMesh.getVertexBy(x, y).map(v => (v.x, v.y)).getOrElse((-1, -1))
  def isOccupied(x: Int, y: Int) = {}

  /** Adds a new item to the stash. */
  def add(wObj: WorldObject) = objectStore ! Add(wObj)

  /** Adds a new static item to the stash. */
  def addStaticObject(obj: StaticObject) = objectStore ! Add(obj)

  /** Adds a new static item to the stash. */
  def addWall(position: Point) = {
    objectStore ! Add(new Wall(position))
  }

  /** Adds a new static item to the stash. */
  def addMovingObject(obj: MovingObject) = objectStore ! Add(obj)

  def world = objectStore

  /**
    * Stash to keep world objects.
    */
  case class ObjectStore() extends Actor {

    var refs: List[WorldObject] = List()

    override def receive: Receive = {

      case Add(wObj) =>
        refs ::= wObj
        Platform.runLater(new Runnable() {
          override def run(): Unit = getChildren.add(wObj.getView)
        })

      case OnClick(event: MouseEvent) => p1Controller ! Move(new Point(event.getX.toInt, event.getY.toInt), 0)
    }
  }

}

case class Dimensions(w: Int, h: Int)

trait WorldAction

case class IsOccupied(x:Int, y:Int) extends WorldAction
case class Add(obj: WorldObject) extends WorldAction
case class OnClick(event: MouseEvent) extends WorldAction

trait Action

case class GetView()

case class GetViewResponse(node: Node)

case class Move(to: Point, velocity: Int)


case class Vertex(x: Int, y: Int) {
  val xIndex = (x - 16) / 32
  val yIndex = (y - 16) / 32
}

class TerrainGraph(width: Int, height: Int) {

  /** vertex graph */
  val vertices = for (x <- 16 until width by 32; y <- 16 until height by 32) yield Vertex(x, y)

  /** Get vertices by their indices. */
  def getVertexBy(x: Int, y: Int) : Option[Vertex] = vertices.find((v) => v.xIndex == x && v.yIndex == y)

}