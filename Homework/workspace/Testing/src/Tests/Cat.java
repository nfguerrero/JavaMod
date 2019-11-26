package Tests;

public class Cat extends Animal
{
	public Cat()
	{
		super("Meow");
	}
	
	@Override
	public void move()
	{
		System.out.println("The Cat moved");
	}
}
