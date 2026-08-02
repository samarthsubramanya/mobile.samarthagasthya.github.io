package dev.samarthagasthya.mobile.components

import androidx.compose.runtime.Composable
import dev.samarthagasthya.mobile.BadgeProvider
import dev.samarthagasthya.mobile.Certification
import dev.samarthagasthya.mobile.SectionScrollState
import dev.samarthagasthya.mobile.cleanHref
import org.jetbrains.compose.web.dom.A
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.H3
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text

@Composable
internal fun CertificationsSection(
    certifications: List<Certification>,
    providers: List<BadgeProvider>,
    scrollState: SectionScrollState,
) {
    SectionShell("certifications", "Certifications", "Current credentials and learning tracks", scrollState) {
        Div(attrs = { classes("card-stack") }) {
            certifications.forEach { cert -> CertificationCard(cert) }
        }
        if (providers.isNotEmpty()) {
            BadgeProviderStrip(providers)
        }
    }
}

@Composable
private fun CertificationCard(cert: Certification) {
    val eyebrow = listOf(cert.provider, cert.date).filter { it.isNotBlank() }.joinToString(" - ")

    Div(attrs = { classes("info-card") }) {
        P(attrs = { classes("eyebrow") }) { Text(eyebrow) }
        H3(attrs = { classes("card-title") }) { Text(cert.name) }
        P(attrs = { classes("body-copy") }) { Text(cert.description) }
        ChipRow(cert.skills)
        cleanHref(cert.badgeUrl)?.let { href ->
            A(href = href, attrs = { classes("text-link"); configureExternalLink(href) }) { Text("Credential") }
        }
    }
}

@Composable
private fun ProviderBadge(provider: BadgeProvider) {
    LazyImage(provider.logo.ifBlank { "/images/profile.webp" }, provider.name, "provider-logo")
    Span { Text(provider.name) }
}

@Composable
private fun BadgeProviderStrip(providers: List<BadgeProvider>) {
    Div(attrs = { classes("provider-strip") }) {
        providers.forEach { provider ->
            val href = cleanHref(provider.url)

            if (href == null) {
                Span(attrs = { classes("provider-pill") }) {
                    Div(attrs = { classes("provider-content") }) { ProviderBadge(provider) }
                }
            } else {
                A(href = href, attrs = { classes("provider-pill"); configureExternalLink(href) }) {
                    Div(attrs = { classes("provider-content") }) { ProviderBadge(provider) }
                }
            }
        }
    }
}
