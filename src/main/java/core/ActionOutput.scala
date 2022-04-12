package core

import play.api.libs.json.JsValue

case class ActionOutput(response: JsValue) {
	def sendToClient(response: JsValue): Unit = {
		println(response)
	}
}