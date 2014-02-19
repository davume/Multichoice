/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dav.astarroute;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author david Umera
 */
public class Routesearch {

    static Tile[][] TileList = new Tile[50][50];
    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(Routesearch.class);

    public static void main(String[] args) {
        String mapfile = "/large_map.txt";
        new Routesearch().ProcessRoute(mapfile);
    }

    private void ProcessRoute(String file) {
        uploadTiles(file);
        Tile target = findStart();

        while (target.getTerrain() != 'X') {
            ArrayList<Tile> scand = scanTileRoutes(target);
            Tile routeTl = selectTileRoute(target, scand);
            target = routeTl;
        }

        DisplayRoute(TileList);

    }

    private void uploadTiles(String map_file) {
        InputStream is = getClass().getResourceAsStream(map_file);
        BufferedReader buff = new BufferedReader(new InputStreamReader(is));
        int row = 0;

        try {
            String line = buff.readLine();
            while (line != null) {
                char[] lineCh = line.toCharArray();
                for (int c = 0; c < lineCh.length; c++) {
                    TileList[c][row] = new Tile(lineCh[c], c, row);
                    TileList[c][row].setTerrainCost(Terraincost(lineCh[c]));
                }
                row++;
                line = buff.readLine();
            }
            //DisplayRoute(TileList);
        } catch (IOException ex) {
            log.error("File line reader error", ex);
        }
    }

    private Tile findStart() {
        Tile start = null;
        int rowln = TileList.length;
        int columnln = TileList[0].length;
        for (int row = 0; row < rowln; row++) {
            for (int column = 0; column < columnln; column++) {
                if (TileList[column][row].getTerrain() == '@') {
                    start = TileList[column][row];
                    break;
                }
            }
        }
        return start;
    }

    private ArrayList<Tile> scanTileRoutes(Tile target) {

        ArrayList<Tile> tileroutes = new ArrayList();
        //Scan surounding routes
        int i = 0;
        for (int y = -1; y <= 1; y++) {
            for (int x = -1; x <= 1; x++) {
                if (!(x == 0 && y == 0)) {
                    try {
                        tileroutes.add(TileList[(int) (target.getxT() + x)][(int) (target.getyT() + y)]);
                    } catch (Exception ex) {
                        log.debug(ex);
                        continue;
                    }
                }
            }
        }


        ArrayList<Tile> Btileroutes = new ArrayList<Tile>(tileroutes);
        //Remove N/A
        Iterator<Tile> iter = tileroutes.iterator();
        while (iter.hasNext()) {
            Tile slctd = iter.next();
            if (slctd.getTerrainCost() == -1 || slctd.isIsScaned()) {
                iter.remove();
            }
        }

        if (tileroutes.isEmpty()) {
            log.debug("Routes is emptyList ");
            Iterator<Tile> iter2 = Btileroutes.iterator();
            while (iter.hasNext()) {
                Tile slctd = iter2.next();
                if (slctd.getTerrainCost() == -1) {
                    iter2.remove();
                }
            }

            tileroutes = new ArrayList<Tile>(Btileroutes);
        }
//        for (Tile slct : tileroutes) {
//            log.debug("Reload --> " + slct.getTerrain() + " - " + slct.getxT() + ":" + slct.getyT() + " :s" + slct.isScaned);
//        }

        return tileroutes;
    }

    private Tile selectTileRoute(Tile target, ArrayList<Tile> scandlist) {
        //Set cost of movement + Distance

        if (scandlist.isEmpty()) {
            log.error("Empty list have been passed " + target.getTerrain());
        }

        for (Tile slct : scandlist) {
            int costmv = CostOfMovement(target, slct);
            int dist = calcDistance(slct);
            slct.setCostToMove(costmv);
            slct.setDist(dist);
            slct.setFinalscore(costmv + dist);
            slct.setScaned();
            slct.setFarcost(costmv);
            
        }

        Tile bestroute = null;
        try {
            bestroute = scandlist.get(0);
        } catch (Exception xe) {
            DisplayRoute(TileList);
            log.error(xe);
        }

        for (Tile tl : scandlist) {
            //Breakout if Dest found
            if (tl.getTerrain() == 'X') {
                bestroute = tl;
                break;
            }

            if (tl.getFinalscore() <= bestroute.getFinalscore()) {
//                if(tl.getFinalscore() == bestroute.getFinalscore()){
//                    log.debug("Need a tie breaker!!! by distance"+tl.getFinalscore() +"&"+bestroute.getFinalscore());
//                    if(tl.getDist() < bestroute.getDist()){
//                        bestroute = tl;
//                    }      
//                }else{
//                bestroute = tl;
//                }
                bestroute = tl;
            }
        }

        if (bestroute.getTerrain() != 'X') {
            bestroute.setTerrain('#');
        }
        return bestroute;
    }

    private int CostOfMovement(Tile target, Tile choice) {
        int costOfmv = target.getFarcost() + choice.getTerrainCost();
        return costOfmv;
    }

    private int calcDistance(Tile target) {
        int dist = Math.abs((target.getxT() - (TileList[0].length - 1)) + (target.getyT() - (TileList.length - 1)));
        return dist;
    }

    private String DisplayRoute(Tile[][] TileList) {

        FileWriter outf = null;
        try {
            outf = new FileWriter("routed_map.txt");

            int rowln = TileList.length;
            int columnln = TileList[0].length;

            for (int row = 0; row < rowln; row++) {

                for (int column = 0; column < columnln; column++) { 
                    System.out.print(TileList[column][row].getTerrain());
                    outf.append(TileList[column][row].getTerrain());
                }
                System.out.println();
                outf.append("\n");
            }

        } catch (IOException ex) {
            log.error(ex);
        } finally {
            try {
                outf.close();
            } catch (IOException ex) {
                log.error(ex);
            }
        }

        return "route";
    }

    private int Terraincost(char c) {
        if (c == '.' || c == '@' || c == 'X') {
            return 1;
        } else if (c == '*') {
            return 2;
        } else if (c == '^') {
            return 3;
        } else {
            return -1;
        }
    }
}
