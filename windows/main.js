// Windows desktop app: opens the planner (site/ copied to app/ at build time) in its own window.
const { app, BrowserWindow, shell, Menu, ipcMain } = require('electron');
const path = require('path');
const fs = require('fs');
const os = require('os');
const PORTABLE = !!process.env.PORTABLE_EXECUTABLE_DIR;
let autoUpdater = null;
if (!PORTABLE && app.isPackaged) { try { ({ autoUpdater } = require('electron-updater')); } catch (e) { autoUpdater = null; } }

function createWindow() {
  const win = new BrowserWindow({
    width: 1280,
    height: 860,
    minWidth: 380,
    title: 'Home Loan Planner',
    icon: path.join(__dirname, 'icon.png'),
    backgroundColor: '#F3F5F9',
    webPreferences: { contextIsolation: true, sandbox: true, preload: path.join(__dirname, 'preload.js'),
      additionalArguments: [`--hlp-portable=${PORTABLE ? 1 : 0}`, `--hlp-auto=${autoUpdater ? 1 : 0}`] },
  });
  Menu.setApplicationMenu(null);
  // bank sites, GitHub and other links open in the default browser
  win.webContents.setWindowOpenHandler(({ url }) => { shell.openExternal(url); return { action: 'deny' }; });
  win.webContents.on('will-navigate', (e, url) => {
    if (!url.startsWith('file://')) { e.preventDefault(); shell.openExternal(url); }
  });
  // Ctrl+P opens the same preview as the Print button
  win.webContents.on('before-input-event', (e, input) => {
    if (input.type === 'keyDown' && input.control && input.key.toLowerCase() === 'p') { e.preventDefault(); printPreview(win, 'Home Loan Planner'); }
  });
  win.loadFile(path.join(__dirname, 'app', 'index.html'), { hash: 'plan' });
}

// Windows' print dialog can't preview an Electron page, so render the page to PDF
// and show it in a viewer window, which has its own Print and Download buttons.
async function printPreview(win, title) {
  const pdf = await win.webContents.printToPDF({ printBackground: true, pageSize: 'A4' });
  const safe = String(title || 'Home Loan Planner').replace(/[^A-Za-z0-9 _-]/g, '').trim() || 'Home Loan Planner';
  const file = path.join(os.tmpdir(), `${safe} ${Date.now()}.pdf`);
  fs.writeFileSync(file, pdf);
  const view = new BrowserWindow({
    width: 900, height: 1000, parent: win, title: `${safe} - Print preview`,
    icon: path.join(__dirname, 'icon.png'), autoHideMenuBar: true,
    webPreferences: { plugins: true, contextIsolation: true, sandbox: true },
  });
  view.on('closed', () => fs.unlink(file, () => {}));
  view.loadFile(file);
}

ipcMain.handle('print-preview', (e, title) => {
  const win = BrowserWindow.fromWebContents(e.sender);
  if (win) return printPreview(win, title);
});

app.whenReady().then(() => {
  createWindow();
  // installed copies update themselves from GitHub releases; the portable .exe shows a banner instead
  if (autoUpdater) { autoUpdater.checkForUpdatesAndNotify().catch(() => {}); }
  app.on('activate', () => { if (BrowserWindow.getAllWindows().length === 0) createWindow(); });
});
app.on('window-all-closed', () => { if (process.platform !== 'darwin') app.quit(); });
