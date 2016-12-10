package io.moo.robot

import javafx.application.{Application}
import javafx.event.EventHandler
import javafx.scene.Scene
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
  /* Actor system of the game. */
  val system = ActorSystem("World")

  override def start(primaryStage: Stage): Unit = {

    /* Terminate the actor system so the application has no more active threads anymore. */
    primaryStage.setOnCloseRequest( new EventHandler[WindowEvent]() {
      override def handle(event: WindowEvent) = {
        system.terminate()
      }
    })

    primaryStage.setTitle(WorldConfiguration.windowTitle)

    val world = new World(system)
    val robot1 = system.actorOf(Props(new Robot(world)), "Mr.Robot")
    world.add(robot1)

    val scene = new Scene(world, WorldConfiguration.width, WorldConfiguration.height)
    scene.getStylesheets.add("./robots.css")

    primaryStage.setScene(scene)
    primaryStage.show()
  }
}

object WorldConfiguration {
  val width = 640
  val height = 480
  val windowTitle = "Mr. Robot"
}