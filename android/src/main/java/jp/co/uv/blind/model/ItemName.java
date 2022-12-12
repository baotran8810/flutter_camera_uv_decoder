package jp.co.uv.blind.model;

import java.util.ArrayList;

public class ItemName implements Comparable<ItemName> {
    private String name;
    private boolean isSelected;
    private float distance;
    private ArrayList<Information> infos;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public ArrayList<Information> getInfos() {
        return infos;
    }

    public void setInfos(ArrayList<Information> infos) {
        this.infos = infos;
    }

    @Override
    public int compareTo(ItemName o) {
        return Float.compare(getDistance(), o.getDistance());
    }
}
