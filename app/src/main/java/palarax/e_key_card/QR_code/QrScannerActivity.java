package palarax.e_key_card.QR_code;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;
import palarax.e_key_card.R;

/**
 * @author Ilya Thai
 */
public class QrScannerActivity extends Fragment implements ZXingScannerView.ResultHandler{

    //https://github.com/dm77/barcodescanner

    private static final String TAG = QrScannerActivity.class.getSimpleName(); //used for debugging
    private ZXingScannerView mScannerView;

    public QrScannerActivity() {
        // Empty constructor required for fragment subclasses
        //http://examples.javacodegeeks.com/android/android-barcode-and-qr-scanner-example/
    }

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mScannerView = new ZXingScannerView(getContext());   // Programmatically initialize the scanner view
        //need to fix
        return inflater.inflate(R.layout.content_main, container, false);
    }

    @Override
    public void handleResult(Result rawResult) {
        // Do something with the result here
        Log.v(TAG, rawResult.getText()); // Prints scan results
        Log.v(TAG, rawResult.getBarcodeFormat().toString()); // Prints the scan format (qrcode, pdf417 etc.)

        // If you would like to resume scanning, call this method below:
        mScannerView.resumeCameraPreview(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();          // Start camera on resume
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();           // Stop camera on pause
    }

}
