import akka.actor.{Actor, ActorLogging, ActorRef, Props, Terminated}
import akka.routing.{ActorRefRoutee, RoundRobinRoutingLogic, Router}

class PurchaseActor extends Actor with ActorLogging {

  override def receive: Receive = {

    case customer: Customer =>
      log.info(s"Thank you for purchasing S8, ${customer.name}")
      sender() ! Mobile

  }
}

class PurchaseMaster extends Actor with ActorLogging {

  private val noOfRoutees = 5
  var router: Router = {
    val routees = Vector.fill(noOfRoutees) {
      val childRef = context.actorOf(Props[PurchaseActor])
      context watch childRef
      ActorRefRoutee(childRef)
    }
    Router(RoundRobinRoutingLogic(), routees)

  }

  override def receive: Receive = {

    case customer: Customer => log.info("sending request to worker PurchaseActor")
      router.route(customer, sender())

    case Terminated(a) =>
      router = router.removeRoutee(a)
      val childRef = context.actorOf(Props[PurchaseActor])
      context watch childRef
      router = router.addRoutee(childRef)

    case _ => log.info("?????")
  }
}


object PurchaseMaster {
  def props: Props = Props[PurchaseMaster]
}
