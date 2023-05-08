package com.example.dotametrics.data.remote.service

import com.example.dotametrics.data.remote.model.constants.abilities.CustomArrayList
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter

class StringJsonAdapter : TypeAdapter<CustomArrayList<String>>() {
    override fun write(out: JsonWriter?, value: CustomArrayList<String>?) {}

    override fun read(`in`: JsonReader?): CustomArrayList<String> {
        val deserializedObject = CustomArrayList<String>()
        val peek: JsonToken = `in`!!.peek()

        if (JsonToken.STRING == peek) {
            val stringValue = `in`.nextString()
            deserializedObject.add(stringValue)
        }

        if (JsonToken.BEGIN_ARRAY == peek) {
            `in`.beginArray()
            while (`in`.hasNext()) {
                val element = `in`.nextString()
                deserializedObject.add(element)
            }
            `in`.endArray()
        }

        return deserializedObject
    }
}