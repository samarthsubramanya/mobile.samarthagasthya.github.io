package dev.samarthagasthya.mobile.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import dev.samarthagasthya.mobile.NavIcon
import dev.samarthagasthya.mobile.SectionScrollState
import dev.samarthagasthya.mobile.submitToFormspree
import org.jetbrains.compose.web.attributes.onSubmit
import org.jetbrains.compose.web.attributes.placeholder
import org.jetbrains.compose.web.attributes.required
import org.jetbrains.compose.web.attributes.rows
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.EmailInput
import org.jetbrains.compose.web.dom.Form
import org.jetbrains.compose.web.dom.H3
import org.jetbrains.compose.web.dom.Label
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text
import org.jetbrains.compose.web.dom.TextArea
import org.jetbrains.compose.web.dom.TextInput

private data class FieldState(val value: String = "", val touched: Boolean = false, val error: String? = null)

private fun validateEmail(value: String): String? = when {
    value.isBlank() -> "Email is required"
    !value.contains("@") || !value.contains(".") -> "Enter a valid email address"
    else -> null
}

private fun validateSubject(value: String): String? =
    if (value.isBlank()) "Subject is required" else null

private fun validateMessage(value: String): String? = when {
    value.isBlank() -> "Message is required"
    value.trim().length < 10 -> "Tell me a bit more (10+ characters)"
    else -> null
}

@Composable
internal fun ContactSection(scrollState: SectionScrollState) {
    SectionShell("contact", "Contact", "Get In Touch", scrollState) {
        P(attrs = { classes("section-copy") }) {
            Text("Have a project or question? Fill out the form and I will get back to you as soon as possible.")
        }

        var email by remember { mutableStateOf(FieldState()) }
        var subject by remember { mutableStateOf(FieldState()) }
        var message by remember { mutableStateOf(FieldState()) }
        var isSubmitting by remember { mutableStateOf(false) }
        var isSubmitted by remember { mutableStateOf(false) }
        var generalError by remember { mutableStateOf<String?>(null) }

        Div(attrs = { classes("contact-form-shell") }) {
            if (isSubmitted) {
                SuccessMessage()
            } else {
                Form(attrs = {
                    onSubmit { event ->
                        event.preventDefault()

                        email = email.copy(touched = true, error = validateEmail(email.value))
                        subject = subject.copy(touched = true, error = validateSubject(subject.value))
                        message = message.copy(touched = true, error = validateMessage(message.value))

                        if (validateEmail(email.value) != null ||
                            validateSubject(subject.value) != null ||
                            validateMessage(message.value) != null
                        ) {
                            generalError = "Please fix the highlighted fields before sending."
                            return@onSubmit
                        }

                        generalError = null
                        isSubmitting = true

                        submitToFormspree(
                            email = email.value.trim(),
                            subject = subject.value.trim(),
                            bodyMessage = message.value.trim(),
                            onSuccess = {
                                isSubmitting = false
                                isSubmitted = true
                            },
                            onError = { errorMessage ->
                                isSubmitting = false
                                generalError = errorMessage
                            },
                        )
                    }
                }) {
                    FormField(
                        id = "email",
                        labelText = "Email Address",
                        icon = "email",
                        error = email.error.takeIf { email.touched },
                    ) {
                        EmailInput(value = email.value, attrs = {
                            id("email")
                            classes("form-control")
                            placeholder("your.email@example.com")
                            required()
                            onInput { event -> email = email.copy(value = event.value, error = null) }
                        })
                    }

                    FormField(
                        id = "subject",
                        labelText = "Subject",
                        icon = "subject",
                        error = subject.error.takeIf { subject.touched },
                    ) {
                        TextInput(value = subject.value, attrs = {
                            id("subject")
                            classes("form-control")
                            placeholder("What is this about?")
                            required()
                            onInput { event -> subject = subject.copy(value = event.value, error = null) }
                        })
                    }

                    FormField(
                        id = "message",
                        labelText = "Message",
                        icon = "message",
                        error = message.error.takeIf { message.touched },
                    ) {
                        TextArea(value = message.value, attrs = {
                            id("message")
                            classes("form-control", "message-control")
                            placeholder("Tell me more about your project or inquiry...")
                            rows(6)
                            required()
                            onInput { event -> message = message.copy(value = event.value, error = null) }
                        })
                    }

                    Button(attrs = {
                        classes("submit-button")
                        attr("type", "submit")
                        if (isSubmitting) attr("disabled", "")
                    }) {
                        NavIcon("send")
                        Span { Text(if (isSubmitting) "Sending..." else "Send Message") }
                    }

                    generalError?.let { errorText ->
                        P(attrs = { classes("form-status", "form-error") }) { Text(errorText) }
                    }
                }
            }
        }
    }
}

@Composable
private fun FormField(id: String, labelText: String, icon: String, error: String?, control: @Composable () -> Unit) {
    Div(attrs = {
        classes(buildList {
            add("form-field")
            if (error != null) add("has-error")
        })
    }) {
        Label(forId = id, attrs = { classes("form-label") }) {
            NavIcon(icon)
            Span { Text(labelText) }
        }
        control()
        if (error != null) {
            Span(attrs = { classes("field-error") }) { Text(error) }
        }
    }
}

@Composable
private fun SuccessMessage() {
    Div(attrs = { classes("success-card") }) {
        NavIcon("success")
        H3(attrs = { classes("card-title") }) { Text("Thank you for your message!") }
        P(attrs = { classes("body-copy") }) { Text("I have received your email and will get back to you shortly.") }
    }
}
