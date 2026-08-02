package dev.samarthagasthya.mobile.components

import androidx.compose.runtime.Composable
import org.jetbrains.compose.web.attributes.AttrsScope
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Img
import org.jetbrains.compose.web.dom.Li
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text
import org.jetbrains.compose.web.dom.Ul
import org.w3c.dom.HTMLAnchorElement

@Composable
internal fun BulletList(items: List<String>) {
    val filtered = items.filter { it.isNotBlank() }
    if (filtered.isEmpty()) return

    Ul(attrs = { classes("bullet-list") }) {
        filtered.forEach { item -> Li { Text(item) } }
    }
}

@Composable
internal fun ChipRow(items: List<String>) {
    val filtered = items.filter { it.isNotBlank() }
    if (filtered.isEmpty()) return

    Div(attrs = { classes("chip-row") }) {
        filtered.forEach { item -> Span(attrs = { classes("mini-chip") }) { Text(item) } }
    }
}

@Composable
internal fun LazyImage(src: String, alt: String, className: String, eager: Boolean = false) {
    Img(src = src, alt = alt, attrs = {
        classes(className)
        attr("loading", if (eager) "eager" else "lazy")
        attr("decoding", "async")
    })
}

internal fun AttrsScope<HTMLAnchorElement>.configureExternalLink(href: String) {
    if (href.startsWith("http")) {
        attr("target", "_blank")
        attr("rel", "noreferrer noopener")
    }
}
