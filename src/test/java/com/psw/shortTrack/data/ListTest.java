package com.psw.shortTrack.data;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

public class ListTest {
	
	@Test
	void TestCheckNamePositive() {
		
		List list = new List("List 1",10);
		PersonalTask task = new PersonalTask("Task 1",10);
		list.addTask(task);
		
		assertEquals(true, list.checkName("Task 1"));
	}
	
	@Test
	void TestCheckNameNegative() {
		
		List list = new List("List 1",10);
		PersonalTask task = new PersonalTask("Task 1",10);
		list.addTask(task);
		
		assertEquals(false, list.checkName("Task 2"));
	}
	
	@Test
	void TestShortByNameWithNumbers() {
		
		List listToTest = new List("List to Test",10);
		PersonalTask task1 = new PersonalTask("Task 1",10);
		PersonalTask task2 = new PersonalTask("Task 2",10);
		PersonalTask task3 = new PersonalTask("Task 3",10);
		PersonalTask task4 = new PersonalTask("Task 4",10);
		listToTest.addTask(task3);
		listToTest.addTask(task1);
		listToTest.addTask(task2);
		listToTest.addTask(task4);
		
		listToTest.sortByName();
		
		List ordeorderedList = new List("Ordered list",11);
		ordeorderedList.addTask(task1);
		ordeorderedList.addTask(task2);
		ordeorderedList.addTask(task3);
		ordeorderedList.addTask(task4);
		
		assertArrayEquals(ordeorderedList.getTaskList().toArray(),listToTest.getTaskList().toArray());
	}
	
	@Test
	void TestShortByNameWithLetters() {
		
		List listToTest = new List("List to Test",10);
		PersonalTask task1 = new PersonalTask("First",10);
		PersonalTask task2 = new PersonalTask("Second",10);
		PersonalTask task3 = new PersonalTask("Third",10);
		PersonalTask task4 = new PersonalTask("Fourth",10);
		listToTest.addTask(task2);
		listToTest.addTask(task1);
		listToTest.addTask(task3);
		listToTest.addTask(task4);
		
		listToTest.sortByName();
		
		List ordeorderedList = new List("Ordered list",11);
		ordeorderedList.addTask(task1);
		ordeorderedList.addTask(task4);
		ordeorderedList.addTask(task2);
		ordeorderedList.addTask(task3);
		
		assertArrayEquals(ordeorderedList.getTaskList().toArray(),listToTest.getTaskList().toArray());
	}
	
	@Test
	void TestShortByDeadlineDateWithoutNullElements() {
		
		List listToTest = new List("List to Test",10);
		PersonalTask task1 = new PersonalTask("Task 1",10);
		task1.setDeadline(LocalDate.of(2022, 3, 11));
		PersonalTask task2 = new PersonalTask("Task 2",10);
		task2.setDeadline(LocalDate.of(2022, 6, 20));
		PersonalTask task3 = new PersonalTask("Task 3",10);
		task3.setDeadline(LocalDate.of(2022, 8, 1));
		PersonalTask task4 = new PersonalTask("Task 4",10);
		task4.setDeadline(LocalDate.of(2023, 3, 11));
		listToTest.addTask(task3);
		listToTest.addTask(task4);
		listToTest.addTask(task2);
		listToTest.addTask(task1);
		
		listToTest.sortByDeadline();
		
		List ordeorderedList = new List("Ordered list",11);
		ordeorderedList.addTask(task1);
		ordeorderedList.addTask(task2);
		ordeorderedList.addTask(task3);
		ordeorderedList.addTask(task4);
		
		assertArrayEquals(ordeorderedList.getTaskList().toArray(),listToTest.getTaskList().toArray());
	}
	
	@Test
	void TestShortByDeadlineDateWithNullElements() {
		
		List listToTest = new List("List to Test",10);
		PersonalTask task1 = new PersonalTask("Task 1",10);
		task1.setDeadline(LocalDate.of(2022, 3, 10));
		PersonalTask task2 = new PersonalTask("Task 2",10);
		task2.setDeadline(LocalDate.of(2022, 3, 15));
		PersonalTask task3 = new PersonalTask("Task 3",10);
		PersonalTask task4 = new PersonalTask("Task 4",10);
		listToTest.addTask(task3);
		listToTest.addTask(task1);
		listToTest.addTask(task2);
		listToTest.addTask(task4);
		
		listToTest.sortByDeadline();
		
		List ordeorderedList = new List("Ordered list",11);
		ordeorderedList.addTask(task1);
		ordeorderedList.addTask(task2);
		ordeorderedList.addTask(task3);
		ordeorderedList.addTask(task4);
		
		assertArrayEquals(ordeorderedList.getTaskList().toArray(),listToTest.getTaskList().toArray());
	}
	
	@Test
	void TestShortByCompleted() {
		
		List listToTest = new List("List to Test",10);
		PersonalTask task1 = new PersonalTask("Task 1",10);
		task1.setCompleted(false);
		PersonalTask task2 = new PersonalTask("Task 2",10);
		task2.setCompleted(false);
		PersonalTask task3 = new PersonalTask("Task 3",10);
		task3.setCompleted(true);
		PersonalTask task4 = new PersonalTask("Task 4",10);
		task4.setCompleted(true);
		listToTest.addTask(task3);
		listToTest.addTask(task1);
		listToTest.addTask(task4);
		listToTest.addTask(task2);
		
		listToTest.sortByCompleted();
		
		List ordeorderedList = new List("Ordered list",11);
		ordeorderedList.addTask(task1);
		ordeorderedList.addTask(task2);
		ordeorderedList.addTask(task3);
		ordeorderedList.addTask(task4);
		
		assertArrayEquals(ordeorderedList.getTaskList().toArray(),listToTest.getTaskList().toArray());
	}
}