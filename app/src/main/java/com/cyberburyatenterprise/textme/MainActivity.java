package com.cyberburyatenterprise.textme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;

import com.cyberburyatenterprise.textme.databinding.ActivityMainBinding;
import com.cyberburyatenterprise.textme.view.AuthFragmentDirections;
import com.cyberburyatenterprise.textme.view.MainFragmentDirections;
import com.cyberburyatenterprise.textme.viewmodel.AuthViewModel;
import com.cyberburyatenterprise.textme.viewmodel.ProfileViewModel;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MainActivity extends AppCompatActivity {


    private MaterialToolbar topAppBar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private NavController navController;

    private boolean isStateRestored;
    ActivityMainBinding binding;

    AuthViewModel authViewModel;
    ProfileViewModel profileViewModel;

    @Override
    protected void onStart() {
        super.onStart();
        isUserLoggedIn();
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        isStateRestored = true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        initializeViewModels();
        binding = ActivityMainBinding.inflate(getLayoutInflater());

        NavHostFragment navHost = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.main_screen_container_view);
        assert navHost != null;
        navController = navHost.getNavController();

        topAppBar      = findViewById(R.id.topAppBar);
        drawerLayout   = findViewById(R.id.main_layout);
        navigationView = findViewById(R.id.navigationView);

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration
                .Builder(navController.getGraph()).setOpenableLayout(drawerLayout).build();

        //NavigationUI.setupWithNavController(topAppBar, navController);
        NavigationUI.setupWithNavController(navigationView, navController);

        int orientation = this.getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            topAppBar.setNavigationOnClickListener(v ->
                    {
                       assert drawerLayout != null;
                       drawerLayout.open();
                    }

            );
        }



        navigationView.setNavigationItemSelectedListener(item -> {
            item.setChecked(true);

            if(item.getTitle().toString().equals(getString(R.string.log_out))) {
                authViewModel.logOut();
                redirectToLogin();
            }
            if(item.getTitle().toString().equals(getString(R.string.settings))) {
                if(item.isChecked())
                //hideTopAppBar();
                moveToSettings();
            }
            if(orientation == Configuration.ORIENTATION_PORTRAIT)
            drawerLayout.close();
            return true;
        });

        setBarVisibility();

        authViewModel.getHasUserBeenLoggedIn().observe(this, aBoolean -> {
            if(aBoolean)
            {
                authViewModel.setHasUserBeenLoggedIn(false);
                moveToMainScreen();
            }
        });
    }

    private void setBarVisibility() {
        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NotNull NavController navController, @NotNull NavDestination navDestination, @Nullable Bundle bundle) {
                boolean showAppBar = false;
                if (bundle != null) {
                    showAppBar = bundle.getBoolean("ShowAppBar", false);

                }
                if(showAppBar) {
                    updateToolbarOnNavigation(View.VISIBLE);
                } else {
                    updateToolbarOnNavigation(View.GONE);
                }
            }
        });
    }

    public void updateToolbarOnNavigation(int visibility){
        topAppBar.setVisibility(visibility);
        navigationView.setVisibility(visibility);
        if(visibility == View.GONE)
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        else
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
    }

    //region appBar methods (depricated as of now)
    public void hideTopAppBar(){
        topAppBar.setVisibility(View.GONE);
    }

    public void showTopAppBar(){
        topAppBar.setVisibility(View.VISIBLE);
    }
    //endregion


    /*public void logOut(){
        FirebaseAuth.getInstance().signOut();
    }*/


    //region navigation methods
    public void returnFromSettings(){
        NavDirections action = MainFragmentDirections.actionMainFragmentToAuthFragment();
        navController.navigate(action);
    }

    public void moveToSettings(){
        NavDirections action = MainFragmentDirections.actionMainFragmentToSettingsFragment();
        navController.navigate(action);
    }

    private void moveToMainScreen() {
        //showTopAppBar();
        NavDirections action = AuthFragmentDirections.actionAuthFragmentToMainFragment();
        navController.navigate(action);
    }
    //endregion

    public void redirectToLogin(){
       // hideTopAppBar();
        NavDirections action = MainFragmentDirections.actionMainFragmentToAuthFragment();
        navController.navigate(action);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //showTopAppBar();
    }


    public void initializeViewModels(){
        authViewModel    = new ViewModelProvider(this).get(AuthViewModel.class);
        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
    }

    public void isUserLoggedIn(){
        if (!authViewModel.isUserLoggedIn() && !isStateRestored)
            redirectToLogin();
    }

}