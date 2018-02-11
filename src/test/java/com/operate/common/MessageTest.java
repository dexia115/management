package com.operate.common;

import java.io.IOException;
import java.util.Properties;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import com.operate.tools.ZooKeeperOperator;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = MessageTest.class)
public class MessageTest {

	@Test
	public void test() {
		try {
			ZooKeeperOperator zkoperator = new ZooKeeperOperator();
			zkoperator.connect("127.0.0.1:2181");

			String zktest = "ZooKeeper的Java API测试";
//			zkoperator.create("/root/child3", zktest.getBytes());

			System.out.println("获取设置的信息：" + new String(zkoperator.getData("/")));

			System.out.println("节点孩子信息:");
//			zkoperator.getChild("/root");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
