package idm.idm;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

import idm.idm.model.User;

public class UsersAdapter extends ArrayAdapter<User> {

    private Context mContext;
    private int mResource;
    private ArrayList<User> mUsers;

    private static class ViewHolder {
        TextView name;
        TextView firstName;
        TextView lastName;
    }

    public UsersAdapter(@NonNull Context context, int resource, @NonNull ArrayList<User> objects) {
        super(context, resource, objects);

        mContext = context;
        mResource = resource;
        mUsers = objects;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        String name = getItem(position).getUsername();
        String firstName = getItem(position).getFirstName();
        String lastName = getItem(position).getLastName();

        final View result;

        final ViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);

            convertView = inflater.inflate(R.layout.adapter_users, parent, false);

            holder = new ViewHolder();

            holder.name = (TextView) convertView.findViewById(R.id.userName);
            holder.firstName = (TextView) convertView.findViewById(R.id.firstName);
            holder.lastName = (TextView) convertView.findViewById(R.id.lastName);

            convertView.setTag(holder);
        }
        else {

            holder = (ViewHolder) convertView.getTag();
        }


        holder.name.setText(name);
        holder.firstName.setText(firstName);
        holder.lastName.setText(lastName);

        return convertView;
    }

}
