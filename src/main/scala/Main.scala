import akka.NotUsed
import akka.actor.typed.{ActorSystem, Behavior, Terminated}
import akka.actor.typed.scaladsl.Behaviors

import actors.Simulator

object Main:
  def apply(): Behavior[NotUsed] =
    Behaviors.setup { context =>
      val simulatorRef = context.spawn(Simulator(), "Simulator")
      context.watch(simulatorRef)
      simulatorRef ! Simulator.Start(100, 1000) //(numero corpi, iterazioni)

      Behaviors.receiveSignal {
        case (_, Terminated(_)) => Behaviors.stopped
      }
    }

@main
def main(): Unit =
  println("Hello world!")
  ActorSystem(Main(), "Simulation")