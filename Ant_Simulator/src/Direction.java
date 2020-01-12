import java.util.ArrayList;

public enum Direction {
    NW(0),
    N(1),
    NE(2),
    W(3),
    C(4),
    E(5),
    SW(6),
    S(7),
    SE(8);

    private int d;

    Direction(int i){
        this.d = i;
    }

    public int getNum(){
        return this.d;
    }

    public ArrayList<Integer> getSide(Direction d){
        ArrayList<Integer> sides = new ArrayList<>();
        switch(d){
            case NW:
                sides.add(W.getNum());
                sides.add(NW.getNum());
                sides.add(N.getNum());
                break;
            case NE:
                sides.add(N.getNum());
                sides.add(NE.getNum());
                sides.add(E.getNum());
                break;
            case W:
                sides.add(SW.getNum());
                sides.add(W.getNum());
                sides.add(NW.getNum());
                break;
            case E:
                sides.add(NE.getNum());
                sides.add(E.getNum());
                sides.add(SE.getNum());
                break;
            case S:
                sides.add(SW.getNum());
                sides.add(S.getNum());
                sides.add(SE.getNum());
                break;
            case SW:
                sides.add(S.getNum());
                sides.add(SW.getNum());
                sides.add(W.getNum());
                break;
            case SE:
                sides.add(S.getNum());
                sides.add(SE.getNum());
                sides.add(E.getNum());
                break;
            default:
                sides.add(NW.getNum());
                sides.add(N.getNum());
                sides.add(NE.getNum());
                break;
        }

        return sides;
    }

    public ArrayList<Integer> getSurrounding(Direction d){
        ArrayList<Integer> front = getSide(d);
        ArrayList<Integer> surrounding = new ArrayList<>();
        for(int i = 0; i < 9; i++){
            surrounding.add(i);
        }
        surrounding.removeAll(front);
        return surrounding;
    }
}
