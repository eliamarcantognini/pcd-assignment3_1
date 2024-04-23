import akka.NotUsed
import akka.actor.typed.{ActorSystem, Behavior, Terminated}
import akka.actor.typed.scaladsl.Behaviors

import actors.Simulator

import view.SimpleView

object Main:
  import Simulator.*

  def apply(): Behavior[NotUsed] =
    Behaviors.setup { context =>
      val logInfo: String => Unit = context.log.info
      logInfo("Main started")
      val simulatorName = "Simulator"
      val simulatorRef = context.spawn(Simulator(simulatorName), simulatorName)
      context.watch(simulatorRef)
//      simulatorRef ! SimulatorRef(simulatorRef)
      Thread.sleep(3000)
      logInfo("Sending start message")
      Thread.sleep(1000)
      simulatorRef ! Start(10, 2)
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
