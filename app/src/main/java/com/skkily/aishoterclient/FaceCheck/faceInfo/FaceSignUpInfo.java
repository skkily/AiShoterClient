package com.skkily.aishoterclient.FaceCheck.faceInfo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FaceSignUpInfo {
    /**
     * request_id : 1577712329,d36f5836-f902-4ca7-876d-a66b24893bb6
     * time_used : 347
     * thresholds : {"1e-3":62.327,"1e-5":73.975,"1e-4":69.101}
     * results : [{"confidence":95.282,"user_id":"","face_token":"90c94770e53e4a391748f8d91e5fa4c5"}]
     */

    private String request_id;
    private int time_used;
    private ThresholdsBean thresholds;
    private List<ResultsBean> results;

    public String getRequest_id() {
        return request_id;
    }

    public void setRequest_id(String request_id) {
        this.request_id = request_id;
    }

    public int getTime_used() {
        return time_used;
    }

    public void setTime_used(int time_used) {
        this.time_used = time_used;
    }

    public ThresholdsBean getThresholds() {
        return thresholds;
    }

    public void setThresholds(ThresholdsBean thresholds) {
        this.thresholds = thresholds;
    }

    public List<ResultsBean> getResults() {
        return results;
    }

    public void setResults(List<ResultsBean> results) {
        this.results = results;
    }

    public static class ThresholdsBean {
        /**
         * 1e-3 : 62.327
         * 1e-5 : 73.975
         * 1e-4 : 69.101
         */

        @SerializedName("1e-3")
        private double _$1e3;
        @SerializedName("1e-5")
        private double _$1e5;
        @SerializedName("1e-4")
        private double _$1e4;

        public double get_$1e3() {
            return _$1e3;
        }

        public void set_$1e3(double _$1e3) {
            this._$1e3 = _$1e3;
        }

        public double get_$1e5() {
            return _$1e5;
        }

        public void set_$1e5(double _$1e5) {
            this._$1e5 = _$1e5;
        }

        public double get_$1e4() {
            return _$1e4;
        }

        public void set_$1e4(double _$1e4) {
            this._$1e4 = _$1e4;
        }
    }

    public static class ResultsBean {
        /**
         * confidence : 95.282
         * user_id :
         * face_token : 90c94770e53e4a391748f8d91e5fa4c5
         */

        private double confidence;
        private String user_id;
        private String face_token;

        public double getConfidence() {
            return confidence;
        }

        public void setConfidence(double confidence) {
            this.confidence = confidence;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getFace_token() {
            return face_token;
        }

        public void setFace_token(String face_token) {
            this.face_token = face_token;
        }
    }
}
