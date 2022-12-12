package jp.co.uv.blind.model;

import java.util.ArrayList;

public class Code {
    private ArrayList<Direction> directions;
    private ArrayList<Facility> facilities;

    public ArrayList<Direction> getDirections() {
        return directions;
    }

    public void setDirections(ArrayList<Direction> directions) {
        this.directions = directions;
    }

    public ArrayList<Facility> getFacilities() {
        return facilities;
    }

    public void setFacilities(ArrayList<Facility> facilities) {
        this.facilities = facilities;
    }
}
