
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class testeHashMap {
  public static void main(String args[]) {

    List<String> limelightName = Arrays.asList("limelight-shooter", "limelight-nova", "limelight-ruim");
    List<Double> ta = Arrays.asList(2.0, 6.0, 8.0);
    List<Boolean> tv = Arrays.asList(true, true, false);

// Sort By limelightName
    concurrentSort(limelightName, limelightName, ta, tv);
    System.out.println("Sort By Name:");
    System.out.println(limelightName);
    System.out.println(ta);
    System.out.println(tv);
    System.out.println();
// Sort By ta
    concurrentSort(ta, limelightName, ta, tv);
    System.out.println("Sort By TA:");
    System.out.println(limelightName);
    System.out.println(ta);
    System.out.println(tv);
    System.out.println();
// Sort By tv
    concurrentSort(tv, limelightName, ta, tv);
    System.out.println("Sort By TV:");
    System.out.println(limelightName);
    System.out.println(ta);
    System.out.println(tv);
    System.out.println();
  }

  public static <T extends Comparable<T>> void concurrentSort(
                                        final List<T> key, List<?>... lists){
    // Create a List of indices
    List<Integer> indices = new ArrayList<Integer>();
    for(int i = 0; i < key.size(); i++)
        indices.add(i);

    // Sort the indices list based on the key
    Collections.sort(indices, new Comparator<Integer>(){
        @Override public int compare(Integer i, Integer j) {
            return key.get(i).compareTo(key.get(j));
        }
    });

    // Create a mapping that allows sorting of the List by N swaps.
    // Only swaps can be used since we do not know the type of the lists
    Map<Integer,Integer> swapMap = new HashMap<Integer, Integer>(indices.size());
    List<Integer> swapFrom = new ArrayList<Integer>(indices.size()),
                  swapTo   = new ArrayList<Integer>(indices.size());
    for(int i = 0; i < key.size(); i++){
        int k = indices.get(i);
        while(i != k && swapMap.containsKey(k))
            k = swapMap.get(k);

        swapFrom.add(i);
        swapTo.add(k);
        swapMap.put(i, k);
    }

    // use the swap order to sort each list by swapping elements
    for(List<?> list : lists)
        for(int i = 0; i < list.size(); i++)
            Collections.swap(list, swapFrom.get(i), swapTo.get(i));
}
}