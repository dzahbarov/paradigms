package search;

public class BinarySearchMin {

    // Существует K, такой что для всех a[i], где i из [0:K) a[i-1] < a[i] и для всех a[i], где i из [K:len(a)-1] a[i-1] < a[i]
    // Все a[i], где i из [K:len(a)-1] < любой a[j], где j из [0:K)
    // Post: R = a[K] && R - минимальный элемент
    public static int iterativeBinarySearchMin(int[] a) {

        int l = -1;
        // Post: l = -1

        int r = a.length;
        // Post: r = a.length && l = -1

        // Inv: a[l] > a[len(a)-1] && a[r] <= a[len(a)-1]
        // Цикл закончится, так как длина отрезка на каждом шаге уменьшается примерно в 2 раза.
        while (r > l + 1) {
            // a[l] > a[len(a)-1] && a[r] <= a[len(a)-1]
            int m = (l + r) / 2;
            // Post: m = (l + r) / 2 && r > l + 1 && a[l] > a[len(a)-1] && a[r] <= a[len(a)-1]

            if (a[m] > a[a.length - 1]) {
                // Pred: m = (l + r) / 2 && a[m] > a[a.length - 1]
                l = m;
                // Post: l = m && a[l] > a[len(a)-1] && a[r] <= a[len(a)-1]
            } else {
                // Pred: m = (l + r) / 2 && a[m] <= a[a.length - 1] && r > l + 1
                r = m;
                // Post: r = m && a[r] <= a[len(a)-1] && a[r] <= a[len(a)-1]
            }
            // Post: a[l] > a[len(a)-1] && a[r] <= a[len(a)-1] && r - l = (r'-l')/2
        }
        // Post: a[l] > a[len(a)-1] && a[r] <= a[len(a)-1] && r = l + 1 && a[r] - минимальный элемент
        return a[r];
    }


    // Существует K, такой что для всех a[i], где из [0:K) a[i-1] < a[i] и для всех a[i], где i из [K:len(a)-1] a[i-1] < a[i]
    // Все a[i], где i из [K:len(a)-1] < любой a[j], где j из [0:K)
    // Post: R = a[K] && R - минимальный элемент
    public static int recursiveBinarySearchMin(int[] a) {
        // Pred: True
        int l = -1, r = a.length;
        // Post: l = -1, r = a.length
        return recursiveBinarySearchMin(a, l, r);
    }

    // Существует K, такой что для всех a[i], где i из [0:K) a[i-1] < a[i] и для всех a[i], где i из [K:len(a)-1] a[i-1] < a[i]
    // Все a[i], где i из [K:len(a)-1] < любой a[j], где j из [0:K)
    // a[l] > a[len(a)-1] && a[r] <= a[len(a)-1]
    // Post: R = a[K] && R - минимальный элемент
    public static int recursiveBinarySearchMin(int[] a, int l, int r) {

        if (r > l + 1) {
            // Pred: a[l] > a[len(a)-1] && a[r] <= a[len(a)-1] && r > l + 1
            int m = (l + r) / 2;
            // Post: m = (l + r) / 2 && a[l] > a[len(a)-1] && a[r] <= a[len(a)-1] && r > l + 1
            if (a[m] > a[a.length-1]) {
                // Pred: a[m] > a[len(a)-1] && m = (l + r) / 2 && a[r] <= a[len(a)-1] && r > l + 1
                return recursiveBinarySearchMin(a, m, r);
                // Post: R = a[K] && R - минимальный элемент
            } else {
                // Pred: a[m] <= x && m = (l + r) / 2 && a[l] > x && (i <= j) => (a[i] >= a[j]) && r > l + 1
                return recursiveBinarySearchMin(a, l, m);
                // Post: R = a[K] && R - минимальный элемент
            }
        } else {
            // a[l] > a[len(a)-1] && a[r] <= a[len(a)-1] && r = l + 1
        }
        // Post: R = a[K] && R - минимальный элемент
        return a[r];
    }

    public static void main(String[] args) {

        int[] numbers = new int[args.length];
        for (int i = 0; i < args.length; i++) {
            numbers[i] = Integer.parseInt(args[i]);
        }

//        int resultOfIterative = iterativeBinarySearchMin(numbers);
//        System.out.println(resultOfIterative);
        int resultOfRecursive = recursiveBinarySearchMin(numbers);
        System.out.println(resultOfRecursive);


    }
}