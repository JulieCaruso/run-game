package adneom.moutons_electriques.game.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import adneom.moutons_electriques.game.R;
import adneom.moutons_electriques.game.model.User;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ScoreAdapter extends RecyclerView.Adapter {

    private Context context;
    private ArrayList<User> items = new ArrayList<>();

    public ScoreAdapter(Context context, ArrayList<User> items) {
        this.context = context;
        this.items.addAll(items);
    }

    public void setItems(ArrayList<User> items) {
        this.items.clear();
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_score, parent, false);
        return new ScoreViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder vholder, int position) {
        ScoreViewHolder holder = (ScoreViewHolder) vholder;
        User user = items.get(position);
        if (user.getPhoto1() != null) {
            Picasso.with(context).load(user.getPhoto1())
                    .fit()
                    .into(holder.image);
        } else {
            holder.image.setImageDrawable(null);
        }
        holder.name.setText(user.toString());
        holder.score.setText(String.valueOf(user.getScore()));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ScoreViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.image)
        ImageView image;
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.score)
        TextView score;

        public ScoreViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
