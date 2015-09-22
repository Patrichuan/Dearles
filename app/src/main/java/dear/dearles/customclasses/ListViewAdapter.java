package dear.dearles.customclasses;

import java.util.ArrayList;
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
    private List<User> UserList = null;
    private ArrayList<User> arraylist;

    public ListViewAdapter(Context context, List<User> UserList) {
        this.context = context;
        this.UserList = UserList;

        inflater = LayoutInflater.from(context);

        arraylist = new ArrayList<User>();
        arraylist.addAll(UserList);
    }



    public class ViewHolder {
        TextView Username;
        TextView Age;
        TextView Description;
        TextView Distance;
        ImageView ProfilePicture;
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

        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.listview_item, null);

            // Locate the TextViews in listview_item.xml
            holder.Username = (TextView) view.findViewById(R.id.Username);
            holder.Age = (TextView) view.findViewById(R.id.Age);
            holder.Distance = (TextView) view.findViewById(R.id.DistanceTo);
            holder.Description = (TextView) view.findViewById(R.id.Description);
            holder.ProfilePicture = (ImageView) view.findViewById(R.id.ProfilePicture);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        // Set the results into TextViews
        holder.Username.setText(UserList.get(position).getUsername().toUpperCase());
        holder.Age.setText(UserList.get(position).getAge() + " años");
        holder.Distance.setText(String.valueOf(UserList.get(position).getDistance()) + " Km");
        holder.Description.setText(UserList.get(position).getDescription());
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

        // Listen for ListView Item Click
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