package com.example.foodapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foodapp.Domain.CategoryDomain;
import com.example.foodapp.R;
import com.example.foodapp.VegActivity;
import com.example.foodapp.item_details;
import com.example.foodapp.manageitems;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    Context context;
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

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int clickedPosition = holder.getAdapterPosition();

                if (clickedPosition == 0) {
                    // Clicked on the first item, start Activity1
                    Intent intent = new Intent(view.getContext(), item_details.class);
                    view.getContext().startActivity(intent);
                } else if (clickedPosition == 1) {
                    // Clicked on the second item, start Activity2
                    Intent intent = new Intent(view.getContext(), VegActivity.class);
                    view.getContext().startActivity(intent);
                } else if (clickedPosition == 2){
                    Intent intent = new Intent(view.getContext(), item_details.class);
                    view.getContext().startActivity(intent);
                    // Handle other items or show a message if needed
                    Toast.makeText(view.getContext(), "Clicked on item " + clickedPosition, Toast.LENGTH_SHORT).show();
                }
            }
        });
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
