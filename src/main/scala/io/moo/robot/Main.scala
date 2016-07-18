package io.moo.robot

import java.awt.Point
import javafx.application.Application
import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.scene.image.{Image, ImageView}
import javafx.scene.input.MouseEvent
import javafx.scene.layout.{GridPane, HBox, Pane, StackPane}
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

    // pane.getStyleClass.add("grid")

    val world = new World
    val robot1 = new Robot(world)
    world.add(robot1)
    world.render()

    val scene = new Scene(world, 640, 480)
    scene.getStylesheets.add("./robots.css")

    primaryStage.setScene(scene)
    primaryStage.show()
  }
}
