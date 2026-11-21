// Gives the page one desktop-only function: open a print preview of the current view.
const { contextBridge, ipcRenderer } = require('electron');

contextBridge.exposeInMainWorld('desktop', {
  printPreview: (title) => ipcRenderer.invoke('print-preview', title),
});
