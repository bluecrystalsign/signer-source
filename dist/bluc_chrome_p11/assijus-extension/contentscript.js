var version;
try {
  version = chrome.runtime.getManifest().version;
} catch (ex) {
  version = '1';
}
document.getElementById("chrome-extension-active").value = version;
