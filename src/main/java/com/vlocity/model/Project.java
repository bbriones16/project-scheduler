/*
 * @author : bbriones
 */
package com.vlocity.model;

import java.util.SortedMap;

public class Project {
	
	private String projectStartDate;
	private SortedMap<Integer, Task> tasks;

	public String getProjectStartDate() {
		return projectStartDate;
	}

	public void setProjectStartDate(String projectStartDate) {
		this.projectStartDate = projectStartDate;
	}

	public SortedMap<Integer, Task> getTasks() {
		return tasks;
	}

	public void setTasks(SortedMap<Integer, Task> tasks) {
		this.tasks = tasks;
	}
	
}
