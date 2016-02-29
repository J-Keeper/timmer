package com.yxy.sch.trigger;

import java.util.Date;

import org.apache.log4j.Logger;

import com.yxy.sch.Job;
import com.yxy.sch.Trigger;

/**
 * 通用普通定时器
 * 
 * @author YXY
 * @date 2015年9月10日 下午3:42:45
 */
public class SimpleTrigger extends Trigger {

	private static final Logger log = Logger.getLogger(SimpleTrigger.class);

	private int repeatCount = REPEAT_INDEFINITELY;

	/** 间隔时间 */
	private int repeatInterval;
	private Date endTime;

	/**
	 * 构建一个指定开始时间，重复执行时间间隔，执行次数，结束时间的定时器
	 * 
	 * @param name
	 *            定时器名称
	 * @param startTime
	 *            开始时间
	 * @param repeatCount
	 *            重复次数
	 * @param repeatInterval
	 *            重复时间间隔-ms
	 * @param endTime
	 *            结束时间
	 * @param task
	 *            要执行任务的名称
	 */
	public SimpleTrigger(String name, Date startTime, int repeatCount,
			int repeatInterval, Date endTime, Job task) {
		setName(name);
		this.repeatCount = repeatCount;
		this.repeatInterval = repeatInterval;
		this.endTime = endTime;
		if (startTime.getTime() > System.currentTimeMillis()) {
			setNextTime(startTime.getTime());
			setTask(task);
		} else {
			setCancel(true);
			log.warn("已取消一个开始于过去时间的任务:" + name);
		}
	}

	/**
	 * 构建在指定时间执行一次的定时器
	 * 
	 * @param name
	 *            定时器名称
	 * @param startTime
	 *            开始时间
	 * @param task
	 *            任务名称
	 */
	public SimpleTrigger(String name, Date startTime, Job task) {
		this(name, startTime, 1, 32767, task);
	}

	/**
	 * 构建指定开始时间，执行次数，执行间隔的定时器
	 * 
	 * @param name
	 *            定时器名称
	 * @param startTime
	 *            开始时间
	 * @param repeatCount
	 *            执行次数
	 * @param repeatInterval
	 *            前后两次执行之间的时间间隔
	 * @param task
	 *            要执行的任务
	 */
	public SimpleTrigger(String name, Date startTime, int repeatCount,
			int repeatInterval, Job task) {
		this(name, startTime, repeatCount, repeatInterval, null, task);
	}

	/**
	 * 构建指定开始时间，重复间隔的定时器
	 * 
	 * @param name
	 *            定时器名称
	 * @param startTime
	 *            开始时间
	 * @param repeatInterval
	 *            重复间隔
	 * @param task
	 *            要执行的任务
	 */
	public SimpleTrigger(String name, Date startTime, int repeatInterval,
			Job task) {
		this(name, startTime, REPEAT_INDEFINITELY, repeatInterval, null, task);
	}

	/**
	 * 构建指定开始时间，结束时间，重复间隔的定时器
	 * 
	 * @param name
	 *            定时器名称
	 * @param startTime
	 *            开始时间
	 * @param endStime
	 *            结束时间
	 * @param repeatInterval
	 *            重复间隔
	 * @param job
	 *            工作名称
	 */
	public SimpleTrigger(String name, Date startTime, Date endStime,
			int repeatInterval, Job job) {
		this(name, startTime, 0, repeatInterval, endStime, job);
	}

	public Date getEndTime() {
		return this.endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public int getRepeatCount() {
		return this.repeatCount;
	}

	public void setRepeatCount(int repeatCount) {
		this.repeatCount = repeatCount;
	}

	public int getRepeatInterval() {
		return this.repeatInterval;
	}

	public void setRepeatInterval(int repeatInterval) {
		this.repeatInterval = repeatInterval;
	}

	/**
	 * 更新下一刻出发时间
	 */
	public void updateNextTime(long curr) {
		setNextTime(curr + getRepeatInterval());
		if (getRepeatCount() == Trigger.REPEAT_INDEFINITELY) {
			return;
		}
		// 指定了结束时间
		if (getEndTime() != null) {
			if (getNextTime() > getEndTime().getTime())
				setComplete(true);
		}
		// 已经执行完指定的次数
		else if (getTriggerCount() + 1 >= getRepeatCount()) {
			setComplete(true);
		}
	}

}
