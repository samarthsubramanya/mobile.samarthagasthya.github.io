package dev.samarthagasthya.mobile

import kotlinx.browser.window
import kotlinx.coroutines.await
import kotlin.js.JSON

private const val PORTFOLIO_DATA_URL =
    "https://raw.githubusercontent.com/samarthsubramanya/samarthagasthya.github.io/refs/heads/revamp_v4/src/data/portfolioData.json"

internal sealed class UiState {
    data object Loading : UiState()
    data class Success(val data: PortfolioData) : UiState()
    data class Error(val message: String) : UiState()
}

internal suspend fun fetchPortfolio(): PortfolioData {
    val response = window.fetch(PORTFOLIO_DATA_URL).await()
    val text = response.text().await()
    return parsePortfolio(JSON.parse(text))
}

internal fun clearLegacyBrowserCaches() {
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
