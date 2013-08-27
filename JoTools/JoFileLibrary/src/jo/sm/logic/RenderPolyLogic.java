package jo.sm.logic;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.ImageIcon;

import jo.sm.data.BlockTypes;
import jo.sm.data.CubeIterator;
import jo.sm.data.RenderPoly;
import jo.sm.data.SparseMatrix;
import jo.sm.ship.data.Block;
import jo.sm.ui.BlockTypeColors;
import jo.vecmath.Matrix3f;
import jo.vecmath.Matrix4f;
import jo.vecmath.Point3f;
import jo.vecmath.Point3i;

public class RenderPolyLogic
{
    public static List<RenderPoly> getRender(SparseMatrix<Block> blocks)
    {
        List<RenderPoly> polys = new ArrayList<RenderPoly>();
        Point3i lower = new Point3i();
        Point3i upper = new Point3i();
        blocks.getBounds(lower, upper);
        getBasicPolys(blocks, upper, lower, polys);
        return polys;
    }

    private static void getBasicPolys(SparseMatrix<Block> blocks,
            Point3i upper, Point3i lower, List<RenderPoly> polys)
    {
        /*
        for (int z = lower.z; z <= upper.z; z++)
        {
            System.out.println("Z="+z);
            for (int y = lower.y; y <= upper.y; y++)
            {
                if (y < 10)
                    System.out.print(" ");
                System.out.print(y+": ");
                for (int x = lower.x; x <= upper.x; x++)
                {
                    Block b = blocks.get(x, y, z);
//                    if (b == null)
//                        System.out.print(" ------------------------");
//                    else
//                        System.out.print(" "+b.getOrientation()+":"+StringUtils.zeroPrefix(Integer.toBinaryString(b.getBitfield()), 24));
                    if (b == null)
                        System.out.print(" --");
                    else
                        System.out.print(" "+StringUtils.spacePrefix(Integer.toString(b.getOrientation()), 2));
                }
                System.out.println();
            }
        }
        */
        for (CubeIterator i = new CubeIterator(lower, upper); i.hasNext(); )
        {
            Point3i p = i.next();
            if (!blocks.contains(p))
                continue;
            Block b = blocks.get(p);
            if (BlockTypes.isCorner(b.getBlockID()) || BlockTypes.isPowerCorner(b.getBlockID()))
                doCorner(blocks, p, polys);
            else if (BlockTypes.isWedge(b.getBlockID()) || BlockTypes.isPowerWedge(b.getBlockID()))
                doWedge(blocks, p, polys);
            else
               doCube(blocks, p, polys);
        }
    }
    
    private static void doCorner(SparseMatrix<Block> blocks, Point3i p, List<RenderPoly> polys)
    {
    }

    private static void doWedge(SparseMatrix<Block> blocks, Point3i p, List<RenderPoly> polys)
    {
        /*
        switch (blocks.get(p).getOrientation())
        {
            case 0: // YPZM
                doXMSquare(blocks, p, polys, RenderPoly.TRI4);
                doXPSquare(blocks, p, polys, RenderPoly.TRI4);
                // no YP face
                doYMSquare(blocks, p, polys, RenderPoly.SQUARE);
                doZPSquare(blocks, p, polys, RenderPoly.SQUARE);
                // no ZM face
                doRect(blocks, p, polys, RenderPoly.YPZM);
                break;
            case 1: // XMYP
                doXPSquare(blocks, p, polys, RenderPoly.SQUARE);
                // no XM face
                // no YP face
                doYMSquare(blocks, p, polys, RenderPoly.SQUARE);
                doZMSquare(blocks, p, polys, RenderPoly.TRI2);
                doZPSquare(blocks, p, polys, RenderPoly.TRI2);
                doRect(blocks, p, polys, RenderPoly.XMYP);
                break;
            case 2: // YPZP
                doXMSquare(blocks, p, polys, RenderPoly.TRI1);
                doXPSquare(blocks, p, polys, RenderPoly.TRI1);
                // no YP face
                doYMSquare(blocks, p, polys, RenderPoly.SQUARE);
                // no ZP face
                doZMSquare(blocks, p, polys, RenderPoly.SQUARE);
                doRect(blocks, p, polys, RenderPoly.YPZP);
                break;
            case 3: // XPYP
                // no XP face
                doXMSquare(blocks, p, polys, RenderPoly.SQUARE);
                // no YP face
                doYMSquare(blocks, p, polys, RenderPoly.SQUARE);
                doZMSquare(blocks, p, polys, RenderPoly.TRI1);
                doZPSquare(blocks, p, polys, RenderPoly.TRI1);
                doRect(blocks, p, polys, RenderPoly.XPYP);
                break;
            case 4: // YMZM
                doXMSquare(blocks, p, polys, RenderPoly.TRI3);
                doXPSquare(blocks, p, polys, RenderPoly.TRI3);
                doYPSquare(blocks, p, polys, RenderPoly.SQUARE);
                // no YM face
                doZPSquare(blocks, p, polys, RenderPoly.SQUARE);
                // no ZM face
                doRect(blocks, p, polys, RenderPoly.YMZM);
                break;
            case 5: // XPYM
                // no XP face
                doXMSquare(blocks, p, polys, RenderPoly.SQUARE);
                doYPSquare(blocks, p, polys, RenderPoly.SQUARE);
                // no YM face
                doZMSquare(blocks, p, polys, RenderPoly.TRI4);
                doZPSquare(blocks, p, polys, RenderPoly.TRI4);
                doRect(blocks, p, polys, RenderPoly.XPYM);
                break;
            case 6: // YMZP
                doXMSquare(blocks, p, polys, RenderPoly.TRI2);
                doXPSquare(blocks, p, polys, RenderPoly.TRI2);
                doYPSquare(blocks, p, polys, RenderPoly.SQUARE);
                // no YM face
                // no ZP face
                doZMSquare(blocks, p, polys, RenderPoly.SQUARE);
                doRect(blocks, p, polys, RenderPoly.YMZP);
                break;
            case 7: // XMYM
                doXPSquare(blocks, p, polys, RenderPoly.SQUARE);
                // no XM face
                doYPSquare(blocks, p, polys, RenderPoly.SQUARE);
                // no YM face
                doZMSquare(blocks, p, polys, RenderPoly.TRI3);
                doZPSquare(blocks, p, polys, RenderPoly.TRI3);
                doRect(blocks, p, polys, RenderPoly.XMYM);
                break;
            case 8: // XPZM
            case 12: // ???
                // no XP face
                doXMSquare(blocks, p, polys, RenderPoly.SQUARE);
                doYPSquare(blocks, p, polys, RenderPoly.TRI2);
                doYMSquare(blocks, p, polys, RenderPoly.TRI2);
                doZPSquare(blocks, p, polys, RenderPoly.SQUARE);
                // no ZM face
                doRect(blocks, p, polys, RenderPoly.ZMXP);
                break;
            case 10: // XMZM
                doXPSquare(blocks, p, polys, RenderPoly.SQUARE);
                // no XM face
                doYPSquare(blocks, p, polys, RenderPoly.TRI3);
                doYMSquare(blocks, p, polys, RenderPoly.TRI3);
                doZPSquare(blocks, p, polys, RenderPoly.SQUARE);
                // no ZM face
                doRect(blocks, p, polys, RenderPoly.ZMXM);
                break;
            case 11: // XMZP
                doXPSquare(blocks, p, polys, RenderPoly.SQUARE);
                // no XM face
                doYPSquare(blocks, p, polys, RenderPoly.TRI4);
                doYMSquare(blocks, p, polys, RenderPoly.TRI4);
                // no ZP face
                doZMSquare(blocks, p, polys, RenderPoly.SQUARE);
                doRect(blocks, p, polys, RenderPoly.ZPXM);
                break;
            case 13: // XPZP
                // no XP face
                doXMSquare(blocks, p, polys, RenderPoly.SQUARE);
                doYPSquare(blocks, p, polys, RenderPoly.TRI1);
                doYMSquare(blocks, p, polys, RenderPoly.TRI1);
                // no ZP face
                doZMSquare(blocks, p, polys, RenderPoly.SQUARE);
                doRect(blocks, p, polys, RenderPoly.ZPXP);
                break;
            default:
                System.out.println("Wedge with unknown ori="+blocks.get(p).getOrientation());
                break;
        }
        */
    }
    
    private static void doCube(SparseMatrix<Block> blocks, Point3i p, List<RenderPoly> polys)
    {
        doXPSquare(blocks, p, polys, RenderPoly.SQUARE);
        doXMSquare(blocks, p, polys, RenderPoly.SQUARE);
        doYPSquare(blocks, p, polys, RenderPoly.SQUARE);
        doYMSquare(blocks, p, polys, RenderPoly.SQUARE);
        doZPSquare(blocks, p, polys, RenderPoly.SQUARE);
        doZMSquare(blocks, p, polys, RenderPoly.SQUARE);
    }

    /*
    private static void doRect(SparseMatrix<Block> blocks, Point3i p,
            List<RenderPoly> polys, int facing)
    {
        RenderPoly rp = new RenderPoly();
        rp.setBlock(blocks.get(p));
        rp.setNormal(facing);
        rp.setCenter(new Point3i(p));
        rp.setType(RenderPoly.RECTANGLE);
        polys.add(rp);
    }
    */

    private static void doZMSquare(SparseMatrix<Block> blocks, Point3i p,
            List<RenderPoly> polys, int type)
    {
        if (!blocks.contains(new Point3i(p.x, p.y, p.z - 1)))
        {
            RenderPoly rp = new RenderPoly();
            rp.setBlock(blocks.get(p));
            rp.setNormal(RenderPoly.ZM);
            rp.setModelPoints(new Point3i[] {
                    new Point3i(p.x + 0, p.y + 0, p.z + 0),
                    new Point3i(p.x + 1, p.y + 0, p.z + 0),
                    new Point3i(p.x + 1, p.y + 1, p.z + 0),
                    new Point3i(p.x + 0, p.y + 1, p.z + 0),
            });
            rp.setType(type);
            polys.add(rp);
        }
    }

    private static void doZPSquare(SparseMatrix<Block> blocks, Point3i p,
            List<RenderPoly> polys, int type)
    {
        if (!blocks.contains(new Point3i(p.x, p.y, p.z + 1)))
        {
            RenderPoly rp = new RenderPoly();
            rp.setBlock(blocks.get(p));
            rp.setNormal(RenderPoly.ZP);
            rp.setModelPoints(new Point3i[] {
                    new Point3i(p.x + 0, p.y + 0, p.z + 1),
                    new Point3i(p.x + 1, p.y + 0, p.z + 1),
                    new Point3i(p.x + 1, p.y + 1, p.z + 1),
                    new Point3i(p.x + 0, p.y + 1, p.z + 1),
            });
            rp.setType(type);
            polys.add(rp);
        }
    }

    private static void doYMSquare(SparseMatrix<Block> blocks, Point3i p,
            List<RenderPoly> polys, int type)
    {
        if (!blocks.contains(new Point3i(p.x, p.y - 1, p.z)))
        {
            RenderPoly rp = new RenderPoly();
            rp.setBlock(blocks.get(p));
            rp.setNormal(RenderPoly.YM);
            rp.setModelPoints(new Point3i[] {
                    new Point3i(p.x + 0, p.y + 0, p.z + 0),
                    new Point3i(p.x + 1, p.y + 0, p.z + 0),
                    new Point3i(p.x + 1, p.y + 0, p.z + 1),
                    new Point3i(p.x + 0, p.y + 0, p.z + 1),
            });
            rp.setType(type);
            polys.add(rp);
        }
    }

    private static void doYPSquare(SparseMatrix<Block> blocks, Point3i p,
            List<RenderPoly> polys, int type)
    {
        if (!blocks.contains(new Point3i(p.x, p.y + 1, p.z)))
        {
            RenderPoly rp = new RenderPoly();
            rp.setBlock(blocks.get(p));
            rp.setNormal(RenderPoly.YP);
            rp.setModelPoints(new Point3i[] {
                    new Point3i(p.x + 0, p.y + 1, p.z + 0),
                    new Point3i(p.x + 1, p.y + 1, p.z + 0),
                    new Point3i(p.x + 1, p.y + 1, p.z + 1),
                    new Point3i(p.x + 0, p.y + 1, p.z + 1),
            });
            rp.setType(type);
            polys.add(rp);
        }
    }

    private static void doXMSquare(SparseMatrix<Block> blocks, Point3i p,
            List<RenderPoly> polys, int type)
    {
        if (!blocks.contains(new Point3i(p.x - 1, p.y, p.z)))
        {
            RenderPoly rp = new RenderPoly();
            rp.setBlock(blocks.get(p));
            rp.setNormal(RenderPoly.XM);
            rp.setModelPoints(new Point3i[] {
                    new Point3i(p.x + 0, p.y + 0, p.z + 0),
                    new Point3i(p.x + 0, p.y + 1, p.z + 0),
                    new Point3i(p.x + 0, p.y + 1, p.z + 1),
                    new Point3i(p.x + 0, p.y + 0, p.z + 1),
            });
            rp.setType(type);
            polys.add(rp);
        }
    }

    private static void doXPSquare(SparseMatrix<Block> blocks, Point3i p,
            List<RenderPoly> polys, int type)
    {
        if (!blocks.contains(new Point3i(p.x + 1, p.y, p.z)))
        {
            RenderPoly rp = new RenderPoly();
            rp.setBlock(blocks.get(p));
            rp.setNormal(RenderPoly.XP);
            rp.setModelPoints(new Point3i[] {
                    new Point3i(p.x + 1, p.y + 0, p.z + 0),
                    new Point3i(p.x + 1, p.y + 1, p.z + 0),
                    new Point3i(p.x + 1, p.y + 1, p.z + 1),
                    new Point3i(p.x + 1, p.y + 0, p.z + 1),
            });
            rp.setType(type);
            polys.add(rp);
        }
    }
    
    public static void transformAndSort(List<RenderPoly> tiles, Matrix4f transform)
    {
        Matrix3f rot = new Matrix3f();
        transform.get(rot);

        boolean[] showing = new boolean[6+12];
        calcShowing(showing, rot, 1, 0, 0, RenderPoly.XP, RenderPoly.XM);
        calcShowing(showing, rot, 0, 1, 0, RenderPoly.YP, RenderPoly.YM);
        calcShowing(showing, rot, 0, 0, 1, RenderPoly.ZP, RenderPoly.ZM);
        calcShowing(showing, rot, 1, 1, 0, RenderPoly.XPYP, RenderPoly.XMYM);
        calcShowing(showing, rot, -1, 1, 0, RenderPoly.XMYP, RenderPoly.XPYM);
        calcShowing(showing, rot, 0, 1, 1, RenderPoly.YPZP, RenderPoly.YMZM);
        calcShowing(showing, rot, 0, -1, 1, RenderPoly.YMZP, RenderPoly.YPZM);
        calcShowing(showing, rot, 1, 0, 1, RenderPoly.ZPXP, RenderPoly.ZMXM);
        calcShowing(showing, rot, 1, 0, -1, RenderPoly.ZMXP, RenderPoly.ZPXM);
        //System.out.println("Showing +x="+showing[0]+", -x="+showing[1]+", +y="+showing[2]+", -y="+showing[3]+", +z="+showing[4]+", -z="+showing[5]);
        for (RenderPoly tile : tiles)
        {
            if (!showing[tile.getNormal()])
            {
                tile.setVisualPoints(null);
                continue;
            }
            Point3i[] model = tile.getModelPoints();
            Point3f[] visual = new Point3f[model.length];
            for (int i = 0; i < visual.length; i++)
            {
                visual[i] = new Point3f(model[i].x, model[i].y, model[i].z);
                transform.transform(visual[i]);
            }
            tile.setVisualPoints(visual);
        }
        Collections.sort(tiles, new Comparator<RenderPoly>(){
            @Override
            public int compare(RenderPoly tile1, RenderPoly tile2)
            {
                if (tile1.getVisualPoints() == null)
                    if (tile2.getVisualPoints() == null)
                        return 0;
                    else
                        return 1;
                else
                    if (tile2.getVisualPoints() == null)
                        return -1;
                    else
                        return (int)Math.signum(tile2.getVisualPoints()[0].z - tile1.getVisualPoints()[0].z);
            }            
        });
    }
    
    private static void calcShowing(boolean[] showing, Matrix3f rot, int dx, int dy, int dz, int axis, int naxis)
    {
        Point3f xp = new Point3f(dx, dy, dz);
        rot.transform(xp);
        showing[axis] = xp.z < 0;
        showing[naxis] = !showing[axis];
    }
    
    public static void draw(Graphics2D g2, List<RenderPoly> tiles, Point3f unitX, Point3f unitY, Point3f unitZ,
    		boolean fancyGraphics)
    {
        for (RenderPoly tile : tiles)
        {
            Point3f[] points = tile.getVisualPoints();
            if (points == null)
                break;
            ImageIcon icon = null;
            if (fancyGraphics)
            	icon = BlockTypeColors.getBlockImage(tile.getBlock().getBlockID());
            if (tile.getType() == RenderPoly.SQUARE)
                renderSquare(g2, points, tile, icon);
            else if ((tile.getType() >= RenderPoly.TRI1) && (tile.getType() <= RenderPoly.TRI4))
                renderTriangle(g2, points, tile, icon);
        }
        
    }

    private static void renderTriangle(Graphics2D g2, Point3f[] corners,
            RenderPoly tile, ImageIcon icon)
    {
        //System.out.println("Render triangle "+tile.getType());
        int pCenter = (tile.getType() - RenderPoly.TRI1);
        int pLeft = (pCenter + 1)%4;
        int pRight = (pCenter + 3)%4;
        if (icon != null)
        {
            float m00 = (corners[pRight].x - corners[pCenter].x)/64f;
            float m10 = (corners[pRight].y - corners[pCenter].y)/64f;
            float m01 = (corners[pLeft].x - corners[pCenter].x)/64f;
            float m11 = (corners[pLeft].y - corners[pCenter].y)/64f;
            float m02 = corners[pCenter].x;
            float m12 = corners[pCenter].y;
            AffineTransform t = new AffineTransform(m00, m10, m01, m11, m02, m12);
            g2.drawImage(icon.getImage(), t, null);
        }
        else
        {
            Path2D p = new Path2D.Float();
            p.moveTo(corners[pCenter].x, corners[pCenter].y);
            p.lineTo(corners[pLeft].x, corners[pLeft].y);
            p.lineTo(corners[pRight].x, corners[pRight].y);
            p.lineTo(corners[pCenter].x, corners[pCenter].y);
            g2.setPaint(BlockTypeColors.getFillColor(tile.getBlock().getBlockID()));
            g2.fill(p);
            g2.setPaint(BlockTypeColors.getOutlineColor(tile.getBlock().getBlockID()));
            g2.draw(p);
        }
    }

    private static void renderSquare(Graphics2D g2, Point3f[] corners,
            RenderPoly tile, ImageIcon icon)
    {
        if (icon != null)
        {
            float m00 = (corners[1].x - corners[0].x)/64f;
            float m10 = (corners[1].y - corners[0].y)/64f;
            float m01 = (corners[3].x - corners[0].x)/64f;
            float m11 = (corners[3].y - corners[0].y)/64f;
            float m02 = corners[0].x;
            float m12 = corners[0].y;
            AffineTransform t = new AffineTransform(m00, m10, m01, m11, m02, m12);
            g2.drawImage(icon.getImage(), t, null);
        }
        else
        {
            Path2D p = new Path2D.Float();
            p.moveTo(corners[0].x, corners[0].y);
            p.lineTo(corners[1].x, corners[1].y);
            p.lineTo(corners[2].x, corners[2].y);
            p.lineTo(corners[3].x, corners[3].y);
            p.lineTo(corners[0].x, corners[0].y);
            g2.setPaint(BlockTypeColors.getFillColor(tile.getBlock().getBlockID()));
            g2.fill(p);
            g2.setPaint(BlockTypeColors.getOutlineColor(tile.getBlock().getBlockID()));
            g2.draw(p);
        }
    }

  }
