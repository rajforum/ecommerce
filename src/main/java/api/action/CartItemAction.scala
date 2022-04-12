package api.action

import play.api.libs.json.Json

import api.workflow.CartItemWorkflow
import core.{ ActionInput, ActionOutput }
import model.CartItem

class CartItemAction {
	def show(actionInput: ActionInput): ActionOutput = {
		val cartItems = CartItemWorkflow.getAllCartItems

		ActionOutput(
			Json.toJson(cartItems.map(cartItem ⇒ Json.toJson(cartItem)))
		)
	}

	def destroy(actionInput: ActionInput): ActionOutput = {
		CartItemWorkflow.deleteCartItems()

		null
	}

	def update(actionInput: ActionInput): ActionOutput = {
		val cartItem = new CartItem(
			101,
			None,
			10,
		) //todo:
		CartItemWorkflow.addCartItem(cartItem)

		null
	}

	def getCheckoutValue(actionInput: ActionInput): ActionOutput = {
		val cartItems = List.empty[CartItem] //todo:
		val postalCode = actionInput.getId("postal_code")

		val amount = CartItemWorkflow.getCheckoutValue(cartItems, postalCode)

		ActionOutput(
			Json.toJson(Map(
				"status" → "success",
				"message" → s"Total value of your shopping cart is: ${amount}"
			))
		)
	}
}