package com.yxy.sch;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;

/**
 * 定时器管理
 * 
 * @author YXY
 * @date 2015年9月10日 下午3:29:00
 */
public class ScheduledManager {
	private Logger log = Logger.getLogger(getClass());
	private static ScheduledManager instance = new ScheduledManager();

	/** 目前所有任务 */
	private Map<String, Trigger> runJobMap = new ConcurrentHashMap<>();
	/** 延迟队列中的任务 */
	private DelayQueue<Trigger> taskQueue = new DelayQueue<>();

	private boolean isWork = true;

	/** 管理线程 */
	private ExecutorService manager = Executors.newSingleThreadExecutor();
	/** 执行线程池 */
	private ExecutorService executor = Executors.newFixedThreadPool(Runtime
			.getRuntime().availableProcessors());

	public static ScheduledManager getInstance() {
		return instance;
	}

	private ScheduledManager() {
		startManager();
	}

	public void startManager() {
		manager.execute(new Runnable() {
			public void run() {
				while (isWork)
					try {
						Trigger trigger = taskQueue.take();
						executor.submit(new JobRun(trigger, runJobMap,
								taskQueue));
					} catch (InterruptedException e) {
						if (isWork) {
							log.error("定时器被打断:", e);
						}
					}
			}
		});
	}

	public void shutdown() {
		this.isWork = false;
		// 试图停止所有正在执行的活动任务，暂停处理正在等待的任务，并返回等待执行的任务列表
		this.manager.shutdownNow();
		// 启动一次顺序关闭，执行以前提交的任务，但不接受新任务
		this.executor.shutdown();
	}

	public Map<String, Trigger> getRunJobMap() {
		return runJobMap;
	}

	public DelayQueue<Trigger> getTaskQueue() {
		return taskQueue;

	}

	public void schedule(Trigger trigger) {
		if ((trigger == null) || (trigger.getName() == null)) {
			new IllegalArgumentException("任务为空or任务参数不完整").printStackTrace();
		} else if (runJobMap.containsKey(trigger.getName())) {
			new RuntimeException("已存在同名任务:" + trigger.getName())
					.printStackTrace();
		} else {
			trigger.setSeqNum(Trigger.sequencer.getAndIncrement());
			boolean ans = taskQueue.offer(trigger);
			if (ans) {
				runJobMap.put(trigger.getName(), trigger);
			} else {
				new RuntimeException("添加任务失败:" + trigger.getName())
						.printStackTrace();
			}
		}
	}

	public Trigger getTrigger(String name) {
		return runJobMap.get(name);
	}

	public boolean contains(String name) {
		return runJobMap.containsKey(name);
	}

	public boolean cancel(String jobName) {
		boolean ans = false;
		Trigger trigger = runJobMap.get(jobName);
		if (trigger != null) {
			trigger.setCancel(true);
			trigger.setTask(null);
			trigger.setListener(null);
			runJobMap.remove(trigger.getName());
			ans = true;
		}
		return ans;
	}
}