package com.eligijus.deeper

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform