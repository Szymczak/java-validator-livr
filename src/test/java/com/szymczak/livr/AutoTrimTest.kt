package com.szymczak.livr

import org.junit.Test
import org.assertj.core.api.Assertions.assertThat
import org.skyscreamer.jsonassert.JSONAssert

class AutoTrimTest {

    @Test
    fun `validate data with automatic trim`() {

        val validator = LIVR.validator().init(
            """
                {
                  "code": "required",
                  "password": [
                    "required",
                    {
                      "min_length": 3
                    }
                  ],
                  "address": {
                    "nested_object": {
                      "street": {
                        "min_length": 5
                      }
                    }
                  }
                }
            """, true
        )

        val output = validator.validate(
            """
                {
                  "code": "  ",
                  "password": " 12  ",
                  "address": {
                    "street": "  hell "
                  }
                }
                """
        )

        assertThat(output).isNull()
        JSONAssert.assertEquals(
            validator.getErrors().toJSONString(),
            """
                {
                  "code": "REQUIRED",
                  "password": "TOO_SHORT",
                  "address": {
                    "street": "TOO_SHORT"
                  }
                }
                """, false
        )
    }
}
