package com.gjcar.utils;

import com.gjcar.data.bean.CityShow;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
/** 
 * ���ܣ�
 * 1.new DBHelper(Context context, String name, CursorFactory factory,int version)��
 *   a.����һ��DBHelper���󣬲��Ҵ���һ������Ϊname���汾��Ϊversion�����ݿ�
 *   b.��һ�Σ�����SQLiteOpenHelper�������onCreate()������������onCreate()�п���д������;
 *   	֮�󴴽�QLiteOpenHelper�����ٵ���onCreate
 * 	 c.���汾�����������db�Ѿ����ڣ�����һ���汾�ţ����ڶ��δ���ĺ͵�һ�β�ͬʱ���ͻ����onUpgrade
 *     ���Ը��±�Ҳ������ɾ����Ȼ���ٴ�����
 *     ��ʱ��������Ե���this.onCreate()��ִ��SQL���
 * */

/**
 * onCreate:��һ�δ���SQLiteOpenHelper�������ã�֮���ٵ���
 * onUpgrade:�����ʵ���
 * */
public class SQL_OpenHelper extends SQLiteOpenHelper{

	public static final int VERSION = 1;
	
	public SQL_OpenHelper(Context context, String name, CursorFactory factory, int version) {
		
		/*����һ�����ݿ�*/
		super(context, "gjcar.db", null, VERSION);
		
	}
	
	/**��һ�ν����������onCreate;֮�󲻻��ٵ���onCreate֮��������ж�version,����onUpgrade*/
	/*������:��ʼ��*/
	@Override
	public void onCreate(SQLiteDatabase db) {
		
		/*�������б�*/
		new SQL_TableHelper().createTable(db, CityShow.class);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
		this.onCreate(db);
	}

	
}
