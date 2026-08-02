package dev.samarthagasthya.mobile

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import dev.samarthagasthya.mobile.components.CertificationsSection
import dev.samarthagasthya.mobile.components.ContactSection
import dev.samarthagasthya.mobile.components.EducationSection
import dev.samarthagasthya.mobile.components.ExperienceSection
import dev.samarthagasthya.mobile.components.Footer
import dev.samarthagasthya.mobile.components.Hero
import dev.samarthagasthya.mobile.components.ProjectsSection
import dev.samarthagasthya.mobile.components.SectionNav
import dev.samarthagasthya.mobile.components.SkeletonView
import dev.samarthagasthya.mobile.components.SkillsSection
import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.coroutines.delay
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.H1
import org.jetbrains.compose.web.dom.Main
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Text

internal val NAV_SECTIONS = listOf(
    Triple("Experience", "experience", "experience"),
    Triple("Projects", "projects", "projects"),
    Triple("Skills", "skills", "skills"),
    Triple("Education", "education", "education"),
    Triple("Certs", "certifications", "certs"),
    Triple("Contact", "contact", "contact"),
)

@Composable
internal fun App() {
    var uiState by remember { mutableStateOf<UiState>(UiState.Loading) }

    LaunchedEffect(Unit) {
        uiState = try {
            UiState.Success(fetchPortfolio())
        } catch (error: Throwable) {
            UiState.Error("Unable to load portfolio data. ${error.message ?: error}")
        }
    }

    when (val state = uiState) {
        is UiState.Loading -> SkeletonView()
        is UiState.Error -> ErrorView(state.message)
        is UiState.Success -> PortfolioView(state.data)
    }
}

@Composable
private fun ErrorView(message: String) {
    Div(attrs = { classes("error-state") }) {
        P(attrs = { classes("eyebrow") }) { Text("Data unavailable") }
        H1 { Text("Portfolio could not be loaded") }
        P { Text(message) }
    }
}

@Composable
private fun PortfolioView(data: PortfolioData) {
    val scrollY by rememberScrollY()
    val heroCompact = scrollY > 140.0
    val scrollState = rememberSectionScrollState(scrollY, NAV_SECTIONS.map { it.second })

    LaunchedEffect(Unit) {
        val hash = window.location.hash.removePrefix("#")
        if (hash.isNotBlank()) {
            delay(60)
            document.getElementById(hash)?.scrollIntoView(js("({ behavior: 'smooth', block: 'start' })"))
        }
    }

    Div(attrs = {
        id("top")
        classes("mobile-shell")
    }) {
        Hero(personal = data.personal, compact = heroCompact)

        Main(attrs = { classes("content") }) {
            ExperienceSection(data.experience, scrollState)
            ProjectsSection(data.projects, scrollState)
            SkillsSection(data.skills, scrollState)
            EducationSection(data.education, scrollState)
            CertificationsSection(data.certifications, data.badgeProviders, scrollState)
            ContactSection(scrollState)
            Footer()
        }

        SectionNav(activeSection = scrollState.activeSection)
        BackToTop(visible = scrollY > 480.0)
    }
}

@Composable
private fun BackToTop(visible: Boolean) {
    Div(attrs = {
        classes(buildList {
            add("back-to-top")
            if (visible) add("visible")
        })
        attr("role", "button")
        attr("aria-label", "Back to top")
        onClick {
            document.getElementById("top")?.scrollIntoView(js("({ behavior: 'smooth', block: 'start' })"))
        }
    }) {
        NavIcon("up", className = "nav-icon")
    }
}
