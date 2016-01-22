package palarax.e_key_card.QR_code;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;

import palarax.e_key_card.R;


/**
 * @author Ilya Thai
 */
public class QrScannerActivity extends Fragment implements View.OnClickListener {

    public static final String TAG = "QR_SCANNER";

    // use a compound button so either checkbox or switch widgets work.
    private CompoundButton autoFocus;
    private CompoundButton useFlash;
    private TextView statusMessage;
    private TextView barcodeValue;
    private ImageView barcode ;
    private Spinner barcodeType;
    private EditText encodedValue;

    private static final int RC_BARCODE_CAPTURE = 9001;

    /**
     * Called when sample is created. Displays generic UI with welcome text.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.e(TAG, "onCreateView");
        View mainView = inflater.inflate(R.layout.qr_scanner_fragment, container, false);

        encodedValue = (EditText) mainView.findViewById(R.id.encode_value);
        barcode = (ImageView) mainView.findViewById(R.id.barcode);
        statusMessage = (TextView) mainView.findViewById(R.id.status_message);
        barcodeValue = (TextView)mainView.findViewById(R.id.barcode_value);
        autoFocus = (CompoundButton) mainView.findViewById(R.id.auto_focus);
        useFlash = (CompoundButton) mainView.findViewById(R.id.use_flash);

        barcodeType = (Spinner) mainView.findViewById(R.id.barcode_type);
        // Specify the layout to use when the list of choices
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.barcode_types, R.layout.spinner_item);
        // Apply the adapter to the spinner
        barcodeType.setAdapter(adapter);

        mainView.findViewById(R.id.generate_barcode).setOnClickListener(this);
        mainView.findViewById(R.id.read_barcode).setOnClickListener(this);

        return mainView;
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        //scan barcode
        BitMatrix matrix=null;
        if (v.getId() == R.id.read_barcode) {
            // launch barcode activity.
            barcode.setImageResource(0);
            Intent intent = new Intent(getActivity(), BarcodeCaptureActivity.class);
            intent.putExtra(BarcodeCaptureActivity.AutoFocus, autoFocus.isChecked());
            intent.putExtra(BarcodeCaptureActivity.UseFlash, useFlash.isChecked());
            startActivityForResult(intent, RC_BARCODE_CAPTURE);

        }
        //generate barcode
        else if (v.getId() == R.id.generate_barcode) {
            //QRCodeWriter writer = new QRCodeWriter();
            com.google.zxing. MultiFormatWriter writer =new MultiFormatWriter();
            try {
                matrix = writer.encode(encodedValue.getText().toString(), getBarcodeType() , 200, 200);
            } catch (Exception e) {
                String error = ""+e;
                String str = error.split(":")[1];
                Toast.makeText(getContext(), "Error: "+str,
                        Toast.LENGTH_LONG).show();}
            }

            try {
                if (!matrix.equals(null)) {
                    barcode.setImageBitmap(toBitmap(matrix));
                }
            }catch (Exception e) {
                Log.i(TAG,"Null exception");
            }
    }

    private BarcodeFormat getBarcodeType()
    {
        String type = barcodeType.getSelectedItem().toString();
        return BarcodeFormat.valueOf(type);
    }

    /**
     * Writes the given Matrix on a new Bitmap object.
     * @param matrix the matrix to write.
     * @return the new {@link Bitmap}-object.
     */
    private static Bitmap toBitmap(BitMatrix matrix){
        Log.e(TAG,"toBitmap");

        int height = matrix.getHeight();
        int width = matrix.getWidth();
        Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_4444);
        for (int x = 0; x < width; x++){
            for (int y = 0; y < height; y++){
                bmp.setPixel(x, y, matrix.get(x,y) ? Color.MAGENTA : Color.TRANSPARENT);
            }
        }
        return bmp;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_BARCODE_CAPTURE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    Barcode barcode = data.getParcelableExtra(BarcodeCaptureActivity.BarcodeObject);
                    statusMessage.setText(R.string.barcode_success);
                    barcodeValue.setText(barcode.displayValue);
                    Log.d(TAG, "Barcode read: " + barcode.displayValue);
                } else {
                    statusMessage.setText(R.string.barcode_failure);
                    Log.d(TAG, "No barcode captured, intent data is null");
                }
            } else {
                statusMessage.setText(String.format(getString(R.string.barcode_error),
                        CommonStatusCodes.getStatusCodeString(resultCode)));
            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    @Override
    public void onResume() {
        super.onResume();
    }

}
