package dev.samarthagasthya.mobile

import kotlinx.browser.document
import kotlinx.browser.window
import org.w3c.dom.HTMLAnchorElement
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLImageElement
import org.w3c.dom.Node
import kotlin.js.JSON

private const val PORTFOLIO_DATA_URL =
    "https://raw.githubusercontent.com/samarthsubramanya/samarthagasthya.github.io/refs/heads/revamp_v4/src/data/portfolioData.json"

private data class PortfolioData(
    val personal: PersonalInfo,
    val education: List<Education>,
    val skills: List<SkillCategory>,
    val experience: List<Experience>,
    val projects: List<Project>,
    val certifications: List<Certification>,
    val badgeProviders: List<BadgeProvider>,
)

private data class PersonalInfo(
    val name: String,
    val title: String,
    val tagline: String,
    val bio: String,
    val email: String,
    val phone: String,
    val location: String,
    val lookingFor: String,
    val social: SocialLinks,
)

private data class SocialLinks(
    val github: String,
    val linkedin: String,
    val twitter: String,
)

private data class Education(
    val degree: String,
    val institution: String,
    val location: String,
    val period: String,
    val gpa: String,
    val highlights: List<String>,
)

private data class SkillCategory(
    val category: String,
    val items: List<SkillItem>,
)

private data class SkillItem(
    val name: String,
    val icon: String,
)

private data class Experience(
    val title: String,
    val company: String,
    val url: String,
    val location: String,
    val period: String,
    val type: String,
    val description: String,
    val achievements: List<String>,
)

private data class Project(
    val title: String,
    val category: String,
    val description: String,
    val technologies: List<String>,
    val image: String,
    val links: ProjectLinks,
    val highlights: List<String>,
)

private data class ProjectLinks(
    val live: String,
    val github: String,
)

private data class Certification(
    val name: String,
    val provider: String,
    val issuer: String,
    val date: String,
    val credentialId: String,
    val description: String,
    val skills: List<String>,
    val badgeUrl: String,
)

private data class BadgeProvider(
    val name: String,
    val logo: String,
    val url: String,
    val description: String,
)

fun main() {
    val root = document.getElementById("root") as? HTMLElement ?: return
    clearLegacyBrowserCaches()
    root.innerHTML = ""
    root.appendChild(loadingView())

    window.fetch(PORTFOLIO_DATA_URL)
        .then { response -> response.text() }
        .then { text ->
            renderPortfolio(root, parsePortfolio(JSON.parse<dynamic>(text)))
            null
        }
        .catch { error ->
            renderError(root, "Unable to load portfolio data. ${error}")
            null
        }
}

private fun clearLegacyBrowserCaches() {
    js(
        """
        (function () {
          if ('serviceWorker' in navigator) {
            navigator.serviceWorker.getRegistrations()
              .then(function (registrations) {
                registrations.forEach(function (registration) { registration.unregister(); });
              })
              .catch(function () {});
          }

          if ('caches' in window) {
            caches.keys()
              .then(function (keys) {
                return Promise.all(keys.map(function (key) { return caches.delete(key); }));
              })
              .catch(function () {});
          }
        })();
        """
    )
}

private fun parsePortfolio(raw: dynamic): PortfolioData =
    PortfolioData(
        personal = parsePersonal(raw.personal),
        education = dynamicList(raw.education, ::parseEducation),
        skills = dynamicList(raw.skills, ::parseSkillCategory),
        experience = dynamicList(raw.experience, ::parseExperience),
        projects = dynamicList(raw.projects, ::parseProject),
        certifications = dynamicList(raw.certifications, ::parseCertification),
        badgeProviders = dynamicList(raw.badgeProviders, ::parseBadgeProvider),
    )

private fun parsePersonal(raw: dynamic): PersonalInfo =
    PersonalInfo(
        name = stringProp(raw, "name"),
        title = stringProp(raw, "title"),
        tagline = stringProp(raw, "tagline"),
        bio = stringProp(raw, "bio"),
        email = stringProp(raw, "email"),
        phone = stringProp(raw, "phone"),
        location = stringProp(raw, "location"),
        lookingFor = stringProp(raw, "lookingFor"),
        social = parseSocial(raw.social),
    )

private fun parseSocial(raw: dynamic): SocialLinks =
    SocialLinks(
        github = stringProp(raw, "github"),
        linkedin = stringProp(raw, "linkedin"),
        twitter = stringProp(raw, "twitter"),
    )

private fun parseEducation(raw: dynamic): Education =
    Education(
        degree = stringProp(raw, "degree"),
        institution = stringProp(raw, "institution"),
        location = stringProp(raw, "location"),
        period = stringProp(raw, "period"),
        gpa = stringProp(raw, "gpa"),
        highlights = stringList(raw.highlights),
    )

private fun parseSkillCategory(raw: dynamic): SkillCategory =
    SkillCategory(
        category = stringProp(raw, "category"),
        items = dynamicList(raw.items, ::parseSkillItem),
    )

private fun parseSkillItem(raw: dynamic): SkillItem =
    SkillItem(
        name = stringProp(raw, "name"),
        icon = stringProp(raw, "icon"),
    )

private fun parseExperience(raw: dynamic): Experience =
    Experience(
        title = stringProp(raw, "title"),
        company = stringProp(raw, "company"),
        url = stringProp(raw, "url"),
        location = stringProp(raw, "location"),
        period = stringProp(raw, "period"),
        type = stringProp(raw, "type"),
        description = stringProp(raw, "description"),
        achievements = stringList(raw.achievements),
    )

private fun parseProject(raw: dynamic): Project =
    Project(
        title = stringProp(raw, "title"),
        category = stringProp(raw, "category"),
        description = stringProp(raw, "description"),
        technologies = stringList(raw.technologies),
        image = stringProp(raw, "image"),
        links = parseProjectLinks(raw.links),
        highlights = stringList(raw.highlights),
    )

private fun parseProjectLinks(raw: dynamic): ProjectLinks =
    ProjectLinks(
        live = stringProp(raw, "live"),
        github = stringProp(raw, "github"),
    )

private fun parseCertification(raw: dynamic): Certification =
    Certification(
        name = stringProp(raw, "name"),
        provider = stringProp(raw, "provider"),
        issuer = stringProp(raw, "issuer"),
        date = stringProp(raw, "date"),
        credentialId = stringProp(raw, "credentialId"),
        description = stringProp(raw, "description"),
        skills = stringList(raw.skills),
        badgeUrl = stringProp(raw, "badgeUrl"),
    )

private fun parseBadgeProvider(raw: dynamic): BadgeProvider =
    BadgeProvider(
        name = stringProp(raw, "name"),
        logo = stringProp(raw, "logo"),
        url = stringProp(raw, "url"),
        description = stringProp(raw, "description"),
    )

private fun <T> dynamicList(source: dynamic, mapper: (dynamic) -> T): List<T> {
    if (source == null) return emptyList()

    val length = (source.length as Number).toInt()
    val items = mutableListOf<T>()
    for (index in 0 until length) {
        items += mapper(source[index])
    }
    return items
}

private fun stringList(source: dynamic): List<String> =
    dynamicList(source) { value ->
        if (value == null) "" else value.toString().trim()
    }.filter { it.isNotBlank() }

private fun stringProp(source: dynamic, key: String): String {
    if (source == null) return ""

    val value = source[key]
    return if (value == null) "" else value.toString().trim()
}

private fun renderPortfolio(root: HTMLElement, data: PortfolioData) {
    root.innerHTML = ""

    val shell = el("div", "mobile-shell")
    shell.id = "top"
    shell.appendChild(hero(data.personal))

    val main = el("main", "content")
    main.appendAll(
        experienceSection(data.experience),
        projectsSection(data.projects),
        skillsSection(data.skills),
        educationSection(data.education),
        certificationsSection(data.certifications, data.badgeProviders),
        contactSection(),
        footerSection(),
    )

    shell.appendChild(main)
    shell.appendChild(sectionDock(data.personal))
    root.appendChild(shell)
    window.setTimeout({ scrollToCurrentHash() }, 0)
}

private fun loadingView(): HTMLElement =
    el("div", "loading-state").appendAll(
        el("div", "loading-mark", "SA"),
        el("p", "loading-copy", "Loading mobile portfolio"),
    )

private fun scrollToCurrentHash() {
    val target = window.location.hash.removePrefix("#")
    if (target.isBlank()) return

    document.getElementById(target)?.asDynamic()?.scrollIntoView(
        js("{ behavior: 'smooth', block: 'start' }")
    )
}

private fun renderError(root: HTMLElement, message: String) {
    root.innerHTML = ""
    root.appendChild(
        el("div", "error-state").appendAll(
            el("p", "eyebrow", "Data unavailable"),
            el("h1", "", "Portfolio could not be loaded"),
            el("p", "", message),
        )
    )
}

private fun hero(personal: PersonalInfo): HTMLElement {
    val header = el("header", "hero")
    val displayName = displayName(personal.name)
    val nameSuffix = personal.name.removePrefix(displayName).trim()

    val profileRow = el("div", "profile-row").appendAll(
        image("/images/profile.webp", personal.name, "avatar", eager = true),
        el("div", "profile-meta").appendAll(
            el("p", "eyebrow", "Portfolio Mobile Version"),
            el("p", "location-line", personal.location),
        )
    )

    val statGrid = el("div", "stat-grid").appendAll(
        statPill("4+ YOE", "Production apps"),
        statPill("MSCS", "Arizona State"),
        statPill("Kotlin", "Mobile systems"),
    )

    val actionRow = el("div", "cta-row")
    cleanHref(personal.email)?.let { actionRow.appendChild(anchor("Email", "mailto:$it", "button")) }
    cleanHref(personal.social.github)?.let { actionRow.appendChild(anchor("GitHub", it, "button")) }
    cleanHref(personal.social.linkedin)?.let { actionRow.appendChild(anchor("LinkedIn", it, "button")) }

    header.appendAll(
        profileRow,
        el("h1", "hero-title", displayName),
    )
    if (nameSuffix.isNotBlank()) {
        header.appendChild(el("p", "name-suffix", nameSuffix))
    }
    header.appendAll(
        el("p", "hero-role", personal.title),
        el("p", "hero-tagline", personal.tagline),
        el("p", "hero-bio", personal.bio),
        statGrid,
        actionRow,
        el("p", "looking-for", personal.lookingFor),
    )

    return header
}

private fun sectionDock(personal: PersonalInfo): HTMLElement {
    val nav = el("nav", "quick-nav")
    nav.setAttribute("aria-label", "Portfolio sections")
    listOf(
        Triple("Experience", "#experience", "experience"),
        Triple("Projects", "#projects", "projects"),
        Triple("Skills", "#skills", "skills"),
        Triple("Education", "#education", "education"),
        Triple("Certs", "#certifications", "certs"),
        Triple("Contact", "#contact", "contact"),
    ).forEach { (label, href, icon) ->
        nav.appendChild(iconAnchor(label, href, icon, "nav-chip"))
    }
    return nav
}

private fun experienceSection(experience: List<Experience>): HTMLElement {
    val section = section("experience", "Experience", "Recent roles and impact")
    val list = el("div", "timeline-list")

    experience.forEach { item ->
        val card = el("article", "timeline-card")
        val company = if (item.company.isBlank()) "Independent" else item.company
        val summary = el("div", "timeline-summary").appendAll(
            el("span", "timeline-period", item.period),
            el("span", "timeline-title", item.title),
            el("span", "timeline-company", company),
        )

        val meta = listOf(item.type, item.location).filter { it.isNotBlank() }.joinToString(" - ")
        val achievements = bulletList(item.achievements)

        card.appendAll(
            summary,
            el("p", "meta-line", meta),
            el("p", "body-copy", item.description),
        )
        if (achievements.hasChildNodes()) card.appendChild(achievements)
        cleanHref(item.url)?.let { card.appendChild(anchor("Company", it, "text-link")) }

        list.appendChild(card)
    }

    section.appendChild(list)
    return section
}

private fun projectsSection(projects: List<Project>): HTMLElement {
    val section = section("projects", "Projects", "Swipe through selected work")
    val track = el("div", "project-track")

    projects.forEach { project ->
        val card = el("article", "project-card")

        card.appendAll(
            el("div", "project-media").appendChildReturn(
                image(project.image.ifBlank { "/images/profile.webp" }, project.title, "project-image")
            ),
            el("p", "eyebrow", project.category),
            el("h3", "card-title", project.title),
            el("p", "body-copy", project.description),
            chipRow(project.technologies),
        )

        val highlights = bulletList(project.highlights.take(3))
        if (highlights.hasChildNodes()) card.appendChild(highlights)

        val actions = el("div", "card-actions")
        cleanHref(project.links.live)?.let { actions.appendChild(anchor("Live", it, "small-button")) }
        cleanHref(project.links.github)?.let { actions.appendChild(anchor("Code", it, "small-button")) }
        if (actions.hasChildNodes()) card.appendChild(actions)

        track.appendChild(card)
    }

    section.appendChild(track)
    return section
}

private fun skillsSection(skills: List<SkillCategory>): HTMLElement {
    val section = section("skills", "Skills", "Tooling organized for quick scanning")
    val stack = el("div", "details-stack")

    skills.forEach { category ->
        val group = el("article", "skill-group")

        group.appendChild(
            el("div", "skill-summary").appendAll(
                el("span", "", category.category),
                el("span", "count-pill", category.items.size.toString()),
            )
        )

        val grid = el("div", "skill-grid")
        category.items.forEach { item ->
            grid.appendChild(
                el("div", "skill-chip").appendAll(
                    image(iconPath(item.icon), item.name, "skill-icon"),
                    el("span", "", item.name),
                )
            )
        }
        group.appendChild(grid)
        stack.appendChild(group)
    }

    section.appendChild(stack)
    return section
}

private fun educationSection(education: List<Education>): HTMLElement {
    val section = section("education", "Education", "Academic foundation")
    val stack = el("div", "card-stack")

    education.forEach { item ->
        val card = el("article", "info-card").appendAll(
            el("p", "eyebrow", item.period),
            el("h3", "card-title", item.degree),
            el("p", "body-copy strong-copy", item.institution),
            el("p", "meta-line", listOf(item.location, "GPA ${item.gpa}").filter { it.isNotBlank() }.joinToString(" - ")),
        )
        val highlights = bulletList(item.highlights)
        if (highlights.hasChildNodes()) card.appendChild(highlights)
        stack.appendChild(card)
    }

    section.appendChild(stack)
    return section
}

private fun certificationsSection(
    certifications: List<Certification>,
    providers: List<BadgeProvider>,
): HTMLElement {
    val section = section("certifications", "Certifications", "Current credentials and learning tracks")
    val stack = el("div", "card-stack")

    certifications.forEach { cert ->
        val card = el("article", "info-card").appendAll(
            el("p", "eyebrow", listOf(cert.provider, cert.date).filter { it.isNotBlank() }.joinToString(" - ")),
            el("h3", "card-title", cert.name),
            el("p", "body-copy", cert.description),
            chipRow(cert.skills),
        )
        cleanHref(cert.badgeUrl)?.let { card.appendChild(anchor("Credential", it, "text-link")) }
        stack.appendChild(card)
    }

    section.appendChild(stack)
    if (providers.isNotEmpty()) section.appendChild(badgeProviderStrip(providers))
    return section
}

private fun badgeProviderStrip(providers: List<BadgeProvider>): HTMLElement {
    val wrapper = el("div", "provider-strip")
    providers.forEach { provider ->
        val href = cleanHref(provider.url)
        val providerContent = el("span", "provider-content").appendAll(
            image(provider.logo.ifBlank { "/images/profile.webp" }, provider.name, "provider-logo"),
            el("span", "", provider.name),
        )

        if (href == null) {
            wrapper.appendChild(el("span", "provider-pill").appendAll(providerContent))
        } else {
            wrapper.appendChild(anchorNode(href, "provider-pill", providerContent))
        }
    }
    return wrapper
}

private fun contactSection(): HTMLElement {
    val section = section("contact", "Contact", "Get In Touch")
    section.appendChild(
        el(
            "p",
            "section-copy",
            "Have a project or question? Fill out the form and I will get back to you as soon as possible.",
        )
    )

    val formShell = el("div", "contact-form-shell")
    val form = el("form", "contact-form")
    val status = el("p", "form-status")
    val emailInput = textInput("email", "email", "email", "your.email@example.com")
    val subjectInput = textInput("subject", "text", "subject", "What is this about?")
    val messageInput = messageInput()
    val submitButton = el("button", "submit-button").appendAll(
        navIcon("send"),
        el("span", "", "Send Message"),
    )
    submitButton.setAttribute("type", "submit")

    form.appendAll(
        formField("email", "Email Address", "email", emailInput),
        formField("subject", "Subject", "subject", subjectInput),
        formField("message", "Message", "message", messageInput),
        submitButton,
        status,
    )

    form.addEventListener("submit", { event ->
        event.preventDefault()

        val email = emailInput.asDynamic().value.toString().trim()
        val subject = subjectInput.asDynamic().value.toString().trim()
        val message = messageInput.asDynamic().value.toString().trim()

        if (email.isBlank() || subject.isBlank() || message.isBlank()) {
            setFormStatus(status, "Please fill out all fields before sending.", true)
            return@addEventListener
        }

        setFormStatus(status, "", false)
        setSubmitState(submitButton, true)

        submitToFormspree(
            email = email,
            subject = subject,
            bodyMessage = message,
            onSuccess = {
                formShell.innerHTML = ""
                formShell.appendChild(successMessage())
            },
            onError = { errorMessage ->
                setSubmitState(submitButton, false)
                setFormStatus(status, errorMessage, true)
            },
        )
    })

    formShell.appendChild(form)
    section.appendChild(formShell)
    return section
}

private fun footerSection(): HTMLElement {
    val footer = el("footer", "footer-section")
    val sourceLink = anchor(
        "samarthsubramanya/mobile.samarthagasthya.github.io",
        "https://github.com/samarthsubramanya/mobile.samarthagasthya.github.io",
        "footer-source-link",
    )
    val githubIcon = navIcon("github")
    githubIcon.asDynamic().setAttribute("class", "footer-github-icon")
    sourceLink.insertBefore(githubIcon, sourceLink.firstChild)

    footer.appendAll(
        el("p", "", "© Built using Kotlin MultiPlatform"),
        el("p", "", "Hosted on Vercel"),
        el("p", "footer-source").appendAll(
            document.createTextNode("Find Source at "),
            sourceLink,
        ),
        el("p", "", "Icons fetched from: Icons8. Logos are trademarks of their respective owners."),
        el("p", "", "Few Cover Images generated using Gemini Nano Banana Pro"),
        el("p", "footer-version", "Version: 2.0.4"),
    )
    return footer
}

private fun textInput(id: String, type: String, name: String, placeholder: String): HTMLElement {
    val input = el("input", "form-control")
    input.id = id
    input.setAttribute("type", type)
    input.setAttribute("name", name)
    input.setAttribute("placeholder", placeholder)
    input.setAttribute("required", "")
    return input
}

private fun messageInput(): HTMLElement {
    val textarea = el("textarea", "form-control message-control")
    textarea.id = "message"
    textarea.setAttribute("name", "message")
    textarea.setAttribute("placeholder", "Tell me more about your project or inquiry...")
    textarea.setAttribute("rows", "6")
    textarea.setAttribute("required", "")
    return textarea
}

private fun formField(id: String, labelText: String, icon: String, control: HTMLElement): HTMLElement {
    val label = el("label", "form-label")
    label.setAttribute("for", id)
    label.appendAll(
        navIcon(icon),
        el("span", "", labelText),
    )

    return el("div", "form-field").appendAll(label, control)
}

private fun setSubmitState(button: HTMLElement, isSubmitting: Boolean) {
    button.asDynamic().disabled = isSubmitting
    button.querySelector("span")?.textContent = if (isSubmitting) "Sending..." else "Send Message"
}

private fun setFormStatus(status: HTMLElement, message: String, isError: Boolean) {
    status.textContent = message
    status.className = if (isError && message.isNotBlank()) "form-status form-error" else "form-status"
}

private fun successMessage(): HTMLElement =
    el("div", "success-card").appendAll(
        navIcon("success"),
        el("h3", "card-title", "Thank you for your message!"),
        el("p", "body-copy", "I have received your email and will get back to you shortly."),
    )

private fun submitToFormspree(
    email: String,
    subject: String,
    bodyMessage: String,
    onSuccess: () -> Unit,
    onError: (String) -> Unit,
) {
    js(
        """
        fetch("https://formspree.io/f/xwvyjnqg", {
          method: "POST",
          headers: {
            "Accept": "application/json",
            "Content-Type": "application/json"
          },
          body: JSON.stringify({
            email: email,
            subject: subject,
            message: bodyMessage
          })
        }).then(function (response) {
          if (response.ok) {
            onSuccess();
            return;
          }

          response.json().then(function (data) {
            var errors = data && data.errors ? data.errors.map(function (error) { return error.message; }).join(", ") : "";
            onError(errors || "Unable to send message. Please try again.");
          }).catch(function () {
            onError("Unable to send message. Please try again.");
          });
        }).catch(function () {
          onError("Network error. Please try again.");
        });
        """
    )
}

private fun section(id: String, eyebrow: String, title: String): HTMLElement {
    val section = el("section", "section")
    section.id = id
    section.appendAll(
        el("p", "eyebrow", eyebrow),
        el("h2", "section-title", title),
    )
    return section
}

private fun statPill(value: String, label: String): HTMLElement =
    el("div", "stat-pill").appendAll(
        el("strong", "", value),
        el("span", "", label),
    )

private fun bulletList(items: List<String>): HTMLElement {
    val list = el("ul", "bullet-list")
    items.filter { it.isNotBlank() }.forEach { item ->
        list.appendChild(el("li", "", item))
    }
    return list
}

private fun chipRow(items: List<String>): HTMLElement {
    val row = el("div", "chip-row")
    items.filter { it.isNotBlank() }.forEach { item ->
        row.appendChild(el("span", "mini-chip", item))
    }
    return row
}

private fun el(tag: String, className: String = "", text: String = ""): HTMLElement {
    val node = document.createElement(tag) as HTMLElement
    if (className.isNotBlank()) node.className = className
    if (text.isNotBlank()) node.textContent = text
    return node
}

private fun image(src: String, alt: String, className: String, eager: Boolean = false): HTMLImageElement {
    val img = document.createElement("img") as HTMLImageElement
    img.src = src
    img.alt = alt
    img.className = className
    img.setAttribute("loading", if (eager) "eager" else "lazy")
    img.setAttribute("decoding", "async")
    return img
}

private fun anchor(label: String, href: String, className: String): HTMLAnchorElement {
    val link = document.createElement("a") as HTMLAnchorElement
    link.href = href
    link.className = className
    link.textContent = label
    configureLinkTarget(link, href)
    return link
}

private fun anchorNode(href: String, className: String, content: Node): HTMLAnchorElement {
    val link = document.createElement("a") as HTMLAnchorElement
    link.href = href
    link.className = className
    link.appendChild(content)
    configureLinkTarget(link, href)
    return link
}

private fun iconAnchor(label: String, href: String, icon: String, className: String): HTMLAnchorElement {
    val link = anchor("", href, className)
    link.setAttribute("aria-label", label)
    link.title = label
    link.appendAll(
        navIcon(icon),
        el("span", "visually-hidden", label),
    )
    return link
}

private fun navIcon(name: String): Node {
    val svg = document.createElementNS(SVG_NS, "svg")
    svg.setAttribute("class", "nav-icon")
    svg.setAttribute("viewBox", "0 0 24 24")
    svg.setAttribute("aria-hidden", "true")
    svg.setAttribute("focusable", "false")
    svg.setAttribute("fill", "none")
    svg.setAttribute("stroke", "currentColor")
    svg.setAttribute("stroke-width", "2")
    svg.setAttribute("stroke-linecap", "round")
    svg.setAttribute("stroke-linejoin", "round")

    when (name) {
        "experience" -> svg.appendAllNs(
            svgCircle("18", "18", "3"),
            svgCircle("6", "6", "3"),
            svgPath("M6 9v12"),
            svgPath("M9 18h6"),
            svgPath("M9 6h5a4 4 0 0 1 4 4v5"),
        )
        "projects" -> svg.appendAllNs(
            svgPath("M3 7h6l2 3h10v7a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2Z"),
            svgPath("M3 7V5a2 2 0 0 1 2-2h4l2 4"),
        )
        "skills" -> svg.appendAllNs(
            svgCircle("12", "12", "3"),
            svgPath("M19.4 15a1.7 1.7 0 0 0 .34 1.88l.06.06a2 2 0 1 1-2.83 2.83l-.06-.06A1.7 1.7 0 0 0 15 19.4a1.7 1.7 0 0 0-1 .6 1.7 1.7 0 0 0-.4 1.08V21a2 2 0 1 1-4 0v-.09A1.7 1.7 0 0 0 8.6 19.4a1.7 1.7 0 0 0-1.88.34l-.06.06a2 2 0 1 1-2.83-2.83l.06-.06A1.7 1.7 0 0 0 4.6 15a1.7 1.7 0 0 0-.6-1 1.7 1.7 0 0 0-1.08-.4H3a2 2 0 1 1 0-4h.09A1.7 1.7 0 0 0 4.6 8.6a1.7 1.7 0 0 0-.34-1.88l-.06-.06a2 2 0 1 1 2.83-2.83l.06.06A1.7 1.7 0 0 0 9 4.6a1.7 1.7 0 0 0 1-.6 1.7 1.7 0 0 0 .4-1.08V3a2 2 0 1 1 4 0v.09A1.7 1.7 0 0 0 15.4 4.6a1.7 1.7 0 0 0 1.88-.34l.06-.06a2 2 0 1 1 2.83 2.83l-.06.06A1.7 1.7 0 0 0 19.4 9c.2.37.52.69.9.9.31.17.67.25 1.03.25H21a2 2 0 1 1 0 4h-.09A1.7 1.7 0 0 0 19.4 15Z"),
        )
        "education" -> svg.appendAllNs(
            svgPath("M3 8l9-4 9 4-9 4Z"),
            svgPath("M7 10v5c0 1.2 2.2 3 5 3s5-1.8 5-3v-5"),
            svgPath("M21 8v6"),
        )
        "certs" -> svg.appendAllNs(
            svgPath("M12 3l2.35 4.76 5.25.76-3.8 3.7.9 5.23L12 15l-4.7 2.45.9-5.23-3.8-3.7 5.25-.76Z"),
            svgPath("M9 19l3 2 3-2"),
        )
        "email" -> svg.appendAllNs(
            svgPath("M4 4h16v16H4Z"),
            svgPath("m4 7 8 6 8-6"),
        )
        "subject" -> svg.appendAllNs(
            svgPath("M5 4h14v16H5Z"),
            svgPath("M8 8h8"),
            svgPath("M8 12h8"),
            svgPath("M8 16h5"),
        )
        "message" -> svg.appendAllNs(
            svgPath("M4 5h16v11H8l-4 4Z"),
            svgPath("M8 9h8"),
            svgPath("M8 13h5"),
        )
        "send" -> svg.appendAllNs(
            svgPath("m22 2-7 20-4-9-9-4Z"),
            svgPath("M22 2 11 13"),
        )
        "success" -> svg.appendAllNs(
            svgCircle("12", "12", "10"),
            svgPath("m9 12 2 2 4-5"),
        )
        "github" -> svg.appendAllNs(
            svgPath("M9 19c-4.5 1.5-5-2-7-3"),
            svgPath("M15 22v-4a4.8 4.8 0 0 0-1-3.5c3 0 6-2 6-5.5 0-1.25-.35-2.48-1-3.5.28-1.15.28-2.35 0-3.5 0 0-1 0-3 1.5a10.5 10.5 0 0 0-6 0C8 2 7 2 7 2c-.3 1.15-.3 2.35 0 3.5A5.4 5.4 0 0 0 6 9c0 3.5 3 5.5 6 5.5-.39.49-.68 1.05-.85 1.65-.17.6-.22 1.23-.15 1.85v4"),
        )
        else -> svg.appendAllNs(
            svgPath("M8 2h8a2 2 0 0 1 2 2v16a2 2 0 0 1-2 2H8a2 2 0 0 1-2-2V4a2 2 0 0 1 2-2Z"),
            svgPath("M10 18h4"),
        )
    }

    return svg
}

private fun Node.appendAllNs(vararg nodes: Node): Node {
    nodes.forEach { appendChild(it) }
    return this
}

private fun svgPath(d: String): Node {
    val path = document.createElementNS(SVG_NS, "path")
    path.setAttribute("d", d)
    return path
}

private fun svgCircle(cx: String, cy: String, r: String): Node {
    val circle = document.createElementNS(SVG_NS, "circle")
    circle.setAttribute("cx", cx)
    circle.setAttribute("cy", cy)
    circle.setAttribute("r", r)
    return circle
}

private fun configureLinkTarget(link: HTMLAnchorElement, href: String) {
    if (href.startsWith("http")) {
        link.target = "_blank"
        link.rel = "noreferrer noopener"
    }
}

private fun HTMLElement.appendAll(vararg nodes: Node): HTMLElement {
    nodes.forEach { appendChild(it) }
    return this
}

private fun HTMLElement.appendChildReturn(node: Node): HTMLElement {
    appendChild(node)
    return this
}

private fun cleanHref(href: String): String? =
    href.trim().takeIf { it.isNotEmpty() && it != "#" }

private fun displayName(name: String): String =
    name.removeSuffix(" M S").ifBlank { name }

private fun iconPath(icon: String): String =
    iconPaths[icon] ?: "/icons/cog.svg"

private const val SVG_NS = "http://www.w3.org/2000/svg"

private val iconPaths = mapOf(
    "faJava" to "/icons/java.svg",
    "faPython" to "/icons/python.svg",
    "faJs" to "/icons/javascript.svg",
    "faReact" to "/icons/react.svg",
    "faHtml5" to "/icons/html5.svg",
    "faCss3Alt" to "/icons/css.svg",
    "faVuejs" to "/icons/vuejs.svg",
    "faSass" to "/icons/sass.svg",
    "faAndroid" to "/icons/android.svg",
    "faAppStoreIos" to "/icons/ios.svg",
    "faNodeJs" to "/icons/nodejs.svg",
    "faDocker" to "/icons/docker.svg",
    "faAws" to "/icons/aws.svg",
    "faGitAlt" to "/icons/git.svg",
    "faGithub" to "/icons/github.svg",
    "faGitlab" to "/icons/gitlab.svg",
    "faLinux" to "/icons/linux.svg",
    "faFigma" to "/icons/figma.svg",
    "faSketch" to "/icons/sketch.svg",
    "faPhp" to "/icons/php.svg",
    "faCode" to "/icons/nextjs.svg",
    "faWind" to "/icons/tailwindcss.svg",
    "faServer" to "/icons/redux.svg",
    "faTS" to "/icons/typescript.svg",
    "faRust" to "/icons/rust.svg",
    "faC" to "/icons/c.svg",
    "faCpp" to "/icons/cplusplus.svg",
    "faKotlin" to "/icons/kotlin.svg",
    "faCSharp" to "/icons/csharp.svg",
    "faDart" to "/icons/dart.svg",
    "faSwift" to "/icons/swift.svg",
    "faCordova" to "/icons/cordova.svg",
    "faFlutter" to "/icons/flutter.svg",
    "faJetpackCompose" to "/icons/jetpack-compose.svg",
    "faKMM" to "/icons/jetpack-compose.svg",
    "faUnity" to "/icons/unity.svg",
    "faUnreal" to "/icons/unreal.svg",
    "faMongo" to "/icons/mongodb.svg",
    "faPSQL" to "/icons/postgresql.svg",
    "faMysql" to "/icons/mysql.svg",
    "faNeo" to "/icons/neo4j.svg",
    "faRealm" to "/icons/realm.svg",
    "faCDB" to "/icons/cockroachdb.svg",
    "faVercel" to "/icons/vercel.svg",
    "faNetlify" to "/icons/netlify.svg",
    "faXML" to "/icons/xml.svg",
    "faJSON" to "/icons/json.svg",
    "faMaterial" to "/icons/materialdesign.svg",
    "faProjectDiagram" to "/icons/project-diagram.svg",
    "faCodeBranch" to "/icons/code-branch.svg",
    "faCloud" to "/icons/cloud.svg",
    "faDharmachakra" to "/icons/kubernetes.svg",
    "faCog" to "/icons/cog.svg",
    "faPaintBrush" to "/icons/paintbrush.svg",
    "faImage" to "/icons/image.svg",
    "faDrawPolygon" to "/icons/draw-polygon.svg",
    "faCube" to "/icons/cube.svg",
    "faUsers" to "/icons/users.svg",
    "faMobileAlt" to "/icons/mobile.svg",
)
