package comp1110.ass2;

import java.util.Arrays;

public enum Tiles
{
    S0(new int[]{1,0,0,0,14,3}),
    S1(new int[]{2,0,0,0,1,12}),
    S2(new int[]{3,0,0,0,15,0}),
    S3(new int[]{4,0,0,0,0,15}),
    S4(new int[]{5,0,0,0,7,9}),
    S5(new int[]{6,0,0,0,5,6}),
    A0(new int[]{7,0,0,0,0,7}),
    A1(new int[]{8,0,0,0,0,5}),
    A2(new int[]{9,0,0,0,0,11}),
    A3(new int[]{10,0,0,0,11,0}),
    A4(new int[]{11,0,0,0,5,0}),
    A5(new int[]{12,0,0,0,7,0}),
    B0(new int[]{13,0,0,0,1,3}),
    B1(new int[]{14,0,0,0,1,2}),
    B2(new int[]{15,0,0,0,5,6});

    public final int[] shape;

    public void set_default()
    {
        this.shape[1] = 0;
        this.shape[2] = 0;
        this.shape[3] = 0;

        if (this.name() == "S0")
        {
            this.shape[4] = 14;
            this.shape[5] = 3;
        }
        else if (this.name() == "S1")
        {
            this.shape[4] = 1;
            this.shape[5] = 12;
        }
        else if (this.name() == "S2")
        {
            this.shape[4] = 15;
            this.shape[5] = 0;
        }
        else if (this.name() == "S3")
        {
            this.shape[4] = 0;
            this.shape[5] = 15;
        }
        else if (this.name() == "S4")
        {
            this.shape[4] = 7;
            this.shape[5] = 9;
        }
        else if (this.name() == "S5")
        {
            this.shape[4] = 5;
            this.shape[5] = 6;
        }
        else if (this.name() == "A0")
        {
            this.shape[4] = 0;
            this.shape[5] = 7;
        }
        else if (this.name() == "A1")
        {
            this.shape[4] = 0;
            this.shape[5] = 5;
        }
        else if (this.name() == "A2")
        {
            this.shape[4] = 0;
            this.shape[5] = 11;
        }
        else if (this.name() == "A3")
        {
            this.shape[4] = 11;
            this.shape[5] = 0;
        }
        else if (this.name() == "A4")
        {
            this.shape[4] = 5;
            this.shape[5] = 0;
        }
        else if (this.name() == "A5")
        {
            this.shape[4] = 7;
            this.shape[5] = 0;
        }
        else if (this.name() == "B0")
        {
            this.shape[4] = 1;
            this.shape[5] = 3;
        }
        else if (this.name() == "B1")
        {
            this.shape[4] = 1;
            this.shape[5] = 2;
        }
        else if (this.name() == "B2")
        {
            this.shape[4] = 5;
            this.shape[5] = 6;
        }


    }

    Tiles(int[] shape)
    {
        this.shape = shape;
    }

    public int reorient(int x)
    {
        x = x + 1;
        if (x==1)
            x=0;
        else if (x == 5)
            x = 1;
        else if (x == 7)
            x = 5;
        else if (x == 11)
            x = 7;
        else if (x == 15)
            x = 11;
        else if (x==16)
            x = 15;

        return x;
    }

    public int[] rotate90(char x)
    {
        int n = Integer.parseInt(Character.toString(x));
        for (int i = 0; i<n;i++)
        {
            this.shape[3] = (this.shape[3]+1) % 8;
            this.shape[4] = reorient(this.shape[4]);
            this.shape[5] = reorient(this.shape[5]);
        }
        return this.shape;
    }

    public boolean check_railway_connection(Tiles w, Tiles x)
    {
        Integer[] left = {4,6,10,7,12,13,14,15};
        Integer[] right = {2,6,8,9,11,12,14,15};
        Integer[] down = {3,5,9,10,11,12,13,15};
        Integer[] up = {1,5,7,8,11,13,14,15};
        if (w.shape[1] == x.shape[1])
        {
            if ((w.shape[2] - x.shape[2]) == 1) //if w is on immediate right of x
            {

                if (Arrays.asList(right).contains(x.shape[5]))
                {

                    if (Arrays.asList(left).contains(w.shape[5]))
                        return true;
                }
            }
            else if (x.shape[2] - w.shape[2] == 1) //if w is on the immediate left of x
            {
                if (Arrays.asList(left).contains(x.shape[5]))
                {
                    if (Arrays.asList(right).contains(w.shape[5]))
                        return true;
                }
            }
        }
        else
        {
            if ((w.shape[1] - x.shape[1]) == 1) //if w is immediately below x
            {

                if (Arrays.asList(down).contains(x.shape[5]))
                {

                    if (Arrays.asList(up).contains(w.shape[5]))
                        return true;
                }
            }
            else if (x.shape[1] - w.shape[1] == 1) //if w is immediately above x
            {
                if (Arrays.asList(up).contains(x.shape[5]))
                {
                    if (Arrays.asList(down).contains(w.shape[5]))
                        return true;
                }
            }
        }
        return false;
    }


    public boolean check_highway_connection(Tiles w, Tiles x)
    {
        Integer[] left = {4,6,10,7,12,13,14,15};
        Integer[] right = {2,6,8,9,11,12,14,15};
        Integer[] down = {3,5,9,10,11,12,13,15};
        Integer[] up = {1,5,7,8,11,13,14,15};
        if (w.shape[1] == x.shape[1])
        {
            if ((w.shape[2] - x.shape[2]) == 1) //if w is on immediate right of x
            {

                if (Arrays.asList(right).contains(x.shape[4]))
                {

                    if (Arrays.asList(left).contains(w.shape[4]))
                        return true;
                }
            }
            else if (x.shape[2] - w.shape[2] == 1) //if w is on the immediate left of x
            {
                if (Arrays.asList(left).contains(x.shape[4]))
                {
                    if (Arrays.asList(right).contains(w.shape[4]))
                        return true;
                }
            }
        }
        else
        {
            if ((w.shape[1] - x.shape[1]) == 1) //if w is immediately below x
            {

                if (Arrays.asList(down).contains(x.shape[4]))
                {

                    if (Arrays.asList(up).contains(w.shape[4]))
                        return true;
                }
            }
            else if (x.shape[1] - w.shape[1] == 1) //if w is immediately above x
            {
                if (Arrays.asList(up).contains(x.shape[4]))
                {
                    if (Arrays.asList(down).contains(w.shape[4]))
                        return true;
                }
            }
        }
        return false;
    }


    public boolean isConnected(Tiles x)
    {
        boolean result = false, result_railway=false, result_highway=false;
        if ((this.shape[4] * x.shape[4] == 0) && (this.shape[5] * x.shape[5] == 0))
            result = false;
        else if ((Math.abs(this.shape[1] - x.shape[1]) > 1) || (Math.abs(this.shape[2] - x.shape[2]) > 1))
            result = false;
        else if ((Math.abs(this.shape[1] - x.shape[1]) == 1) && (Math.abs(this.shape[2] - x.shape[2]) == 1))
            result = false;
        else
        {
            if ((this.shape[4] * x.shape[4] != 0))
                //highway
            {
                result_highway = check_highway_connection(this, x);
            }
            if ((this.shape[5] * x.shape[5]) != 0)
                //railway
            {
                result_railway = check_railway_connection(this, x);
            }
        }
        result = result_highway || result_railway;
        return result;
    }

    public int[] translate(String x)
    {
        this.shape[1] = ((int)(x.charAt(0))-65);
        this.shape[2] = Integer.parseInt(Character.toString(x.charAt(1)));

        return this.shape;

    }



}