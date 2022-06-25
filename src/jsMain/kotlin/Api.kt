package ru.mipt.npm.nica.emd

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer

import kotlinx.browser.window

val endpoint = window.location.origin // only needed until https://github.com/ktorio/ktor/issues/1695 is resolved

val jsonClient = HttpClient {
    install(JsonFeature) { serializer = KotlinxSerializer() }
}

suspend fun getConfig(): ConfigFile {
    return jsonClient.get(endpoint + CONFIG_URL)
}

suspend fun getEMD(api_url: String): String {
    return jsonClient.get(endpoint + api_url)
}

suspend fun getSoftwareVersions(): List<SoftwareVersion> {
    return jsonClient.get(endpoint + SOFTWARE_URL)
}
