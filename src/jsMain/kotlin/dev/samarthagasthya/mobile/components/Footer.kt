package dev.samarthagasthya.mobile.components

import androidx.compose.runtime.Composable
import dev.samarthagasthya.mobile.NavIcon
import org.jetbrains.compose.web.dom.A
import org.jetbrains.compose.web.dom.Footer as FooterTag
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Text

@Composable
internal fun Footer() {
    FooterTag(attrs = { classes("footer-section") }) {
        P { Text("© Built using Kotlin Multiplatform") }
        P { Text("Hosted on Vercel") }
        P(attrs = { classes("footer-source") }) {
            Text("Find Source at ")
            A(
                href = "https://github.com/samarthsubramanya/mobile.samarthagasthya.github.io",
                attrs = {
                    classes("footer-source-link")
                    configureExternalLink("https://github.com")
                },
            ) {
                NavIcon("github", className = "footer-github-icon")
                Text("samarthsubramanya/mobile.samarthagasthya.github.io")
            }
        }
        P { Text("Icons fetched from: Icons8. Logos are trademarks of their respective owners.") }
        P { Text("Few Cover Images generated using Gemini Nano Banana Pro") }
        P(attrs = { classes("footer-version") }) { Text("Version: 2.1.0 (Compose Multiplatform)") }
    }
}
