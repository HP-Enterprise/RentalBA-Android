package com.gjcar.data.service;

import java.util.ArrayList;
import java.util.List;

import com.gjcar.data.bean.ScoreChange;

public class ScoreChangeHelper {

	public static List<ScoreChange> getList(List<ScoreChange> list){
		
		List<ScoreChange> mylist = new ArrayList<ScoreChange>();
		
		for (ScoreChange scoreChange : list) {
			
			if(scoreChange.applySource.equals("2") || scoreChange.applySource.equals("0")){
				mylist.add(scoreChange);
			}
		}
		
		return mylist;
	}
}
