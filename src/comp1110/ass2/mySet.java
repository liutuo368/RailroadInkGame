package comp1110.ass2;

import java.util.HashMap;
import java.util.HashSet;


/**
 * Author : Jihirhsu Narayan
 * Description : This class was created for additional feature wherein the count of moves beginning with each tile was maintained
 */

public class mySet extends HashSet<String>
{
    public HashMap<String, Integer> count = new HashMap<>();

    @Override
    public boolean add(String element)
    {
        String substring = element.substring(0,2);

//        if (this.count.get(substring) > 200)
//            return false;

        if (this.count.containsKey(substring))
            this.count.put(substring, this.count.get(substring)+1);
        else
            this.count.put(substring, 1);
        return super.add(element);
    }


}
