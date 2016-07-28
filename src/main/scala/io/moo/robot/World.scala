package io.moo.robot

import java.awt.Point
import javafx.event.EventHandler
import javafx.scene.input.MouseEvent
import javafx.scene.layout.Pane

/**
  * This class does nothing unless you provide some docs.
  *
  * @author void
  */
class World extends Pane {
  var objects : List[WorldObject] = List()

  setOnMouseClicked(new EventHandler[MouseEvent]() {
    override def handle(event: MouseEvent) =  objects.foreach {
      case mo: MovingObject => mo.move(new Point(event.getX.toInt, event.getY.toInt), 0)
      case _ =>
    }
  })

  def render() =  objects.foreach(obj => getChildren.add(obj.getView))
  def add(worldObject: WorldObject) = {
    objects = worldObject :: objects
  }


}

class WorldUnit
class Move
case class Dimensions(w: Int, h: Int)