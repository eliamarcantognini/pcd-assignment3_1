import akka.NotUsed
import akka.actor.typed.{ActorSystem, Behavior, Terminated}
import akka.actor.typed.scaladsl.Behaviors
import actors.{Simulator, View}

object Main:
  import Simulator.*
  import View.*

  def apply(): Behavior[NotUsed] =
    Behaviors.setup { context =>
      val logInfo: String => Unit = context.log.info
      logInfo("Main started")
//      val withGui = true
      val withGui = false
      val simulatorName = "Simulator"
      val viewName = "View"
      val nBodies = 100
      val iterations = 50000
      val simulatorRef = context.spawn(Simulator(nBodies, iterations, simulatorName), simulatorName)
      val viewRef = context.spawn(View(viewName, withGui), viewName)
      context.watch(simulatorRef)
      viewRef ! SimulatorRef(simulatorRef)
      Behaviors.receiveSignal {
        case (_, Terminated(_)) => Behaviors.stopped
      }
    }

object Application extends App:
  println("Hello world!")
  ActorSystem(Main(), "Simulation")
