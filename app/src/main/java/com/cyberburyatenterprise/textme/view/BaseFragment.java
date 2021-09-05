package com.cyberburyatenterprise.textme.view;

import android.os.Bundle;

import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.AndroidViewModel;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cyberburyatenterprise.textme.R;


public class BaseFragment extends Fragment {

    ViewDataBinding binding;
    private AndroidViewModel viewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_base, container, false);
    }

    public AndroidViewModel getViewModel() {
        return viewModel;
    }

    public void setViewModel(AndroidViewModel viewModel) {
        this.viewModel = viewModel;
    }
}