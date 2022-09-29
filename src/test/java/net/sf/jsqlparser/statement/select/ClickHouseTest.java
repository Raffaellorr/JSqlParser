package net.sf.jsqlparser.statement.select;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.parser.SimpleNode;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import org.junit.jupiter.api.Test;

public class ClickHouseTest {
    @Test
    void addPlainUpdatedAt() throws JSQLParserException {
        String sql = "select count() from t1";
        Statement parsed = CCJSqlParserUtil.parse(sql);
        Select select = (Select) parsed;
        PlainSelect selectBody = (PlainSelect) select.getSelectBody();
        selectBody.addSelectItems(new SelectExpressionItem(new Column().withColumnName("updated_at")).withAlias(new Alias("updated_at")));
        System.out.println(select);
    }

    @Test
    void addComplexUpdatedAt() throws JSQLParserException {
        String sql = "select toDateTime(arrayMax(x -> toUInt64(x), [e.updated_at, c.updated_at])) from t1";
        Select select = (Select) CCJSqlParserUtil.parse(sql);
        System.out.println(select);
    }

    @Test
    void unionAll() throws JSQLParserException {
        String sql = "select * from t1 union all select * from t2";
        Select select = (Select) CCJSqlParserUtil.parse(sql);
        SetOperationList selectBody = (SetOperationList) select.getSelectBody();
        for (SelectBody body : selectBody.getSelects()) {
            ((PlainSelect) body).addSelectItems(new SelectExpressionItem(new Column().withColumnName("updated_at")).withAlias(new Alias("updated_at")));
        }
        System.out.println(select);
    }


    @Test
    void addTableSource() throws JSQLParserException {
        String sql = "select * from t1";
        Select select = (Select) CCJSqlParserUtil.parse(sql);
        changeTableSource((PlainSelect) select.getSelectBody());
        System.out.println(select);
    }

    @Test
    void addNestedTableSource() throws JSQLParserException {
        String sql = "select * from (select * from t1)";
        Select select = (Select) CCJSqlParserUtil.parse(sql);
        PlainSelect selectBody = (PlainSelect) select.getSelectBody();
        SubSelect fromItem = (SubSelect) selectBody.getFromItem();
        changeTableSource((PlainSelect) fromItem.getSelectBody());
        System.out.println(select);
    }

    void changeTableSource(PlainSelect selectBody) {
        selectBody.setFromItem(new Table("{{ source('db'), 'table' }}"));
    }

    @Test
    void array() throws JSQLParserException {
        String sql = "select func([1,2,c]) from t1";
        Statement parsed = CCJSqlParserUtil.parse(sql);
        System.out.println(parsed);
    }

    @Test
    void lambda() throws JSQLParserException {
        String sql = "select count(c -> toUInt64(d)), c from t1";
        SimpleNode node = (SimpleNode) CCJSqlParserUtil.parseAST(sql);
        node.dump("*");
        Statement parsed = CCJSqlParserUtil.parse(sql);
        System.out.println(parsed);
    }



    @Test
    void createView() throws JSQLParserException {
        String sql = "CREATE TABLE sap_o2c.v_late_gi_el\n" +
                "(\n" +
                "\n" +
                "    `caseid` String,\n" +
                "\n" +
                "    `docid` String,\n" +
                "\n" +
                "    `act_docid` String,\n" +
                "\n" +
                "    `act_docitem` String,\n" +
                "\n" +
                "    `activity` String,\n" +
                "\n" +
                "    `actime` DateTime('UTC'),\n" +
                "\n" +
                "    `user_name` String\n" +
                ") AS\n" +
                "SELECT\n" +
                "    elbase.caseid AS caseid,\n" +
                "\n" +
                "    elbase.docid AS docid,\n" +
                "\n" +
                "    mm.mblnr AS act_docid,\n" +
                "\n" +
                "    concat(mm.mblnr,\n" +
                " toString(mm.mjahr),\n" +
                " toString(mm.zeile)) AS act_docitem,\n" +
                "\n" +
                "    if((mm.cpudt > dn.wadat) AND (mm.shkzg = 'H'),\n" +
                " 'delivery due date passed',\n" +
                " 'delivery on time') AS activity,\n" +
                "\n" +
                "    toDateTime(concat(toString(dn.wadat),\n" +
                " ' ',\n" +
                " '23:59:59'),\n" +
                " 'UTC') AS actime,\n" +
                "\n" +
                "    mm.user_name AS user_name\n" +
                "FROM sap_o2c.elbase AS el\n" +
                "INNER JOIN sap_o2c.v_delivery AS dn ON el.docid = concat(leftPad(dn.mandt,\n" +
                " 3,\n" +
                " '0'),\n" +
                " leftPad(dn.vbeln,\n" +
                " 10,\n" +
                " '0'),\n" +
                " leftPad(dn.posnr,\n" +
                " 6,\n" +
                " '0'))\n" +
                "INNER JOIN sap_o2c.v_material AS mm ON (mm.vbeln_im = leftPad(dn.vbeln,\n" +
                " 10,\n" +
                " '0')) AND (toString(mm.vbelp_im) = leftPad(dn.posnr,\n" +
                " 6,\n" +
                " '0'))\n" +
                "WHERE mm.shkzg = 'H';";
        Statement parsed = CCJSqlParserUtil.parse(sql);
        System.out.println(parsed);
    }

}
