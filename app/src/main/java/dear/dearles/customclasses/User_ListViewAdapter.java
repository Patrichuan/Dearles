package dear.dearles.customclasses;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import dear.dearles.R;
import dear.dearles.activities.UserBigProfile;
import dear.dearles.glide.CropSquareTransformation;


public class User_ListViewAdapter extends BaseAdapter {

    /* Regex Explained:
         #      - A literal #
         (      - Start of capture group
         \\w+   - One or more word characters
         )      - End of capture group
    */
    Pattern MY_PATTERN = Pattern.compile("#(\\w+)");

    // Declare Constants
    final static double DISTANCE_NEAR_IN_KILOMETERS = 5;
    final static double DISTANCE_FAR_IN_KILOMETERS = 10;
    final static double DISTANCE_VERY_FAR_IN_KILOMETERS = 15;

    // Declare Variables
    Context context;
    LayoutInflater inflater;
    List<User> UserList;
    ArrayList<User> UserArraylist;
    ArrayList<String> UserHashtags;
    String Description;


    public User_ListViewAdapter(Context context, List<User> UserList) {
        this.context = context;
        this.UserList = UserList;

        // Declaro, ordeno por distancia respecto al usuario de - a + y añado al arraylist
        UserArraylist = new ArrayList<User>();
        UserHashtags = new ArrayList<String>();
        Collections.sort(UserList);
        UserArraylist.addAll(UserList);

        inflater = LayoutInflater.from(context);
    }

    public class ViewHolder {
        TextView Username, Age, Description, TestName;
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
            view = inflater.inflate(R.layout.listview_user_item, null);
            // Locate the TextViews in listview_user_itemm.xml.xml
            holder.Username = (TextView) view.findViewById(R.id.Username);
            holder.TestName = (TextView) view.findViewById(R.id.testUsername);
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
        holder.TestName.setText("< 5km");

        holder.Age.setText(", " + UserList.get(position).getAge());

        // Change color of Description Hashtags and make them bold. Then add them to a list
        Description = UserList.get(position).getDescription();
        SpannableStringBuilder sb = new SpannableStringBuilder(Description);
        Matcher mat = MY_PATTERN.matcher(Description);
        while (mat.find()) {
            UserHashtags.add(mat.group(1));
            sb.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.hashtagColor)), mat.start(), mat.end(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            sb.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), mat.start(), mat.end(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        }
        holder.Description.setText(sb);

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
                intent.putExtra("profilePicture", (UserList.get(position).getProfilePicture()));
                intent.putExtra("description", (UserList.get(position).getDescription()));
                /*
                intent.putStringArrayListExtra("hashtags", UserHashtags);
                UserHashtags = new ArrayList<String>();
                */
                // Start UserBigProfile Class
                context.startActivity(intent);
            }
        });

        return view;
    }

}