package Tests;

public class Animal 
{
	String sound;
	
	public Animal(String noise)
	{
		this.sound = noise;
		move();
		System.out.println(getClass());
	}
	
	public String getSound()
	{
		return this.sound;
	}
	
	public void move()
	{
		System.out.println("The Animal moved.");
	}
}
