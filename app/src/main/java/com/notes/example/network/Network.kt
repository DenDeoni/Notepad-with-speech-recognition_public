package com.notes.example.network

import com.fasterxml.jackson.core.util.RequestPayload
import javax.ws.rs.client.*
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.MultivaluedMap
import kotlin.properties.Delegates

class Network(var url: String) {

    var body: String by Delegates.notNull()
    var status: Int by Delegates.notNull()
    lateinit var headers: MultivaluedMap<String, Any>

    private fun buildClient(): Client {
        return ClientBuilder.newClient()
    }

    private fun buildRequest(): WebTarget {
        return buildClient().target(url)
    }

    private fun buildResponse(): Invocation.Builder {
        return buildRequest().request(MediaType.TEXT_PLAIN_TYPE)
    }

    fun readContent() {

        val response = buildResponse().get()

        status = response.status
        headers = response.headers
        body = response.readEntity(String::class.java)
    }

    fun deleteNote(): Int {

        val response = buildResponse().delete()

        status = response.status
        return status
    }

    fun updateNote(payload: RequestPayload) {
        val response = buildResponse().put(Entity.json(payload))
        status = response.status
    }

    fun createNote(payload: RequestPayload) {
        val response = buildResponse().post(Entity.json(payload))
        status = response.status
    }

}