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

	private static final HttpSolrServer solrServer = new HttpSolrServer("http://192.168.25.128:8081/solr");

	/**
	 * @throws Exception
	 *             查询索引库
	 */

	@Test
	public void fn() throws Exception {

		System.out.println(solrServer);
		SolrQuery query = new SolrQuery();
		query.set("q", "text_item:白色");
		// query.setFilterQueries("id:[0 TO 10]");
		query.set("df", "item_title");
		query.setSort("item_created", ORDER.desc);
		query.setStart(1);
		query.setRows(10);
		query.setHighlight(true);
		query.addHighlightField("item_title");
		query.setHighlightSimplePre("<h1 style='color:red'>");
		query.setHighlightSimplePost("<h1>");

		QueryResponse response = solrServer.query(query);
		Map<String, Map<String, List<String>>> hrs = response.getHighlighting();
		System.out.println(hrs);
		SolrDocumentList rs = response.getResults();
		System.out.println("查询的数据量为:" + rs.getNumFound());
		for (SolrDocument doc : rs) {

			System.out.println(
					doc.get("id") + "" + doc.get("item_title") + ":高亮字段:" + hrs.get(doc.get("id")).get("item_title"));

		}

	}

	/**
	 * @throws Exception
	 *            先删除再 添加索引库
	 */

	@Test
	public void fn1() throws Exception {
		solrServer.deleteById("1");
		SolrInputDocument solrIn = new SolrInputDocument();
		solrIn.addField("id", "1");
		solrIn.addField("item_title", "添加索引文档");
		solrIn.addField("item_sell_point", "我能说什么呢,这已经是最好的文档了");
		solrIn.addField("item_image", "哈哈,i hava nothing to say");
		solrIn.addField("item_price", "天下大势,分久必合,合久必分");
		solrIn.addField("item_created", "念天地之悠悠,独怆然而涕下,陈奎 ");
		solrServer.add(solrIn);
		solrServer.commit();
		solrServer.optimize();
		System.out.println("添加索引完成");

	}

}
