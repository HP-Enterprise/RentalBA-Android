package com.gjcar.activity.fragment5;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gjcar.app.R;
import com.gjcar.utils.AnnotationViewFUtils;

public class Fragment5 extends Fragment{

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.fragment3, null);		
		AnnotationViewFUtils.injectObject(this, getActivity(), view);
		
		return view;
	}
}
