package dev.samarthagasthya.mobile.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import dev.samarthagasthya.mobile.Project
import dev.samarthagasthya.mobile.SectionScrollState
import dev.samarthagasthya.mobile.cleanHref
import org.jetbrains.compose.web.dom.A
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.H3
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Text
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.events.Event
import kotlin.math.roundToInt

@Composable
internal fun ProjectsSection(projects: List<Project>, scrollState: SectionScrollState) {
    SectionShell("projects", "Projects", "Swipe through selected work", scrollState) {
        val activeIndex = remember { mutableStateOf(0) }
        val trackRef = remember { mutableStateOf<HTMLDivElement?>(null) }

        Div(attrs = {
            classes("project-track")
            ref { element ->
                trackRef.value = element
                val listener: (Event) -> Unit = {
                    val cardWidth = element.scrollWidth.toDouble() / projects.size
                    if (cardWidth > 0) {
                        val index = (element.scrollLeft / cardWidth).roundToInt().coerceIn(0, projects.size - 1)
                        if (index != activeIndex.value) activeIndex.value = index
                    }
                }
                element.addEventListener("scroll", listener, js("({ passive: true })"))
                onDispose { element.removeEventListener("scroll", listener) }
            }
        }) {
            projects.forEach { project -> ProjectCard(project) }
        }

        if (projects.size > 1) {
            Div(attrs = { classes("carousel-dots") }) {
                projects.indices.forEach { index ->
                    Div(attrs = {
                        classes(buildList {
                            add("carousel-dot")
                            if (index == activeIndex.value) add("active")
                        })
                        attr("role", "button")
                        attr("aria-label", "Go to project ${index + 1}")
                        onClick {
                            trackRef.value?.let { element ->
                                val cardWidth = element.scrollWidth.toDouble() / projects.size
                                val options = js("({ behavior: 'smooth' })")
                                options.left = cardWidth * index
                                element.asDynamic().scrollTo(options)
                            }
                        }
                    })
                }
            }
        }
    }
}

@Composable
private fun ProjectCard(project: Project) {
    Div(attrs = { classes("project-card") }) {
        Div(attrs = { classes("project-media") }) {
            LazyImage(project.image.ifBlank { "/images/profile.webp" }, project.title, "project-image")
        }
        P(attrs = { classes("eyebrow") }) { Text(project.category) }
        H3(attrs = { classes("card-title") }) { Text(project.title) }
        P(attrs = { classes("body-copy") }) { Text(project.description) }
        ChipRow(project.technologies)
        BulletList(project.highlights.take(3))

        val liveHref = cleanHref(project.links.live)
        val codeHref = cleanHref(project.links.github)
        if (liveHref != null || codeHref != null) {
            Div(attrs = { classes("card-actions") }) {
                liveHref?.let { href ->
                    A(href = href, attrs = { classes("small-button"); configureExternalLink(href) }) { Text("Live") }
                }
                codeHref?.let { href ->
                    A(href = href, attrs = { classes("small-button"); configureExternalLink(href) }) { Text("Code") }
                }
            }
        }
    }
}
