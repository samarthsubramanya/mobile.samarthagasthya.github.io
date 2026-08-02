package dev.samarthagasthya.mobile.components

import androidx.compose.runtime.Composable
import dev.samarthagasthya.mobile.NavIcon
import dev.samarthagasthya.mobile.PersonalInfo
import dev.samarthagasthya.mobile.cleanHref
import dev.samarthagasthya.mobile.displayName
import org.jetbrains.compose.web.dom.A
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.H1
import org.jetbrains.compose.web.dom.Header
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.TagElement
import org.jetbrains.compose.web.dom.Text
import org.w3c.dom.HTMLElement

@Composable
internal fun Hero(personal: PersonalInfo, compact: Boolean) {
    val name = displayName(personal.name)
    val nameSuffix = personal.name.removePrefix(name).trim()

    Header(attrs = {
        classes(buildList {
            add("hero")
            if (compact) add("compact")
        })
    }) {
        Div(attrs = { classes("profile-row") }) {
            LazyImage("/images/profile.webp", personal.name, "avatar", eager = true)
            Div(attrs = { classes("profile-meta") }) {
                P(attrs = { classes("eyebrow") }) { Text("Portfolio Mobile Version") }
                P(attrs = { classes("location-line") }) { Text(personal.location) }
            }
        }

        H1(attrs = { classes("hero-title") }) { Text(name) }
        if (nameSuffix.isNotBlank()) {
            P(attrs = { classes("name-suffix") }) { Text(nameSuffix) }
        }
        P(attrs = { classes("hero-role") }) { Text(personal.title) }
        P(attrs = { classes("hero-tagline") }) { Text(personal.tagline) }
        P(attrs = { classes("hero-bio") }) { Text(personal.bio) }

        Div(attrs = { classes("stat-grid") }) {
            StatPill("4+ YOE", "Production apps")
            StatPill("MSCS", "Arizona State")
            StatPill("Kotlin", "Mobile systems")
        }

        Div(attrs = { classes("cta-row") }) {
            cleanHref(personal.email)?.let { email ->
                A(href = "mailto:$email", attrs = { classes("button") }) { Text("Email") }
            }
            cleanHref(personal.social.github)?.let { href ->
                A(href = href, attrs = { classes("button"); configureExternalLink(href) }) { Text("GitHub") }
            }
            cleanHref(personal.social.linkedin)?.let { href ->
                A(href = href, attrs = { classes("button"); configureExternalLink(href) }) { Text("LinkedIn") }
            }
        }

        P(attrs = { classes("looking-for") }) { Text(personal.lookingFor) }
    }
}

@Composable
private fun StatPill(value: String, label: String) {
    Div(attrs = { classes("stat-pill") }) {
        TagElement<HTMLElement>("strong", applyAttrs = null) { Text(value) }
        Span { Text(label) }
    }
}
