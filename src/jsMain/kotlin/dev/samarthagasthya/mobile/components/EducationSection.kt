package dev.samarthagasthya.mobile.components

import androidx.compose.runtime.Composable
import dev.samarthagasthya.mobile.Education
import dev.samarthagasthya.mobile.SectionScrollState
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.H3
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Text

@Composable
internal fun EducationSection(education: List<Education>, scrollState: SectionScrollState) {
    SectionShell("education", "Education", "Academic foundation", scrollState) {
        Div(attrs = { classes("card-stack") }) {
            education.forEach { item -> EducationCard(item) }
        }
    }
}

@Composable
private fun EducationCard(item: Education) {
    val meta = listOf(item.location, "GPA ${item.gpa}").filter { it.isNotBlank() }.joinToString(" - ")

    Div(attrs = { classes("info-card") }) {
        P(attrs = { classes("eyebrow") }) { Text(item.period) }
        H3(attrs = { classes("card-title") }) { Text(item.degree) }
        P(attrs = { classes("body-copy", "strong-copy") }) { Text(item.institution) }
        P(attrs = { classes("meta-line") }) { Text(meta) }
        BulletList(item.highlights)
    }
}
