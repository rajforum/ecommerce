package core

import org.apache.commons.httpclient.methods.{ GetMethod, PostMethod }
import org.apache.commons.httpclient.{ HttpClient, HttpMethodBase }

import play.api.libs.json.{ JsNull, JsValue, Json }

object HTTPUtil {
	def executeGet(url: String): JsValue = {
		execute(new GetMethod(url))
	}

	def executePost(
		url: String,
		bodyContent: Option[String] = None
	): JsValue = {
		val post = new PostMethod(url)

		if (bodyContent.isDefined) {
			post.setRequestBody(bodyContent.get)
		}

		execute(post)
	}

	private def execute(method: HttpMethodBase): JsValue = {
		var response: String = null

		try {
			method.setRequestHeader("Content-Type", "application/json; UTF-8")
			val client = new HttpClient
			val status = client.executeMethod(method)

			if (status >= 200 && status < 300) {
				response = method.getResponseBodyAsString
			}
		} catch {
			case e: Exception ⇒ {
				println(s"### ERROR: ${e.getMessage} ------")
			}
		} finally {
			method.releaseConnection()
		}

		if (response == null) {
			JsNull
		} else {
			Json.parse(response)
		}
	}
}