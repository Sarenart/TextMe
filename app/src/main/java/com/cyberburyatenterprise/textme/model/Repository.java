package com.cyberburyatenterprise.textme.model;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

public class Repository {

    Application application;
    private FirebaseAuth mAuth;
    FirebaseDatabase database;
    private static Repository instance;

    public static Repository getInstance(Application application){
        if(instance == null)
            instance = new Repository(application);
        return instance;
    }

    private Repository(Application application){
        this.application = application;
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
    }

    public FirebaseUser isUserLoggedIn(){
        return mAuth.getCurrentUser();
    }

    public Task<AuthResult> signInUserWithEmailAndPassword(User user){
        return mAuth.signInWithEmailAndPassword(user.getEmail(), user.getPassword());
    }

    public void signOutUser(){
        mAuth.signOut();
    }

    public Task<AuthResult> signUpUser(User user){
        return mAuth.createUserWithEmailAndPassword(user.getEmail(), user.getPassword());
    }

    public void saveUserInDatabase(User user) {
        try {
            database.getReference("users")
                    .child(mAuth.getCurrentUser().getUid())
                    .setValue(user)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<Void> task) {
                            if (task.isSuccessful())
                                Toast.makeText(application.getBaseContext(), "User added successfully", Toast.LENGTH_SHORT).show();
                            else Toast.makeText(application.getBaseContext(), "Fail to save user", Toast.LENGTH_SHORT).show();
                        }
                    });
            signOutUser();
        }catch (DatabaseException exception){
            Log.d("User storage", "Couldn't save user");
        }
    }
}
