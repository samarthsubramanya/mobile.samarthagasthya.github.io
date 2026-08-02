package dev.samarthagasthya.mobile

import org.jetbrains.compose.web.renderComposable

fun main() {
    clearLegacyBrowserCaches()
    renderComposable(rootElementId = "root") {
        App()
    }
}
