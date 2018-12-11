package sample;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static java.util.Collections.sort;

public class Algo {

    public LongNum getNext(LongNum prev, LongNum n) {
        LongNum cur = (prev.pow(two).subst(one)).module(n);
        //System.out.println(cur.getString());
        return cur;
    }

    ArrayList<LongNum> multlist;
    ArrayList<String> stringMultlist;
    LongNum one = new LongNum();
    LongNum two = new LongNum();
    LongNum zero = new LongNum();
    LongNum three = new LongNum();
    LongNum four = new LongNum();
    LongNum five = new LongNum();
    LongNum seven = new LongNum();
    LongNum eight = new LongNum();

    public Algo() {
        one.add(1);
        two.add(2);
        zero.add(0);
        three.add(3);
        four.add(4);
        five.add(5);
        seven.add(7);
        eight.add(8);
    }

    public ArrayList<LongNum> rhoPollard(LongNum n) {
        ArrayList<LongNum> res = new ArrayList<>();
        for (Integer i = 2; i <= 100; ++i) {
            LongNum temp = new LongNum();
            temp.fromString(i.toString());
            while (n.module(temp).compareWith(zero) == 0) {
                n = n.divide(temp);
                res.add(temp);
            }
        }
        if (n.compareWith(one) == 0) {
            return res;
        }
        ArrayList<LongNum> a2 = rhoPollardRec(n);
        res.addAll(a2);
        return res;
    }

    public ArrayList<LongNum> rhoPollardRec(LongNum n) {
        LongNum y = two.copy();
        int i = 0;
        LongNum first = two.copy();
        LongNum second = two.copy();
        while ((n.GCD(y).compareWith(one) == 0 || n.GCD(y).compareWith(n) == 0) && i < 1000) {
            first = getNext(first, n);
            second = getNext(getNext(second, n), n);
            y = first.subst(second);
            i++;

        }
        y = y.GCD(n);
        ArrayList<LongNum> a = new ArrayList<>();
        if (i == 1000) {
            a.add(n);
            return a;
        }
        ArrayList<LongNum> a1 = rhoPollardRec(n.divide(y));
        ArrayList<LongNum> a2 = rhoPollardRec(y);
        a.addAll(a1);
        a.addAll(a2);
        return a;
    }

    public boolean testMillerRabin(LongNum n) {
        int k = 10;
        if (n.module(two).compareWith(zero) == 0) {
            if (n.compareWith(two) == 0) return true;
            else return false;
        }
        LongNum m = LongNum.div2(n.subst(one));
        int t = 1;
        while (m.module(two).compareWith(zero) == 0) {
            m = (LongNum.div2(m));
            t++;
        }
        for (int i = 1; i < k + 1; i++) {
            LongNum a = LongNum.random(n.size()).module(n.subst(one)).addition(one);
            LongNum u = a.powMod(m, n);
            if (u.compareWith(one) != 0) {
                int j = 1;
                while (u.module(n).compareWith(n.subst(one)) != 0 && j < t) {
                    u = u.powMod(two, n);
                    j++;
                }
                if (u.module(n).compareWith(n.subst(one)) != 0) {
                    return false;
                }
            }
        }
        return true;
    }

    public int calcJacobieSymbol(LongNum a , LongNum n){

        if(a.compareWith(zero)==0){
            return 0;
        }
        if(a.compareWith(one)==0){
            return 1;
        }
        LongNum a1 = a.copy();
        int e = 0;
        while(a1.module(two).compareWith(zero)==0){
            e++;
            a1 = LongNum.div2(a1);
        }
        int s = 1;
        if(e%2==0){
            s = 1;
        }else if(n.module(eight).compareWith(one) ==0||
                n.module(eight).compareWith(seven)==0){
            s = 1;
        }else if(n.module(eight).compareWith(three) ==0||
                n.module(eight).compareWith(five)==0){
            s = -1;
        }
        if(n.module(four).compareWith(three) ==0&&
                a1.module(four).compareWith(three)==0){
            s = s * (-1);
        }
        LongNum n1 = n.module(a1);
        if(a1.compareWith(one)==0)return s;
        else return s*calcJacobieSymbol(n1,a1);
    }
    public int calcLejandreSymbol( LongNum a, LongNum n){
        if(testMillerRabin(n) == false) return 2;
        return calcJacobieSymbol(a , n);
    }
    public LongNum fEuler(LongNum n){
        LongNum ret = one.copy();
        for(LongNum i = two.copy();(i.multiply(i)).compareWith(n)<1;i =i.addition(one)){
            LongNum p = one.copy();
            while(n.module(i).compareWith(zero)==0){
                p = p.multiply(i);
                n = n.divide(i);
            }
            if(( p = p.divide(i)).compareWith(one)>-1)ret =  ret.multiply(p.multiply(i.subst(one)));
        }
        n = n.subst(one);
        if(n.compareWith(zero)!=0) return n.multiply(ret);
        else return ret;
    }
    public int fMebeus(LongNum n){
        ArrayList<LongNum> arrayList = rhoPollard(n);
        ArrayList<String> stringList = new ArrayList<>();
        for(LongNum num : arrayList){
            stringList.add(num.getString());
        }
        stringList.sort(String::compareToIgnoreCase);
        String strprev = "abs";
        for(String str : stringList){
            if(str.equals(strprev))return 0;
        }
        if(stringList.size()%2 == 0)return 1;
        else return -1;

    }

    public LongNum discreteLogBSGS(LongNum a , LongNum b, LongNum m){
        LongNum n =  m.sqrt().addition(one);
        HashMap<String , LongNum> vals = new HashMap<>();
        for(LongNum i = n.copy();i.compareWith(one)==1;i = i.subst(one)){
            vals.put(a.powMod(i.multiply(n), m).getString(), i);
        }
        for(LongNum i = zero.copy();i.compareWith(n)!=1;i = i.addition(one)){
            LongNum cur = (a.powMod(i.multiply(n),m).multiply(b)).module(m);
            String strcur = cur.getString();
            if(vals.containsKey(strcur)){
                LongNum ans = (vals.get(strcur).multiply(n)).subst(i);
                if(ans.compareWith(m)==-1){
                    return  ans;
                }
            }
        }
        return null;
    }

    public LongNum Chipolla(LongNum a, LongNum p) {

        LongNum w;
        LongNum b;
        while (true) {
            b = LongNum.random(a.size()).addition(one).module(a);
            w = b.multiply(b).subst(a).module(p);
            if (w.equals(zero))
                return b;
            if (calcLejandreSymbol(w, p) != 1)
                break;
        }
        return SuperMultiply(b, w, p.addition(one).divide(two), p);

    }
    private LongNum SuperMultiply(LongNum a, LongNum b, LongNum step, LongNum p) {
        LongNum res = zero.copy();
        LongNum t1;
        LongNum t2;
        LongNum t3;
        for (LongNum i = zero.copy(); i.compareWith(step) != 1; i = i.addition(one)) {
            if (i.module(two).equals(zero)) {
                t1 = C(i, step);
                t2 = t1.multiply(a.powMod(step.subst(one), p)).module(p);
                t3 = t2.multiply(b.powMod(i.divide(two), p)).module(p);
                res = res.addition(t3).module(p);
            }
        }
        return res;
    }
    private LongNum C(LongNum k, LongNum n) {
        LongNum res = one.copy();
        if (k.compareWith(n.subst(k)) == -1)
            k = n.subst(k);
        for (LongNum i = k.addition(one); i.compareWith(n) != 1; i = i.addition(one))
            res = res.multiply(i);
        for (LongNum i = one.copy(); i.compareWith(n.subst(k)) != 1; i = i.addition(one))
            res = res.divide(i);
        return (res);
    }

}
