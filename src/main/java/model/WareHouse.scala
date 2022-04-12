package model

import play.api.libs.functional.syntax.{ toFunctionalBuilderOps, unlift }
import play.api.libs.json.Format.GenericFormat
import play.api.libs.json.{ Format, JsPath }

case class WareHouse(
	postal_code: Long,
	distance_in_kilometers: BigDecimal
)

object WareHouse {
	implicit val wareHouse: Format[WareHouse] = (
		(JsPath \ "postal_code").format[Long] and
			(JsPath \ "distance_in_kilometers").format[BigDecimal]
		) (WareHouse.apply, unlift(WareHouse.unapply))

	def build(resultMap: Map[String, String]): WareHouse = this (
		resultMap("postal_code").toLong,
		BigDecimal(resultMap("distance_in_kilometers")),
	)
}