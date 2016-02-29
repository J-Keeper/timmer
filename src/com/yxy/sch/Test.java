package com.yxy.sch;

import com.yxy.sch.trigger.SimpleTrigger;

public class Test {
	public static void main(String[] args) {
		ScheduledManager manager = ScheduledManager.getInstance();
		manager.startManager();

		manager.schedule(new SimpleTrigger("SimpleTest", DateUtil
				.getTodayDate("18:12:30"), 10, 6000, new Job() {
			@Override
			public void execute(Trigger t) {
				System.err.println("Hello!");
			}
		}));
	}
}
