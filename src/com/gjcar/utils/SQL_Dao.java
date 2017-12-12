package com.gjcar.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.gjcar.data.bean.CityShow;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class SQL_Dao {
	public final static int Type_String = 1;
	
	public final static int Type_Short = 2;
	public final static int Type_Int = 3;
	public final static int Type_Long = 4;

	public final static int Type_Float = 5;
	public final static int Type_Double = 6;
	
	@SuppressLint("NewApi")
	public void insert(SQLiteDatabase db, Object object){
		
		Class<?> classType = object.getClass();

		Field[] fs = classType.getDeclaredFields();
		
		String Tab_Name = StringHelper.getClassName(classType.getName());
		
		System.out.println("���뿪ʼ");
		ContentValues cv = new ContentValues();

		for (int i = 0; i < fs.length; i++) {
			
			fs[i].setAccessible(true);//�������ʣ�ȡ��age��˽��Ȩ�ޡ��ö�����Է���
			System.out.println("�ֶ�--"+fs[i].getName());
			System.out.println("�ֶ�����--"+fs[i].getType().getName());
			if(fs[i].getName().equals("id")){
				continue;
			}
			try {
				if(fs[i].getType().getName().equals("java.lang.Integer")){
					
					cv.put(fs[i].getName(), (Integer)fs[i].get(object));
					continue;
				}
				
				if(fs[i].getType().getName().equals("java.lang.String")){
					
					cv.put(fs[i].getName(), (String)fs[i].get(object));
					System.out.println("ֵ"+cv.getAsString(fs[i].getName()));
					continue;
				}
				
				if(fs[i].getType().getName().equals("java.lang.Double")){
					
					cv.put(fs[i].getName(), (Double)fs[i].get(object));
					continue;
				}
				
				if(fs[i].getType().getName().equals("java.lang.Float")){
					
					cv.put(fs[i].getName(), (Float)fs[i].get(object));
					continue;
				}
				
				if(fs[i].getType().getName().equals("java.lang.Short")){
					
					cv.put(fs[i].getName(), (Short)fs[i].get(object));
					continue;
				}
				
				if(fs[i].getType().getName().equals("java.lang.Long")){
	
					cv.put(fs[i].getName(), (Long)fs[i].get(object));
					continue;
				}
			
				cv.put(fs[i].getName(), "");//������������6������ʱ����Ϊ�ַ�""
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		System.out.println("������");
		System.out.println("id"+cv.getAsInteger("id"));
		System.out.println("cityNum"+cv.getAsString("cityNum"));
		System.out.println("cityName"+cv.getAsString("cityName"));
		
//		show.id = 1;	/*����id*/
//		show.cityNum = "002";	/*���б��*/
//		show.cityName = "�Ͼ�";	/*��������*/	
//		show.parentNum = "001";	/*����ʡ�ݱ��*/
//		show.latitude = 180.00;	/*γ��*/
//		show.longitude = 190.00;	/*����*/
//		
//		show.belong = "N";
//		show.isHot = 1;
//		show.label = "label";
//		show.pinyin = "nanjing";
//		show.storeShows = null;
//		cv.getAsString("cityName");
		db.insertWithOnConflict(Tab_Name, null, cv, SQLiteDatabase.CONFLICT_REPLACE);
//		db.in
		System.out.println("�������");
	}
	
	/** ��ȡ������¼������ */
	public int count(SQLiteDatabase db, Class<?> classType){
		//��ѯ���
		String sql = " SELECT count(*) FROM " + StringHelper.getClassName(classType.getName()); 
		//��ȡ���
		Cursor result = db.rawQuery(sql, null);
		
		result.moveToFirst();
		return result.getInt(0);

	}

	public <T> List<T> findAll(SQLiteDatabase db, Class<T> classType){
		
		String sql = "select * FROM  " + StringHelper.getClassName(classType.getName())+" order by id desc " ;//

		//��ѯ
		System.out.println("�ɹ���ȡ1");
		Cursor result = db.rawQuery(sql, null);

		List<T> list = new ArrayList<T>();
		
		for(result.moveToFirst(); !result.isAfterLast(); result.moveToNext()){
			
			T t = null;
			try {
				t = (T) Select(classType.newInstance(), result);
			} catch (Exception e) {}
			
			list.add(t);
			System.out.println("sqlId��"+((CityShow)t).cityId);
			System.out.println("sqlnumber��"+((CityShow)t).id);
			System.out.println("sql���У�"+((CityShow)t).cityName);
			System.out.println("sql���У�"+((CityShow)t).cityName);
			System.out.println("sql���ȣ�"+((CityShow)t).longitude);
		}
		
		return list;
	}
	
	/**
	 * �ֶη��䣬�ǰ���ĸ�����
	 */
	private Object Select(Object object, Cursor result){
		
		Class<?> classType = object.getClass();
		
		Field[] fs = classType.getDeclaredFields();
		System.out.println("ȫ������-----------------------------------");
		
		for (int i = 0; i < fs.length; i++) {
			System.out.println("������"+i+fs[i].getName());
			System.out.println("��������"+fs[i].getType().getName());
			
			if(fs[i].getType().getName().equals("java.lang.Integer")){
				fs[i].setAccessible(true);//�������ʣ�ȡ��age��˽��Ȩ�ޡ��ö�����Է���
				try {
					fs[i].set(object, result.getInt(i));
					System.out.println("����ֵ"+result.getInt(i));
				} catch (Exception e) {
					e.printStackTrace();
				} 
			}
			
			if(fs[i].getType().getName().equals("java.lang.String")){
				fs[i].setAccessible(true);//�������ʣ�ȡ��age��˽��Ȩ�ޡ��ö�����Է���
				try {
					fs[i].set(object, result.getString(i));
					System.out.println("����ֵ"+result.getString(i));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			if(fs[i].getType().getName().equals("java.lang.Double")){
				fs[i].setAccessible(true);//�������ʣ�ȡ��age��˽��Ȩ�ޡ��ö�����Է���
				try {
					fs[i].set(object, result.getDouble(i));
					System.out.println("����ֵ"+result.getDouble(i));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			if(fs[i].getType().getName().equals("java.lang.Float")){
				fs[i].setAccessible(true);//�������ʣ�ȡ��age��˽��Ȩ�ޡ��ö�����Է���
				try {
					fs[i].set(object, result.getFloat(i));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			if(fs[i].getType().getName().equals("java.lang.Short")){
				fs[i].setAccessible(true);//�������ʣ�ȡ��age��˽��Ȩ�ޡ��ö�����Է���
				try {
					fs[i].set(object, result.getShort(i));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			if(fs[i].getType().getName().equals("java.lang.Long")){
				fs[i].setAccessible(true);//�������ʣ�ȡ��age��˽��Ȩ�ޡ��ö�����Է���
				try {
					fs[i].set(object, result.getLong(i));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return object;
	}
}
