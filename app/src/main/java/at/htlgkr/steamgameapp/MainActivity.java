package at.htlgkr.steamgameapp;

import android.content.DialogInterface;
import android.content.res.AssetManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import at.htlgkr.steam.Game;
import at.htlgkr.steam.ReportType;
import at.htlgkr.steam.SteamBackend;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    private static final String GAMES_CSV = "games.csv";
    ListView lv;
    SteamBackend sb = new SteamBackend();
    List<ReportTypeSpinnerItem> spinnerItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lv = findViewById(R.id.gamesList);

        loadGamesIntoListView();
        setUpReportSelection();
        setUpSearchButton();
        setUpAddGameButton();
        setUpSaveButton();
    }

    private void loadGamesIntoListView() {
        AssetManager assets = getAssets();

        try {
            sb.loadGames(assets.open(GAMES_CSV));
            GameAdapter gameAdapter = new GameAdapter(this, sb.getGames());
            lv.setAdapter(gameAdapter);
        } catch (IOException e) {
            System.out.println("IOException");
        }
    }

    private void setUpReportSelection() {
        Spinner s= findViewById(R.id.chooseReport);
        s.setOnItemSelectedListener(this);
        fillSpinnerItems();
        ArrayAdapter ar = new ArrayAdapter(this, android.R.layout.simple_list_item_1,spinnerItems);
        s.setAdapter(ar);
    }

    private void setUpSearchButton() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        LinearLayout ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.VERTICAL);
        EditText et = new EditText(this);
        et.setId(R.id.dialog_search_field);
        et.setHint("Name");
        ll.addView(et);
        Button search = findViewById(R.id.search);
        search.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                alert.setTitle(SteamGameAppConstants.ENTER_SEARCH_TERM);
                alert.setView(ll);
                alert.setPositiveButton("OK!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (et.getText() != null || !(et.getText().toString().equals(""))) {

                            List<Game> games = new ArrayList<>();
                            String searching = et.getText().toString();
                            for (Game g : sb.getGames()) {
                                if (g.getName().toLowerCase().contains(searching.toLowerCase())) {
                                    games.add(g);
                                }
                            }
                            bindViewtoAdapter(games);

                        }
                        else{
                            bindViewtoAdapter(sb.getGames());
                            setUpSearchButton();
                        }
                    }
                });
                alert.setNegativeButton("Back", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                alert.show();
            }
        });
    }

    private void setUpAddGameButton() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        LinearLayout ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.VERTICAL);
        EditText et1 = new EditText(this);
        et1.setId(R.id.dialog_name_field);
        et1.setHint("Name");
        ll.addView(et1);
        EditText et2 = new EditText(this);
        et2.setId(R.id.dialog_date_field);
        et2.setHint("Date");
        ll.addView(et2);
        EditText et3 = new EditText(this);
        et3.setId(R.id.dialog_price_field);
        et3.setHint("Price");
        ll.addView(et3);
        Button addGame = findViewById(R.id.addGame);
        addGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alert.setTitle(SteamGameAppConstants.NEW_GAME_DIALOG_TITLE);
                alert.setView(ll);
                alert.setPositiveButton("OK!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
                        Game g = null;
                        try {
                            g = new Game(et1.getText().toString(),sdf.parse(et2.getText().toString()), Double.parseDouble(et3.getText().toString()));
                            sb.addGame(g);

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        bindViewtoAdapter(sb.getGames());
                        setUpAddGameButton();
                    }
                });
                alert.setNegativeButton("Back", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        setUpAddGameButton();
                    }
                });
                alert.show();
            }
        });
    }

    private void setUpSaveButton() {
        Button save = findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    sb.store(openFileOutput(SteamGameAppConstants.SAVE_GAMES_FILENAME,MODE_PRIVATE));
                    setUpSaveButton();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    public void bindViewtoAdapter(List<Game> list) {
        GameAdapter adapter = new GameAdapter(this,list);
        lv.setAdapter(adapter);
    }
    private void fillSpinnerItems() {
        spinnerItems.add(new ReportTypeSpinnerItem(ReportType.NONE, SteamGameAppConstants.SELECT_ONE_SPINNER_TEXT));
        spinnerItems.add(new ReportTypeSpinnerItem(ReportType.SUM_GAME_PRICES, SteamGameAppConstants.SUM_GAME_PRICES_SPINNER_TEXT));
        spinnerItems.add(new ReportTypeSpinnerItem(ReportType.AVERAGE_GAME_PRICES, SteamGameAppConstants.AVERAGE_GAME_PRICES_SPINNER_TEXT));
        spinnerItems.add(new ReportTypeSpinnerItem(ReportType.UNIQUE_GAMES, SteamGameAppConstants.UNIQUE_GAMES_SPINNER_TEXT));
        spinnerItems.add(new ReportTypeSpinnerItem(ReportType.MOST_EXPENSIVE_GAMES, SteamGameAppConstants.MOST_EXPENSIVE_GAMES_SPINNER_TEXT));
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        ReportTypeSpinnerItem rtsi = (ReportTypeSpinnerItem) adapterView.getItemAtPosition(i);
        String message = null;
        switch (rtsi.getType()) {
            case SUM_GAME_PRICES:
                message = SteamGameAppConstants.ALL_PRICES_SUM  + sb.sumGamePrices();
                break;
            case AVERAGE_GAME_PRICES:
                message = SteamGameAppConstants.ALL_PRICES_AVERAGE  + sb.averageGamePrice();
                break;
            case UNIQUE_GAMES:
                message = SteamGameAppConstants.UNIQUE_GAMES_COUNT  + sb.getUniqueGames().size();
                break;
            case MOST_EXPENSIVE_GAMES:
                message = SteamGameAppConstants.MOST_EXPENSIVE_GAMES;
                List<Game> list = sb.selectTopNGamesDependingOnPrice(3);
                for (int j = 0; j < list.size(); j++) {
                    message += "\n" + list.get(j).toString();
                }
                break;
            default:
                message = null;
                break;
        }
        if (message != null) {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle(rtsi.getDisplayText()).setMessage(message).setNeutralButton("OK", null).show();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
