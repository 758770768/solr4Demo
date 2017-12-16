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
 // ��̬����slorServer����������linux�е�solr
	private static final HttpSolrServer solrServer = new HttpSolrServer("http://192.168.25.128:8081/solr");

	/**
	 * @throws Exception
	 *             ��ѯ������
	 */

	@Test
	public void fn() throws Exception {
   
		 // �������ӳɹ�
		System.out.println(solrServer);
		// ����solr��ѯ����
		SolrQuery query = new SolrQuery();
		// ����ģ����ѯ�ֶ�
		query.set("q", "text_item:��ɫ");
		// query.setFilterQueries("id:[0 TO 10]");
		//����Ĭ�ϲ�ѯ�ֶ�
		query.set("df", "item_title");
		// ��������ʽ
		query.setSort("item_created", ORDER.desc);
		// ���÷�ҳ
		query.setStart(1);
		query.setRows(10);
		//  �򿪸���
		query.setHighlight(true);
//		��Ӹ����ֶ�
		query.addHighlightField("item_title");
//		���ø���ǰ׺
		query.setHighlightSimplePre("<h1 style='color:red'>");
//		���ø�����׺
		query.setHighlightSimplePost("<h1>");
//   ���Ͳ�ѯ
		QueryResponse response = solrServer.query(query);
//		�������ֶκ���ͨ��ѯ�ֶ���Ҫ�ֿ������Ǹ����ֶ�
		Map<String, Map<String, List<String>>> hrs = response.getHighlighting();
//		��ӡ�����ֶ�����
		System.out.println(hrs);
//		��ȡ��ѯ�ĵ�����
		SolrDocumentList rs = response.getResults();
//		��ѭ����Ӧ��ѯ����
		System.out.println("��ѯ��������Ϊ:" + rs.getNumFound());
		for (SolrDocument doc : rs) {

			System.out.println(
					doc.get("id") + "" + doc.get("item_title") + 
//					�����ֶ���Ҫͨ���ַ�����ȡ
					":�����ֶ�:" + hrs.get(doc.get("id")).get("item_title"));

		}

	}

	/**
	 * @throws Exception
	 *            ��ɾ���� ���������
	 */

	@Test
	public void fn1() throws Exception {
//		����id�h�� �ĵ�
		solrServer.deleteById("1");
//		����ĵ��������
		SolrInputDocument solrIn = new SolrInputDocument();
//		�ĵ��������
		solrIn.addField("id", "1");
		solrIn.addField("item_title", "��������ĵ�");
		solrIn.addField("item_sell_point", "����˵ʲô��,���Ѿ�����õ��ĵ���");
		solrIn.addField("item_image", "����,i hava nothing to say");
		solrIn.addField("item_price", "���´���,�־ñغ�,�Ͼñط�");
		solrIn.addField("item_created", "�����֮����,����Ȼ������,�¿� ");
//		���ĵ���ӵ���������
		solrServer.add(solrIn);
//		�ύ
		solrServer.commit();
//		�Ż���ѯ����
		solrServer.optimize();
//		��ӳɹ��ֶ�
		System.out.println("����������");

	}

}
