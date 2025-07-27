package com.splearn

import org.mockito.Mockito
import org.springframework.boot.SpringApplication
import org.springframework.context.ConfigurableApplicationContext
import kotlin.test.Test

class SplearnApplicationTest {

    @Test
    fun run() {
        val contextMock = Mockito.mock(ConfigurableApplicationContext::class.java)

        Mockito.mockStatic(SpringApplication::class.java).use { mocked ->
            mocked.`when`<ConfigurableApplicationContext> {
                SpringApplication.run(SplearnApplication::class.java, *arrayOf<String>())
            }.thenReturn(contextMock)

            main(emptyArray<String>())

            mocked.verify { SpringApplication.run(SplearnApplication::class.java, *arrayOf()) }
        }
    }

}
