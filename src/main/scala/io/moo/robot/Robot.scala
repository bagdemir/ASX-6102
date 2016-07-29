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

  private var position = new Point(10, 10)

  private val dimensions = new Dimensions(48, 48)

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

trait MovingObject extends WorldObject {
  def move(toPoint: Point, velocity: Int)

}

/**
  * Robot is the first prototype.
  */
class Robot(world: World) extends MovingObject {
  val pic = new ImageView(new Image(getClass().getResourceAsStream("/robot.gif")))
  pic.setFitWidth(getDimensions.w)
  pic.setFitHeight(getDimensions.h)
  pic.setX(getXY().x)
  pic.setY(getXY().y)
  pic.getStyleClass.add("grid")
  pic.setFocusTraversable(true)


  override def getView(): Node = pic

  override def move(to: Point, velocity: Int): Unit = {
    val toPoint = new Point((to.x - getDimensions.w / 2.0d).toInt, (to.y - getDimensions.h / 2.0d).toInt)
    val movementLength = 1
    val timer = new Timer()
    val xSign = (toPoint.x - getPosition.x) / Math.abs(toPoint.x - getPosition.x)
    val ySign = (toPoint.y - getPosition.y) / Math.abs(toPoint.y - getPosition.y)
    val step = Math.abs(toPoint.x - getPosition.x).toDouble / Math.abs(toPoint.y - getPosition.y).toDouble

    var totalMove = 0
    var totalStep = 1

    val task = new TimerTask {
      var counter = 0

      def run() = {
        counter = counter + 1
        totalMove = totalMove + 1


        if (step < 1) {
          pic.setY(pic.getY + movementLength * ySign)
          if (totalMove > (totalStep * (1d / step))) {
            pic.setX(pic.getX + movementLength * xSign)
            counter = 0
            totalStep = totalStep + 1
          }
        } else {
          pic.setX(pic.getX + movementLength * xSign)

          println(s"totalMove: $totalMove limit: ${totalStep * step}")
          if (totalMove > (totalStep * step)) {
            println("moving down")
            pic.setY(pic.getY + movementLength * ySign)
            counter = 0
            totalStep = totalStep + 1
          }
        }

        if (pic.getX > (toPoint.x - 2) && pic.getX < (toPoint.x + 2) ||
          pic.getY > (toPoint.y - 2) && pic.getY < (toPoint.y + 2)) {
          timer.cancel()
          setPosition(toPoint)
          pic.setX(toPoint.x)
          pic.setY(toPoint.y)
        }
      }
    }
    timer.schedule(task, 0L, 10L)
  }
}
