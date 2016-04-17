package chart;

import java.awt.Font;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.jfree.data.*; 
import org.jfree.data.category.*;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.chart.*; 
import org.jfree.chart.plot.*;
import org.jfree.chart.title.LegendTitle;
import org.jfree.chart.title.TextTitle;

import database.SQLop;;

public class Chart {
	static final int SITE = 1;
	static final int YEAR = 2;
	
	SQLop sqlop = new SQLop();
	String keyword = new String();
	
	public Chart(String kw){
		keyword = kw;
		sqlop.initialize();
		System.out.println(sqlop.countAllResult(keyword));
		siteChart();
		yearChart();
		sqlop.close();
	}
	
	public void siteChart(){
		CategoryDataset dataset = getSiteDataset();
		JFreeChart chart = ChartFactory.createBarChart(
				"site source",
				"site", 
				"number", 
				dataset,
				PlotOrientation.VERTICAL,
				false,
				false,
				false);
		FileOutputStream fos_jpg = null; 
        try { 
            fos_jpg = new FileOutputStream(".\\barchartTEST.jpg"); 
            ChartUtilities.writeChartAsJPEG(fos_jpg,(float)1.0,chart,400,300,null); 
        } catch (Exception e) {
			e.printStackTrace();
		} finally { 
            try { 
                fos_jpg.close(); 
            } catch (Exception e) {} 
        } 
	}
	
	private CategoryDataset getSiteDataset(){
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		HashMap<String, Integer> hashmap = sqlop.count(SITE, keyword);
		List<Map.Entry<String, Integer>> entry =
			    new ArrayList<Map.Entry<String, Integer>>(hashmap.entrySet());
		for(Map.Entry<String, Integer> i: entry){
			dataset.addValue(i.getValue().intValue(), "����", i.getKey());
		}
		return dataset;
	}
	
	public void yearChart(){
		DefaultCategoryDataset dataset = getYearDataset();
		
		JFreeChart chart = ChartFactory.createLineChart("��ݷֲ�ͳ��",
				null,
				null,
				dataset,
				PlotOrientation.VERTICAL,
				false,
				false,
				false);
		
	    chart.getTitle().setFont(new Font("����",Font.BOLD,20));   
	    
		FileOutputStream fos_jpg = null; 
        try { 
            fos_jpg = new FileOutputStream(".\\linechartTEST.jpg"); 
            ChartUtilities.writeChartAsJPEG(fos_jpg,(float)1.0,chart,400,300,null); 
        } catch (Exception e) {
			e.printStackTrace();
		} finally { 
            try { 
                fos_jpg.close(); 
            } catch (Exception e) {} 
        } 
	}
	
	private DefaultCategoryDataset getYearDataset(){
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		HashMap<String, Integer> hashmap = sqlop.count(YEAR, keyword);
		
		List<Map.Entry<String, Integer>> entry =
			    new ArrayList<Map.Entry<String, Integer>>(hashmap.entrySet());
		Collections.sort(entry, new Comparator<Map.Entry<String, Integer>>() {   
		    public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {   
		        return (o1.getKey()).toString().compareTo(o2.getKey());
		    }
		});
		
		for(Map.Entry<String, Integer> i: entry){
			dataset.addValue(i.getValue(),"year",i.getKey());
		}
		
		return dataset;
	}
}
