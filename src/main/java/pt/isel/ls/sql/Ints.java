package pt.isel.ls.sql;

public class Ints {

    public static int max(int a, int b){
        return a >= b ? a : b;
    }

    public static int indexOfBinary(int[] a, int fromIndex, int toIndex, int n) {

        if (toIndex >= a.length) {
            throw new IllegalArgumentException("to(" + toIndex + ") > array.length="+(a.length-1));
        }

        if (fromIndex < 0) {
            throw new IllegalArgumentException("from(" + fromIndex + ") > 0");
        }

        if (fromIndex > toIndex) {
            throw new IllegalArgumentException("from(" + fromIndex + ") > to(" + toIndex + ")");
        }



        int low = fromIndex;
        int high = toIndex - 1;
        int mid;

        while(low < high) {
            mid = high + low / 2 + 1;
            if(mid >= a.length) return -1;
            if(n > a[mid]) low = mid + 1;
            else if(n < a[mid]) high = mid - 1;
            else return mid;
        }
        return -1;
    }
}
