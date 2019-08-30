/*
 * @author : bbriones
 */
package com.vlocity;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.github.dexecutor.core.DefaultDexecutor;
import com.github.dexecutor.core.DexecutorConfig;
import com.github.dexecutor.core.ExecutionConfig;
import com.github.dexecutor.core.support.ThreadPoolUtil;
import com.github.dexecutor.core.task.TaskProvider;
import com.vlocity.model.Project;
import com.vlocity.model.Task;

public class Scheduler {
	
	private ExecutorService executorService;
	private DexecutorConfig<Integer, Integer> config;
	private DefaultDexecutor<Integer, Integer> executor;
	private static Project project;
		
	public Scheduler(Project proj) {
		this.project = proj;
		this.executorService = newExecutor();
		this.config = new DexecutorConfig<>(executorService, new SleepyTaskProvider());
		this.executor = new DefaultDexecutor<Integer, Integer>(config);
		this.setDependencies();
	}
	private ExecutorService newExecutor() {
		return Executors.newFixedThreadPool(ThreadPoolUtil.ioIntesivePoolSize());
	}
	
	private static class SleepyTaskProvider implements TaskProvider<Integer, Integer> {

		public com.github.dexecutor.core.task.Task<Integer, Integer> provideTask(final Integer id) {

			return new com.github.dexecutor.core.task.Task<Integer, Integer>() {

				public Integer execute() {
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					System.out.println(id);
					Task t = project.getTasks().get(id);
					generateSchedule(t);
					return id;
				}
			};			
		}		
	}
	
	public static void main(String[] args) {
		
		Project proj = new Project();
		SortedMap<Integer, Task> tasks = new TreeMap<Integer, Task>();
		
		Task one = new Task();
		one.setTaskId(new Integer(1));
		one.setDuration(2);
		
		Task two = new Task();
		two.setTaskId(new Integer(2));
		two.setDuration(4);
		List<Integer> twoDepends = new ArrayList<Integer>();
		twoDepends.add(one.getTaskId());
		two.setDependencies(twoDepends);
		
		Task three = new Task();
		three.setTaskId(new Integer(3));
		three.setDuration(3);
		List<Integer> threeDepends = new ArrayList<Integer>();
		threeDepends.add(one.getTaskId());
		threeDepends.add(two.getTaskId());
		three.setDependencies(threeDepends);
		
		Task four = new Task();
		four.setTaskId(new Integer(4));
		four.setDuration(3);
		List<Integer> fourDepends = new ArrayList<Integer>();
		fourDepends.add(one.getTaskId());
		four.setDependencies(fourDepends);
		
		tasks.put(three.getTaskId(), three);
		tasks.put(two.getTaskId(), two);
		tasks.put(one.getTaskId(), one);
		tasks.put(four.getTaskId(), four);
		
		proj.setTasks(tasks);
		
		Scheduler scheduler = new Scheduler(proj);
		scheduler.generate();
		System.exit(0); 
	}
	public void generate() {
		executor.execute(ExecutionConfig.NON_TERMINATING);
	}
	public void setDependencies() {
		SortedMap<Integer, Task> tasks = project.getTasks();
		
		Set tasksSet = tasks.entrySet();
		Iterator tasksIter = tasksSet.iterator();
		
		while(tasksIter.hasNext()) {
			Map.Entry<Integer, Task> m = (Map.Entry<Integer, Task>)tasksIter.next();
			Integer key = m.getKey();
			
			Task t = tasks.get(key);
			List<Integer> dependencies = t.getDependencies();
			List<LocalDate> endDates = new ArrayList<LocalDate>();
			if(!isNullOrEmpty(dependencies)) {
				for(Integer dependency : dependencies) {
					executor.addDependency(dependency, key);
				}
			}
			
		}
	}
	
	private synchronized static void generateSchedule(Task task){
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MM-dd");
		LocalDate startDate = !isNullOrEmpty(project.getProjectStartDate()) ? LocalDate.parse(project.getProjectStartDate()) : LocalDate.now();
		
		if(isNullOrEmpty(task.getDependencies())) {
			LocalDate endDate = startDate.plusDays(task.getDuration());
			task.setStartDate(startDate.format(formatter));
			task.setEndDate(endDate.format(formatter));
		}else {
			List<Integer> dependencies = task.getDependencies();
			List<LocalDate> endDates = new ArrayList<LocalDate>();
			for(Integer dependencyId : dependencies) {
				endDates.add(LocalDate.parse(project.getTasks().get(dependencyId).getEndDate()));
			}
			LocalDate maxEndDate = Collections.max(endDates);
			LocalDate endDate = startDate.plusDays(task.getDuration());
			task.setStartDate(maxEndDate.format(formatter));
			task.setEndDate(maxEndDate.plusDays(task.getDuration()).format(formatter));
		}
		System.out.println("VALUE = " + task);
	}
	
	private static long getTotalDuration(long duration, List<Task> dependencies) {
		long totalDuration = duration;
		for(Task dependency : dependencies) {
			totalDuration += dependency.getDuration();
		}
		return totalDuration;
	}
    public static boolean isNullOrEmpty(Object pTestObject) {
        // First test for Null
        if (pTestObject == null) {
            return true;
        }

        // Now Test for Empty. Test for Strings, Collections, Maps, and Arrays

        if (pTestObject instanceof String) {
            if ("".equals(((String) pTestObject).trim())) {
                return true;
            } else {
                return false;
            }
        }

        if (pTestObject instanceof Collection) {
            if (((Collection) pTestObject).isEmpty()) {
                return true;
            } else {
                return false;
            }
        }

        if (pTestObject instanceof Map) {
            if (((Map) pTestObject).isEmpty()) {
                return true;
            } else {
                return false;
            }
        }

        if (pTestObject instanceof Object[]) {
            if (((Object[]) pTestObject).length <= 0) {
                return true;
            } else {
                return false;
            }
        }

        if (pTestObject instanceof int[]) {
            if (((int[]) pTestObject).length <= 0) {
                return true;
            } else {
                return false;
            }
        }

        if (pTestObject instanceof char[]) {
            if (((char[]) pTestObject).length <= 0) {
                return true;
            } else {
                return false;
            }
        }

        if (pTestObject instanceof double[]) {
            if (((double[]) pTestObject).length <= 0) {
                return true;
            } else {
                return false;
            }
        }

        if (pTestObject instanceof float[]) {
            if (((float[]) pTestObject).length <= 0) {
                return true;
            } else {
                return false;
            }
        }

        if (pTestObject instanceof byte[]) {
            if (((byte[]) pTestObject).length <= 0) {
                return true;
            } else {
                return false;
            }
        }

        if (pTestObject instanceof long[]) {
            if (((long[]) pTestObject).length <= 0) {
                return true;
            } else {
                return false;
            }
        }

        if (pTestObject instanceof short[]) {
            if (((short[]) pTestObject).length <= 0) {
                return true;
            } else {
                return false;
            }
        }

        // If pTestObject is not a String, Collection, Map, or Array, then don't
        // test
        // for empty. Just return false (since it's not null).
        return false;
    }
	
}
