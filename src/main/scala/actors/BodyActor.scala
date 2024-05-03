package actors

import actors.Simulator.{BodyCreated, SimulatorMessages, Updates}
import akka.actor.typed.scaladsl.{AbstractBehavior, ActorContext, Behaviors, LoggerOps}
import akka.actor.typed.{ActorRef, ActorSystem, Behavior}
import model.{Body, Boundary, P2d, V2d}


final case class Tick(bodyList: List[Body], replyTo: ActorRef[Updates])


class BodyActor(context: ActorContext[Tick], id: Int, mass: Double, pos: P2d, val dt: Double, val boundary: Boundary, parentRef: ActorRef[SimulatorMessages]) extends AbstractBehavior(context):

  val body: Body =
    val v = Body(id, pos, V2d(0, 0), mass)
    parentRef ! SimulatorMessages.BodyCreated(v)
    v


  override def onMessage(msg: Tick): Behavior[Tick] =

    computeNewPosition(msg.bodyList)

    msg.replyTo ! Updates(body)

    this


  private def computeNewPosition(bodyList: List[Body]): Unit =
    /* compute total force on bodies */
    val totalForce = computeTotalForceOnBody(bodyList)
    /* compute instant acceleration */
    val acc = new V2d(totalForce).scalarMul(1.0 / body.getMass)
    /* update velocity */
    body.updateVelocity(acc, dt)
    body.updatePos(dt)
    body.checkAndSolveBoundaryCollision(boundary)

  private def computeTotalForceOnBody(bodyList: List[Body]): V2d =
    val totalForce = new V2d(0, 0)
    /* compute total repulsive force */
    for (otherBody <- bodyList)
      if (!(body == otherBody)) try
        val forceByOtherBody = body.computeRepulsiveForceBy(otherBody)
        totalForce.sum(forceByOtherBody)
      catch
        case ignored: Exception =>
    /* add friction force */
    totalForce.sum(body.getCurrentFrictionForce)
    totalForce



