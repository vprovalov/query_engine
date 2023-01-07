package dataframe;

import com.opencsv.exceptions.CsvValidationException;
import datasource.CsvDataSource;
import datasource.ParquetDataSource;
import exceptions.QueryException;
import logicalplan.Column;
import logicalplan.LogicalExpr;
import logicalplan.Scan;
import logicalplan.aggregate.*;
import logicalplan.literal.*;
import physicalplan.expression.aggregate.MaxExpression;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

public class ExecutionContext {
    public DataFrame csv(final Path filename) {
        try {
            return new DataFrameImpl(new Scan(
                    filename.getFileName().toString(),
                    new CsvDataSource(filename, Optional.empty(), true, 124),
                    new ArrayList<>()));
        } catch (IOException ex) {
            throw new QueryException("Error opening csv", ex);
        } catch (CsvValidationException ex) {
            throw new QueryException("Error opening csv", ex);
        }
    }

    public DataFrame parquet(final Path filepath) {
        return new DataFrameImpl(new Scan(
                filepath.getFileName().toString(),
                new ParquetDataSource(filepath, 128),
                new ArrayList<>()));
    }

    public static Column col(final String name) {
        return new Column(name);
    }

    public static LiteralString lit(final String value) {
        return new LiteralString(value);
    }

    public static LiteralInt lit(final int value) {
        return new LiteralInt(value);
    }

    public static LiteralLong lit(final long value) {
        return new LiteralLong(value);
    }

    public static LiteralBoolean lit(final boolean value) {
        return new LiteralBoolean(value);
    }

    public static LiteralFloat lit(final float value) {
        return new LiteralFloat(value);
    }

    public static LiteralDouble lit(final double value) {
        return new LiteralDouble(value);
    }

    public static LiteralDateTime lit(final Date value) {
        return new LiteralDateTime(value);
    }

    public static Max max(final LogicalExpr expr) { return new Max(expr); }
    public static Min min(final LogicalExpr expr) { return new Min(expr); }
    public static Sum sum(final LogicalExpr expr) { return new Sum(expr); }
    public static Count count(final LogicalExpr expr) { return new Count(expr); }
    public static Average avg(final LogicalExpr expr) { return new Average(expr); }
}
