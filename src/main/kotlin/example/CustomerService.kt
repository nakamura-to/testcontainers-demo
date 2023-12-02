package example

import org.komapper.core.dsl.Meta
import org.komapper.core.dsl.QueryDsl
import org.komapper.jdbc.JdbcDatabase

class CustomerService(private val db: JdbcDatabase) {
    // Customerエンティティクラスのメタモデル
    private val c = Meta.customer

    fun createCustomer(customer: Customer) {
        db.runQuery {
            // 対応するSQL： insert into customers (id, name) values (?, ?)
            QueryDsl.insert(c).single(customer)
        }
    }

    fun getAllCustomers(): List<Customer> {
        return db.runQuery {
            // 対応するSQL： select t0_.id, t0_.name from customers as t0_
            QueryDsl.from(c)
        }
    }
}