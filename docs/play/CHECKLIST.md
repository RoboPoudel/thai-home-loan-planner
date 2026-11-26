# Google Play release checklist

## 1. One-time setup (GitHub)
1. Add the signing secrets in the repo: **Settings → Secrets and variables → Actions → New repository secret**
   - `ANDROID_KEYSTORE_B64` and `ANDROID_KEYSTORE_PASSWORD` (values are in the private `android-signing-secrets.txt` – never commit that file).
2. Re-run **Actions → Android & Windows apps → Run workflow**. The release then contains `HomeLoanPlanner-1.0.N-play.aab`.
   - The Play build has no self-update banner and no APK/exe download cards (Play rules).
   - Targets Android 15 (API 35), as Play requires for new apps.

## 2. Play Console – create the app
- App name: *Home Loan Planner Thailand* · Default language: English (United States) · App · Free.
- **Play App Signing:** keep it on (recommended). Our key becomes the *upload key*.

## 3. App content (Policy → App content)
| Item | Answer |
|---|---|
| Privacy policy | https://robopoudel.github.io/thai-home-loan-planner/privacy.html |
| Ads | No ads |
| App access | All functionality available without special access |
| Content rating | Questionnaire → category *Utility/Productivity*; answer No to everything → rated Everyone/3+ |
| Target audience | 18 and over |
| Data safety | **No data collected, no data shared.** Data is processed only on the device. Encryption in transit: yes (HTTPS). Users can delete data by uninstalling. |
| Financial features | Select **"Loan calculators"** (or *Other* → "Mortgage calculator and planning tool"). Declare that the app does **not** offer, arrange or facilitate loans. |
| Government app | No |
| News app | No |
| Health | None |

## 4. Store listing
Texts and graphics are in `docs/play/listing.md`. Add translations (Thai first) under *Store listing → Manage translations*.

## 5. Testing (new personal developer accounts)
- Personal accounts created after Nov 2023 must run a **closed test with at least 12 testers for 14 days in a row** before production access.
- Create *Testing → Closed testing → track*, upload the `.aab`, add testers by e-mail list (family and friends with Android phones), share the opt-in link, and ask them to keep the app installed for 14 days.
- Then apply for production access (questions about the test) – review usually takes a few days.

## 6. Every new version
Push to `main` → the workflow builds a new `-play.aab` with a higher version code → upload it to the testing or production track.
