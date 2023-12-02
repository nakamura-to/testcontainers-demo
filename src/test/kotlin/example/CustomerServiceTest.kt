package example

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.komapper.jdbc.JdbcDatabase

@ExtendWith(DatabaseTestSupport::class)
class CustomerServiceTest(db: JdbcDatabase) {
    
    private val customerService = CustomerService(db)

    @Test
    fun shouldGetCustomers() {
        customerService.createCustomer(Customer(1L, "George"))
        customerService.createCustomer(Customer(2L, "John"))

        val customers = customerService.getAllCustomers()
        assertEquals(2, customers.size)
    }
}