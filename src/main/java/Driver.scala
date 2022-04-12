import scala.io.StdIn._

import api.workflow.{ DBCartItemWorkflow, DBProductWorkflow }
import model.CartItem

object Driver {
	def main(args: Array[String]) {
		initiateService()
	}

	def initiateService(): Unit = {
		try {
			print("Enter postal code (465535 - 465545): ")
			val postalCode = readLong()
			println()

			validatePostalCode(postalCode)

			val cartItems = DBCartItemWorkflow.getAllCartItems

			if (cartItems.nonEmpty) {
				UserInterFace.cartItemLayout(cartItems)
				val productIdMap = UserInterFace.itemSelectionLayout()

				if (DBProductWorkflow.validateUserProduct(cartItems, productIdMap)) {
					UserInterFace.checkoutLayout(cartItems, postalCode)
				} else {
					println("##### Invalid selection of product id and quantity #####")
				}
			} else {
				println("No items available, please try later")
			}
		} catch {
			case e: Exception ⇒ println(e.getMessage)
		}
	}

	def validatePostalCode(postalCode: Long): Unit = {
		if ((postalCode < 465535) || (postalCode > 465545)) {
			throw new Exception("Invalid postal code, valid ones are 465535 to 465545.")
		}
	}
}

object UserInterFace {
	def cartItemLayout(cartItems: List[CartItem]): Unit = {


		println("Product Id\t Description \t Available Quantity")
		println("----------\t ------------ \t -----------------")

		cartItems.foreach(cartItem ⇒ {
			println(s"${cartItem.product_id}\t\t\t ${cartItem.description.getOrElse("N/A")} \t\t\t ${cartItem.quantity}")
			println("----------\t ------------ \t -----------------")
		})
	}

	def itemSelectionLayout(): Map[Long, Int] = {
		var addAnother: String = "yes"
		var productIdMap = Map.empty[Long, Int]

		while (addAnother.trim.toLowerCase == "yes") {
			print("Enter product id: ")
			val productId = readLong()
			println()

			print("Enter quantity: ")
			val quantity = readInt()
			println()

			productIdMap += (productId → quantity)

			addAnother = readLine("Would you like to add another item: (yes/no):")
		}
		println("----------------------------------------")
		productIdMap
	}

	def checkoutLayout(cartItems: List[CartItem], postalCode: Long): Unit = {
		val amount = DBCartItemWorkflow.getCheckoutValue(cartItems, postalCode)

		println(s"Total payable amount ${amount}")
	}
}