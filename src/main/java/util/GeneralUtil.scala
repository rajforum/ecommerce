package util

import play.api.libs.json.{ JsNull, JsValue }

import model.CartItem

object ShippingChart {
	val distanceAxis: List[(BigDecimal, BigDecimal)] = List(
		(0, 5), (5, 20), (20, 50),
		(50, 500), (500, 800), (800, Integer.MAX_VALUE)
	)

	val weightAxis: List[(BigDecimal, BigDecimal)] = List(
		(0, 2000),
		(2001, 5000),
		(5001, 20000),
		(20000, Integer.MAX_VALUE)
	)

	val priceGraph: Array[BigDecimal] = Array(
		12, 15, 20, 50, 100, 220,
		14, 18, 24, 55, 110, 250,
		16, 25, 30, 80, 130, 270,
		21, 35, 50, 90, 150, 300
	)
}

object UrlUtil {
	private val domain = "https://e-commerce-api-recruitment.herokuapp.com"

	object CartUrl {
		def getItems: String = s"${domain}/cart/items"

		def addItem(): String = s"${domain}/cart/item"

		def deleteItems(): String = s"${domain}/cart/items"

		def getCheckoutValue(portalCode: Long): String = s"${domain}/checkout-value?shipping_postal_code=${portalCode}"
	}

	object ProductUrl {
		def getProduct(productId: Long = 101): String = s"${domain}/product/${productId}"
	}

	object WareHouseUrl {
		def getWareHouse(postalCode: Long): String = s"${domain}/warehouse/distance?postal_code=${postalCode}"
	}
}

object DBUtil {
	object CartTable {
		def getItems: String = s"SELECT * from cart_item"

		def addItem(cartItem: CartItem): String = s"INSERT cart_item values (${cartItem.product_id}, ${cartItem.description.get}, ${cartItem.quantity})"

		def deleteItems(): String = s"TRUNCATE table cart_item"
	}

	object ProductTable {
		def getProduct(productId: Long = 101): String = s"SELECT * from product where id = ${productId}"
	}

	object WareHouseTable {
		def getWareHouse(postalCode: Long): String = s"SELECT * from ware_house where postal_code = ${postalCode}"
	}
}

object JsUtil {
	def isNull(value: JsValue): Boolean = {
		(value == null || value == JsNull)
	}
}