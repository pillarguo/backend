package cmdc.db;

import java.io.IOException;
import java.io.Reader;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.io.Resources;

/**
 * @author GLZ
 *	测试的，没用到
 */
public class DBAccess {
	public SqlSession getSqlSession() throws IOException{
		Reader reader=Resources.getResourceAsReader("cmdc/config/Configuration.xml");
		
		SqlSessionFactory sqlSessionFactory=new SqlSessionFactoryBuilder().build(reader);
		
		SqlSession sqlSession=sqlSessionFactory.openSession();
		
		return sqlSession;
	}
}
