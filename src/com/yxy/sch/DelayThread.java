package com.yxy.sch;

public class DelayThread implements Runnable {

	private String name;

	// /** 延时控制机制 */
	private volatile boolean paused;

	// private Lock lock;
	// private Condition cdt;

	public DelayThread(String name) {
		this.name = name;
		// this.lock = new ReentrantLock();
		// cdt = lock.newCondition();
		// System.err.println(name + "初始化完成..");
	}

	@Override
	public void run() {
		int i = 0;
		while (!paused) {
			i++;
			if (i % 10 == 0) {
				delay(3000);
				System.err.println(name + "开始执行" + i);
			}
		}
	}

	public void delay(int seconds) {
		// this.paused = false;
		try {
			super.wait(seconds);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// lock.lock();
		// try {
		// this.paused = true;
		// cdt.await(seconds, TimeUnit.SECONDS);
		// } catch (InterruptedException e) {
		// e.printStackTrace();
		// } finally {
		// lock.unlock();
		// this.paused = false;
		// }
		// lock.unlock();
	}

}
