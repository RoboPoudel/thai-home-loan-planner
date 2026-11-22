// Gives the page a few desktop-only things: print preview and how updates work for this copy.
const { contextBridge, ipcRenderer } = require('electron');
const arg = (k) => (process.argv.find((a) => a.startsWith(`--${k}=`)) || '').split('=')[1];

contextBridge.exposeInMainWorld('desktop', {
  printPreview: (title) => ipcRenderer.invoke('print-preview', title),
  isPortable: arg('hlp-portable') === '1',
  autoUpdates: arg('hlp-auto') === '1',
});
