package httpProxySrc.ProxyTest;

import java.net.*;
import java.io.*;

public class HttpProxy extends Thread {
	static public int CONNECT_RETRIES = 5;
	static public int CONNECT_PAUSE = 5;
	static public int TIMEOUT = 50;
	static public int BUFSIZ = 1024;
	static public boolean logging = false;
	static public OutputStream log = null;
	// ���������õ�Socket
	protected Socket socket;
	// �ϼ���������������ѡ
	static private String parent = null;
	static private int parentPort = -1;

	static public void setParentProxy(String name, int pport) {
		parent = name;
		parentPort = pport;
	}

	// �ڸ���Socket�ϴ���һ�������̡߳�
	public HttpProxy(Socket s) {
		socket = s;
		start();
	}

	public void writeLog(int c, boolean browser) throws IOException {
		log.write(c);
	}

	public void writeLog(byte[] bytes, int offset, int len, boolean browser)
			throws IOException {
		for (int i = 0; i < len; i++)
			writeLog((int) bytes[offset + i], browser);
	}

	// Ĭ������£���־��Ϣ�����
	// ��׼����豸��
	// ��������Ը�����
	public String processHostName(String url, String host, int port, Socket sock) {
		java.text.DateFormat cal = java.text.DateFormat.getDateTimeInstance();
		System.out.println(cal.format(new java.util.Date()) + " - " + url + " "
				+ sock.getInetAddress() + "<BR>");
		return host;
	}

	public void run() {
		String line;
		String host;
		int port = 80;
		Socket outbound = null;
		try {
			socket.setSoTimeout(TIMEOUT);
			InputStream is = socket.getInputStream();
			OutputStream os = null;
			try {
				// ��ȡ�����е�����
				line = "";
				host = "";
				int state = 0;
				boolean space;
				while (true) {
					int c = is.read();
					if (c == -1)
						break;
					if (logging)
						writeLog(c, true);
					space = Character.isWhitespace((char) c);
					switch (state) {
					case 0:
						if (space)
							continue;
						state = 1;
					case 1:
						if (space) {
							state = 2;
							continue;
						}
						line = line + (char) c;
						break;
					case 2:
						if (space)
							continue; // ��������հ��ַ�
						state = 3;
					case 3:
						if (space) {
							state = 4;
							// ֻ�����������Ʋ���
							String host0 = host;
							int n;
							n = host.indexOf("//");
							if (n != -1)
								host = host.substring(n + 2);
							n = host.indexOf('/');
							if (n != -1)
								host = host.substring(0, n);
							// �������ܴ��ڵĶ˿ں�
							n = host.indexOf(":");
							if (n != -1) {
								port = Integer.parseInt(host.substring(n + 1));
								host = host.substring(0, n);
							}
							host = processHostName(host0, host, port, socket);
							if (parent != null) {
								host = parent;
								port = parentPort;
							}
							int retry = CONNECT_RETRIES;
							while (retry-- != 0) {
								try {
									outbound = new Socket(host, port);
									break;
								} catch (Exception e) {
								}
								// �ȴ�
								Thread.sleep(CONNECT_PAUSE);
							}
							
							if (outbound == null)
							{
								System.out.println("outbound is null");
								break;
							}
							outbound.setSoTimeout(TIMEOUT);
							os = outbound.getOutputStream();
							os.write(line.getBytes());
							os.write(' ');
							os.write(host0.getBytes());
							os.write(' ');
							pipe(is, outbound.getInputStream(), os, socket
									.getOutputStream());
							break;
						}
						host = host + (char) c;
						break;
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
		} finally {
			try {
				socket.close();
			} catch (Exception e1) {
			}
			try {
				outbound.close();
			} catch (Exception e2) {
			}
		}
	}

	static public void main(String args[]) {
		System.out.println("�ڶ˿�808��������������\n");
		HttpProxy.log = System.out;
		HttpProxy.logging = false;
		HttpProxy.startProxy(808, HttpProxy.class);
	}

	static public void startProxy(int port, Class clobj) {
		ServerSocket ssock;
		Socket sock;
		try {
			ssock = new ServerSocket(port);
			while (true) {
				Class[] sarg = new Class[1];
				Object[] arg = new Object[1];
				sarg[0] = Socket.class;
				arg[0] = ssock.accept();
				System.out.println(arg[0]);
				try {
					java.lang.reflect.Constructor cons = clobj
							.getDeclaredConstructor(sarg);
					arg[0] = ssock.accept();
					cons.newInstance(arg); // ����HttpProxy�����������ʵ��
				} catch (Exception e) {
					Socket esock = (Socket) arg[0];
					try {
						esock.close();
					} catch (Exception ec) {
					}
				}
			}
		} catch (IOException e) {
		}
	}

	void pipe(InputStream inputstream, InputStream inputstream1,
			OutputStream outputstream, OutputStream outputstream1)
			throws IOException {
		try {
			byte abyte0[] = new byte[BUFSIZ];
			do {
				int i;
				try {
					if ((i = inputstream.read(abyte0)) > 0) {
						outputstream.write(abyte0, 0, i);
						if (logging)
							writeLog(abyte0, 0, i, true);
					} else if (i < 0)
						break;
				} catch (InterruptedIOException interruptedioexception) {
				}
				try {
					int j;
					if ((j = inputstream1.read(abyte0)) > 0) {
						outputstream1.write(abyte0, 0, j);
						if (logging)
							writeLog(abyte0, 0, j, false);
						continue;
					}
					if (j < 0)
						break;
				} catch (InterruptedIOException interruptedioexception1) {
				}
			} while (true);
		} catch (Exception exception) {
			System.out.println("Pipe�쳣: " + exception);
		}
	}

}