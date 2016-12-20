package io.moo.robot

import javafx.application.Application
import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.stage.{Stage, WindowEvent}

import akka.actor.Actor.Receive
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
    primaryStage.setOnCloseRequest(new EventHandler[WindowEvent]() {
      override def handle(event: WindowEvent) = {
        system.terminate()
      }
    })

    primaryStage.setTitle(WorldConfiguration.windowTitle)

    val robot = new Robot
    val p1Controller = system.actorOf(Props(new UserController(robot)))
    val world = new World(system, p1Controller)
    world.add(robot)

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