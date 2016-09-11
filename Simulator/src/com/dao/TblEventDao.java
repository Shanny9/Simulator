package com.dao;

import java.sql.SQLException;
import java.util.List;

import com.model.TblEvent;

public interface TblEventDao {
	
	public void addEvent(TblEvent event) throws SQLException;
	public void deleteEvent(int event_id) throws SQLException;
	public void updateEvent(TblEvent event, int eventId) throws SQLException;
	public List<TblEvent> getAllEvents(int startPageIndex, int recordsPerPage);
	public List<TblEvent> getAllEvents();
	public TblEvent getEventById(int event_id);
	public int getEventCount();
}
