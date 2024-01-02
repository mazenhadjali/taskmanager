package com.example.taskmanager.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.taskmanager.R;
import com.example.taskmanager.core.DataBaseHelper;

public class ProfileFragment extends Fragment {
    private EditText newPasswordEditText;
    private EditText confirmPasswordEditText;
    private Button saveButton;

    private DataBaseHelper databaseHelper;

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        databaseHelper = new DataBaseHelper(this.getActivity());

        newPasswordEditText = view.findViewById(R.id.etNewPassword);
        confirmPasswordEditText = view.findViewById(R.id.etConfirmPassword);
        saveButton = view.findViewById(R.id.saveButton);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNewPassword();
            }
        });

        updateSaveButtonState();
        // Enable the save button only when new password and confirm password match
        confirmPasswordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                updateSaveButtonState();
            }
        });

        return view;
    }

    private void updateSaveButtonState() {
        String newPassword = newPasswordEditText.getText().toString();
        String confirmPassword = confirmPasswordEditText.getText().toString();
        saveButton.setEnabled(newPassword.equals(confirmPassword) && !newPassword.isEmpty());
    }

    private void saveNewPassword() {
        String newPassword = newPasswordEditText.getText().toString();
        databaseHelper.changeUserPassword(newPassword);
        Toast.makeText(getContext(), "New password saved: " + newPassword, Toast.LENGTH_SHORT).show();
    }
}
