# Home Loan Planner

Developed by **Henry Poudel** · [hennrywebworm@gmail.com](mailto:hennrywebworm@gmail.com) · WhatsApp [+66 87 407 5116](https://wa.me/66874075116)

Plan a home loan in Thailand: compare bank offers, stress-test rate rises, track your payments, check refinancing, and prepare your application – in 8 languages (English, ไทย, မြန်မာ, नेपाली, हिन्दी, Tiếng Việt, 中文, 日本語).

**Use it now:** https://robopoudel.github.io/thai-home-loan-planner/

**Apps:** [latest release](https://github.com/RoboPoudel/thai-home-loan-planner/releases/latest) – Android `.apk` and Windows `.exe` (portable or installer). On iPhone, open the website and choose *Share → Add to Home Screen*.

## What it does

| Tab | |
|---|---|
| My loans | Several homes or loans side by side: monthly total, balances, combined debt-to-income, debt-free date |
| Savings goal | How long to save the down payment + transfer cash, monthly target for a date, deposit log |
| Plan | Fixed, your own variable steps (up to 20), or a bank's offer; extra payments (amount, % of installment, % of balance; monthly or yearly) and a lump sum |
| Bank rates | Thai banks' promotional rates side by side, total interest for your loan; check an offer you received (photo read by AI inside Claude, or typed) |
| Affordability | Debt-to-income (DSR), maximum loan and property price; income in another currency; "get the bank to say yes" – debts to pay off, income, term or down payment needed |
| Area check | Price per sqm vs Bangkok/Chiang Mai area ranges and nearby listings, likely rent and yield, appraisal gap |
| Living costs | Common fee, sinking fund, utilities, insurance, repairs, land & building tax – the real monthly cost |
| Costs & tax | Transfer-day cash, the 0.01% fee measure, yearly tax saved from the interest deduction |
| Stress test | +1/+2/+3% and your own rise, with DSR check |
| Invest vs prepay | Same monthly budget, which leaves you richer |
| Documents | Checklists for Thai employees, business owners, freelancers and foreigners; attach photos/PDFs of each document (kept only on your device) |
| What-if | Sliders for price, down payment, rate, term and extra; save plans and compare them |
| Journey | Booking → transfer → retention/refinance timeline, plus reminders: calendar file (.ics) and phone notifications in the Android app |
| Tracker | Log real payments and see whether you're ahead of plan |
| Progress | % paid ring, 15 achievements, a shareable progress card picture |
| Refinance | Stay vs retention vs refinance (penalty, fees, break-even) and top-up loans |
| Rent vs buy | Wealth after N years both ways, plus rental yield |
| Apply | Official home-loan pages of banks in Thailand and Myanmar, and a printable information sheet |
| Report | Bank-ready multi-page PDF: key figures, rate plan, affordability, stress test, bank comparison, yearly schedule, documents |
| Share & sync | Plan link, Excel planner import/export |

Also: voice questions and read-aloud answers in the advisor, any loan currency (THB, MMK, USD …), a start-here wizard, light/dark mode with 6 colours, bank rates that update themselves from `site/rates.json`, and apps that tell you (Android, portable Windows) or install (Windows setup) when a new version is out.

Everything runs in your browser; inputs are saved only on your device.

## Excel planner

`site/Mortgage_Planner.xlsx` is the full spreadsheet version. The web app can read your numbers from it and write the web plan back into it (Share & sync tab).

## Repository layout

```
app/index.html          the planner (single file, no build step)
site/                   what GitHub Pages serves: index.html (built), manifest, service worker, icons, Excel file
tools/make_site.py      builds site/index.html from app/index.html (adds the PWA bits)
tools/enginetest.js     checks the calculations against the Excel planner
tools/check_rates.js    validates site/rates.json
site/rates.json         current bank rates – edit this to update every copy of the app
android/                WebView app; the APK bundles site/
windows/                Electron app; the .exe bundles site/
.github/workflows/      deploy Pages; build the APK and .exe and publish a release on every push to main
```

To change the planner, edit `app/index.html` and push – the website and both apps rebuild automatically.

Rates and fees change; this is not financial advice – confirm with your bank before signing.

## Updating bank rates

Edit `site/rates.json` (rates are fractions: 2.55% = `0.0255`; `mrr: null` means "estimate from the other banks"), set `updated` to today's date, and push. The website redeploys in about a minute and every copy of the planner – website, Android, Windows – picks up the new rates the next time it opens online. A monthly scheduled check can do this for you.

## Android signing

Release APKs are signed with a permanent key so updates install over the previous version. The key lives in two repository secrets (Settings → Secrets and variables → Actions): `ANDROID_KEYSTORE_B64` and `ANDROID_KEYSTORE_PASSWORD`. Without them the build falls back to a debug-signed APK.

## Google Play
The workflow also builds a signed `-play.aab` (Android 15 / API 35, no self-update) when the signing secrets are set. Store texts, screenshots, the feature graphic and a step-by-step checklist are in [`docs/play`](docs/play/CHECKLIST.md). Privacy policy: https://robopoudel.github.io/thai-home-loan-planner/privacy.html
