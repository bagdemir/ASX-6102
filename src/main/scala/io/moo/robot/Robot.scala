package io.moo.robot

import java.awt.Point
import java.util.{Timer, TimerTask}
import javafx.scene.Node
import javafx.scene.image.{Image, ImageView}

import akka.actor.{Actor}

/**
  * This class does nothing unless you provide some docs.
  *
  * @author Erhan Bagdemir
  */
trait WorldObject {

  private var position = new Point(30, 30)

  private val dimensions = new Dimensions(48, 48)


  /**
    * Updates the graphics.
    */
  def update()

  /**
    * @return The view representation.
    */
  def getView: Node

  /**
    * @return The position of the object.
    */
  def getPosition: Point = position

  /**
    * @return The top-left corner.
    */
  def getXY(): Point = new Point(position.x - (dimensions.w / 2.0d).toInt, position.y - (dimensions.h / 2.0d).toInt)

  /**
    * @return Sets the position of the object according to its center point.
    */
  def setPosition(newPos: Point): Unit = {
    position = newPos
  }

  /**
    * @return The dimensions.
    */
  def getDimensions = dimensions
}

/**
  * Objects are able to move or be moved in the world, should implement this trait.
  * Every time the object moves, it calls update method to inform the graphic objects about the move.
  *
  * @author Erhan Bagdemir
  */
trait MovingObject extends WorldObject with Actor {

  var moving = false

  def verticalMovement(toPoint: Point): Int = if (toPoint.y != getPosition.y) {
    (toPoint.y - getPosition.y) / Math.abs(toPoint.y - getPosition.y)
  } else 1

  def horizontalMovement(toPoint: Point): Int = if (toPoint.x != getPosition.x) {
    (toPoint.x - getPosition.x) / Math.abs(toPoint.x - getPosition.x)
  } else 1

  def delta(toPoint: Point) =
    if (toPoint.y == getPosition.y) Math.abs(toPoint.x - getPosition.x).toDouble
    else if (toPoint.x == getPosition.x) Math.abs(toPoint.y - getPosition.y).toDouble
    else Math.abs(toPoint.x - getPosition.x).toDouble / Math.abs(toPoint.y - getPosition.y).toDouble

  def move(to: Point, velocity: Int): Unit = {

    if (moving) return

    moving = true
    val movementLength = 1
    val timer = new Timer()
    val xSign = horizontalMovement(to)
    val ySign = verticalMovement(to)
    val step = delta(to)
    var totalMove = 0
    var totalStep = 1

    val task = new TimerTask {
      def run() = {
        totalMove += 1
        if (step < 1) {
          setPosition(new Point(getPosition.x, getPosition.y + movementLength * ySign))
          if (step > 0 && totalMove > (totalStep * (1d / step))) {
            setPosition(new Point(getPosition.x + movementLength * xSign, getPosition.y))
            totalStep += 1
          }
        } else {
         setPosition(new Point(getPosition.x + movementLength * xSign, getPosition.y))
          if (totalMove > (totalStep * step)) {
            setPosition(new Point(getPosition.x, getPosition.y + movementLength * ySign))
            totalStep += 1
          }
        }
        // snap
        if (Math.abs(getPosition.x - to.x) < 3 && Math.abs(getPosition.y - to.y) < 3) {
          timer.cancel()
          setPosition(to) // correction
          moving = false
        }
        update()
      }
    }
    timer.schedule(task, 0L, 10L)
  }
}

/**
  * Robot is the first prototype.
  */
class Robot(world: World) extends MovingObject {
  val graphics = new ImageView(new Image(getClass().getResourceAsStream("/robot.gif")))
  graphics.getStyleClass.add("grid")
  graphics.setFocusTraversable(true)

  update()

  override def getView(): Node = graphics

  override def update(): Unit = {
    graphics.setFitWidth(getDimensions.w)
    graphics.setFitHeight(getDimensions.h)
    graphics.setX(getXY().x)
    graphics.setY(getXY().y)
  }

  override def receive: Receive = {
    case GetView => world.stash ! GetViewResponse(getView)
    case Move(to, velocity) => move(to, velocity)
    case _ => println("I donno what you're saying!")
  }
}
