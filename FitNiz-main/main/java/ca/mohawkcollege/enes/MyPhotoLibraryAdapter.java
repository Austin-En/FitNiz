package ca.mohawkcollege.enes;


import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class MyPhotoLibraryAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final ArrayList<Bitmap[]> images;
    private final String[] months;

    /**
     * This adapter is used to display the listview with images that are saved inside the database
     * @param context
     * @param months This string array should be 24 strings to display the current year and the past year months
     * @param images This array holds 2 images from each month for the current year and the past year (The first one found in the month and the last one found in the month)
     */
    public MyPhotoLibraryAdapter(Context context, String[] months, ArrayList<Bitmap[]> images) {
        super(context, R.layout.photo_list_item, months);
        this.context = context;
        this.months = months;
        this.images = images;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.photo_list_item, parent, false);

        final TextView textView = (TextView) rowView.findViewById(R.id.text_view_month);

        textView.setText(months[position]);


        final ImageView imageView = (ImageView) rowView.findViewById(R.id.image_view_photo1);
        final ImageView imageView2 = (ImageView) rowView.findViewById(R.id.image_view_photo2);

        if(images.get(position)[0] != null){
            imageView.setImageBitmap(images.get(position)[0]);
        }

        if(images.get(position)[1] != null) {
            imageView2.setImageBitmap(images.get(position)[1]);
        }

        return rowView;
    }
}
