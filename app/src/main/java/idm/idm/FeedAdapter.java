package idm.idm;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import idm.idm.model.Feed;

public class FeedAdapter extends ArrayAdapter<Feed> {

    private Context mContext;
    private int mResource;
    private ArrayList<Feed> mUsers;

    private static class ViewHolder {
        TextView name;
        ImageView media_url;
        TextView caption;
        TextView timestamp;
    }

    public FeedAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Feed> objects) {
        super(context, resource, objects);

        mContext = context;
        mResource = resource;
        mUsers = objects;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        String name = getItem(position).getUser();
        String media_url = getItem(position).getMedia_url();
        String time = getItem(position).getTimestamp();
        String caption = getItem(position).getCaption();

        final View result;

        final ViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);

            convertView = inflater.inflate(R.layout.adapter_feed, parent, false);

            holder = new ViewHolder();

            holder.name = (TextView) convertView.findViewById(R.id.user);
            holder.caption = (TextView) convertView.findViewById(R.id.caption);
            holder.timestamp = (TextView) convertView.findViewById(R.id.time);
            holder.media_url = (ImageView) convertView.findViewById(R.id.picture);

            convertView.setTag(holder);
        }
        else {

            holder = (ViewHolder) convertView.getTag();
        }


        holder.name.setText(name);
        holder.caption.setText(caption);
        holder.timestamp.setText(time);
        Picasso.get().load(media_url).into(holder.media_url);


        return convertView;
    }

}
