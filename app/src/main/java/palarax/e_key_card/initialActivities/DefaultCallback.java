package palarax.e_key_card.initialActivities;

import android.app.AlertDialog;
import android.content.Context;
import android.widget.Toast;

import com.backendless.async.callback.BackendlessCallback;
import com.backendless.exceptions.BackendlessFault;

import dmax.dialog.SpotsDialog;
import palarax.e_key_card.R;

/**
 * @author Ilya Thai
 */
public class DefaultCallback<T> extends BackendlessCallback<T>
{
    Context context;
    AlertDialog progressDialog;

    public DefaultCallback( Context context )
    {
        this.context = context;
        progressDialog = new SpotsDialog(context, R.style.Custom);
        progressDialog.show();

    }

    @Override
    public void handleResponse( T response )
    {
        progressDialog.dismiss();
    }

    //This override is optional
    @Override
    public void handleFault( BackendlessFault fault )
    {
        progressDialog.dismiss();
        Toast.makeText(context, fault.getMessage(), Toast.LENGTH_SHORT).show();
    }
}