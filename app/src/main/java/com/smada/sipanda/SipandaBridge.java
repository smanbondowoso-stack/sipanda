package com.smada.sipanda;

import android.app.Activity;
import android.webkit.JavascriptInterface;

import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions;
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning;

/**
 * Jembatan JavaScript "SipandaNative" untuk pemindai QR native.
 *
 * Halaman SIPANDA berjalan di dalam iframe Apps Script yang tidak diberi izin kamera oleh browser,
 * sehingga getUserMedia() selalu ditolak ("Kamera langsung diblokir"). Pemindai Google Code Scanner
 * (Play Services) berjalan di luar WebView dan tidak membutuhkan izin CAMERA.
 *
 * Alur: halaman memanggil scanQr(), lalu membaca hasilnya berkala dengan pollQr()
 * ("" = belum ada hasil, "CANCEL" = dibatalkan/gagal, selain itu = isi QR).
 */
public class SipandaBridge {
    private final Activity activity;
    private volatile String result = "";
    private volatile boolean scanning = false;

    public SipandaBridge(Activity activity) {
        this.activity = activity;
    }

    @JavascriptInterface
    public void scanQr() {
        if (scanning) return;
        scanning = true;
        result = "";
        activity.runOnUiThread(() -> {
            try {
                GmsBarcodeScannerOptions options = new GmsBarcodeScannerOptions.Builder()
                        .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
                        .build();
                GmsBarcodeScanning.getClient(activity, options).startScan()
                        .addOnSuccessListener(barcode -> {
                            String v = barcode.getRawValue();
                            result = (v == null || v.isEmpty()) ? "CANCEL" : v;
                            scanning = false;
                        })
                        .addOnCanceledListener(() -> {
                            result = "CANCEL";
                            scanning = false;
                        })
                        .addOnFailureListener(e -> {
                            result = "CANCEL";
                            scanning = false;
                        });
            } catch (Exception e) {
                result = "CANCEL";
                scanning = false;
            }
        });
    }

    @JavascriptInterface
    public String pollQr() {
        String r = result;
        if (!r.isEmpty()) result = "";
        return r;
    }
}
