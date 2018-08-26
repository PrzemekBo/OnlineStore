package com.capgemini;

import com.capgemini.dao.impl.CustomerDaoImplTest;
import com.capgemini.dao.impl.ProductDaoImplTest;
import com.capgemini.dao.impl.TransactionDaoImplTest;
import com.capgemini.service.impl.CustomerServiceImplTest;
import com.capgemini.service.impl.ProductServiceImplTest;
import com.capgemini.service.impl.TransactionServiceImplTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({CustomerServiceImplTest.class, ProductServiceImplTest.class, TransactionServiceImplTest.class
, ProductDaoImplTest.class, CustomerDaoImplTest.class, TransactionDaoImplTest.class})
public class DemoApplicationTests {


}
