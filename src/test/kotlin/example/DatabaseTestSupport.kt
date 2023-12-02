package example

import org.junit.jupiter.api.extension.AfterTestExecutionCallback
import org.junit.jupiter.api.extension.BeforeAllCallback
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.api.extension.ParameterContext
import org.junit.jupiter.api.extension.ParameterResolver
import org.komapper.core.dsl.Meta
import org.komapper.core.dsl.QueryDsl
import org.komapper.jdbc.JdbcDatabase
import org.komapper.tx.core.TransactionProperty
import org.komapper.tx.jdbc.JdbcTransactionSession

class DatabaseTestSupport :
    BeforeAllCallback,
    BeforeTestExecutionCallback,
    AfterTestExecutionCallback,
    ParameterResolver {

    companion object {
        @Volatile
        private var initialized: Boolean = false
        // Komapperのデータベース
        private val db = JdbcDatabase(url = System.getProperty("jdbc.url") ?: error("jdbc.url not found"))
        // Komapperのトランザクションマネージャー
        private val transactionManager = run {
            val session = db.config.session as JdbcTransactionSession
            session.transactionManager
        }
    }

    // 最初のテストを実行する前に一度だけ cusotmers テーブルを作成する
    override fun beforeAll(context: ExtensionContext) {
        if (!initialized) {
            initialized = true
            db.runQuery {
                QueryDsl.create(Meta.customer)
            }
        }
    }

    // テストメソッドの実行前にトランザクションを開始する
    override fun beforeTestExecution(context: ExtensionContext) {
        transactionManager.begin(TransactionProperty.Name(context.displayName))
    }

    // テストメソッドの実行後にトランザクションをロールバックする
    override fun afterTestExecution(context: ExtensionContext) {
        transactionManager.rollback()
    }

    // テストクラスのコンストラクターの型をチェックする
    override fun supportsParameter(
        parameterContext: ParameterContext,
        extensionContext: ExtensionContext,
    ): Boolean = parameterContext.parameter.type === JdbcDatabase::class.java

    // テストクラスのコンストラクターに db を渡す
    override fun resolveParameter(
        parameterContext: ParameterContext,
        extensionContext: ExtensionContext,
    ): Any = db
}
