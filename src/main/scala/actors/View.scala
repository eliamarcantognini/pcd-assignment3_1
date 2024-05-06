package actors

import actors.Simulator.SimulatorMessages
import actors.View.ViewMessages
import akka.actor.typed.scaladsl.{ActorContext, Behaviors}
import akka.actor.typed.{ActorRef, ActorSystem, Behavior, scaladsl}
import model.{Body, Boundary, Commands, P2d, V2d}
import view.PrinterView
import view.gui.GUIView

import scala.collection.mutable.ListBuffer
import scala.util.Random
import scala.collection.JavaConverters.*

object View:

  enum ViewMessages:
    case Start()
    case Stop()
    case SimulatorRef(ref: ActorRef[SimulatorMessages])
    case DisplayBodies(bodies: List[Body], virtualTime: Double, iteration: Long, boundary: Boundary)

  export ViewMessages.*

  def apply(name: String, gui: Boolean): Behavior[ViewMessages] =
    Behaviors.setup { ctx =>
      ctx.log.info("Object Simulator apply, creation simulator in waiting")
      new View(ctx, name, gui).waiting
    }

class View private(ctx: ActorContext[ViewMessages], name: String, gui: Boolean):

  import View.*

  private val view = if gui then new GUIViewActor(1000,1000,ctx.self) else new PrinterViewActor(ctx.self)
//    val view = new GUIView(1000,1000)
//    view.addListener{
//        case Commands.START => this.simulatorRef.get ! SimulatorMessages.Start(ctx.self)
//        case Commands.STOP => this.simulatorRef.get ! SimulatorMessages.Stop
//    }
//    view
//  else
//    val view = new PrinterView()
//    view.addListener {
//      case Commands.START => this.simulatorRef.get ! SimulatorMessages.Start(ctx.self)
//      case Commands.STOP => this.simulatorRef.get ! SimulatorMessages.Stop
//    }
//    view
//  view.addListener {
//      case Commands.START => this.simulatorRef.get ! SimulatorMessages.Start(ctx.self)
//      case Commands.STOP => this.simulatorRef.get ! SimulatorMessages.Stop
//  }


  private var simulatorRef: Option[ActorRef[SimulatorMessages]] = None

  private val waiting: Behavior[ViewMessages] =
    Behaviors.receiveMessagePartial {
      case DisplayBodies(b, vt, it, bounds) =>
        this.view.display(b.asJava, vt, it, bounds)
        this.simulatorRef.get ! SimulatorMessages.ViewCompletedDisplay
        Behaviors.same
      case SimulatorRef(ref) =>
        this.simulatorRef = Some(ref)
//        view.addListener {
//          case Commands.START => this.simulatorRef.get ! SimulatorMessages.Start(ctx.self)
//          case Commands.STOP => this.simulatorRef.get ! SimulatorMessages.Stop
//        }
        Behaviors.same
      case Start() =>
        this.simulatorRef.get ! SimulatorMessages.Start(ctx.self)
        Behaviors.same
      case Stop() =>
        this.simulatorRef.get ! SimulatorMessages.Stop
        Behaviors.same
    }

//  private val simulating: Behavior[ViewMessages] =
//    Behaviors.receiveMessagePartial {
//      case DisplayBodies(b, vt, it, bounds) =>
//        this.view.display(b.asJava, vt, it, bounds)
//        this.simulatorRef.get ! SimulatorMessages.ViewCompletedDisplay
//        Behaviors.same
//    }
