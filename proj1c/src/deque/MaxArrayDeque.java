package deque;
import java.util.Comparator;
public class MaxArrayDeque<T> extends ArrayDeque<T> {

    private Comparator<T> comp;

    public MaxArrayDeque(Comparator<T> c) {
        super();
        comp = c;

    }
    // return the max item in the array dec using instance variable comp
    public T max() {
        if (this.isEmpty()) {
            return null;
        }
        T maxitem = this.get(0);
        for (int i = 0; i < this.size(); i += 1) {
            int x = comp.compare(maxitem, this.get(i));
            if (x < 0) {
                maxitem = this.get(i);
            }
        }
        return maxitem;
    }
    // return the max item in the array dec using variable c
    public T max(Comparator<T> c) {
        if (this.isEmpty()) {
            return null;
        }
        T maxitem = this.get(0);
        for (int i = 0; i < this.size(); i += 1) {
            int x = c.compare(maxitem, this.get(i));
            if (x < 0) {
                maxitem = this.get(i);
            }
        }
        return maxitem;
    }
}

