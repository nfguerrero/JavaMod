package hw1;

public class SelectionSort
{
	private long start;
	private long end;
	
    void sort(int arr[])
    {
    	start = System.currentTimeMillis();
    	int n = arr.length;
        for (int i=1; i<n; ++i)
        {
            int key = arr[i];
            int j = i-1;
 
            /* Move elements of arr[0..i-1], that are
               greater than key, to one position ahead
               of their current position */
            while (j>=0 && arr[j] > key)
            {
                arr[j+1] = arr[j];
                j = j-1;
            }
            arr[j+1] = key;
        }
        end = System.currentTimeMillis();
    }
 
    // Prints the array
    void printArray(int arr[])
    {
        System.out.println(end-start);
    }
 
    
}