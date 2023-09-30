package ru.ezhov.example.jsontokotlin

import com.fasterxml.jackson.databind.ObjectMapper

fun main() {
    val jsonObjectAsText = """
        {
           "id": 0,
           "text": "string",
           "parentId": 0,
           "author": {
              "id": 0,
              "name": "string"
           },
           "date": "2023-08-30T06:35:25.944Z",
           "files": [
              "string"
           ],
           "answers": [
              {
                 "name": "string",
                 "comment": {
                    "text": "string",
                    "id": "string"
                 }
              }
           ],
           "liked": true,
           "likesCount": 0
        }
    """.trimIndent()

    println(JsonToKotlinGenerator(jsonObjectAsText).generate())
}

class JsonToKotlinGenerator(
    private val jsonObjectAsText: String,
) {
    fun generate(): String {
        val mapper = ObjectMapper()
        val objectAsMap = mapper.readValue(jsonObjectAsText, Map::class.java)

        val fields = mutableListOf<Field>()

        val mapObjects = mutableMapOf<String, NewObject>()

        objectAsMap.forEach { (k, v) ->
            if (v is List<*>) {
                if (v.isEmpty()) {
                    fields.add(Field(k.toString(), "List<String>"))
                } else {
                    val firstValue = v.first()
                }
            } else if (v is Map<*, *>) {
                val objectName = k.toString().replaceFirstChar { it.uppercase() }
            } else {
                fields.add(Field(k.toString(), "String"))
            }
        }


        return fields.toString()
    }
}

data class Field(val name: String, val type: String)

data class NewObject(val objectName: String, val name: String, val type: String, val fields: List<Field>)

//data class ExampleRequest(
//    val id: String,
//    val text: String,
//    val parentId: String,
//    val author: AuthorRequest,
//    val date: String,
//    val files: List<String>,
//    val answers: List<AnswersRequest>,
//    val liked: String,
//    val likesCount: String,
//)
//
//data class AuthorRequest(
//    val id: String,
//    val name: String,
//)
//
//data class AnswersRequest(
//    val name: String,
//    val comment: String,
//)
