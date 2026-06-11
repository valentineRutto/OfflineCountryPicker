# Offline Country Picker

A Jetpack Compose country and phone code picker that works fully offline.

## Features

- Offline country list with flags, ISO country codes, dial codes, currencies, capitals, and languages.
- Compose UI for selecting countries in a modal bottom sheet or dialog.
- Configurable selected-country and picker-row display options.
- Sources and Javadocs are generated for Maven Central publication.

## Installation

```kotlin
dependencies {
    implementation("io.github.valentinerutto:offline-country-picker:1.0.0")
}
```

## Usage

```kotlin
var selectedCountry by remember {
    mutableStateOf(CountryDataProvider.getCountryByCode("KE"))
}

CountryCodePickerUI(
    selectedCountry = selectedCountry,
    onCountrySelected = { selectedCountry = it },
    selectedCountryDisplayOptions = CountryDisplayDefaults.FlagAndDialCode,
    pickerItemDisplayOptions = CountryDisplayDefaults.FlagNameCodeAndDialCode
)
```

To show the picker as a dialog:

```kotlin
CountryCodePickerUI(
    selectedCountry = selectedCountry,
    onCountrySelected = { selectedCountry = it },
    pickerPresentation = CountryPickerPresentation.DIALOG
)
```

To show the picker as a modal bottomsheet:

```kotlin
CountryCodePickerUI(
    selectedCountry = selectedCountry,
    onCountrySelected = { selectedCountry = it },
    pickerPresentation = CountryPickerPresentation.MODAL_BOTTOM_SHEET
)
```

## Maven Central Release Checklist

1. Create or verify the `io.github.valentinerutto` namespace in Sonatype Central Portal.
2. Generate a GPG key and publish its public key to a public keyserver.
3. Provide signing credentials through environment variables or Gradle properties:

```bash
export SIGNING_KEY="$(gpg --armor --export-secret-keys 2205F5198516B77D37C364D0F9C4C2BDDB3F96DB)"
export SIGNING_PASSWORD="your-key-password"
```

Alternatively set `signingInMemoryKey` and `signingInMemoryKeyPassword` in a private `~/.gradle/gradle.properties` file:

```properties
signingInMemoryKey=<output from: gpg --armor --export-secret-keys 2205F5198516B77D37C364D0F9C4C2BDDB3F96DB>
signingInMemoryKeyPassword=your-key-password
```

Do not commit your signing key or passphrase.

4. Build the signed Central Portal bundle:

```bash
./gradlew :OfflineCountryPicker:bundleReleasePublicationForCentralPortal
```

5. Upload the generated zip from:

```text
OfflineCountryPicker/build/central-portal-bundle/
```

6. Review validation results in Central Portal, then publish the deployment.

## License

MIT. See [LICENSE](LICENSE).
