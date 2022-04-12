package api.workflow

import play.api.libs.json.Format.GenericFormat

import core.HTTPUtil
import db.mysqlDB
import model.{ CartItem, Product }
import util.DBUtil.ProductTable
import util.UrlUtil.ProductUrl

object ProductWorkflow {
	def getProduct(productId: Long): Product = {
		val response = HTTPUtil.executeGet(ProductUrl.getProduct(productId))

		response.as[Product]
	}
}

object DBProductWorkflow {
	def getProduct(productId: Long): Product = {
		val resultMaps = mysqlDB.selectQuery(ProductTable.getProduct(productId))

		Product.build(resultMaps.head)
	}

	def validateUserProduct(cartItems: List[CartItem], productIdMap: Map[Long, Int]): Boolean = {
		productIdMap.exists(productDetails ⇒ {
			val (id, quantity) = productDetails

			cartItems.exists(cartItem ⇒ {
				cartItem.product_id == id && cartItem.quantity == quantity
			})
		})
	}
}