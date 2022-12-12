package jp.co.uv.blind.model;

import java.io.Serializable;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class Direction implements Serializable {
    private int id;

    private String codeID;

    private String codeIDJP;

    private String codeType;

    private String codeBrand;

    private String codeBrandJP;

    private String coursename;

    private String coursenameJP;

    private String noPoints;

    private String placeName;

    private String footStepDirection;

    private ArrayList<Information> infos;

    private String date;

    private int fav;

    private int companyID;

    private String projectID;


    public Direction() {

    }

    public Direction(String coursename, ArrayList<Information> infos, String placeName, String footStepDirection) {
        this.coursename = coursename;
        this.infos = infos;
        this.placeName = placeName;
        this.footStepDirection = footStepDirection;
    }

    public Direction(Direction another) {
        this.id = another.id;
        this.coursename = another.coursename;
        this.infos = another.infos;
        this.placeName = another.placeName;
        this.footStepDirection = another.footStepDirection;
        this.codeID = another.codeID;
        this.companyID = another.companyID;
    }


    public String getCodeID() {
        return codeID;
    }

    public void setCodeID(String codeID) {
        this.codeID = codeID;
    }

    public String getCodeType() {
        return codeType;
    }

    public void setCodeType(String codeType) {
        this.codeType = codeType;
    }

    public String getCodeBrand() {
        return codeBrand;
    }

    public void setCodeBrand(String codeBrand) {
        this.codeBrand = codeBrand;
    }

    public String getNoPoints() {
        return noPoints;
    }

    public void setNoPoints(String noPoints) {
        this.noPoints = noPoints;
    }

    public String getCoursename() {
        return coursename;
    }

    public void setCoursename(String coursename) {
        this.coursename = coursename;
    }

    public ArrayList<Information> getInfos() {
        return infos;
    }

    public void setInfos(ArrayList<Information> infos) {
        this.infos = infos;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getFootStepDirection() {
        return footStepDirection;
    }

    public void setFootStepDirection(String footStepDirection) {
        this.footStepDirection = footStepDirection;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getFav() {
        return fav;
    }

    public void setFav(int fav) {
        this.fav = fav;
    }

    public String getCoursenameJP() {
        return coursenameJP;
    }

    public void setCoursenameJP(String coursenameJP) {
        this.coursenameJP = coursenameJP;
    }

    public String getCodeBrandJP() {
        return codeBrandJP;
    }

    public void setCodeBrandJP(String codeBrandJP) {
        this.codeBrandJP = codeBrandJP;
    }

    public String getCodeIDJP() {
        return codeIDJP;
    }

    public void setCodeIDJP(String codeIDJP) {
        this.codeIDJP = codeIDJP;
    }

    public int getCompanyID() {
        return companyID;
    }

    public void setCompanyID(int companyID) {
        this.companyID = companyID;
    }

    public String getProjectID() {
        return projectID;
    }

    public void setProjectID(String projectID) {
        this.projectID = projectID;
    }
}
