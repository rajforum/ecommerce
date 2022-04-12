package db

import java.sql.{ Connection, DriverManager }

import scala.collection.mutable.ListBuffer

object mysqlDB {
	private val driver = "com.mysql.cj.jdbc.Driver"
	private val dbName = "ecommerce"
	private val url = s"jdbc:mysql://localhost/${dbName}"
	private val username = "root"
	private val password = ""
	private var connection: Connection = null

	def getConnection: Connection = connection

	def openDBConnection(): Unit = {
		try {
			if (connection != null) {
				return
			}

			Class.forName(driver)
			connection = DriverManager.getConnection(url, username, password)
		} catch {
			case e: Exception => println(e.printStackTrace())
		}
	}

	def closeConnection(): Unit = {
		if (connection != null) {
			connection.close()
			connection = null
		}
	}

	def selectQuery(query: String): ListBuffer[Map[String, String]] = {
		val resultMap = ListBuffer.empty[Map[String, String]]

		try {
			openDBConnection()

			val statement = connection.createStatement()
			val resultSet = statement.executeQuery(query)

			val tableMeta = resultSet.getMetaData
			val columnCount = tableMeta.getColumnCount

			while (resultSet.next()) {
				var rowMap = Map.empty[String, String]

				for (index <- 1 to columnCount) {
					val columnName = tableMeta.getColumnName(index)
					val columnValue = resultSet.getString(index)

					rowMap += (columnName → columnValue)
				}

				resultMap += rowMap
			}
		} catch {
			case e: Exception ⇒ println(e.printStackTrace())
		} finally {
			closeConnection()
		}

		resultMap
	}

	def updateQuery(query: String): Unit = {
		try {
			openDBConnection()
			val statement = connection.createStatement()
			statement.executeQuery(query)
		} catch {
			case e: Exception ⇒ println(e.printStackTrace())
		} finally {
			closeConnection()
		}
	}

	def deleteQuery(query: String): Unit = {
		try {
			openDBConnection()
			val statement = connection.createStatement()
			statement.executeQuery(query)
		} catch {
			case e: Exception ⇒ println(e.printStackTrace())
		} finally {
			closeConnection()
		}
	}
}