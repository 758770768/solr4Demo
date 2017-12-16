package org.solrDemo;

import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

public class SolrDemo {
 // 静态最终slorServer服务器连接linux中的solr
	private static final HttpSolrServer solrServer = new HttpSolrServer("http://192.168.25.128:8081/solr");

	/**
	 * @throws Exception
	 *             查询索引库
	 */

	@Test
	public void fn() throws Exception {
   
		 // 测试连接成功
		System.out.println(solrServer);
		// 创建solr查询对象
		SolrQuery query = new SolrQuery();
		// 设置模糊查询字段
		query.set("q", "text_item:白色");
		// query.setFilterQueries("id:[0 TO 10]");
		//设置默认查询字段
		query.set("df", "item_title");
		// 设置排序方式
		query.setSort("item_created", ORDER.desc);
		// 设置分页
		query.setStart(1);
		query.setRows(10);
		//  打开高亮
		query.setHighlight(true);
//		添加高亮字段
		query.addHighlightField("item_title");
//		设置高亮前缀
		query.setHighlightSimplePre("<h1 style='color:red'>");
//		设置高亮后缀
		query.setHighlightSimplePost("<h1>");
//   发送查询
		QueryResponse response = solrServer.query(query);
//		高亮的字段和普通查询字段需要分开下面是高亮字段
		Map<String, Map<String, List<String>>> hrs = response.getHighlighting();
//		打印高亮字段内容
		System.out.println(hrs);
//		获取查询文档内容
		SolrDocumentList rs = response.getResults();
//		简单循环答应查询数据
		System.out.println("查询的数据量为:" + rs.getNumFound());
		for (SolrDocument doc : rs) {

			System.out.println(
					doc.get("id") + "" + doc.get("item_title") + 
//					高亮字段需要通此种方法获取
					":高亮字段:" + hrs.get(doc.get("id")).get("item_title"));

		}

	}

	/**
	 * @throws Exception
	 *            先删除再 添加索引库
	 */

	@Test
	public void fn1() throws Exception {
//		依據id刪除 文档
		solrServer.deleteById("1");
//		获得文档输入对象
		SolrInputDocument solrIn = new SolrInputDocument();
//		文档中添加域
		solrIn.addField("id", "1");
		solrIn.addField("item_title", "添加索引文档");
		solrIn.addField("item_sell_point", "我能说什么呢,这已经是最好的文档了");
		solrIn.addField("item_image", "哈哈,i hava nothing to say");
		solrIn.addField("item_price", "天下大势,分久必合,合久必分");
		solrIn.addField("item_created", "念天地之悠悠,独怆然而涕下,陈奎 ");
//		将文档添加到服务其中
		solrServer.add(solrIn);
//		提交
		solrServer.commit();
//		优化查询索引
		solrServer.optimize();
//		添加成功字段
		System.out.println("添加索引完成");

	}

}
