package com.szymczak.livr

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.skyscreamer.jsonassert.JSONAssert

class ListLengthTests {

    @Test
    fun `list of equal length returns returns a valid output`() {
        //given
        val validator = LIVR.validator()
            .init(
                """
                    {
                      "list": [
                            {
                              "list_length": 3
                            }
                          ]
                    } 
                """, false
            )

        //when
        val output = validator.validate(
            """
                {
                  "list": ["one","two","three"]
                }"""
        )

        //then
        JSONAssert.assertEquals(
            output.toJSONString(),
            """
                {
                  "list": ["one","two","three"]
                }
            """, false
        )
    }

    @Test
    fun `too short a list returns returns a too few items error`() {
        //given
        val validator = LIVR.validator()
            .init(
                """
                    {
                      "list": [
                            {
                              "list_length": 3
                            }
                          ]
                    } 
                """, false
            )

        //when
        validator.validate(
            """
                {
                  "list": ["one"]
                }"""
        )

        //then
        assertThat(validator.getErrors()["list"])
            .isEqualTo("TOO_FEW_ITEMS")
    }

    @Test
    fun `too long a list returns returns a too many items error`() {
        //given
        val validator = LIVR.validator()
            .init(
                """
                    {
                      "list": [
                            {
                              "list_length": 2
                            }
                          ]
                    } 
                """, false
            )

        //when
        validator.validate(
            """
                {
                  "list": ["one","two","three"]
                }"""
        )

        //then
        assertThat(validator.getErrors()["list"])
            .isEqualTo("TOO_MANY_ITEMS")
    }

    @Test
    fun `list of a length between a min and max limit returns a valid output`() {
        //given
        val validator = LIVR.validator()
            .init(
                """
                    {
                      "list": [
                            {
                              "list_length": [2,5]
                            }
                          ]
                    } 
                """, false
            )

        //when
        val output = validator.validate(
            """
                {
                  "list": ["one","two"]
                }"""
        )

        //then
        JSONAssert.assertEquals(
            output.toJSONString(),
            """
                {
                  "list": ["one","two"]
                }
            """, false
        )
    }

    @Test
    fun `too short a list of a length between a min and a max limit returns a too few items error`() {
        //given
        val validator = LIVR.validator()
            .init(
                """
                    {
                      "list": [
                            {
                              "list_length": [5,10]
                            }
                          ]
                    } 
                """, false
            )

        //when
        validator.validate(
            """
                {
                  "list": ["one","two","three"]
                }"""
        )

        //then
        assertThat(validator.getErrors()["list"])
            .isEqualTo("TOO_FEW_ITEMS")
    }

    @Test
    fun `too long a list of a length between a min and a max limit returns a too many items error`() {
        //given
        val validator = LIVR.validator()
            .init(
                """
                    {
                      "list": [
                            {
                              "list_length": [2,3]
                            }
                          ]
                    } 
                """, false
            )

        //when
        validator.validate(
            """
                {
                  "list": ["one","two","three","four"]
                }"""
        )

        //then
        assertThat(validator.getErrors()["list"])
            .isEqualTo("TOO_MANY_ITEMS")
    }

    @Test
    fun `input which is not a list returns a format error`() {
        //given
        val validator = LIVR.validator()
            .init(
                """
                    {
                      "list": [
                            {
                              "list_length": [2,3]
                            }
                          ]
                    } 
                """, false
            )

        //when
        validator.validate(
            """
                {
                  "list": "one"
                }"""
        )

        //then
        assertThat(validator.getErrors()["list"])
            .isEqualTo("FORMAT_ERROR")
    }

}
