public class Obstacle_Data {
    private boolean[][] points;

    public Obstacle_Data(int w, int h) {
        this.points = new boolean[w][h];
        for(int x = 0; x < w; x++){
            for(int y = 0; y < h; y++){
                points[x][y] = false;
            }
        }
    }

    public boolean[][] getPoints() {
        return this.points;
    }

    public boolean[][] oneObstacle(){
        for(int x = 0; x < points.length; x++){
            for(int y = 0; y < points[x].length; y++){
                if( ((x-100)*0.707+(y-35)*0.707)*((x-100)*0.707+(y-35)*0.707)/36+
                        ((x-100)*0.707-(y-35)*0.707)*((x-100)*0.707-(y-35)*0.707)/1240 <= 1 ){
                    points[x][y] = true;
                }
            }
        }
        return points;
    }

    public boolean[][] twoObstacle(){
        for(int x = 0; x < points.length; x++){
            for(int y = 0; y < points[x].length; y++){
                if( ((x-50)*0.707+(y-35)*0.707)*((x-50)*0.707+(y-35)*0.707)/36+
                        ((x-55)*0.707-(y-35)*0.707)*((x-55)*0.707-(y-35)*0.707)/1240 <= 1 ){
                    points[x][y] = true;
                }
                if( ((x-100)*0.707+(y-70)*0.707)*((x-100)*0.707+(y-70)*0.707)/36+
                        ((x-100)*0.707-(y-70)*0.707)*((x-100)*0.707-(y-70)*0.707)/1240 <= 1 ){
                    points[x][y] = true;
                }
            }
        }
        return points;
    }
}
