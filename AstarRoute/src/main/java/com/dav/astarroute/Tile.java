/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dav.astarroute;

/**
 *
 * @author david
 */
public class Tile {
    private int xT;
    private int yT;
    private int dist;
    private int farcost = 0;
    private int costToMove;
    private int finalscore;
    private char Terrain;
    private int TerrainCost;
    private boolean scaned = false;
   
    public Tile(char Terrain, int xTileid, int yTileid){
        this.Terrain = Terrain;
        this.xT = xTileid;
        this.yT = yTileid;
    }


    public int getFarcost() {
        return farcost;
    }

    public void setFarcost(int farcost) {
        this.farcost = farcost;
    }

    public int getCostToMove() {
        return costToMove;
    }

    public void setCostToMove(int costToMove) {
        this.costToMove = costToMove;
    }

    public int getFinalscore() {
        return finalscore;
    }

    public void setFinalscore(int finalscore) {
        this.finalscore = finalscore;
    }
    
    public char getTerrain() {
        return Terrain;
    }

    public int getTerrainCost() {
        return TerrainCost;
    }
    
    public void setTerrainCost(int TerrainCost) {
        this.TerrainCost = TerrainCost;
    }
    
    public int getxT() {
        return xT;
    }

    public int getyT() {
        return yT;
    }
    
    public boolean isIsScaned() {
        return scaned;
    }
    
    public void setScaned() {
        this.scaned = true;
    }
    
    public int getDist() {
        return dist;
    }

    public void setDist(int dist) {
        this.dist = dist;
    }
     
    public void setTerrain(char Terrain) {
        this.Terrain = Terrain;
    }
}
