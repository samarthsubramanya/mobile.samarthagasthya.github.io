package dev.samarthagasthya.mobile.components

import androidx.compose.runtime.Composable
import org.jetbrains.compose.web.dom.Div

@Composable
internal fun SkeletonView() {
    Div(attrs = { classes("mobile-shell") }) {
        Div(attrs = { classes("skeleton-hero") }) {
            Div(attrs = { classes("skeleton", "skeleton-avatar") })
            Div(attrs = { classes("skeleton", "skeleton-line", "skeleton-line-wide") })
            Div(attrs = { classes("skeleton", "skeleton-line") })
            Div(attrs = { classes("skeleton", "skeleton-line", "skeleton-line-short") })
        }
        repeat(3) {
            Div(attrs = { classes("skeleton", "skeleton-card") })
        }
    }
}
