import java.util.ArrayList;

public class Surrounding {
    private PheromoneData NW, N, NE, W, C, E, SW, S, SE;

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

    public Surrounding(PheromoneData NW, PheromoneData n, PheromoneData NE, PheromoneData w, PheromoneData c,
                       PheromoneData e, PheromoneData SW, PheromoneData s, PheromoneData SE) {
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

    public PheromoneData getNW() {
        return NW;
    }

    public ArrayList<PheromoneData> getInList(){
        ArrayList<PheromoneData> list = new ArrayList<>(9);
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

    public void setNW(PheromoneData NW) {
        this.NW = NW;
    }

    public PheromoneData getN() {
        return N;
    }

    public void setN(PheromoneData n) {
        N = n;
    }

    public PheromoneData getNE() {
        return NE;
    }

    public void setNE(PheromoneData NE) {
        this.NE = NE;
    }

    public PheromoneData getW() {
        return W;
    }

    public void setW(PheromoneData w) {
        W = w;
    }

    public PheromoneData getC() {
        return C;
    }

    public void setC(PheromoneData c) {
        C = c;
    }

    public PheromoneData getE() {
        return E;
    }

    public void setE(PheromoneData e) {
        E = e;
    }

    public PheromoneData getSW() {
        return SW;
    }

    public void setSW(PheromoneData SW) {
        this.SW = SW;
    }

    public PheromoneData getS() {
        return S;
    }

    public void setS(PheromoneData s) {
        S = s;
    }

    public PheromoneData getSE() {
        return SE;
    }

    public void setSE(PheromoneData SE) {
        this.SE = SE;
    }
}
