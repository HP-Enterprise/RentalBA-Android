package com.gjcar.utils;

import com.gjcar.data.bean.CityShow;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
/** 
 * 功能：
 * 1.new DBHelper(Context context, String name, CursorFactory factory,int version)：
 *   a.创建一个DBHelper对象，并且创建一个名称为name，版本号为version的数据库
 *   b.第一次，创建SQLiteOpenHelper，会调用onCreate()方法，所以在onCreate()中可以写创建表;
 *   	之后创建QLiteOpenHelper不会再调用onCreate
 * 	 c.当版本号升级：如果db已经存在，就有一个版本号，当第二次传入的和第一次不同时，就会调用onUpgrade
 *     可以更新表：也即是先删除表，然后再创建表
 *     此时创建表可以调用this.onCreate()或执行SQL语句
 * */

/**
 * onCreate:第一次创建SQLiteOpenHelper类对象调用，之后不再调用
 * onUpgrade:升级彩调用
 * */
public class SQL_OpenHelper extends SQLiteOpenHelper{

	public static final int VERSION = 1;
	
	public SQL_OpenHelper(Context context, String name, CursorFactory factory, int version) {
		
		/*创建一个数据库*/
		super(context, "gjcar.db", null, VERSION);
		
	}
	
	/**第一次进来，会调用onCreate;之后不会再调用onCreate之后进来：判断version,调用onUpgrade*/
	/*创建表:初始化*/
	@Override
	public void onCreate(SQLiteDatabase db) {
		
		/*创建城市表*/
		new SQL_TableHelper().createTable(db, CityShow.class);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
		this.onCreate(db);
	}

	
}
