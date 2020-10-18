package com.szymczak.livr

import com.szymczak.livr.util.MyFuncClass
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.skyscreamer.jsonassert.JSONAssert
import java.util.function.Function

class RulesReplacementTest {

    @Test
    fun `Validate data with registered rules`() {
        val validator = LIVR.validator()
        val defaultRules = validator.defaultRules
        val originalRules = mutableMapOf<String, Function<Any, Any>?>()
        val newRules = mutableMapOf<String, Function<Any, Any>?>()

        for (key in defaultRules.keys) {
            val ruleBuilder = defaultRules[key]
            originalRules[key] = ruleBuilder
            newRules[key] = MyFuncClass.patchRule(key, ruleBuilder)
        }

        validator.registerDefaultRules(newRules)

        val newValidator = validator.init(
            """{
                          "name": [
                            "required"
                          ],
                          "phone": {
                            "max_length": 10
                          }
                        }
                     """, true
        )

        val output = newValidator.validate("""{"phone": "123456789123456"}""")

        assertThat(output).isNull()

        JSONAssert.assertEquals(
            validator.getErrors().toJSONString(), """
                {
                  "name": {
                    "code": "REQUIRED",
                    "rule": {
                      "required": []
                    }
                  },
                  "phone": {
                    "code": "TOO_LONG",
                    "rule": {
                      "max_length": [
                        10
                      ]
                    }
                  }
                }
                """, false
        )
    }
}
