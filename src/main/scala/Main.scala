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
      val simulatorName = "Simulator"
      val viewName = "View"
      val simulatorRef = context.spawn(Simulator(simulatorName), simulatorName)
      val viewRef = context.spawn(View(viewName), viewName)
      context.watch(simulatorRef)
      viewRef ! SimulatorRef(simulatorRef)
      //      simulatorRef ! SimulatorRef(simulatorRef)
      //      simulatorRef ! ViewRef(viewRef)
      Thread.sleep(1000)
      logInfo("Sending start message")
      Thread.sleep(1000)
      viewRef ! ViewMessages.Start(10,5)
      //      simulatorRef ! Start(10, 2)
      //      Thread.sleep(3000)
      //      logInfo("Re-Sending start message")
      //      Thread.sleep(1000)
      //      simulatorRef ! Start(10, 10)
      //      Thread.sleep(3000)
      //      logInfo("Sending bodycreated message")
      //      Thread.sleep(1000)
      //      simulatorRef ! BodyCreated2
      //      Thread.sleep(3000)
      //      context.log.info("After sleep, send message")
      //      simulatorRef ! Simulator.Start(300, 300) //(numero corpi, iterazioni)
      Behaviors.receiveSignal {
        case (_, Terminated(_)) => Behaviors.stopped
      }
    }

object Application extends App:
  println("Hello world!")
  ActorSystem(Main(), "Simulation")
//  SimpleView.display()
