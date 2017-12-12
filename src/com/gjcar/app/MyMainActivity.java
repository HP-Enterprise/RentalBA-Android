package com.gjcar.app;

import com.gjcar.annotation.ContentView;
import com.gjcar.annotation.ContentWidget;
import com.gjcar.utils.F;
import com.gjcar.utils.AnnotationViewUtils;
import com.gjcar.utils.IntentHelper;
import com.gjcar.view.dialog.DateTimePickerDialog;
import com.gjcar.view.helper.ViewInitHelper;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

@ContentView(R.layout.mytab_title)
public class MyMainActivity extends Activity {


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AnnotationViewUtils.injectObject(this, this);
				
		/*初始化时间*/
		//ViewInitHelper.init_f1_DateTime(new TextView[]{take_date,take_time,return_date,return_time});
		
//		IntentHelper.startActivity(context, cls, keys, values)
	}


	

}
