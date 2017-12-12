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
		
		System.out.println("插入开始");
		ContentValues cv = new ContentValues();

		for (int i = 0; i < fs.length; i++) {
			
			fs[i].setAccessible(true);//暴力访问，取消age的私有权限。让对象可以访问
			System.out.println("字段--"+fs[i].getName());
			System.out.println("字段类型--"+fs[i].getType().getName());
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
					System.out.println("值"+cv.getAsString(fs[i].getName()));
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
			
				cv.put(fs[i].getName(), "");//当不符合以上6中类型时，设为字符""
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		System.out.println("插入种");
		System.out.println("id"+cv.getAsInteger("id"));
		System.out.println("cityNum"+cv.getAsString("cityNum"));
		System.out.println("cityName"+cv.getAsString("cityName"));
		
//		show.id = 1;	/*城市id*/
//		show.cityNum = "002";	/*城市编号*/
//		show.cityName = "南京";	/*城市名称*/	
//		show.parentNum = "001";	/*所属省份编号*/
//		show.latitude = 180.00;	/*纬度*/
//		show.longitude = 190.00;	/*经度*/
//		
//		show.belong = "N";
//		show.isHot = 1;
//		show.label = "label";
//		show.pinyin = "nanjing";
//		show.storeShows = null;
//		cv.getAsString("cityName");
		db.insertWithOnConflict(Tab_Name, null, cv, SQLiteDatabase.CONFLICT_REPLACE);
//		db.in
		System.out.println("插入结束");
	}
	
	/** 获取整个记录的数量 */
	public int count(SQLiteDatabase db, Class<?> classType){
		//查询语句
		String sql = " SELECT count(*) FROM " + StringHelper.getClassName(classType.getName()); 
		//获取结果
		Cursor result = db.rawQuery(sql, null);
		
		result.moveToFirst();
		return result.getInt(0);

	}

	public <T> List<T> findAll(SQLiteDatabase db, Class<T> classType){
		
		String sql = "select * FROM  " + StringHelper.getClassName(classType.getName())+" order by id desc " ;//

		//查询
		System.out.println("成功获取1");
		Cursor result = db.rawQuery(sql, null);

		List<T> list = new ArrayList<T>();
		
		for(result.moveToFirst(); !result.isAfterLast(); result.moveToNext()){
			
			T t = null;
			try {
				t = (T) Select(classType.newInstance(), result);
			} catch (Exception e) {}
			
			list.add(t);
			System.out.println("sqlId："+((CityShow)t).cityId);
			System.out.println("sqlnumber："+((CityShow)t).id);
			System.out.println("sql城市："+((CityShow)t).cityName);
			System.out.println("sql城市："+((CityShow)t).cityName);
			System.out.println("sql经度："+((CityShow)t).longitude);
		}
		
		return list;
	}
	
	/**
	 * 字段反射，是按字母排序的
	 */
	private Object Select(Object object, Cursor result){
		
		Class<?> classType = object.getClass();
		
		Field[] fs = classType.getDeclaredFields();
		System.out.println("全部变量-----------------------------------");
		
		for (int i = 0; i < fs.length; i++) {
			System.out.println("变量名"+i+fs[i].getName());
			System.out.println("变量类型"+fs[i].getType().getName());
			
			if(fs[i].getType().getName().equals("java.lang.Integer")){
				fs[i].setAccessible(true);//暴力访问，取消age的私有权限。让对象可以访问
				try {
					fs[i].set(object, result.getInt(i));
					System.out.println("变量值"+result.getInt(i));
				} catch (Exception e) {
					e.printStackTrace();
				} 
			}
			
			if(fs[i].getType().getName().equals("java.lang.String")){
				fs[i].setAccessible(true);//暴力访问，取消age的私有权限。让对象可以访问
				try {
					fs[i].set(object, result.getString(i));
					System.out.println("变量值"+result.getString(i));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			if(fs[i].getType().getName().equals("java.lang.Double")){
				fs[i].setAccessible(true);//暴力访问，取消age的私有权限。让对象可以访问
				try {
					fs[i].set(object, result.getDouble(i));
					System.out.println("变量值"+result.getDouble(i));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			if(fs[i].getType().getName().equals("java.lang.Float")){
				fs[i].setAccessible(true);//暴力访问，取消age的私有权限。让对象可以访问
				try {
					fs[i].set(object, result.getFloat(i));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			if(fs[i].getType().getName().equals("java.lang.Short")){
				fs[i].setAccessible(true);//暴力访问，取消age的私有权限。让对象可以访问
				try {
					fs[i].set(object, result.getShort(i));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			if(fs[i].getType().getName().equals("java.lang.Long")){
				fs[i].setAccessible(true);//暴力访问，取消age的私有权限。让对象可以访问
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
