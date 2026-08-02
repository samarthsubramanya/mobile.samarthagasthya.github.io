package dev.samarthagasthya.mobile.components

import androidx.compose.runtime.Composable
import dev.samarthagasthya.mobile.NAV_SECTIONS
import dev.samarthagasthya.mobile.NavIcon
import org.jetbrains.compose.web.dom.A
import org.jetbrains.compose.web.dom.Nav
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text

@Composable
internal fun SectionNav(activeSection: String) {
    Nav(attrs = {
        classes("quick-nav")
        attr("aria-label", "Portfolio sections")
    }) {
        NAV_SECTIONS.forEach { (label, sectionId, icon) ->
            val isActive = sectionId == activeSection
            A(href = "#$sectionId", attrs = {
                classes(buildList {
                    add("nav-chip")
                    if (isActive) add("active")
                })
                attr("aria-label", label)
                attr("aria-current", if (isActive) "true" else "false")
                title(label)
            }) {
                NavIcon(icon)
                Span(attrs = { classes("visually-hidden") }) { Text(label) }
            }
        }
    }
}
