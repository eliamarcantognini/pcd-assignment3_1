package actors

import actors.View.ViewMessages
import akka.actor.typed.ActorRef
import model.Commands
import view.gui.GUIView

class GUIViewActor(w: Int, h: Int, actorRef: ActorRef[ViewMessages]) extends GUIView(w,h){

  this.setStopEnabled(false)

  this.addListener {
    case Commands.START =>
      this.setStartEnabled(false)
      this.setStopEnabled(true)
      this.actorRef ! ViewMessages.Start()
    case Commands.STOP =>
      this.setStartEnabled(false)
      this.setStopEnabled(false)
      this.actorRef ! ViewMessages.Stop()
  }

}
