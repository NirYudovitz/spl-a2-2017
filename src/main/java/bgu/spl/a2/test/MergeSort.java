/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bgu.spl.a2.test;

import bgu.spl.a2.Task;
import bgu.spl.a2.WorkStealingThreadPool;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

public class MergeSort extends Task<int[]> {

    private final int[] array;

    public MergeSort(int[] array) {
        this.array = array;
    }

    public static void main(String[] args) throws InterruptedException {
        WorkStealingThreadPool pool = new WorkStealingThreadPool(4);
        int n = 1000000; //you may check on different number of elements if you like
        int[] array = new Random().ints(n).toArray();

        MergeSort task = new MergeSort(array);

        CountDownLatch l = new CountDownLatch(1);
        pool.start();
        pool.submit(task);
        task.getResult().whenResolved(() -> {
            //warning - a large print!! - you can remove this line if you wish
            System.out.println(Arrays.toString(task.getResult().get()));
            l.countDown();
        });

        l.await();
        pool.shutdown();
        String msg = isSorted(array)? "The array is sorted!" : "The arrat isn't sorted ):";
        System.out.println(msg);
    }

    private static boolean isSorted(int[] array) {
        for (int i = 0; i < array.length - 1; i++) {
            if (array[i] > array[i + 1])
                return false;
        }

        return true;
    }

    @Override
    protected void start() {
        if (array.length == 1) {
            complete(array);
        }
        else{
            List<Task<int[]>> tasks = new ArrayList<>();
            int middle = array.length / 2;
            Task<int[]> left = new MergeSort(Arrays.copyOfRange(array, 0, middle));
            Task<int[]> right = new MergeSort(Arrays.copyOfRange(array, middle, array.length));
            tasks.add(left);
            tasks.add(right);
            spawn(tasks.toArray(new Task<?>[tasks.size()]));

            whenResolved(tasks, () -> {
                //merge two sorted children and complete
                merge(left.getResult().get(), right.getResult().get());
                complete(array);
            });
        }
    }

    /**
     * Merge two sorted arrays to a single sorted array
     *
     * @param firstSortedArray First sorted array
     * @param secondSortedArray Second sorted array
     */
    private void merge(int[] firstSortedArray, int[] secondSortedArray) {
        int firstArrayIndx = 0;
        int secondArrayIndx = 0;
        int originalArrayIndx = 0;

        // Override original array with same values but sorted
        while (firstArrayIndx < firstSortedArray.length && secondArrayIndx < secondSortedArray.length) {
            if (firstSortedArray[firstArrayIndx] <= secondSortedArray[secondArrayIndx]) {
                array[originalArrayIndx] = firstSortedArray[firstArrayIndx];
                firstArrayIndx ++;
            } else {
                array[originalArrayIndx] = secondSortedArray[secondArrayIndx];
                secondArrayIndx ++;
            }
            originalArrayIndx ++;
        }

        // Copy rest of arr1
        System.arraycopy(firstSortedArray, firstArrayIndx, array, originalArrayIndx,
                        firstSortedArray.length - firstArrayIndx);

        // Copy rest of arr2
        System.arraycopy(secondSortedArray, secondArrayIndx, array, originalArrayIndx,
                secondSortedArray.length - secondArrayIndx);
    }
}