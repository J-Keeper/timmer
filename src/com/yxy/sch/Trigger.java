package com.yxy.sch;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 每个任务对应的触发器
 * 
 * @author YXY
 * @date 2015年9月10日 下午2:53:09
 */
public abstract class Trigger implements Delayed {

	/** 要触发的次数，默认为-1，表示只触发一次 */
	public static int REPEAT_INDEFINITELY = -1;
	/** 触发器的优先 */
	public static final AtomicLong sequencer = new AtomicLong(0L);
	/** 任务创建创建时间 */
	private long createTime = System.currentTimeMillis();

	/** 任务名称 */
	private String name;
	/** 准备时间 */
	private long preTime;
	/** 下一刻校验时间 */
	private long nextTime;

	/** 该任务是否已经完成 */
	private boolean complete = false;
	/** 是否已经取消 */
	private boolean cancel = false;
	/** 已经触发的次数 */
	private int triggerCount = 0;

	/** 触发器优先级 */
	private long seqNum;

	/** 要执行的任务 */
	private Job task;
	/** TODO 任务的监听对象(扩展) */
	private JobListener listener;

	/**
	 * 直接更新下一刻执行时间的方法
	 * 
	 * @param nextTime
	 */
	public abstract void updateNextTime(long paramLong);

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getPreTime() {
		return preTime;
	}

	public void setPreTime(long preTime) {
		this.preTime = preTime;
	}

	public long getNextTime() {
		return nextTime;
	}

	public void setNextTime(long nextTime) {
		this.nextTime = nextTime;
	}

	public boolean isComplete() {
		return complete;
	}

	public void setComplete(boolean complete) {
		this.complete = complete;
	}

	public boolean isCancel() {
		return cancel;
	}

	public void setCancel(boolean cancel) {
		this.cancel = cancel;
	}

	public int getTriggerCount() {
		return triggerCount;
	}

	public void setTriggerCount(int triggerCount) {
		this.triggerCount = triggerCount;
	}

	public Job getTask() {
		return task;
	}

	public void setTask(Job task) {
		this.task = task;
	}

	public JobListener getListener() {
		return listener;
	}

	public void setListener(JobListener listener) {
		this.listener = listener;
	}

	public void setSeqNum(long seqNum) {
		this.seqNum = seqNum;
	}

	public long getSeqNum() {
		return seqNum;
	}

	/**
	 * 任务优先级比较
	 */
	public int compareTo(Delayed o) {
		int ans = 0;
		if (o == this) {
			return ans;
		}
		if ((o instanceof Trigger)) {
			Trigger trg = (Trigger) o;
			// 下一刻执行时间越近的优先,时间相同比较优先级
			if (getNextTime() == trg.getNextTime()) {
				if (getSeqNum() < trg.getSeqNum()) {
					ans = -1;
				} else {
					ans = 1;
				}
			} else if (getNextTime() < trg.getNextTime())
				ans = -1;
			else {
				ans = 1;
			}

		} else if (getDelay(TimeUnit.MILLISECONDS) < o
				.getDelay(TimeUnit.MILLISECONDS)) {
			ans = -1;
		} else if (getDelay(TimeUnit.MILLISECONDS) > o
				.getDelay(TimeUnit.MILLISECONDS)) {
			ans = 1;
		}
		return ans;
	}

	public long getDelay(TimeUnit unit) {
		return nextTime - System.currentTimeMillis();
	}

	public long getCreateTime() {
		return createTime;
	}

	public String toString() {
		return "Trigger [createTime=" + createTime + ", name=" + name
				+ ", preTime=" + preTime + ", nextTime=" + nextTime
				+ ", complete=" + complete + ", cancel=" + cancel
				+ ", triggerCount=" + triggerCount + ", seqNum=" + seqNum
				+ ", task=" + task + ", listener=" + listener + "]";
	}
}