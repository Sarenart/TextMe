package com.cyberburyatenterprise.textme.view;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.cyberburyatenterprise.textme.R;
import com.cyberburyatenterprise.textme.databinding.FragmentRegisterBinding;
import com.cyberburyatenterprise.textme.model.User;
import com.cyberburyatenterprise.textme.viewmodel.AuthViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

import org.jetbrains.annotations.NotNull;

public class RegisterFragment extends Fragment {

    FragmentRegisterBinding binding;
    AuthViewModel authViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_register, container, false);
        return binding.getRoot();
    //return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        authViewModel = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);
        binding.setUser(authViewModel.getLoggingUser().getValue());
        binding.setRegisterClickHandlers(new RegisterClickHandlers(getContext()));

    }
    public class RegisterClickHandlers{

        Context context;
        public RegisterClickHandlers(Context context) {
            this.context = context;
        }

        public void onRegisterPressed(View view){

            String email          = binding.emailLineRegister.getText().toString().trim();
            String password       = binding.passwordLineRegister.getText().toString().trim();
            String repeatPassword = binding.repeatPasswordLineRegister.getText().toString().trim();

            if(email.isEmpty()) {
                binding.emailLineRegister.setError("E-mail is required");
                binding.emailLineRegister.requestFocus();
                return;
            }

            if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
            {
                binding.passwordLineRegister.setError("Invalid email");
                binding.passwordLineRegister.requestFocus();
                return;
            }

            if(password.isEmpty()){
                binding.passwordLineRegister.setError("Password is required");
                binding.passwordLineRegister.requestFocus();
                return;
            }

            if(password.length() < 6)
            {
                binding.passwordLineRegister.setError("The minimum length of the password is 6 characters");
                binding.passwordLineRegister.requestFocus();
                return;
            }
            if(!password.equals(repeatPassword))
            {
                binding.repeatPasswordLineRegister.setError("Password do not match");
                binding.repeatPasswordLineRegister.requestFocus();
                return;
            }

            binding.progressBarRegister.setVisibility(View.VISIBLE);

            User registeredUser = new User("", "", email, password);
            authViewModel.registerUser(registeredUser)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        authViewModel.saveUserInDatabase(registeredUser);
                        binding.progressBarRegister.setVisibility(View.GONE);

                        //Redirect to Login
                        redirectToLogin();
                    }
                    else Toast.makeText(RegisterFragment.this.getContext(), "failed to register, try again", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    public void redirectToLogin(){
        authViewModel.setHasUserBeenRegistered(true);
    }
}