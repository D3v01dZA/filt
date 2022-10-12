package net.filt.model

import io.github.cdimascio.dotenv.Dotenv
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.statements.StatementContext
import org.jetbrains.exposed.sql.statements.expandArgs
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.LoggerFactory
import java.sql.Connection

fun initDB(dotenv: Dotenv): Database {
    val database = Database.connect("jdbc:sqlite:" + dotenv.get("db"), "org.sqlite.JDBC")
    TransactionManager.manager.defaultIsolationLevel = Connection.TRANSACTION_SERIALIZABLE
    transaction {
        addLogger(Slf4jSqlLogger)
        SchemaUtils.createMissingTablesAndColumns(UsersModel)
    }
    return database
}

object Slf4jSqlLogger : SqlLogger {

    private val logger = LoggerFactory.getLogger(Slf4jSqlLogger::class.java)

    override fun log(context: StatementContext, transaction: Transaction) {
        if (logger.isInfoEnabled) {
            logger.info(context.expandArgs(TransactionManager.current()))
        }
    }
}