package tps03_J_CSFas;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;

final class PoolService {
	
	private final ExecutorService pool = Executors.newFixedThreadPool(10);
	public void doSomething() {
		Future<?> future = pool.submit(new Task());
		// ...
		try { 
			future.get();
		} catch (InterruptedException e) { 
			Thread.currentThread().interrupt(); // Reset interrupted status
		} catch (ExecutionException e) { 
			Throwable exception = e.getCause(); // Forward to exception reporter
		} 
	}
	
	public static class ExceptionThreadFactory implements ThreadFactory { 
		private static final ThreadFactory defaultFactory = Executors.defaultThreadFactory();
		private final Thread.UncaughtExceptionHandler handler;

		public ExceptionThreadFactory( Thread.UncaughtExceptionHandler handler) {
			this.handler = handler;
		}

		@Override 
		public Thread newThread(Runnable run) { 
			Thread thread = defaultFactory.newThread(run); 
			thread.setUncaughtExceptionHandler(handler); 
			return thread;
		} 
	}
	
	public static class MyExceptionHandler extends Exception implements Thread.UncaughtExceptionHandler {
		// ...
		@Override public void uncaughtException(Thread thread, Throwable t) { 
			// Recovery or logging code
		} 
	}
	
	public static void main(String[] args) throws InterruptedException, IOException {
		PoolService service = new PoolService();
		service.doSomething();
	}
}
