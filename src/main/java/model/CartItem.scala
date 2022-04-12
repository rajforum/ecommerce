package model

import play.api.libs.functional.syntax.{ toFunctionalBuilderOps, unlift }
import play.api.libs.json.{ Format, JsPath }
import play.api.libs.json.Format.GenericFormat

case class CartItem(
	product_id: Long,
	description: Option[String] = None,
	quantity: Int
)

object CartItem {
	implicit val cartItem: Format[CartItem] = (
		(JsPath \ "product_id").format[Long] and
		(JsPath \ "description").formatNullable[String] and
		(JsPath \ "quantity").format[Int]
	) (CartItem.apply, unlift(CartItem.unapply))

	def build(resultMap: Map[String, String]): CartItem = this(
		resultMap("product_id").toLong,
		Some(resultMap("description")),
		resultMap("quantity").toInt
	)
}