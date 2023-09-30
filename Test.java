import java.util.LinkedList;
import java.util.ListIterator;

public class Test {
    public static void main(String[] args) {
        LinkedList<Integer> lst = new LinkedList<>();
        lst.add(3);
        lst.add(6);
        
        ListIterator<Integer> it = lst.listIterator(2);
        while (it.hasNext()) {
            int n = it.next();
            System.out.println(it.previousIndex());
        }
    }
}
