package com.psw.shortTrack.data;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;

public abstract class TaskOrganizer implements Serializable {
	
	private static final long serialVersionUID = 5424054108791604712L;
	
	public static int idCount = 1;
	protected int id;
	protected String name;
	protected ArrayList<Task> taskList;
	
	public TaskOrganizer(String name) {
		this.name = name;
		this.id = idCount++;
		
		taskList = new ArrayList<Task>();
	}
	
	public TaskOrganizer(String name, int id) {
		this.name = name;
		this.id = id;
		
		taskList = new ArrayList<Task>();
	}
	
	public TaskOrganizer(String name, int id, ArrayList<Task> taskList) {
		this.name = name;
		this.id = id;
		this.taskList = taskList;
	}
	
	public int getID() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setID(int id) {
		this.id = id;
	}
	
	public void setName(String newName) {
		name = newName;
	}
	
	public ArrayList<Task> getTaskList() {
		return taskList;
	}
	
	public void removeTask(Task task) {
		taskList.remove(task);
	}
	
	public boolean checkName(String name) {
		
		for(Task t : taskList) {
			if(t.getName().equals(name))
				return true;
		}
		
		return false;
	}
	
	public void sortByName() {
		
		if(taskList == null)
			return;
		
		ArrayList<Task> newTaskList = new ArrayList<Task>();
        while(taskList.size() != 0) {
        	Task pickTask = taskList.get(0);
    		for(Task t : taskList) {
    			if(t.getName().compareTo(pickTask.getName()) < 0)
    				pickTask = t;
    		}
    		taskList.remove(pickTask);
    		newTaskList.add(pickTask);
        }
        
        taskList = newTaskList;
        
	}
	
	public void sortByCreatedDate() {
		
		if(taskList == null)
			return;
		
		ArrayList<Task> newTaskList = new ArrayList<Task>();
        while(taskList.size() != 0) {
        	Task pickTask = taskList.get(0);
    		for(Task t : taskList) {
    			if(t.getCreatedDate().compareTo(pickTask.getCreatedDate()) < 0)
    				pickTask = t;
    		}
    		taskList.remove(pickTask);
    		newTaskList.add(pickTask);
        }
        
        taskList = newTaskList;
        
	}
	
	public void sortByDeadline() {
		
		if(taskList == null)
			return;	
		
		ArrayList<Task> newTaskList = new ArrayList<Task>();	
		for(int i=0; i < taskList.size(); i++) {
        	Task pickTask = taskList.get(i);
        	
        	if(pickTask.getDeadlineDate() == null)
        		continue;
        	
    		for(Task t : taskList) {
    			if(t.getDeadlineDate() == null)
            		continue;
    			
    			if(t.getDeadlineDate().compareTo(pickTask.getDeadlineDate()) < 0)
    				pickTask = t;
    		}
    		
    		taskList.remove(pickTask);
    		newTaskList.add(pickTask);
    		i--;
        }
        
        while(taskList.size() != 0) {
			Task t = taskList.get(0);
			taskList.remove(t);
        	newTaskList.add(t);
		}
        
        taskList = newTaskList;
        
	}
	
	public void sortByCompleted() {
		
		if(taskList == null)
			return;
		
		ArrayList<Task> newTaskList = new ArrayList<Task>();	
		for(int i=0; i < taskList.size(); i++) {
			Task t = taskList.get(i);
			
			if(!t.chekCompleted()) {
        		taskList.remove(t);
        		newTaskList.add(t);
        		i--;
        	}
		}
		
		while(taskList.size() != 0) {
			Task t = taskList.get(0);
			taskList.remove(t);
        	newTaskList.add(t);
		}
        
        taskList = newTaskList;

	}
	
	// Add all tasks that include that string in taskList
		public void findTaskByName(String searchName, ArrayList<Task> tasks) {
			
			for(Task t: taskList) {
				if(t.getName().contains(searchName))
					tasks.add(t);
			}

		}
		
		public void findTaskByCreatedDate(String searchCreatedDate, ArrayList<Task> tasks) {
			
			for(Task t: taskList) {
				
				String createdDate = t.getCreatedDate().toString();
				if(createdDate.contains(searchCreatedDate))
					tasks.add(t);
			}

		}
		
		public void findTaskByDeadline(String searchDeadline, ArrayList<Task> tasks) {
			
			String deadline;
			for(Task t: taskList) {
				
				LocalDate deadlineDate = t.getDeadlineDate();
				if(deadlineDate == null)
					deadline = "";
				else
					deadline = deadlineDate.toString();
				
				if(deadline.contains(searchDeadline))
					tasks.add(t);
			}

		}

}