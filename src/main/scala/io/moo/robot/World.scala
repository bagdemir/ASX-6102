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
class World(system: ActorSystem) extends Pane {

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

  /** Map of the world. */
  val map = Map(terrain, this)

  map.loadTerrain()

  /** Forwards the mouse event to the stash. */
  setOnMouseClicked(new EventHandler[MouseEvent]() {
    override def handle(event: MouseEvent) = objectStore ! OnClick(event)
  })

  /** Adds a new item to the stash. */
  def add(ref: ActorRef) = objectStore ! Add(ref)

  /** Adds a new static item to the stash. */
  def addStaticObject(obj: StaticObject) = objectStore ! Add(system.actorOf(Props(obj)))

  /** Adds a new static item to the stash. */
  def addWall(position: Point) = {
    objectStore ! Add(system.actorOf(Props(new Wall(this, position))))
  }

  /** Adds a new static item to the stash. */
  def addMovingObject(obj: MovingObject) = objectStore ! Add(system.actorOf(Props(obj)))

  def world = objectStore

  /**
    * Stash to keep world objects.
    */
  case class ObjectStore() extends Actor {
    var refs : List[ActorRef] = List()

    override def receive: Receive = {
      case Add(ref) =>
        refs ::= ref
        ref ! GetView
      case GetViewResponse(node: Node) => Platform.runLater(new Runnable() {
        override def run(): Unit = getChildren.add(node)
      })
      case OnClick(event: MouseEvent) =>  refs.foreach { ref =>
        ref ! Move(new Point(event.getX.toInt, event.getY.toInt), 0)
      }
    }
  }
}

case class Dimensions(w: Int, h: Int)

trait WorldAction
case class Add(ref: ActorRef) extends WorldAction
case class OnClick(event: MouseEvent) extends WorldAction

trait Action
case class GetView()
case class GetViewResponse(node: Node)
case class Move(to: Point, velocity: Int)
