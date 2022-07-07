package actors

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.scaladsl.LoggerOps
import akka.actor.typed.{ActorRef, ActorSystem, Behavior}
import model.{Body, Boundary, P2d, V2d}

import java.security.URIParameter
import java.util
import java.util.Random
import scala.collection.mutable.ListBuffer

object Simulator:

  sealed trait SimulatorMessages
  final case class Start(n: Int, it: Int) extends SimulatorMessages
  final case class Update(body: Body) extends SimulatorMessages

  val bodyList: List[Body] = List()
  val mass = 10
  val dt = 0.01
  val boundary: Boundary = Boundary(-6, -6, 6, 6)

  val bodyActors: ListBuffer[ActorRef[Tick]] = ListBuffer()

  def apply(): Behavior[SimulatorMessages] =
    Behaviors.receive((context, message) => message match
      case Start(n, it) => StartSimulation(n, it)
      case _ => Behaviors.stopped
    )

  private def StartSimulation(n: Int, it: Int): Behavior[SimulatorMessages] =
    Behaviors.setup(ctx => {
      val rand = new Random(System.currentTimeMillis)
      for (i <- 0 to n) {
        val x = boundary.getX0 * 0.25 + rand.nextDouble * (boundary.getX1 - boundary.getX0) * 0.25
        val y = boundary.getY0 * 0.25 + rand.nextDouble * (boundary.getY1 - boundary.getY0) * 0.25
        val pos = P2d(x, y)
        bodyActors += ctx.spawn(Behaviors.setup(BodyActor(_, i, mass, pos, dt, boundary)), "BodyActor" + i)
      }
      
      //bodyActors.toList.foreach(a => a ! Tick)

      Behaviors.receiveMessage(msg => msg match
        case Update(body) => ??? // prende la nuova posizione e comunica alla view
        case _ => Behaviors.same //Ignore other messages
      )
    })
