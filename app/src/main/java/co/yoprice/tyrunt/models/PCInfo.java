package co.yoprice.tyrunt.models;

/**
 * Created by Charl on 3/27/2016.
 */
public class PCInfo {
    public String pc_name, pc_version, pc_user, pc_processor;
    public PCInfo(){}

    public String getPc_processor() {
        return pc_processor;
    }

    public PCInfo setPc_processor(String pc_processor) {
        this.pc_processor = pc_processor;
        return this;
    }

    public static PCInfo Builder(){
        return new PCInfo();
    }

    public String getPc_name() {
        return pc_name;
    }

    public PCInfo setPc_name(String pc_name) {
        this.pc_name = pc_name;
        return this;
    }

    public String getPc_version() {
        return pc_version;
    }

    public PCInfo setPc_version(String pc_version) {
        this.pc_version = pc_version;
        return this;
    }

    public String getPc_user() {
        return pc_user;
    }

    public PCInfo setPc_user(String pc_user) {
        this.pc_user = pc_user;
        return this;
    }
}
