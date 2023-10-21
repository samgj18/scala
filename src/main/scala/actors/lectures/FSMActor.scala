package actors.lectures

import akka.actor._
import scala.concurrent.duration._

object FSMActor extends App {

  // Messages
  case object VendingError
  case class RequestProduct(product: String)
  case class Instruction(msg: String)
  case class ReceiveMoney(amount: Int)
  case class Deliver(product: String)
  case class GiveBackChange(amount: Int)

  sealed trait VendingState
  case object Idle         extends VendingState
  case object Operational  extends VendingState
  case object WaitForMoney extends VendingState

  sealed trait VendingData
  case object Uninitialized                                                     extends VendingData
  case class Initialized(inventory: Map[String, Int], prices: Map[String, Int]) extends VendingData
  case class WaitForMoney(
      inventory: Map[String, Int],
      prices: Map[String, Int],
      product: String,
      money: Int,
      requester: ActorRef
  ) extends VendingData

  class VendingMachineFSM extends FSM[VendingState, VendingData] {
    startWith(Idle, Uninitialized)

    // Partial Function from Event to State
    when(Idle) {
      case Event(initialize @ Initialized(inventory, prices), Uninitialized) =>
        goto(Operational) using initialize

      case _ => {
        sender() ! VendingError
        stay()
      }
    }

    when(Operational) {
      case Event(RequestProduct(product), Initialized(inventory, prices)) =>
        inventory.get(product) match {
          case None | Some(0) => {
            sender() ! VendingError
            stay()
          }
          case Some(_)        => {
            val price = prices.get(product)
            price match {
              case None        => {
                sender() ! VendingError
                goto(Idle) using Uninitialized
              }
              case Some(price) => {
                sender() ! Instruction(s"Please insert $price")
                goto(WaitForMoney) using WaitForMoney(inventory, prices, product, 0, sender())
              }
            }
          }
        }

      case _ => {
        sender() ! VendingError
        goto(Idle) using Uninitialized
      }
    }

    when(WaitForMoney, stateTimeout = 1.second) {
      case Event(StateTimeout, WaitForMoney(inventory, prices, product, money, requester))         => {
        requester ! VendingError
        if (money > 0) requester ! GiveBackChange(money)
        goto(Operational) using Initialized(inventory, prices)
      }
      case Event(ReceiveMoney(amount), WaitForMoney(inventory, prices, product, money, requester)) =>
        val price = prices.get(product)
        price match {
          case None        => {
            sender() ! VendingError
            goto(Idle) using Uninitialized
          }
          case Some(price) => {
            if ((money + amount) >= price) {
              requester ! Deliver(product)
              if (money + amount - price > 0) requester ! GiveBackChange(money + amount - price)

              inventory.get(product) match {
                case None        => {
                  sender() ! VendingError
                  goto(Idle) using Uninitialized
                }
                case Some(stock) => goto(Operational) using Initialized(inventory.updated(product, stock), prices)
              }
            } else {
              val remaining = price - money - amount
              requester ! Instruction(s"Please insert the rest of the money $remaining")
              stay() using WaitForMoney(inventory, prices, product, money + amount, requester)
            }
          }
        }
    }

    whenUnhandled {
      case Event(_, _) => {
        sender() ! VendingError
        stay()
      }
    }

    onTransition { case oldState -> newState =>
      log.info(s"onTransition from $oldState to $newState")
    }

    initialize()
  }
}
