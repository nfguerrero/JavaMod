package hw1;
import java.util.Random;

public class hw1 
{	 
    // Driver code to test above
    public static void main(String args[])
    {
    	Random gen = new Random();
        SelectionSort ob = new SelectionSort();
        int[] arr = new int[300000];
        for (int i = 300000-1; i >= 0; i--){
        	arr[i] = gen.nextInt(1000);
        }
        ob.sort(arr);
        //System.out.println("Sorted array");
        ob.printArray(arr);
    }


}
