package com.eligijus.deeper.data.remote

import com.eligijus.deeper.data.remote.dto.LoginRequestDto
import com.eligijus.deeper.data.remote.dto.LoginResponseDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class DeeperApi (
    private val client: HttpClient
) {
    suspend fun login(
        email: String,
        password: String
    ): LoginResponseDto {

        val response = client.post(
            "https://bathus.staging.deeper.eu/api/login"
        ) {
            contentType(ContentType.Application.Json)

            setBody(
                LoginRequestDto(
                    email = email,
                    password = password
                )
            )

        }
        println("HTTP status: ${response.status}")
        return response.body()
    }
}