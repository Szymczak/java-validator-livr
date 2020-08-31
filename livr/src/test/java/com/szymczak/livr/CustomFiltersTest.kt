package com.szymczak.livr

import com.szymczak.livr.util.MyFuncClass
import org.junit.Test
import org.skyscreamer.jsonassert.JSONAssert

class CustomFiltersTest {

    @Test
    fun `Validate data with registered rules`() {

        val rules = mapOf(
            "my_trim" to MyFuncClass.my_trim,
            "my_lc" to MyFuncClass.my_lc,
            "my_ucfirst" to MyFuncClass.my_ucfirst
        )

        val validator = LIVR.validator()
            .registerDefaultRules(rules)
            .init(
                """
                    {
                      "word1": [
                        "my_trim",
                        "my_lc",
                        "my_ucfirst"
                      ],
                      "word2": [
                        "my_trim",
                        "my_lc"
                      ],
                      "word3": [
                        "my_ucfirst"
                      ]
                    } 
                """, false
            )

        val output = validator.validate(
            """
                {
                  "word1": " wordOne ",
                  "word2": " wordTwo ",
                  "word3": "wordThree "
                }
                """
        )

        JSONAssert.assertEquals(
            output.toJSONString(),
            """
                {
                  "word1": "Wordone",
                  "word2": "wordtwo",
                  "word3": "WordThree "
                }
            """, false
        )
    }
}
