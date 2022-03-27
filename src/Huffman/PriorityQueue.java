package Huffman;

import java.util.ArrayList;
import java.util.Arrays;

public class PriorityQueue<T extends Comparable<T>> {

    private int size = 0;
    private final java.util.List<T> minHeap;

    public PriorityQueue(){
        minHeap = new ArrayList<>();
    }

    public PriorityQueue(T[] data){
        size = data.length;
        minHeap = new ArrayList<>(data.length);
        minHeap.addAll(Arrays.asList(data).subList(0, size));
        heapify();
    }

    private void heapify(){
        int idx = size / 2 - 1;
        for(; idx >= 0; idx--)
            sink(idx);
    }

    public int size(){
        return size;
    }

    private void sink(int idx){
        int l = 2 * idx + 1;
        int r = 2 * idx + 2;
        int smallest = l;

        if(r < size && minHeap.get(r).compareTo(minHeap.get(l)) < 0) smallest = r;
        if(l >= size || minHeap.get(smallest).compareTo(minHeap.get(idx)) >= 0) return;

        T temp = minHeap.get(idx);
        minHeap.set(idx, minHeap.get(smallest));
        minHeap.set(smallest, temp);
        sink(smallest);
    }

    private void swim(int idx){
        int parent = (idx - 1) / 2;

        if(parent >= 0 && minHeap.get(parent).compareTo(minHeap.get(idx)) > 0){
            T temp = minHeap.get(parent);
            minHeap.set(parent, minHeap.get(idx));
            minHeap.set(idx, temp);
            swim(parent);
        }
    }

    public void add(T data){
        if(data == null) throw new IllegalArgumentException();
        minHeap.add(data);
        swim(size);
        //sink(size);
        size++;
    }

    public T poll(){
        if(size == 0) return null;
        T data = minHeap.get(0);
        size--;
        minHeap.set(0, minHeap.get(size));
        minHeap.remove(size);

        sink(0);
        return data;
    }

}
