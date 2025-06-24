package com.amadiyawa.feature_base.common.util

import kotlin.random.Random

fun generateRandomHash(size: Int): String {
    val chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
    return (1..size)
        .map { chars[Random.nextInt(chars.length)] }
        .joinToString("")
}