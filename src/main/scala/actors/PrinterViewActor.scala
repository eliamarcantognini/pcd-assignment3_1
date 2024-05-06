package actors

import actors.View.ViewMessages
import akka.actor.typed.ActorRef
import model.Commands
import view.PrinterView
import view.gui.GUIView

class PrinterViewActor(actorRef: ActorRef[ViewMessages]) extends PrinterView(){

  this.addListener {
    case Commands.START =>
      this.actorRef ! ViewMessages.Start()
    case Commands.STOP =>
      this.actorRef ! ViewMessages.Stop()
  }

}
