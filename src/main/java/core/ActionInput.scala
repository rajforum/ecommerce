package core

case class ActionInput(
	params: Map[String, String]
) {
	def getParam(name: String, default: String = null): String = {
		params.getOrElse(name, default)
	}

	def getId(name: String, default: Long = -1L): Long = {
		params.getOrElse(name, default).toString.toLong
	}
}