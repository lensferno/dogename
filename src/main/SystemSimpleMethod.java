package main;

import java.util.*;

public class systemSimpleMethod {
    Map group;
    ArrayList<String> sysMethodList = new ArrayList<>();
    int groupAmount;
    int groupLength;
    int currentGroup;
    int currentNameNumber;
    List ignoredGroupName;
    List removedList;
    Random rand;

    void mixUpList() {
    }

    public void makeGroup(ArrayList nameList) {
        removedList.clear();
        ignoredGroupName.clear();
        sysMethodList.clear();
        sysMethodList.addAll(nameList);
        mixUpList();

        groupAmount = (int) Math.sqrt(sysMethodList.size());
        int removedAmount = sysMethodList.size() % groupAmount;
        int temp = 0;

        for (int i = 0; i < removedAmount; i++) {
            temp = rand.nextInt(sysMethodList.size());
            removedList.add(sysMethodList.get(temp));
            sysMethodList.remove(temp);
        }

        group = new HashMap<Integer, ArrayList>();
        groupLength = (int) (sysMethodList.size() / groupAmount);

        ArrayList tempList=null;
        for (int i = 0; i<groupAmount;i++){
            group.put(i, new ArrayList());
            for (int a = 0;a<groupLength;a++){
                tempList=(ArrayList) group.get(i);
                tempList.add(sysMethodList.get(groupLength * 2));

            }
        }
        currentNameNumber = 0;
        currentGroup = 0;
    }
}
