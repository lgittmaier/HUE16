package at.htlgkr.steam;


import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class SteamBackend {
    List<Game> games;

    public SteamBackend() {
         games = new ArrayList<>();

    }

    public void loadGames(InputStream inputStream)  {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            String line = br.readLine();
            while ((line = br.readLine()) != null) {
                String[] tmp = line.split(";");
                games.add(new Game(tmp[0], sdf.parse(tmp[1]), Double.parseDouble(tmp[2])));
            }
        } catch (IOException e) {
            System.err.println("IOException");
        } catch (ParseException x) {
            System.err.println("ParseException");
        }
    }

    public void store(OutputStream fileOutputStream) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        try (BufferedWriter br = new BufferedWriter(new OutputStreamWriter(fileOutputStream))) {
            for (Game g : games) {
                br.write(g.getName() + ";" + sdf.format(g.getReleaseDate()) + ";" + g.getPrice());
                br.newLine();
            }
            br.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Game> getGames() {
        return this.games;
    }

    public void setGames(List<Game> games) {
        this.games = games;
    }

    public void addGame(Game newGame) {
        games.add(newGame);
    }

    public double sumGamePrices() {
        return games.stream().mapToDouble(Game::getPrice).sum();
    }

    public double averageGamePrice() {
        return games.stream().mapToDouble(Game::getPrice).sum() / games.size();

    }

    public List<Game> getUniqueGames() {

        Predicate<Game> same = c -> games.stream().anyMatch(x -> x.getName().equals(c.getName()));
        return games.stream().filter(same).distinct().collect(Collectors.toList());
    }

    public List<Game> selectTopNGamesDependingOnPrice(int n) {
        return games.stream().sorted((o1, o2) -> (int) (o2.getPrice() - o1.getPrice())).limit(n).collect(Collectors.toList());

    }
}



