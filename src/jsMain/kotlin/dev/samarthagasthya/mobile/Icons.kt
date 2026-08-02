package dev.samarthagasthya.mobile

import androidx.compose.runtime.Composable
import org.jetbrains.compose.web.ExperimentalComposeWebSvgApi
import org.jetbrains.compose.web.svg.Circle
import org.jetbrains.compose.web.svg.Path
import org.jetbrains.compose.web.svg.Svg
import org.jetbrains.compose.web.svg.d

@OptIn(ExperimentalComposeWebSvgApi::class)
@Composable
internal fun NavIcon(name: String, className: String = "nav-icon") {
    Svg(
        viewBox = "0 0 24 24",
        attrs = {
            classes(className)
            attr("aria-hidden", "true")
            attr("focusable", "false")
            attr("fill", "none")
            attr("stroke", "currentColor")
            attr("stroke-width", "2")
            attr("stroke-linecap", "round")
            attr("stroke-linejoin", "round")
        },
    ) {
        when (name) {
            "experience" -> {
                Circle(cx = 18, cy = 18, r = 3)
                Circle(cx = 6, cy = 6, r = 3)
                Path(d = "M6 9v12")
                Path(d = "M9 18h6")
                Path(d = "M9 6h5a4 4 0 0 1 4 4v5")
            }
            "projects" -> {
                Path(d = "M3 7h6l2 3h10v7a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2Z")
                Path(d = "M3 7V5a2 2 0 0 1 2-2h4l2 4")
            }
            "skills" -> {
                Circle(cx = 12, cy = 12, r = 3)
                Path(
                    d = "M19.4 15a1.7 1.7 0 0 0 .34 1.88l.06.06a2 2 0 1 1-2.83 2.83l-.06-.06A1.7 1.7 0 0 0 15 19.4a1.7 1.7 0 0 0-1 .6 1.7 1.7 0 0 0-.4 1.08V21a2 2 0 1 1-4 0v-.09A1.7 1.7 0 0 0 8.6 19.4a1.7 1.7 0 0 0-1.88.34l-.06.06a2 2 0 1 1-2.83-2.83l.06-.06A1.7 1.7 0 0 0 4.6 15a1.7 1.7 0 0 0-.6-1 1.7 1.7 0 0 0-1.08-.4H3a2 2 0 1 1 0-4h.09A1.7 1.7 0 0 0 4.6 8.6a1.7 1.7 0 0 0-.34-1.88l-.06-.06a2 2 0 1 1 2.83-2.83l.06.06A1.7 1.7 0 0 0 9 4.6a1.7 1.7 0 0 0 1-.6 1.7 1.7 0 0 0 .4-1.08V3a2 2 0 1 1 4 0v.09A1.7 1.7 0 0 0 15.4 4.6a1.7 1.7 0 0 0 1.88-.34l.06-.06a2 2 0 1 1 2.83 2.83l-.06.06A1.7 1.7 0 0 0 19.4 9c.2.37.52.69.9.9.31.17.67.25 1.03.25H21a2 2 0 1 1 0 4h-.09A1.7 1.7 0 0 0 19.4 15Z"
                )
            }
            "education" -> {
                Path(d = "M3 8l9-4 9 4-9 4Z")
                Path(d = "M7 10v5c0 1.2 2.2 3 5 3s5-1.8 5-3v-5")
                Path(d = "M21 8v6")
            }
            "certs" -> {
                Path(d = "M12 3l2.35 4.76 5.25.76-3.8 3.7.9 5.23L12 15l-4.7 2.45.9-5.23-3.8-3.7 5.25-.76Z")
                Path(d = "M9 19l3 2 3-2")
            }
            "email" -> {
                Path(d = "M4 4h16v16H4Z")
                Path(d = "m4 7 8 6 8-6")
            }
            "subject" -> {
                Path(d = "M5 4h14v16H5Z")
                Path(d = "M8 8h8")
                Path(d = "M8 12h8")
                Path(d = "M8 16h5")
            }
            "message" -> {
                Path(d = "M4 5h16v11H8l-4 4Z")
                Path(d = "M8 9h8")
                Path(d = "M8 13h5")
            }
            "send" -> {
                Path(d = "m22 2-7 20-4-9-9-4Z")
                Path(d = "M22 2 11 13")
            }
            "success" -> {
                Circle(cx = 12, cy = 12, r = 10)
                Path(d = "m9 12 2 2 4-5")
            }
            "github" -> {
                Path(d = "M9 19c-4.5 1.5-5-2-7-3")
                Path(
                    d = "M15 22v-4a4.8 4.8 0 0 0-1-3.5c3 0 6-2 6-5.5 0-1.25-.35-2.48-1-3.5.28-1.15.28-2.35 0-3.5 0 0-1 0-3 1.5a10.5 10.5 0 0 0-6 0C8 2 7 2 7 2c-.3 1.15-.3 2.35 0 3.5A5.4 5.4 0 0 0 6 9c0 3.5 3 5.5 6 5.5-.39.49-.68 1.05-.85 1.65-.17.6-.22 1.23-.15 1.85v4"
                )
            }
            "chevron" -> {
                Path(d = "m6 9 6 6 6-6")
            }
            "up" -> {
                Path(d = "m18 15-6-6-6 6")
            }
            else -> {
                Path(d = "M8 2h8a2 2 0 0 1 2 2v16a2 2 0 0 1-2 2H8a2 2 0 0 1-2-2V4a2 2 0 0 1 2-2Z")
                Path(d = "M10 18h4")
            }
        }
    }
}
