package com.yxy.sch.trigger;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.yxy.sch.Job;
import com.yxy.sch.Trigger;

/**
 * 天定时器
 * 
 * @author YXY
 * @date 2015年9月10日 下午3:38:24
 */
public class DailyTrigger extends Trigger {

	/** 定时器时间查询队列 */
	private List<Calendar> calList;
	private int index = 0;

	/**
	 * 
	 * @param name
	 *            任务名称
	 * @param expression
	 *            执行时间(多个时间之间用;隔开)
	 * @param job
	 *            要执行的工作
	 */
	public DailyTrigger(String name, String expression, Job job) {
		setName(name);
		setTask(job);
		setCalList(passTimeExp(expression));
		setNextTime(calList.get(0).getTimeInMillis());
	}

	/**
	 * 在每天指定的多个时间点执行的任务
	 * 
	 * @param name
	 *            定时器名称
	 * @param calList
	 *            要执行的时间点
	 * @param job
	 *            要执行的任务
	 */
	public DailyTrigger(String name, List<Calendar> calList, Job job) {
		setName(name);
		setTask(job);
		setCalList(passCalList(calList));
		setNextTime(calList.get(0).getTimeInMillis());
	}

	/**
	 * 过时时间设定
	 * 
	 * @param calList
	 * @return List<Calendar>
	 * @throws
	 * @author YXY
	 */
	private List<Calendar> passCalList(List<Calendar> calList) {
		Calendar curr = Calendar.getInstance();
		for (Calendar cal : calList) {
			// 时间不是今天，就设置成今天
			if (cal.compareTo(curr) < 0) {
				cal.add(5, 1);
			}
		}
		calSort(calList);
		return calList;
	}

	/**
	 * 根据时间点设置触发时间队列
	 * 
	 * @param expression
	 * @return
	 */
	private List<Calendar> passTimeExp(String expression) {
		List<Calendar> calList = new ArrayList<>();
		String[] array = expression.trim().split(";");
		Calendar curr = Calendar.getInstance();
		for (String hourMin : array)
			if (!hourMin.isEmpty()) {
				String[] time = hourMin.split(":");
				int hour = Integer.parseInt(time[0].trim());
				int minute = 0;
				if (time.length > 1) {
					minute = Integer.parseInt(time[1].trim());
				}
				int second = 0;
				if (time.length > 2) {
					second = Integer.parseInt(time[2].trim());
				}
				Calendar cal = Calendar.getInstance();
				cal.set(11, hour);
				cal.set(12, minute);
				cal.set(13, second);
				// 不是今天的时间就设置成今天的时间
				if (cal.compareTo(curr) < 0) {
					cal.add(5, 1);
				}
				calList.add(cal);
			}
		// 时间先后的排序
		calSort(calList);
		return calList;
	}

	private void calSort(List<Calendar> calList) {
		Collections.sort(calList, new Comparator<Calendar>() {
			@Override
			public int compare(Calendar o1, Calendar o2) {
				return (o1).compareTo(o2);
			}
		});
	}

	public void updateNextTime(long curr) {
		Calendar cal = calList.get(index);
		cal.add(5, 1);
		index = ((index + 1) % calList.size());
		setNextTime(calList.get(index).getTimeInMillis());
	}

	public List<Calendar> getCalList() {
		return this.calList;
	}

	public void setCalList(List<Calendar> calList) {
		this.calList = calList;
	}

}
