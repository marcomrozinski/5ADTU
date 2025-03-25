package dk.dtu.compute.course02324.assignment3.lists.implementations;

import javax.validation.constraints.NotNull;
import java.util.Comparator;
import java.util.List;

public class BubbleSort {


    public static <T> void sort(@NotNull Comparator<? super T> comp, @NotNull List<T> list) {
        if (list == null || comp == null) {
            throw new IllegalArgumentException("The list need to have a valid");
        }
        boolean swapped;
        int j = list.size();
        do{
            swapped = false;
            for (int i = 0; i + 1 < j; i++) {
                if (comp.compare(list.get(i), list.get(i + 1)) > 0) {
                    T temp = list.get(i);
                    list.set(i, list.get(i + 1));
                    list.set(i + 1, temp);
                    swapped = true;
                }
            }
            j--;
        } while(swapped);
    }

}


