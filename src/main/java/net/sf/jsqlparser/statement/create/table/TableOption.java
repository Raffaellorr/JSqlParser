package net.sf.jsqlparser.statement.create.table;

import net.sf.jsqlparser.expression.PartitionByClause;
import net.sf.jsqlparser.statement.select.OrderByElement;

public class TableOption {
    private Engine engine;
    private OrderByElement orderBy;
    private PartitionByClause partitionBy;
    private PrimaryKey primaryKey;

    public TableOption() {
    }

    public Engine getEngine() {
        return engine;
    }

    public void setEngine(Engine engine) {
        this.engine = engine;
    }

    public OrderByElement getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(OrderByElement orderBy) {
        this.orderBy = orderBy;
    }

    public PartitionByClause getPartitionBy() {
        return partitionBy;
    }

    public void setPartitionBy(PartitionByClause partitionBy) {
        this.partitionBy = partitionBy;
    }

    public PrimaryKey getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(PrimaryKey primaryKey) {
        this.primaryKey = primaryKey;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ENGINE = ").append(engine.getName()).append(" ");
        if (partitionBy != null) {
            partitionBy.toStringPartitionBy(sb);
        }
        if (primaryKey != null) {
            primaryKey.toStringPrimaryKey(sb);
        }
        sb.append("ORDER BY ").append(orderBy.toString());
        return sb.toString();
    }
}
