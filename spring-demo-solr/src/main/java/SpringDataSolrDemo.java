import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.Query;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.core.query.SolrDataQuery;
import org.springframework.data.solr.core.query.result.ScoredPage;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

//添加 修改 删除 查询
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext-solr.xml"})
public class SpringDataSolrDemo {
    @Autowired
    private SolrTemplate solrTemplate;

    //springdate添加  只要id不变就是修改
    @Test
    public void testAdd()throws Exception{
        Item item=new Item();
        item.setId(1L);
        item.setBrand("华为");
        item.setCategory("手机");
        item.setGoodsId(1L);
        item.setSeller("华为2号专卖店");
        item.setTitle("华为Mate9");
        item.setPrice(new BigDecimal(2000));
        solrTemplate.saveBean(item,1000);//自动提交
        //solrTemplate.commit();手动提交
    }
    //根据id查询
    @Test
    public void testFindById()throws Exception{
        Item item = solrTemplate.getById("1", Item.class);
        System.out.println(item);
    }
    //根据id删除
    @Test
    public void testDeleteId()throws Exception{
        solrTemplate.deleteById("1");
        solrTemplate.commit();
    }
    @Test
    public void testAddList(){
        List<Item> list=new ArrayList<Item>();
        for(int i=0;i<100;i++){
            Item item=new Item();
            item.setId(i+1L);
            item.setBrand("华为");
            item.setCategory("手机");
            item.setGoodsId(1L);
            item.setSeller("华为2号专卖店");
            item.setTitle("华为Mate"+i);
            item.setPrice(new BigDecimal(2000+i));
            list.add(item);
        }
        solrTemplate.saveBeans(list);
        solrTemplate.commit();
    }
    //分页查询
    @Test
    public void testFindByPage(){
        Query query = new SimpleQuery("*:*");
        //分页 开始行
        query.setOffset(0);
        //每页数
        query.setRows(10);
        //查询
        ScoredPage<Item> page = solrTemplate.queryForPage(query, Item.class);
        //总条数
        long totalElements = page.getTotalElements();
        System.out.println("总条数:"+totalElements);
        //总页数
        int totalPages = page.getTotalPages();
        System.out.println("总页数:"+totalPages);
        //结果集
        List<Item> itemList = page.getContent();
        for (Item item : itemList) {
            System.out.println(item);
        }
    }
    //根据条件查询
    @Test
    public void testFindByPageQuery(){
        Criteria criteria = new Criteria("item_title").contains("0");//多条件 可再多加
        Query query = new SimpleQuery(criteria);//精准查询 只能查询一个条件
        //分页 开始行
        query.setOffset(0);
        //每页数
        query.setRows(10);
        //查询
        ScoredPage<Item> page = solrTemplate.queryForPage(query, Item.class);
        //总条数
        long totalElements = page.getTotalElements();
        System.out.println("总条数:"+totalElements);
        //总页数
        int totalPages = page.getTotalPages();
        System.out.println("总页数:"+totalPages);
        //结果集
        List<Item> itemList = page.getContent();
        for (Item item : itemList) {
            System.out.println(item);
        }
    }
    //根据条件删除 全删除
    @Test
    public void testDeleteByQuery(){
        //Criteria criteria = new Criteria("item_title").contains("0");
        SolrDataQuery query = new SimpleQuery("*:*");
        solrTemplate.delete(query);
        solrTemplate.commit();
    }




}
