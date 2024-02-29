package com.example.donutplayer;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.donutplayer.databinding.FolderViewBinding;

import java.util.ArrayList;

public class FolderAdapter extends RecyclerView.Adapter<FolderAdapter.FolderHolder> {
    private Context context;
    private ArrayList<FolderData> folderList;

    public FolderAdapter(Context context, ArrayList<FolderData> folderList) {
        this.context = context;
        this.folderList = folderList;
    }

    class FolderHolder extends RecyclerView.ViewHolder{

        private TextView folderName;
        private LinearLayout root;
        public FolderHolder(FolderViewBinding binding) {
            super(binding.getRoot());
            this.folderName = binding.folderNameTextView;
            this.root = binding.getRoot();
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
        holder.folderName.setText(folderList.get(position).getFolderName());
        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, FolderActivity.class);
                intent.putExtra("position", position);
                ContextCompat.startActivity(context, intent, null);
            }
        });
    }

    @Override
    public int getItemCount() {
        return folderList.size();
    }


}
