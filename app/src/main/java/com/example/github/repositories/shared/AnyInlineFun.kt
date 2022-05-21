package com.example.github.repositories.shared

@Suppress("unused")
inline fun <reified T> T?.getLogTag() = T::class.simpleName