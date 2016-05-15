package com.dao;

import java.util.List;

import com.model.TblEvent;

public interface TblEventDao {
	
	public void addEvent(TblEvent event);
	public void deleteEvent(String name);
	public void updateEvent(TblEvent event);
	public List<TblEvent> getAllEvents(int startPageIndex, int recordsPerPage);
	public List<TblEvent> getAllEvents();
	public TblEvent getEventById(String eventName);
	public int getEventCount();
}
