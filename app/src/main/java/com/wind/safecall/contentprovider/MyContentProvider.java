package com.wind.safecall.contentprovider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by zhangcong on 2017/4/8.
 */

public class MyContentProvider extends ContentProvider {
    private MyOpenHelper myOpenHelper;
    private String DB_name="blackname";//数据库名
    private String Table_name="blacknametable";//数据表名
    private SQLiteDatabase sqLiteDatabase;
    private static UriMatcher uriMatcher;
    public static  final String AUTHORITY="blacknum";
    public static final  Uri uri =Uri.parse("content://blacknum/path_simon");

    // 注册该内容提供者匹配的uri
    static {
        uriMatcher=new UriMatcher(UriMatcher.NO_MATCH);//Creates the root node of the URI tree.
        uriMatcher.addURI(AUTHORITY,"path_simon",1);// 代表当前表中的所有的记录,第三个参数必须为正数
        uriMatcher.addURI(AUTHORITY,"path_simon/1",2);// 代表当前表中的某条特定的记录，记录id便是#处得数字
    }
    //数据表列名映射
    private static  final  String blacknum="blacknum";
    private static final String _id = "id";

    @Override
    public boolean onCreate() {
        try {
            myOpenHelper=new MyOpenHelper(getContext(),DB_name,null,1);
        }
        catch (Exception e)
        {
            return  false;
        }
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor cursor = null;
        sqLiteDatabase = myOpenHelper.getReadableDatabase();
        int code = uriMatcher.match(uri);//addURI()传的第三个参数
        switch (code) {
            case 1:
                cursor = sqLiteDatabase.query(Table_name, projection, selection,
                        selectionArgs, null, null, sortOrder);
                break;
            case 2:
                // 从uri中解析出ID
                long id = ContentUris.parseId(uri);
                selection = (selection == null || "".equals(selection.trim())) ? _id
                        + "=" + id
                        : selection + " and " + _id + "=" + id;
                cursor = sqLiteDatabase.query(Table_name, projection, selection,
                        selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("参数错误");
        }

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        sqLiteDatabase=myOpenHelper.getWritableDatabase();
        sqLiteDatabase = myOpenHelper.getWritableDatabase();
        int code = uriMatcher.match(uri);//前面addURI传的第三个参数
        switch (code) {
            case 1:
                sqLiteDatabase.insert(Table_name, blacknum, values);
                break;
            case 2:
                long id = sqLiteDatabase.insert(Table_name, blacknum, values);
                // withAppendId将id添加到uri的最后
                ContentUris.withAppendedId(uri, id);
                break;
            default:
                throw new IllegalArgumentException("异常参数");
        }

        return uri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        sqLiteDatabase=myOpenHelper.getWritableDatabase();
        int code=uriMatcher.match(uri);
        int num=0;
        switch (code){
            case 1:
                num=sqLiteDatabase.delete(Table_name,selection,selectionArgs);
                break;
            case 2:
                long _id = ContentUris.parseId(uri);
                selection="_id=?";
                selectionArgs=new String[]{String.valueOf(_id)};
                num=sqLiteDatabase.delete(Table_name,selection,selectionArgs);
                break;
            default:
                break;
        }
        return num;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    private class MyOpenHelper extends SQLiteOpenHelper
    {

        public MyOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            /**
             * 官方解释
             * Called when the database is created for the first time. This is where the
             * creation of tables and the initial population of the tables should happen.
             *
             * @param db The database.
             */
            String sql = " create table if not exists " + Table_name
                    + "(blacknum  varchar(20),id INTEGER)";
            db.execSQL(sql);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
}
