package com.gjcar.utils;

import java.lang.reflect.Field;

import android.database.sqlite.SQLiteDatabase;

public class SQL_TableHelper {
	
	/**����id,����id�����ˣ����Դ�i=1,��ʼ*/
	public void createTable(SQLiteDatabase db, Class<?> classType){
		
		Field[] fs = classType.getDeclaredFields();
		
		String Tab_Name = StringHelper.getClassName(classType.getName());
		String Primary_Key = "[PK_"+Tab_Name+"]";
		StringBuffer sql = new StringBuffer("CREATE TABLE IF NOT EXISTS " + Tab_Name + "(");
		
		for (int i = 0; i < fs.length; i++) {
			if(fs[i].getName().equals("id")){
				sql.append(" id Integer NOT NULL , ");
				continue;
			}
			sql.append(fs[i].getName()+",");
		}

		sql.append(" CONSTRAINT "+Primary_Key+" PRIMARY KEY (id) ");
		sql.append(")");
		
		System.out.println("�������"+sql.toString());
		db.execSQL(sql.toString());System.out.println("����");
	} 
}
