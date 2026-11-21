"""Build site/index.html (GitHub Pages, PWA, Android and Windows apps) from app/index.html.

Adds the web-app manifest, icons and the offline service worker. Run from the repo root:
    python3 tools/make_site.py
"""
import pathlib, re

root = pathlib.Path(__file__).resolve().parent.parent
src = (root / "app" / "index.html").read_text(encoding="utf-8")

head = """<link rel="manifest" href="manifest.webmanifest">
<meta name="theme-color" content="#2E5BE6">
<link rel="icon" type="image/png" href="icons/icon-192.png">
<link rel="apple-touch-icon" href="icons/apple-touch-icon.png">
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="apple-mobile-web-app-title" content="Home Loan">
"""
sw = """<script>
if ('serviceWorker' in navigator && location.protocol === 'https:' && !/androidplatform\\.net$/.test(location.hostname)) {
  window.addEventListener('load', () => navigator.serviceWorker.register('sw.js').catch(() => {}));
}
</script>
"""
out = src.replace("</head>", head + "</head>", 1).replace("</body>", sw + "</body>", 1)
out = out.replace("</footer>", '</footer>\n<footer style="padding-top:0"><a href="Mortgage_Planner.xlsx" download>⬇ Excel planner (Mortgage_Planner.xlsx)</a> · <a href="https://github.com/RoboPoudel/thai-home-loan-planner">GitHub</a></footer>', 1)
(root / "site" / "index.html").write_text(out, encoding="utf-8")
print("site/index.html written,", len(out), "bytes")
