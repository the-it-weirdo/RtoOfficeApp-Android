package com.example.debaleen.project2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHelper extends SQLiteOpenHelper {
    public static final String utcol1 = "Id";
    public static final String utcol2 = "Name";
    public static final String utcol3 = "Email";
    public static final String utcol4 = "Phone";
    public static final String utcol5 = "Gender";
    public static final String utcol6 = "Password";
    public static final String utcol7 = "Type";
    public static final String vtcol1 = "Id";
    public static final String vtcol2 = "CarNumber";
    public static final String vtcol3 = "Owner";
    public static final String vtcol4 = "Type";
    public static final String vtcol5 = "RegDate";
    public static final String vtcol6 = "Insurance";
    public static final String vtcol7 = "PollutionStatus";
    public static final String table1Name = "users";
    public static final String table2Name = "vehicles";
    public static final String dbName = "PROJECT2.db";

    public DataBaseHelper(Context context)//constructor
    {
        super(context, dbName, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + table1Name + "(" + utcol1 + " INTEGER PRIMARY KEY AUTOINCREMENT," + utcol2 + " TEXT," + utcol3 + " TEXT," + utcol4 + " TEXT," + utcol5 + " TEXT," + utcol6 + " TEXT,"+ utcol7 + " TEXT)");
        db.execSQL("CREATE TABLE " + table2Name + "(" + vtcol1 + " INTEGER PRIMARY KEY AUTOINCREMENT," + vtcol2 + " TEXT," + vtcol3 + " TEXT," + vtcol4 + " TEXT," + vtcol5 + " TEXT," + vtcol6 + " TEXT," + vtcol7 + " TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + table1Name);
        db.execSQL("DROP TABLE IF EXISTS " + table2Name);
    }

    public boolean insertIntoT1(String name, String email, String phone, String gender, String password, String type) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(utcol2, name);
        cv.put(utcol3, email);
        cv.put(utcol4, phone);
        cv.put(utcol5, gender);
        cv.put(utcol6, password);
        cv.put(utcol7, type);

        long res = db.insert(table1Name, null, cv);
        if (res > 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean insertIntoT2(String carNumber, String owner, String type, String regdate, String insurance, String pollutionstatus) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(vtcol2, carNumber);
        cv.put(vtcol3, owner);
        cv.put(vtcol4, type);
        cv.put(vtcol5, regdate);
        cv.put(vtcol6, insurance);
        cv.put(vtcol7, pollutionstatus);

        long res = db.insert(table2Name, null, cv);
        if (res > 0) {
            return true;
        } else {
            return false;
        }
    }

    /*public boolean Insert(String nm,String em,String ph,String cr)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put(col2,nm);
        cv.put(col3,em);
        cv.put(col4,ph);
        cv.put(col5,cr);
        long res= db.insert(tableName,null,cv);
        if(res>0)
        {
            return true;
        }
        else
        {
            return false;
        }
    }*/

    public boolean deleteFromT1(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        long res = db.delete(table1Name, utcol1 + "=?", new String[]{id});

        if (res > 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean deleteFromT2(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        long res = db.delete(table2Name, vtcol1 + "=?", new String[]{id});

        if (res > 0) {
            return true;
        } else {
            return false;
        }
    }
    /*
    public boolean Delete(String id)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        long res= db.delete(tableName,col1+"=?",new String[]{id});
        if(res>0)
        {
            return true;
        }
        else
        {
            return false;
        }
    }*/

    public boolean updateInT1(String id, String name, String email, String phone, String gender, String password, String type)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(utcol2, name);
        cv.put(utcol3, email);
        cv.put(utcol4, phone);
        cv.put(utcol5, gender);
        cv.put(utcol6, password);
        cv.put(utcol7, type);

        long res = db.update(table1Name, cv, utcol1+"=?", new String[] {id});

        if(res>0)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public boolean updateInT2(String id, String carNumber, String owner, String type, String regdate, String insurance, String pollutionstatus)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(vtcol2, carNumber);
        cv.put(vtcol3, owner);
        cv.put(vtcol4, type);
        cv.put(vtcol5, regdate);
        cv.put(vtcol6, insurance);
        cv.put(vtcol7, pollutionstatus);

        long res = db.update(table2Name, cv, vtcol1+"=?", new String[] {id});

        if(res>0)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    /*public boolean Update(String id,String nm,String em,String ph,String cr)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put(col2,nm);
        cv.put(col3,em);
        cv.put(col4,ph);
        cv.put(col5,cr);
        long res= db.update(tableName,cv,col1+"=?",new String[]{id});
        if(res>0)
        {
            return true;
        }
        else
        {
            return false;
        }
    }*/

    public Cursor displayAllInT1()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res=db.rawQuery("select * from "+table1Name,null);
        return res;
    }

    public Cursor displayAllInT2()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res=db.rawQuery("select * from "+table2Name,null);
        return res;
    }
    /*public Cursor DisplayAll()
    {
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor res=db.rawQuery("select * from "+tableName,null);
        return res;
    }*/

    public Cursor displayByIdFromT1(String id)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor res=db.rawQuery("select * from "+table1Name+" where "+utcol1+"='"+id+"'",null);
        return res;
    }

    public Cursor displayByIdFromT2(String id)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor res=db.rawQuery("select * from "+table2Name+" where "+vtcol1+"='"+id+"'",null);
        return res;
    }
    /*public Cursor DisplayById(String id)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor res=db.rawQuery("select * from "+tableName+" where "+col1+"='"+id+"'",null);
        return res;
    }*/

    public Cursor displayByCarNumber(String carNumber)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+table2Name+" where "+vtcol2+"='"+carNumber.trim()+"'", null);
        return res;
    }

    public Cursor displayByCarNumberOrOwnerName(String query)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+table2Name+" where "+vtcol2+"='"+query.trim()+"' or "+vtcol3+"='"+query.trim()+"'", null);
        return res;
    }

    public Cursor displayByEmailPasswordType(String email, String password, String type)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+table1Name+" where "+utcol3+"='"+email.trim()+"' and "+utcol6+"='"+password+"' and "+utcol7+"='"+type+ "'", null);
        return res;
    }

    public Cursor displayByEmail(String email)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+table1Name+" where "+utcol3+"='"+email.trim()+"'", null);
        return res;
    }
    /*public Cursor DisplayByEmail(String em)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor res=db.rawQuery("select * from "+tableName+" where "+col3+"='"+em.trim()+"'",null);
        return res;
    }
    public Cursor DisplayByEmailPassword(String em,String pw)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor res=db.rawQuery("select * from Student where Email='"+em.trim()+"'"+" and Password='"+pw.trim()+"'",null);
        return res;
    }
}*/


}