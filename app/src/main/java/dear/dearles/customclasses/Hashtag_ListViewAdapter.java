package dear.dearles.customclasses;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.parse.ParseObject;

import java.util.ArrayList;

import dear.dearles.R;
import dear.dearles.activities.SearchResults;


public class Hashtag_ListViewAdapter extends BaseAdapter {

    // Declare Variables
    Context context;
    LayoutInflater inflater;
    ArrayList<ParseObject> HashtagRows;

    public Hashtag_ListViewAdapter(Context context, ArrayList<ParseObject> HashtagRows) {
        this.context = context;
        this.HashtagRows = HashtagRows;
        inflater = LayoutInflater.from(context);
    }

    public class ViewHolder {
        TextView Tag, Ocurrencies;
    }

    @Override
    public int getCount() {
        return HashtagRows.size();
    }

    @Override
    public Object getItem(int position) {
        return HashtagRows.get(position);
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
            holder.Tag = (TextView) view.findViewById(R.id.Tag);
            holder.Ocurrencies = (TextView) view.findViewById(R.id.Ocurrencies);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        // Set the results into TextViews ----------------------------------------------------------------------------
        String Hashtag = HashtagRows.get(position).get("Tag").toString();
        String FormattedHashtag = "#"+Hashtag.substring(0, 1).toUpperCase() + Hashtag.substring(1).toLowerCase();

        int Ocurrencies = (int) HashtagRows.get(position).get("Ocurrencies");
        String FormattedOcurrencies = String.valueOf(Ocurrencies) + " usuarias han usado este hashtag";

        holder.Tag.setText(FormattedHashtag);
        holder.Ocurrencies.setText(FormattedOcurrencies);

        // Set listener for ListView Item Click ----------------------------------------------------------------------
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // Creo el array de Strings con los nombres de los usuarios que usan el hashtag clickeado
                ArrayList<String> UsersUsingHashtag = new ArrayList<>();
                ArrayList<Object> al = (ArrayList<Object>) HashtagRows.get(position).getList("Users");
                for (Object obj : al){
                    UsersUsingHashtag.add(obj.toString().substring(1, obj.toString().length() - 1));
                }

                // Y paso como parametro dicho array de Strings y el hashtag al que pertenece dicha lista de usuarios
                Intent intent = new Intent(context, SearchResults.class);
                intent.putExtra("Hashtag", HashtagRows.get(position).get("Tag").toString());
                intent.putStringArrayListExtra("UsersUsingHashtag", UsersUsingHashtag);

                // Start SearchResults
                context.startActivity(intent);
            }
        });

        return view;
    }
}