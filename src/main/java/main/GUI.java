package main;

import javax.swing.*;

import chart.*;
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

public class GUI extends JFrame implements ActionListener {
	JLabel label1 = new JLabel("*请输入关键词信息：");
	JLabel label1_1 = new JLabel("---- 爬取选项（二选一）");
	JLabel label1_2 = new JLabel();
	JLabel label2 = new JLabel("爬取范围：");
	JLabel label3 = new JLabel("特定网站：");

	JTextField field1 = new JTextField(30);
	JTextField field2 = new JTextField(30);

	JButton button = new JButton("抓取网页");
	JButton button_stop = new JButton("停止抓取");
	JButton button_next = new JButton("生成文摘");

	JScrollPane scrollPane = null;

	String option[] = { "", "科技部", "工信部", "发改委", };
	String option2[] = { "", "新闻", "论文", "专利" };
	String departments[] = { "","www.most.gov.cn", "www.miit.gov.cn",
			"www.sdpc.gov.cn" };

	JComboBox add = new JComboBox(option);
	JComboBox add2 = new JComboBox(option2);

	JDialog frame1 = new JDialog();
	JLabel label_wrong = new JLabel();

	Spider spider = null;

	GUI() {
		// 设置界面布局
		JPanel north = new JPanel();// 大容器
		north.setLayout(new GridLayout(6, 2));

		add.setMaximumRowCount(3);

		north.add(label1);
		north.add(field1);
		north.add(label1_1);
		north.add(label1_2);
		north.add(label2);
		north.add(add);
		north.add(label3);
		north.add(add2);
		north.add(button);
		north.add(button_stop);
		north.add(button_next);

		add(north, BorderLayout.CENTER);

		setBounds(100, 100, 500, 200);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		validate();

		label_wrong.setHorizontalAlignment(SwingConstants.CENTER);
		frame1.add(label_wrong);
		frame1.setTitle("出错");
		frame1.setLocation(500, 300);
		frame1.setSize(150, 80);
		frame1.setModal(true);

		// 设置事件
		button.addActionListener(this);
		button_stop.addActionListener(this);
		button_next.addActionListener(this);
		// add.addActionListener(this);
	}

	public void actionPerformed(ActionEvent e) {
		Object object = e.getSource();

		if (object == add) {
			System.out.println(add.getSelectedItem().toString());
		}
		if (object == button_stop) {
			System.out.println("finish!!!!!!!!!!!!!!!!!!");
			spider.close();

		}
		if (object == button_next) {
			// System.out.println("@@@@@@");
			String key = field1.getText();
			if (key.replaceAll(" ", "").length() == 0) {
				label_wrong.setText("关键词不能为空");
				frame1.setVisible(true);
			} else
				new Result_face(key);

		}
		if (object == button) {
			int index = add.getSelectedIndex();
			int index2 = add2.getSelectedIndex();
			// System.out.println("http://www.baidu.com/s?wd="+field1.getText().trim()+"%20site:"+departments[index]);
			try {
				if (index == 1 || index == 2 || index == 3) {
					if (index == 1) {
						spider = Spider.create(new Most());
					} else if (index == 2) {
						spider = Spider.create(new Miit());
					} else if (index == 3) {
						spider = Spider.create(new sdpc());
					}
					spider.addUrl("http://cn.bing.com/search?q=site%3A"
									+ departments[index] + "+%22"
									+ field1.getText() + "%22+filetype%3Ahtml")
							// "http://www.baidu.com/ns?word=机床"http://www.baidu.com/s?wd=机床
							// site:www.most.gov.cn

							.addPipeline(new ConsolePipeline())
							.addPipeline(new MysqlPipeline()).start();
					System.out.println("@@@@@@");
				} else if (index2 == 1) {
					spider = Spider.create(new Ifengnews());
					spider.addUrl(
							"http://zhannei.baidu.com/cse/search?q="
									+ field1.getText()+"&s=16378496155419916178")
							// http://news.baidu.com/ns?word=机床

							.addPipeline(new ConsolePipeline())
							.addPipeline(new MysqlPipeline()).start();
				} else if (index2 == 2) {
					spider = Spider.create(new Wanfang());
					spider.addUrl(
							"http://s.wanfangdata.com.cn/Paper.aspx?q="
									+ field1.getText())
							// http://news.baidu.com/ns?word=机床

							.addPipeline(new ConsolePipeline())
							.addPipeline(new MysqlPipeline()).start();
				} else if (index2 == 3) {
					spider = Spider.create(new Patent());
					spider.addUrl("http://www.soopat.com/Home/Result?SearchWord="+field1.getText()+"&PatentIndex=0&Sort=1&Valid=2")
							.addPipeline(new ConsolePipeline())
							.addPipeline(new MysqlPipeline()).start();
				}
				

			} catch (Exception e1) {
			}
		}
	}

	public static void main(String args[]) {
		new initdatabase().initialize();// 建立数据库和表

		new GUI();

	}
}
