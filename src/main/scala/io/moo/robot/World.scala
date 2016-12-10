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
      | oxxxxx
      | o
      | o""".stripMargin

  /** Stash actor keeps accounts of world objects. */
  val stash = system.actorOf(Props(new Stash))

  /** Map of the world. */
  val map = Map(terrain, this)

  map.loadTerrain()

  /** Forwards the mouse event to the stash. */
  setOnMouseClicked(new EventHandler[MouseEvent]() {
    override def handle(event: MouseEvent) = stash ! OnClick(event)
  })

  /** Adds a new item to the stash. */
  def add(ref: ActorRef) = stash ! Add(ref)

  /** Adds a new static item to the stash. */
  def addStaticObject(obj: StaticObject) = stash ! Add(system.actorOf(Props(obj)))

  /** Adds a new static item to the stash. */
  def addWall(position: Point) = {
    stash ! Add(system.actorOf(Props(new Wall(this, position))))
  }

  /** Adds a new static item to the stash. */
  def addMovingObject(obj: MovingObject) = stash ! Add(system.actorOf(Props(obj)))

  def world = stash

  /**
    * Stash to keep world objects.
    */
  case class Stash() extends Actor {
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
