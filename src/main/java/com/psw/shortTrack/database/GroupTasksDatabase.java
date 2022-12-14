package com.psw.shortTrack.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;

import org.postgresql.util.PSQLException;

import com.psw.shortTrack.data.Account;
import com.psw.shortTrack.data.GroupTask;
import com.psw.shortTrack.data.Task;

public class GroupTasksDatabase extends Database {

	/**
	 * Creates a new task in the database. 
	 * 
	 * @param task Group task to add to the database
	 * @return (True) Success; (False) Error - The assigned_to account doesn't exist or null values
	 * 
	 * @throws NotFoundException If the group doesn't exist
	 * @throws SQLException If there was an error to connect to the database
	 */
	public static boolean createTask(GroupTask task) throws NotFoundException, SQLException {
		
		try {
			task.setID(executeQueryReturnInt(
				"INSERT INTO projeto.group_tasks (group_id, assigned_to, name, description, created_date, deadline_date, state)\r\n"
				+ "VALUES (" + toSQL(task.getParentID()) + "," + toSQL((String)task.getAssignedToEmail()) + "," 
				+ toSQL((String)task.getName()) + "," + toSQL((String)task.getDescription()) + "," 
				+ toSQL((LocalDate)task.getCreatedDate()) + "," + toSQL((LocalDate)task.getDeadlineDate()) + "," 
				+ toSQL(task.isCompleted()) + ")\r\n"
				+ "RETURNING id;")
			);
			return true;
		} catch (PSQLException psql) {
			if (psql.getSQLState().startsWith("23")) {
				if (!GroupsDatabase.existGroup(task.getParentID())) {
					throw new NotFoundException();
				}
				else {
					return false;
				}
			}
			
			throw psql;
		}
 		
	}
	
	/**
	 * Deletes a group task from database
	 * 
	 * @param id Task's id
	 * @return (True) If it succeeds; (False) The task still exists
	 * 
	 * @throws SQLException If there was an error in the connection to the database
	 */
	public static boolean deleteTask(int id) throws SQLException {
		
		if (executeUpdate(
			"DELETE FROM projeto.group_tasks WHERE id=" + toSQL(id) + ";"
		) > 0) {
			return true;
		}
		else {
			return !existTask(id);
		}
		
	}
	
	/**
	 * Returns all the tasks form the group identified with this id
	 * 
	 * @param group_id Desired Group's id
	 * @return ArrayList with all the group's tasks
	 * 
	 * @throws SQLException If there was an error in the connection to the database
	 */
	public static ArrayList<Task> getAllTasks(int group_id) throws SQLException {

		try (Connection connection = getConnection()){
			
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(
				"SELECT group_tasks.*, account.name AS assigned_name\r\n"
				+ "FROM projeto.group_tasks LEFT JOIN projeto.account ON assigned_to=account.email\r\n"
				+ "WHERE group_id=" + toSQL(group_id) + ";"
			);
			
			return resultSet_to_Task_Array(rs, null);
		}
		
	}
	
	/**
	 * Returns all the tasks from the group identified by this group_id and assigned to this member
	 * 
	 * @param group_id Group's id
	 * @param member Member assigned to
	 * @return ArrayList with all the group's tasks with member as assigned to
	 * 
	 * @throws SQLException If there was an error in the connection to the database
	 */
	public static ArrayList<Task> getAllTasks(int group_id, Account member) throws SQLException {
		
		try (Connection connection = getConnection()){
			
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(
					"SELECT * FROM projeto.group_tasks WHERE group_id=" + toSQL(group_id) + " AND assigned_to=" + toSQL((String)member.getEmail()) + ";"
			);
			
			return resultSet_to_Task_Array(rs, member);
		}
		
	}
	
	/**
	 * Updates groupTask data in the database. You need to provide all the updated data.
	 * Before changing the data, it verifies if the user is still the manager.
	 * 
	 * @param id Task's id
	 * @param email User's email
	 * @param newName Task's new name
	 * @param newDescription Task's new description
	 * @param newDeadline Task's new deadline date
	 * @param newState Task's new completed state
	 * @param newAssignedTo Task's new assigned to member
	 * @return (True) Success; (False) If the user has no privileges to edit the task or null values
	 * 
	 * @throws NotFoundException If the task doesn't exist
	 * @throws SQLException If there was an error in the connection to the database
	 */
	public static boolean updateTask(int id, String email, String newName, String newDescription, LocalDate newDeadline) 
			throws SQLException {
		
		if (executeUpdate(
				"UPDATE projeto.group_tasks\r\n"
				+ "SET name=" + toSQL((String)newName) + ",description=" + toSQL((String)newDescription) 
				+ ",deadline_date=" + toSQL((LocalDate)newDeadline) + "\r\n"
				+ "FROM projeto.groups WHERE group_id=groups.id\r\n"
				+ "AND group_tasks.id=" + toSQL(id) + " AND (assigned_to=" + toSQL((String)email) 
				+ " OR manager=" + toSQL((String)email) + ");"
		) > 0) {
			return true;
		}
		else if (!existTask(id)) {
			throw new NotFoundException();
		}
		else {
			return false;
		}
		
	}
	
	/**
	 * Updates the new state of a task to the database
	 * 
	 * @param id Task's id
	 * @param newState New state of the task
	 * @return (True) Success; (False) You have no privileges to edit the task
	 * 
	 * @throws NotFoundException If the task doesn't exist
	 * @throws SQLException
	 */
	public static boolean changeState(int id, boolean newState) throws NotFoundException, SQLException {
		
		if (newState)
			NotificationDatabase.clearHelpRequests(id);
		
		if (executeUpdate(
				"UPDATE projeto.group_tasks SET state=" + toSQL(newState) + " WHERE id=" + toSQL(id) + ";"
		) > 0) {
			return true;
		}
		else if (!existTask(id)) {
			throw new NotFoundException();
		}
		else {
			return false;
		}
		
	}
	
	/**
	 * 
	 * Changes the assigned to user of the task
	 * 
	 * @param id Task's id
	 * @param assignedTo New assigned to's email
	 * @return (True) Success; (False) 
	 * 
	 * @throws NotFoundException If the task doesn't exist
	 * @throws SQLException If there is a connection error
	 */
	public static boolean changeAssignedTo(int id, String assignedTo) throws NotFoundException, SQLException{
		
		NotificationDatabase.clearHelpRequests(id);
		if (executeUpdate(
				"UPDATE projeto.group_tasks SET assigned_to=" + toSQL((String)assignedTo) + "\r\n"
				+ "WHERE id=" + toSQL(id) + ";"
		) > 0) {
			return true;
		}
		else if (!existTask(id)) {
			throw new NotFoundException();
		}
		else {
			return false;
		}
		
	}
	
	/**
	 * Returns all the group tasks in a resultSet
	 * 
	 * @param rs ResultSet
	 * @return ArrayList(Task) with all the group tasks in the result set
	 * 
	 * @throws SQLException If there was an error in the connection to the database
	 */
	private static ArrayList<Task> resultSet_to_Task_Array(ResultSet rs, Account member) throws SQLException {
		
		ArrayList<Task> arrayTask = new ArrayList<Task>();
		while (rs.next()) {
			String deadline_str = rs.getString("deadline_date");
			LocalDate deadline = null;
			if (deadline_str != null)
				deadline = LocalDate.parse(deadline_str);
			String created_str = rs.getString("created_date");
			LocalDate createdDate = null;
			if (created_str != null)
				createdDate = LocalDate.parse(created_str);
			
			Account assignTo = member;
			if (assignTo == null && rs.getString("assigned_to") != null) {
				assignTo = new Account(rs.getString("assigned_to"), rs.getString("assigned_name"));
			}
			
			arrayTask.add(new GroupTask(rs.getString("name"),
										rs.getInt("id"),
										rs.getString("description"),
										createdDate,
										deadline,
										rs.getBoolean("state"),
										rs.getInt("group_id"),
										assignTo
										));
		}
		return arrayTask;
		
	}
	
	/**
	 * Checks if the user is either the manager or the assigned to person of the task
	 * 
	 * @param id Task's id
	 * @param email User's email
	 * @return (True) The user is the manager or the assigned to person of the task; (False) No privileges
	 * 
	 * @throws NotFoundException If the task doesn't exist
	 * @throws SQLException If there is a connection error
	 */
	public static boolean hasPrivilege(int id, String email) throws NotFoundException, SQLException {
		
		if (!existTask(id)) {
			throw new NotFoundException();
		}
		return executeQueryReturnBoolean(
			"SELECT EXISTS(SELECT 1 FROM projeto.group_tasks LEFT JOIN projeto.groups ON group_id=groups.id\r\n"
			+ "WHERE group_tasks.id=" + toSQL(id) 
			+ " AND (assigned_to=" + toSQL((String)email) + " OR groups.manager=" + toSQL((String)email) + "));"
		);
		
	}
	
	/**
	 * 
	 * Checks if the task with the provided id exists in the database
	 * 
	 * @param id Task's id
	 * @return (True) It exists; (False) Otherwise
	 * 
	 * @throws SQLException If there is a conenction error
	 */
	private static boolean existTask(int id) throws SQLException {
		
		return executeQueryReturnBoolean(
			"SELECT EXISTS(SELECT 1 FROM projeto.group_tasks WHERE id=" + toSQL(id) + ");"
		);
		
	}
	
	
}
