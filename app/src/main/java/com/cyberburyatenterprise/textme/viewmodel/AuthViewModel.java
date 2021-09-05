package com.cyberburyatenterprise.textme.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.cyberburyatenterprise.textme.model.Repository;
import com.cyberburyatenterprise.textme.model.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

import org.jetbrains.annotations.NotNull;

public class AuthViewModel extends AndroidViewModel {

    private Repository repos;
    private MutableLiveData<Boolean> hasUserBeenLoggedIn = new MutableLiveData<>();
    private MutableLiveData<Boolean> hasUserBeenRegistered = new MutableLiveData<>();
    private MutableLiveData<User> loggingUser = new MutableLiveData<>();
    private MutableLiveData<User> registeringUser = new MutableLiveData<>();

    public AuthViewModel(@NonNull @NotNull Application application) {
        super(application);
        repos = Repository.getInstance(application);
    }

    public boolean isUserLoggedIn(){
        return repos.isUserLoggedIn() != null;
    }

    public LiveData<User> getLoggingUser() {
        return loggingUser;
    }


    public LiveData<User> getRegisteringUser() {
        return registeringUser;
    }

    public Task<AuthResult> registerUser(User user){
        return repos.signUpUser(user);
    }

    public Task<AuthResult> logInUserWithEmailAndPassword(User user){
        return repos.signInUserWithEmailAndPassword(user);
    }
    public void logOut(){
        repos.signOutUser();
    }

    public void saveUserInDatabase(User user){
        repos.saveUserInDatabase(user);
    }

    public LiveData<Boolean> getHasUserBeenRegistered() {
        return hasUserBeenRegistered;
    }

    public void setHasUserBeenRegistered(boolean hasUserBeenRegistered) {
        this.hasUserBeenRegistered.setValue(hasUserBeenRegistered);
    }
    public MutableLiveData<Boolean> getHasUserBeenLoggedIn() {
        return hasUserBeenLoggedIn;
    }

    public void setHasUserBeenLoggedIn(boolean hasUserBeenLoggedIn) {
        this.hasUserBeenLoggedIn.setValue(hasUserBeenLoggedIn);
    }
}
