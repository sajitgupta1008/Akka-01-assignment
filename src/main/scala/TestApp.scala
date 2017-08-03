import akka.actor.ActorSystem
import akka.util.Timeout
import akka.pattern.ask
import scala.concurrent.duration.DurationInt

object TestApp extends App {

  private val name = "sajit"
  private val address = "lko"
  private val creditCardNo = 43546464745L
  private val mobile = "8743922586"

  val customer = Customer(name,address ,creditCardNo ,mobile )

  val system = ActorSystem("system")

  val purchaseActor = system.actorOf(PurchaseMaster.props)
  val validationActor = system.actorOf(ValidationActor.props(purchaseActor))
  val purchaseRequestHandler = system.actorOf(PurchaseRequestHandler.props(validationActor))

  implicit val timeout = Timeout(10 seconds)
  import scala.concurrent.ExecutionContext.Implicits.global

  val f = purchaseRequestHandler ? customer
  f foreach println

}
