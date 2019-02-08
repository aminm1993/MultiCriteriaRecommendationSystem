package ir.mohammadpour.app.ui.util;

import java.util.ArrayList;
import java.util.List;

import ir.mohammadpour.app.model.SubService;
import ir.mohammadpour.app.ui.adapter.CategoryAdapter;

/**
 * Created by Amin on 2017-08-26.
 */
public class Consts {
    public static String SERVER_URL="http://89.39.208.240";
    public static  Boolean [] spinnerStates;
    public static String [] spinnerIds;
    public static String [] spinnerDescriptions;
    public static String [] spinnerAddresses;

    public static List<SubService> search;
    public static List<SubService> temp;
    public static CategoryAdapter adapter=null;
    public static int filterSpinnerId=0;
    public static int index=-1;
    public static String LastKalaArrayFiltered="main";
    public static int last=0;

    public static List<SubService> arr2=new ArrayList<>();
    public static List<Integer> arr_count=new ArrayList<>();
}
