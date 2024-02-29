package com.example.donutplayer;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.donutplayer.databinding.FolderViewBinding;
import com.example.donutplayer.databinding.FragmentFoldersBinding;

import java.lang.reflect.GenericArrayType;
import java.util.ArrayList;

public class FoldersFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentFoldersBinding binding = FragmentFoldersBinding.inflate(inflater, container, false);

        binding.folderRecyclerView.setHasFixedSize(true);
        binding.folderRecyclerView.setItemViewCacheSize(10);
        binding.folderRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.folderRecyclerView.setAdapter(new FolderAdapter(requireContext(), MainActivity.folderList));
        binding.totalFolders.setText("Total Folders: " + MainActivity.folderList.size());
        return binding.getRoot();
    }

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_folders, container, false);
//        FragmentFoldersBinding binding = FragmentFoldersBinding.bind(view);
//
//        ArrayList<String> tempList = new ArrayList<>();
//        tempList.add("a folder");
//        tempList.add("b folder");
//        tempList.add("c folder");
//        tempList.add("d folder");
//        tempList.add("e folder");
//
//        binding.folderRecyclerView.setHasFixedSize(true);
//        binding.folderRecyclerView.setItemViewCacheSize(10);
//        binding.folderRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
//        binding.folderRecyclerView.setAdapter(new FolderAdapter(requireContext(), tempList));
//        return view;
//    }
}