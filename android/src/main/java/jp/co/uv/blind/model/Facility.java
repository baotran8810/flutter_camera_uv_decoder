package jp.co.uv.blind.model;
import java.io.Serializable;
import java.util.ArrayList;

public class Facility implements Serializable {
    private int id;

    private String codeTypes;

    private String brochureCode;

    private String branchNumber;

    private String brochureName;

    private String brochureNameJP;

    private String date;

    private int fav;

    private ArrayList<Information> infos;

    private int companyID;

    private String codeID;

    private String projectID;

    private boolean isChoosen;

    public Facility() {
    }

    public Facility(Facility clone) {
        this.id = clone.id;
        this.codeTypes = clone.codeTypes;
        this.brochureCode =clone. brochureCode;
        this.branchNumber = clone.branchNumber;
        this.brochureName = clone.brochureName;
        this.brochureNameJP = clone.brochureNameJP;
        this.date =clone. date;
        this.fav = clone.fav;
        this.infos = clone.infos;
        this.isChoosen = clone.isChoosen;
        this.companyID = clone.companyID;
        this.codeID = clone.codeID;
    }

    public int getCompanyID() {
        return companyID;
    }

    public void setCompanyID(int companyID) {
        this.companyID = companyID;
    }

    public String getCodeID() {
        return codeID;
    }

    public void setCodeID(String codeID) {
        this.codeID = codeID;
    }

    public String getCodeTypes() {
        return codeTypes;
    }

    public void setCodeTypes(String codeTypes) {
        this.codeTypes = codeTypes;
    }

    public String getBrochureCode() {
        return brochureCode;
    }

    public void setBrochureCode(String brochureCode) {
        this.brochureCode = brochureCode;
    }

    public String getBranchNumber() {
        return branchNumber;
    }

    public void setBranchNumber(String branchNumber) {
        this.branchNumber = branchNumber;
    }

    public String getBrochureName() {
        return brochureName;
    }

    public void setBrochureName(String brochureBranch) {
        this.brochureName = brochureBranch;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ArrayList<Information> getInfos() {
        return infos;
    }

    public void setInfos(ArrayList<Information> infos) {
        this.infos = infos;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isChoosen() {
        return isChoosen;
    }

    public void setChoosen(boolean choosen) {
        isChoosen = choosen;
    }

    public String getBrochureNameJP() {
        return brochureNameJP;
    }

    public void setBrochureNameJP(String brochureNameJP) {
        this.brochureNameJP = brochureNameJP;
    }

    public int getFav() {
        return fav;
    }

    public void setFav(int fav) {
        this.fav = fav;
    }

    public String getProjectID() {
        return projectID;
    }

    public void setProjectID(String projectID) {
        this.projectID = projectID;
    }
}
