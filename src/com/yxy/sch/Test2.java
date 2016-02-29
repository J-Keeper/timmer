package com.yxy.sch;

import com.yxy.sch.trigger.SimpleTrigger;

public class Test2 {
	public static void main(String[] args) {
		ScheduledManager manager = ScheduledManager.getInstance();
		manager.schedule(new SimpleTrigger("test", DateUtil
				.getTodayDate("18:12:50"), 3, 6000, new Job() {
			@Override
			public void execute(Trigger t) {
				System.err.println("How are you!");
			}
		}));
	}
}
