package api.action

import play.api.libs.json.Json

import api.workflow.WareHouseWorkflow
import core.{ ActionInput, ActionOutput }

class WareHouseAction {
	def index(actionInput: ActionInput): ActionOutput = {
		val postalCode = actionInput.getId("postal_code")
		val wareHouse = WareHouseWorkflow.getWareHouse(postalCode)

		ActionOutput(Json.toJson(wareHouse))
	}
}