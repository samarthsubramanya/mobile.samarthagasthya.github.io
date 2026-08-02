package dev.samarthagasthya.mobile

internal fun submitToFormspree(
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
