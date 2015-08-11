package dear.dearles.customclasses;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import dear.dearles.R;
import dear.dearles.activities.SingleItemView;


public class ListViewAdapter extends BaseAdapter {

    // Declare Variables
    Context context;
    LayoutInflater inflater;
    ImageLoader imageLoader;
    private List<User> UserList = null;
    private ArrayList<User> arraylist;

    public ListViewAdapter(Context context, List<User> UserList) {
        this.context = context;
        this.UserList = UserList;

        inflater = LayoutInflater.from(context);

        this.arraylist = new ArrayList<User>();
        this.arraylist.addAll(UserList);

        imageLoader = new ImageLoader(context);
    }











    public class ViewHolder {
        TextView Username;
        TextView Age;
        TextView Description;
        ImageView Thumbnail;
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
            view = inflater.inflate(R.layout.singleitemview, null);

            // Locate the TextViews in listview_item.xml
            holder.Username = (TextView) view.findViewById(R.id.Username);
            holder.Age = (TextView) view.findViewById(R.id.Age);
            holder.Description = (TextView) view.findViewById(R.id.Description);
            holder.Thumbnail = (ImageView) view.findViewById(R.id.Thumbnail);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Set the results into TextViews
        holder.Username.setText(UserList.get(position).getUsername());
        holder.Age.setText(UserList.get(position).getAge());
        holder.Description.setText(UserList.get(position).getDescription());
        // Set the results into ImageView
        imageLoader.DisplayImage(UserList.get(position).getThumbnail(), holder.Thumbnail);
        // Listen for ListView Item Click
        view.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // Send single item click data to SingleItemView Class
                Intent intent = new Intent(context, SingleItemView.class);
                // Pass all data rank
                intent.putExtra("username", (UserList.get(position).getUsername()));
                // Pass all data country
                intent.putExtra("age", (UserList.get(position).getAge()));
                 // Pass all data population
                intent.putExtra("Description", UserList.get(position).getDescription());
                // Pass all data flag
                intent.putExtra("Thumbnail", (UserList.get(position).getThumbnail()));
                // Start SingleItemView Class
                context.startActivity(intent);
            }
        });
        return view;
    }
}