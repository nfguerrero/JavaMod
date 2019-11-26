package bot;

import java.util.Random;

public class Bot2 
{
	private boolean[] first;
	private int small;
	private int start;
	private int balance;
	
	public Bot2(int size, int s, int startBalance)
	{
		this.first = new boolean[size];
		this.small = s;
		this.start = startBalance;
		this.balance = startBalance;
	}
	
	public void run()
	{
		Random gen = new Random();
		while (this.balance > 0 && this.balance < 2*this.start)
		{
			while (true)
			{
				int one = gen.nextInt(100); this.first[0] = one >= 50; this.balance -= 100;
				if (one < small) {
					this.balance += 400; break;
				}
				int two = gen.nextInt(100); this.first[1] = two >= 50; this.balance -= 200;
				if (two < small) {
					this.balance += 800; break;
				}
				if (this.first[0] && this.first[1]) {
					runLT(gen);
				}
				else if (!this.first[0] && !this.first[1]) {
					runGT(gen);
				}
				else {
					runMix(gen);
				}
				break;
			}
		}
	}
	
	public void runGT(Random gen)
	{
		int bet = 400;
		while (true)
		{
			int next = gen.nextInt(100); this.balance -= bet;
			if (next >= 50) {
				this.balance += 2*bet;
				break;
			}
			else {
				bet += bet;
				if (bet > this.balance) {
					break;
				}
			}
		}
	}
	
	public void runLT(Random gen)
	{
		int bet = 400;
		while (true)
		{
			int next = gen.nextInt(100); this.balance -= bet;
			if (next < 50) {
				this.balance += 2*bet;
				break;
			}
			else {
				bet += bet;
				if (bet > this.balance) {
					break;
				}
			}
		}
	}
	
	public void runMix(Random gen)
	{
		int bet = 400;
		int counter;
		if (this.first[1]) {counter = 1;}
		else {counter = 2;}
		while (true)
		{
			int next = gen.nextInt(100); this.balance -= bet;
			if (counter%2 == 0) {
				if (next >= 50) {
					this.balance += 2*bet;
					break;
				}
				else {
					bet += bet;
					if (bet > this.balance) {
						break;
					}
				}
			}
			else {
				if (next < 50) {
					this.balance += 2*bet;
					break;
				}
				else {
					bet += bet;
					if (bet > this.balance) {
						break;
					}
				}
			}
			counter++;
		}
	}

	public int getBalance() {return this.balance;}
	
	public static void main(String[] args)
	{
		int counter = 0;
		for (int i = 0; i < 100000; i++)
		{
			Bot2 bot = new Bot2(2, 25, 1000000);
			bot.run();
			if (bot.getBalance() >= 200000) {
				counter++;
			}
			
			if (i%1000 == 0) {
				System.out.print(i/1000);
			}
		
		}
		System.out.println("\n" + counter/100000.0);
	}
}



