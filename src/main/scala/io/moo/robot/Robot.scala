package io.moo.robot

import javafx.scene.Node
import javafx.scene.image.{Image, ImageView}

/**
  * This class does nothing unless you provide some docs.
  *
  * @author void
  */
trait WorldObject {
  def getView() : Node
}

trait MovingObject extends WorldObject {
  def move(move: Move, amount: WorldUnit)

}

/**
  * Robot is the first prototype.
  */
class Robot(world: World) extends MovingObject {
  val pic = new ImageView(new Image(getClass().getResourceAsStream("/robot.gif")))
  pic.setScaleX(0.2d)
  pic.setScaleY(0.2d)

  override def move(move: Move, amount: WorldUnit): Unit = ???

  override def getView(): Node = pic
}
