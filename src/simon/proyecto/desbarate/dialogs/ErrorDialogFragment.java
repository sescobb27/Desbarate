package simon.proyecto.desbarate.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class ErrorDialogFragment extends DialogFragment {
    private Dialog error_dialog;

    public ErrorDialogFragment() {
        super();
    }

    public void setDialog(Dialog error_dialog) {
    	this.error_dialog = error_dialog;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return error_dialog;
    }
}
