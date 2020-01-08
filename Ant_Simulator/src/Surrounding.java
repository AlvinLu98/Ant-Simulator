import java.util.ArrayList;

public class Surrounding {
    private Pheromone_Data NW, N, NE, W, C, E, SW, S, SE;

    public Surrounding(){
        this.NW = null;
        this.N = null;
        this.NE = null;
        this.W = null;
        this.C = null;
        this.E = null;
        this.SW = null;
        this.S = null;
        this.SE = null;
    }

    public Surrounding(Pheromone_Data NW, Pheromone_Data n, Pheromone_Data NE, Pheromone_Data w, Pheromone_Data c,
                       Pheromone_Data e, Pheromone_Data SW, Pheromone_Data s, Pheromone_Data SE) {
        this.NW = NW;
        this.N = n;
        this.NE = NE;
        this.W = w;
        this.C = c;
        this.E = e;
        this.SW = SW;
        this.S = s;
        this.SE = SE;
    }

    public Pheromone_Data getNW() {
        return NW;
    }

    public ArrayList<Pheromone_Data> getInList(){
        ArrayList<Pheromone_Data> list = new ArrayList<>(9);
        list.add(NW);
        list.add(N);
        list.add(NE);
        list.add(W);
        list.add(C);
        list.add(E);
        list.add(SW);
        list.add(S);
        list.add(SE);
        return list;
    }

    public void setNW(Pheromone_Data NW) {
        this.NW = NW;
    }

    public Pheromone_Data getN() {
        return N;
    }

    public void setN(Pheromone_Data n) {
        N = n;
    }

    public Pheromone_Data getNE() {
        return NE;
    }

    public void setNE(Pheromone_Data NE) {
        this.NE = NE;
    }

    public Pheromone_Data getW() {
        return W;
    }

    public void setW(Pheromone_Data w) {
        W = w;
    }

    public Pheromone_Data getC() {
        return C;
    }

    public void setC(Pheromone_Data c) {
        C = c;
    }

    public Pheromone_Data getE() {
        return E;
    }

    public void setE(Pheromone_Data e) {
        E = e;
    }

    public Pheromone_Data getSW() {
        return SW;
    }

    public void setSW(Pheromone_Data SW) {
        this.SW = SW;
    }

    public Pheromone_Data getS() {
        return S;
    }

    public void setS(Pheromone_Data s) {
        S = s;
    }

    public Pheromone_Data getSE() {
        return SE;
    }

    public void setSE(Pheromone_Data SE) {
        this.SE = SE;
    }
}
