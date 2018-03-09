package thrift;

import org.apache.thrift.TException;

/**
 * @author zenngwei
 * @date 2018/03/02 16:47
 */
//public class HelloWorldImpl implements HelloWorldService.Iface {
public class HelloWorldImpl{

    public HelloWorldImpl() {
    }

//    @Override
    public String sayHello(String username) throws TException {
        return "Hi," + username + " welcome to thrift demo world";
    }
}