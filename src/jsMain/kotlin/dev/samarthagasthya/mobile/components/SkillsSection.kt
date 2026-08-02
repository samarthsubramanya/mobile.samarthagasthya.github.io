package dev.samarthagasthya.mobile.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import dev.samarthagasthya.mobile.NavIcon
import dev.samarthagasthya.mobile.SectionScrollState
import dev.samarthagasthya.mobile.SkillCategory
import dev.samarthagasthya.mobile.iconPath
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text

@Composable
internal fun SkillsSection(skills: List<SkillCategory>, scrollState: SectionScrollState) {
    SectionShell("skills", "Skills", "Tooling organized for quick scanning", scrollState) {
        val expanded = remember { mutableStateOf(skills.firstOrNull()?.category) }

        Div(attrs = { classes("details-stack") }) {
            skills.forEach { category -> SkillGroup(category, expanded) }
        }
    }
}

@Composable
private fun SkillGroup(category: SkillCategory, expanded: MutableState<String?>) {
    val isExpanded = expanded.value == category.category

    Div(attrs = {
        classes(buildList {
            add("skill-group")
            if (isExpanded) add("expanded")
        })
    }) {
        Div(attrs = {
            classes("skill-summary")
            attr("role", "button")
            attr("aria-expanded", isExpanded.toString())
            onClick { expanded.value = if (isExpanded) null else category.category }
        }) {
            Span { Text(category.category) }
            Span(attrs = { classes("count-pill") }) { Text(category.items.size.toString()) }
            NavIcon("chevron", className = "chevron-icon")
        }

        if (isExpanded) {
            Div(attrs = { classes("skill-grid") }) {
                category.items.forEach { item ->
                    Div(attrs = { classes("skill-chip") }) {
                        LazyImage(iconPath(item.icon), item.name, "skill-icon")
                        Span { Text(item.name) }
                    }
                }
            }
        }
    }
}
