package com.cyberburyatenterprise.textme.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cyberburyatenterprise.textme.R;
import com.cyberburyatenterprise.textme.databinding.FragmentAuthBinding;
import com.cyberburyatenterprise.textme.viewmodel.AuthViewModel;
import com.google.android.material.tabs.TabLayout;

import org.jetbrains.annotations.NotNull;

public class AuthFragment extends BaseFragment {
    FragmentAuthBinding binding;
    NavController childNavController;
    TabLayout.Tab registerTab;
    TabLayout.Tab loginTab;

    AuthViewModel authViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_auth, container, false);
        return binding.getRoot();
        //return inflater.inflate(R.layout.fragment_auth, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        authViewModel = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);
        NavHostFragment navHost = (NavHostFragment) getChildFragmentManager().findFragmentById(R.id.auth_screen_container_view);
        assert navHost != null;
        childNavController = navHost.getNavController();

        loginTab = binding.tabLayout.newTab().setText("Login");
        registerTab = binding.tabLayout.newTab().setText("Register");
        binding.tabLayout.addTab(loginTab);
        binding.tabLayout.addTab(registerTab);

        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab == loginTab){
                    NavDirections action = RegisterFragmentDirections.actionRegisterFragmentToLoginFragment();
                    childNavController.navigate(action);
                }
                else if(tab == registerTab){
                    NavDirections action = LoginFragmentDirections.actionLoginFragmentToRegisterFragment();
                    childNavController.navigate(action);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        authViewModel.getHasUserBeenRegistered().observe(requireActivity(), aBoolean -> {
            if(aBoolean)
            {
                authViewModel.setHasUserBeenRegistered(false);
                binding.tabLayout.getTabAt(0).select();
            }
        });
    }
}