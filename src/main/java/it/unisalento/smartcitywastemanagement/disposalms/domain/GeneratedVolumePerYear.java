package it.unisalento.smartcitywastemanagement.disposalms.domain;

import org.bson.types.Decimal128;
import java.util.Map;

public class GeneratedVolumePerYear {


    private int year;

    private Decimal128 mixedWaste;

    private Map<String, Decimal128> sortedWaste;


    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public Decimal128 getMixedWaste() {
        return mixedWaste;
    }

    public void setMixedWaste(Decimal128 mixedWaste) {
        this.mixedWaste = mixedWaste;
    }

    public Map<String, Decimal128> getSortedWaste() {
        return sortedWaste;
    }

    public void setSortedWaste(Map<String, Decimal128> sortedWaste) {
        this.sortedWaste = sortedWaste;
    }
}
