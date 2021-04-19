package at.htlgkr.steamgameapp;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.zip.Inflater;

import at.htlgkr.steam.Game;

public class GameAdapter extends BaseAdapter {
    List<Game> games;
    LayoutInflater inflater;

    public GameAdapter(Context context , List<Game> games) {
        this.games = games;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return games.size();
    }

    @Override
    public Object getItem(int position) {
        return games.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View givenView, ViewGroup parent) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        Game game = games.get(position);
        View listItem = (givenView == null) ? inflater.inflate(R.layout.game_item_layout, null) : givenView;
        ((TextView) listItem.findViewById(R.id.txt_brand)).setText(game.getName());
        ((TextView) listItem.findViewById(R.id.txt_model)).setText(sdf.format(game.getReleaseDate()));
        ((TextView) listItem.findViewById(R.id.txt_price)).setText(game.getPrice() + "");
        return listItem;
    }
}