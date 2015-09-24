package dear.dearles.customclasses;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import dear.dearles.R;
import dear.dearles.activities.UserBigProfile;
import dear.dearles.glide.CropSquareTransformation;


public class ListViewAdapter extends BaseAdapter {

    // Declare Variables
    Context context;
    LayoutInflater inflater;
    List<User> UserList;
    ArrayList<User> arraylist;
    DecimalFormat df = new DecimalFormat("####0.000");

    final static double DISTANCE_NEAR_IN_KILOMETERS = 5;
    final static double DISTANCE_FAR_IN_KILOMETERS = 10;
    final static double DISTANCE_VERY_FAR_IN_KILOMETERS = 15;


    public ListViewAdapter(Context context, List<User> UserList) {
        this.context = context;
        this.UserList = UserList;

        // Declaro, ordeno por distancia respecto al usuario de - a + y añado al arraylist
        arraylist = new ArrayList<User>();
        Collections.sort(UserList);
        arraylist.addAll(UserList);

        inflater = LayoutInflater.from(context);
    }

    public class ViewHolder {
        TextView Username, Age, Description;
        ImageView DistanceColored, ProfilePicture;
    }

    @Override
    public int getCount() {
        return UserList.size();
    }

    @Override
    public Object getItem(int position) {
        return UserList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;

        // Set holder ------------------------------------------------------------------------------------------------
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.listview_item, null);
            // Locate the TextViews in listview_item.xml
            holder.Username = (TextView) view.findViewById(R.id.Username);
            holder.Age = (TextView) view.findViewById(R.id.Age);
            holder.DistanceColored = (ImageView) view.findViewById(R.id.DistanceColored);
            holder.Description = (TextView) view.findViewById(R.id.Description);
            holder.ProfilePicture = (ImageView) view.findViewById(R.id.ProfilePicture);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        // Set the results into TextViews ----------------------------------------------------------------------------
        holder.Username.setText(UserList.get(position).getUsername().toUpperCase());
        holder.Age.setText(UserList.get(position).getAge() + " años");
        holder.Description.setText(UserList.get(position).getDescription());
        // Color the distance indicator
        Double DistanceFromMe = UserList.get(position).getDistance();
        if (DistanceFromMe<=DISTANCE_NEAR_IN_KILOMETERS){
            holder.DistanceColored.setBackgroundColor(context.getResources().getColor(R.color.distance_near));
        } else if (DistanceFromMe<=DISTANCE_FAR_IN_KILOMETERS) {
            holder.DistanceColored.setBackgroundColor(context.getResources().getColor(R.color.distance_far));
        } else {
            holder.DistanceColored.setBackgroundColor(context.getResources().getColor(R.color.distance_very_far));
        }
        // Set the profile image
        Glide.with(context)
                .load(UserList.get(position).getProfilePicture())
                .asBitmap()
                .transform(new CropSquareTransformation(context))
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        holder.ProfilePicture.setImageBitmap(resource);
                    }
                });


        // Set listener for ListView Item Click ----------------------------------------------------------------------
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // Send single item click data to UserBigProfile Class
                Intent intent = new Intent(context, UserBigProfile.class);
                intent.putExtra("username", (UserList.get(position).getUsername()));
                intent.putExtra("age", (UserList.get(position).getAge()));
                intent.putExtra("description", UserList.get(position).getDescription());
                intent.putExtra("profilePicture", (UserList.get(position).getProfilePicture()));
                // Start UserBigProfile Class
                context.startActivity(intent);
            }
        });

        return view;
    }

}