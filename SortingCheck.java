package com.enterprise.MyPOCs;

import java.util.Arrays;
import java.util.List;

public class SortingCheck {

	public static void main(String[] args) {
//		List<String> inputList = Arrays.asList("Arctic", "Antarctica", "Belgium", "Chile", "China");
		List<String> inputList = Arrays.asList("CAB", "BB", "CB", "DB", "EB");
		if(isSorted(inputList)){
			System.out.println("EVERYTHING LOOKS GOOD ! ");
		} else {
			System.out.println("LIST IS NOT SORTED YO !");
		}

	}
	
	public static boolean isSorted(List<String> inputList){
		boolean sorted = true;        
	    for (int index = 1; index < inputList.size(); index++) {
	        if (inputList.get(index-1).compareTo(inputList.get(index)) > 0) {
	        	sorted = false;
	        	System.out.println("Second Last Element: "+inputList.get(index-1));
	        	System.out.println("Last Element: "+inputList.get(index));
	        }
	    }
	    return sorted;
	}

}
