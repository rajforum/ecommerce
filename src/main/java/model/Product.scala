package model

import play.api.libs.functional.syntax.{ toFunctionalBuilderOps, unlift }
import play.api.libs.json.{ Format, JsPath }
import play.api.libs.json.Format.GenericFormat

case class Product(
	id: Long,
	name: String,
	description: String,
	category: String,
	image: String,
	discount_percentage: Int,
	weight_in_grams: BigDecimal
)

object Product {
	implicit val product: Format[Product] = (
		(JsPath \ "id").format[Long] and
		(JsPath \ "name").format[String] and
		(JsPath \ "description").format[String] and
		(JsPath \ "category").format[String] and
		(JsPath \ "image").format[String] and
		(JsPath \ "discount_percentage").format[Int] and
		(JsPath \ "weight_in_grams").format[BigDecimal]
	) (Product.apply, unlift(Product.unapply))

	def build(resultMap: Map[String, String]): Product = this(
		resultMap("id").toLong,
		resultMap("name"),
		resultMap("description"),
		resultMap("category"),
		resultMap("image"),
		resultMap("discount_percentage").toInt,
		BigDecimal(resultMap("weight_in_grams"))
	)
}