package actors

import actors.Simulator.SimulatorMessages
import actors.View.ViewMessages
import akka.actor.typed.scaladsl.{ActorContext, Behaviors}
import akka.actor.typed.{ActorRef, ActorSystem, Behavior, scaladsl}
import model.{Body, Boundary, P2d, V2d}
import view.PrinterView

import scala.collection.mutable.ListBuffer
import scala.util.Random
import scala.collection.JavaConverters.*

object View:

  // messages ending with number are fake messages for testing
  enum ViewMessages:
    //    case BodyCreated(body: Body)
    //    case Next
    ////    case SimulatorRef(ref: ActorRef[SimulatorMessages])
    case Start(n: Int, it: Int)
    //    case Stop
    //    case Updates(body: Body)
    case SimulatorRef(ref: ActorRef[SimulatorMessages])
    case DisplayBodies(bodies: List[Body], virtualTime: Double, iteration: Long, boundary: Boundary)

  export ViewMessages.*

  def apply(name: String): Behavior[ViewMessages] =
    Behaviors.setup { ctx =>
      ctx.log.info("Object Simulator apply, creation simulator in waiting")
      //      new View(name).waiting
      new View(ctx, name).waiting
    }

//class View private(name: String):
class View private(ctx: ActorContext[ViewMessages], name: String):
  //

  import View.*
  import view.*

  private val view = new PrinterView()
  private var simulatorRef: Option[ActorRef[SimulatorMessages]] = None

  private val waiting: Behavior[ViewMessages] =
    Behaviors.receiveMessagePartial {
      case DisplayBodies(b, vt, it, bounds) =>
        this.view.display(b.asJava, vt, it, bounds)
        this.simulatorRef.get ! SimulatorMessages.ViewCompletedDisplay
        Behaviors.same
      case SimulatorRef(ref) =>
        this.simulatorRef = Some(ref)
        //        this.simulatorRef ! ctx.self
        Behaviors.same
      case Start(n, it) =>
        this.simulatorRef.get ! SimulatorMessages.Start(ctx.self, n, it)
        Behaviors.same

    }
//  //  private var ownSimulatorRef: Option[ActorRef[SimulatorMessages]] = None
//  private var bodyList: List[Body] = List()
//  private val bodyListBuffer: ListBuffer[Body] = ListBuffer()
//  private val mass = 10
//  private val dt = 0.01
//  private val boundary: Boundary = Boundary(-6, -6, 6, 6)
//  private var bodyNumberSimulation: Int = 0
//  private var simulationIteration: Int = 0
//  private var actualSimulationIteration: Int = 0
//
//  private val bodyActors: ListBuffer[ActorRef[Tick]] = ListBuffer()
//
//  private val waiting: Behavior[SimulatorMessages] =
//    this.enteringStateLog("waiting")
//    Behaviors.receiveMessagePartial {
//      case Start(nBody, it) =>
//        this.receivedMsgFromStateLog("waiting", Start(nBody, it))
//        startSimulation(nBody, it)
//        Thread.sleep(3000)
//        this.waitingBodiesInitialized
////      case SimulatorRef(ref) =>
//        //        this.ownSimulatorRef = Some(ref)
////        Behaviors.same
//
//    }
//
//  private lazy val waitingBodiesInitialized: Behavior[SimulatorMessages] =
//    val stateName = "waitingBodiesInitialized"
//    this.enteringStateLog(stateName)
//    Behaviors.receiveMessagePartial {
//      case BodyCreated(body) =>
//        this.receivedMsgFromStateLog(stateName, BodyCreated(body))
//        bodyListBuffer.append(body)
//        if bodyListBuffer.size == this.bodyNumberSimulation then
//          bodyList = bodyListBuffer.sortBy(b => b.getId).toList
//          this.ctx.log.info("All bodies initialized")
//          //          this.ownSimulatorRef.get ! Next
//          ctx.self ! Next
//          this.started
//        else
//          Behaviors.same
//    }
//
//  //  private lazy val idleCycle: Behavior[SimulatorMessages] = this.started
//
//  private lazy val started: Behavior[SimulatorMessages] =
//    val nameState = "started"
//    this.enteringStateLog(nameState)
//    var bodyUpdated = 0
//
//    Behaviors.receiveMessagePartial {
//      case Updates(body) =>
//        this.receivedMsgFromStateLog(nameState, Updates(body))
//        this.bodyListBuffer.append(body)
//        bodyUpdated = bodyUpdated + 1
//        if bodyUpdated >= this.bodyNumberSimulation then
//          this.bodyList = this.bodyListBuffer.sortBy(b => b.getId).toList
//          //          this.ownSimulatorRef.get ! Next
//          ctx.self ! Next
//        Behaviors.same
//      case Next =>
//        this.actualSimulationIteration = this.actualSimulationIteration + 1
//        if this.actualSimulationIteration > this.simulationIteration then
//          this.stopped
//        else
//          this.ctx.log.info(s"Start iteration $actualSimulationIteration")
//          bodyUpdated = 0
//          this.bodyListBuffer.clear()
//          this.tickBodyActors()
//          Behaviors.same
//      case Stop => this.stopped
//    }
//
//  private lazy val stopped: Behavior[SimulatorMessages] =
//    Behaviors.same
//
//  private def tickBodyActors(): Unit =
//    for
//      i <- bodyActors
//    yield
//      this.ctx.log.info(s"Telling to actor $i")
//      i.tell(Tick(this.bodyList, ctx.self))
//
//  private def startSimulation(n: Int, it: Int): Unit =
//    this.bodyNumberSimulation = n
//    this.simulationIteration = it
//    ctx.log.info(s"Simulation started with $n body for $it iterations")
//    val rand = new Random(System.currentTimeMillis)
//    for
//      i <- 0 until n
//      x = boundary.getX0 * 0.25 + rand.nextDouble * (boundary.getX1 - boundary.getX0) * 0.25
//      y = boundary.getY0 * 0.25 + rand.nextDouble * (boundary.getY1 - boundary.getY0) * 0.25
//      pos = P2d(x, y)
//    yield bodyActors += ctx.spawn(Behaviors.setup(BodyActor(_, i, mass, pos, dt, boundary, ctx.self)), "BodyActor" + i)
//
//
//  private def enteringStateLog(state: String): Unit =
//    this.ctx.log.info(s"Entering in $state")
//
//  private def receivedMsgFromStateLog(state: String, msg: SimulatorMessages): Unit =
//    this.ctx.log.info(s"From $state - Received message $msg")
