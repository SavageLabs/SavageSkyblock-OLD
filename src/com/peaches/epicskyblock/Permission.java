package com.peaches.epicskyblock;

public class Permission {
    private Boolean BUILD;
    private Boolean BREAK;
    private Boolean BUTTON;
    private Boolean LEVER;
    private Boolean CONTAINER;
    private Boolean INVITE;
    private Boolean KICK;
    private Boolean SETHOME;
    private Boolean SETWARP;
    private Boolean PROMOTE;
    private Boolean WARP;
    private Boolean BAN;
    private Boolean FlY;
    private Boolean DISBAND;
    private Boolean Permissions;

    public Permission(Boolean BUILD, Boolean BREAK, Boolean BUTTON, Boolean LEVER, Boolean CONTAINER, Boolean INVITE, Boolean KICK, Boolean SETHOME, Boolean SETWARP, Boolean PROMOTE, Boolean WARP, Boolean BAN, Boolean FLY, Boolean DISBAND, Boolean PERMISSIONS) {
        this.BUILD = BUILD;
        this.BREAK = BREAK;
        this.BUTTON = BUTTON;
        this.LEVER = LEVER;
        this.CONTAINER = CONTAINER;
        this.INVITE = INVITE;
        this.KICK = KICK;
        this.SETHOME = SETHOME;
        this.SETWARP = SETWARP;
        this.PROMOTE = PROMOTE;
        this.WARP = WARP;
        this.BAN = BAN;
        this.FlY = FLY;
        this.DISBAND = DISBAND;
        this.Permissions = PERMISSIONS;
    }

    public Boolean getPermissions() {
        return Permissions;
    }

    public void setPermissions(Boolean permissions) {
        Permissions = permissions;
    }

    public Boolean getBUILD() {
        return BUILD;
    }

    public void setBUILD(Boolean BUILD) {
        this.BUILD = BUILD;
    }

    public Boolean getBREAK() {
        return BREAK;
    }

    public void setBREAK(Boolean BREAK) {
        this.BREAK = BREAK;
    }

    public Boolean getBUTTON() {
        return BUTTON;
    }

    public void setBUTTON(Boolean BUTTON) {
        this.BUTTON = BUTTON;
    }

    public Boolean getLEVER() {
        return LEVER;
    }

    public void setLEVER(Boolean LEVER) {
        this.LEVER = LEVER;
    }

    public Boolean getCONTAINER() {
        return CONTAINER;
    }

    public void setCONTAINER(Boolean CONTAINER) {
        this.CONTAINER = CONTAINER;
    }

    public Boolean getINVITE() {
        return INVITE;
    }

    public void setINVITE(Boolean INVITE) {
        this.INVITE = INVITE;
    }

    public Boolean getKICK() {
        return KICK;
    }

    public void setKICK(Boolean KICK) {
        this.KICK = KICK;
    }

    public Boolean getSETHOME() {
        return SETHOME;
    }

    public void setSETHOME(Boolean SETHOME) {
        this.SETHOME = SETHOME;
    }

    public Boolean getSETWARP() {
        return SETWARP;
    }

    public void setSETWARP(Boolean SETWARP) {
        this.SETWARP = SETWARP;
    }

    public Boolean getPROMOTE() {
        return PROMOTE;
    }

    public void setPROMOTE(Boolean PROMOTE) {
        this.PROMOTE = PROMOTE;
    }

    public Boolean getWARP() {
        return WARP;
    }

    public void setWARP(Boolean WARP) {
        this.WARP = WARP;
    }

    public Boolean getBAN() {
        return BAN;
    }

    public void setBAN(Boolean BAN) {
        this.BAN = BAN;
    }

    public Boolean getFlY() {
        return FlY;
    }

    public void setFlY(Boolean flY) {
        FlY = flY;
    }

    public Boolean getDISBAND() {
        return DISBAND;
    }

    public void setDISBAND(Boolean DISBAND) {
        this.DISBAND = DISBAND;
    }
}
