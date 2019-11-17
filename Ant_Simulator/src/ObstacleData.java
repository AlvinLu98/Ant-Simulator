public class ObstacleData {
    private int[][] points;

    public ObstacleData(int w, int h) {
        this.points = new int[w][h];
        for(int x = 0; x < w; x++){
            for(int y = 0; y < h; y++){
                points[x][y] = 0;
            }
        }
    }

    public int[][] getPoints() {
        return this.points;
    }

    public int[][] oneObstacle(){
        for(int x = 0; x < points.length; x++){
            for(int y = 0; y < points[x].length; y++){
                if( ((x-55)*0.707+(y-35)*0.707)*((x-55)*0.707+(y-35)*0.707)/36+
                        ((x-55)*0.707-(y-35)*0.707)*((x-55)*0.707-(y-35)*0.707)/1024 <= 1 ){
                    points[x][y] = 1;
                }
            }
        }
        return points;
    }
}
