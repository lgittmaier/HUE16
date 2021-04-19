package at.htlgkr.steam;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class Game {
    public static final String DATE_FORMAT = "dd.MM.yyyy";

    private String name;
    private Date releaseDate;
    private double price;

    public Game(String name, Date releaseDate, double price) {
        this.name=name;
        this.releaseDate = releaseDate;
        this.price = price;
    }
    public Game(){

    }

    public String getName() {
       return this.name;
    }

    public void setName(String name) {
        // Implementieren Sie diese Methode
        this.name = name;
    }

    public Date getReleaseDate() {
        // Implementieren Sie diese Methode
        return this.releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public double getPrice() {
        return this.price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        return "[" + sdf.format(getReleaseDate()) + "] " + getName() + " " + getPrice();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Game)) return false;
        Game game = (Game) o;
        return getName().equals(game.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getReleaseDate(), getPrice());
    }


    public String toStringwrite() {
       return this.getName() +";"+ this.releaseDate +";"+this.price;
    }
}

