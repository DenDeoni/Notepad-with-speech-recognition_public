package com.notes.example.utils


import com.notes.example.constans.*
import com.notes.example.network.Network
import org.json.JSONArray

class ParsingJSON() {
    fun parseJson(): JSONArray {
        val network = Network(BASE_URL)
        network.readContent()
        return JSONArray(network.body)
    }
}