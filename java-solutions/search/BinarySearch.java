package search;

public class BinarySearch {
    // Pred: i <= j => a[i] >= a[j]
    // Post: R : a[R] <= x, R - минимален
    public static int iterativeBinarySearch(int x, int[] a) {
        // Pred: (i <= j) => (a[i] >= a[j])
        int l = -1;
        // Post: l = -1 && (i <= j) => (a[i] >= a[j])

        // Pred: l = -1 && (i <= j) => (a[i] >= a[j])
        int r = a.length;
        // Post: r = a.length && l = -1 && (i <= j) => (a[i] >= a[j])

        // Inv: a[l] > x && a[r] <= x && (i <= j) => (a[i] >= a[j])
        // Цикл закончится, так как длина отрезка на каждом шаге уменьшается примерно в 2 раза.
        while (r > l + 1) {
            // a[l] > x && a[r] <= x && (i <= j) => (a[i] >= a[j]) && r > l + 1
            int m = (l + r) / 2;
            // Post: m = (l + r) / 2 && a[l] > x && a[r] <= x && (i <= j) => (a[i] >= a[j]) && r > l + 1

            if (a[m] > x) {
                // Pred: a[m] > x && m = (l + r) / 2 && a[l] > x && a[r] <= x && (i <= j) => (a[i] >= a[j]) && r > l + 1
                l = m;
                // Post: l = m && a[l] > x && (i <= j) => (a[i] >= a[j]) && a[r] <= x
            } else {
                // Pred: a[m] <= x && && m = (l + r) / 2 && a[l] > x && a[r] <= x && (i <= j) => (a[i] >= a[j]) && r > l + 1
                r = m;
                // Post: r = m && a[r] <= x && (i <= j) => (a[i] >= a[j]) && a[l] > x
            }
            // Post: a[r] <= x && a[l] > x && (i <= j) => (a[i] >= a[j]) && r - l = (r'-l')/2
        }
        // Post: a[l] > x && a[r] <= x && r = l + 1 &&  r - минимальный, потому что a[r-1] > x
        return r;
    }



    // Pred: (i <= j) => (a[i] >= a[j])
    // Post: R : a[R] <= x, R - минимален
    public static int recursiveBinarySearch(int x, int[] a) {
        // Pred: (i <= j) => (a[i] >= a[j])
        int l = -1, r = a.length;
        // Post: a[l] > x && a[r] <= x && (i <= j) => (a[i] >= a[j])
        return recursiveBinarySearch(x, a, l, r);
    }

    // Pred: a[l] > x && a[r] <= x && (i <= j) => (a[i] >= a[j])
    // Выйдем из рекурсии, так как длина отрезка каждый раз уменьшается примерно в 2 раза.
    // Post: R : a[R] <= x, R - минимален
    public static int recursiveBinarySearch(int x, int[] a, int l, int r) {
        // a[l] > x && a[r] <= x && (i <= j) => (a[i] >= a[j])
        if (r > l + 1) {
            // Pred: a[l] > x && a[r] <= x && (i <= j) => (a[i] >= a[j]) && r > l + 1
            int m = (l + r) / 2;
            // Post: m = (l + r) / 2 && a[l] > x && a[r] <= x && (i <= j) => (a[i] >= a[j]) && r > l + 1
            if (a[m] > x) {
                // Pred: a[m] > x && m = (l + r) / 2 && a[r] <= x && (i <= j) => (a[i] >= a[j]) && r > l + 1
                return recursiveBinarySearch(x, a, m, r);
                // Post: R : a[R] <= x, R - минимален
            } else {
                // Pred: a[m] <= x && m = (l + r) / 2 && a[l] > x && (i <= j) => (a[i] >= a[j]) && r > l + 1
                return recursiveBinarySearch(x, a, l, m);
                // Post: R : a[R] <= x, R - минимален
            }
        } else {
            // a[l] > x && a[r] <= x && (i <= j) => (a[i] >= a[j]) && r = l + 1
        }
        // a[r] <= x, r - минимальный
        return r;
    }

    public static void main(String[] args) {

        int element = Integer.parseInt(args[0]);
        int[] numbers = new int[args.length - 1];
        for (int i = 1; i < args.length; i++) {
            numbers[i - 1] = Integer.parseInt(args[i]);
        }

        int resultOfIterative = iterativeBinarySearch(element, numbers);
        System.out.println(resultOfIterative);
//        int resultOfRecursive = recursiveBinarySearch(element, numbers);
//        System.out.println(resultOfRecursive);


    }
}