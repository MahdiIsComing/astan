package ir.upcoders.sheydaee.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import ir.upcoders.sheydaee.model.Martyr;

/**
 * Created by mahdi on 12/13/16.
 */
public class DbHandler extends SQLiteOpenHelper {
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "martyrs";

    // Martyrs table name
    private static final String TABLE_MARTYRS = "martyr";

    // Contacts Table Columns names
    private static final String MARTYR_ID = "id";
    private static final String MARTYR_FNAME = "fname";
    private static final String MARTYR_LNAME = "lname";
    private static final String MARTYR_AVATAR_ID = "avatar";
    private static final String MARTYR_BIRTH_PLACE = "bplace";
    private static final String MARTYR_MARTYRDOM_PLACE = "mplace";
    private static final String MARTYR_BIRTH_DATE = "bdate";
    private static final String MARTYR_MARTYRDOM_DATE = "mdate";
    private static final String MARTYR_BIO_ID = "bio";
    private static final String MARTYR_MEMO_ID = "memo";
    private static final String MARTYR_WILL_ID = "will";

    public DbHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_MARTYRS_TABLE = "CREATE TABLE " + TABLE_MARTYRS + "("
                + MARTYR_ID + " INTEGER PRIMARY KEY," + MARTYR_BIO_ID + " TEXT,"
                + MARTYR_MEMO_ID + " TEXT," + MARTYR_WILL_ID + " TEXT," + MARTYR_FNAME + " TEXT," +
                MARTYR_LNAME + " TEXT," + MARTYR_AVATAR_ID + " TEXT," + MARTYR_BIRTH_PLACE + " TEXT," +
                MARTYR_MARTYRDOM_PLACE + " TEXT," + MARTYR_BIRTH_DATE + " TEXT,"
                + MARTYR_MARTYRDOM_DATE + " TEXT" + ")";
        db.execSQL(CREATE_MARTYRS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MARTYRS);

        // Create tables again
        onCreate(db);
    }

    // Adding new martyr
    public void addMartyr(Martyr martyr) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(MARTYR_ID, martyr.getId()); // Martyr ID
        values.put(MARTYR_FNAME, martyr.getFirst_name());
        values.put(MARTYR_LNAME, martyr.getLast_name());
        values.put(MARTYR_BIRTH_DATE, martyr.getBirth_date());
        values.put(MARTYR_BIRTH_PLACE, martyr.getBirth_place());
        values.put(MARTYR_MARTYRDOM_DATE, martyr.getMartyrdom_date());
        values.put(MARTYR_MARTYRDOM_PLACE, martyr.getMartyrdom_place());
        if (martyr.getMartyr_bio_id() != null && martyr.getMartyr_memo_id() != null &&
                martyr.getMartyr_will_id() != null) {
            values.put(MARTYR_BIO_ID, martyr.getMartyr_bio_id()); // Martyr BIO ID
            values.put(MARTYR_MEMO_ID, martyr.getMartyr_memo_id()); // Martyr MEMO ID
            values.put(MARTYR_WILL_ID, martyr.getMartyr_will_id()); // Martyr WILL ID
        }

        // Inserting Row
        db.insert(TABLE_MARTYRS, null, values);
        db.close(); // Closing database connection

    }

    // Getting single martyr
    public String[] getMartyr(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_MARTYRS,
                new String[]{MARTYR_ID, MARTYR_BIO_ID, MARTYR_MEMO_ID, MARTYR_WILL_ID, MARTYR_FNAME,
                        MARTYR_LNAME, MARTYR_AVATAR_ID, MARTYR_BIRTH_PLACE, MARTYR_MARTYRDOM_PLACE,
                        MARTYR_BIRTH_DATE, MARTYR_MARTYRDOM_DATE},
                MARTYR_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            String[] martyrContent = {cursor.getString(0), cursor.getString(1),
                    cursor.getString(2), cursor.getString(3), cursor.getString(4),
                    cursor.getString(5), cursor.getString(6), cursor.getString(7),
                    cursor.getString(8), cursor.getString(9), cursor.getString(10)
            };
            // return contents id
            return martyrContent;
        } else {
            return null;
        }
    }

    // Getting All Contacts
    public List<Martyr> getAllMartyrs() {
        List<Martyr> martyrList = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + TABLE_MARTYRS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // loop through all rows and add to list
        if (cursor.moveToFirst()){
            do{
                Martyr martyr = new Martyr();
                martyr.setId(Integer.parseInt(cursor.getString(0)));
                try {
                    martyr.setMartyr_bio_id(Integer.parseInt(cursor.getString(1)));
                    martyr.setMartyr_memo_id(Integer.parseInt(cursor.getString(2)));
                    martyr.setMartyr_will_id(Integer.parseInt(cursor.getString(3)));
                }catch (Exception e){
                    e.printStackTrace();
                }

                martyr.setFirst_name(cursor.getString(4));
                martyr.setLast_name(cursor.getString(5));

                martyr.setBirth_place(cursor.getString(7));
                martyr.setMartyrdom_place(cursor.getString(8));
                martyr.setBirth_date(cursor.getString(9));
                martyr.setMartyrdom_date(cursor.getString(10));

                martyrList.add(martyr);

            }while (cursor.moveToNext());
        }
        return martyrList;
    }

    // Getting contacts Count
    public int getMartyrsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_MARTYRS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        // return count
        return cursor.getCount();
    }

//    // Updating single contact
    public int updateMartyr(Martyr martyr) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(MARTYR_BIO_ID, martyr.getMartyr_bio_id());
        values.put(MARTYR_MEMO_ID, martyr.getMartyr_memo_id());
        values.put(MARTYR_WILL_ID, martyr.getMartyr_will_id());

        // updating row
        return db.update(TABLE_MARTYRS, values, MARTYR_ID + " = ?",
                new String[] { String.valueOf(martyr.getId()) });
    }

//    // Deleting single contact
//    public void deleteMartyr(Martyr contact) {
//    }
}
