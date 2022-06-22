package com.macdev.whatsgrouplinks.adapters;



import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;
import com.macdev.whatsgrouplinks.BottomNavigationActivity;
import com.macdev.whatsgrouplinks.GroupDetailsActivity;
import com.macdev.whatsgrouplinks.R;
import com.macdev.whatsgrouplinks.model.ModelGroups;

import java.util.List;

public class AdapterGroups extends RecyclerView.Adapter<AdapterGroups.ViewHolder> {



    String groupUrlDb;


    private List<ModelGroups> modelGroups;
    Context context;
    private int position;

    public AdapterGroups(List<ModelGroups> modelGroups, Context context) {
        this.modelGroups = modelGroups;
        this.context = context;
    }

    @NonNull
    @Override
    public AdapterGroups.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.group_list_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterGroups.ViewHolder holder, int position) {



        final ModelGroups currentGroup = modelGroups.get(position);



 //       holder.groupImage.setImageURI(modelGroups.get(position).getGroupImage());

        String url = modelGroups.get(position).getGroupImage();
        Glide.with(context)
                .load(url) // image url
                .placeholder(R.drawable.ic_image_24) // any placeholder to load at start
                .error(R.drawable.ic_image_24)  // any image in case of error
                .override(100, 100) // resizing
                .centerCrop()
                .into(holder.groupImage);
        holder.groupTitleText.setText(modelGroups.get(position).getGroupName());
        holder.groupCategoryText.setText(modelGroups.get(position).getGroupCategory());

        holder.groupLinkHolder.setText(modelGroups.get(position).getGroupLink());

        holder.cardViewGroupList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(context, GroupDetailsActivity.class);
                intent.putExtra("image",currentGroup.getGroupImage());
                intent.putExtra("groupTitle",currentGroup.getGroupName());
                intent.putExtra("groupCategory",currentGroup.getGroupCategory());
                intent.putExtra("groupLink",currentGroup.getGroupLink());

                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);



            }
        });




        holder.joinBtnLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               groupUrlDb = currentGroup.getGroupLink();


                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(groupUrlDb));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                if (intent.resolveActivity(context.getPackageManager()) != null) {
                    context.startActivity(intent);
                }
//                Uri groupUrl = Uri.parse(groupUrlDb);
//                Intent intent = new Intent(Intent.ACTION_SENDTO,groupUrl);
//                view.getContext().startActivity(intent);



            }
        });

    }

    @Override
    public int getItemCount() {
        return modelGroups.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        public TextView groupTitleText, groupCategoryText, groupLinkHolder;
        public ShapeableImageView groupImage;
        public LinearLayout joinBtnLayout;
        public ImageView groupIcon;
        public CardView cardViewGroupList;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);


          this.groupTitleText = (TextView) itemView.findViewById(R.id.text_group_title);
         this.groupCategoryText = (TextView) itemView.findViewById(R.id.text_group_category);
         this.groupImage = (ShapeableImageView) itemView.findViewById(R.id.group_Image);
         this.groupIcon= (ImageView) itemView.findViewById(R.id.image_button_add);
         this.groupLinkHolder = (TextView) itemView.findViewById(R.id.groupLinkTextHidden);
         this.joinBtnLayout = (LinearLayout) itemView.findViewById(R.id.join_layout);
         this.cardViewGroupList = (CardView) itemView.findViewById(R.id.card_view_group_list);







        }



    }
}
