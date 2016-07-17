package io.moo.robot

import javafx.scene.layout.Pane

/**
  * This class does nothing unless you provide some docs.
  *
  * @author void
  */
class World(pane: Pane) {
  var objects : List[WorldObject] = List()
  def render() =  objects.foreach(obj => pane.getChildren.add(obj.getView))
  def add(worldObject: WorldObject) = {
    objects = worldObject :: objects
  }
}

class WorldUnit

class Move