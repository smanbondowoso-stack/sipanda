# SIPANDA – Aplikasi Android

Sistem Informasi Pemantauan Ibadah SMADA. Aplikasi ini membuka web app Google Apps Script
(`app_url` di `app/src/main/res/values/strings.xml`) di dalam WebView, dengan ikon & layar pembuka dari logo SIPANDA.

## Cara 1 – Android Studio
1. Ekstrak zip, buka Android Studio → **Open** → pilih folder `sipanda-android`.
2. Tunggu Gradle sync selesai.
3. **Build → Build Bundle(s) / APK(s) → Build APK(s)**.
4. APK ada di `app/build/outputs/apk/debug/app-debug.apk` – kirim ke HP dan pasang
   (izinkan "Install dari sumber tidak dikenal").

## Cara 2 – Tanpa Android Studio (GitHub Actions)
1. Buat repository GitHub baru, unggah seluruh isi folder ini (termasuk `.github`).
2. Buka tab **Actions → Build APK → Run workflow**.
3. Setelah selesai, unduh artifact **SIPANDA-apk** (berisi `app-debug.apk`).

## Mengganti URL
Ubah `app_url` di `app/src/main/res/values/strings.xml`, lalu build ulang.
Tip: di Apps Script gunakan **Deploy → Manage deployments → Edit → Version baru** agar URL `/exec` tidak berubah.

## Catatan penting
- Deployment Apps Script harus **Who has access: Anyone** (Siapa saja). Jika memakai login Google,
  Google memblokir login di dalam WebView ("disallowed_useragent").
- Untuk rilis ke Play Store, buat keystore dan build **release** (`./gradlew bundleRelease`) dengan signing config sendiri.
