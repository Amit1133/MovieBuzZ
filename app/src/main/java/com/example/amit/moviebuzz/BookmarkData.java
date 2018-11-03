package com.example.amit.moviebuzz;

public class BookmarkData {
        private int mId;
        private String mImdb;
        private String mTitle;

        public BookmarkData(int mId, String mImdb, String mTitle) {
            this.mId = mId;
            this.mImdb = mImdb;
            this.mTitle = mTitle;
        }

        public BookmarkData() {
            //empty
        }

        public int getmId() {
            return mId;
        }

        public void setmId(int mId) {
            this.mId = mId;
        }

        public String getmImdb() {
            return mImdb;
        }

        public void setmImdb(String mImdb) {
            this.mImdb = mImdb;
        }

        public String getmTitle() {
            return mTitle;
        }

        public void setmTitle(String mTitle) {
            this.mTitle = mTitle;
        }

}

