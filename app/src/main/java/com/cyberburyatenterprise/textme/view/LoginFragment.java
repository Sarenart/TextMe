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
import com.cyberburyatenterprise.textme.databinding.FragmentLoginBinding;
import com.cyberburyatenterprise.textme.model.User;
import com.cyberburyatenterprise.textme.viewmodel.AuthViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

import org.jetbrains.annotations.NotNull;

public class LoginFragment extends Fragment {

    FragmentLoginBinding binding;
    AuthViewModel authViewModel;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false);
        return binding.getRoot();
        //return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        authViewModel = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);

        binding.setUser(authViewModel.getLoggingUser().getValue());
        binding.setLoginClickHandlers(new LoginClickHandlers(getContext()));
    }

    public class LoginClickHandlers {

        Context context;
        public LoginClickHandlers(Context context) {
            this.context = context;
        }

        public void onLoginPressed(View view){
            String email    = binding.emailLineLogin.getText().toString().trim();
            String password = binding.passwordLineLogin.getText().toString().trim();

            if(email.isEmpty()) {
                binding.emailLineLogin.setError("E-mail is required");
                binding.emailLineLogin.requestFocus();
                return;
            }

            if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
            {
                binding.passwordLineLogin.setError("Invalid email");
                binding.passwordLineLogin.requestFocus();
                return;
            }

            if(password.isEmpty()){
                binding.emailLineLogin.setError("Password is required");
                binding.emailLineLogin.requestFocus();
                return;
            }

            if(password.length() < 6)
            {
                binding.passwordLineLogin.setError("The minimum length of the password is 6 characters");
                binding.passwordLineLogin.requestFocus();
                return;
            }

            binding.progressBarLogin.setVisibility(View.VISIBLE);

            User loggedInUser = new User("", "", email, password);
            authViewModel.logInUserWithEmailAndPassword(loggedInUser).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        binding.progressBarLogin.setVisibility(View.GONE);
                        authViewModel.setHasUserBeenLoggedIn(true);
                    }
                    else{
                        binding.progressBarLogin.setVisibility(View.GONE);
                        Toast.makeText(LoginFragment.this.getContext(), "Login failed, try again", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }
}