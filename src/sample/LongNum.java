package sample;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class LongNum {
    private List<Integer> value = new ArrayList<>();
    private static int base = 10;
    public static LongNum random(int  n){
        LongNum res = new LongNum();
        for(int i =0;i<n;i++){
            res.add(ThreadLocalRandom.current().nextInt(0, 10));
        }
        res.normalize();
        return res;
    }
    public boolean fromString(String num) {
        StringBuilder numm = new StringBuilder(num);
        numm.reverse();
        num = numm.toString();
        value = new ArrayList<>();
        for (char a : num.toCharArray()) {
            if (a < 48 || a > 57) return false;
            value.add(a - 48);
        }
        return true;
    }

    public LongNum copy() {
        LongNum res = new LongNum();
        for (int n : this.value) {
            res.add(n);
        }
        return res;
    }

    public String getString() {
        String res = "";
        for (int i = value.size() - 1; i >= 0; --i) {
            res += value.get(i);
        }
        return res;
    }

    public Integer get(int index) {
        try {
            return value.get(index);
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    public void normalize() {
        if (this.size() == 0) return;
        int i = this.size() - 1;
        while (this.get(i) == 0) {
            if (this.size() == 1) break;
            this.value.remove(i);
            i--;
        }
    }

    public void set(int index, int val) {
        value.set(index, val);
    }

    public void add(int val) {
        value.add(val);
    }

    public int size() {
        return this.value.size();
    }

    public int compareWith(LongNum b) {
        this.normalize();
        b.normalize();
        if (this.value.size() == b.size()) {
            for (int i = b.size() - 1; i >= 0; i--) {
                if (this.value.get(i) < b.get(i)) return -1;
                if (this.value.get(i) > b.get(i)) return 1;
            }
            return 0;
        } else if (this.value.size() < b.size()) return -1;
        else return 1;
    }

    public LongNum addition(LongNum b) {
        int newSize = Math.max(this.size(), b.size());
        int carry = 0;
        LongNum res = new LongNum();
        for (int i = 0; i < this.size(); i++) {
            res.add(this.get(i));
        }
        for (int i = 0; i <= newSize; i++) {
            if (b.get(i) == null) b.add(0);
            if (this.get(i) == null) this.add(0);
            if (res.get(i) == null) res.add(0);
            res.set(i, carry + this.get(i) + b.get(i));
            carry = Math.floorDiv(res.get(i), base);
            res.set(i, Math.floorMod(res.get(i), base));
        }
        this.normalize();
        res.normalize();
        b.normalize();
        return res;
    }

    public LongNum subst(LongNum b) {
        LongNum res;
        this.normalize();
        b.normalize();
        //System.out.println(this.getString() + "-" + b.getString());
        if (this.compareWith(b) == -1) {
            res = b.copy();
            b = this.copy();
        } else res = this.copy();
        int n = res.size() - b.size();
        for (int i = 0; i < n; i++) {
            b.add(0);
        }
        int newSize = res.size();
        //System.out.println(res.getString() + "-" + b.getString());
        for (int i = newSize - 1; i >= 0; i--) {
            int newN = res.get(i) - b.get(i);
            res.set(i, newN);
        }
        for (int i = 0; i < newSize; i++) {
            if (res.get(i) < 0) {
                res.set(i, res.get(i) + base);
                //System.out.println(res.get(i));
                res.set(i + 1, res.get(i + 1) - 1);
            }
        }
        res.normalize();
        return res;
    }

    public LongNum multiply(LongNum b) {
        LongNum res = new LongNum();
        for (int i = 0; i < this.size(); i++) {
            int add = 0;
            int j;
            for (j = 0; j < b.size(); ++j) {
                if (res.get(i + j) == null) res.add(0);
                add += res.get(i + j) + this.get(i) * b.get(j);
                res.set(i + j, add % base);
                add /= base;
            }
            while (add > 0) {
                if (res.get(i + j) == null) res.add(0);
                res.set(i + j, add % base);
                add /= base;
                j++;
            }
        }
        res.normalize();
        return res;
    }

    //for binary search 2 division
    public static LongNum div2(LongNum divided) {
        LongNum res = divided.copy();
        for (int i = res.size() - 1; i >= 0; i--) {
            if (i != 0) res.set(i - 1, res.get(i - 1) + (res.get(i) % 2) * base);
            res.set(i, Math.floorDiv(res.get(i), 2));
        }
        res.normalize();
        return res;
    }

    public LongNum sqrt() {
        this.normalize();
        LongNum r = new LongNum();
        LongNum l = new LongNum();
        int newSMax = Math.floorDiv(this.size(), 2) + Math.floorMod(this.size(), 2);
        for (int i = 0; i < newSMax; i++) {
            r.add(9);
        }
        for (int i = 0; i < newSMax - 1; i++) {
            l.add(0);
        }
        l.add(1);
        LongNum m;
        LongNum one = new LongNum();
        one.add(1);
        while (one.compareWith(r.subst(l)) != 0) {
            m = div2(l.addition(r));
            LongNum mm = m.multiply(m);
            if (mm.compareWith(this) == 0) return m;
            else if (mm.compareWith(this) == -1) l = m.copy();
            else r = m.copy();
        }
        l.normalize();
        return l;
    }
    @Override
    public boolean equals(Object obj){
        if (obj == null) {
            return false;
        }

        if (!LongNum.class.isAssignableFrom(obj.getClass())) {
            return false;
        }
        final LongNum b = (LongNum)obj;
        if(this.compareWith(b)==0)return true;
        else return false;
    }
    public LongNum divide(LongNum b) {

        ///binary search setup
        this.normalize();
        b.normalize();
        LongNum l = new LongNum();
        LongNum r = new LongNum();
        LongNum one = new LongNum();
        one.add(1);
        for (int i = 0; i <= (this.size() - b.size()); i++) {
            r.add(9);
        }
        for (int i = 0; i < (this.size() - b.size() - 2); i++) {
            l.add(0);
        }
        if ((this.size() - b.size()) - 1 < 0) l.add(0);
        else l.add(1);


        LongNum m;

        while (one.compareWith(r.subst(l)) < 0) {
            m = div2(l.addition(r));
            LongNum mm = m.multiply(b);
            if (mm.compareWith(this) == 0) return m;
            else if (mm.compareWith(this) == -1) l = m.copy();
            else r = m.copy();
        }

        if (r.multiply(b).compareWith(this) <= 0)
                return r;
        return l;
    }

    public LongNum pow(LongNum b) {
        LongNum res = this.copy();
        LongNum zero = new LongNum();
        zero.add(0);
        LongNum one = new LongNum();
        one.add(1);
        LongNum two = new LongNum();
        two.add(2);
        if (b.compareWith(zero) == 0) {
            return one;
        } else if (b.compareWith(one) == 0) {
            return res;
        } else if (b.compareWith(two) == 0) {
            return res.multiply(res);
        } else {
            return (((res.pow(div2(b)))).pow(two)).multiply(res.pow(b.module(two)));
        }
    }

    public LongNum powMod(LongNum b, LongNum mod) {
        LongNum res = this.copy();
        LongNum zero = new LongNum();
        zero.add(0);
        LongNum one = new LongNum();
        one.add(1);
        LongNum two = new LongNum();
        two.add(2);
        if (b.compareWith(zero) == 0) {
            return one;
        } else if (b.compareWith(one) == 0) {
            return res.module(mod);
        } else if (b.compareWith(two) == 0) {
            return (res.multiply(res)).module(mod);
        } else {
            return (res.powMod(div2(b), mod)).powMod(two, mod).multiply(res.powMod(b.module(two), mod)).module(mod);
        }
    }

    public LongNum module(LongNum mod) {
        LongNum div = this.divide(mod);
        LongNum res = this.subst(div.multiply(mod));
        res.normalize();
        return res;
    }

    public LongNum sysSolving(ArrayList<LongNum> a, ArrayList<LongNum> m) {
        LongNum res = new LongNum();
        LongNum zero = new LongNum();
        zero.add(0);
        ArrayList<LongNum> x = new ArrayList<>();
        for(int i = 0;i<a.size();i++){
            x.add(a.get(i));
            for(int j =0;j<i;j++){
                LongNum r = reversed(m , j , i);
                if(r==null) return null;
                x.set(i , r.multiply(x.get(i).subst(x.get(j))));
                x.set(i , x.get(i).module(m.get(i)));
            }
        }
        res = zero.copy();
        LongNum mult = new LongNum();
        mult.add(1);
        int i =0;
        for(LongNum xi : x){
            res = res.addition(xi.multiply(mult));
            mult = mult.multiply(m.get(i));
            i++;
        }
        return res;
    }
    public  LongNum reversed(ArrayList<LongNum> m, int i, int j){
        return m.get(i).inverseElementForMod(m.get(j));
    }
    public LongNum inverseElementForMod(LongNum mod){
        ArrayList<LongNum> triple = gcdExtended(this, mod);
        LongNum g = triple.get(0);
        LongNum x = triple.get(1);
        LongNum one = new LongNum();
        one.add(1);
        if(g.compareWith(one)==0){
           return x.module(mod).addition(mod).module(mod);
        }
        else return null;
    }
    public ArrayList<LongNum> gcdExtended(LongNum a, LongNum b){
        ArrayList<LongNum> triple = new ArrayList<>();
        LongNum zero = new LongNum();
        zero.add(0);
        LongNum one = new LongNum();
        one.add(1);
        if(a.compareWith(zero)==0){
            triple.add(b);
            triple.add(zero.copy());
            triple.add(one.copy());
            return triple;
        }else{
            triple = gcdExtended(b.module(a), a);
            ArrayList<LongNum> res = new ArrayList<>();
            res.add(triple.get(0));
            res.add(triple.get(2).subst(b.divide(a).multiply(triple.get(1))));
            res.add(triple.get(1));
            return res;
        }
    }
    public LongNum GCD(LongNum arg) {
        LongNum a = this.copy();
        LongNum b = arg.copy();
        LongNum zero = new LongNum();
        zero.add(0);
        while (b.compareWith(zero) != 0 && a.compareWith(zero) != 0) {
            if (a.compareWith(b) == 1) {
                a = a.module(b);
            } else {
                b = b.module(a);
            }
        }
        return a.addition(b);
    }

}
