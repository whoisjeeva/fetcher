package app.spidy.fetcher.models

import app.spidy.fetcher.BuildConfig

data class Argument(
    val params: HashMap<String, Any?> = hashMapOf(),
    val headers: HashMap<String, Any?> = hashMapOf(
        "User-Agent" to "Fetcher/" + BuildConfig.VERSION_NAME
    )
)