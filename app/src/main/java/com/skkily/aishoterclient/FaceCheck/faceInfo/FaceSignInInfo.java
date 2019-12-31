package com.skkily.aishoterclient.FaceCheck.faceInfo;

import java.util.List;

public class FaceSignInInfo {
    /**
     * faceset_token : bfd8ede007f0fdf9dbf0f704fc0dd8ef
     * time_used : 473
     * face_count : 4
     * face_added : 1
     * request_id : 1577714790,771e9b32-30d9-44a5-9cf3-f54ac803d66f
     * outer_id : faceHub
     * failure_detail : []
     */

    private String faceset_token;
    private int time_used;
    private int face_count;
    private int face_added;
    private String request_id;
    private String outer_id;
    private List<?> failure_detail;

    public String getFaceset_token() {
        return faceset_token;
    }

    public void setFaceset_token(String faceset_token) {
        this.faceset_token = faceset_token;
    }

    public int getTime_used() {
        return time_used;
    }

    public void setTime_used(int time_used) {
        this.time_used = time_used;
    }

    public int getFace_count() {
        return face_count;
    }

    public void setFace_count(int face_count) {
        this.face_count = face_count;
    }

    public int getFace_added() {
        return face_added;
    }

    public void setFace_added(int face_added) {
        this.face_added = face_added;
    }

    public String getRequest_id() {
        return request_id;
    }

    public void setRequest_id(String request_id) {
        this.request_id = request_id;
    }

    public String getOuter_id() {
        return outer_id;
    }

    public void setOuter_id(String outer_id) {
        this.outer_id = outer_id;
    }

    public List<?> getFailure_detail() {
        return failure_detail;
    }

    public void setFailure_detail(List<?> failure_detail) {
        this.failure_detail = failure_detail;
    }
}
