package com.yxy.sch;

import java.util.Map;
import java.util.concurrent.DelayQueue;

import org.apache.log4j.Logger;

/**
 * 延迟任务执行线程
 * 
 * @author YXY
 * @date 2015年9月10日 下午3:05:07
 */
public class JobRun implements Runnable {
	private Logger log = Logger.getLogger(getClass());

	private Trigger trigger;

	/** 目前拥有的任务列表 */
	private Map<String, Trigger> runJobMap;
	/** 延迟中的任务 */
	private DelayQueue<Trigger> taskQueue;

	public JobRun(Trigger trigger, Map<String, Trigger> runJobMap,
			DelayQueue<Trigger> taskQueue) {
		this.trigger = trigger;
		this.runJobMap = runJobMap;
		this.taskQueue = taskQueue;
	}

	@Override
	public void run() {
		// 任务执行完成
		if (trigger.isComplete()) {
			runJobMap.remove(trigger.getName());
			return;
		}
		// 不继续执行
		if (this.trigger.isCancel()) {
			return;
		}
		try {
			long start = System.currentTimeMillis();
			beforeFire(trigger, System.currentTimeMillis());
			fire();
			afterFire(trigger, start);
			long cost = System.currentTimeMillis() - start;
			String notice = String.format("%s 任务执行时间: %s ms", new Object[] {
					trigger.getName(), Long.valueOf(cost) });
			log.warn(notice);
			// 任务执行完成
			if (trigger.isComplete()) {
				runJobMap.remove(trigger.getName());
				return;
			}
			taskQueue.offer(trigger);
		} catch (Exception e) {
			log.error("Trigger Error, Trigger=" + trigger.getName(), e);
			if (!taskQueue.offer(trigger)) {
				log.warn("重新加入任务队列失败: Trigger=" + trigger.getName());
			}
		}
	}

	private void fire() {
		if (trigger.isCancel() || trigger.isComplete()) {
			return;
		}
		try {
			trigger.getTask().execute(trigger);
		} catch (Exception e) {
			log.error("Job.execute() error!", e);
		}

		// TODO 扩展 && (trigger.getListener() != null)
		// if (trigger.getTriggerCount() == 0) {
		// // trigger.getListener().startJob();
		// try {
		// trigger.getTask().execute(trigger);
		// } catch (Exception e) {
		// log.error("Job.execute() error!", e);
		// }
		// }
		// if ((trigger.getListener() != null) && (trigger.isComplete())) {
		// try {
		// trigger.getListener().endJob();
		// } catch (Exception e) {
		// log.error("Listener.endJob() error!", e);
		// }
		// }
	}

	/**
	 * 开始触发前的准备
	 * 
	 * @param trigger
	 * @param curr
	 */
	private void beforeFire(Trigger trigger, long curr) {
		trigger.setPreTime(curr);
		trigger.updateNextTime(curr);
	}

	/**
	 * 触发完成之后的处理
	 * 
	 * @param trigger
	 * @param curr
	 */
	private void afterFire(Trigger trigger, long curr) {
		// 已经触发次数
		trigger.setTriggerCount(trigger.getTriggerCount() + 1);
		// 优先数+1
		trigger.setSeqNum(Trigger.sequencer.getAndIncrement());
	}
}