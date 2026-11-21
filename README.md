# Home Loan Planner

Developed by **Henry Poudel** · [hennrywebworm@gmail.com](mailto:hennrywebworm@gmail.com) · WhatsApp [+66 87 407 5116](https://wa.me/66874075116)

Plan a home loan in Thailand: compare bank offers, stress-test rate rises, track your payments, check refinancing, and prepare your application – in 8 languages (English, ไทย, မြန်မာ, नेपाली, हिन्दी, Tiếng Việt, 中文, 日本語).

**Use it now:** https://robopoudel.github.io/thai-home-loan-planner/

**Apps:** [latest release](https://github.com/RoboPoudel/thai-home-loan-planner/releases/latest) – Android `.apk` and Windows `.exe` (portable or installer). On iPhone, open the website and choose *Share → Add to Home Screen*.

## What it does

| Tab | |
|---|---|
| Plan | Fixed, your own variable steps (up to 20), or a bank's offer; extra payments (amount, % of installment, % of balance; monthly or yearly) and a lump sum |
| Bank rates | Thai banks' promotional rates side by side, total interest for your loan |
| Affordability | Debt-to-income (DSR), maximum loan and property price |
| Costs & tax | Transfer-day cash, the 0.01% fee measure, yearly tax saved from the interest deduction |
| Stress test | +1/+2/+3% and your own rise, with DSR check |
| Invest vs prepay | Same monthly budget, which leaves you richer |
| Documents | Checklists for Thai employees, business owners, freelancers and foreigners |
| What-if | Sliders for price, down payment, rate, term and extra; save plans and compare them |
| Journey | Booking → transfer → retention/refinance timeline with dates from your transfer date |
| Tracker | Log real payments and see whether you're ahead of plan |
| Refinance | Stay vs retention vs refinance (penalty, fees, break-even) and top-up loans |
| Rent vs buy | Wealth after N years both ways, plus rental yield |
| Apply | Official home-loan pages of banks in Thailand and Myanmar, and a printable information sheet |
| Share & sync | Plan link, Excel planner import/export |

Everything runs in your browser; inputs are saved only on your device.

## Excel planner

`site/Mortgage_Planner.xlsx` is the full spreadsheet version. The web app can read your numbers from it and write the web plan back into it (Share & sync tab).

## Repository layout

```
app/index.html          the planner (single file, no build step)
site/                   what GitHub Pages serves: index.html (built), manifest, service worker, icons, Excel file
tools/make_site.py      builds site/index.html from app/index.html (adds the PWA bits)
tools/enginetest.js     checks the calculations against the Excel planner
android/                WebView app; the APK bundles site/
windows/                Electron app; the .exe bundles site/
.github/workflows/      deploy Pages; build the APK and .exe and publish a release on every push to main
```

To change the planner, edit `app/index.html` and push – the website and both apps rebuild automatically.

Rates and fees change; this is not financial advice – confirm with your bank before signing.
