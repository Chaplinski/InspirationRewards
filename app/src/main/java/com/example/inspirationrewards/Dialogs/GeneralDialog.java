package com.example.inspirationrewards.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import com.example.inspirationrewards.R;

import androidx.appcompat.app.AppCompatDialogFragment;

public class GeneralDialog extends AppCompatDialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        String getTitle = getArguments().getString("title");
//        String getContent = getArguments().getString("content");
//        int getIcon = getArguments().getInt("icon");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Save Changes?");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                // Do nothing but close the dialog

            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                // Do nothing but close the dialog

            }
        });

        builder.setIcon(R.drawable.logo);

        return builder.create();
    }


}
