package com.edmProxy.util.proxy.check;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.net.SocketFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.net.SocketFactory;

/**
 * Socket factory for Socks5 proxy
 * 
 * @author Atul Aggarwal
 */
public class Socks5ProxySocketFactory extends SocketFactory
{
	
	public static void main(String[] args) throws IOException {
		ProxyInfo proxy=new ProxyInfo();
		proxy.setProxyAddress("107.149.216.10");
		proxy.setProxyPort(1080);
		proxy.setProxyUsername("User-001");
		proxy.setProxyPassword("123456");
		Socks5ProxySocketFactory Socks5ProxySocketFactory=new Socks5ProxySocketFactory(proxy);
		Socks5ProxySocketFactory.socks5ProxifiedSocket(proxy.getProxyAddress(),proxy.getProxyPort());
	}
	
	
    private ProxyInfo proxy;
    
    public Socks5ProxySocketFactory(ProxyInfo proxy)
    {
        this.proxy = proxy;
    }

    public Socket createSocket(String host, int port) 
        throws IOException, UnknownHostException
    {
        return socks5ProxifiedSocket(host,port);
    }

    public Socket createSocket(String host ,int port, InetAddress localHost,
                                int localPort)
        throws IOException, UnknownHostException
    {
        
        return socks5ProxifiedSocket(host,port);
        
    }

    public Socket createSocket(InetAddress host, int port)
        throws IOException
    {
        
        return socks5ProxifiedSocket(host.getHostAddress(),port);
        
    }

    public Socket createSocket( InetAddress address, int port, 
                                InetAddress localAddress, int localPort) 
        throws IOException
    {
        
        return socks5ProxifiedSocket(address.getHostAddress(),port);
        
    }
    
    private Socket socks5ProxifiedSocket(String host, int port) 
        throws IOException
    {
        Socket socket = null;
        InputStream in = null;
        OutputStream out = null;
        String proxy_host = proxy.getProxyAddress();
        int proxy_port = proxy.getProxyPort();
        String user = proxy.getProxyUsername();
        String passwd = proxy.getProxyPassword();
        
        try
        {
            socket=new Socket(proxy_host, proxy_port); 

           // System.out.println(socket.getSoTimeout());
            in=socket.getInputStream();
            out=socket.getOutputStream();

            socket.setTcpNoDelay(true);

            byte[] buf=new byte[1024];
            int index=0;

/*
                   +-----+----------------+---------------+
                   |VER | NMETHODS | METHODS  |
                   +-----+----------------+---------------+
                   |   1   |          1         |    1 to 255   |
                   +-----+----------------+---------------+

   The VER field is set to X'05' for this version of the protocol.  The
   NMETHODS field contains the number of method identifier octets that
   appear in the METHODS field.
   
   VER(版本)在这个协议版本中被设置为X'05'。NMETHODS(方法选择)中包含了在METHODS(方法)中出现的方法标识 八位组的数目。


   The values currently defined for METHOD are:

          o  X'00' NO AUTHENTICATION REQUIRED
          o  X'01' GSSAPI
          o  X'02' USERNAME/PASSWORD
          o  X'03' to X'7F' IANA ASSIGNED
          o  X'80' to X'FE' RESERVED FOR PRIVATE METHODS
          o  X'FF' NO ACCEPTABLE METHODS
          
          
          当前被定义的METHOD的值有： 
　　>> X'00' 无验证需求 
　　>> X'01' 通用安全服务应用程序接口(GSSAPI) 
　　>> X'02' 用户名/密码(USERNAME/PASSWORD) 
　　>> X'03' 至 X'7F' IANA 分配(IANA ASSIGNED) 
　　>> X'80' 至 X'FE' 私人方法保留(RESERVED FOR PRIVATE METHODS) 
　　>> X'FF' 无可接受方法(NO ACCEPTABLE METHODS) 
    ***IANA是负责全球INTERNET上的IP地址进行编号分配的机构(译者著)***

*/

            buf[index++]=5;

            buf[index++]=2;//表示METHODS 字段中有两个字节的标识
            buf[index++]=0;           // NO AUTHENTICATION REQUIRED
            buf[index++]=2;           // USERNAME/PASSWORD

            out.write(buf, 0, index);

/*
    The server selects from one of the methods given in METHODS, and
    sends a METHOD selection message:
    
    服务器从METHODS给出的方法中选出一种，发送一个METHOD selection(方法选择)报文：

                         +-----+------------+
                         |VER | METHOD |
                         +-----+------------+
                         |    1  |       1        |
                         +-----+------------+
*/
      //in.read(buf, 0, 2);
            fill(in, buf, 2);

            boolean check=false;
            switch((buf[1])&0xff)
            {
                case 0:                // NO AUTHENTICATION REQUIRED  如果服务器选择了“无验证需求”，那么直接返回
                    check=true;
                    break;
                case 2:                // USERNAME/PASSWORD  如果服务器选择了“用户名/密码”，那么使用“用户名/密码”进行子协商
                    if(user==null || passwd==null)
                    {
                        break;
                    }

/*
 * 然后客户和服务器进入由选定认证方法所决定的子协商过程(sub-negotiation)(上面case 2:已经选择了“用户名/密码”子协商)。各种不同的方法的子协商过程的描述请参考各自的备忘录。
 * 
   Once the SOCKS V5 server has started, and the client has selected the
   Username/Password Authentication protocol, the Username/Password
   subnegotiation begins.  This begins with the client producing a
   Username/Password request:
   
   一旦SOCKS V5服务器运行，并且客户端选择了用户名/密码认证协议以后，就开始了用户名/密码协议的子协商过程。客户端先产生一个用户名/密码协议的请求：


           +-----+--------+------------+--------+--------------+
           |VER | ULEN |  UNAME   | PLEN |  PASSWD  |
           +-----+--------+------------+--------+--------------+
           |   1   |    1     |  1 to 255  |   1     |   1 to 255   |
           +-----+--------+------------+--------+--------------+

   The VER field contains the current version of the subnegotiation,
   which is X'01'. The ULEN field contains the length of the UNAME field
   that follows. The UNAME field contains the username as known to the
   source operating system. The PLEN field contains the length of the
   PASSWD field that follows. The PASSWD field contains the password
   association with the given UNAME.
   
   VER中指明了子协商的当前版本，现在使用的是X’01’。ULEN域中包含了下一个UNAME域的长度。
   UNAME中包含一个源操作系统(source operating system)所知道的用户名。
   PLEN中指明了紧随其后的PASSWD的长度。PASSWD中则包含了对应UNAME用户的密码。

*/
                    index=0;
                    buf[index++]=1;//设置VER 为 X'01'
                    buf[index++]=(byte)(user.length());//设置ULEN的值
                    System.arraycopy(user.getBytes(), 0, buf, index, 
                        user.length());//设置UNAME的值
                    index+=user.length();//移动数组下标
                    buf[index++]=(byte)(passwd.length());//设置PLEN的值
                    System.arraycopy(passwd.getBytes(), 0, buf, index, 
                        passwd.length());//设置PASSWD的值
                    index+=passwd.length();//移动数组下标

                    out.write(buf, 0, index);

/*
   The server verifies the supplied UNAME and PASSWD, and sends the
   following response:
   
   服务器验证用户名和密码，并且返回：

                        +-----+------------+
                        |VER | STATUS  |
                        +-----+------------+
                        |   1   |      1        |
                        +-----+------------+

   A STATUS field of X'00' indicates success. If the server returns a
   `failure' (STATUS value other than X'00') status, it MUST close the
   connection.
   
   一个X‘00’的状态域意味着成功。如果服务器返回一个“failure”状态(X'00'以外的状态值)，必须关闭连接。
  **** 如果STATUS中返回X’00’则说明通过验证。如果服务器返回非X’00’则说明验证失败，并且关闭连接。****
*/
                    //in.read(buf, 0, 2);
                    fill(in, buf, 2);
                    if(buf[1]==0)
                    {
                        check=true;
                    }
                    break;
                default:
            }

            //如果服务器返回失败状态，则关闭连接
            if(!check)
            {
                try
                {
                    socket.close();
                }
                catch(Exception eee)
                {
                }
                //throw new ProxyException(ProxyInfo.ProxyType.SOCKS5,
                    //"fail in SOCKS5 proxy");
            }

/*
 * 如果返回成功，则进一步细节的请求
 * 
      The SOCKS request is formed as follows:      
      SOCKS请求如下表所示：

        +-----+-------+--------+-------+---------------+---------------+
        |VER | CMD |  RSV  | ATYP | DST.ADDR | DST.PORT |
        +-----+-------+--------+-------+---------------+---------------+
        |   1   |    1    |  X'00'  |    1    |   Variable    |         2         |
        +-----+-------+--------+-------+---------------+---------------+

      Where:

      o  VER    protocol version: X'05'  VER 协议版本: X’05’
      o  CMD
         o  CONNECT X'01'
         o  BIND X'02'
         o  UDP ASSOCIATE X'03'
      o  RSV    RESERVED  保留
      o  ATYP   address type of following address  后面的地址类型
         o  IP V4 address: X'01'
         o  DOMAINNAME: X'03'  域名：X’03’
         o  IP V6 address: X'04'
      o  DST.ADDR       desired destination address  目的地址
      o  DST.PORT desired destination port in network octet order  以网络字节顺序出现的目的地端口号
         
         ATYP字段中描述了地址字段(DST.ADDR，BND.ADDR)所包含的地址类型：
            · X'01'   基于IPV4的IP地址，4个字节长
            · X'03'   基于域名的地址，地址字段中的第一字节是以字节为单位的该域名的长度，没有结尾的NUL字节。
            · X'04'   基于IPV6的IP地址，16个字节长
*/
     
            index=0;
            buf[index++]=5;
            buf[index++]=1;       // CONNECT
            buf[index++]=0;

            byte[] hostb=host.getBytes();
            int len=hostb.length;
            buf[index++]=3;      // DOMAINNAME 标识该地址使用“基于域名的地址” X'03'
            buf[index++]=(byte)(len);//地址字段中的第一字节是该地址域名的长度，设置第一个字节的值
            System.arraycopy(hostb, 0, buf, index, len);//地址字段的值
            index+=len;
            buf[index++]=(byte)(port>>>8);//“无符号”右移位运算符（>>>），它使用了“零扩展”：无论正负，都在高位插入0，将port转化为32位，然后右移8位，然后转化为字节（8位），结果就是取出port的高24位的低8位的字节
            buf[index++]=(byte)(port&0xff);//将那个int的port转化成十六位的byte运算，然后提取0xff位的值（最低一个字节）0xff 的二进制表示为11111111，结果是取出port的低8位的字节

            out.write(buf, 0, index);

/*
   The SOCKS request information is sent by the client as soon as it has
   established a connection to the SOCKS server, and completed the
   authentication negotiations.  The server evaluates the request, and
   returns a reply formed as follows:
   
   一旦建立了一个到SOCKS服务器的连接，并且完成了认证方式的协商过程，客户机将会发送一个SOCKS请求信息给服务器。服务器将会评估该请求，以如下格式返回答复

        +-----+------+--------+--------+---------------+--------------+
        |VER | REP |  RSV   | ATYP | BND.ADDR | BND.PORT |
        +-----+------+--------+--------+---------------+--------------+
        |   1   |    1   |  X'00'  |     1    |   Variable    |       2          |
        +-----+------+--------+--------+---------------+--------------+

   Where:

   o  VER    protocol version: X'05'
   o  REP    Reply field:
      o  X'00' succeeded
      o  X'01' general SOCKS server failure
      o  X'02' connection not allowed by ruleset
      o  X'03' Network unreachable
      o  X'04' Host unreachable
      o  X'05' Connection refused
      o  X'06' TTL expired
      o  X'07' Command not supported
      o  X'08' Address type not supported
      o  X'09' to X'FF' unassigned
   o  RSV    RESERVED
   o  ATYP   address type of following address
      o  IP V4 address: X'01'
      o  DOMAINNAME: X'03'
      o  IP V6 address: X'04'
   o  BND.ADDR       server bound address
   o  BND.PORT       server bound port in network octet order
*/

      //in.read(buf, 0, 4);
            fill(in, buf, 4);//从buf的[0]位置填充4长度的字节给buf

            //如果不成功
            if(buf[1]!=0)
            {
                try
                {
                    socket.close();
                }
                catch(Exception eee)
                {
                }
//                throw new ProxyException(ProxyInfo.ProxyType.SOCKS5, 
//                    "server returns "+buf[1]);
            }

            //如果应答成功，则进一步查看地址类型,buf[3]便是ATYP
            switch(buf[3]&0xff)
            {
                case 1:
                    //in.read(buf, 0, 6);
                 //如果是IP V4 address: X'01'，则继续重输入流当前位置(已经是4)开始，
                 //重新填充6个长度的字节，读取到的字节便是BND.ADDR和BND.PORT，注意IP V4 中，BND.ADDR的长度是4                 
                    fill(in, buf, 6);
                    break;
                case 3:
                    //in.read(buf, 0, 1);
                 //如果是DOMAINNAME: X'03'，则继续重输入流当前位置(已经是4)开始，
                 //重新填充若干个字节，读取到的字节便是BND.ADDR和BND.PORT，注意DOMAINNAME: X'03'中，
                 //地址BND.ADDR字段中的第一字节是该地址域名的长度
                    fill(in, buf, 1);//由于地址BND.ADDR字段中的第一字节是该地址域名的长度，所以把该地址域的长度读取出来并保存在buf[0]中
                    //in.read(buf, 0, buf[0]+2);
                    fill(in, buf, (buf[0]&0xff)+2);//继续读取剩余字节，(buf[0]&0xff)+2  为BND.ADDR 的剩余字节加上BND.PORT的字节
                    break;
                case 4:
                    //in.read(buf, 0, 18);
                 //如果是IP V6 address: X'04'，则继续重输入流当前位置(已经是4)开始，
                 //重新填充18个长度的字节，读取到的字节便是BND.ADDR和BND.PORT，注意IP V6 中，BND.ADDR的长度是16，加上后面的BND.PORT 2个字节等于18个字节
                    fill(in, buf, 18);//
                    break;
                default:
            }
            return socket;
            
        }
        catch(RuntimeException e)
        {
            throw e;
        }
        catch(Exception e)
        {
            try
            {
                if(socket!=null)
                {
                    socket.close(); 
                }
            }
            catch(Exception eee)
            {
            }
            String message="ProxySOCKS5: "+e.toString();
            if(e instanceof Throwable)
            {
//                throw new ProxyException(ProxyInfo.ProxyType.SOCKS5,message, 
//                    (Throwable)e);
            }
            throw new IOException(message);
        }
    }
    
    private void fill(InputStream in, byte[] buf, int len) 
      throws IOException
    {
        int s=0;
        while(s<len)
        {
         //试图读取len-s个字节到buf中，但可能只能读取到i字节(i < len-s),
         //当 i < len-s 时候，需要循环读取完len-s个字节给buf，那么下次读取给buf时候，插入字节的位置就是s+i
            int i=in.read(buf, s, len-s);
            if(i<=0)
            {
//                throw new ProxyException(ProxyInfo.ProxyType.SOCKS5, "stream " +
//                    "is closed");
            }
            s+=i;
        }
    }
}

