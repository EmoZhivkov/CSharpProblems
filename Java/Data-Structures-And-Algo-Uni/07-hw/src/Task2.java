import java.util.ArrayList;
import java.util.Scanner;

public class Task2 {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();


        MinHeap minHeap = new MinHeap(n/2 + 2);
        MaxHeap maxHeap = new MaxHeap(n/2 + 2);

        double currentMedian = 0;
        for (int i = 0; i < n; i++) {
            int current = scanner.nextInt();

            if (current > currentMedian) {
                minHeap.insert(current);
            } else {
                maxHeap.insert(current);
            }

            if (minHeap.size + 1 < maxHeap.size) {
                minHeap.insert(maxHeap.getMax());
                maxHeap.extractMax();
            } else if (maxHeap.size + 1 < minHeap.size) {
                maxHeap.insert(minHeap.extractMin());
            }

            if (minHeap.size > maxHeap.size) {
                currentMedian = minHeap.getMin();
            } else if (maxHeap.size > minHeap.size) {
                currentMedian = maxHeap.getMax();
            } else {
                currentMedian = ((double)minHeap.getMin() +maxHeap.getMax())/2;
            }
            System.out.printf("%.1f\n", currentMedian);
        }

    }

    static class MinHeap {
        int[] arr;
        int size;

        MinHeap(int n) {
            this.arr = new int[n + 1];
            this.arr[0] = Integer.MIN_VALUE;
            this.size = 1;
        }

        void print() {
            for (int i = 1; i < this.size; i++) {
                System.out.println(arr[i]);
            }
        }

        int getMin() {
            return this.arr[1];
        }

        int extractMin() {
            if (this.size == 0) {
                return -1;
            }

            int n = arr[1];

            swap(1, (this.size - 1));
            this.size--;
            topHeapify();


            return n;
        }

        void topHeapify() {
            int i = 1;
            int current = arr[i];

            if (2 * i > this.size - 1) {
                return;
            }
            int leftChild = arr[2 * i];

            if (2 * i + 1 > this.size - 1) {
                return;
            }
            int rightChild = arr[2 * i + 1];

            while ((current > leftChild || current > rightChild)) {
                if (leftChild > rightChild) {
                    swap(i, 2 * i + 1);
                    i = 2 * i + 1;

                    current = arr[i];
                    if (2 * i > this.size - 1) {
                        return;
                    }
                    leftChild = arr[2 * i];

                    if (2 * i + 1 > this.size - 1) {
                        return;
                    }
                    rightChild = arr[2 * i + 1];
                } else {
                    swap(i, 2 * i);
                    i = 2 * i;

                    current = arr[i];
                    if (2 * i > this.size - 1) {
                        return;
                    }
                    leftChild = arr[2 * i];

                    if (2 * i + 1 > this.size - 1) {
                        return;
                    }
                    rightChild = arr[2 * i + 1];
                }
            }
        }

        void insert(int value) {
            this.arr[this.size] = value;
            this.size++;
            botHeapify();
        }

        void botHeapify() {
            int i = this.size - 1;
            int current = this.arr[i];
            int parent = this.arr[i / 2];

            while (current < parent) {
                swap(i, i / 2);
                i /= 2;

                current = this.arr[i];
                parent = this.arr[i / 2];
            }
        }

        void swap(int firstIndex, int secondIndex) {
            int first = arr[firstIndex];

            arr[firstIndex] = arr[secondIndex];
            arr[secondIndex] = first;
        }
    }

    static class MaxHeap {
        int[] arr;
        int size;

        MaxHeap(int n) {
            this.arr = new int[n + 1];
            this.arr[0] = Integer.MAX_VALUE;
            this.size = 1;
        }

        void print() {
            for (int i = 1; i < this.size; i++) {
                System.out.println(arr[i]);
            }
        }

        int getMax() {
            return this.arr[1];
        }

        int extractMax() {
            if (this.size == 0) {
                return -1;
            }

            int n = arr[1];

            swap(1, (this.size - 1));
            this.size--;
            topHeapify();


            return n;
        }

        void topHeapify() {
            int i = 1;
            int current = arr[i];

            if (2 * i > this.size - 1) {
                return;
            }
            int leftChild = arr[2 * i];

            if (2 * i + 1 > this.size - 1) {
                return;
            }
            int rightChild = arr[2 * i + 1];

            while ((current < leftChild || current < rightChild)) {
                if (leftChild < rightChild) {
                    swap(i, 2 * i + 1);
                    i = 2 * i + 1;

                    current = arr[i];
                    if (2 * i > this.size - 1) {
                        return;
                    }
                    leftChild = arr[2 * i];

                    if (2 * i + 1 > this.size - 1) {
                        return;
                    }
                    rightChild = arr[2 * i + 1];
                } else {
                    swap(i, 2 * i);
                    i = 2 * i;

                    current = arr[i];
                    if (2 * i > this.size - 1) {
                        return;
                    }
                    leftChild = arr[2 * i];

                    if (2 * i + 1 > this.size - 1) {
                        return;
                    }
                    rightChild = arr[2 * i + 1];
                }
            }
        }

        void insert(int value) {
            this.arr[this.size] = value;
            this.size++;
            botHeapify();
        }

        void botHeapify() {
            int i = this.size - 1;
            int current = this.arr[i];
            int parent = this.arr[i / 2];

            while (current > parent) {
                swap(i, i / 2);
                i /= 2;

                current = this.arr[i];
                parent = this.arr[i / 2];
            }
        }

        void swap(int firstIndex, int secondIndex) {
            int first = arr[firstIndex];

            arr[firstIndex] = arr[secondIndex];
            arr[secondIndex] = first;
        }
    }
}
