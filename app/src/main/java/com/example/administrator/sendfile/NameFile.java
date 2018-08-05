package com.example.administrator.sendfile;

/**
 * Created by Administrator on 2016/12/7.
 */

public class NameFile {
    int Type;
    String name;
    String fileBig;
    String fileTime;
    String fileUri;
    boolean fileChoose;
    int process;

    public int getProcess() {
        return process;
    }

    public void setProcess(int process) {
        this.process = process;
    }

    public boolean isFileChoose() {
        return fileChoose;
    }

    public void setFileChoose(boolean fileChoose) {
        this.fileChoose = fileChoose;
    }

    public String getFileUri() {
        return fileUri;
    }

    public void setFileUri(String fileUri) {
        this.fileUri = fileUri;
    }

    public int getType() {
        return Type;
    }

    public void setType(int type) {
        Type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFileBig() {
        return fileBig;
    }

    public void setFileBig(String fileBig) {
        this.fileBig = fileBig;
    }

    public String getFileTime() {
        return fileTime;
    }

    public void setFileTime(String fileTime) {
        this.fileTime = fileTime;
    }
}
