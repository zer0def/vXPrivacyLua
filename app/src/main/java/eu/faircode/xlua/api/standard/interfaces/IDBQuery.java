package eu.faircode.xlua.api.standard.interfaces;

import eu.faircode.xlua.XDatabase;
import eu.faircode.xlua.api.standard.database.SqlQuerySnake;

public interface IDBQuery {
    SqlQuerySnake createQuery(XDatabase db);
}
