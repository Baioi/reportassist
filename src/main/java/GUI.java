import javax.swing.*;

import NLP.NLP;
import database.initdatabase;

import java.io.*;
import java.util.List;
import java.awt.*;
import java.awt.event.*;

import us.codecraft.webmagic.*;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.pipeline.FilePipeline;
import us.codecraft.webmagic.pipeline.MysqlPipeline;
import us.codecraft.webmagic.processor.example.*;
public class GUI extends JFrame implements ActionListener
{
	JLabel label1=new JLabel("请输入关键词信息：");
	JLabel label2=new JLabel("选择网站：");
	
	JTextField field1=new JTextField(30);
	JTextField field2=new JTextField(30);
	
	JButton button=new JButton("抓取网页");
	JButton button_stop=new JButton("停止抓取");
	JButton button_next=new JButton("生成文摘");
	JTextArea area=new JTextArea();
	
	String option[]={"科技部","工信部","发改委","新闻"};
	String departments[]={"www.most.gov.cn","www.miit.gov.cn","www.sdpc.gov.cn"};
	
	JComboBox add=new JComboBox(option);
	
	Spider spider = null;
	GUI()
	{	
		//设置界面布局
		JPanel north=new JPanel();//大容器
		north.setLayout(new GridLayout(4,2));
		
		add.setMaximumRowCount(3);
		
		north.add(label1);
		north.add(field1);
		north.add(label2);
		north.add(add);
		north.add(button);
		north.add(button_stop);
		north.add(button_next);
		
		add(north,BorderLayout.NORTH);
		add(new JScrollPane(area),BorderLayout.CENTER);
		
		setBounds(100,100,500,500);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		validate();
		
		//设置事件
		button.addActionListener(this);
		button_stop.addActionListener(this);
		button_next.addActionListener(this);
		//add.addActionListener(this);
	}
	
		public void actionPerformed(ActionEvent e)
		{
			Object object=e.getSource();
			
			if(object==add)
			{
			    System.out.println(add.getSelectedItem().toString());
			}
			if(object==button_stop)
			{
				System.out.println("finish!!!!!!!!!!!!!!!!!!");
			    spider.close();
			   
			}
			if(object==button_next)
			{
				System.out.println("@@@@@@");
				List<String> result=new NLP().summary(field1.getText());
				for(int i=0;i<result.size();i++){
					System.out.println(result.get(i));
					area.append(result.get(i));
				}
			}
			if(object==button)
			{
				String department;
				int index=add.getSelectedIndex();
				
				//System.out.println("http://www.baidu.com/s?wd="+field1.getText().trim()+"%20site:"+departments[index]);
				try{
					if (index==0){
					spider=Spider.create(new Most());
					spider.addUrl("http://www.baidu.com/s?wd=%22"+field1.getText()+"%22%20site:"+departments[index])
					// "http://www.baidu.com/ns?word=机床"http://www.baidu.com/s?wd=机床
					// site:www.most.gov.cn

					.addPipeline(new ConsolePipeline())
					.addPipeline(new MysqlPipeline()).start();
					System.out.println("@@@@@@");
					}else if(index==1){
						spider=Spider.create(new Miit());
								spider.addUrl("http://www.baidu.com/s?wd=%22"+field1.getText()+"%22%20site:"+departments[index])
						// "http://www.baidu.com/ns?word=机床"http://www.baidu.com/s?wd=机床
						// site:www.most.gov.cn

						.addPipeline(new ConsolePipeline())
						.addPipeline(new MysqlPipeline()).start();
								
					}else if(index==2){
						spider=Spider.create(new sdpc());
								spider.addUrl("http://www.baidu.com/s?wd=%22"+field1.getText()+"%22%20site:"+departments[index])
						// "http://www.baidu.com/ns?word=机床"http://www.baidu.com/s?wd=机床
						// site:www.most.gov.cn

						.addPipeline(new ConsolePipeline())
						.addPipeline(new MysqlPipeline()).start();
					}else{
						spider=Spider.create(new News());
								spider.addUrl("http://www.baidu.com/s?wd=%22"+field1.getText()+"%22")
						// http://news.baidu.com/ns?word=机床

						.addPipeline(new ConsolePipeline())
						.addPipeline(new MysqlPipeline()).start();
					}
					
				}catch(Exception e1){}
			}
		}
	
	
	
    public static void main(String args[])
    {
    	new initdatabase().initialize();//建立数据库和表
    	
    	new GUI();
    	
    	
    }
}
