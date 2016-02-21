package gojas.ru.bashclient;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by gojas on 13.07.2015.
 */
public class QuotesDatabase {
    DatabaseHelper helper;
    SQLiteDatabase database;

    private static QuotesDatabase localDatabaseInstance;

    private QuotesDatabase(Context context){
        helper=new DatabaseHelper(context);
        database=helper.getWritableDatabase();
    }

    public static QuotesDatabase getInstance(Context context) {
        if (localDatabaseInstance == null){
            localDatabaseInstance = new QuotesDatabase(context);
        }
        return localDatabaseInstance;
    }

    public void addVote(String quoteId,int vote){
        String query="insert into votes ( quote_id, vote ) values (?,?)";
        database.execSQL(query,new String[]{quoteId,String.valueOf(vote)});
    }

    public void deleteVoteByQuoteId(int quoteId){
        String query="delete from votes WHERE quote_id = ?";
        database.execSQL(query,new String[]{String.valueOf(quoteId)});
    }

    public Cursor getVoteByQuoteId(int quoteId){
        String[] args=new String[]{String.valueOf(quoteId)};
        String query="SELECT _id, quote_id, vote FROM votes WHERE quote_id= ?";
        Cursor cursor=database.rawQuery(query,args);
        if(cursor!=null){
            cursor.moveToFirst();
        }
        return cursor;
    }

    public Cursor getVotes(){
        String query="SELECT _id, quote_id, vote FROM votes";
        Cursor cursor=database.rawQuery(query,null);
        if(cursor!=null){
            cursor.moveToFirst();
        }
        return cursor;
    }

    public void clearVotes(){
        database.execSQL("delete from votes");
    }

    public void addCache(int type,String json){
        String query="insert into cache (type,cache) values (?,?)";
        database.execSQL(query,new String[]{String.valueOf(type),json});
    }

    public void deleteCacheByType(int type){
        String query="DELETE from cache WHERE type= ?";
        String[] args=new  String[]{String.valueOf(type)};
        database.execSQL(query,args);
    }

    public Cursor getCacheByType(int type){
        String query="SELECT _id, type, cache FROM cache WHERE type= ?";
        String[] args=new String[]{String.valueOf(type)};
        Cursor cursor=database.rawQuery(query, args);
        if(cursor!=null){
            cursor.moveToFirst();
        }
        return cursor;
    }

    public void addBookMark(Quote quote){
        String query="INSERT into bookmarks (quote_id,quote_text) values (?,?)";
        database.execSQL(query,new String[]{String.valueOf(quote.getId()),quote.toJSON()});
    }

    public Cursor getBookMarks(){
        String query="SELECT _id,quote_id,quote_text FROM bookmarks";
        Cursor cursor=database.rawQuery(query,null);
        if(cursor!=null){
            cursor.moveToFirst();
        }
        return cursor;
    }

    public Cursor getBookMark(int quote_id){
        String query="SELECT _id,quote_id,quote_text FROM bookmarks WHERE quote_id = ?";
        String[] args=new String[]{String.valueOf(quote_id)};
        Cursor cursor=database.rawQuery(query,args);
        if(cursor!=null){
            cursor.moveToFirst();
        }
        return cursor;
    }

    public void deleteBookMark(int quote_id){
        String query="DELETE from bookmarks WHERE quote_id = ?";
        String[] args=new String[]{String.valueOf(quote_id)};
        database.execSQL(query,args);
    }

    public void updateVote(int quote_id,int vote){
        Cursor cursor=getVoteByQuoteId(quote_id);
        String query="replace into votes (_id,quote_id,vote) values (?,?,?)";
        database.execSQL(query,new String[]{String.valueOf(cursor.getInt(cursor.getColumnIndex("_id"))),String.valueOf(quote_id),String.valueOf(vote)});
    }
    public class DatabaseHelper extends SQLiteOpenHelper {
        public static final String dbName="quotes.db";
        public static final String CREATE_VOTES_TABLE="CREATE TABLE votes (_id INTEGER primary key AUTOINCREMENT,"+" quote_id text,"+" vote integer"+");";
        public static final String CREATE_CACHE_TABLE="CREATE TABLE cache (_id INTEGER primary key AUTOINCREMENT,"+" type integer,"+" cache text"+");";
        public static final String CREATE_BOOKMARK_TABLE="CREATE TABLE bookmarks (_id INTEGER primary key AUTOINCREMENT,"+"quote_id integer,"+"quote_text text"+");";
        public DatabaseHelper(Context context) {
            super(context, dbName, null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_VOTES_TABLE);
            db.execSQL(CREATE_CACHE_TABLE);
            db.execSQL(CREATE_BOOKMARK_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
}
