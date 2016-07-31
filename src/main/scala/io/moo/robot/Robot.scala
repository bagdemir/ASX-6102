package io.moo.robot

import java.awt.Point
import java.util.{Timer, TimerTask}
import javafx.scene.Node
import javafx.scene.image.{Image, ImageView}

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
trait MovingObject extends WorldObject {

  def verticalMovement(toPoint: Point): Int = if (toPoint.y != getPosition.y) {
    (toPoint.y - getPosition.y) / Math.abs(toPoint.y - getPosition.y)
  } else 1

  def horizontalMovement(toPoint: Point): Int = if (toPoint.x != getPosition.x) {
    (toPoint.x - getPosition.x) / Math.abs(toPoint.x - getPosition.x)
  } else 1

  def move(to: Point, velocity: Int): Unit = {
    val movementLength = 1
    val timer = new Timer()
    val xSign = horizontalMovement(to)
    val ySign = verticalMovement(to)
    val step = Math.abs(to.x - getPosition.x).toDouble / Math.abs(to.y - getPosition.y).toDouble

    var totalMove = 0
    var totalStep = 1

    val task = new TimerTask {
      def run() = {
        totalMove = totalMove + 1
        if (step < 1) {
          setPosition(new Point(getPosition.x, getPosition.y + movementLength * ySign))
          println(s"totalMove: ${totalMove} > next${totalStep * (1d / step)} whereas step = ${step}")
          if (totalMove > (totalStep * (1d / step))) {
            setPosition(new Point(getPosition.x + movementLength * xSign, getPosition.y))
            totalStep = totalStep + 1
          }
        } else {

          println(s"totalMove: ${totalMove} > ${totalStep * step} whereas step = ${step}")
          setPosition(new Point(getPosition.x + movementLength * xSign, getPosition.y))
          if (totalMove > (totalStep * step)) {
            setPosition(new Point(getPosition.x, getPosition.y + movementLength * ySign))
            totalStep = totalStep + 1
          }
        }
        // snap
        if (getPosition.x == to.x || getPosition.y == to.y) {
          timer.cancel()
          println(s"position: ${getPosition} target: ${to}")
          setPosition(to) // correction
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

}
