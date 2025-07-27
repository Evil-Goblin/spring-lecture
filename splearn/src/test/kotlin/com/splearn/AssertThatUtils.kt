package com.splearn

import org.assertj.core.api.AssertProvider
import org.assertj.core.api.Assertions.assertThat
import org.springframework.test.json.JsonPathValueAssert
import java.util.function.Consumer

class AssertThatUtils {
    companion object {
        fun notNull(): Consumer<AssertProvider<JsonPathValueAssert>> = Consumer { assertThat(it).isNotNull }

        fun <T> equalTo(expected: T): Consumer<AssertProvider<JsonPathValueAssert>> = Consumer { assertThat(it).isEqualTo(expected) }
    }
}
