package com.blogspot.programer27android.playsound;

public class PlayInfo {
    String qori,surat,urlInfo;
    public PlayInfo(){
    }

    public PlayInfo(String qori, String surat, String urlInfo) {
        this.qori = qori;
        this.surat = surat;
        this.urlInfo = urlInfo;
    }

    public String getQori() {
        return qori;
    }

    public String getSurat() {
        return surat;
    }

    public String getUrlInfo() {
        return urlInfo;
    }
}
