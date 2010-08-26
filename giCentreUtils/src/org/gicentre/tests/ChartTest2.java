package org.gicentre.tests;

import org.gicentre.utils.stat.BarChart;
import org.gicentre.utils.stat.XYChart;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PVector;

//  ****************************************************************************************
/** Tests more sophisticated chart options in a Processing sketch. 
 *  @author Jo Wood, giCentre, City University London.
 *  @version 3.1, 26th August, 2010. 
 */ 
// *****************************************************************************************

/* This file is part of giCentre utilities library. gicentre.utils is free software: you can 
 * redistribute it and/or modify it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 * 
 * gicentre.utils is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  
 * See the GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License along with this
 * source code (see COPYING.LESSER included with this source code). If not, see 
 * http://www.gnu.org/licenses/.
 */

@SuppressWarnings("serial")
public class ChartTest2 extends PApplet
{
    // ------------------------------ Starter method ------------------------------- 

    /** Creates a simple application to test the chart drawing utilities.
     *  @param args Command line arguments (ignored). 
     */
    public static void main(String[] args)
    {   
        PApplet.main(new String[] {"org.gicentre.tests.ChartTest2"});
    }

    // ----------------------------- Object variables ------------------------------

    private BarChart barChart;
    private XYChart  xyChart;

    private boolean showXAxis, showYAxis;
    private boolean showXAxisLabel, showYAxisLabel;
    private boolean transpose;
    private boolean capValues;
    private int barGap=2;

    // ---------------------------- Processing methods -----------------------------

    /** Sets up the chart and fonts.
     */
    public void setup()
    {   
        size(550,350);
        smooth();
        textFont(createFont("Helvetica",10));
        textSize(10);
        
        showXAxis      = true;
        showYAxis      = true;
        showXAxisLabel = true;
        showYAxisLabel = true;
        transpose      = false;
        capValues      = false;

        float[] chartData = new float[] {12,-7,16,13,25};

        barChart = new BarChart(this);
        barChart.setData(chartData);
        barChart.transposeAxes(false);
        barChart.setBarGap(barGap);
        barChart.setBarColour(color(200,150,150));
        barChart.setCategoryAxisLabel("This is the x axis");
        barChart.setValueAxisLabel("This is the y axis");
        barChart.showCategoryAxis(showXAxis);
        barChart.showValueAxis(showYAxis);
        barChart.setCategoryAxisAt(0);          // Allow bars to dip below axis when negative. 
        
        xyChart = new XYChart(this);
        xyChart.setData(new float[] {1,2,3,4,5}, chartData);
        
        xyChart.setLineColour(color(80,30,30));
        xyChart.setLineWidth(2);
        xyChart.setPointSize(10);
        xyChart.setPointColour(color(80,80,150,180));
        xyChart.setXAxisAt(0);
        xyChart.setMinY(barChart.getMinValue()); // Scale line graph to use same space as bar graph.
        xyChart.setMaxY(barChart.getMaxValue());
    }

    /** Draws some charts.
     */
    public void draw()
    {   
        background(255);
        noLoop();
                
        // Draw the bar chart first, then overlay the line chart.
        barChart.draw(1,1,width-2,height-2);  
        
        PVector bottomLeft = barChart.getDataToScreen(new PVector(0,barChart.getMinValue()));
        PVector topRight   = barChart.getDataToScreen(new PVector(barChart.getNumBars()-1,barChart.getMaxValue()));
                 
        xyChart.draw(bottomLeft.x-2,topRight.y-2,topRight.x+5-bottomLeft.x,bottomLeft.y-topRight.y+5);

    }
    
    public void mousePressed()
    {
        PVector dp = barChart.getScreenToData(new PVector(mouseX,mouseY));
        if (dp == null)
        {
            System.out.println("Screen point of "+mouseX+","+mouseY+" is outside the data area.");
        }
        else
        {
            System.out.println("Screen point of "+mouseX+","+mouseY+" gives XY data point of "+dp.x+","+dp.y);
            PVector sp = barChart.getDataToScreen(dp);
            System.out.println("    which in turn gives screen point of "+sp.x+","+sp.y);
        }
    }

    public void keyPressed()
    {
        if (key == 'c')
        {
            capValues = ! capValues;
            
            if (capValues)
            {
                barChart.setMaxValue(20);
                barChart.setMinValue(-5);
                xyChart.setMaxY(20);
                xyChart.setMinY(-5);
            }
            else
            {
                // Use data to determine range.
                barChart.setMaxValue(Float.NaN);
                barChart.setMinValue(Float.NaN);
                xyChart.setMinY(barChart.getMinValue()); 
                xyChart.setMaxY(barChart.getMaxValue());
            }
            loop();
        }
        else if (key == 'x')
        {
            showXAxis = !showXAxis;
            barChart.showCategoryAxis(showXAxis);
            //xyChart.showXAxis(showXAxis);
            loop();
        }
        else if (key == 'y')
        {
            showYAxis = !showYAxis;
            barChart.showValueAxis(showYAxis);
            //xyChart.showYAxis(showYAxis);
            loop();
        }
        else if (key == 'l')
        {
            showXAxisLabel = !showXAxisLabel;
            showYAxisLabel = !showYAxisLabel;
            
            xyChart.setXAxisLabel(showXAxisLabel?"This is the x axis":null);
            xyChart.setYAxisLabel(showYAxisLabel?"This is the y axis":null);
            
            barChart.setCategoryAxisLabel(showXAxisLabel?"This is the x axis":null);
            barChart.setValueAxisLabel(showYAxisLabel?"This is the y axis":null);
            
            loop();
        }
        if (key == 't')
        {
            transpose = !transpose;
            barChart.transposeAxes(transpose);
            barChart.setReverseCategories(transpose);
            xyChart.transposeAxes(transpose);
            loop();
        }

        if (key == CODED)
        {
            if ((keyCode == PConstants.LEFT) && (barGap > 0))
            {
                barGap--;
                barChart.setBarGap(barGap);
                loop();
            }
            else if ((keyCode == PConstants.RIGHT) && (barGap < width))
            {
                barGap++;
                barChart.setBarGap(barGap);
                loop();
            }
        }
    }
}