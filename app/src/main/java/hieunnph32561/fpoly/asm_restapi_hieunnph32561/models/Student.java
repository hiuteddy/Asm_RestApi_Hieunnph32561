package hieunnph32561.fpoly.asm_restapi_hieunnph32561.models;

public class Student {
    private String _id;
    private String name;
    private int tuoi;
    private String mssv;
    private boolean trangthaihoc;

    public Student(String name, int tuoi, String mssv, boolean trangthaihoc) {
        this.name = name;
        this.tuoi = tuoi;
        this.mssv = mssv;
        this.trangthaihoc = trangthaihoc;
    }

    public Student(String _id, String name, int tuoi, String mssv, boolean trangthaihoc) {
        this._id = _id;
        this.name = name;
        this.tuoi = tuoi;
        this.mssv = mssv;
        this.trangthaihoc = trangthaihoc;
    }



    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTuoi() {
        return tuoi;
    }

    public void setTuoi(int tuoi) {
        this.tuoi = tuoi;
    }

    public String getMssv() {
        return mssv;
    }

    public void setMssv(String mssv) {
        this.mssv = mssv;
    }

    public boolean isTrangthaihoc() {
        return trangthaihoc;
    }

    public void setTrangthaihoc(boolean trangthaihoc) {
        this.trangthaihoc = trangthaihoc;
    }

// Constructor, getters, and setters


}
