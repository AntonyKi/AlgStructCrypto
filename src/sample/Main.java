package sample;

import sample.LongNum;

import java.io.InputStream;
import java.util.ArrayList;

public class Main {
    public static void main(String args[]){
        LongNum a = new LongNum();
        LongNum b = new LongNum();
        LongNum z = new LongNum();
        a.fromString("3");
        b.fromString("2");
        z.fromString("105");
       /* LongNum c = a.multiply(b);
        LongNum d = a.addition(b);*/
        //LongNum e = a.subst(b);
        /*LongNum k = a.divide(b);
        System.out.println(k.getString());
        System.out.println(a.sqrt().getString());
        System.out.println(c.getString());
        System.out.println(d.getString());*/
        //System.out.println(e.getString());
        /*System.out.println((b.compareWith(a)));
        System.out.println(a.module(b).getString());
        System.out.println((a.pow(b)).getString());
        System.out.println((a.powMod(b, z)).getString());*/
        Algo algo = new Algo();
        LongNum aaa = new LongNum();
        LongNum bbb = new LongNum();
        aaa.fromString("1001");
        bbb.fromString("9907");
        Integer f = algo.calcJacobieSymbol(aaa, bbb);
        System.out.println(f.toString());
        System.out.println(algo.fEuler(a).getString());
        f= algo.fMebeus(z);
        System.out.println(f.toString());
        a.fromString("5");
        b.fromString("7");
        z.fromString("9");
        LongNum res = algo.discreteLogBSGS(a, b, z);
        if(res!=null)
        System.out.println(res.getString());
        /*for(Integer i = 2; i<100;i++){
            LongNum cur = new LongNum();
            cur.fromString(i.toString());
            System.out.println(i.toString() + " " + algo.testMillerRabin(cur));
        }*/
        //aaa = aaa.pow(a);

        /*ArrayList<LongNum> finale = algo.rhoPollard(aaa);
        for(LongNum elem : finale){
            System.out.print(elem.getString()+ " ");
        }*/
        //System.out.println(algo.testMillerRabin(aaa));
    }
}
