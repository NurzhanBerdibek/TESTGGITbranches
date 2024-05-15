import java.util.Arrays;

interface MyCollection {
    void add(int element);
    void add(int index, int element) throws Exception;
    void clear();
    boolean contains(int o);
    int get(int index);
    int indexOf(int o);
    void insertElementAt(int element, int index);
    boolean isEmpty();
    void removeAt(int index);
    boolean remove(int element);
    void removeAll(int element);
    void reverse();
    void set(int index, int element);
    int size();
    void sort();
    int[] toArray();
    String toString();
}

class MyVector implements MyCollection {
    private int[] array;
    private int size;

    public MyVector() {
        array = new int[10];
        size = 0;
    }

    public MyVector(int[] a) {
        array = Arrays.copyOf(a, a.length * 2);
        size = a.length;
    }

    public void add(int element) {
        if (size == array.length) {
            array = Arrays.copyOf(array, array.length * 2);
        }
        array[size++] = element;
    }

    public void add(int index, int element) throws Exception {
        if (index > size) {
            throw new Exception("Index out of bounds");
        }
        if (size == array.length) {
            array = Arrays.copyOf(array, array.length * 2);
        }
        for (int i = size; i > index; i--) {
            array[i] = array[i - 1];
        }
        array[index] = element;
        size++;
    }

    public void clear() {
        size = 0;
    }

    public boolean contains(int o) {
        for (int i = 0; i < size; i++) {
            if (array[i] == o) {
                return true;
            }
        }
        return false;
    }

    public int get(int index) {
        return array[index];
    }

    public int indexOf(int o) {
        for (int i = 0; i < size; i++) {
            if (array[i] == o) {
                return i;
            }
        }
        return -1;
    }

    public void insertElementAt(int element, int index) {
        if (size == array.length) {
            array = Arrays.copyOf(array, array.length * 2);
        }
        for (int i = size; i > index; i--) {
            array[i] = array[i - 1];
        }
        array[index] = element;
        size++;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void removeAt(int index) {
        for (int i = index; i < size - 1; i++) {
            array[i] = array[i + 1];
        }
        size--;
    }

    public boolean remove(int element) {
        int index = indexOf(element);
        if (index != -1) {
            removeAt(index);
            return true;
        }
        return false;
    }

    public void removeAll(int element) {
        int count = 0;
        for (int i = 0; i < size; i++) {
            if (array[i] == element) {
                count++;
            }
        }
        int[] newArray = new int[size - count];
        int newIndex = 0;
        for (int i = 0; i < size; i++) {
            if (array[i] != element) {
                newArray[newIndex++] = array[i];
            }
        }
        array = newArray;
        size -= count;
    }

    public void reverse() {
        for (int i = 0; i < size / 2; i++) {
            int temp = array[i];
            array[i] = array[size - i - 1];
            array[size - i - 1] = temp;
        }
    }

    public void set(int index, int element) {
        array[index] = element;
    }

    public int size() {
        return size;
    }

    public void sort() {
        for (int i = 0; i < size - 1; i++) {
            for (int j = 0; j < size - i - 1; j++) {
                if (array[j] > array[j + 1]) {
                    int temp = array[j];
                    array[j] = array[j + 1];
                    array[j + 1] = temp;
                }
            }
        }
    }

    public int[] toArray() {
        return Arrays.copyOf(array, size);
    }

    public String toString() {
        StringBuilder result = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            result.append(array[i]);
            if (i < size - 1) {
                result.append(", ");
            }
        }
        result.append("]");
        return result.toString();
    }
}

public class MyVectorTest {
    public static void main(String[] args) {
        MyVector vector = new MyVector();
        vector.add(5);
        vector.add(10);
        vector.add(2);

        System.out.println("Original vector: " + vector);

        try {
            vector.add(1, 7);
            System.out.println("After adding at index 1: " + vector);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        vector.remove(10);
        System.out.println("After removing 10: " + vector);

        vector.reverse();
        System.out.println("After reversing: " + vector);

        vector.sort();
        System.out.println("After sorting: " + vector);


    }
}
