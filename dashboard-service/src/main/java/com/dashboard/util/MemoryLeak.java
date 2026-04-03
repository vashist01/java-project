package com.dashboard.util;

import java.util.Arrays;
import java.util.stream.IntStream;

public class MemoryLeak {
    public static void main(String[] args){
        int arr[]  =  {1,3,4,5};
        int n  = 5;

        int output = IntStream.rangeClosed(1,n).sum() - Arrays.stream(arr).sum();
        System.out.println(output);
    }
}
