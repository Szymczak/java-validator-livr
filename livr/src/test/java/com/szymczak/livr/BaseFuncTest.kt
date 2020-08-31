package com.szymczak.livr

import com.szymczak.livr.util.IterationMethods
import org.assertj.core.api.Assertions.assertThat
import org.json.simple.JSONArray
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser
import org.junit.Test
import org.skyscreamer.jsonassert.JSONAssert

class BaseFuncTest {

    private val parser = JSONParser()

    @Test
    fun positive() {
        IterationMethods.listFilesForFolder("positive")
            .forEach { json ->
                //given
                val rules = parser.parseToJsonObject(json, "rules")
                val input = parser.parseToJsonObject(json, "input")
                val output = parser.parseToJsonObject(json, "output").toJSONString()
                val validator = LIVR.validator().init(rules, false)

                //when
                val result = validator.validate(input).toJSONString()

                //then
                assertThat(validator.getErrors()).isNull()
                JSONAssert.assertEquals(result, output, false)
            }
    }

    @Test
    fun negative() {
        IterationMethods.listFilesForFolder("negative")
            .forEach { json ->
                //given
                val rules = parser.parseToJsonObject(json, "rules")
                val input = parser.parseToJsonObject(json, "input")
                val errors = parser.parseToJsonObject(json, "errors").toJSONString()
                val validator = LIVR.validator().init(rules, false)

                //when
                validator.validate(input)
                val errorResult =
                    (parser.parse(JSONObject.toJSONString(validator.getErrors())) as JSONObject).toJSONString()

                //then
                JSONAssert.assertEquals(errorResult, errors, false)
            }
    }

    @Test
    fun `aliases positive`() {
        IterationMethods.listFilesForFolder("aliases_positive")
            .forEach { json ->
                //given
                val rules = parser.parseToJsonObject(json, "rules")
                val input = parser.parseToJsonObject(json, "input")
                val output = parser.parseToJsonObject(json, "output").toJSONString()
                val aliases = parser.parseToJsonArray(json, "aliases")
                val validator = LIVR.validator().init(rules, false)

                aliases.forEach {
                    validator.registerAliasedRule(it as JSONObject)
                }

                //when
                val result = validator.validate(input).toJSONString()

                //then
                assertThat(validator.getErrors()).isNull()

                //output
                JSONAssert.assertEquals(result, output, false)
            }
    }

    @Test
    fun `aliases negative`() {
        IterationMethods.listFilesForFolder("aliases_negative")
            .forEach { json ->
                //given
                val rules = parser.parseToJsonObject(json, "rules")
                val input = parser.parseToJsonObject(json, "input")
                val errors = parser.parseToJsonObject(json, "errors").toJSONString()
                val aliases = parser.parseToJsonArray(json, "aliases")
                val validator = LIVR.validator().init(rules, false)

                aliases.forEach {
                    validator.registerAliasedRule(it as JSONObject)
                }

                //when
                validator.validate(input)
                val errorResult =
                    (parser.parse(JSONObject.toJSONString(validator.getErrors())) as JSONObject).toJSONString()

                //then
                JSONAssert.assertEquals(errorResult, errors, false)
            }
    }

    @Test
    fun `null check test`() {

        //given
        val validator = LIVR.validator().prepare()
        //when
        val result = validator.validate(null as Any?)
        //then
        assertThat(result).isNull()
        JSONAssert.assertEquals(
            validator.getErrors().toJSONString(),
            """{"base":"FORMAT_ERROR"}""",
            false
        )
    }

    private fun JSONParser.parseToJsonObject(json: JSONObject, value: String) =
        parse(json.getValueAsString(value)) as JSONObject

    private fun JSONParser.parseToJsonArray(json: JSONObject, value: String) =
        parse(json.getValueAsString(value)) as JSONArray

    private fun JSONObject.getValueAsString(value: String) = getValue(value) as String

}
