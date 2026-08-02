package dev.samarthagasthya.mobile.components

import androidx.compose.runtime.Composable
import dev.samarthagasthya.mobile.SectionScrollState
import org.jetbrains.compose.web.dom.ContentBuilder
import org.jetbrains.compose.web.dom.H2
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Section
import org.jetbrains.compose.web.dom.Text
import org.w3c.dom.HTMLElement

@Composable
internal fun SectionShell(
    id: String,
    eyebrow: String,
    title: String,
    scrollState: SectionScrollState,
    content: ContentBuilder<HTMLElement>,
) {
    val visible = id in scrollState.revealedSections

    Section(attrs = {
        this.id(id)
        classes(buildList {
            add("section")
            add("reveal")
            if (visible) add("reveal-visible")
        })
    }) {
        P(attrs = { classes("eyebrow") }) { Text(eyebrow) }
        H2(attrs = { classes("section-title") }) { Text(title) }
        content()
    }
}
