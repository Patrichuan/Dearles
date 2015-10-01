package dear.dearles.customclasses;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

import dear.dearles.R;
import dear.dearles.activities.UserBigProfile;
import dear.dearles.glide.CropSquareTransformation;

/**
 * Created by Pat on 01/10/2015.
 */
public class Hashtag_ListViewAdapter extends BaseAdapter {

    // Declare Variables
    Context context;
    LayoutInflater inflater;
    ArrayList<ParseObject> HashtagList;

    public Hashtag_ListViewAdapter(Context context, ArrayList<ParseObject> HashtagList) {
        this.context = context;
        this.HashtagList = HashtagList;
        inflater = LayoutInflater.from(context);
    }

    public class ViewHolder {
        TextView Tag, Ocurrencies;
    }

    @Override
    public int getCount() {
        return HashtagList.size();
    }

    @Override
    public Object getItem(int position) {
        return HashtagList.get(position);
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
            view = inflater.inflate(R.layout.listview_hashtag_item, null);
            // Locate the TextViews in listview_user_item.xml
            holder.Tag = (TextView) view.findViewById(R.id.Tag);
            holder.Ocurrencies = (TextView) view.findViewById(R.id.Ocurrencies);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        // Set the results into TextViews ----------------------------------------------------------------------------
        String Hashtag = HashtagList.get(position).get("Tag").toString();
        Hashtag = "#"+Hashtag.substring(0, 1).toUpperCase() + Hashtag.substring(1).toLowerCase();

        String Ocurrencies = HashtagList.get(position).get("Ocurrencies").toString();
        Ocurrencies = Ocurrencies + " usuarias han usado este hashtag";

        holder.Tag.setText(Hashtag);
        holder.Ocurrencies.setText(Ocurrencies);
        return view;

        /*
        // Set listener for ListView Item Click ----------------------------------------------------------------------
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // Send single item click data to UserBigProfile Class
                Intent intent = new Intent(context, UserBigProfile.class);
                intent.putExtra("username", (UserList.get(position).getUsername()));
                // Start UserBigProfile Class
                context.startActivity(intent);
            }
        });
        */

    }
}