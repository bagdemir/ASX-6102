package io.moo.robot

import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.image.{Image, ImageView}
import javafx.scene.layout.GridPane
import javafx.stage.Stage

/**
  * @author bagdemir
  */
object GameApp {
  def main(args: Array[String]): Unit = {
    Application.launch(classOf[GameApp], args: _*)
  }
}

class GameApp extends Application {
  override def start(primaryStage: Stage): Unit = {

    primaryStage.setTitle("Robots")

    val grid = new GridPane
    grid.getStyleClass.add("grid")

    val world = new World(grid)
    val robot = new Robot(world)
    world.add(robot)
    world.render()

    val scene = new Scene(grid, 640, 480)
    scene.getStylesheets.add("./robots.css")

    primaryStage.setScene(scene)
    primaryStage.show()
  }
}
