package io.moo.robot

import java.awt.Point

import scala.util.matching.Regex

/**
  * Created by bagdemir on 08/12/2016.
  */
object Map {
  def apply(terrain: String, world: World) = new Map(terrain, world)
}

class Map(val terrain: String, val world: World) {


  def loadTerrain(): Unit = {
    val pattern = new Regex("x|X")
    terrainArray.indices.foreach(row => pattern.findAllIn(terrainArray(row).trim).matchData.foreach(x => draw(row,
      x.start)))
  }

  def draw(rowNo: Int, left: Int) = {
    val horizontalScaleRatio = (WorldConfiguration.width - (2 * 32)) / calculateSize._1
    world.addWall(new Point(32 + (32 * left), 32 + (rowNo * 32)))
  }

  def terrainArray = terrain.split('\n')

  def calculateSize: (Int, Int) = {
    val rows = terrain.split("\\n")
    (rows.max.length, rows.length)
  }
}
