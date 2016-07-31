package io.moo.robot

import java.awt.Point
import javafx.application.{Application, Platform}
import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.scene.image.{Image, ImageView}
import javafx.scene.input.MouseEvent
import javafx.scene.layout.{GridPane, HBox, Pane, StackPane}
import javafx.stage.{Stage, WindowEvent}

import akka.actor.{ActorSystem, Props}


/**
  * Game application.
  *
  * @author Erhan Bagdemir
  */
object GameApp {
  def main(args: Array[String]): Unit = {
    Application.launch(classOf[GameApp], args: _*)
 }
}

class GameApp extends Application {
  val system = ActorSystem("World")

  override def start(primaryStage: Stage): Unit = {

    Platform.setImplicitExit(false)

    primaryStage.setOnCloseRequest( new EventHandler[WindowEvent]() {
      override def handle(event: WindowEvent) = {
        system.terminate()
      }
    })

    primaryStage.setTitle("Robots")

    val world = new World(system)
    val robot1 = system.actorOf(Props(new Robot(world)), "Mr.Robot")
    world.add(robot1)

    val scene = new Scene(world, 640, 480)
    scene.getStylesheets.add("./robots.css")

    primaryStage.setScene(scene)
    primaryStage.show()
  }
}
