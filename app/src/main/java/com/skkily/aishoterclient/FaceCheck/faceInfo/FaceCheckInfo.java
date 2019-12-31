package com.skkily.aishoterclient.FaceCheck.faceInfo;

import java.util.List;

public class FaceCheckInfo {

    /**
     * time_used : 481
     * faces : [{"face_rectangle":{"width":779,"top":217,"left":183,"height":779},"face_token":"481d71ccc53a872ecc412913f08545dd"}]
     * image_id : Eu7LwPot7RDBmfHMzmffmA==
     * request_id : 1577683047,0120e24e-b70a-4477-ac9a-1f2f134de6a2
     * face_num : 1
     */

    private int time_used;
    private String image_id;
    private String request_id;
    private int face_num;
    private List<FacesBean> faces;

    public int getTime_used() {
        return time_used;
    }

    public void setTime_used(int time_used) {
        this.time_used = time_used;
    }

    public String getImage_id() {
        return image_id;
    }

    public void setImage_id(String image_id) {
        this.image_id = image_id;
    }

    public String getRequest_id() {
        return request_id;
    }

    public void setRequest_id(String request_id) {
        this.request_id = request_id;
    }

    public int getFace_num() {
        return face_num;
    }

    public void setFace_num(int face_num) {
        this.face_num = face_num;
    }

    public List<FacesBean> getFaces() {
        return faces;
    }

    public void setFaces(List<FacesBean> faces) {
        this.faces = faces;
    }

    public static class FacesBean {
        /**
         * face_rectangle : {"width":779,"top":217,"left":183,"height":779}
         * face_token : 481d71ccc53a872ecc412913f08545dd
         */

        private FaceRectangleBean face_rectangle;
        private String face_token;

        public FaceRectangleBean getFace_rectangle() {
            return face_rectangle;
        }

        public void setFace_rectangle(FaceRectangleBean face_rectangle) {
            this.face_rectangle = face_rectangle;
        }

        public String getFace_token() {
            return face_token;
        }

        public void setFace_token(String face_token) {
            this.face_token = face_token;
        }

        public static class FaceRectangleBean {
            /**
             * width : 779
             * top : 217
             * left : 183
             * height : 779
             */

            private int width;
            private int top;
            private int left;
            private int height;

            public int getWidth() {
                return width;
            }

            public void setWidth(int width) {
                this.width = width;
            }

            public int getTop() {
                return top;
            }

            public void setTop(int top) {
                this.top = top;
            }

            public int getLeft() {
                return left;
            }

            public void setLeft(int left) {
                this.left = left;
            }

            public int getHeight() {
                return height;
            }

            public void setHeight(int height) {
                this.height = height;
            }
        }
    }
}
