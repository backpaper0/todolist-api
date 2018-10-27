package com.example.dao;

import java.util.List;

import org.seasar.doma.Dao;
import org.seasar.doma.Delete;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
import org.seasar.doma.Update;
import org.seasar.doma.boot.ConfigAutowireable;
import org.seasar.doma.jdbc.Result;

import com.example.entity.Todo;

@Dao
@ConfigAutowireable
public interface TodoDao {

    @Select
    List<Todo> selectAll();

    @Insert
    Result<Todo> insert(Todo entity);

    @Update(sqlFile = true)
    int updateDoneById(Integer id, boolean done);

    @Delete(sqlFile = true)
    int deleteByDone(boolean done);

    default int deleteDone() {
        return deleteByDone(true);
    }
}
