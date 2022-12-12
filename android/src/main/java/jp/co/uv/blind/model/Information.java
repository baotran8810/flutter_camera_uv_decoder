package jp.co.uv.blind.model;
import androidx.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class Information implements Comparable<Information>{
    private String routenumber;
    private String pointnumber;

    private String pointname;

    private String pointnameJP;

    private String position_info;
    private String beacon_id;
    private String barrier_info;

    private String substitute;

    private String substituteJP;

    private ArrayList<String> facilityCategory;

    private String direction;
    private String distance;
    private String walkingMin;
    private String time;
//    private boolean isCheckIn;

    public Information(){

    }
    public Information(String routenumber, String pointnumber, String pointname, String position_info, String barrier_info) {
        this.routenumber = routenumber;
        this.pointnumber = pointnumber;
        this.pointname = pointname;
        this.position_info = position_info;
        this.barrier_info = barrier_info;
    }

    public String getSubstitute() {
        return substitute;
    }

    public void setSubstitute(String substitute) {
        this.substitute = substitute;
    }

    public String getRoutenumber() {
        return routenumber;
    }

    public void setRoutenumber(String routenumber) {
        this.routenumber = routenumber;
    }

    public String getPointnumber() {
        return pointnumber;
    }

    public void setPointnumber(String pointnumber) {
        this.pointnumber = pointnumber;
    }

    public String getPointname() {
        return pointname;
    }

    public void setPointname(String pointname) {
        this.pointname = pointname;
    }

    public String getPosition_info() {
        return position_info;
    }

    public void setPosition_info(String position_info) {
        this.position_info = position_info;
    }

    public String getBarrier_info() {
        return barrier_info;
    }

    public void setBarrier_info(String barrier_info) {
        this.barrier_info = barrier_info;
    }

    public String getBeacon_id() {
        return beacon_id;
    }

    public void setBeacon_id(String beacon_id) {
        this.beacon_id = beacon_id;
    }
    public ArrayList<String> getFacilityCategory() {
        return facilityCategory;
    }

    public void setFacilityCategory(ArrayList<String> facilityCategory) {
        this.facilityCategory = facilityCategory;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPointnameJP() {
        return pointnameJP;
    }

    public void setPointnameJP(String pointnameJP) {
        this.pointnameJP = pointnameJP;
    }

    public String getSubstituteJP() {
        return substituteJP;
    }

    public void setSubstituteJP(String substituteJP) {
        this.substituteJP = substituteJP;
    }

    public String getWalkingMin() {
        return walkingMin;
    }

    public void setWalkingMin(String walkingMin) {
        this.walkingMin = walkingMin;
    }
//
//    public boolean isCheckIn() {
//        return isCheckIn;
//    }
//
//    public void setCheckIn(boolean checkIn) {
//        isCheckIn = checkIn;
//    }

    @Override
    public int compareTo(@NonNull Information o) {
        return getFacilityCategory().get(0).compareTo(o.getFacilityCategory().get(0));
    }
    public LatLng getLocation(){
        String locationInformation = getPosition_info();
        String[] location = locationInformation.split(",");
        double lat = Double.parseDouble(location[0]);
        double lng = Double.parseDouble(location[1]);
        LatLng latLng = new LatLng(lat,lng);
        return latLng;
    }
}
