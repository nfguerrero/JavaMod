package edu.iastate.cs228.hw2;

public class Stopwatch {
	private long elapsedTime = 0;
	private long mostRecentStartTime = -1;
	private boolean started = false;
	private boolean running = false;
	
	public void start() {
		if (running) {
			return;
		}
		elapsedTime = 0;
		mostRecentStartTime = System.nanoTime();
		running = true;
		started = true;
	}
	
	public void stop() {
		if (!running) {
			return;
		}
		elapsedTime += System.nanoTime() - mostRecentStartTime;
		running = false;
	}
	
	public void resume() {
		if (!started) {
			throw new IllegalStateException();
		}
		if (running) {
			return;
		}
		mostRecentStartTime = System.nanoTime();
		running = true;
	}
	
	public long getElapsedTime() {
		if (!started) {
			throw new IllegalStateException();
		}
		if (running) {
			return System.nanoTime() - mostRecentStartTime + elapsedTime;
		}
		return elapsedTime;
	}
}
