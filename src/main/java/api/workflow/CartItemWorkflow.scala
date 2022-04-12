package api.workflow

import play.api.libs.json.Json

import core.HTTPUtil
import db.mysqlDB
import model.CartItem
import util.DBUtil.CartTable
import util.UrlUtil.CartUrl
import util.{ JsUtil, ShippingChart }

object CartItemWorkflow {
	def getAllCartItems: List[CartItem] = {
		val resp = HTTPUtil.executeGet(CartUrl.getItems)

		if (JsUtil.isNull(resp)) {
			List()
		} else {
			resp.as[List[CartItem]]
		}
	}

	def addCartItem(cartItem: CartItem): Unit = {
		HTTPUtil.executePost(
			CartUrl.addItem(),
			Some(Json.stringify(Json.toJson(cartItem)))
		)
	}

	def deleteCartItems(): Unit = {
		val body = Map("action" → "empty_cart")

		HTTPUtil.executePost(
			CartUrl.deleteItems(),
			Some(Json.stringify(Json.toJson(body)))
		)
	}

	def getCheckoutValue(cartItemList: List[CartItem], postalCode: Long): BigDecimal = {
		var totalPrice = BigDecimal(0)

		val itemsByProduct = cartItemList.groupBy(_.product_id)
		val productDetails = itemsByProduct.map(item ⇒ {
			val product = ProductWorkflow.getProduct(item._1)

			(product, item._2)
		})

		val wareHouse = WareHouseWorkflow.getWareHouse(postalCode)
		productDetails.foreach(productDetail ⇒ {
			val (productModel, cartItems) = productDetail
			val totalQuantity = cartItems.foldLeft(0)(_ + _.quantity)
			val totalWeight = productModel.weight_in_grams * totalQuantity

			totalPrice += getShippingCost(wareHouse.distance_in_kilometers, totalWeight)
		})

		totalPrice
	}

	private def getShippingCost(distance: BigDecimal, weightInGrams: BigDecimal): BigDecimal = {
		val distanceIndex = ShippingChart.distanceAxis.indexWhere(distanceRange ⇒ {
			(distance >= distanceRange._1) && (distance < distanceRange._2)
		}) + 1
		val weightIndex = ShippingChart.weightAxis.indexWhere(weightIndex ⇒ {
			(weightInGrams >= weightIndex._1) && (weightInGrams <= weightIndex._2)
		}) + 1

		val priceIndex = (distanceIndex * weightIndex) - 1

		ShippingChart.priceGraph(priceIndex)
	}
}

object DBCartItemWorkflow {
	def getAllCartItems: List[CartItem] = {
		val resultMaps = mysqlDB.selectQuery(CartTable.getItems)

		resultMaps.map(resultMap ⇒ {
			CartItem.build(resultMap)
		}).toList
	}

	def addCartItem(cartItem: CartItem): Unit = {
		mysqlDB.selectQuery(CartTable.addItem(cartItem))
	}

	def deleteCartItems(): Unit = {
		mysqlDB.selectQuery(CartTable.deleteItems())
	}

	def getCheckoutValue(cartItemList: List[CartItem], postalCode: Long): BigDecimal = {
		var totalPrice = BigDecimal(0)

		val itemsByProduct = cartItemList.groupBy(_.product_id)
		val productDetails = itemsByProduct.map(item ⇒ {
			val product = DBProductWorkflow.getProduct(item._1)

			(product, item._2)
		})

		val wareHouse = DBWareHouseWorkflow.getWareHouse(postalCode)
		productDetails.foreach(productDetail ⇒ {
			val (productModel, cartItems) = productDetail
			val totalQuantity = cartItems.foldLeft(0)(_ + _.quantity)
			val totalWeight = productModel.weight_in_grams * totalQuantity

			totalPrice += getShippingCost(wareHouse.distance_in_kilometers, totalWeight)
		})

		totalPrice
	}

	private def getShippingCost(distance: BigDecimal, weightInGrams: BigDecimal): BigDecimal = {
		val distanceIndex = ShippingChart.distanceAxis.indexWhere(distanceRange ⇒ {
			(distance >= distanceRange._1) && (distance < distanceRange._2)
		}) + 1
		val weightIndex = ShippingChart.weightAxis.indexWhere(weightIndex ⇒ {
			(weightInGrams >= weightIndex._1) && (weightInGrams <= weightIndex._2)
		}) + 1

		val priceIndex = (distanceIndex * weightIndex) - 1

		ShippingChart.priceGraph(priceIndex)
	}
}