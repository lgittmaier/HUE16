package at.htlgkr.steamgameapp;

import at.htlgkr.steam.ReportType;

public class ReportTypeSpinnerItem {
    private  ReportType type;
    private String displayText;
    public ReportTypeSpinnerItem(ReportType type, String displayText) {
       this.type = type;
       this.displayText = displayText;
    }

    public ReportType getType(){
        return this.type;
    }

    public String getDisplayText() {
        return this.displayText;
    }

    @Override
    public String toString() {

        return getDisplayText();
    }

}