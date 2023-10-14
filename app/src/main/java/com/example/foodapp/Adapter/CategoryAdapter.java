package com.example.foodapp.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foodapp.Domain.CategoryDomain;
import com.example.foodapp.R;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    public CategoryAdapter(ArrayList<CategoryDomain> categoryDomains) {
        this.categoryDomains = categoryDomains;
    }

    ArrayList<CategoryDomain> categoryDomains;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate= LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_category,parent,false);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.categoryName.setText(categoryDomains.get(position).getTitle());
        String picUrl="";
        switch (position){
            case 0:{
                picUrl="cat_1";
                break;
            }
            case 1:{
                picUrl="cat_2";
                break;
            }
            case 2:{
                picUrl="cat_3";
                break;
            }
            case 3:{
                picUrl="cat_4";
                break;
            }

        }
        int drawableResouceId=holder.itemView.getContext().getResources().getIdentifier(picUrl,"drawable",holder.itemView.getContext().getPackageName());
        Glide.with((holder.itemView.getContext())).load(drawableResouceId).into(holder.categorypic);
    }

    @Override
    public int getItemCount() {
        return categoryDomains.size();
    }
   public class  ViewHolder extends RecyclerView.ViewHolder{
TextView categoryName;
ImageView categorypic;
ConstraintLayout mainLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryName=itemView.findViewById(R.id.categoryName);
            categorypic=itemView.findViewById(R.id.categoryPic);

        }
    }
}
