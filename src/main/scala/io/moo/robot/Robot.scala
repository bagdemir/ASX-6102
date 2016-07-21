package io.moo.robot

import java.awt.Point
import java.util.{Timer, TimerTask}
import javafx.scene.Node
import javafx.scene.image.{Image, ImageView}

/**
  * This class does nothing unless you provide some docs.
  *
  * @author void
  */
trait WorldObject {
  var pos = new Point(10, 10)

  def getView(): Node

  def getPos(): Point = pos
}

trait MovingObject extends WorldObject {
  def move(toPoint: Point, velocity: Int)

}

/**
  * Robot is the first prototype.
  */
class Robot(world: World) extends MovingObject {
  val pic = new ImageView(new Image(getClass().getResourceAsStream("/robot.gif")))
  pic.setFitWidth(48.0d)
  pic.setFitHeight(48.0d)
  pic.setX(getPos().x)
  pic.setY(getPos().y)
  pic.getStyleClass.add("grid")
  pic.setFocusTraversable(true)

  override def getView(): Node = pic

  override def move(toPoint: Point, velocity: Int): Unit = {
    val horizontalMovement = Math.abs(toPoint.x - getPos().x) >= Math.abs(toPoint.x - getPos().x)
    val timer = new Timer()
    val task = new TimerTask {
      var counter = 0

      def run() = {
        val horizontalDirection = (toPoint.x - getPos().x) / Math.abs(toPoint.x - getPos().x)
        val verticalDirection = (toPoint.y - getPos().y) / Math.abs(toPoint.y - getPos().y)
        val movementLength = 1

        if (horizontalMovement) {
          val step = Math.abs(toPoint.x - getPos().x).toDouble / Math.abs(toPoint.y - getPos().y).toDouble
          val next = pic.getX + (movementLength * horizontalDirection)

          counter = counter + 1

          pic.setX(next)
          if (counter > step) {
            pic.setY(pic.getY + (movementLength * verticalDirection))
            counter = 0
          }
          if (pic.getX > (toPoint.x - 5) && pic.getX < (toPoint.x + 5) ||
            pic.getY > (toPoint.y - 5) && pic.getY < (toPoint.y + 5)) {
            timer.cancel()
            pos = new Point(pic.getX.toInt, pic.getY.toInt)
          }
        } else {
          val step = Math.abs(toPoint.y - getPos().y).toDouble / Math.abs(toPoint.x - getPos().x).toDouble
          val next = pic.getY + (movementLength * verticalDirection)

          counter = counter + 1

          pic.setX(next)
          if (counter > step) {
            pic.setY(pic.getX +  + (movementLength * horizontalDirection))
            counter = 0
          }

          if (pic.getX > (toPoint.x - 5) && pic.getX < (toPoint.x + 5) ||
            pic.getY > (toPoint.y - 5) && pic.getY < (toPoint.y + 5)) {
            timer.cancel()
            pos = new Point(pic.getX.toInt, pic.getY.toInt)
          }
        }
      }
    }
    timer.schedule(task, 0L, 10L)
  }
}
