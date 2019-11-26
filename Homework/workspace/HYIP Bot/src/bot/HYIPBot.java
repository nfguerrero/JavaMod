package bot;

import java.util.ArrayList;
import java.util.Scanner;

public class HYIPBot
{
	public static void main(String[] args)
	{
		double bal = 0.01768606; double rate = 0.07; double time = 20;
		//System.out.println(quick(bal, rate, time));
		//System.out.println(profit(bal, rate, time));
		interact(bal, rate, time);
	}
	
	public static String quick(double bal, double rate, double time)
	{
		double endBal = 0;
		int days = (int) time;
		for (int i = 0; i < days; i++)
		{
			endBal+=(bal*rate);
		}
		endBal+=(rate*(time%1));
		String str1 = endBal+""; String str2 = (endBal-bal)+"";
		String str = str1.substring(0, 11)+"; "+str2.substring(0, 11);
		return str;
	}
	
	public static String profit(double bal, double rate, double time)
	{
		double newBal = bal; double endBal= 0;
		int days1 = (int) time; days1 = days1/2;
		for (int i = 0; i < days1; i++)
		{
			newBal+=(newBal*rate);
		}
		for (int i = 0 ; i < days1; i++)
		{
			endBal+=(newBal*rate);
		}
		String str1 = endBal+""; String str2 = (endBal-bal)+"";
		String str = str1.substring(0, 11)+"; "+str2.substring(0, 11);
		return str;
	}

	public static void interact(double bal, double rate, double time)
	{
		Scanner scan = new Scanner(System.in);
		double newBal = bal; double endBal = 0; int day = 1;
		ArrayList<Double> list = new ArrayList<Double>();
		list.add(new Double(19));list.add(bal); 
		for (int i = 1; i < time+1; i++)
		{	
			System.out.println("Day "+day+":");
			double profit = newBal*rate;
			String str = (profit)+""; str = str.substring(0, 11);
			System.out.println("-Active: "+newBal);
			if ((endBal+"").length()>12){System.out.println("Withdrawn: "+(endBal+"").substring(0, 11));}
			System.out.println("-Profit: "+str);
			System.out.print("-Reinvest: ");
			String ans = scan.next();
			if (ans.equals("y")){
				newBal+=profit;i=1;
				list.add(new Double(20));list.add(profit); }
			else{
				endBal+=profit;}
			if (list.size()>0 && list.get(0)==0){
				newBal-=list.get(1);
				list.remove(1);list.remove(0);}
			for (int j = 0; j < list.size(); j+=2){
				list.set(j, list.get(j)-1);}
			day++;
		}
		scan.close();
		String str1 = endBal+""; String str2 = (endBal-bal)+"";
		String str = str1.substring(0, 11)+"; "+str2.substring(0, 11);
		System.out.println(str);
	}
}
