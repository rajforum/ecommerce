package api.workflow

import core.HTTPUtil
import db.mysqlDB
import model.WareHouse
import util.DBUtil.WareHouseTable
import util.UrlUtil.WareHouseUrl

object WareHouseWorkflow {
	def getWareHouse(postalCode: Long): WareHouse = {
		val url = WareHouseUrl.getWareHouse(postalCode)
		val response = HTTPUtil.executeGet(url)

		new WareHouse(
			postalCode,
			BigDecimal((response \ "distance_in_kilometers").get.toString())
		)
	}
}

object DBWareHouseWorkflow {
	def getWareHouse(postalCode: Long): WareHouse = {
		val resultMaps = mysqlDB.selectQuery(WareHouseTable.getWareHouse(postalCode))

		WareHouse.build(resultMaps.head)
	}
}