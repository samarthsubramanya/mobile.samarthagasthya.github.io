package dev.samarthagasthya.mobile

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import kotlinx.browser.document
import kotlinx.browser.window
import org.w3c.dom.events.Event

/** Tracks `window.scrollY`, updated on every scroll event. */
@Composable
internal fun rememberScrollY(): State<Double> {
    val scrollY = remember { mutableStateOf(window.scrollY) }
    DisposableEffect(Unit) {
        val listener: (Event) -> Unit = { scrollY.value = window.scrollY }
        window.addEventListener("scroll", listener, js("({ passive: true })"))
        onDispose { window.removeEventListener("scroll", listener) }
    }
    return scrollY
}

/**
 * Holds which section is "active" for the bottom nav and which sections have been scrolled into
 * view at least once (for the reveal animation). Recomputed synchronously from [scrollY] on every
 * recomposition rather than via per-section IntersectionObservers or an async LaunchedEffect:
 * IntersectionObservers only sample at rest between frames and miss sections entirely during a
 * fast fling or anchor-jump; an async effect keyed on scrollY can likewise fall behind when scroll
 * events fire faster than it can complete, leaving the nav pointing at a stale section. Reading
 * `getBoundingClientRect` directly in the composable body keeps this always in sync with the
 * exact scrollY being composed, and the handful of calls involved is cheap.
 */
internal class SectionScrollState(
    val activeSection: String,
    val revealedSections: Set<String>,
)

@Composable
internal fun rememberSectionScrollState(scrollY: Double, sectionIds: List<String>): SectionScrollState {
    val revealedSections = remember { mutableStateOf(emptySet<String>()) }

    val viewportHeight = window.innerHeight.toDouble()
    val bandY = viewportHeight * 0.42
    val revealY = viewportHeight - 60.0

    var activeSection = sectionIds.first()
    val newlyRevealed = mutableSetOf<String>()
    sectionIds.forEach { id ->
        val rect = document.getElementById(id)?.getBoundingClientRect() ?: return@forEach
        if (rect.top <= bandY && rect.bottom >= bandY) activeSection = id
        if (rect.top < revealY) newlyRevealed += id
    }

    if (!revealedSections.value.containsAll(newlyRevealed)) {
        val merged = revealedSections.value + newlyRevealed
        SideEffect { revealedSections.value = merged }
    }

    return SectionScrollState(activeSection, revealedSections.value + newlyRevealed)
}
