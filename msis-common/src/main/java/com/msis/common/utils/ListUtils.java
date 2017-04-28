package com.msis.common.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ListUtils 
{
	@SuppressWarnings("unchecked")
	public static <T> List<T> toList(final Iterable<T> iterable) {
	    if (iterable != null && iterable.iterator().hasNext())
	    	return (List<T>) StreamSupport.stream(iterable.spliterator(), false)
	                        .collect(Collectors.toList());
	    return null;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> List<T> okList(List<T> list) {
		if (list == null)
			list = new ArrayList<T>();
		return list;
	}
}
