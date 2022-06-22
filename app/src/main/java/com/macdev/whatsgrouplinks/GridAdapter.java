package com.macdev.whatsgrouplinks;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.macdev.whatsgrouplinks.ui.categories.CategoriesFragment;

public class GridAdapter extends BaseAdapter {

    LayoutInflater inflater;

    Context context;
    String[] mCategoriesName;
    int[] mCategoryImages;

    public GridAdapter(Context context, String[] mCategoriesName, int[] mCategoryImages) {
        this.context = context;
        this.mCategoriesName = mCategoriesName;
        this.mCategoryImages = mCategoryImages;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mCategoryImages.length;
    }

    @Override
    public Object getItem(int i) {
        return mCategoryImages[i];
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if(view == null){
            view = inflater.inflate(R.layout.item_categories, null);
        }

        TextView textView = view.findViewById(R.id.category_name);
        textView.setText(mCategoriesName[i]);

        ImageView imageView = view.findViewById(R.id.image_category);
        imageView.setImageResource(mCategoryImages[i]);

        return view;
    }
}
