import java.util.Iterator;
import java.util.NoSuchElementException;

class TwoDimArray<T> implements Iterable<T> {
    private T[][] array;

    public TwoDimArray(T[][] array) {
        this.array = array;
    }

    @Override
    public Iterator<T> iterator() {
        return new TwoDimArrayIterator();
    }

    private class TwoDimArrayIterator implements Iterator<T> {
        private int row = 0;
        private int col = 0;

        @Override
        public boolean hasNext() {
            return row < array.length && col < array[row].length;
        }

        @Override
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException("No more elements in the array");
            }

            T element = array[row][col];
            col++;

            if (col >= array[row].length) {
                col = 0;
                row++;
            }

            return element;
        }
    }
}
