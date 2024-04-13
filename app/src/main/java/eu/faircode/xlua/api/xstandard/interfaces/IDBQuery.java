package eu.faircode.xlua.api.xstandard.interfaces;

import eu.faircode.xlua.XDatabase;
import eu.faircode.xlua.api.xstandard.database.SqlQuerySnake;

public interface IDBQuery {
    SqlQuerySnake createQuery(XDatabase db);
}
