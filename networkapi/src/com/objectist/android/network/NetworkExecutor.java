package com.objectist.android.network;

import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import android.util.Log;

public class NetworkExecutor {
	private Object commonParameters ;
	final ArrayList<Cancellable> tasks = new ArrayList<Cancellable>();
	@SuppressWarnings("unused")
	private static final String CLASS_NAME = NetworkExecutor.class.getSimpleName();
	final ExecutorService executor;
	final BlockingQueue<Runnable>queue ;
	//private static CyclicBarrier barrier ;
	
	
	private static final NetworkExecutor instance = new NetworkExecutor();
	public static NetworkExecutor getInstance() {
		//barrier = new CyclicBarrier(100*2+1) ;
		return instance;
		
	}
	public void setCommonParameters(Object appProperties) {
		this.commonParameters = appProperties;
	}
	public Object getCommonParameters() {
		return commonParameters;
	}
	
	private NetworkExecutor() {
		queue = new ArrayBlockingQueue<Runnable>(16);
		
		executor = new ThreadPoolExecutor(8, 16, 0L, TimeUnit.MILLISECONDS, queue);
		
	}
	
	public void shutdown() {
		executor.shutdown();
		tasks.clear();
	}
	
	public void execute(Cancellable task) {
		executor.execute(task);
	
	    
			
			tasks.add(task);
			
		
		
	}

	public void cancelTasksWithRequestId(Object requestId) {
		for (int i = 0; i < tasks.size(); i++) {
			Cancellable task = tasks.get(i);
			if (task.getRequestId() == requestId) {
				Log.v("ronj","Cancel task #[" + task  + "]");
				task.cancel();
				tasks.remove(i);
				i=i-1;
			}
		}
		
	}
	
	
	


}
