package com.example.donutplayer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.donutplayer.databinding.FolderViewBinding;

import java.util.ArrayList;

public class FolderAdapter extends RecyclerView.Adapter<FolderAdapter.FolderHolder> {
    private Context context;
    private ArrayList<String> folderList;

    public FolderAdapter(Context context, ArrayList<String> folderList) {
        this.context = context;
        this.folderList = folderList;
    }

    class FolderHolder extends RecyclerView.ViewHolder{

        private TextView folderName;
        public FolderHolder(FolderViewBinding binding) {
            super(binding.getRoot());
            folderName = binding.folderNameTextView;
        }
    }

    @NonNull
    @Override
    public FolderHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        FolderViewBinding binding = FolderViewBinding.inflate(layoutInflater, parent, false);
        return new FolderHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull FolderHolder holder, int position) {
        holder.folderName.setText(folderList.get(position));
    }

    @Override
    public int getItemCount() {
        return folderList.size();
    }


}
