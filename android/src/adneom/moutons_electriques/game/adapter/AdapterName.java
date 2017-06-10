package adneom.moutons_electriques.game.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import adneom.moutons_electriques.game.R;
import adneom.moutons_electriques.game.model.User;

public class AdapterName extends ArrayAdapter<User> {
    private ArrayList<User> items = new ArrayList<>();
    private ArrayList<User> filteredItems = new ArrayList<>();
    private Context context;
    private int viewRessourceId;

    private OnClickUser callback;

    private CharSequence currentConstraint;

    public interface OnClickUser {
        void onClick(User user);
    }

    public AdapterName(Context context, ArrayList<User> users, int viewRessourceId, OnClickUser callback) {
        super(context, viewRessourceId, users);
        this.items.addAll(users);
        this.filteredItems.addAll(users);
        this.viewRessourceId = viewRessourceId;
        this.context = context;
        this.callback = callback;
    }

    public void setItems(ArrayList<User> users) {
        items.clear();
        items.addAll(users);
        notifyDataSetChanged();
        getFilter().filter(currentConstraint);
    }

    @Override
    public int getCount() {
        return filteredItems.size();
    }

    @Nullable
    @Override
    public User getItem(int position) {
        return this.filteredItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            convertView = inflater.inflate(viewRessourceId, parent, false);
        }
        final User user = getItem(position);
        TextView name = (TextView) convertView.findViewById(R.id.text_normal);
        name.setText(user.toString());
        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onClick(user);
            }
        });
        return convertView;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            currentConstraint = constraint;
            FilterResults filterResults = new FilterResults();
            List<Object> results = new ArrayList<>();

            if (constraint == null || constraint.toString().length() == 0) {
                filterResults.count = items.size();
                filterResults.values = new ArrayList<>(items);
            } else {
                String constraintText = constraint.toString().toLowerCase();
                for (User user : items) {
                    if (user.toString().toLowerCase().contains(constraintText)) {
                        results.add(user);
                    }
                }
                filterResults.count = results.size();
                filterResults.values = results;
            }
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            ArrayList<User> list = (ArrayList<User>) results.values;
            filteredItems.clear();
            filteredItems.addAll(list);
            notifyDataSetChanged();
        }
    };
}

