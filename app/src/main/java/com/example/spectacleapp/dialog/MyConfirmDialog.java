package com.example.spectacleapp.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.spectacleapp.R;

public class MyConfirmDialog extends AppCompatDialogFragment {

    private MyDialogListener myDialogListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle("Confirmer la suppression compte !");
        alertDialogBuilder.setIcon(getResources().getDrawable(R.drawable.ic_info_red));
        alertDialogBuilder.setMessage("Êtes-vous sûr de vouloir supprimer votre compte ?");
        alertDialogBuilder.setCancelable(true);
        alertDialogBuilder.setPositiveButton("OUI", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                myDialogListener.confirmSuppresion(true);
            }
        });
        alertDialogBuilder.setNegativeButton("NON", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                myDialogListener.confirmSuppresion(false);
            }
        });

        return alertDialogBuilder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            myDialogListener = (MyDialogListener) context;
        } catch (Exception e) {
            throw new ClassCastException(context.toString()+
                    "must implement MyDialogListener");
        }

    }

    public interface MyDialogListener {
        void confirmSuppresion(boolean confirm);
    }
}