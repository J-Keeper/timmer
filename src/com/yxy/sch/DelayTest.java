package com.yxy.sch;

public class DelayTest {

	public static void main(String[] args) {
		new Thread(new DelayThread("1号")).start();
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		new Thread(new DelayThread("2号")).start();
	}
}
