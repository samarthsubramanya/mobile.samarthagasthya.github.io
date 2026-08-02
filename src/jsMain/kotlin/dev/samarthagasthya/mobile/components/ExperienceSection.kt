package dev.samarthagasthya.mobile.components

import androidx.compose.runtime.Composable
import dev.samarthagasthya.mobile.Experience
import dev.samarthagasthya.mobile.SectionScrollState
import dev.samarthagasthya.mobile.cleanHref
import org.jetbrains.compose.web.dom.A
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text

@Composable
internal fun ExperienceSection(experience: List<Experience>, scrollState: SectionScrollState) {
    SectionShell("experience", "Experience", "Recent roles and impact", scrollState) {
        Div(attrs = { classes("timeline-list") }) {
            experience.forEach { item -> TimelineCard(item) }
        }
    }
}

@Composable
private fun TimelineCard(item: Experience) {
    val company = item.company.ifBlank { "Independent" }
    val meta = listOf(item.type, item.location).filter { it.isNotBlank() }.joinToString(" - ")

    Div(attrs = { classes("timeline-card") }) {
        Div(attrs = { classes("timeline-summary") }) {
            Span(attrs = { classes("timeline-period") }) { Text(item.period) }
            Span(attrs = { classes("timeline-title") }) { Text(item.title) }
            Span(attrs = { classes("timeline-company") }) { Text(company) }
        }
        if (meta.isNotBlank()) {
            P(attrs = { classes("meta-line") }) { Text(meta) }
        }
        P(attrs = { classes("body-copy") }) { Text(item.description) }
        BulletList(item.achievements)
        cleanHref(item.url)?.let { href ->
            A(href = href, attrs = {
                classes("text-link")
                configureExternalLink(href)
            }) { Text("Company") }
        }
    }
}
