package api.action

import play.api.libs.json.Json

import api.workflow.ProductWorkflow
import core.{ ActionInput, ActionOutput }

class ProductAction {
	def index(actionInput: ActionInput): ActionOutput = {
		val productId = actionInput.getId("product_id")

		val product = ProductWorkflow.getProduct(productId)

		ActionOutput(Json.toJson(product))
	}
}