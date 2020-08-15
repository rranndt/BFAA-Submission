package com.belajar.bfaa_submission1.Db;

import android.net.Uri;
import android.provider.BaseColumns;

public class DbContract {

    public static final String AUTHORITY = "com.belajar.bfaa_submission1";
    public static final String SCHEME = "content";

    public static final String TABLE_NAME = "likes";
    public static final class LikesColumns implements BaseColumns {
        public static final String ID = "id";
        public static final String AVATAR = "avatar";
        public static final String USERNAME = "username";
        public static final String URL = "url";

        public static final Uri CONTENT_URI = new Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_NAME)
                .build();
    }
}
