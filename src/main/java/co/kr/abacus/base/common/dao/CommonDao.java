package co.kr.abacus.base.common.dao;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CommonDao {

    private final SqlSessionTemplate sqlSession;

    public CommonDao(SqlSessionTemplate sqlSession) {
        this.sqlSession = sqlSession;
    }

    public Object selectOne(String queryId) {
        return sqlSession.selectOne(queryId);
    }

    public Object selectOne(String queryId, Object param) {
        return sqlSession.selectOne(queryId, param);
    }

    public <E> List<E> selectList(String queryId) {
        return sqlSession.selectList(queryId);
    }

    public <E> List<E> selectList(String queryId, Object param) {
        return sqlSession.selectList(queryId, param);
    }

    public int insert(String queryId) {
        return sqlSession.insert(queryId);
    }

    public int insert(String queryId, Object param) {
        return sqlSession.insert(queryId, param);
    }

    public int update(String queryId) {
        return sqlSession.update(queryId);
    }

    public int update(String queryId, Object param) {
        return sqlSession.update(queryId, param);
    }

    public int delete(String queryId) {
        return sqlSession.delete(queryId);
    }

    public int delete(String queryId, Object param) {
        return sqlSession.delete(queryId, param);
    }
}
