import akka.actor.{Actor, ActorLogging, ActorRef, Props}

class ValidationActor(purchaseMasterRef: ActorRef) extends Actor with ActorLogging {

  private var itemCount = 1000

  override def receive: Receive = {

    case customer: Customer =>

      if (itemCount > 0) {
        itemCount -= 1
        log.info("Item count is " + itemCount)
        log.info("Forwarding customer request to PurchaseActor")
        purchaseMasterRef forward customer
      }
      else {
        log.error("Out of stock !")
        sender() ! "Samsung galaxy S8 is out of stock!!!"
      }


  }
}

object ValidationActor {
  def props(purchaseActor: ActorRef): Props = Props(classOf[ValidationActor], purchaseActor)
}



