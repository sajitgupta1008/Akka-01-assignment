import akka.actor.{Actor, ActorLogging, ActorRef, Props}


class PurchaseRequestHandler(validationActor: ActorRef) extends Actor with ActorLogging {
  override def receive: Receive = {

    case customer: Customer =>
      log.info("Forwarding customer request to ValidationActor")
      validationActor forward customer


    case _ => log.error("Please supply customer details.")
      sender() ! "I can handle only S8 purchase requests."

  }
}

object PurchaseRequestHandler {
  def props(validationActor: ActorRef): Props = Props(classOf[PurchaseRequestHandler], validationActor)
}

