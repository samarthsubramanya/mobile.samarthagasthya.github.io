package dev.samarthagasthya.mobile

internal data class PortfolioData(
    val personal: PersonalInfo,
    val education: List<Education>,
    val skills: List<SkillCategory>,
    val experience: List<Experience>,
    val projects: List<Project>,
    val certifications: List<Certification>,
    val badgeProviders: List<BadgeProvider>,
)

internal data class PersonalInfo(
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

internal data class SocialLinks(
    val github: String,
    val linkedin: String,
    val twitter: String,
)

internal data class Education(
    val degree: String,
    val institution: String,
    val location: String,
    val period: String,
    val gpa: String,
    val highlights: List<String>,
)

internal data class SkillCategory(
    val category: String,
    val items: List<SkillItem>,
)

internal data class SkillItem(
    val name: String,
    val icon: String,
)

internal data class Experience(
    val title: String,
    val company: String,
    val url: String,
    val location: String,
    val period: String,
    val type: String,
    val description: String,
    val achievements: List<String>,
)

internal data class Project(
    val title: String,
    val category: String,
    val description: String,
    val technologies: List<String>,
    val image: String,
    val links: ProjectLinks,
    val highlights: List<String>,
)

internal data class ProjectLinks(
    val live: String,
    val github: String,
)

internal data class Certification(
    val name: String,
    val provider: String,
    val issuer: String,
    val date: String,
    val credentialId: String,
    val description: String,
    val skills: List<String>,
    val badgeUrl: String,
)

internal data class BadgeProvider(
    val name: String,
    val logo: String,
    val url: String,
    val description: String,
)

internal fun parsePortfolio(raw: dynamic): PortfolioData =
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

internal fun cleanHref(href: String): String? =
    href.trim().takeIf { it.isNotEmpty() && it != "#" }

internal fun displayName(name: String): String =
    name.removeSuffix(" M S").ifBlank { name }

internal fun iconPath(icon: String): String =
    iconPaths[icon] ?: "/icons/cog.svg"

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
