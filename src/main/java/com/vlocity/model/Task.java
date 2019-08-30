/*
 * @author : bbriones
 */
package com.vlocity.model;

import java.util.List;

public class Task {
	private Integer taskId;
	private String startDate;
	private String endDate;
	private long duration;//Unit - in days
	private List<Integer> dependencies;
	
	public Integer getTaskId() {
		return taskId;
	}
	public void setTaskId(Integer taskId) {
		this.taskId = taskId;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public long getDuration() {
		return duration;
	}
	public void setDuration(long duration) {
		this.duration = duration;
	}
	public List<Integer> getDependencies() {
		return dependencies;
	}
	public void setDependencies(List<Integer> dependencies) {
		this.dependencies = dependencies;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Schedule [taskId=");
		builder.append(taskId);
		builder.append(", startDate=");
		builder.append(startDate);
		builder.append(", endDate=");
		builder.append(endDate);
		builder.append(", duration=");
		builder.append(duration);
		builder.append(", dependencies=");
		builder.append(dependencies);
		builder.append("]");
		return builder.toString();
	}
	
	
}
